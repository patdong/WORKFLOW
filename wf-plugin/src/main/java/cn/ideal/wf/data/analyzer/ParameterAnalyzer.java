package cn.ideal.wf.data.analyzer;
/**
 * 参数解析器
 * @author 郭佟燕
 * @version 2.0
 *
 */
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;

import cn.ideal.wf.cache.WorkflowCache;
import cn.ideal.wf.model.Workflow;
import cn.ideal.wf.service.WorkflowWFService;

@Service
public class ParameterAnalyzer implements Analyzer {
	@Autowired
    private WorkflowWFService workflowWFService;
	@Override
	public Storage dataAnalyze(HttpServletRequest request, Long wfId) throws Exception{
		Workflow wf = WorkflowCache.getValue(wfId);
		if(wf == null) {
			wf = workflowWFService.find(wfId);
			WorkflowCache.put(wf);
		}
		if(wf == null) return null;
		
		Storage storage = new Storage();
		storage.setWfId(wfId);
		storage.setTableName(wf.getWftableBrief().getName());
		if(!StringUtils.isEmpty(request.getParameter("bizId"))){
			storage.setBizId(Long.parseLong(request.getParameter("bizId")));
		}
		
		return storage;
	}

	
}
