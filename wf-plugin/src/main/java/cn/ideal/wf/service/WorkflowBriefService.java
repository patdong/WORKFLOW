package cn.ideal.wf.service;

/**
 * 工作流简述服务接口
 * @author 郭佟燕
 * @version 2.0
 */

import cn.ideal.wf.model.WorkflowBrief;


public interface WorkflowBriefService {
	
	/**
	 * 新增一条流程简介
	 */
	boolean addFlowBrief(WorkflowBrief wfb);
	
	/**
	 * 根据流程的变动更新流程简介
	 */
	boolean updateFlowBrief(WorkflowBrief wfb);	
	
	/**
	 * 结束流程简介
	 */
	boolean endFlowBrief(Long bizId);
	
	boolean endFlowBrief(Long bizId,String actionName);
	
	WorkflowBrief find(Long bizId);
	
	/**
	 * 
	 * 冻结当前办理人的操作
	 */
	boolean frozenFlowBrief(Long bizId);
	
	boolean unFrozenFlowBrief(Long bizId);
	
	WorkflowBrief findDoingFlow(Long bizId);
}
