package cn.ideal.wf.jdbc.dao;
/**
 * 持久层采用spring jdbc方式操作MySQL数据库
 * @author 郭佟燕
 * @version 2.0
 */
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import cn.ideal.wf.cache.TableBriefCache;
import cn.ideal.wf.data.analyzer.Storage;
import cn.ideal.wf.model.WorkflowTableBrief;

import com.mysql.jdbc.StringUtils;

@Service("WFMYSQLExecutor")
public class WFMySQLExecutor implements SQLExecutor {
	@Autowired
	private JdbcTemplate jdbcTemplate;
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

			int idx = jdbcTemplate.update(prebuf.toString() + sufbuf.toString());
			if(idx > 0){	
				Map<String,Object> maxId = jdbcTemplate.queryForMap("select last_insert_id() as ID" );
				if(maxId != null){
					BigInteger bizId = (BigInteger)maxId.get("ID");
					//保存业务关联主表
					prebuf.setLength(0);
					WorkflowTableBrief tb = TableBriefCache.getValue(storage.getTbId());
					prebuf.append("insert into table_summary ( ");
					prebuf.append("bizId,tableName,wfId,title,createdUserId,createdUserName,createdOrgId,createdOrgName,curUserId,curUserName,createdDate,modifiedDate,status,action ");
					prebuf.append(" ) ");
					prebuf.append(" values (");
					prebuf.append(bizId);
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
					prebuf.append("now()");
					prebuf.append(",");
					prebuf.append("now()");
					prebuf.append(",'");
					prebuf.append("有效");
					prebuf.append("','");
					prebuf.append("创建");
					prebuf.append("')");
					int id = jdbcTemplate.update(prebuf.toString());
					if(id > 0){	
						//保存子表数据
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
								jdbcTemplate.update(prebuf.toString() + sufbuf.toString());
								//保存主表和子表关系
								maxId = jdbcTemplate.queryForMap("select last_insert_id() as ID" );
								if(maxId != null) {
									jdbcTemplate.update("insert into table_keys(tableName,zkey,stableName,skey) values('"+storage.getTableName()+"',"+bizId+",'"+stbname+"',"+maxId.get("ID")+")");
								}
							}
						}
						return jdbcTemplate.queryForMap("select * from " + storage.getTableName() + " where id = "+ bizId);
					}
				}
			}			
			
		}catch(Exception e){
			e.printStackTrace();
		}
				
		return null;
	}

	@Override
	public void save(String sql) {
		// TODO Auto-generated method stub
		
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
				for(String stbname : storage.getsFields().keySet()){
					for(Map<String,String> fields :storage.getsFields(stbname)){
						prebuf.setLength(0);
						sufbuf.setLength(0);
						if(StringUtils.isNullOrEmpty(fields.get("ID"))){
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
							jdbcTemplate.update(prebuf.toString() + sufbuf.toString());
							Map<String,Object> maxId = jdbcTemplate.queryForMap("select last_insert_id() as ID" );
							if(maxId != null) {
								jdbcTemplate.update("insert into table_keys(tableName,zkey,stableName,skey) values('"+storage.getTableName()+"',"+storage.getBizId()+",'"+stbname+"',"+maxId.get("ID")+")");
							}
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
				
				return jdbcTemplate.queryForMap("select * from " + storage.getTableName() + " where id = "+ storage.getBizId());
			}
						
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}

}
