package cn.ideal.wf.answeringaction;

import cn.ideal.wf.model.WorkflowUser;

/**
 * 应答行为接口
 * @author 郭佟燕
 * @version 2.0
 *
 */
public interface Action {

	/**
	 * 根据不同的业务编号分发
	 * @param user
	 * @param answer
	 * @param users
	 * @return
	 * @throws Exception
	 */
	boolean dispense(Answer answer,WorkflowUser user, WorkflowUser ...users) throws Exception;
	
	/**
	 * 根据分发编号应答
	 * @param user
	 * @param answer
	 * @return
	 * @throws Exception
	 */
	boolean response(Answer answer, WorkflowUser user)throws Exception;
}
