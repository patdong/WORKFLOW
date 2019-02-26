package cn.ideal.wf.processor;
import cn.ideal.wf.model.WorkflowNode;
/**
 * 业务平台处理
 * @author 郭佟燕
 * @version 2.0
 */
import cn.ideal.wf.model.WorkflowUser;

public interface Processor {

	/**
	 * 推进流程同时更新业务关联表的相关信息。业务主表不涉及。
	 * @param wfId
	 * @param bizId
	 * @param wfu
	 * @return
	 * @throws Exception
	 */
	boolean doAction(Long wfId, Long bizId, WorkflowUser wfu) throws Exception;
	
	/**
	 * 推进流程，根据传入的节点。
	 * @param wfId
	 * @param bizId
	 * @param wfu
	 * @param node
	 * @return
	 * @throws Exception
	 */
	boolean doAction(Long wfId, Long bizId, WorkflowUser wfu, WorkflowNode node) throws Exception;
	
	/**
	 * 推进流程，根据传入的节点和办理人
	 * @param wfId
	 * @param bizId
	 * @param wfu
	 * @param node
	 * @param nextWfu
	 * @return
	 * @throws Exception
	 */
	boolean doAction(Long wfId, Long bizId, WorkflowUser wfu, WorkflowNode node,WorkflowUser ...nextWfu) throws Exception;

	
	/**
	 * 流程按钮操作
	 * @param wfId
	 * @param bizId
	 * @param wfu
	 * @param buttonName
	 * @return
	 * @throws Exception
	 */
	boolean doButton(Long wfId, Long bizId, WorkflowUser wfu,String buttonName) throws Exception;
	
	String findNodeName(Long wfId,Long bizId);
	
}
