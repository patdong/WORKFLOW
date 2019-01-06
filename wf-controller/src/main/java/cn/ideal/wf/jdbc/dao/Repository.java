package cn.ideal.wf.jdbc.dao;
/**
 * 根据前端设置的表，动态创建对应的表
 * @author 郭佟燕
 * @version 2.0
 *
 */
public interface Repository {
	/**
	 * 创建表
	 * @param sql
	 */
	void createTable(String sql);
	/**
	 * 修改表
	 * @param sql
	 */
	void alterTable(String sql);
	/**
	 * 保存数据
	 * @param sql
	 */
	void save(String sql);
}
