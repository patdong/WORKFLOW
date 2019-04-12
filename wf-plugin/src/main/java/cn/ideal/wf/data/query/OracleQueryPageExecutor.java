package cn.ideal.wf.data.query;
/**
 * 查询列表页处理器
 * @author 郭佟燕
 * @version 2.0
 */
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.ideal.wf.data.analyzer.Storage;
import cn.ideal.wf.jdbc.dao.SQLExecutor;

@Service("WFORACLEQueryPageExecutor")
public class OracleQueryPageExecutor implements QueryPageExecutor{
	
	@Autowired
	@Qualifier("WFORACLEExecutor")
	private SQLExecutor sqlExecutor;

	/**
	 * 查询符合条件记录总数
	 * 
	 */
	public Long queryAll(Storage storage){
		if(storage == null) return null;
		Object scope = storage.getParameters().get("scope");
		if(scope != null && ((String)scope).equals("workflow")) return queryWorkflowAll(storage);
		
		List<Map<String,Object>> rs = sqlExecutor.query("select count(*) as total from " + storage.getTableName() +
				" a inner join table_summary b on b.bizId = a.Id where b.createdUserId = "+storage.getUser().getUserId() +
				" and b.wfId="+storage.getWfId());
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
		List<Map<String,Object>> rs = sqlExecutor.query(
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
		buf.append(" where b.wfId = "+ storage.getWfId() );
		buf.append(" and b.createdUserId = " + storage.getUser().getUserId());
		if(parameters!=null && parameters.size() > 0){
			
		}		
		buf.append(") x WHERE rownum <="+(storage.getBeginNumber()+storage.getSize())+" ) WHERE  rn >=  "+storage.getBeginNumber());		
		return sqlExecutor.query(buf.toString());	
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
		return sqlExecutor.query(buf.toString());	
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
		List<Map<String,Object>> rs = sqlExecutor.query(buf.toString());	
		if(rs.size() > 0) return rs.get(0);
		return null;
	}
}
