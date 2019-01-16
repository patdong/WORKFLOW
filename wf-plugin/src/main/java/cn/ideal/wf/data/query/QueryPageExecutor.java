package cn.ideal.wf.data.query;
/**
 * 查询列表页处理器
 * @author 郭佟燕
 * @version 2.0
 */
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import cn.ideal.wf.data.analyzer.Storage;
import cn.ideal.wf.jdbc.dao.SQLExecutor;

@Service
@PropertySource("classpath:application.properties")
public class QueryPageExecutor {
	
	@Value("${workflow.database.executor}")
	private String executorName;

	private SQLExecutor sqlExecutor;
	
	@Autowired
    public void setSQLExecutor(ApplicationContext context) {
		sqlExecutor = (SQLExecutor) context.getBean(executorName);
    }

	/**
	 * 查询符合条件记录总数
	 * 
	 */
	public Long queryAll(Storage storage){
		if(storage == null) return null;
		List<Map<String,Object>> rs = sqlExecutor.query("select count(*) as total from " + storage.getTableName());
		if(rs.size() > 0) {
			return Long.parseLong(rs.get(0).get("TOTAL").toString());
		}
		return null;
	}
	
	/**
	 * 查询符合条件的指定记录信息
	 * 
	 */
	public List<Map<String,Object>> queryPage(Storage storage){
		
		Map<String,Object> parameters = storage.getParameters();
		StringBuilder buf = new StringBuilder();
		buf.append("select * from " );
		buf.append(storage.getTableName());
		if(parameters!=null && parameters.size() > 0){
			buf.append(" where ");
		}
		buf.append(" limit ");
		buf.append(storage.getBeginNumber());
		buf.append(" , ");
		buf.append(storage.getSize());
		return sqlExecutor.query(buf.toString());		
	}

	/**
	 * 查询指定业务编号的记录，有且仅有一条
	 * @param storage
	 * @return
	 */
	public Map<String,Object> query(Storage storage){
		
		StringBuilder buf = new StringBuilder();
		buf.append("select * from " );
		buf.append(storage.getTableName());
		buf.append(" where ");
		buf.append(" Id = ");
		buf.append(storage.getBizId());
		List<Map<String,Object>> rs = sqlExecutor.query(buf.toString());	
		if(rs.size() > 0) return rs.get(0);
		return null;
	}
}
