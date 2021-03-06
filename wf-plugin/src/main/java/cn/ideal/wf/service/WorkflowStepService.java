package cn.ideal.wf.service;

/**
 * 工作流办理人服务接口
 * @author 郭佟燕
 * @version 2.0
 */

import java.util.List;

import cn.ideal.wf.model.WorkflowStep;
import cn.ideal.wf.model.WorkflowUser;

public interface WorkflowStepService {

	/**
	 * 增加流程操作
	 */
	WorkflowStep addFlowStep(WorkflowStep workflowStep);
	
	/**
	 * 结束流程操作
	 */
	boolean endFlowStep(Long stepId, WorkflowUser wfu);
	
	/**
	 * 结束流程操作根据流程编号，并注入操作名称
	 */
	boolean endFlowSteps(Long flowId,String actionName, WorkflowUser wfu);
	
	/**
	 * 获得指定用户的操作流程
	 */
	WorkflowStep findWrokflowStep(Long bizId,Long wfId,WorkflowUser user);
	
	/**
	 * 获得指定用户的操作流程
	 */
	WorkflowStep findWrokflowStep(Long bizId, Long wfId, String nodeName);
	
	/**
	 * 获取指定业务的所有流程操作记录
	 */
	List<WorkflowStep> findAll(Long bizId);
	
	WorkflowStep find(Long stepId);
	
	List<WorkflowStep> findAllByFlowId(Long flowId);
	
	boolean setWorkflowStepUser(Long stepId, WorkflowUser user);
	
	List<WorkflowStep> findUNFinshedWrokflowStep(Long bizId, Long wfId);
	
	List<WorkflowStep> findDoingflowSteps(Long bizId, Long wfId);
	
	WorkflowStep findDoingflowStep(Long bizId, Long wfId, Long userId);

	boolean wakeFlowStep(Long stepId);
	
	boolean pushMsg(Long stepId, String reason);
	
	WorkflowStep findDoingflowStep(Long flowId,Long userId);
}
