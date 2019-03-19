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
import cn.ideal.wf.cache.WorkflowCache;
import cn.ideal.wf.data.analyzer.Storage;
import cn.ideal.wf.model.Workflow;
import cn.ideal.wf.model.WorkflowTableBrief;
import cn.ideal.wf.model.WorkflowTableElement;

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
			for(WorkflowTableElement em : storage.getFields()){					
				prebuf.append(em.getFieldName());
				prebuf.append(",");
				//TO-DO:根据不同的字段类型做特殊处理
				sufbuf.append("'"+em.getDataValue()+"'");
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
					Workflow wf = WorkflowCache.getValue(storage.getWfId());
					WorkflowTableBrief tb = TableBriefCache.getValue(wf.getTbId());
					prebuf.append("insert into table_summary ( ");
					prebuf.append("bizId,wfId,title,createdUserId,createdUserName,createdOrgId,createdOrgName,curUserId,curUserName,createdDate,modifiedDate,status,action ");
					prebuf.append(" ) ");
					prebuf.append(" values (");
					prebuf.append(bizId);
					prebuf.append(",");
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
		try{			
			prebuf.append("update "+storage.getTableName() );
			prebuf.append(" set ");
			for(WorkflowTableElement em : storage.getFields()){					
				prebuf.append(em.getFieldName());
				prebuf.append(" = ");
				//TO-DO:根据不同的字段类型做特殊处理
				prebuf.append("'"+em.getDataValue()+"'");
				prebuf.append(",");
			}
			prebuf.setLength(prebuf.length()-1);
			prebuf.append(" where Id = ");
			prebuf.append(storage.getBizId());

			int id = jdbcTemplate.update(prebuf.toString());
			if(id > 0){
				return jdbcTemplate.queryForMap("select * from " + storage.getTableName() + " where id = "+ storage.getBizId());
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}

}
