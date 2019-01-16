package cn.ideal.wfpf.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import cn.ideal.wf.model.WorkflowUser;
import cn.ideal.wf.service.PlatformService;

@Service
public class PlatformServiceImpl implements PlatformService{

	@Override
	public List<WorkflowUser> findUsersByRoleIdAndOrgId(Long roleId, Long orgId) {
		// TODO Auto-generated method stub
		return null;
	}

}
