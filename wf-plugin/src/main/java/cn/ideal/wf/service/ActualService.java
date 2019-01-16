package cn.ideal.wf.service;
/**
 * 业务平台处理
 * @author 郭佟燕
 * @version 2.0
 */
import cn.ideal.wf.model.WorkflowUser;

public interface ActualService {

	/**
	 * 推进流程同时更新业务关联表的相关信息。业务主表不涉及。
	 * @param wfId
	 * @param bizId
	 * @param wfu
	 * @return
	 * @throws Exception
	 */
	boolean flowPass(Long wfId, Long bizId, WorkflowUser wfu) throws Exception;
}
