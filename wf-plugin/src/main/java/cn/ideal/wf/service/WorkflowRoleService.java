package cn.ideal.wf.service;

/**
 * 获取流程节点办理人员集合
 * @author 郭佟燕
 * @version 2.0
 */
import java.util.List;

import cn.ideal.wf.model.WorkflowUser;

public interface WorkflowRoleService {
	
	List<WorkflowUser> findUsers(Long roleId);
}
