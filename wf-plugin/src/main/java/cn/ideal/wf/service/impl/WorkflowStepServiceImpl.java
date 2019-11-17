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
import cn.ideal.wf.model.WorkflowBrief;
import cn.ideal.wf.model.WorkflowFlow;
import cn.ideal.wf.model.WorkflowStep;
import cn.ideal.wf.model.WorkflowTableSummary;
import cn.ideal.wf.model.WorkflowUser;
import cn.ideal.wf.service.WorkflowBriefService;
import cn.ideal.wf.service.WorkflowFlowService;
import cn.ideal.wf.service.WorkflowStepService;
import cn.ideal.wf.service.WorkflowTableService;

@Service
public class WorkflowStepServiceImpl implements WorkflowStepService{

	@Autowired
	private WorkflowStepMapper workflowStepMapper;
	@Autowired
	private WorkflowBriefService workflowBriefService;
	@Autowired
	private WorkflowTableService workflowTableService;
	@Autowired
	private WorkflowFlowService workflowFlowService;
	
	@Override
	public WorkflowStep addFlowStep(WorkflowStep workflowStep) {
		int idx = workflowStepMapper.addFlowStep(workflowStep);
		if(idx > 0){
			return this.find(workflowStep.getStepId());
		}
		
		return null;
	}

	@Override
	public boolean endFlowStep(Long stepId,WorkflowUser wfu) {
		WorkflowStep wfs = new WorkflowStep();
		wfs.setStepId(stepId);
		wfs.setFinishedDate(new Date());
		wfs.setStatus(WFConstants.WF_STATUS_END);
		wfs.setActionName(WFConstants.WF_ACTION_PASS);
		wfs.setExecuteUserId(wfu.getUserId());
		wfs.setExecuteUserName(wfu.getUserName());
		int idx = workflowStepMapper.endFlowStep(wfs);
		
		if(idx > 0) return true;
		else return false;
	}

	@Override
	public WorkflowStep findWrokflowStep(Long bizId, Long wfId,WorkflowUser user) {
		Map<String,Object> conds = new HashMap<String,Object>();
		conds.put("bizId", bizId);
		conds.put("wfId", wfId);
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
		WorkflowStep oldWfs = this.find(stepId);		
		WorkflowStep wfs = new WorkflowStep(null,stepId);
		wfs.setDispatchUserId(user.getUserId());
		wfs.setDispatchUserName(user.getUserName());
		wfs.setUnitId(user.getUnitId());
		wfs.setUnitName(user.getUnitName());
		
		//更新流程步骤表
		int idx = workflowStepMapper.setWorkflowStepUser(wfs);		
		if(idx > 0) {
			if(oldWfs != null) {
				//更新流程概述表
				WorkflowBrief wfb = new WorkflowBrief();
				wfb.setStepId(stepId);
				wfb.setOldDispatchUserId(","+oldWfs.getDispatchUserId()+",");
				wfb.setDispatchUserId(","+user.getUserId()+",");
				workflowBriefService.updateDispatchedUser(wfb);
				
				//更新业务概述表
				WorkflowTableSummary wfts = new WorkflowTableSummary();
				WorkflowFlow wff = workflowFlowService.findFlow(oldWfs.getFlowId());
				wfts.setCurUserId(","+user.getUserId()+",");
				wfts.setCurUserName(","+user.getUserName()+",");
				wfts.setOldCurUserId(oldWfs.getDispatchUserId());
				wfts.setBizId(wff.getBizId());
				wfts.setWfId(wff.getWfId());
				workflowTableService.updateCurrentUser(wfts);
			}			
			return true;			
		}
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
	public WorkflowStep findWrokflowStep(Long bizId, Long wfId,String nodeName) {
		Map<String,Object> conds = new HashMap<String,Object>();
		conds.put("bizId", bizId);
		conds.put("wfId", wfId);
		conds.put("nodeName", nodeName);
		
		return workflowStepMapper.findWrokflowStep(conds);
	}

	@Override
	public List<WorkflowStep> findUNFinshedWrokflowStep(Long bizId, Long wfId) {
		return workflowStepMapper.findUNFinshedWrokflowStep(bizId, wfId);
	}

	@Override
	public boolean wakeFlowStep(Long stepId) {
		int idx = workflowStepMapper.wakeFlowStep(stepId);
		if(idx > 0) return true;
		return false;
	}

	@Override
	public WorkflowStep findDoingflowStep(Long bizId, Long wfId,Long userId) {
		List<WorkflowStep> wfss = workflowStepMapper.findDoingflowSteps(bizId, wfId, userId);
		if(wfss.size() > 0) return wfss.get(0);
		return null;
	}

	@Override
	public List<WorkflowStep> findDoingflowSteps(Long bizId, Long wfId) {
		return workflowStepMapper.findDoingflowSteps(bizId, wfId, null);
	}

	@Override
	public boolean pushMsg(Long stepId, String reason) {
		int idx = workflowStepMapper.pushMsg(stepId, reason);
		if(idx > 0) return true;
		return false;
	}

	@Override
	public WorkflowStep findDoingflowStep(Long flowId, Long userId) {
		return workflowStepMapper.findDoingflowStep(flowId, userId);
	}

}
