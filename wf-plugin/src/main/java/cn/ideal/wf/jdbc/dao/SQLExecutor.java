package cn.ideal.wf.jdbc.dao;

import java.util.List;
import java.util.Map;

import cn.ideal.wf.data.analyzer.Storage;
import cn.ideal.wf.model.WorkflowUser;

/**
 * 持久层采用spring jdbc方式操作数据库
 * @author 郭佟燕
 * @version 2.0
 */
public interface SQLExecutor {

	int execute(String sql);
	
	Map<String,Object> save(Storage storage);
	
	Map<String,Object> update(Storage storage);
	
	List<Map<String,Object>> query(String sql);
	
	Long queryAll(Storage storage);
	
	Long queryWorkflowAll(Storage storage);
	
	List<Map<String,Object>> queryPage(Storage storage);
	
	List<Map<String,Object>> queryWorkflowPage(Storage storage);	
	
	Map<String,Object> query(Storage storage);
	
	void migrateComments(Long bizId, Long tbId, String tableName,WorkflowUser user);
	
	Long queryAll(Long userId);
	
	List<Map<String, Object>> queryPage(Long userId,Long pageNumber,Long pageSize);
	
	Long queryWorkflowAll(Long userId);
	
	List<Map<String, Object>> queryWorkflowPage(Long userId,Long pageNumber,Long pageSize);
	
	Long queryWorkedflowAll(Long userId);
	
	List<Map<String, Object>> queryWorkedflowPage(Long userId,Long pageNumber,Long pageSize);
	
	Long queryWorkedflowAll();
	
	List<Map<String, Object>> queryWorkedflowPage(Long pageNumber,Long pageSize);
}
