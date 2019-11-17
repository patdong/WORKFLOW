package cn.ideal.wf.action;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ideal.wf.common.WFConstants;
import cn.ideal.wf.model.WorkflowNode;
import cn.ideal.wf.model.WorkflowTableSummary;
/**
 * 自动返回发起人
 * @author 郭佟燕
 * @version 2.0
 *
 */
import cn.ideal.wf.model.WorkflowUser;
import cn.ideal.wf.service.WorkflowFlowService;
import cn.ideal.wf.service.WorkflowStepService;
import cn.ideal.wf.service.WorkflowTableService;

@Service("AutoBackAction")
public class AutoBackAction extends Utils implements Action{
	@Autowired
    private WorkflowFlowService workflowFlowService;
	@Autowired
    private WorkflowStepService workflowStepService;
	@Autowired
    private WorkflowTableService workflowTableService;
	
	@Override
	public boolean action(Long bizId, Long wfId, WorkflowUser user,WorkflowUser... users) throws Exception {		
		boolean res = true;
		WorkflowNode nextNode = this.findNextNode(wfId,bizId);
		if(nextNode == null) {
			res =this.endFlow(bizId,wfId,user);
			if(!res) throw new Exception("AutoBackActon-流程自动返回失败");	
			return true;
		}
							
		List<WorkflowUser> wfuLst =  this.findUsersForNode(nextNode,bizId,wfId);
		users = wfuLst.toArray(new WorkflowUser[wfuLst.size()]);
	
		res = workflowFlowService.endAndAddFlow(bizId,wfId,nextNode.getNodeName(),WFConstants.WF_ACTION_DOING,user,users);
		if(res){
			WorkflowTableSummary wfts = new WorkflowTableSummary();			
			wfts = this.setCurrentUserForTableSummary(wfts, nextNode, users);
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
	public boolean action(Long bizId, Long wfId, WorkflowUser user) throws Exception {		
		return false;
	}

}
