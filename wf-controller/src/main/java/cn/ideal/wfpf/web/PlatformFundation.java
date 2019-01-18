package cn.ideal.wfpf.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;

import cn.ideal.wf.model.WorkflowUser;
import cn.ideal.wfpf.service.CertificationService;

public class PlatformFundation {
	@Autowired
	private CertificationService certificationService;
	
	/**
	 * 从业务平台获取流程平台间用户
	 * 根据实际业务平台做处理
	 * @param request
	 * @return
	 */
	public WorkflowUser getWorkflowUser(HttpServletRequest request){
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
