package cn.ideal.wf.jdbc.dao;
/**
 * 持久层采用spring jdbc方式操作MySQL数据库
 * @author 郭佟燕
 * @version 2.0
 * 
 * * 
		 * sql生成器查询语句中必须按照这样的别名进行定义
		 * 数据库表别名定义：
		 * table_summary  别名： ts
		 * 业务表                         别名：biz
		 * workflow_flow  别名：wff
		 * workflow_step  别名：wfs
		 * workflow_brief 别名：wfb
		 * workflow_node  别名：wfn
		 * workflow_dispenser 别名：wfd
		 * workflow_receiver  别名：wfr
		 * 
 */
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.ideal.wf.cache.factory.TableBriefCacheFactory;
import cn.ideal.wf.data.analyzer.Storage;
import cn.ideal.wf.model.WorkflowTableBrief;
import cn.ideal.wf.model.WorkflowUser;


@Service("WFMYSQLExecutor")
public class WFMySQLExecutor extends SQLUtils implements SQLExecutor {
	@Autowired
	private JdbcTemplate wfJdbcTemplate;
	@Autowired
	private TableBriefCacheFactory tableBriefCacheFactory;
	@Override
	public Map<String,Object> save(Storage storage) {
		//判断执行sql的表是否存在
		StringBuilder prebuf = new StringBuilder();	
		StringBuilder sufbuf = new StringBuilder();	
		try{	
			//保存业务主表
			prebuf.append("insert into "+storage.getTableName() +"(");
			sufbuf.append(" values (");
			for(String key : storage.getFields().keySet()){					
				prebuf.append(key);
				prebuf.append(",");
				//TO-DO:根据不同的字段类型做特殊处理
				sufbuf.append("'"+storage.getFields().get(key)+"'");
				sufbuf.append(",");
			}
			prebuf.setLength(prebuf.length()-1);
			sufbuf.setLength(sufbuf.length()-1);
			prebuf.append(")");
			sufbuf.append(")");

			int idx = wfJdbcTemplate.update(prebuf.toString() + sufbuf.toString());
			if(idx > 0){	
				Map<String,Object> maxId = wfJdbcTemplate.queryForMap("select last_insert_id() as ID" );
				if(maxId != null){
					BigInteger bizId = (BigInteger)maxId.get("ID");
					//保存业务关联主表
					prebuf.setLength(0);
					WorkflowTableBrief tb = tableBriefCacheFactory.getValue(storage.getTbId());
					prebuf.append("insert into table_summary ( ");
					prebuf.append("bizId,tbId,tableName,wfId,defId,title,createdUserId,createdUserName,createdOrgId,createdOrgName,curUserId,curUserName,createdDate,modifiedDate,status,action ");
					prebuf.append(" ) ");
					prebuf.append(" values (");
					prebuf.append(bizId);
					prebuf.append(",");
					prebuf.append(storage.getTbId());
					prebuf.append(",'");
					prebuf.append(storage.getTableName());
					prebuf.append("',");
					prebuf.append(storage.getWfId());
					prebuf.append(",");
					prebuf.append(storage.getDefId());
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
					prebuf.append("',',");
					prebuf.append(storage.getUser().getUserId());
					prebuf.append(",',',");
					prebuf.append(storage.getUser().getUserName());
					prebuf.append(",',");
					prebuf.append("now()");
					prebuf.append(",");
					prebuf.append("now()");
					prebuf.append(",'");
					prebuf.append("有效");
					prebuf.append("','");
					prebuf.append("创建");
					prebuf.append("')");
					int id = wfJdbcTemplate.update(prebuf.toString());
					if(id > 0){	
						//保存子表数据
						if(storage.getsFields() != null){
							for(String stbname : storage.getsFields().keySet()){
								for(Map<String,String> fields :storage.getsFields(stbname)){
									prebuf.setLength(0);
									sufbuf.setLength(0);
									prebuf.append("insert into "+stbname +"(");
									sufbuf.append(" values (");
									for(String field : fields.keySet()){	
										if(!field.equals("ID")){
											prebuf.append(field);
											prebuf.append(",");
											//TO-DO:根据不同的字段类型做特殊处理
											sufbuf.append("'"+fields.get(field)+"'");
											sufbuf.append(",");
										}						
									}
									prebuf.setLength(prebuf.length()-1);
									sufbuf.setLength(sufbuf.length()-1);
									prebuf.append(")");
									sufbuf.append(")");
									wfJdbcTemplate.update(prebuf.toString() + sufbuf.toString());
									//保存主表和子表关系
									maxId = wfJdbcTemplate.queryForMap("select last_insert_id() as ID" );
									if(maxId != null) {
										wfJdbcTemplate.update("insert into table_keys(tableName,zkey,stableName,skey) values('"+storage.getTableName()+"',"+bizId+",'"+stbname+"',"+maxId.get("ID")+")");
									}
								}
							}
						}
						return wfJdbcTemplate.queryForMap("select a.*,b.summaryId,b.tbId,b.wfId from " + storage.getTableName() + " a inner join table_summary b on a.id=b.bizId and b.tbId="+storage.getTbId()+" where a.id = "+ bizId);
					}
				}
			}			
			
		}catch(Exception e){
			e.printStackTrace();
		}
				
		return null;
	}

	@Override
	public int execute(String sql) {
		return this.wfJdbcTemplate.update(sql);		
	}

	@Override
	public List<Map<String, Object>> query(String sql) {
		return wfJdbcTemplate.queryForList(sql);
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

			int id = wfJdbcTemplate.update(prebuf.toString());
			if(id > 0){
				//保存子表数据
				if(storage.getsFields() != null){
					for(String stbname : storage.getsFields().keySet()){
						//存储有效的子表序列号
						List<String> sIds = new ArrayList<String>();
						for(Map<String,String> fields :storage.getsFields(stbname)){
							prebuf.setLength(0);
							sufbuf.setLength(0);
							if(StringUtils.isEmpty(fields.get("ID"))){
								prebuf.append("insert into "+stbname +"(");
								sufbuf.append(" values (");
								for(String field : fields.keySet()){
									if(!field.equals("ID")){
										prebuf.append(field);
										prebuf.append(",");
										//TO-DO:根据不同的字段类型做特殊处理
										sufbuf.append("'"+fields.get(field)+"'");
										sufbuf.append(",");
									}
								}
								prebuf.setLength(prebuf.length()-1);
								sufbuf.setLength(sufbuf.length()-1);
								prebuf.append(")");
								sufbuf.append(")");
								wfJdbcTemplate.update(prebuf.toString() + sufbuf.toString());
								Map<String,Object> maxId = wfJdbcTemplate.queryForMap("select last_insert_id() as ID" );
								if(maxId != null) {
									wfJdbcTemplate.update("insert into table_keys(tableName,zkey,stableName,skey) values('"+storage.getTableName()+"',"+storage.getBizId()+",'"+stbname+"',"+maxId.get("ID")+")");
								}
								sIds.add(maxId.get("ID").toString());
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
								wfJdbcTemplate.update(prebuf.toString() + sufbuf.toString());
								sIds.add((String)fields.get("ID"));
							}
							
						}
						
						//删除清空的子表记录	
						if(sIds.size() > 0){
							wfJdbcTemplate.update("delete from " + stbname + " where "
											 +"ID in (select id from (select a.id from "+stbname+" a "
								             +"inner join table_keys b on a.id = b.skey and b.zkey = "+storage.getBizId()+" and b.stableName = '"+stbname+"') p ) "
								             +"and ID not in (select id from (select a.id from "+stbname+" a "
								             +"inner join table_keys b on a.id = b.skey and b.zkey = "+storage.getBizId()+" and b.stableName = '"+stbname+"' and b.skey in ("+sIds.toString().substring(1,sIds.toString().length()-1)+")) t)");
								             						
							}
						}
				}
				return wfJdbcTemplate.queryForMap("select a.*,b.summaryId,b.tbId,b.wfId from " + storage.getTableName() + " a inner join table_summary b on a.id=b.bizId and b.tbId="+storage.getTbId()+" where a.id = "+ storage.getBizId());
			}
						
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * 查询符合条件记录总数
	 * 
	 */
	public Long queryAll(Storage storage){
		if(storage == null) return null;
		String scope = storage.getScope();
		if(scope != null && scope.equals("approve")) return queryWorkflowAll(storage);
		if(scope != null && scope.equals("approved")) return queryWorkedflowAll(storage);
		if(scope != null && scope.equals("know")) return queryKnowAll(storage);
		if(scope != null && scope.equals("node")) return queryNodeAll(storage);
		if(scope != null && scope.equals("sendKnow")) return querySendKnowAll(storage);
		
		StringBuilder buf = new StringBuilder();		
		buf.append("select count(*) as total from ");
		buf.append(storage.getTableName());
		buf.append(" biz inner join table_summary ts on ts.bizId = biz.Id where ts.tbId = "+storage.getTbId() );
		buf.append(" and ts.status='有效'");
		buf.append(this.getParams(storage,"biz"));
		buf.append(this.getParams(storage,"ts"));
		List<Map<String,Object>> rs = this.query(buf.toString());
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
		StringBuilder buf = new StringBuilder();
		buf.append("select count(*) as total from ");
		buf.append(storage.getTableName());
		buf.append(" biz inner join table_summary ts on ts.bizId = biz.Id and ts.tbId = "+storage.getTbId() );
		buf.append(" and ts.status='有效'");
		buf.append(" inner join workflow_brief wfb on ts.wfId = wfb.wfId and ts.bizId = wfb.bizId");
		buf.append(" where ts.tbId="+storage.getTbId());
		buf.append(" and wfb.finishedDate is null ");
		buf.append(this.getParams(storage,"biz"));
		buf.append(this.getParams(storage,"wfb"));
		List<Map<String,Object>> rs = this.query(buf.toString());
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
		
		String scope = storage.getScope();
		if(scope != null && scope.equals("approve")) return queryWorkflowPage(storage);
		if(scope != null && scope.equals("approved")) return queryWorkedflowPage(storage);
		if(scope != null && scope.equals("know")) return queryKnowPage(storage);
		if(scope != null && scope.equals("node")) return queryNodePage(storage);
		if(scope != null && scope.equals("sendKnow")) return querySendKnowPage(storage);
		
		StringBuilder buf = new StringBuilder();
		buf.append("select * from " );
		buf.append(storage.getTableName());
		buf.append(" biz ");
		buf.append(" inner join table_summary ts on biz.Id = ts.bizId ");
		buf.append(" where ts.tbId = "+storage.getTbId() +" and ts.tableName = '"+ storage.getTableName()+"'" );
		buf.append(" and ts.status='有效'" );
		buf.append(this.getParams(storage,"biz"));
		buf.append(this.getParams(storage,"ts"));
		buf.append(" order by ");	
		buf.append(this.getParams(storage,"order"));
		buf.append(" limit ");
		buf.append(storage.getBeginNumber());
		buf.append(" , ");
		buf.append(storage.getSize());
		return this.query(buf.toString());	
	}

	/**
	 * 查询流程列表
	 * @param storage
	 * @return
	 */
	public List<Map<String,Object>> queryWorkflowPage(Storage storage){
		
		StringBuilder buf = new StringBuilder();
		buf.append("select biz.*,ts.* from " );
		buf.append(storage.getTableName());
		buf.append(" biz ");		
		buf.append(" inner join table_summary ts on biz.Id = ts.bizId and ts.tbId="+storage.getTbId() +" and ts.status='有效'");
		buf.append(" inner join workflow_brief wfb on wfb.bizId = ts.bizId and wfb.wfId=ts.wfId");
		buf.append(" where ");
		buf.append(" wfb.finishedDate is null ");
		buf.append(this.getParams(storage,"biz"));
		buf.append(this.getParams(storage,"wfb"));
		buf.append(" order by ");
		buf.append(this.getParams(storage,"order"));
		buf.append(" limit ");
		buf.append(storage.getBeginNumber());
		buf.append(" , ");
		buf.append(storage.getSize());
		return this.query(buf.toString());	
	}
	
	/**
	 * 统计经办流程列表的总数
	 * @param storage
	 * @return
	 */
	public Long queryWorkedflowAll(Storage storage){
		if(storage == null) return null;	
		StringBuilder buf = new StringBuilder();		
		buf.append("select count(*) as total from ");
		buf.append(storage.getTableName());
		buf.append(" biz inner join table_summary ts on ts.bizId = biz.Id and ts.tbId = "+storage.getTbId() );
		buf.append(" and ts.status='有效'");
		buf.append(" inner join (select distinct wff.wfId,wff.bizId from workflow_flow wff where wff.flowid in ");
		buf.append(" (select flowid from workflow_step wfs where wfs.finishedDate is not null");
		buf.append(this.getParams(storage,"wfs"));	
		buf.append(" )" );
		buf.append(" ) tmp on ts.wfid = tmp.wfid and ts.bizId=tmp.bizId and ts.status='有效'" );	
		buf.append(" where ts.tbId = "+storage.getTbId());
		buf.append(this.getParams(storage,"biz"));
		List<Map<String,Object>> rs = this.query(buf.toString());
		if(rs.size() > 0) {
			return Long.parseLong(rs.get(0).get("TOTAL").toString());
		}
		return null;
	}
	
	/**
	 * 统计发送传阅列表的总数
	 * @param storage
	 * @return
	 */
	public Long querySendKnowAll(Storage storage){
		if(storage == null) return null;	
		StringBuilder buf = new StringBuilder();
		buf.append("select count(*) as total from workflow_dispenser wfd ");
		buf.append(" inner join "+storage.getTableName() +" biz on biz.Id=wfd.bizId ");
		buf.append(" where wfd.tbId="+storage.getTbId());
		buf.append(this.getParams(storage,"wfd"));
		List<Map<String,Object>> rs = this.query(buf.toString());
		if(rs.size() > 0) {
			return Long.parseLong(rs.get(0).get("TOTAL").toString());
		}
		return null;
	}
	
	
	/**
	 * 统计接收传阅列表的总数
	 * @param storage
	 * @return
	 */
	public Long queryKnowAll(Storage storage){
		if(storage == null) return null;	
		StringBuilder buf = new StringBuilder();
		buf.append("select count(*) as total from workflow_dispenser wfd ");
		buf.append(" inner join "+storage.getTableName() +" biz on biz.Id=wfd.bizId");
		buf.append(" where wfd.tbId="+storage.getTbId());
		buf.append(" and wfd.receiveUserIds like concat(concat('%,'," + storage.getUser().getUserId() + "),',%')");
		List<Map<String,Object>> rs = this.query(buf.toString());
		if(rs.size() > 0) {
			return Long.parseLong(rs.get(0).get("TOTAL").toString());
		}
		return null;
	}
	
	/**
	 * 统计已过指定办理节点的列表总数
	 * @param storage
	 * @return
	 */
	public Long queryNodeAll(Storage storage){
		if(storage == null) return null;	
		StringBuilder buf = new StringBuilder();
		buf.append("select count(*) as total from table_summary ts ");
		buf.append(" inner join "+storage.getTableName() +" biz on biz.Id=ts.bizId");
		buf.append(" where ts.tbId="+storage.getTbId());
		buf.append(" and ts.bizId in (select wff.bizId from workflow_flow wff where wff.flowId in ( ");
		buf.append(" select wfs.flowID from workflow_step wfs where wfs.flowid in ( ");
		buf.append(" select wff.flowId from workflow_flow wff ");
		buf.append(" inner join workflow_node wfn on wff.nodeName = wfn.nodeName and wfn.wfId="+storage.getWfId());
		buf.append(" and wff.finishedDate is not null ");
		buf.append(this.getParams(storage,"wfn") + ")");
		buf.append(this.getParams(storage,"wfs") + "))");
		buf.append(this.getParams(storage,"biz"));
		List<Map<String,Object>> rs = this.query(buf.toString());
		if(rs.size() > 0) {
			return Long.parseLong(rs.get(0).get("TOTAL").toString());
		}
		return null;
	}
	
	/**
	 * 查询经办过的流程列表
	 * @param storage
	 * @return
	 */
	public List<Map<String,Object>> queryWorkedflowPage(Storage storage){
		
		StringBuilder buf = new StringBuilder();	
		buf.append("select biz.*,ts.* from " );
		buf.append(storage.getTableName());
		buf.append(" biz ");		
		buf.append(" inner join table_summary ts on biz.Id = ts.bizId and ts.tbId="+storage.getTbId() +" and ts.status='有效'");
		buf.append(" inner join (select distinct wff.wfId,wff.bizId from workflow_flow wff where wff.flowid in ");
		buf.append(" (select flowid from workflow_step wfs where wfs.finishedDate is not null ");
		buf.append(this.getParams(storage,"wfs"));
		buf.append(" )) tmp on ts.wfid = tmp.wfid and ts.bizId=tmp.bizId and ts.status='有效'" );
		buf.append(" where ts.tbId = "+storage.getTbId());
		buf.append(this.getParams(storage,"biz"));
		buf.append(" order by ");
		buf.append(this.getParams(storage,"order"));
		buf.append(" limit ");
		buf.append(storage.getBeginNumber());
		buf.append(" , ");
		buf.append(storage.getSize());
		return this.query(buf.toString());	
	}
	
	/**
	 * 查询发送传阅列表
	 * @param storage
	 * @return
	 */
	public List<Map<String,Object>> querySendKnowPage(Storage storage){
		
		StringBuilder buf = new StringBuilder();		
		buf.append("select biz.*,ts.*,wfd.dispenseId,wfd.receiveUserNames,wfd.createdDate as dispensedDate,wfd.content from " );
		buf.append(storage.getTableName());
		buf.append(" biz ");
		buf.append(" inner join table_summary ts on biz.Id = ts.bizId and ts.tbId="+storage.getTbId() +" and ts.status='有效'");
		buf.append(" inner join workflow_dispenser wfd on wfd.bizId = ts.bizId and wfd.tbId=ts.tbId");		
		buf.append(this.getParams(storage,"wfd"));		
		buf.append(" order by ");
		buf.append(this.getParams(storage,"order"));
		buf.append(" limit ");
		buf.append(storage.getBeginNumber());
		buf.append(" , ");
		buf.append(storage.getSize());
		return this.query(buf.toString());	
	}
	
	/**
	 * 查询收到传阅列表
	 * @param storage
	 * @return
	 */
	public List<Map<String,Object>> queryKnowPage(Storage storage){
		
		StringBuilder buf = new StringBuilder();		
		buf.append("select biz.*,ts.*,wfd.dispenseId,wfd.dispenseUserName,wfd.createdDate as dispensedDate, wfr.createdDate as receivedDate from " );
		buf.append(storage.getTableName());
		buf.append(" biz ");
		buf.append(" inner join table_summary ts on biz.Id = ts.bizId and ts.tbId="+storage.getTbId()+" and ts.status='有效'");
		buf.append(" inner join workflow_dispenser wfd on wfd.bizId = ts.bizId and wfd.tbId = ts.tbId " );		
		buf.append(" left join workflow_receiver wfr on wfd.dispenseId = wfr.dispenseId ");
		buf.append(" where wfd.receiveUserIds like concat(concat('%,'," + storage.getUser().getUserId() + "),',%')");
		buf.append(" order by ");
		buf.append(this.getParams(storage,"order"));
		buf.append(" limit ");
		buf.append(storage.getBeginNumber());
		buf.append(" , ");
		buf.append(storage.getSize());	
		return this.query(buf.toString());	
	}
			
	/**
	 * 查询已办理过的节点列表
	 * @param storage
	 * @return
	 */
	public List<Map<String,Object>> queryNodePage(Storage storage){
		
		StringBuilder buf = new StringBuilder();		
		buf.append("select biz.*,ts.* from " );
		buf.append(storage.getTableName());
		buf.append(" biz ");
		buf.append(" inner join table_summary ts on biz.Id = ts.bizId and ts.tbId="+storage.getTbId() +" and ts.status='有效'");
		buf.append(" where ");
		buf.append(" ts.bizId in (select wff.bizId from workflow_flow wff where wff.flowId in ( ");
		buf.append(" select wfs.flowId from workflow_step wfs where wfs.flowid in ( ");
		buf.append(" select wff.flowId from workflow_flow wff ");
		buf.append(" inner join workflow_node wfn on wff.nodeName = wfn.nodeName and wfn.wfId="+storage.getWfId());
		buf.append(" and wff.finishedDate is not null ");
		buf.append(this.getParams(storage,"wfn") + ")");
		buf.append(this.getParams(storage,"wfs") + "))");
		buf.append(this.getParams(storage,"biz"));
		buf.append(" order by ");
		buf.append(this.getParams(storage,"order"));
		buf.append(" limit ");
		buf.append(storage.getBeginNumber());
		buf.append(" , ");
		buf.append(storage.getSize());
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

	@Override
	public void migrateComments(Long bizId, Long tbId, String tableName,WorkflowUser user) {
		List<Map<String,Object>> fields = this.query(""
				+ " select a.newFieldName from table_element a "
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
	public Long queryAll(Long userId,Long tbId,Map<String,String> params,Map<String,String> orders) {
		if(userId == null) return 0l;
		StringBuilder cond = this.getParams(params);
		if(tbId != null) cond.append(" and ts.tbId=").append(tbId);
		
		List<Map<String,Object>> rs = this.query("select count(*) as total from table_summary ts where ts.createdUserId = "+ userId +" and ts.status='有效'" +cond.toString());
		if(rs.size() > 0) {
			return Long.parseLong(rs.get(0).get("TOTAL").toString());
		}
		return 0l;
	}

	@Override
	public List<Map<String, Object>> queryPage(Long userId,Long tbId,Map<String,String> params,Map<String,String> orders,Long pageNumber,Long pageSize) {
		if(pageNumber == null) return null;
		StringBuilder cond = this.getParams(params);
		if(tbId != null) cond.append(" and ts.tbId=").append(tbId);
		StringBuilder order = this.getOrders(orders);
		
		StringBuilder buf = new StringBuilder();
		buf.append("select * from table_summary ts ");
		buf.append(" where ts.createdUserId = " + userId +" and ts.status='有效'"+cond.toString());
		buf.append(order.toString());		
		buf.append(" limit ");
		buf.append(pageNumber-1);
		buf.append(" , ");
		buf.append(pageNumber);
		return this.query(buf.toString());
	}

	@Override
	public Long queryWorkflowAll(Long userId,Long tbId,Map<String,String> params,Map<String,String> orders) {
		if(userId == null) return 0l;
		StringBuilder cond = this.getParams(params);
		if(tbId != null) cond.append(" and ts.tbId=").append(tbId);
		
		List<Map<String,Object>> rs = this.query("select count(*) as total from table_summary ts "
				+ "inner join workflow_brief b on b.wfId = ts.wfId and ts.bizId=b.bizId "
				+ "where ts.curUserId like concat(concat('%,',"+userId+",',%'))  and ts.status='有效' "+cond.toString()
				+ "and b.finishedDate is null");
		if(rs.size() > 0) {
			return Long.parseLong(rs.get(0).get("TOTAL").toString());
		}
		return 0l;
	}

	@Override
	public List<Map<String, Object>> queryWorkflowPage(Long userId, Long tbId,Map<String,String> params,Map<String,String> orders,Long pageNumber, Long pageSize) {
		if(pageNumber == null) return null;
		StringBuilder cond = this.getParams(params);
		if(tbId != null) cond.append(" and ts.tbId=").append(tbId);		
		StringBuilder order = this.getOrders(orders);
		
		StringBuilder buf = new StringBuilder();
		buf.append("select ts.* from " );		
		buf.append(" table_summary ts ");
		buf.append(" inner join workflow_brief b on b.wfId = ts.wfId and ts.bizId=b.bizId ");
		buf.append(" where ");
		buf.append(" ts.curUserId like concat(concat('%,',"+userId+",',%')) and ts.status='有效' "+cond.toString());
		buf.append(" and b.finishedDate is null");
		buf.append(order.toString());		
		buf.append(" limit ");
		buf.append(pageNumber-1);
		buf.append(" , ");
		buf.append(pageNumber);
		return this.query(buf.toString());
	}

	@Override
	public Long queryWorkedflowAll(Long userId,Long tbId,Map<String,String> params,Map<String,String> orders) {
		if(userId == null) return 0l;
		StringBuilder cond = this.getParams(params);
		if(tbId != null) cond.append(" and ts.tbId=").append(tbId);
		
		List<Map<String,Object>> rs = this.query(
				  "select count(*) as total from table_summary ts "
				+ "inner join (select distinct b.wfId,b.bizId  from workflow_flow b where b.flowid in "
				+ "(select flowid from workflow_step c where c.dispatchuserid = "+ userId 
				+ " and c.finishedDate is not null ) "
                + ") d on ts.wfid = d.wfid and ts.bizId=d.bizId and ts.status='有效'"+cond.toString());
		if(rs.size() > 0) {
			return Long.parseLong(rs.get(0).get("TOTAL").toString());
		}
		return 0l;
	}

	@Override
	public List<Map<String, Object>> queryWorkedflowPage(Long userId,Long tbId,Map<String,String> params,Map<String,String> orders,Long pageNumber, Long pageSize) {
		if(pageNumber == null) return null;
		StringBuilder cond = this.getParams(params);
		if(tbId != null) cond.append(" and ts.tbId=").append(tbId);		
		StringBuilder order = this.getOrders(orders);		
		
		StringBuilder buf = new StringBuilder();
		buf.append("select ts.* from " );		
		buf.append(" table_summary ts ");
		buf.append(" inner join (select distinct b.wfId,b.bizId  from workflow_flow b where b.flowid in ");
		buf.append(" (select flowid from workflow_step c where c.dispatchuserid = "+ userId);
		buf.append("  and c.finishedDate is not null )" );
		buf.append(" ) d on ts.wfid = d.wfid and ts.bizId=d.bizId and ts.status='有效'"+cond.toString());
		buf.append(order.toString());	
		buf.append(" limit ");
		buf.append(pageNumber-1);
		buf.append(" , ");
		buf.append(pageNumber);
		return this.query(buf.toString());
	}

	@Override
	public Long queryWorkedflowAll(Map<String,String> params,Map<String,String> orders) {
		StringBuilder cond = this.getParams(params);		
		List<Map<String,Object>> rs = this.query("select count(*) as total from table_summary ts "
				+ "inner join (select * from workflow_flow b where b.finishedDate is null "
                + " and b.nodename !='创建') d on a.wfid = d.wfid and ts.bizId=d.bizId and ts.status='有效'" +cond.toString());
		if(rs.size() > 0) {
			return Long.parseLong(rs.get(0).get("TOTAL").toString());
		}
		return 0l;
	}

	@Override
	public List<Map<String, Object>> queryWorkedflowPage(Map<String,String> params,Map<String,String> orders,Long pageNumber,Long pageSize) {
		if(pageNumber == null) return null;
		StringBuilder cond = this.getParams(params);	
		StringBuilder order = this.getOrders(orders);
		StringBuilder buf = new StringBuilder();
		buf.append("select ts.* from " );		
		buf.append(" table_summary ts ");
		buf.append(" inner join (select * from workflow_flow b where b.finishedDate is null " );
		buf.append(" and b.nodename !='创建') d on ts.wfid = d.wfid and a.bizId=d.bizId and ts.status='有效'" + cond.toString());
		buf.append(order.toString());
		buf.append(" limit ");
		buf.append(pageNumber-1);
		buf.append(" , ");
		buf.append(pageNumber);
		return this.query(buf.toString());
	}
	
	private String getDate(){		
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss"); 
		return formatter.format(new Date());
	}
}
