package cn.ideal.wf.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.ideal.wf.common.WFConstants;
import cn.ideal.wf.dao.WorkflowBriefMapper;
import cn.ideal.wf.model.WorkflowBrief;
import cn.ideal.wf.service.WorkflowBriefService;

@Service
public class WorkflowBriefServiceImpl implements WorkflowBriefService{
	@Autowired
	private WorkflowBriefMapper workflowBriefMapper;
	
	@Override
	public boolean addFlowBrief(WorkflowBrief wfb) {
		int idx = workflowBriefMapper.addFlowBrief(wfb);
		
		if(idx > 0) return true;
		return false;
	}

	@Override
	public boolean updateFlowBrief(WorkflowBrief wfb) {
		int idx = workflowBriefMapper.updateFlowBrief(wfb);
		
		if(idx > 0) return true;
		return false;
	}

	@Override
	public boolean endFlowBrief(Long bizId,Long wfId) {
		WorkflowBrief wfb = new WorkflowBrief();
		wfb.setBizId(bizId);
		wfb.setWfId(wfId);
		wfb.setFinishedDate(new Date());
		wfb.setActionName(WFConstants.WF_ACTION_PASS);
		wfb.setStatus(WFConstants.WF_STATUS_END);
		int idx = workflowBriefMapper.endFlowBrief(wfb);
		
		if(idx > 0) return true;
		return false;
	}
	
	@Override
	public boolean endFlowBrief(Long bizId,Long wfId,String actionName) {
		WorkflowBrief wfb = new WorkflowBrief();
		wfb.setBizId(bizId);
		wfb.setWfId(wfId);
		wfb.setFinishedDate(new Date());
		wfb.setActionName(actionName);
		if(StringUtils.isEmpty(actionName)){
			wfb.setActionName(WFConstants.WF_ACTION_PASS);
		}
		wfb.setStatus(WFConstants.WF_STATUS_END);
		int idx = workflowBriefMapper.endFlowBrief(wfb);
		
		if(idx > 0) return true;
		return false;
	}

	@Override
	public WorkflowBrief find(Long bizId,Long wfId) {
		return workflowBriefMapper.find(bizId,wfId);
	}

	@Override
	public boolean frozenFlowBrief(Long bizId,Long wfId) {
		WorkflowBrief wfb = new WorkflowBrief();
		wfb.setBizId(bizId);
		wfb.setWfId(wfId);
		wfb.setStatus(WFConstants.WF_STATUS_SLEEP);
		int idx = workflowBriefMapper.updateStatusFlowBrief(wfb);
		if(idx <= 0) return false;
		return true;
	}

	@Override
	public boolean unFrozenFlowBrief(Long bizId,Long wfId) {
		WorkflowBrief wfb = new WorkflowBrief();
		wfb.setBizId(bizId);
		wfb.setWfId(wfId);
		wfb.setStatus(WFConstants.WF_STATUS_PASSING);
		int idx = workflowBriefMapper.updateStatusFlowBrief(wfb);
		if(idx <= 0) return false;
		return true;
	}

	@Override
	public WorkflowBrief findDoingFlow(Long bizId,Long wfId) {
		return workflowBriefMapper.findDoingFlow(bizId,wfId);
	}
	
}
