package cn.ideal.wf.processor;
/**
 * 业务平台处理
 * @author 郭佟燕
 * @version 2.0
 */
import java.util.Map;

import cn.ideal.wf.answeringaction.Answer;
import cn.ideal.wf.model.WorkflowUser;
import cn.ideal.wf.notification.Notifier;

public interface Processor {

	/**
	 * 推进流程同时更新业务关联表的相关信息。业务主表不涉及。
	 * @param tbId
	 * @param bizId
	 * @param wfu
	 * @return
	 * @throws Exception
	 */
	boolean doAction(Long tbId, Long bizId, Long wfId,WorkflowUser wfu, Notifier notifier) throws Exception;
	
	/**
	 * 推进流程，根据传入的节点。
	 * @param tbId
	 * @param bizId
	 * @param wfu
	 * @param node
	 * @return
	 * @throws Exception
	 */
	boolean doAction(Long tbId, Long bizId, Long wfId, WorkflowUser wfu, Long nodeId, Notifier notifier) throws Exception;
	
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
	boolean doAction(Long tbId, Long bizId, Long wfId, WorkflowUser wfu, Long nodeId, Notifier notifier,WorkflowUser ...nextWfu) throws Exception;

	
	/**
	 * 流程按钮操作
	 * @param tbId
	 * @param bizId
	 * @param wfu
	 * @param buttonName
	 * @return
	 * @throws Exception
	 */
	boolean doButton(Long tbId, Long bizId, Long wfId, WorkflowUser wfu,String buttonName, Notifier notifier) throws Exception;
	
	/**
	 * 流程按钮消息操作
	 * @param tbId
	 * @param bizId
	 * @param wfu
	 * @param buttonName
	 * @param msg
	 * @param notifier
	 * @return
	 * @throws Exception
	 */
	boolean doButton(Long tbId, Long bizId, Long wfId, WorkflowUser wfu,String buttonName, String msg, Notifier notifier) throws Exception;
	/**
	 * 流程上辅助应答按钮操作，不推进流程
	 * @param answer  应答实体
	 * @param wfu     操作人
	 * @param mode    应答类型
	 * @return
	 * @throws Exception
	 */
	boolean doAnswer(Answer answer, WorkflowUser wfu,String buttonName, String mode) throws Exception;
	
	Map<String,String> findCurrentNode(Long tbId,Long bizId,WorkflowUser wfu);
	
}
