package cn.ideal.wf.data.analyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.ideal.wf.model.WorkflowTableBrief;
import cn.ideal.wf.service.WorkflowTableService;
import cn.ideal.wf.service.WorkflowWFService;


@Service
public class StorageAnalyzer implements Analyzer{
	@Autowired
	private WorkflowWFService wfService;
	@Autowired
	private WorkflowTableService tableService;
	@Override
	public Storage dataAnalyze(HttpServletRequest request, Long tbId,Long wfId) throws Exception{
		WorkflowTableBrief wftb = tableService.find(tbId); //TableBriefCache.getValue(tbId);		
		Storage storage = new Storage();
		storage.setTableName(wftb.getName());
		storage.setWfId(wfId);
		storage.setTbId(tbId);
		if(!StringUtils.isEmpty(request.getParameter("defId"))) storage.setDefId(Long.parseLong(request.getParameter("defId")));
		
		//获得主表单的字段并赋值
		List<String> fields = tableService.findTableFieldNames(tbId);
		Map<String,String> keyVal = new HashMap<String,String>();
		for(String field : fields){
			if(request.getParameter(storage.getTableName()+"_"+field) == null) keyVal.put(field,"");
			else keyVal.put(field,request.getParameter(storage.getTableName()+"_"+field));
		}		
		storage.setFields(keyVal);
		
		//获得子表单的字段并赋值
		List<WorkflowTableBrief> wftbs = tableService.findAllSubTables(tbId);
		List<Map<String,String>> sFields = new ArrayList<Map<String,String>>();
		
		for(WorkflowTableBrief tb : wftbs){			
			String[] ids = request.getParameterValues(tb.getName()+"_ID");
			if(ids != null){
				fields = tableService.findTableFieldNames(tb.getTbId());
				//增加关键字的字段-隐藏字段
				fields.add("ID");
				for(int i=0;i<ids.length;i++){										
					keyVal = new HashMap<String,String>();
					for(String field : fields){
						keyVal.put(field,request.getParameterValues(tb.getName()+"_"+field)[i]);
					}
					//过滤掉空记录
					if(isEmptyRecord(keyVal)){
						if(!StringUtils.isEmpty(keyVal.get("ID"))) storage.getsIds(tb.getName()).add(Long.parseLong(keyVal.get("ID")));
					}else sFields.add(keyVal);
				}
			}
			storage.setsFields(tb.getName(), sFields);
			sFields = new ArrayList<Map<String,String>>();
		}
		return storage;
	}

	/**
	 * 判断记录是否是空记录
	 * @param keyVal
	 * @return
	 */
	boolean isEmptyRecord(Map<String,String> keyVal){
		boolean valid = true;
		for(String key : keyVal.keySet()){
			if(!key.equals("ID")){
				if(!StringUtils.isEmpty(keyVal.get(key))) return false;
			}
		}
		return valid;
	}
}
