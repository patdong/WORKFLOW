package cn.ideal.wf.answeringaction;
/**
 * 传阅行为
 * @author 郭佟燕
 * @version 2.0
 */
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ideal.wf.action.Utils;
import cn.ideal.wf.common.WFConstants;
import cn.ideal.wf.model.WorkflowAnswer;
import cn.ideal.wf.model.WorkflowBrief;
import cn.ideal.wf.model.WorkflowUser;
import cn.ideal.wf.service.WorkflowAnswerService;
import cn.ideal.wf.service.WorkflowBriefService;

@Service("DispenseAction")
public class DispenseAction extends Utils implements Action{
	@Autowired
	private WorkflowBriefService workflowBriefService;
	@Autowired
	private WorkflowAnswerService workflowAnswerService;
	
	@Override
	public boolean dispense(Answer answer,WorkflowUser user, WorkflowUser... users) throws Exception {
		WorkflowAnswer wfa = new WorkflowAnswer();
		wfa.setTbId(answer.getTbId());
		wfa.setBizId(answer.getBizId());
		wfa.setWfId(answer.getWfId());
		wfa.setContent(answer.getContent());
		wfa.setCreatedDate(new Date());
		wfa.setDispenseUserId(user.getUserId());
		wfa.setReceiveUserIds(answer.getUserIds());
		wfa.setDispenseUserName(user.getUserName());			
		wfa.setType(answer.getType());
		switch(wfa.getType()){
		case WFConstants.WF_BUTTON_ACTION_FLOW:
			wfa.setTypeId(answer.getTypeId());
			if(wfa.getTypeId() == null) {
				WorkflowBrief wfb = workflowBriefService.find(answer.getBizId(), answer.getWfId());					
				if(wfb != null)wfa.setTypeId(wfb.getFlowId());
			}				
			break;
		case WFConstants.WF_BUTTON_ACTION_ANSWER:
			wfa.setTypeId(answer.getTypeId());
			break;
		}
		if(wfa.getTypeId() == null) throw new Exception("不能获取业务来源无法分发!");
		if(wfa.getReceiveUserIds() == null) throw new Exception("没有接收人无法分发!");
		wfa.setReceiveUserIds(","+wfa.getReceiveUserIds()+",");
		wfa.setReceiveUserNames(","+answer.getUserNames()+",");
		return workflowAnswerService.dispense(wfa);			
				
	}

	@Override
	public boolean response(Answer answer, WorkflowUser user) throws Exception {
		WorkflowAnswer wfa = new WorkflowAnswer();
		wfa.setContent(answer.getContent());
		wfa.setCreatedDate(new Date());
		wfa.setDispenseId(answer.getDispenseId());
		wfa.setReceiveUserId(user.getUserId());
		wfa.setReceiveUserName(user.getUserName());
		return workflowAnswerService.response(wfa);
	}
	
	
	
	

}
