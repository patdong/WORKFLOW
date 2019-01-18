package cn.ideal.wf.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ideal.wf.model.WorkflowBrief;
import cn.ideal.wf.model.WorkflowFlow;
import cn.ideal.wf.model.WorkflowStep;
import cn.ideal.wf.model.WorkflowUser;
import cn.ideal.wf.service.WorkflowBriefService;
import cn.ideal.wf.service.WorkflowFlowService;
import cn.ideal.wf.service.WorkflowStepService;

/**
 * 调度行为
 * @author 郭佟燕
 * @version 2.0
 *
 */
@Service("DispatchAction")
public class DispatchAction implements Action {
	@Autowired
	private WorkflowFlowService wfService;
	@Autowired
	private WorkflowStepService wfStepService;
	@Autowired
	private WorkflowBriefService wfBriefService;
	
	/**
	 * TO-DO 逻辑有问题待优化
	 */
	@Override
	public boolean action(Long bizId, WorkflowUser user , WorkflowUser ...users) {
		WorkflowFlow wf = wfService.findDoingFlow(bizId);
		List<WorkflowStep> wfs = wfStepService.findAllByFlowId(wf.getFlowId());
		boolean res = true;
		for(WorkflowStep item : wfs){
			if(user.getUnitId() == null) user.setUnitId(item.getUnitId());
			if(user.getUnitName() == null) user.setUnitName(item.getUnitName());
			boolean temp = wfStepService.setWorkflowStepUser(item.getStepId(), user);	
			
			if(temp){
				WorkflowBrief wfb = wfBriefService.find(wf.getBizId());				
				wfb.setDispatchUserId(","+user.getUserId()+",");
				wfb.setUnitId(user.getUnitId());				
				res = wfBriefService.updateFlowBrief(wfb);
			}
		}
		
		return res;
	}

	@Override
	public boolean action(Long bizId, WorkflowUser user) {
		// TODO Auto-generated method stub
		return false;
	}

}
