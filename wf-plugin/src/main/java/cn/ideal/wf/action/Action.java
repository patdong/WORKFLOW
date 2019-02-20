package cn.ideal.wf.action;

import cn.ideal.wf.model.WorkflowUser;

/**
 * 节点行为接口
 * @author 郭佟燕
 * @version 2.0
 *
 */
public interface Action {

	boolean action(Long bizId, Long wfId, WorkflowUser user, WorkflowUser ...users) throws Exception;
	
	boolean action(Long bizId, Long wfId, WorkflowUser user) throws Exception;
}
