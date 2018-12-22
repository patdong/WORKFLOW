package cn.ideal.wf.service;

import java.util.List;

import cn.ideal.wf.model.WorkflowNode;
import cn.ideal.wf.model.WorkflowUser;

public interface WorkflowNodeService {

	/**
	 * 获得指定节点的下一个节点集
	 */
	List<WorkflowNode> findNextNodes(Long nodeId);
	
	/**
	 * 根据指定的节点名称和模块获得下一个节点集
	 */
	List<WorkflowNode> findNextNodes(String nodeName,Long moduleId);
	
	/**
	 * 获得指定节点的下一个操作人集
	 */
	List<WorkflowUser> findNodeUsers(Long nodeId); 
	
	/**
	 * 获得指定节点的下一个操作人集
	 */
	List<WorkflowUser> findNodeUsers(String nodeName); 
	
	/**
	 * 获得指定模块的按序节点集
	 */
	List<WorkflowNode> findSeqNodes(Long moduleId);
	
	/**
	 * 获得指定业务模块的按序节点办理状态集合
	 */
	List<WorkflowNode> findSeqNodes(Long moduleId,Long bizId);
	
	WorkflowNode save(WorkflowNode node);

}
