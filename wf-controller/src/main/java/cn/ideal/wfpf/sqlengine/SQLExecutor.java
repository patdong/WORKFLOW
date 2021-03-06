package cn.ideal.wfpf.sqlengine;

/**
 * 采用spring jdbcTemplate的方式操作数据库
 * @author 郭佟燕
 * @version 2.0
 *
 */

public interface SQLExecutor {

	/**
	 * 执行拼接好的sql
	 * 
	 */
	void executeSql(String... sql) throws Exception;
	
	
	/**
	 * 拼接sql，拼接的数据来源于表单设置的字段信息
	 * @param tbId
	 */
	void createTable(Long tbId,String tableName) throws Exception;	
	
	/**
	 * 判断表名是否已经存在
	 * @param tableName
	 * @return
	 */
	boolean isExist(String tableName);
	
	/**
	 * 删除表结构
	 * @param tableName
	 * @throws Exception
	 */
	void dropTable(String tableName) throws Exception;
	
	/**
	 * 重命名表名
	 * @param tableName
	 * @param oldTableName
	 * @throws Exception
	 */
	void rename(String tableName,String oldTableName) throws Exception;
}
