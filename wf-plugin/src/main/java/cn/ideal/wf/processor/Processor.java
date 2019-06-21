package cn.ideal.wf.processor;
/**
 * 业务平台处理
 * @author 郭佟燕
 * @version 2.0
 */
import cn.ideal.wf.model.WorkflowUser;

public interface Processor {

	/**
	 * 推进流程同时更新业务关联表的相关信息。业务主表不涉及。
	 * @param tbId
	 * @param bizId
	 * @param wfu
	 * @return
	 * @throws Exception
	 */
	boolean doAction(Long tbId, Long bizId, WorkflowUser wfu) throws Exception;
	
	/**
	 * 推进流程，根据传入的节点。
	 * @param tbId
	 * @param bizId
	 * @param wfu
	 * @param node
	 * @return
	 * @throws Exception
	 */
	boolean doAction(Long tbId, Long bizId, WorkflowUser wfu, Long nodeId) throws Exception;
	
	/**
	 * 推进流程，根据传入的节点和办理人
	 * @param tbId
	 * @param bizId
	 * @param wfu
	 * @param node
	 * @param nextWfu
	 * @return
	 * @throws Exception
	 */
	boolean doAction(Long tbId, Long bizId, WorkflowUser wfu, Long nodeId,WorkflowUser ...nextWfu) throws Exception;

	
	/**
	 * 流程按钮操作
	 * @param tbId
	 * @param bizId
	 * @param wfu
	 * @param buttonName
	 * @return
	 * @throws Exception
	 */
	boolean doButton(Long tbId, Long bizId, WorkflowUser wfu,String buttonName) throws Exception;
	
	String findNodeName(Long tbId,Long bizId,WorkflowUser wfu);
	
}
