package cn.ideal.wf.sqlengine.impl;
/**
 * MySQL表生成器
 * @author 郭佟燕
 * @version 2.0
 */
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import cn.ideal.wf.model.TableBrief;
import cn.ideal.wf.model.TableElement;
import cn.ideal.wf.service.TableService;
import cn.ideal.wf.sqlengine.SQLExecutor;

import com.mysql.jdbc.StringUtils;

@Service
public class MySQLExecutor implements SQLExecutor {	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private TableService tableService;
	@Autowired
	private MySQLCreator mySQLCreator;
	
	@Override
	public void executeSql(String sql) {
		jdbcTemplate.execute(sql);
		
	}

	@Override
	public void save(String sql) {
		jdbcTemplate.execute(sql);		
	}

	@Override
	public void jointSql(Long tbId) {
		String tableName = null;
		TableBrief tb = tableService.find(tbId);
		if(tb != null) tableName = "tb_"+tb.getName();
		List<TableElement> tbemLst = tableService.findAllTableElements(tbId);
		
		StringBuilder strBuilder = new StringBuilder();
		//判断表单是否已经配置了数据库对应的表名称
		if(!StringUtils.isNullOrEmpty(tableName)){
			SqlRowSet rs = null;
			try{
				rs = jdbcTemplate.queryForRowSet("select * from "+tableName);
			}catch(Exception e){				
			}
			//判断表单对应的表是否已经存在于数据库中
			if(rs != null){
				
			}else{
				//拼接初始化sql
				this.executeSql(mySQLCreator.dropTable(tableName));
				this.executeSql(mySQLCreator.setCharacter(null));
				
				//拼接字段信息				
				strBuilder.append(mySQLCreator.createTable(tableName)+"(");
				for(TableElement te : tbemLst){
					if(te.getNewFieldDataType() != null){
						if(te.getNewFieldDataType().equals("String")){
							strBuilder.append(mySQLCreator.createVarchar(te.getFieldName(), Boolean.parseBoolean(te.getConstraint()), te.getLength(),te.getNewLabelName()));
						}else if(te.getNewFieldDataType().equals("DateTime")){
							strBuilder.append(mySQLCreator.createDateTime(te.getFieldName(), Boolean.parseBoolean(te.getConstraint()), te.getNewLabelName()));
						}else if(te.getNewFieldDataType().equals("Int")){
							strBuilder.append(mySQLCreator.createInt(te.getFieldName(), Boolean.parseBoolean(te.getConstraint()), te.getNewLabelName()));
						}
					}
					strBuilder.append(",");
				}
				strBuilder.append(mySQLCreator.createPrimaryKey(null));
				strBuilder.append(") ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET="+MySQLCreator.CHARACTER+" COLLATE="+MySQLCreator.COLLATE+" ROW_FORMAT=DYNAMIC COMMENT='"+tb.getTableName()+"'");
				
				this.executeSql(strBuilder.toString());
			}
			
		}
	}
}
