package cn.ideal.wf.sqlengine;

/**
 * 采用spring jdbcTemplate的方式操作数据库
 * @author 郭佟燕
 * @version 2.0
 *
 */

public interface SQLExecutor {

	
	void executeSql(String sql);
	
	/**
	 * 保存数据
	 * @param sql
	 */
	void save(String sql);
	
	void jointSql(Long tbId);	
}
