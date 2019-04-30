package cn.ideal.wf.action;
/**
 * 流程办结行为
 * @author 郭佟燕
 * @version 2.0
 *
 */
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ideal.wf.common.WFConstants;
import cn.ideal.wf.model.WorkflowTableSummary;
import cn.ideal.wf.model.WorkflowUser;
import cn.ideal.wf.service.WorkflowFlowService;
import cn.ideal.wf.service.WorkflowTableService;

@Service("EndAction")
public class EndAction implements Action {
	@Autowired
    private WorkflowFlowService workflowFlowService;
	@Autowired
    private WorkflowTableService workflowTableService;
	
	@Override
	public boolean action(Long bizId, Long wfId,  WorkflowUser user ,WorkflowUser ...users) throws Exception{
		boolean res = workflowFlowService.endFlow(bizId,wfId,user);
		if(res){
			WorkflowTableSummary wfts = new WorkflowTableSummary();	
			wfts.setModifiedDate(new Date());
			//任何动作都反应在action字段上
			wfts.setAction(WFConstants.WF_ACTION_END);
			wfts.setFinishedDate(new Date());
			wfts.setBizId(bizId);
			wfts.setWfId(wfId);
			res = workflowTableService.synchTableSummary(wfts);
			return res;
		}
		return false;
	}

	@Override
	public boolean action(Long bizId, Long wfId,  WorkflowUser user) throws Exception {
		if(bizId == null) throw new Exception("无效业务");		
		workflowFlowService.endFlow(bizId,wfId,user);
		
		return false;
	}

}
