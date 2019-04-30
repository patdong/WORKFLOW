package cn.ideal.wfpf.sqlengine;

/**
 * sql语句的生成器
 * @author 郭佟燕
 * @version 2.0
 *
 */
public interface SQLCreator {	
	
	String createInt(String fieldName,Boolean immutable,String comment);
	
	String createVarchar(String fieldName,Boolean immutable, Long length, String comment);
	
	String createDateTime(String fieldName,Boolean immutable,String comment);
	
	String setPrimaryKey(String tableName,String fieldName);
	
	String setSequence(String tableName, String fieldName);
	
	String setCharacter(String characterSet);
	
	String createTable(String tableName);
	
	String dropTable(String tableName);
	
	String dropSequence(String tableName);
	
	String getComment(String tableName,String fieldName,String comment);
	
}
