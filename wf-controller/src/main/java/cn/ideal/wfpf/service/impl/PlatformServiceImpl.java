package cn.ideal.wfpf.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import cn.ideal.wf.model.WorkflowUser;
import cn.ideal.wf.service.PlatformService;
import cn.ideal.wfpf.service.CertificationService;

@Service
public class PlatformServiceImpl implements PlatformService{
	@Autowired
	private CertificationService certificationService;
	
	@Override
	public List<WorkflowUser> findUsersByRoleIdAndOrgId(Long roleId, Long orgId,Long senderId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WorkflowUser getWorkflowUser(HttpServletRequest request) {
		if(request == null) return null;
		WorkflowUser wfUser = new WorkflowUser();
		Object principal = request.getUserPrincipal();
		if (principal instanceof UsernamePasswordAuthenticationToken) {
            User user = (User)((UsernamePasswordAuthenticationToken) principal).getPrincipal();
            cn.ideal.wfpf.model.User pfUser = certificationService.findUser(user.getUsername());
            
            wfUser.setUserId(pfUser.getId());
            wfUser.setUserName(pfUser.getUsername());  
            wfUser.setUnitId(1l);
            wfUser.setUnitName("平台");
                 
        }
		
		return wfUser;
	}

}
