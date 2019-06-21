package cn.ideal.wf.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.ideal.wf.data.analyzer.Storage;
import cn.ideal.wf.jdbc.dao.SQLConnector;
import cn.ideal.wf.model.WorkflowUser;

@Service
public class JdbcSQLExecutor {

	@Autowired
	private JdbcTemplate jdbcTemplate;
		
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={java.lang.RuntimeException.class,java.lang.Exception.class})	
	public void save(String sql) {
		SQLConnector.getSQLExecutor().execute(sql);
	}
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={java.lang.RuntimeException.class,java.lang.Exception.class})
	public Map<String, Object> save(Storage storage) {
		return SQLConnector.getSQLExecutor().save(storage);
	}
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor={java.lang.RuntimeException.class,java.lang.Exception.class})
	public Map<String, Object> update(Storage storage) {
		return SQLConnector.getSQLExecutor().update(storage);
	}

	public List<Map<String, Object>> query(String sql) {
		return SQLConnector.getSQLExecutor().query(sql);
	}

	public Long queryAll(Storage storage) {
		return SQLConnector.getSQLExecutor().queryAll(storage);
	}

	public Long queryWorkflowAll(Storage storage) {
		return SQLConnector.getSQLExecutor().queryWorkflowAll(storage);
	}

	public List<Map<String, Object>> queryPage(Storage storage) {
		return SQLConnector.getSQLExecutor().queryPage(storage);
	}

	public List<Map<String, Object>> queryWorkflowPage(Storage storage) {
		return SQLConnector.getSQLExecutor().queryWorkflowPage(storage);
	}

	public Map<String, Object> query(Storage storage) {
		return SQLConnector.getSQLExecutor().query(storage);
	}
	
	public void migrateComments(Long bizId, Long tbId, String tableName,WorkflowUser user){
		SQLConnector.getSQLExecutor().migrateComments(bizId, tbId, tableName, user);
	}
	
	public Long queryAll(Long userId){
		return SQLConnector.getSQLExecutor().queryAll(userId);
	}
	
	public List<Map<String, Object>> queryPage(Long userId,Long pageNumber,Long pageSize){
		return SQLConnector.getSQLExecutor().queryPage(userId, pageNumber,pageSize);
	}
	
	public Long queryWorkflowAll(Long userId){
		return SQLConnector.getSQLExecutor().queryWorkflowAll(userId);
	}
	
	public List<Map<String, Object>> queryWorkflowPage(Long userId,Long pageNumber,Long pageSize){
		return SQLConnector.getSQLExecutor().queryWorkflowPage(userId, pageNumber,pageSize);
	}
	
	public Long queryWorkedflowAll(Long userId){
		return SQLConnector.getSQLExecutor().queryWorkedflowAll(userId);
	}
	
	public List<Map<String, Object>> queryWorkedflowPage(Long userId,Long pageNumber,Long pageSize){
		return SQLConnector.getSQLExecutor().queryWorkedflowPage(userId, pageNumber,pageSize);
	}
	
	public Long queryWorkedflowAll(){
		return SQLConnector.getSQLExecutor().queryWorkedflowAll();
	}
	
	public List<Map<String, Object>> queryWorkedflowPage(Long pageNumber,Long pageSize){
		return SQLConnector.getSQLExecutor().queryWorkedflowPage(pageNumber,pageSize);
	}
}
