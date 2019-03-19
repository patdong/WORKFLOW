package cn.ideal.wfpf.sqlengine.impl;
/**
 * MySQL 中各字段类型的生成器
 * @author 郭佟燕
 * @version 2.0
 */
import org.springframework.stereotype.Service;

import cn.ideal.wfpf.sqlengine.SQLCreator;

@Service("oracleCreator")
public class OracleCreator implements SQLCreator {
	
	@Override
	public String createInt(String fieldName,Boolean immutable, String comment) {
		String sql = "";		
		sql = fieldName + " NUMBER ";
		if(immutable) sql += " NOT NULL ";		
		return sql;
	}

	@Override
	public String createVarchar(String fieldName,Boolean immutable, Long length, String comment) {
		String sql = "";		
		sql = fieldName + " varchar2("+length+")";
		if(immutable) sql += "NOT NULL ";
		
		return sql;
	}

	@Override
	public String createDateTime(String fieldName,Boolean immutable, String comment) {
		String sql = "";		
		sql = fieldName + " DATE ";
		if(immutable) sql += " NOT NULL ";
		
		return sql;
	}	

	@Override
	public String setCharacter(String characterSet) {
		return null;
	}

	@Override
	public String createTable(String tableName) {			
		return "CREATE TABLE "+ tableName;
	}

	@Override
	public String dropTable(String tableName) {	
		return "DROP TABLE "+ tableName;		
	}

	@Override
	public String getComment(String tableName,String fieldName,String comment) {
		return " COMMENT ON COLUMN "+tableName+"."+fieldName+" IS '"+comment+"'";
	}

	@Override
	public String setPrimaryKey(String tableName, String fieldName) {
		return "ALTER TABLE "+tableName+" ADD (CONSTRAINT "+tableName+"_PK PRIMARY KEY (Id))";
		
	}

	@Override
	public String setSequence(String tableName, String fieldName) {
		return "CREATE SEQUENCE sq_"+tableName+" START WITH 1 INCREMENT BY 1 MAXVALUE 1E27 MINVALUE 1 NOCYCLE NOCACHE ORDER";		
	}

}
