package cn.ideal.wf.formula;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.druid.util.StringUtils;

/**
 * 计算公式处理器
 * @author 郭佟燕
 * @version 2.0
 *
 */
public class DataFormulaProcessor {
		
	/**
	 * 对request中的数据进行计算
	 * @param express
	 * @param request
	 * @return
	 */
	public static String caculate(String express, HttpServletRequest request){
		//解析表达式
		String[] exprs = processExpr(express);		
		//取值		
		String[] values = processValue(exprs,request);				
		//计算
		Double result = null;
		switch (exprs[1]){
		case "+":			
			result = getDouble(values[0]) + getDouble(values[2]);
			break;
		case "-":
			result = getDouble(values[0]) - getDouble(values[2]);
			break;
		case "*":
			if(exprs[2].equals("$n")){
				values[2] = "1";
			}
			result = getDouble(values[0]) * getDouble(values[2]);
			break;
		case "/":
			if(exprs[2].equals("$n")){
				values[2] = String.valueOf(request.getParameterValues(exprs[0].substring(1)).length);
			}
			result = getDouble(values[0]) / getDouble(values[2]);
			break;
		}
		return result.toString();
	}
	
	
	/**
	 * 对数据库中的数据进行计算
	 * @param express
	 * @param res  当前记录
	 * @param reses 记录集
	 * @return
	 */
	public static String caculate(String express, Map<String,Object> res, List<Map<String,Object>> reses){
		//解析表达式
		String[] exprs = processExpr(express);		
		//取值		
		String[] values = processValue(exprs,res,reses);	
		
		if(values == null) return "";
		//计算
		BigDecimal result = new BigDecimal(0);
		switch (exprs[1]){
		case "+":			
			result = getBigDecimal(values[0]).add(getBigDecimal(values[2]));
			break;
		case "-":
			result = getBigDecimal(values[0]).subtract(getBigDecimal(values[2]));
			break;
		case "*":
			if(exprs[2].equals("$n")){				
				for(String val : values){
					if(StringUtils.isEmpty(val)) continue;
					result = result.add(new BigDecimal(val));
				}
				
			}else{
				result = getBigDecimal(values[0]).multiply(getBigDecimal(values[2]));
			}
			break;
		case "/":
			if(exprs[2].equals("$n")){				
				for(String val : values){
					if(StringUtils.isEmpty(val)) continue;
					result = result.add(new BigDecimal(val));
				}
				result = result.divide(new BigDecimal(values.length));
			}
			result = getBigDecimal(values[0]).divide(getBigDecimal(values[2]));
			break;
		}
		return result.toString();
	}
	
	/**
	 * 解析表达式
	 * @param express
	 * @return
	 */
	static String[] processExpr(String express){
		String regex = ("^(\\S+)([*|/|+|-])(\\S+)$");
		
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(express);
		String[] exprs = new String[3];

		if(m.find()){			
			for(int i=1;i<=m.groupCount();i++){
				exprs[i-1] = m.group(i);					
			}
			
		}
		
		return exprs;
	}
	
	/**
	 * 取值
	 * @param exprs
	 * @param request
	 * @return
	 */
	static String[] processValue(String[] exprs, Map<String,Object> res, List<Map<String,Object>> reses){
		if(res == null && reses == null) return null;		
		List<String> values = new ArrayList<String>();
		if(exprs[1] == null) return null;
		switch (exprs[1]){
		case "+":
			if(res == null) break;
			for(String key : res.keySet()){
				if(key.indexOf(exprs[0]) > 0 ) {
					if(res.get(key) != null) values.add(res.get(key).toString());
				}
				if(key.indexOf(exprs[1]) > 0 ) {
					if(res.get(key) != null) values.add(res.get(key).toString());
				}
			}			
			break;
		case "-":
			if(res == null) break;
			for(String key : res.keySet()){
				if(key.indexOf(exprs[0]) > 0 ) {
					if(res.get(key) != null) values.add(res.get(key).toString());
				}
				if(key.indexOf(exprs[1]) > 0 ) {
					if(res.get(key) != null) values.add(res.get(key).toString());
				}
			}	
			break;
		case "*":
			if(exprs[2].equals("$n")){
				if(reses == null) break;
				for(Map<String,Object> map : reses){
					for(String key : map.keySet()){
						if(exprs[0].indexOf(key) > 0 ) {
							if(map.get(key) != null) {
								if(map.get(key) != null) values.add(map.get(key).toString());						
							}
						}
					}	
				}
			}else{
				if(res == null) break;
				for(String key : res.keySet()){
					if(key.indexOf(exprs[0]) > 0 ) {
						if(res.get(key) != null) values.add(res.get(key).toString());
					}
					if(key.indexOf(exprs[1]) > 0 ) {
						if(res.get(key) != null) values.add(res.get(key).toString());
					}
				}	
			}
			break;
		case "/":
			if(exprs[2].equals("$n")){
				if(reses == null) break;
				for(Map<String,Object> map : reses){
					for(String key : map.keySet()){
						if(exprs[0].indexOf(key) > 0 ) {
							if(map.get(key) != null) values.add(map.get(key).toString());						
						}
					}	
				}
			}else{
				if(res == null) break;
				for(String key : res.keySet()){
					if(key.indexOf(exprs[0]) > 0 ) {
						if(res.get(key) != null) values.add(res.get(key).toString());
					}
					if(key.indexOf(exprs[1]) > 0 ) {
						if(res.get(key) != null) values.add(res.get(key).toString());
					}
				}	
			}			
			break;
		}
		
		if(values.size() == 0) return null;
		
		return values.toArray(new String[values.size()]);
	}
	
	
	static String[] processValue(String[] exprs,HttpServletRequest request){
		String[] values = new String[3];
		Double init = new Double(0);
		if(request.getParameterValues(exprs[0].substring(1)) != null){
			for(String value : request.getParameterValues(exprs[0].substring(1))){
				init += getDouble(value);
			}
		}
		values[0] = init.toString();
		
		init = new Double(0);
		if(request.getParameterValues(exprs[2].substring(1)) != null){
			for(String value : request.getParameterValues(exprs[2].substring(1))){
				init += getDouble(value);
			}
		}
		values[2] = init.toString();
		
		return values;
	}
	
	
	/**
	 * 类型转换
	 * @param value
	 * @return
	 */
	static Double getDouble(String value){
		try{
			Double data = new Double(value);
			return data;
		}catch(Exception e){
			return new Double(0);
		}
	}
	
	
	/**
	 * 类型转换
	 * @param value
	 * @return
	 */
	static BigDecimal getBigDecimal(String value){
		try{
			BigDecimal data = new BigDecimal(value);
			return data;
		}catch(Exception e){
			return new BigDecimal(0);
		}
	}
	
	
	public static void main(String[] args){
		String exp = "$_projectAmount*$n";
		DataFormulaProcessor.caculate(exp, null);
	}
}
