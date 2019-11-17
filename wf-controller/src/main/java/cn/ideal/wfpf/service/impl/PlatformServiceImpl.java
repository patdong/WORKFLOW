package cn.ideal.wfpf.service.impl;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import cn.ideal.wf.model.WorkflowUser;
import cn.ideal.wf.service.PlatformService;
import cn.ideal.wfpf.service.CertificationService;

import com.alibaba.druid.util.StringUtils;

@Service
public class PlatformServiceImpl implements PlatformService{
	@Autowired
	private CertificationService certificationService;
	
	@Override
	public List<WorkflowUser> getUsersByRoleIdAndOrgId(Long roleId, Long orgId,Long creatorId) {
		//当角色所在的单位编码是9999时，则根据创建用户所在的单位查询角色
		if(creatorId != null){
			//ToDo 根据创建用户所在的单位进行查询
		}else{
			//todo 根据指定角色所在的单位进行查询
			
		}
		
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

	@Override
	public List<WorkflowUser> getWorkflowUsers(String userIds) {
		if(StringUtils.isEmpty(userIds)) return null;
		List<WorkflowUser> wfUsers = new LinkedList<WorkflowUser>();		
		for(String id : userIds.split(",")){
			if(StringUtils.isEmpty(id)) continue;
			WorkflowUser wfUser = new WorkflowUser();
			wfUser.setUserId(Long.parseLong(id));
			wfUser.setUserName("test");
			wfUser.setUnitId(1l);
            wfUser.setUnitName("平台");
            wfUsers.add(wfUser);
		}
		return wfUsers;
	}

}
