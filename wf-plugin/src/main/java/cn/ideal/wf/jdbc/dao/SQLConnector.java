package cn.ideal.wf.jdbc.dao;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class SQLConnector {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private static SQLExecutor sqlExecutor;
	
	@Autowired
    public void setSQLExecutor(ApplicationContext context) {
		try {
			String driver = jdbcTemplate.getDataSource().getConnection().getMetaData().getDriverName();			
			if(driver.toLowerCase().contains("mysql")) sqlExecutor = (SQLExecutor) context.getBean("WFMYSQLExecutor");
			if(driver.toLowerCase().contains("oracle")) sqlExecutor = (SQLExecutor) context.getBean("WFORACLEExecutor");
			if(sqlExecutor == null){
				String url = jdbcTemplate.getDataSource().getConnection().getMetaData().getURL();				
				if(url.toLowerCase().contains("mysql")) sqlExecutor = (SQLExecutor) context.getBean("WFMYSQLExecutor");
				if(url.toLowerCase().contains("oracle")) sqlExecutor = (SQLExecutor) context.getBean("WFORACLEExecutor");
			}
		} catch (SQLException e) {			
			e.printStackTrace();
		}		
    }
	
	public static SQLExecutor getSQLExecutor(){
		return sqlExecutor;
	}
}
