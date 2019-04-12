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

import cn.ideal.wf.cache.TableBriefCache;
import cn.ideal.wf.cache.WorkflowCache;
import cn.ideal.wf.model.Workflow;
import cn.ideal.wf.model.WorkflowTableBrief;
import cn.ideal.wf.service.WorkflowWFService;

import com.alibaba.druid.util.StringUtils;

@Service
public class ParameterAnalyzer implements Analyzer {
	@Autowired
    private WorkflowWFService workflowWFService;
	
	@Override
	public Storage dataAnalyze(HttpServletRequest request, Long tbId) throws Exception{
		//TO-DO 此方法还没写完
		WorkflowTableBrief wftb = TableBriefCache.getValue(tbId);
		Workflow wf = WorkflowCache.getValue(wftb.getWfId());
		if(wf == null) {
			wf = workflowWFService.find(wftb.getWfId());
			WorkflowCache.put(wf);
		}
		if(wf == null) return null;
		
		Storage storage = new Storage();
		storage.setWfId(wftb.getWfId());
		storage.setTbId(tbId);
		storage.setTableName(wftb.getName());
		if(!StringUtils.isEmpty(request.getParameter("bizId"))){
			storage.setBizId(Long.parseLong(request.getParameter("bizId")));
		}
		
		return storage;
	}

	
}
