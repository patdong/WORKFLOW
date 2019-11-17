package cn.ideal.wf.data.analyzer;
/**
 * 参数解析器
 * @author 郭佟燕
 * @version 2.0
 *
 */
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ideal.wf.model.WorkflowTableBrief;
import cn.ideal.wf.model.WorkflowUser;
import cn.ideal.wf.service.PlatformService;
import cn.ideal.wf.service.WorkflowTableService;
import cn.ideal.wf.service.WorkflowWFService;

import com.alibaba.druid.util.StringUtils;

@Service
public class ParameterAnalyzer implements Analyzer {
	@Autowired
    private WorkflowWFService workflowWFService;
	@Autowired
    private WorkflowTableService workflowTableService;
	@Autowired
	private PlatformService platformService;
	
	
	@Override
	public Storage dataAnalyze(HttpServletRequest request, Long tbId,Long wfId, String scope) throws Exception{
		WorkflowTableBrief wftb = workflowTableService.find(tbId);	    
		Storage storage = new Storage();
		storage.setWfId(wftb.getWfId());
		storage.setTbId(tbId);
		storage.setTableName(wftb.getName());
		storage.setUser(platformService.getWorkflowUser(request));
		storage.setScope(scope);
		if(!StringUtils.isEmpty(request.getParameter("bizId"))){
			storage.setBizId(Long.parseLong(request.getParameter("bizId")));
		}
		storage.setParameters(this.getParams(request,tbId,scope,storage.getUser()));
				
		return storage;
	}

	/**
	 * sql生成器中必须按照这样的别名进行定义
	 * 数据库表别名定义：
	 * table_summary  别名： ts
	 * 业务表                         别名：biz
	 * workflow_flow  别名：wff
	 * workflow_step  别名：wfs
	 * workflow_brief 别名：wfb
	 * workflow_node  别名：wfn
	 * 
	 */
	private Map<String,Map<String,Object>> getParams(HttpServletRequest request,Long tbId, String scope,WorkflowUser user){
		//过滤查询条件
		Map<String,Map<String,Object>> condparams = new HashMap<String,Map<String,Object>>();
		
		//从request中解析页面查询参数
		String queryString = request.getQueryString();
		if(queryString != null){
			String[] conds = queryString.split("&");			
			for(String cond : conds){
				//处理区间段查询
				String key = cond.substring(0,cond.indexOf("="));				
				String value = request.getParameter(key);	
								
				if(!StringUtils.isEmpty(value)){					
					if(value.contains("+")){
						if(value.indexOf("+") > 0) {					
							String[] vals = new String[2];
							if(value.indexOf("+") == 0){
								vals[1] = value.substring(value.indexOf("+")+1);
							}else if(value.indexOf("+") == value.length()-1){
								vals[0] = value.substring(0,value.indexOf("+"));
							}else{
								vals[0] = value.substring(0,value.indexOf("+"));
								vals[1] = value.substring(value.indexOf("+")+1);
							}							
							putCondition(condparams,key, vals);	
						}
					}else putCondition(condparams,key, value);									
				}
			}			
		}		
		
		if(scope != null){
			switch (scope){
			case "apply":
				putCondition(condparams,"ts_createdUserId", user.getUserId().toString());
				break;
			case "approve":
				putCondition(condparams,"wfb_dispatchUserId", user.getUserId().toString());
				break;
			case "approved":
				putCondition(condparams,"wfs_dispatchUserId", user.getUserId().toString());
				break;
			case "node":
				putCondition(condparams,"wfn_nodeId", request.getParameter("wfn_nodeId"));
				putCondition(condparams,"wfs_dispatchUserId", user.getUserId().toString());
				break;
			case "sendKnow":
				putCondition(condparams,"wfd_dispenseUserId", user.getUserId().toString());
				break;
			case "know":
				putCondition(condparams,"wfr_receiveUserId", user.getUserId().toString());
				break;				
			}
			
			putCondition(condparams,"ts.createddate", "desc");
			
		}
		return condparams;
	}
	
	private void putCondition(Map<String,Map<String,Object>> condparams,String key,Object value){
		String pre = null;
		if(key.indexOf("biz_") == 0) {
			key = "biz."+key.substring(4);
			pre = "biz";
		}else if(key.indexOf("ts_") == 0) {
			key = "ts."+key.substring(3);
			pre = "ts";
		}else if(key.indexOf("wfb_") == 0) {
			key = "wfb."+key.substring(4);
			pre = "wfb";
		}else if(key.indexOf("wfs_") == 0) {
			key = "wfs."+key.substring(4);
			pre = "wfs";
		}else if(key.indexOf("wfn_") == 0) {
			key = "wfn."+key.substring(4);
			pre = "wfn";
		}else if(key.indexOf("wfd_") == 0) {
			key = "wfd."+key.substring(4);
			pre = "wfd";
		}else if(key.indexOf("wfr_") == 0) {
			key = "wfr."+key.substring(4);
			pre = "wfr";
		}
		//排序
		if(value instanceof String){
			if(value.equals("desc") || value.equals("asc")) pre = "order";
		}
		
		if(pre == null) return;
		
		switch (pre){
		case "biz":
			Map<String,Object> bizparams = condparams.get("biz");
			if(bizparams == null) {
				condparams.put("biz", new LinkedHashMap<String,Object>());
				bizparams = condparams.get("biz");
			}
			bizparams.put(key, value);
			break;
		case "ts":
			Map<String,Object> tsparams = condparams.get("ts");
			if(tsparams == null) {
				condparams.put("ts", new LinkedHashMap<String,Object>());
				tsparams = condparams.get("ts");
			}
			tsparams.put(key, value);
			break;
		case "wfb":
			Map<String,Object> wfbparams = condparams.get("wfb");
			if(wfbparams == null) {
				condparams.put("wfb", new LinkedHashMap<String,Object>());
				wfbparams = condparams.get("wfb");
			}
			wfbparams.put(key, value);
			break;
		case "wfs":
			Map<String,Object> wfsparams = condparams.get("wfs");
			if(wfsparams == null) {
				condparams.put("wfs", new LinkedHashMap<String,Object>());
				wfsparams = condparams.get("wfs");
			}
			wfsparams.put(key, value);
			break;
		case "wfn":
			Map<String,Object> wfnparams = condparams.get("wfn");
			if(wfnparams == null) {
				condparams.put("wfn", new LinkedHashMap<String,Object>());
				wfnparams = condparams.get("wfn");
			}
			wfnparams.put(key, value);
			break;
		case "wfd":
			Map<String,Object> wfdparams = condparams.get("wfd");
			if(wfdparams == null) {
				condparams.put("wfd", new LinkedHashMap<String,Object>());
				wfdparams = condparams.get("wfd");
			}
			wfdparams.put(key, value);
			break;
		case "wfr":
			Map<String,Object> wfrparams = condparams.get("wfr");
			if(wfrparams == null) {
				condparams.put("wfr", new LinkedHashMap<String,Object>());
				wfrparams = condparams.get("wfr");
			}
			wfrparams.put(key, value);
			break;
		case "order":
			Map<String,Object> orderparams = condparams.get("order");
			if(orderparams == null) {
				condparams.put("order", new LinkedHashMap<String,Object>());
				orderparams = condparams.get("order");
			}
			orderparams.put(key, value);
		}
		
	}
	
}
