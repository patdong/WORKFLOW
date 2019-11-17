package cn.ideal.wf.service;

/**
 * 工作流流程服务接口
 * @author 郭佟燕
 * @version 2.0
 */
import java.util.List;

import cn.ideal.wf.model.WorkflowFlow;
import cn.ideal.wf.model.WorkflowUser;

public interface WorkflowFlowService {
		
	/**
	 * 启动流程
	 */
	WorkflowFlow startFlow(Long bizId,Long wfId, String nodeName) throws Exception;
	
	/**
	 * 启动流程同时创建启动的操作人
	 */
	WorkflowFlow startFlow(Long bizId,Long wfId,String nodeName,WorkflowUser user) throws Exception;
	
	/**
	 * 结束业务流程
	 */
	boolean endFlow(Long bizId,Long wfId,WorkflowUser user);
	
	/**
	 * 结束业务流程同时设置结束的动作
	 */
	boolean endFlow(Long bizId,Long wfId,String actionName,WorkflowUser wfu);
	
	/**
	 * 结束业务流程并创建新的流程及流程操作人
	 */	
	boolean endAndAddFlow(Long bizId,Long wfId, String nodeName,String actionName,WorkflowUser user, WorkflowUser...users) throws Exception;
	
	/**
	 * 获得未完成的流程
	 */
	WorkflowFlow findDoingFlow(Long bizId,Long wfId);
	
	/**
	 * 获得所有已完成的流程
	 */
	List<WorkflowFlow> findDongFlow(Long bizId,Long wfId);
	
	/**
	 * 获取指定业务的所有流程记录
	 */
	List<WorkflowFlow> findAll(Long bizId,Long wfId);
	
	/**
	 * 获取指定的流程记录
	 */
	WorkflowFlow findFlow(Long flowId);
	
	List<WorkflowUser> findWorkflowUsers(Long flowId);
	
	List<WorkflowFlow> findAllWithSteps(Long bizId,Long wfId);
	
	/**
	 * 获取前一个流程记录
	 * @param bizId
	 * @param wfId
	 * @return
	 */
	WorkflowFlow findPrevFlow(Long bizId, Long wfId);
	
	/**
	 * 获取发起人的流程记录
	 * @param bizId
	 * @param wfId
	 * @return
	 */
	WorkflowFlow findCreatorFlow(Long bizId, Long wfId);
	
	/**
	 * 根据流程节点获取流程记录
	 * @param bizId
	 * @param wfId
	 * @param nodeName
	 * @return
	 */
	List<WorkflowFlow> findWorkflowWithSteps(Long bizId,Long wfId,String nodeName);
	
}
