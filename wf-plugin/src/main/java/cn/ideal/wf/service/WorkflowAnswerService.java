package cn.ideal.wf.service;

import cn.ideal.wf.model.WorkflowAnswer;

/**
 * 应答接口
 * @author 郭佟燕
 * @version 2.0
 */

public interface WorkflowAnswerService {

	/**
	 * 分发
	 * @param wfa
	 * @return
	 */
	boolean dispense(WorkflowAnswer wfa);
	
	/**
	 * 应答
	 * @param wfa
	 * @return
	 */
	boolean response(WorkflowAnswer wfa);
}
