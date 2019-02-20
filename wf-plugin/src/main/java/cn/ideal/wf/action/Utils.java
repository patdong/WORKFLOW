package cn.ideal.wf.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import cn.ideal.wf.common.WFConstants;
import cn.ideal.wf.model.WorkflowFlow;
import cn.ideal.wf.model.WorkflowNode;
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
	public List<WorkflowUser> findUsersForNode(WorkflowNode node) throws Exception{
		if(node == null) return null;
		List<WorkflowUser> wfuLst = null;
			
		switch(node.getuType()){			
			case WFConstants.WF_NODE_TYPE_USER:
				if(node.getUsers() == null)  throw new Exception("没有办理人无法创建流程");	
				wfuLst = new ArrayList<WorkflowUser>(node.getUsers());									
				break;
			case WFConstants.WF_NODE_TYPE_ROLE:
				if(node.getRole() == null) throw new Exception("没有设置角色无法创建流程");
				wfuLst = platformService.findUsersByRoleIdAndOrgId(node.getRole().getRoleId(), node.getRole().getUnitId());												
				break;
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
		List<WorkflowNode> wfns = workflowNodeService.findNextNodes(wf.getNodeName(), wf.getWfId());
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
}
