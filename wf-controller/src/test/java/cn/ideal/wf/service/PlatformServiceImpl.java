package cn.ideal.wf.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cn.ideal.wf.model.WorkflowUser;

//@Service
public class PlatformServiceImpl implements PlatformService{
	
	
	@Override
	public List<WorkflowUser> getUsersByRoleIdAndOrgId(Long roleId, Long orgId,Long senderId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WorkflowUser getWorkflowUser(HttpServletRequest request) {
		
		return null;
	}

	@Override
	public List<WorkflowUser> getWorkflowUsers(String userIds) {
		// TODO Auto-generated method stub
		return null;
	}

}
