package cn.ideal.wf.processor;
import java.util.List;

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
	boolean flowPass(Long wfId, Long bizId, WorkflowUser wfu) throws Exception;
	
	/**
	 * 推进流程，根据传入的节点。
	 * @param wfId
	 * @param bizId
	 * @param wfu
	 * @param node
	 * @return
	 * @throws Exception
	 */
	boolean flowPass(Long wfId, Long bizId, WorkflowUser wfu, WorkflowNode node) throws Exception;
	
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
	boolean flowPass(Long wfId, Long bizId, WorkflowUser wfu, WorkflowNode node,WorkflowUser ...nextWfu) throws Exception;
	
	/**
	 * 人工干预流程办理
	 * @param wfId
	 * @param bizId
	 * @param wfu
	 * @param action
	 * @return
	 * @throws Exception
	 */
	boolean actionPass(Long wfId, Long bizId, WorkflowUser wfu, String action) throws Exception;
	
	/**
	 * 人工干预流程到指定的人
	 * @param wfId
	 * @param bizId
	 * @param wfu
	 * @param action
	 * @param users
	 * @return
	 * @throws Exception
	 */
	boolean actionPass(Long wfId, Long bizId, WorkflowUser wfu, String action, WorkflowUser ...users) throws Exception;
	/**
	 * 获取指定节点的办理人
	 * @param node
	 * @return
	 * @throws Exception
	 */
	List<WorkflowUser> findUsersForNode(WorkflowNode node) throws Exception;
	
	WorkflowNode findNode(Long wfId,Long bizId);
	
}
