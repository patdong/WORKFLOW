package cn.ideal.wf.action;
/**
 * 推进流程行为
 * @author 郭佟燕
 * @version 2.0
 */
import cn.ideal.wf.model.WorkflowUser;

public class PassAction implements Action {

	@Override
	public boolean action(Long bizId, WorkflowUser user, WorkflowUser... users) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean action(Long bizId, WorkflowUser user) {
		// TODO Auto-generated method stub
		return false;
	}

}
