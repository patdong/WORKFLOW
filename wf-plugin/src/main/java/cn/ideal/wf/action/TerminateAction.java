package cn.ideal.wf.action;

import org.springframework.stereotype.Service;

import cn.ideal.wf.model.WorkflowUser;

/**
 * 终止行为
 * @author 郭佟燕
 * @version 2.0
 *
 */
@Service("TerminateAction")
public class TerminateAction implements Action {

	@Override
	public boolean action(Long bizId, WorkflowUser user , WorkflowUser ...users) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean action(Long bizId, WorkflowUser user) {
		// TODO Auto-generated method stub
		return false;
	}

}
