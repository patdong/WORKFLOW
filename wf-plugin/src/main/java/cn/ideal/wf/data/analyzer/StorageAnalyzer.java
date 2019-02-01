package cn.ideal.wf.data.analyzer;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ideal.wf.cache.WorkflowCache;
import cn.ideal.wf.model.Workflow;
import cn.ideal.wf.model.WorkflowTableElement;
import cn.ideal.wf.service.WorkflowTableService;
import cn.ideal.wf.service.WorkflowWFService;

@Service
public class StorageAnalyzer implements Analyzer{
	@Autowired
	private WorkflowWFService wfService;
	@Autowired
	private WorkflowTableService tableService;
	@Override
	public Storage dataAnalyze(HttpServletRequest request, Long wfId) throws Exception{
		Workflow wf = WorkflowCache.getValue(wfId);
		if(wf == null) {
			wf = wfService.find(wfId);
			WorkflowCache.put(wf);
		}
		if(wf == null) throw new Exception("没有配置流程");
		if(wf.getWftableBrief() == null) throw new Exception("没有配置表单");
		Storage storage = new Storage();
		storage.setTableName(wf.getWftableBrief().getName());		
		storage.setWfId(wfId);
		//获得关联表单的字段并赋值
		List<WorkflowTableElement> wftems = tableService.findAllTableElements(wf.getTableId());				
		for(WorkflowTableElement item : wftems){
			item.setDataValue(request.getParameter(item.getFieldName()));
		}
		storage.setFields(wftems);
		
		return storage;
	}

}
