package cn.ideal.wf.jdbc.dao;
/**
 * sql条件拼接
 * @author 郭佟燕
 * @version 2.0
 */
import java.util.Map;

import cn.ideal.wf.data.analyzer.Storage;

public class SQLUtils {

	/**
	 * 获得查询条件参数
	 * @param params
	 * @return
	 */
	public StringBuilder getParams(Map<String,String> params){
		StringBuilder cond = new StringBuilder();
		if(params != null){
			for(String key : params.keySet()){				
				cond.append(" and ").append(key).append("=").append(params.get(key));
			}
		}
		
		return cond;
	}
	
	/**
	 * 获得排序参数
	 * @param orders
	 * @return
	 */
	public StringBuilder getOrders(Map<String,String> orders){		
		StringBuilder order = new StringBuilder(" order by ");		
		if(orders != null && orders.entrySet().size() > 0){
			for(String key : orders.keySet()){
				order.append(key).append(" ").append(orders.get(key));
			}
		}else{
			order.append(" ts.createddate ");
		}
		
		return order;
	}
	
	/**
	 * 解析查询条件
	 * @param storage
	 * @return
	 */
	public String getParams(Storage storage,String pre){
		StringBuilder buf = new StringBuilder();
		Map<String,Map<String,Object>> condparams = storage.getParameters();
		Map<String,Object> params = condparams.get(pre);
		if(params == null) return buf.toString();
		for(String key : params.keySet()){
			if(params.get(key) instanceof String){
				if(pre.equals("order")){
					if(buf.length() > 0) buf.append(",");
					buf.append(key + " " + params.get(key));
				}else{
					if(key.equals("wfb.dispatchUserId")){
						buf.append(" and " + key + " like concat(concat('%,'," + params.get(key) + "),',%')");
					}else{				
						buf.append(" and " + key + " = '" + params.get(key)+"'");
					}
				}
			}else if(params.get(key) instanceof String[]){
				String[] vals = (String[])params.get(key);
				if(vals[0] != null){
					buf.append(" and " + key + " >= '" + vals[0] +"'" );
				}
				if(vals[1] != null){
					buf.append(" and " + key + " <= '" + vals[1] +"'" );
				}
			}
		}		
		return buf.toString();
	}
}
