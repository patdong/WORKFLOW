package cn.ideal.wf.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import cn.ideal.wf.data.analyzer.Storage;
import cn.ideal.wf.jdbc.dao.SQLConnector;

@Service
public class JdbcSQLExecutor {

	@Autowired
	private JdbcTemplate jdbcTemplate;
		
		
	public void save(String sql) {
		SQLConnector.getSQLExecutor().save(sql);

	}

	public Map<String, Object> save(Storage storage) {
		return SQLConnector.getSQLExecutor().save(storage);
	}

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
}
