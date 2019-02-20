package cn.ideal.wf.action;
/**
 * 终止行为
 * @author 郭佟燕
 * @version 2.0
 *
 */
import org.springframework.stereotype.Service;

import cn.ideal.wf.model.WorkflowUser;

@Service("TerminateAction")
public class TerminateAction implements Action {

	@Override
	public boolean action(Long bizId, Long wfId, WorkflowUser user ,WorkflowUser ...users) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean action(Long bizId, Long wfId,  WorkflowUser user) {
		// TODO Auto-generated method stub
		return false;
	}

}
