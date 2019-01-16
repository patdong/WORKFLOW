package cn.ideal.wf.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.ideal.wf.common.WFConstants;
import cn.ideal.wf.model.WorkflowFlow;
import cn.ideal.wf.model.WorkflowNode;
import cn.ideal.wf.model.WorkflowUser;
import cn.ideal.wf.service.ActualService;
import cn.ideal.wf.service.PlatformService;
import cn.ideal.wf.service.WorkflowBriefService;
import cn.ideal.wf.service.WorkflowFlowService;
import cn.ideal.wf.service.WorkflowNodeService;
import cn.ideal.wf.service.WorkflowStepService;

public class ActualServiceImpl implements ActualService {
	@Autowired
    private WorkflowFlowService workflowFlowService;
    @Autowired
    private WorkflowNodeService workflowNodeService;    
    @Autowired
    private WorkflowStepService workflowStepService;
    @Autowired
    private WorkflowBriefService workflowBriefService;
    @Autowired
    private PlatformService platformService;
    
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean flowPass(Long wfId, Long bizId, WorkflowUser wfu) throws Exception {
		boolean res = true;
		WorkflowFlow wf = workflowFlowService.findDoingFlow(bizId);
		//判断是否已经创建了流程，未创建则先创建再流转
		if(wf == null){
			workflowFlowService.startFlow(bizId, wfId, WFConstants.WF_NODE_START);
		}
		wf = workflowFlowService.findDoingFlow(bizId);
		List<WorkflowNode> wfns = workflowNodeService.findNextNodes(wf.getNodeName(), wf.getWfId());
		if(wfns == null || wfns.size() == 0) {
			res = workflowFlowService.endFlow(bizId);			
			if(!res) throw new Exception("流程办结失败");
			else{			
				//res = this.end(lsId, Constants.LAWSUIT_END);
				if(res) return true;
				else throw new Exception("流程办结失败");
			}
			
		}else{
			//存在下一个办理节点
			WorkflowNode nextNode = wfns.get(0);
			//下一个节点类型是用户
			List<WorkflowUser> wfuLst = new ArrayList<WorkflowUser>();			
			if(nextNode.getuType().equals(WFConstants.WF_NODE_TYPE_USER)){				
				if(nextNode.getUser() != null){	
					wfuLst = new ArrayList<WorkflowUser>(nextNode.getUser());					
				}else{
					throw new Exception("没有办理人无法创建流程");
				}
				
			}else if(nextNode.getuType().equals(WFConstants.WF_NODE_TYPE_ROLE) ||
					nextNode.getuType().equals(WFConstants.WF_NODE_TYPE_ORG)){
				//下一个节点的类型是角色或单位
				if(nextNode.getRole() != null){
					wfuLst = platformService.findUsersByRoleIdAndOrgId(nextNode.getRole().getRoleId(), nextNode.getRole().getUnitId());								
				}else{
					throw new Exception("没有设置角色无法创建流程");
				}
			}
			
			if(wfuLst.size() > 0){				
				res = workflowFlowService.endFlowNewFlow(wf, wfuLst.toArray(new WorkflowUser[wfuLst.size()]));
			}else{							
				throw new Exception("没有办理人无法创建流程");
			}
		}		
				
		return res;
	}

}
