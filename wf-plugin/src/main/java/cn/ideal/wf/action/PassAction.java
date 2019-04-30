package cn.ideal.wf.action;
/**
 * 推进流程行为
 * @author 郭佟燕
 * @version 2.0
 */
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ideal.wf.common.WFConstants;
import cn.ideal.wf.model.WorkflowFlow;
import cn.ideal.wf.model.WorkflowNode;
import cn.ideal.wf.model.WorkflowTableSummary;
import cn.ideal.wf.model.WorkflowUser;
import cn.ideal.wf.service.WorkflowFlowService;
import cn.ideal.wf.service.WorkflowStepService;
import cn.ideal.wf.service.WorkflowTableService;

@Service("PassAction")
public class PassAction extends Utils implements Action{
	@Autowired
    private WorkflowFlowService workflowFlowService;
	@Autowired
    private WorkflowStepService workflowStepService;
	@Autowired
    private WorkflowTableService workflowTableService;
	
	@Override
	public boolean action(Long bizId, Long wfId,  WorkflowUser user, WorkflowUser... users) throws Exception {
		boolean res = true;
		WorkflowNode nextNode = this.findNextNode(wfId,bizId);
		if(nextNode == null) {
			res =this.endFlow(bizId,wfId,user);
			if(!res) throw new Exception("PassActon-流程办结失败");	
			return true;
		}
			
		if(users == null || users.length == 0) {
			WorkflowFlow wff = workflowFlowService.findSenderFlow(bizId, wfId);
			Long senderId = null;
			if(wff != null) {
				if(wff.getWorkflowSteps() != null && wff.getWorkflowSteps().size() > 0) senderId = wff.getWorkflowSteps().get(0).getDispatchUserId();
			}
			List<WorkflowUser> wfuLst =  this.findUsersForNode(nextNode,senderId);
			users = wfuLst.toArray(new WorkflowUser[wfuLst.size()]);
		}
			
		res = workflowFlowService.endAndAddFlow(bizId,wfId,nextNode.getNodeName(),WFConstants.WF_ACTION_DOING,user,users);
		if(res){
			String curUserName = "";
			for(WorkflowUser item : users) curUserName += item.getUserName() + ",";	
			WorkflowTableSummary wfts = new WorkflowTableSummary();	
			if(users.length > 0) wfts.setCurUserId(users[0].getUserId());
			wfts.setCurUserName(curUserName);
			wfts.setModifiedDate(new Date());
			//任何动作都反应在action字段上
			wfts.setAction(nextNode.getNodeName());	
			wfts.setBizId(bizId);
			wfts.setWfId(wfId);
			res = workflowTableService.synchTableSummary(wfts);
			return res;
		}
		return false;
	}

	
	public boolean action(Long bizId, Long wfId,  WorkflowUser user,WorkflowNode nextNode, WorkflowUser... users) throws Exception {
		boolean res = true;		
		if(nextNode == null) {
			nextNode = this.findNextNode(wfId,bizId);
		}
		if(nextNode == null) {
			res =this.endFlow(bizId,wfId,user);
			if(!res) throw new Exception("PassActon-流程办结失败");	
			return true;
		}
			
		if(users == null) {	
			WorkflowFlow wff = workflowFlowService.findSenderFlow(bizId, wfId);
			Long senderId = null;
			if(wff != null) {
				if(wff.getWorkflowSteps() != null && wff.getWorkflowSteps().size() > 0) senderId = wff.getWorkflowSteps().get(0).getDispatchUserId();
			}
			List<WorkflowUser> wfuLst =  this.findUsersForNode(nextNode,senderId);
			users = wfuLst.toArray(new WorkflowUser[wfuLst.size()]);
		}
			
		res = workflowFlowService.endAndAddFlow(bizId,wfId,nextNode.getNodeName(),WFConstants.WF_ACTION_DOING,user,users);
		if(res){
			String curUserName = "";
			
			for(WorkflowUser item : users) curUserName += item.getUserName() + ",";	
			WorkflowTableSummary wfts = new WorkflowTableSummary();
			if(users.length > 0) wfts.setCurUserId(users[0].getUserId());
			wfts.setCurUserName(curUserName);
			wfts.setModifiedDate(new Date());
			//任何动作都反应在action字段上
			wfts.setAction(nextNode.getNodeName());	
			wfts.setBizId(bizId);
			wfts.setWfId(wfId);
			res = workflowTableService.synchTableSummary(wfts);
			return res;
		}
		return false;
	}
	
	@Override
	public boolean action(Long bizId, Long wfId,  WorkflowUser user) {
		// TODO Auto-generated method stub
		return false;
	}

}
