package cn.ideal.wf.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.ideal.wf.common.WFConstants;
import cn.ideal.wf.dao.WorkflowFlowMapper;
import cn.ideal.wf.model.WorkflowBrief;
import cn.ideal.wf.model.WorkflowFlow;
import cn.ideal.wf.model.WorkflowNode;
import cn.ideal.wf.model.WorkflowStep;
import cn.ideal.wf.model.WorkflowUser;
import cn.ideal.wf.service.WorkflowBriefService;
import cn.ideal.wf.service.WorkflowFlowService;
import cn.ideal.wf.service.WorkflowNodeService;
import cn.ideal.wf.service.WorkflowStepService;

@Service
public class WorkflowFlowServiceImpl implements WorkflowFlowService{

	@Autowired
	private WorkflowFlowMapper workflowMapper;
	@Autowired
	private WorkflowStepService workflowStepService;
	@Autowired
	private WorkflowNodeService workflowNodeService;
	@Autowired
	private WorkflowBriefService workflowBriefService;

	/**
	 * 创建流程的节点可以传入也可以默认用系统节点。
	 */
	@Override
	public WorkflowFlow startFlow(Long bizId, Long moduleId, String nodeName) throws Exception{
		WorkflowFlow wf = new WorkflowFlow();
		wf.setBizId(bizId);
		wf.setWfId(moduleId);
		if(nodeName == null) wf.setNodeName(WFConstants.WF_NODE_STRAT);
		else wf.setNodeName(nodeName);
		wf.setCreatedDate(new Date());
		wf.setStatus(WFConstants.WF_STATUS_PASSING);
		wf.setActionName(WFConstants.WF_ACTION_START);
		wf.setFlowParentId(null);
		wf.setTimeDiffer(0l);
		workflowMapper.addFlow(wf);
		WorkflowBrief wfb = new WorkflowBrief();
		wfb.setCreatedDate(new Date());
		wfb.setBizId(wf.getBizId());
		wfb.setFlowId(wf.getFlowId());
		wfb.setNodeName(wf.getNodeName());
		wfb.setModuleId(wf.getWfId());
		wfb.setStatus(WFConstants.WF_STATUS_PASSING);
		workflowBriefService.addFlowBrief(wfb);
		return this.findFlow(wf.getFlowId());
	}
	
	@Override
	public boolean endFlow(Long bizId) {
		WorkflowFlow workflow = this.findDoingFlow(bizId);
		if(workflow == null) return false;
		WorkflowFlow wf = new WorkflowFlow();
		wf.setFlowId(workflow.getFlowId());
		wf.setBizId(bizId);
		wf.setFinishedDate(new Date());
		wf.setStatus(WFConstants.WF_STATUS_END);
		wf.setActionName(WFConstants.WF_ACTION_PASS);
		boolean res = this.endFlow(wf);		
		if(res) res = workflowBriefService.endFlowBrief(bizId);
		return res;
	}

	@Override
	public WorkflowFlow findDoingFlow(Long bizId) {
		return workflowMapper.findDoingFlow(bizId);
	}

	@Override
	public List<WorkflowFlow> findDongFlow(Long bizId) {
		return workflowMapper.findDongFlow(bizId);
	}

	@Override
	public List<WorkflowFlow> findAll(Long bizId) {
		return workflowMapper.findAll(bizId);
	}

	@Override
	public WorkflowFlow findFlow(Long flowId) {
		return workflowMapper.find(flowId);
	}

	@Override
	public WorkflowFlow startFlow(Long bizId, Long moduleId, String nodeName,WorkflowUser user) throws Exception{
		WorkflowFlow wf = this.startFlow(bizId, moduleId,nodeName);
		WorkflowStep wfs = new WorkflowStep(wf.getFlowId(),null);
		wfs.setDispatchUserId(user.getUserId());
		wfs.setDispatchUserName(user.getUserName());
		wfs.setUnitId(user.getUnitId());
		wfs.setUnitName(user.getUnitName());
		wfs.setActionName(WFConstants.WF_ACTION_START);
		wfs.setStatus(WFConstants.WF_STATUS_PASSING);
		wfs.setCreatedDate(new Date());
		wfs.setSerial(0l);
		wfs.setTimeDiffer(0l);
		wfs = workflowStepService.addFlowStep(wfs);
		WorkflowBrief wfb = new WorkflowBrief();
		wfb.setDispatchUserId(","+wfs.getDispatchUserId()+",");
		wfb.setUnitId(wfs.getUnitId());
		wfb.setModifiedDate(new Date());
		wfb.setBizId(bizId);
		wfb.setFlowId(wf.getFlowId());
		wfb.setNodeName(wf.getNodeName());
		workflowBriefService.updateFlowBrief(wfb);
		return wf;
	}

	/**
	 * 结束当前的流程并创建新的流程及操作人
	 *
	 */
	@Override
	public boolean endAndAddFlow(Long bizId,WorkflowNode node, WorkflowUser... users) throws Exception{
		if(bizId == null) throw new Exception("无效业务");
		if(node == null) throw new Exception("无效节点");		
		WorkflowFlow wf = this.endCurFlow(bizId);
		if(wf != null){			
			WorkflowFlow newWf = new WorkflowFlow();
			newWf.setNodeName(node.getNodeName());
			newWf.setFlowParentId(wf.getFlowId());
			newWf.setCreatedDate(new Date());
			newWf.setFinishedDate(null);
			newWf.setStatus(WFConstants.WF_STATUS_PASSING);
			newWf.setActionName(WFConstants.WF_ACTION_DOING);		
			newWf = this.addFlow(newWf, users);
			if(newWf != null) return true;
		}
		return false;
	}

	@Override
	public List<WorkflowUser> findWorkflowUsers(Long bizId) {
		return this.workflowMapper.findWorkflowUsers(bizId);
	}
	
	@Override
	public List<WorkflowFlow> findWorkflowWithSteps(Long bizId) {
		return workflowMapper.findWorkflowWithSteps(bizId);
	}

	@Override
	public boolean endFlow(Long bizId, String actionName,WorkflowUser wfu) {		
		WorkflowFlow wf = this.findDoingFlow(bizId);
		wf.setFinishedDate(new Date());
		wf.setStatus(WFConstants.WF_STATUS_END);
		wf.setActionName(actionName);
		if(StringUtils.isEmpty(actionName)){
			wf.setActionName(WFConstants.WF_ACTION_PASS);
		}
		wf.setTimeDiffer(0l);
		boolean res = this.endFlow(wf);
		if(res){
			res = workflowStepService.endFlowSteps(wf.getFlowId(),actionName,wfu);
		}
		workflowBriefService.endFlowBrief(bizId,actionName);
		return res;
	}

	/**
	 * 结束当前流程
	 */
	@Override
	public WorkflowFlow endCurFlow(Long bizId) {
		if(bizId == null) return null;
		WorkflowFlow workflow = this.findDoingFlow(bizId);
		if(workflow == null) return null;
		WorkflowFlow wf = new WorkflowFlow();
		wf.setFlowId(workflow.getFlowId());
		wf.setBizId(bizId);
		wf.setFinishedDate(new Date());
		wf.setStatus(WFConstants.WF_STATUS_END);
		wf.setActionName(WFConstants.WF_ACTION_PASS);
		boolean res = this.endFlow(wf);
		if(res) return workflow;
		else return null;
	}

	private WorkflowFlow addFlow(WorkflowFlow workflow) {
		workflowMapper.addFlow(workflow);
		WorkflowBrief wfb = new WorkflowBrief();
		wfb.setCreatedDate(new Date());
		wfb.setBizId(workflow.getBizId());
		wfb.setFlowId(workflow.getFlowId());
		wfb.setNodeName(workflow.getNodeName());
		wfb.setModuleId(workflow.getWfId());
		workflowBriefService.updateFlowBrief(wfb);
		return this.findFlow(workflow.getFlowId());
	}
	/**
	 * 结束本流程
	 */
	private boolean endFlow(WorkflowFlow workflow) {		
		if(workflow.getFinishedDate() == null) workflow.setFinishedDate(new Date());
		workflow.setStatus(WFConstants.WF_STATUS_END);
		if(workflow.getActionName() == null) workflow.setActionName(WFConstants.WF_ACTION_PASS);
		
		int idx = workflowMapper.endFlow(workflow);		
		if(idx > 0) {
			return workflowStepService.endFlowSteps(workflow.getFlowId());			
		}
		else return false;		
	}

	private WorkflowFlow addFlow(WorkflowFlow workflow, WorkflowUser... users) throws Exception {
		WorkflowFlow wf = this.addFlow(workflow);
		if(users == null){
			List<WorkflowUser> wfus = workflowNodeService.findNodeUsers(workflow.getNodeName());
			if(wfus == null || wfus.size() == 0) throw new Exception("没有办理人无法创建流程");
			else users = wfus.toArray(new WorkflowUser[wfus.size()]);
		}
		
		if(users == null) throw new Exception("没有办理人无法创建流程");
		
		Long i=0l;	
		String dispatchUserId = ",";
		Long stepId = null;
		for(WorkflowUser wfu : users){			
			WorkflowStep wfs = new WorkflowStep(wf.getFlowId(),null);
			wfs.setDispatchUserId(wfu.getUserId());
			wfs.setDispatchUserName(wfu.getUserName());
			wfs.setUnitId(wfu.getUnitId());
			wfs.setUnitName(wfu.getUnitName());
			wfs.setActionName(WFConstants.WF_ACTION_DOING);
			wfs.setStatus(WFConstants.WF_STATUS_PASSING);
			wfs.setSerial(i++);
			wfs.setCreatedDate(new Date());			
			wfs.setTimeDiffer(0l);
			wfs = workflowStepService.addFlowStep(wfs);	
			stepId = wfs.getStepId();
			dispatchUserId += wfu.getUserId()+",";
		}
		
		WorkflowBrief wfb = new WorkflowBrief();
		if(dispatchUserId.split(",",-1).length <= 3) wfb.setStepId(stepId);
		wfb.setDispatchUserId(dispatchUserId);
		wfb.setUnitId(users[0].getUnitId());
		wfb.setModifiedDate(new Date());
		wfb.setNodeName(workflow.getNodeName());
		wfb.setModifiedDate(new Date());
		wfb.setBizId(workflow.getBizId());
		wfb.setFlowId(workflow.getFlowId());
		wfb.setActionName(workflow.getActionName());
		wfb.setFinishedDate(null);
		workflowBriefService.updateFlowBrief(wfb);
		
		return this.findFlow(workflow.getFlowId());
	}
}
