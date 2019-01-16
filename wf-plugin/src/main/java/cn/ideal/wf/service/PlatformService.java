package cn.ideal.wf.service;

import java.util.List;

import cn.ideal.wf.model.WorkflowUser;

/**
 * 提供平台内数据获取接口
 * 流程服务提供接口，需要业务平台完成实现
 * @author 郭佟燕
 * @version 2.0
 *
 */
public interface PlatformService {

	/**
	 * 根据流程平台指定的角色编号+组织编号获取平台内对应角色的用户
	 * @param roleId
	 * @param orgId
	 * @return
	 */
	List<WorkflowUser> findUsersByRoleIdAndOrgId(Long roleId, Long orgId);
		
}
