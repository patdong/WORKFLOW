package cn.ideal.wf.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ideal.wf.model.WorkflowUser;
import cn.ideal.wf.service.WorkflowFlowService;

/**
 * 办结行为
 * @author 郭佟燕
 * @version 2.0
 *
 */
@Service("EndAction")
public class EndAction implements Action {
	@Autowired
	private WorkflowFlowService wfService;
	
	@Override
	public boolean action(Long bizId, WorkflowUser user , WorkflowUser ...users) throws Exception{
		return false;
	}

	@Override
	public boolean action(Long bizId, WorkflowUser user) throws Exception {
		if(bizId == null) throw new Exception("无效业务");		
		wfService.endFlow(bizId);
		
		return false;
	}

}
