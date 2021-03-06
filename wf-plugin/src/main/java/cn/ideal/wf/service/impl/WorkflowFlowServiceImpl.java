package cn.ideal.wf.service.impl;

import java.util.ArrayList;
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
	public WorkflowFlow startFlow(Long bizId,Long wfId, String nodeName) throws Exception{
		WorkflowFlow wf = new WorkflowFlow();
		wf.setBizId(bizId);
		wf.setWfId(wfId);
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
		wfb.setWfId(wf.getWfId());
		wfb.setStatus(WFConstants.WF_STATUS_PASSING);
		workflowBriefService.addFlowBrief(wfb);
		return this.findFlow(wf.getFlowId());
	}
	
	@Override
	public boolean endFlow(Long bizId,Long wfId,WorkflowUser user) {
		WorkflowFlow workflow = this.findDoingFlow(bizId,wfId);
		if(workflow == null) return false;
		WorkflowFlow wf = new WorkflowFlow();
		wf.setFlowId(workflow.getFlowId());
		wf.setBizId(bizId);
		wf.setWfId(wfId);
		wf.setFinishedDate(new Date());
		wf.setStatus(WFConstants.WF_STATUS_END);
		wf.setActionName(WFConstants.WF_ACTION_PASS);
		boolean res = this.endFlow(wf,WFConstants.WF_ACTION_PASS,user);
		if(res) res = workflowBriefService.endFlowBrief(bizId,wfId);
		return res;
	}

	@Override
	public WorkflowFlow findDoingFlow(Long bizId,Long wfId) {
		return workflowMapper.findDoingFlow(bizId,wfId);
	}

	@Override
	public List<WorkflowFlow> findDongFlow(Long bizId,Long wfId) {
		return workflowMapper.findDongFlow(bizId,wfId);
	}

	@Override
	public List<WorkflowFlow> findAll(Long bizId,Long wfId) {
		return workflowMapper.findAll(bizId,wfId);
	}

	@Override
	public WorkflowFlow findFlow(Long flowId) {
		return workflowMapper.find(flowId);
	}

	@Override
	public WorkflowFlow startFlow(Long bizId, Long wfId, String nodeName,WorkflowUser user) throws Exception{
		WorkflowFlow wf = this.startFlow(bizId, wfId,nodeName);
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
		wfb.setWfId(wfId);
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
	public boolean endAndAddFlow(Long bizId,Long wfId,String nodeName,String actionName,WorkflowUser user, WorkflowUser... users) throws Exception{
		if(bizId == null) throw new Exception("无效业务");
		if(nodeName == null) throw new Exception("无效节点");		
		WorkflowFlow wf = this.endCurFlow(bizId,wfId,actionName,user);
		if(wf != null){			
			WorkflowFlow newWf = new WorkflowFlow();
			newWf.setBizId(bizId);
			newWf.setWfId(wf.getWfId());
			newWf.setNodeName(nodeName);
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
	public List<WorkflowUser> findWorkflowUsers(Long flowId) {
		return this.workflowMapper.findWorkflowUsers(flowId);
	}
	
	@Override
	public List<WorkflowFlow> findAllWithSteps(Long bizId,Long wfId) {
		return workflowMapper.findAllWithSteps(bizId,wfId);
	}

	@Override
	public boolean endFlow(Long bizId,Long wfId, String actionName,WorkflowUser wfu) {		
		WorkflowFlow wf = this.findDoingFlow(bizId,wfId);
		wf.setFinishedDate(new Date());
		wf.setStatus(WFConstants.WF_STATUS_END);
		wf.setActionName(actionName);
		if(StringUtils.isEmpty(actionName)){
			wf.setActionName(WFConstants.WF_ACTION_PASS);
		}
		wf.setTimeDiffer(0l);
		boolean res = this.endFlow(wf,actionName,wfu);
		if(res){
			res = workflowStepService.endFlowSteps(wf.getFlowId(),actionName,wfu);
		}
		workflowBriefService.endFlowBrief(bizId,wf.getWfId(),actionName);
		return res;
	}

	/**
	 * 结束当前流程
	 */
	private WorkflowFlow endCurFlow(Long bizId,Long wfId,String actionName,WorkflowUser user) {
		if(bizId == null) return null;
		WorkflowFlow workflow = this.findDoingFlow(bizId,wfId);
		if(workflow == null) return null;
		WorkflowFlow wf = new WorkflowFlow();
		wf.setFlowId(workflow.getFlowId());
		wf.setBizId(bizId);
		wf.setWfId(wfId);
		wf.setFinishedDate(new Date());
		wf.setStatus(WFConstants.WF_STATUS_END);
		wf.setActionName(actionName);
		if(actionName == null) wf.setActionName(WFConstants.WF_ACTION_PASS);
		else if(actionName.equals(WFConstants.WF_ACTION_DOING)) wf.setActionName(WFConstants.WF_ACTION_PASS);
		boolean res = this.endFlow(wf,actionName,user);
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
		wfb.setWfId(workflow.getWfId());
		workflowBriefService.updateFlowBrief(wfb);
		return this.findFlow(workflow.getFlowId());
	}
	/**
	 * 结束本流程
	 */
	private boolean endFlow(WorkflowFlow workflow,String actionName, WorkflowUser user) {		
		if(workflow.getFinishedDate() == null) workflow.setFinishedDate(new Date());
		workflow.setStatus(WFConstants.WF_STATUS_END);
		if(workflow.getActionName() == null) {
			if(actionName == null) workflow.setActionName(WFConstants.WF_ACTION_PASS);
			else workflow.setActionName(actionName);
		}
		
		int idx = workflowMapper.endFlow(workflow);		
		if(idx > 0) {
			WorkflowStep wfs = workflowStepService.findDoingflowStep(workflow.getFlowId(), user.getUserId());
			if(wfs != null) {
				return workflowStepService.endFlowStep(wfs.getStepId(),user);
			}else{
				return false;
			}
						
		}
		else return false;		
	}

	private WorkflowFlow addFlow(WorkflowFlow workflow, WorkflowUser... users) throws Exception {
		WorkflowFlow wf = this.addFlow(workflow);
		if(users == null || users.length == 0){
			//如果流程节点已经办理过则从直接流程办理中获取办理用户
			WorkflowStep wfs = workflowStepService.findWrokflowStep(workflow.getBizId(), workflow.getWfId(), workflow.getNodeName());
			//如果没有流程则直接从节点中获取用户
			List<WorkflowUser> wfus = new ArrayList<WorkflowUser>();
			if(wfs != null){
				WorkflowUser wfu = new WorkflowUser();
				wfu.setUserId(wfs.getDispatchUserId());
				wfu.setUserName(wfs.getDispatchUserName());
				wfu.setUnitId(wfs.getUnitId());
				wfu.setUnitName(wfs.getUnitName());
				wfus.add(wfu);
			}
			if(wfus.size() == 0) wfus = workflowNodeService.findNodeUsers(workflow.getNodeName(),wf.getWfId());
			if(wfus == null || wfus.size() == 0) throw new Exception("没有办理人无法创建流程");
			else users = wfus.toArray(new WorkflowUser[wfus.size()]);
		}
		
		if(users == null || users.length == 0) throw new Exception("没有办理人无法创建流程");
		
		Long i=0l;	
		String dispatchUserId = ",";
		Long steps[] = new Long[users.length];		
		WorkflowNode node = workflowNodeService.findNode(workflow.getNodeName(),wf.getWfId());
		
		for(WorkflowUser wfu : users){			
			WorkflowStep wfs = new WorkflowStep(wf.getFlowId(),null);
			wfs.setDispatchUserId(wfu.getUserId());
			wfs.setDispatchUserName(wfu.getUserName());
			wfs.setUnitId(wfu.getUnitId());
			wfs.setUnitName(wfu.getUnitName());
			wfs.setActionName(WFConstants.WF_ACTION_DOING);
			switch (node.getnType()){
			case WFConstants.WF_NODE_TYPE_SINGLE:
				wfs.setStatus(WFConstants.WF_STATUS_PASSING);
				dispatchUserId += wfu.getUserId()+",";
				break;
			case WFConstants.WF_NODE_TYPE_SERIAL:
				if(i.compareTo(0l) == 0) {
					wfs.setStatus(WFConstants.WF_STATUS_PASSING);
					dispatchUserId += wfu.getUserId()+",";
				}
				else wfs.setStatus(WFConstants.WF_STATUS_SLEEP);
				break;
			case WFConstants.WF_NODE_TYPE_PARALLEL:
				wfs.setStatus(WFConstants.WF_STATUS_PASSING);
				dispatchUserId += wfu.getUserId()+",";
				break;
			}
			
			wfs.setSerial(i++);
			wfs.setCreatedDate(new Date());			
			wfs.setTimeDiffer(0l);
			wfs = workflowStepService.addFlowStep(wfs);	
			steps[i.intValue()-1] =wfs.getStepId();			
		}
		
		WorkflowBrief wfb = new WorkflowBrief();
		switch (node.getnType()){
		case WFConstants.WF_NODE_TYPE_SINGLE:
			wfb.setStepId(steps[0]);
			break;
		case WFConstants.WF_NODE_TYPE_SERIAL:
			wfb.setStepId(steps[0]);
			break;
		case WFConstants.WF_NODE_TYPE_PARALLEL:			
			break;
		}		
		wfb.setDispatchUserId(dispatchUserId);
		wfb.setUnitId(users[0].getUnitId());
		wfb.setModifiedDate(new Date());
		wfb.setNodeName(workflow.getNodeName());
		wfb.setModifiedDate(new Date());
		wfb.setBizId(workflow.getBizId());
		wfb.setWfId(workflow.getWfId());
		wfb.setFlowId(workflow.getFlowId());
		wfb.setActionName(workflow.getActionName());
		wfb.setFinishedDate(null);
		workflowBriefService.updateFlowBrief(wfb);
		
		return this.findFlow(workflow.getFlowId());
	}

	@Override
	public WorkflowFlow findPrevFlow(Long bizId, Long wfId) {
		return workflowMapper.findPrevFlow(bizId, wfId);
	}

	@Override
	public WorkflowFlow findCreatorFlow(Long bizId, Long wfId) {
		return workflowMapper.findCreatorFlow(bizId, wfId);
	}

	@Override
	public List<WorkflowFlow> findWorkflowWithSteps(Long bizId, Long wfId,String nodeName) {
		return workflowMapper.findWorkflowWithSteps(bizId, wfId, nodeName);
	}
}
