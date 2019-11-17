package cn.ideal.wf.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import cn.ideal.wf.common.WFConstants;
import cn.ideal.wf.model.WorkflowFlow;
import cn.ideal.wf.model.WorkflowNode;
import cn.ideal.wf.model.WorkflowStep;
import cn.ideal.wf.model.WorkflowTableSummary;
import cn.ideal.wf.model.WorkflowUser;
import cn.ideal.wf.service.PlatformService;
import cn.ideal.wf.service.WorkflowFlowService;
import cn.ideal.wf.service.WorkflowNodeService;
import cn.ideal.wf.service.WorkflowTableService;

public abstract class Utils {
	@Autowired
    private WorkflowFlowService workflowFlowService;
    @Autowired
    private WorkflowNodeService workflowNodeService;
    @Autowired
    private WorkflowTableService workflowTableService;
    @Autowired
    private PlatformService platformService;
	/**
	 * 获取指定节点的办理人
	 * @param node
	 * @return
	 * @throws Exception
	 */
	public List<WorkflowUser> findUsersForNode(WorkflowNode node,Long bizId,Long wfId) throws Exception{
		if(node == null) return null;
		List<WorkflowUser> wfuLst = null;
		//判断节点是否已经在流程中做了流转
		List<WorkflowFlow> wffs = workflowFlowService.findWorkflowWithSteps(bizId, wfId, node.getNodeName());
		if(wffs.size() > 0){
			wfuLst = new LinkedList<WorkflowUser>();
			for(WorkflowStep step : wffs.get(0).getWorkflowSteps()){
				WorkflowUser user = new WorkflowUser();
				user.setUserId(step.getDispatchUserId());
				user.setUserName(step.getDispatchUserName());
				user.setUnitId(step.getUnitId());
				user.setUnitName(step.getUnitName());
				wfuLst.add(user);
			}
		}else{				
			switch(node.getuType()){
				case WFConstants.WF_NODE_WORK_USER:
					if(node.getUsers() == null)  throw new Exception("没有办理人无法创建流程");	
					wfuLst = new ArrayList<WorkflowUser>(node.getUsers());									
					break;
				case WFConstants.WF_NODE_WORK_ROLE:
					if(node.getRole() == null) throw new Exception("没有设置角色无法创建流程");
					//当角色所在的单位编码是9999时，则根据发送用户所在的单位查询角色
					if(node.getRole().getUnitId().compareTo(WFConstants.WF_BACKUP_VALUE) == 0){
						//根据创建用户所在的单位进行查询
						WorkflowFlow wff = workflowFlowService.findCreatorFlow(bizId, wfId);
						Long creatorId = null;
						if(wff != null) {
							if(wff.getWorkflowSteps() != null && wff.getWorkflowSteps().size() > 0) {
								creatorId = wff.getWorkflowSteps().get(0).getDispatchUserId();
							}
							wfuLst = platformService.getUsersByRoleIdAndOrgId(node.getRole().getRoleId(), node.getRole().getUnitId(),creatorId);
						}else{
							throw new Exception("流程未开始无法创建流程");
						}
						
					}else{
						//根据指定角色所在的单位进行查询						
						wfuLst = platformService.getUsersByRoleIdAndOrgId(node.getRole().getRoleId(), node.getRole().getUnitId(),null);
					}
																	
					break;
			}
		}
		return wfuLst;
	}

	/**
	 * 获取当前的续办节点
	 * @param wfId
	 * @param bizId
	 * @return
	 */
	public WorkflowNode findNextNode(Long wfId, Long bizId) {
		WorkflowFlow wf = workflowFlowService.findDoingFlow(bizId,wfId);
		
		List<WorkflowNode> wfns = workflowNodeService.findNextNodes(wf.getNodeName(), wf.getWfId(),bizId);		
		if(wfns == null || wfns.size() == 0) return null;
		if(wfns.size() > 0) return wfns.get(0);
		return null;
	}
	
	/**
	 * 流程办结
	 * @param bizId
	 * @param wfId
	 * @return
	 */
	public boolean endFlow(Long bizId,Long wfId,WorkflowUser user){		
		boolean res = workflowFlowService.endFlow(bizId,wfId,user);
		if(res){
			WorkflowTableSummary wfts = new WorkflowTableSummary();	
			wfts.setModifiedDate(new Date());
			wfts.setFinishedDate(new Date());
			wfts.setAction(WFConstants.WF_ACTION_END);
			wfts.setBizId(bizId);
			wfts.setWfId(wfId);
			workflowTableService.endTableSummary(wfts);
			return true;
		}
		
		return false;
	}
	
	/**
	 * 为业务表设置办理人员
	 * @param wfts
	 * @param nextNode
	 * @param users
	 * @return
	 */
	public WorkflowTableSummary setCurrentUserForTableSummary(WorkflowTableSummary wfts,WorkflowNode nextNode,WorkflowUser... users){
		switch (nextNode.getnType()){
		case WFConstants.WF_NODE_TYPE_SINGLE:
			wfts.setCurUserName(","+users[0].getUserName()+",");
			wfts.setCurUserId(","+users[0].getUserId()+",");	
			break;
		case WFConstants.WF_NODE_TYPE_SERIAL:
			wfts.setCurUserName(","+users[0].getUserName()+",");
			wfts.setCurUserId(","+users[0].getUserId()+",");	
			break;
		case WFConstants.WF_NODE_TYPE_PARALLEL:	
			String curUserName = ",",curUserId = ",";
			for(WorkflowUser item : users) {
				curUserName += item.getUserName() + ",";
				curUserId += item.getUserId() + ",";
			}
			wfts.setCurUserName(curUserName);
			wfts.setCurUserId(curUserId);
			break;
		}
		return wfts;
	}
}
