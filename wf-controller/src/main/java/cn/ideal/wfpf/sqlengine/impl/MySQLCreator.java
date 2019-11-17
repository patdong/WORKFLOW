package cn.ideal.wfpf.sqlengine.impl;
/**
 * MySQL 中各字段类型的生成器
 * @author 郭佟燕
 * @version 2.0
 */
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.ideal.wfpf.sqlengine.SQLCreator;

@Service("mySQLCreator")
public class MySQLCreator implements SQLCreator {
	
	static final String CHARACTER = "utf8mb4";
	static final String COLLATE = "utf8mb4_0900_ai_ci";

	@Override
	public String createInt(String fieldName,Boolean immutable, String comment) {
		String sql =  "`"+fieldName + "` int(11) ";
		if(immutable) sql += " NOT NULL ";
		sql += " COMMENT '"+comment+"'";
		
		return sql;
	}

	@Override
	public String createVarchar(String fieldName,Boolean immutable, Long length, String comment) {
		String sql = "`"+fieldName + "` varchar("+length+") CHARACTER SET "+ CHARACTER +" COLLATE "+ COLLATE;
		if(immutable) sql += " NOT NULL ";
		sql +=" COMMENT '"+comment+"'";
		
		return sql;
	}

	@Override
	public String createDateTime(String fieldName,Boolean immutable, String comment) {
		String sql = "`"+fieldName + "` datetime ";
		if(immutable) sql +=" NOT NULL ";
		sql +=" COMMENT '"+comment+"'";
		
		return sql;
	}

	
	@Override
	public String setCharacter(String characterSet) {
		if(StringUtils.isEmpty(characterSet)) characterSet = CHARACTER;
		return "SET character_set_client = "+ characterSet;
	}

	@Override
	public String createTable(String tableName) {

		return "CREATE TABLE "+ tableName;
	}

	@Override
	public String dropTable(String tableName) {
		return "DROP TABLE IF EXISTS  "+ tableName;
	}

	@Override
	public String getComment(String tableName, String fieldName, String comment) {		
		return null;
	}

	@Override
	public String setPrimaryKey(String tableName, String fieldName) {
		return "`"+fieldName + "` int(11) NOT NULL AUTO_INCREMENT  COMMENT  '关键字',  PRIMARY KEY (`"+fieldName+"`) USING BTREE";
	}

	@Override
	public String setSequence(String tableName, String fieldName) {		
		return null;
	}

	@Override
	public String dropSequence(String tableName) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
