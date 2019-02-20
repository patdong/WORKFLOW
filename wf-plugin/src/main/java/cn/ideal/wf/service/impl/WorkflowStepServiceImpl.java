package cn.ideal.wf.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.ideal.wf.common.WFConstants;
import cn.ideal.wf.dao.WorkflowStepMapper;
import cn.ideal.wf.model.WorkflowStep;
import cn.ideal.wf.model.WorkflowUser;
import cn.ideal.wf.service.WorkflowStepService;

@Service
public class WorkflowStepServiceImpl implements WorkflowStepService{

	@Autowired
	private WorkflowStepMapper workflowStepMapper;
	
	@Override
	public WorkflowStep addFlowStep(WorkflowStep workflowStep) {
		int idx = workflowStepMapper.addFlowStep(workflowStep);
		if(idx > 0){
			return this.find(workflowStep.getStepId());
		}
		
		return null;
	}

	@Override
	public boolean endFlowStep(Long stepId) {
		WorkflowStep wfs = new WorkflowStep();
		wfs.setStepId(stepId);
		wfs.setFinishedDate(new Date());
		wfs.setStatus(WFConstants.WF_STATUS_END);
		wfs.setActionName(WFConstants.WF_ACTION_PASS);
		int idx = workflowStepMapper.endFlowStep(wfs);
		
		if(idx > 0) return true;
		else return false;
	}

	@Override
	public WorkflowStep findWrokflowStep(Long bizId, WorkflowUser user) {
		Map<String,Object> conds = new HashMap<String,Object>();
		conds.put("bizId", bizId);
		conds.put("userId", user.getUserId());
		conds.put("unitId",user.getUnitId());		
		
		return workflowStepMapper.findWrokflowStep(conds);
	}

	@Override
	public List<WorkflowStep> findAll(Long bizId) {
		return workflowStepMapper.findAll(bizId);
	}

	@Override
	public WorkflowStep find(Long stepId) {
		return workflowStepMapper.find(stepId);
	}	

	@Override
	public List<WorkflowStep> findAllByFlowId(Long flowId) {
		return workflowStepMapper.findAllByFlowId(flowId);
	}

	@Override
	public boolean setWorkflowStepUser(Long stepId, WorkflowUser user) {
		WorkflowStep wfs = new WorkflowStep(null,stepId);
		wfs.setDispatchUserId(user.getUserId());
		wfs.setDispatchUserName(user.getUserName());
		wfs.setUnitId(user.getUnitId());
		wfs.setUnitName(user.getUnitName());
		int idx = workflowStepMapper.setWorkflowStepUser(wfs);
		
		if(idx > 0) return true;
		return false;
	}

	@Override
	public boolean endFlowSteps(Long flowId, String actionName, WorkflowUser wfu) {
		WorkflowStep wfs = new WorkflowStep();
		wfs.setFlowId(flowId);
		wfs.setFinishedDate(new Date());
		wfs.setStatus(WFConstants.WF_STATUS_END);
		wfs.setActionName(actionName);
		if(wfu != null) {
			wfs.setExecuteUserId(wfu.getUserId());
			wfs.setExecuteUserName(wfu.getUserName());
		}
		if(StringUtils.isEmpty(actionName)){
			wfs.setActionName(WFConstants.WF_ACTION_PASS);
		}
		int idx = workflowStepMapper.endFlowStep(wfs);
		
		if(idx > 0) return true;
		return false;
	}

	@Override
	public WorkflowStep findWrokflowStep(Long bizId, String nodeName) {
		Map<String,Object> conds = new HashMap<String,Object>();
		conds.put("bizId", bizId);
		conds.put("nodeName", nodeName);
		
		return workflowStepMapper.findWrokflowStep(conds);
	}

}
