package cn.ideal.cf.restful;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.ideal.cf.model.CFOrg;
import cn.ideal.cf.model.CFRole;
import cn.ideal.cf.model.CFUser;
import cn.ideal.cf.service.OrgService;
import cn.ideal.cf.service.RoleService;
import cn.ideal.cf.service.UserService;

@RestController
@RequestMapping("/certification")
public class CertificationController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private OrgService orgService;
	
	@Autowired
	private RoleService roleService;
	
	@GetMapping(value = "findusers")
    public List<CFUser> findUsers(@RequestParam("username") String userName) {		
        List<CFUser> users = this.userService.findUsers(userName);
        return users;
    }
	
	@GetMapping(value = "findorgs")
    public List<CFOrg> findOrgs(@RequestParam("orgname") String orgName) {		
        List<CFOrg> orgs = this.orgService.findOrgs(orgName);
        return orgs;
    }
	
	@GetMapping(value = "findroles")
    public List<CFRole> findRoles() {
        List<CFRole> roles = this.roleService.findRoles();
        return roles;
    }
}
