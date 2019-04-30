package cn.ideal.wfpf.sqlengine.impl;
/**
 * MySQL表生成器
 * @author 郭佟燕
 * @version 2.0
 */
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.ideal.wfpf.model.TableElement;
import cn.ideal.wfpf.service.TableService;
import cn.ideal.wfpf.sqlengine.SQLExecutor;



@Service("WFPFORACLEExecutor")
public class OracleExecutor implements SQLExecutor {	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private TableService tableService;
	@Autowired
	private OracleCreator oracleCreator;
	
	private String primaryKey = "ID";
	@Override
	public void executeSql(String... sql) throws Exception{
		jdbcTemplate.batchUpdate(sql);
		//jdbcTemplate.execute(sql);
		
	}

	@Override
	public void createTable(Long tbId,String tableName) throws Exception {
		List<TableElement> tbemLst = tableService.findTableFieldsToDBCheck(tbId);
		
		StringBuilder strBuilder = new StringBuilder();
		List<String> sql = new ArrayList<String>();
		
		//判断表单是否已经配置了数据库对应的表名称
		if(!StringUtils.isEmpty(tableName)){
			SqlRowSet rs = null;
			try{
				rs = jdbcTemplate.queryForRowSet("select * from "+tableName);
			}catch(Exception e){				
			}
			
			
			//判断表单对应的表是否已经存在于数据库中
			if(rs != null){
				
			}else{
				//拼接初始化sql				
				//拼接字段信息				
				strBuilder.append(oracleCreator.createTable(tableName)+"(");
				for(TableElement te : tbemLst){
					if(te.getNewFieldDataType() != null){
						if(te.getNewFieldDataType().equals("String")){
							strBuilder.append(oracleCreator.createVarchar(te.getFieldName(), Boolean.parseBoolean(te.getConstraint()), te.getNewLength(),te.getNewLabelName()));
							strBuilder.append(",");
						}else if(te.getNewFieldDataType().equals("DateTime")){
							strBuilder.append(oracleCreator.createDateTime(te.getFieldName(), Boolean.parseBoolean(te.getConstraint()), te.getNewLabelName()));
							strBuilder.append(",");
						}else if(te.getNewFieldDataType().equals("Int")){
							strBuilder.append(oracleCreator.createInt(te.getFieldName(), Boolean.parseBoolean(te.getConstraint()), te.getNewLabelName()));
							strBuilder.append(",");
						}						
					}
					if(!StringUtils.isEmpty(te.getNewHiddenFieldName())){
						strBuilder.append(oracleCreator.createVarchar(te.getNewHiddenFieldName(), Boolean.FALSE, 100l,te.getNewLabelName()+"隐藏项"));
						strBuilder.append(",");
					}
				}
				strBuilder.append(oracleCreator.createInt("ID",Boolean.TRUE,primaryKey)+")");
				sql.add(strBuilder.toString());
				strBuilder.setLength(0);
				for(TableElement te : tbemLst){
					if(te.getNewFieldDataType() != null){						
						strBuilder.append(oracleCreator.getComment(tableName,te.getFieldName(),te.getNewLabelName()));														
					}
					if(!StringUtils.isEmpty(te.getNewHiddenFieldName())){
						strBuilder.append(oracleCreator.getComment(tableName,te.getNewHiddenFieldName(),te.getNewLabelName()+"隐藏项"));				
					}
				}
				
				sql.add(oracleCreator.setPrimaryKey(tableName, primaryKey));				
				sql.add(oracleCreator.setSequence(tableName, primaryKey));				
				try{
					this.executeSql(sql.toArray(new String[sql.size()]));
				}catch(Exception e){
					throw new Exception("创建表"+tableName+"失败!");
				}
			}
		
			
		}
		
	}

	@Override
	public boolean isExist(String tableName) {
		try{
			jdbcTemplate.queryForRowSet("select * from "+tableName);
		}catch(Exception e){
			return false;
		}
		return true;
	}

	@Override
	public void dropTable(String tableName) throws Exception {
		this.executeSql(oracleCreator.dropTable(tableName));	
		this.executeSql(oracleCreator.dropSequence(tableName));	
	}
	
	@Override
	public void rename(String tableName, String oldTableName) throws Exception {		
		try{
			jdbcTemplate.execute("ALTER TABLE "+oldTableName+" TO "+tableName);
		}catch(Exception e){
			throw e;
		}
	}
}
