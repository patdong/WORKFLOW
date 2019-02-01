package cn.ideal.wf.action;

import org.springframework.stereotype.Service;

import cn.ideal.wf.model.WorkflowUser;

/**
 * 暂缓行为
 * @author 郭佟燕
 * @version 2.0
 *
 */
@Service("PostPhoneAction")
public class PostPhoneAction implements Action {

	@Override
	public boolean action(Long bizId, Long wfId,  WorkflowUser user , WorkflowUser ...users) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean action(Long bizId, Long wfId,  WorkflowUser user) {
		// TODO Auto-generated method stub
		return false;
	}

}
