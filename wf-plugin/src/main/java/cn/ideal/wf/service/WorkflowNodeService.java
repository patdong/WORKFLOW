package cn.ideal.wf.service;

/**
 * 工作流节点服务接口
 * @author 郭佟燕
 * @version 2.0
 */
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
	List<WorkflowNode> findNextNodes(String nodeName,Long wfId);
	
	/**
	 * 获得指定节点的下一个操作人集
	 */
	List<WorkflowUser> findNodeUsers(Long nodeId); 
	
	/**
	 * 获得指定节点的下一个操作人集
	 */
	List<WorkflowUser> findNodeUsers(String nodeName); 
	
	
	WorkflowNode save(WorkflowNode node);
	
	List<WorkflowNode> findAll(Long wfId);
	
	WorkflowNode[][] getTreeNodes(Long wfId,Long bizId);

	WorkflowNode[][] getTreeNodes(Long wfId);
}
