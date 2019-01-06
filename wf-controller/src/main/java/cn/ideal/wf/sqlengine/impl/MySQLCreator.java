package cn.ideal.wf.sqlengine.impl;
/**
 * MySQL 中各字段类型的生成器
 * @author 郭佟燕
 * @version 2.0
 */
import org.springframework.stereotype.Service;

import com.mysql.jdbc.StringUtils;

import cn.ideal.wf.sqlengine.SQLCreator;

@Service("mySQLCreator")
public class MySQLCreator implements SQLCreator {
	
	static final String CHARACTER = "utf8mb4";
	static final String COLLATE = "utf8mb4_0900_ai_ci";
	
	private StringBuilder strBuilder = new StringBuilder();
	@Override
	public String createInt(String fieldName,Boolean immutable, String comment) {
		strBuilder.setLength(0);		
		strBuilder.append(fieldName + " int(11) ");
		if(immutable) strBuilder.append(" NOT NULL ");
		strBuilder.append(" COMMENT '"+comment+"'");
		
		return strBuilder.toString();
	}

	@Override
	public String createVarchar(String fieldName,Boolean immutable, Long length, String comment) {
		strBuilder.setLength(0);		
		strBuilder.append(fieldName + " varchar("+length+")");
		strBuilder.append(" CHARACTER SET "+ CHARACTER );
		strBuilder.append(" COLLATE "+ COLLATE);
		if(immutable) strBuilder.append("NOT NULL ");
		strBuilder.append(" COMMENT '"+comment+"'");
		
		return strBuilder.toString();
	}

	@Override
	public String createDateTime(String fieldName,Boolean immutable, String comment) {
		strBuilder.setLength(0);		
		strBuilder.append(fieldName + " datetime ");
		if(immutable) strBuilder.append(" NOT NULL ");
		strBuilder.append(" COMMENT '"+comment+"'");
		
		return strBuilder.toString();
	}

	@Override
	public String createPrimaryKey(String fieldName) {
		strBuilder.setLength(0);
		if(StringUtils.isNullOrEmpty(fieldName) ) fieldName = "Id";
		strBuilder.append(fieldName + " int(11) ");
		strBuilder.append(" NOT NULL AUTO_INCREMENT");
		strBuilder.append(" COMMENT  '关键字',");
		strBuilder.append(" PRIMARY KEY ("+fieldName+") USING BTREE");
		return strBuilder.toString();
	}

	@Override
	public String setCharacter(String characterSet) {
		if(StringUtils.isNullOrEmpty(characterSet)) characterSet = CHARACTER;
		strBuilder.setLength(0);		
		strBuilder.append("SET character_set_client = "+ characterSet);
		return strBuilder.toString();
	}

	@Override
	public String createTable(String tableName) {
		strBuilder.setLength(0);			
		strBuilder.append("CREATE TABLE "+ tableName);
		
		return strBuilder.toString();
	}

	@Override
	public String dropTable(String tableName) {
		strBuilder.setLength(0);		
		strBuilder.append("DROP TABLE IF EXISTS  "+ tableName);
		
		return strBuilder.toString();
	}

}
