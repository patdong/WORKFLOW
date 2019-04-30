package cn.ideal.wf.jdbc.dao;
/**
 * 持久层采用spring jdbc方式操作MySQL数据库
 * @author 郭佟燕
 * @version 2.0
 */
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.ideal.wf.data.analyzer.Storage;
import cn.ideal.wf.model.WorkflowTableBrief;
import cn.ideal.wf.model.WorkflowUser;
import cn.ideal.wf.service.WorkflowTableService;
import cn.ideal.wf.service.WorkflowWFService;


@Service("WFORACLEExecutor")
public class WFOracleExecutor implements SQLExecutor {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private WorkflowWFService wfService;	
	@Autowired
	private WorkflowTableService wftableService;
	
	@Override
	public Map<String,Object> save(Storage storage) {
		//判断执行sql的表是否存在
		StringBuilder prebuf = new StringBuilder();	
		StringBuilder sufbuf = new StringBuilder();	
		BigDecimal bizId = null;
		try{	
			Map<String,Object> maxId = jdbcTemplate.queryForMap("select sq_"+storage.getTableName()+".nextval  as ID from dual " );
			if(maxId != null){
				bizId = (BigDecimal)maxId.get("ID");
			}
			
			//保存业务主表
			prebuf.append("insert into "+storage.getTableName() +"(ID,");
			sufbuf.append(" values ( "+bizId+",");
			for(String field : storage.getFields().keySet()){					
				prebuf.append(field);
				prebuf.append(",");
				//TO-DO:根据不同的字段类型做特殊处理
				sufbuf.append("'"+storage.getFields().get(field)+"'");
				sufbuf.append(",");
			}
			prebuf.setLength(prebuf.length()-1);
			sufbuf.setLength(sufbuf.length()-1);
			prebuf.append(")");
			sufbuf.append(")");

			int idx = jdbcTemplate.update(prebuf.toString() + sufbuf.toString());
			if(idx > 0){				
				//保存业务关联主表
				prebuf.setLength(0);				
				WorkflowTableBrief tb = wftableService.find(storage.getTbId()); //TableBriefCache.getValue(storage.getTbId());
				prebuf.append("insert into table_summary ( ");
				prebuf.append("bizId,tbId,tableName,wfId,title,createdUserId,createdUserName,createdOrgId,createdOrgName,curUserId,curUserName,createdDate,modifiedDate,status,action ");
				prebuf.append(" ) ");
				prebuf.append(" values (");
				prebuf.append(bizId);
				prebuf.append(",");
				prebuf.append(storage.getTbId());
				prebuf.append(",'");
				prebuf.append(storage.getTableName());
				prebuf.append("',");
				prebuf.append(storage.getWfId());
				prebuf.append(",'");
				prebuf.append(tb.getTableName());
				prebuf.append("',");
				prebuf.append(storage.getUser().getUserId());
				prebuf.append(",'");
				prebuf.append(storage.getUser().getUserName());
				prebuf.append("',");
				prebuf.append(storage.getUser().getUnitId());
				prebuf.append(",'");
				prebuf.append(storage.getUser().getUnitName());
				prebuf.append("',");
				prebuf.append(storage.getUser().getUserId());
				prebuf.append(",'");
				prebuf.append(storage.getUser().getUserName());
				prebuf.append("',");
				prebuf.append("to_date('"+getDate()+"','YYYY-MM-DD HH24:MI:SS')");
				prebuf.append(",");
				prebuf.append("to_date('"+getDate()+"','YYYY-MM-DD HH24:MI:SS')");
				prebuf.append(",'");
				prebuf.append("有效");
				prebuf.append("','");
				prebuf.append("创建");
				prebuf.append("')");
				int id = jdbcTemplate.update(prebuf.toString());				
				if(id > 0){
					//保存子表数据
					if(storage.getsFields() != null){ 
						for(String stbname : storage.getsFields().keySet()){
							for(Map<String,String> fields :storage.getsFields(stbname)){
								maxId = jdbcTemplate.queryForMap("select sq_"+stbname+".nextval  as ID from dual " );
								fields.put("ID", maxId.get("ID").toString());
								prebuf.setLength(0);
								sufbuf.setLength(0);
								prebuf.append("insert into "+stbname +"(");
								sufbuf.append(" values (");
								for(String field : fields.keySet()){									
									prebuf.append(field);
									prebuf.append(",");
									//TO-DO:根据不同的字段类型做特殊处理
									sufbuf.append("'"+fields.get(field)+"'");
									sufbuf.append(",");													
								}							
								prebuf.setLength(prebuf.length()-1);
								sufbuf.setLength(sufbuf.length()-1);
								prebuf.append(")");
								sufbuf.append(")");
								jdbcTemplate.update(prebuf.toString() + sufbuf.toString());
								//保存主表和子表关系							
								jdbcTemplate.update("insert into table_keys(tableName,zkey,stableName,skey) values('"+storage.getTableName()+"',"+bizId+",'"+stbname+"',"+maxId.get("ID")+")");							
							}
						}	
					}
					return jdbcTemplate.queryForMap("select * from " + storage.getTableName() + " where id = "+ bizId);
				}
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
				
		return null;
	}

	@Override
	public int execute(String sql) {
		return jdbcTemplate.update(sql);		
	}

	@Override
	public List<Map<String, Object>> query(String sql) {
		return jdbcTemplate.queryForList(sql);
	}

	@Override
	public Map<String, Object> update(Storage storage) {
		//判断执行sql的表是否存在
		StringBuilder prebuf = new StringBuilder();	
		StringBuilder sufbuf = new StringBuilder();
		try{			
			prebuf.append("update "+storage.getTableName() );
			prebuf.append(" set ");
			for(String field : storage.getFields().keySet()){					
				prebuf.append(field);
				prebuf.append(" = ");
				//TO-DO:根据不同的字段类型做特殊处理
				prebuf.append("'"+storage.getFields().get(field)+"'");
				prebuf.append(",");
			}
			prebuf.setLength(prebuf.length()-1);
			prebuf.append(" where Id = ");
			prebuf.append(storage.getBizId());

			int id = jdbcTemplate.update(prebuf.toString());
			if(id > 0){
				//保存子表数据
				if(storage.getsFields() != null){
					for(String stbname : storage.getsFields().keySet()){					
						for(Map<String,String> fields :storage.getsFields(stbname)){						
							prebuf.setLength(0);
							sufbuf.setLength(0);
							if(StringUtils.isEmpty(fields.get("ID"))){
								Map<String,Object> maxId = jdbcTemplate.queryForMap("select sq_"+stbname+".nextval  as ID from dual " );
								fields.put("ID", maxId.get("ID").toString());
								prebuf.append("insert into "+stbname +"(");
								sufbuf.append(" values (");
								for(String field : fields.keySet()){								
									prebuf.append(field);
									prebuf.append(",");
									//TO-DO:根据不同的字段类型做特殊处理
									sufbuf.append("'"+fields.get(field)+"'");
									sufbuf.append(",");								
								}
								prebuf.setLength(prebuf.length()-1);
								sufbuf.setLength(sufbuf.length()-1);
								prebuf.append(")");
								sufbuf.append(")");
								System.out.println(prebuf.toString() + sufbuf.toString());
								jdbcTemplate.update(prebuf.toString() + sufbuf.toString());							
								jdbcTemplate.update("insert into table_keys(tableName,zkey,stableName,skey) values('"+storage.getTableName()+"',"+storage.getBizId()+",'"+stbname+"',"+maxId.get("ID")+")");							
							}else{
								prebuf.append("update "+stbname );
								prebuf.append(" set ");
								for(String field : fields.keySet()){
									if(!field.equals("ID")){
										prebuf.append(field);
										prebuf.append(" = ");
										//TO-DO:根据不同的字段类型做特殊处理
										prebuf.append("'"+fields.get(field)+"'");
										prebuf.append(",");
									}
								}
								prebuf.setLength(prebuf.length()-1);
								prebuf.append(" where Id = ");
								prebuf.append(fields.get("ID"));
								jdbcTemplate.update(prebuf.toString() + sufbuf.toString());
							}						
						}							
						//删除清空的子表记录
						for(Long key : storage.getsIds(stbname)){
							jdbcTemplate.update("delete from " + stbname + " where ID = "+ key);
						}
					}
				}
				return jdbcTemplate.queryForMap("select * from " + storage.getTableName() + " where id = "+ storage.getBizId());
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}

	private String getDate(){
		//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		//LocalDateTime timePoint = LocalDateTime.now();
		//return timePoint.format(formatter);
		
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss"); 
		return formatter.format(new Date());
	}
	
	/**
	 * 查询符合条件记录总数
	 * 
	 */
	public Long queryAll(Storage storage){
		if(storage == null) return null;
		Object scope = storage.getParameters().get("scope");
		if(scope != null && ((String)scope).equals("workflow")) return queryWorkflowAll(storage);
		
		List<Map<String,Object>> rs = this.query("select count(*) as total from " + storage.getTableName() +
				" a inner join table_summary b on b.bizId = a.Id where b.tbId = "+storage.getTbId() +" and b.createdUserId = "+storage.getUser().getUserId() +
				" and b.tableName = '"+ storage.getTableName()+"'");
		if(rs.size() > 0) {
			return Long.parseLong(rs.get(0).get("TOTAL").toString());
		}
		return null;
	}
	
	/**
	 * 统计流程列表的总数
	 * @param storage
	 * @return
	 */
	public Long queryWorkflowAll(Storage storage){
		if(storage == null) return null;		
		List<Map<String,Object>> rs = this.query(
				"select count(*) as total from workflow_brief "
				+ " where dispatchUserId like concat(concat('%,',"+storage.getUser().getUserId()+",',%')) "
				+ " and wfId="+storage.getWfId()
				+ " and finishedDate is null "
				);
		if(rs.size() > 0) {
			return Long.parseLong(rs.get(0).get("TOTAL").toString());
		}
		return null;
	}
	
	
	/**
	 * 查询申请列表
	 * 
	 */
	public List<Map<String,Object>> queryPage(Storage storage){
		
		Object scope = storage.getParameters().get("scope");
		if(scope != null && ((String)scope).equals("workflow")) return queryWorkflowPage(storage);
		
		Map<String,Object> parameters = storage.getParameters();
		StringBuilder buf = new StringBuilder();
		buf.append("SELECT * FROM  (  SELECT x.*, rownum rn FROM (");
		buf.append("select * from " );
		buf.append(storage.getTableName());
		buf.append(" a ");
		buf.append(" inner join table_summary b on a.Id = b.bizId ");
		buf.append(" where b.tbId = "+storage.getTbId() +" and b.tableName = '"+ storage.getTableName()+"'" );
		buf.append(" and b.createdUserId = " + storage.getUser().getUserId());
		buf.append(" order by a.ID");
		if(parameters!=null && parameters.size() > 0){
			
		}		
		buf.append(") x WHERE rownum <="+(storage.getBeginNumber()+storage.getSize())+" ) WHERE  rn >=  "+storage.getBeginNumber());		
		return this.query(buf.toString());	
	}

	/**
	 * 查询流程列表
	 * @param storage
	 * @return
	 */
	public List<Map<String,Object>> queryWorkflowPage(Storage storage){
		
		StringBuilder buf = new StringBuilder();
		buf.append("SELECT * FROM  (  SELECT x.*, rownum rn FROM (");
		buf.append("select a.*,c.* from " );
		buf.append(storage.getTableName());
		buf.append(" a ");
		buf.append(" inner join workflow_brief b on b.bizId = a.Id and b.wfId="+storage.getWfId());
		buf.append(" inner join table_summary c on a.Id = c.bizId and c.wfId="+storage.getWfId());
		buf.append(" where ");
		buf.append(" b.dispatchUserId like concat(concat('%,',"+storage.getUser().getUserId()+",',%')) " );
		buf.append(" and b.finishedDate is null ");
		buf.append(") x WHERE rownum <="+(storage.getBeginNumber()+storage.getSize())+" ) WHERE  rn >=  "+storage.getBeginNumber());	
		return this.query(buf.toString());	
	}
	
	/**
	 * 查询指定业务编号的记录，有且仅有一条
	 * @param storage
	 * @return
	 */
	public Map<String,Object> query(Storage storage){
		
		StringBuilder buf = new StringBuilder();
		buf.append("select * from " );
		buf.append(storage.getTableName());		
		buf.append(" where ");
		buf.append(" Id = ");
		buf.append(storage.getBizId());
		List<Map<String,Object>> rs = this.query(buf.toString());	
		if(rs.size() > 0) return rs.get(0);
		return null;
	}

	/**
	 * 处理审批意见
	 * 在流程推进时将主表中的审批意见迁移到审批意见表，同时将主表的对应意见置空。
	 * @param bizId
	 * @param tbId
	 * @param tableName
	 * @param user
	 */
	@Override
	public void migrateComments(Long bizId, Long tbId, String tableName,WorkflowUser user) {
		List<Map<String,Object>> fields = this.query(""
				+ " select b.fieldName from table_element a "
				+ " left join element_library b on a.emId = b.emId "
				+ " where a.tbId = "+tbId
				+ " and a.newFieldType = '审批意见'");
		if(fields != null && fields.size() > 0){
			String fieldNames = "";
			String express = "";
			for(Map<String,Object> field : fields){
				for(Map.Entry<String,Object> item : field.entrySet()){
					fieldNames += item.getValue().toString() + ",";
					express += item.getValue() + " = null ,";
				}
			}
			if(fieldNames.length()>0) fieldNames = fieldNames.substring(0,fieldNames.length()-1);
			if(express.length()>0) express = express.substring(0,express.length()-1);
			List<Map<String,Object>> fieldValues = this.query("select "+fieldNames+" from "+tableName+" where id="+bizId);
			if(fieldValues != null && fieldValues.size() > 0){
				int idx = 0;
				for(Map.Entry<String,Object> fieldValue : fieldValues.get(0).entrySet()){
					if(fieldValue.getValue() != null){
						idx = this.execute(" insert into workflow_comment(tbId,bizId,fieldName,remark,remarkDate,userId,userName) "
								+ " values("+tbId+","+bizId+",'"+fieldValue.getKey()+"','"+fieldValue.getValue().toString()+"',"
										+ " '"+this.getDate()+"',"+user.getUnitId()+",'"+user.getUserName()+"')");
						if(idx <= 0) return;
					}
				}
				if(idx > 0) this.execute("update "+tableName +" set "+express+" where id ="+bizId);
			}
		}		
	}

	@Override
	public Long queryAll(Long userId) {
		if(userId == null) return 0l;
		
		List<Map<String,Object>> rs = this.query("select count(*) as total from table_summary a where a.createdUserId = "+ userId );
		if(rs.size() > 0) {
			return Long.parseLong(rs.get(0).get("TOTAL").toString());
		}
		return 0l;
	}

	@Override
	public List<Map<String, Object>> queryPage(Long userId,Long pageNumber,Long pageSize) {
		if(pageNumber == null) pageNumber = 1l;
		StringBuilder buf = new StringBuilder();
		buf.append("SELECT * FROM  (  SELECT x.*, rownum rn FROM (");
		buf.append("select a.* from " );		
		buf.append(" table_summary a ");
		buf.append(" where ");
		buf.append(" a.createdUserId = " + userId +" order by a.createddate ");
		buf.append(") x WHERE rownum <="+(pageNumber*pageSize)+" ) WHERE  rn >  "+(pageNumber-1)*pageSize);	
		return this.query(buf.toString());	
	}

	@Override
	public Long queryWorkflowAll(Long userId) {
		if(userId == null) return 0l;
		
		List<Map<String,Object>> rs = this.query("select count(*) as total from table_summary a "
				+ "inner join workflow_brief b on b.wfId = a.wfId and a.bizId=b.bizId "
				+ "where a.curUserId = "+ userId +" "
						+ "and b.finishedDate is null");
		if(rs.size() > 0) {
			return Long.parseLong(rs.get(0).get("TOTAL").toString());
		}
		return 0l;
	}

	@Override
	public List<Map<String, Object>> queryWorkflowPage(Long userId,Long pageNumber, Long pageSize) {
		if(pageNumber == null) pageNumber = 1l;
		StringBuilder buf = new StringBuilder();
		buf.append("SELECT * FROM  (  SELECT x.*, rownum rn FROM (");
		buf.append("select a.* from " );		
		buf.append(" table_summary a ");
		buf.append(" inner join workflow_brief b on b.wfId = a.wfId and a.bizId=b.bizId ");
		buf.append(" where ");
		buf.append(" a.curUserId = " + userId );
		buf.append(" and b.finishedDate is null");
		buf.append(") x WHERE rownum <="+(pageNumber*pageSize)+" ) WHERE  rn >  "+(pageNumber-1)*pageSize);	
		return this.query(buf.toString());	
	}
}
