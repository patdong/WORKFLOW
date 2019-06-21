package cn.ideal.wf.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cn.ideal.wf.model.WorkflowUser;

//@Service
public class PlatformServiceImpl implements PlatformService{
	
	
	@Override
	public List<WorkflowUser> findUsersByRoleIdAndOrgId(Long roleId, Long orgId,Long senderId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WorkflowUser getWorkflowUser(HttpServletRequest request) {
		
		return null;
	}

}
