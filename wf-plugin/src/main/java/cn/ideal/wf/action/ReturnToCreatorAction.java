package cn.ideal.wf.action;
/**
 * 退回到业务发起人行为
 * @author 郭佟燕
 * @version 2.0
 *
 */
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ideal.wf.common.WFConstants;
import cn.ideal.wf.model.WorkflowFlow;
import cn.ideal.wf.model.WorkflowTableSummary;
import cn.ideal.wf.model.WorkflowUser;
import cn.ideal.wf.service.WorkflowFlowService;
import cn.ideal.wf.service.WorkflowNodeService;
import cn.ideal.wf.service.WorkflowTableService;

@Service("ReturnToCreatorAction")
public class ReturnToCreatorAction extends Utils implements Action {
	@Autowired
    private WorkflowFlowService workflowFlowService;
	@Autowired
    private WorkflowTableService workflowTableService;
	@Autowired
    private WorkflowNodeService workflowNodeService;
	
	@Override
	public boolean action(Long bizId, Long wfId,  WorkflowUser user, WorkflowUser ...users) throws Exception {
		boolean res = true;
		WorkflowFlow wff = workflowFlowService.findCreatorFlow(bizId, wfId);
		
		if(users == null ||users.length == 0){
			List<WorkflowUser> wfuLst = workflowFlowService.findWorkflowUsers(wff.getFlowId());
			users = wfuLst.toArray(new WorkflowUser[wfuLst.size()]);
		}		
		res = workflowFlowService.endAndAddFlow(bizId,wfId,WFConstants.WF_NODE_START,WFConstants.WF_ACTION_RETURN,user,users);
		if(res){
			String curUserName = "";
			for(WorkflowUser item : users) curUserName += item.getUserName() + ",";	
			WorkflowTableSummary wfts = new WorkflowTableSummary();	
			if(users.length > 0) wfts.setCurUserId(","+users[0].getUserId()+",");
			wfts.setCurUserName(","+curUserName);
			wfts.setModifiedDate(new Date());
			//任何动作都反应在action字段上
			wfts.setAction("退回"+wff.getNodeName());	
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
