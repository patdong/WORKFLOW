package cn.ideal.wfpf.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import cn.ideal.cf.service.OrgService;
import cn.ideal.cf.service.RoleService;
import cn.ideal.cf.service.UserService;
import cn.ideal.wfpf.model.CertificationOrg;
import cn.ideal.wfpf.model.CertificationRole;
import cn.ideal.wfpf.model.CertificationUser;
import cn.ideal.wfpf.model.Role;
import cn.ideal.wfpf.model.User;
import cn.ideal.wfpf.service.CertificationService;

@Service
public class CertificationServiceImpl implements CertificationService {
	@Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
	@Autowired
	private OrgService orgService;
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	
	@Value("${restful.url}")
	private String restfulurl;
	@Override
	public User findUser(String username) {
		List<cn.ideal.cf.model.CFUser> users = userService.findUsersWithPassword(username);
		if(users != null && users.size()>0) {
			cn.ideal.cf.model.CFUser cfuser = users.get(0);
			User user = new User();
			
			user.setId(cfuser.getUserId());
			user.setUsername(username);
			user.setPassword(cfuser.getPassword());				
			Role role = new Role();
			role.setId(1l);
			role.setName("ROLE_ADMIN");
			Set<Role> roles = new HashSet<Role>();
			roles.add(role);
			user.setRoles(roles);
			return user;
		}else{
			User user = new User();
			
			user.setId(1l);
			user.setUsername("admin");
			user.setPassword("p@ssw0rd");
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			//user.setPassword(new ShaPasswordEncoder().encodePassword(user.getPassword(), null));
			Role role = new Role();
			role.setId(1l);
			role.setName("ROLE_ADMIN");
			Set<Role> roles = new HashSet<Role>();
			roles.add(role);
			user.setRoles(roles);
			return user;
		}
		
	}

	@Override
	public List<CertificationUser> findUsers(String username) throws Exception{		
		
		List<cn.ideal.cf.model.CFUser> users = userService.findUsers(username);
		List<CertificationUser> cfusers = new ArrayList<CertificationUser>();
		for(cn.ideal.cf.model.CFUser user : users){
			CertificationUser cfuser = new CertificationUser();
			cfuser.setUserId(user.getUserId());
			cfuser.setUserName(user.getRealName());
			cfuser.setCurrentOrgName(user.getCurrentOrgName());
			cfuser.setOrgName(user.getOrgName());
			cfuser.setCurrentOrgId(user.getCurrentOrgId());
        	cfusers.add(cfuser);
		}
		if(cfusers.size() == 0){
			CertificationUser cfuser = new CertificationUser();
			cfuser.setUserId(1l);
			cfuser.setUserName("admin");
			cfuser.setCurrentOrgName("平台");
			cfuser.setOrgName("平台");
			cfuser.setCurrentOrgId(1l);
        	cfusers.add(cfuser);
		}
		/*
		String url = restfulurl+"/certification/findusers?username=";
		String params = URLEncoder.encode(username,"UTF-8");
		URL certification = new URL(url+params);
        URLConnection cf = certification.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(cf.getInputStream()));
        String inputLine;
        List<CertificationUser> users = new ArrayList<CertificationUser>();
        while ((inputLine = in.readLine()) != null) {        	  	
        	ObjectMapper mapper = new ObjectMapper();
        	List<Map<String,Object>> lst = mapper.readValue(inputLine,new TypeReference<List<Map<String,Object>>>(){});
        	for(Map<String,Object> element : lst){
        		CertificationUser user = new CertificationUser();
	        	user.setUserId(new Long((Integer)element.get("userId")));
	        	user.setUserName((String)element.get("realName"));
	        	user.setCurrentOrgName((String)element.get("currentOrgName"));
	        	user.setOrgName((String)element.get("orgName"));
	        	user.setCurrentOrgId(new Long((Integer)element.get("currentOrgId")));
	        	users.add(user);
        	}
        	
        }
            
        in.close();
		*/
		return cfusers;
	}

	@Override
	public List<CertificationOrg> findOrgs(String orgname) throws Exception {
		
		List<cn.ideal.cf.model.CFOrg> orgs = orgService.findOrgs(orgname);
		List<CertificationOrg> cforgs = new ArrayList<CertificationOrg>();
		for(cn.ideal.cf.model.CFOrg org : orgs){
			CertificationOrg cforg = new CertificationOrg();
			cforg.setOrgId(org.getOrgId());        		
			cforg.setCurrentOrgName(org.getCurrentOrgName());
			cforg.setOrgName(org.getOrgName());
        	cforgs.add(cforg);
		}
		
		/*
		String url = restfulurl+"/certification/findorgs?orgname=";
		String params = URLEncoder.encode(orgname,"UTF-8");
		URL certification = new URL(url+params);
        URLConnection cf = certification.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(cf.getInputStream()));
        String inputLine;
        List<CertificationOrg> orgs = new ArrayList<CertificationOrg>();
        while ((inputLine = in.readLine()) != null) {        	  	
        	ObjectMapper mapper = new ObjectMapper();
        	List<Map<String,Object>> lst = mapper.readValue(inputLine,new TypeReference<List<Map<String,Object>>>(){});
        	for(Map<String,Object> element : lst){
        		CertificationOrg org = new CertificationOrg();
        		org.setOrgId(new Long((Integer)element.get("orgId")));        		
        		org.setCurrentOrgName((String)element.get("currentOrgName"));
        		org.setOrgName((String)element.get("orgName"));
	        	orgs.add(org);
        	}
        	
        }
            
        in.close();
		*/
		return cforgs;
	}

	@Override
	public List<CertificationRole> findRoles() throws Exception {
		List<cn.ideal.cf.model.CFRole> roles = roleService.findRoles();
		List<CertificationRole> cfroles = new ArrayList<CertificationRole>();
		for(cn.ideal.cf.model.CFRole role :roles){
			CertificationRole cfrole = new CertificationRole();
			cfrole.setRoleId(role.getRoleId());        		
			cfrole.setRoleName(role.getRoleName());        		
    		cfroles.add(cfrole);
		}
		
		
		
		/*
		String url = restfulurl+"/certification/findroles";		
		URL certification = new URL(url);
        URLConnection cf = certification.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(cf.getInputStream()));
        String inputLine;
        List<CertificationRole> roles = new ArrayList<CertificationRole>();
        while ((inputLine = in.readLine()) != null) {        	  	
        	ObjectMapper mapper = new ObjectMapper();
        	List<Map<String,Object>> lst = mapper.readValue(inputLine,new TypeReference<List<Map<String,Object>>>(){});
        	for(Map<String,Object> element : lst){
        		CertificationRole role = new CertificationRole();
        		role.setRoleId(new Long((Integer)element.get("roleId")));        		
        		role.setRoleName((String)element.get("roleName"));        		
        		roles.add(role);
        	}
        	
        }
            
        in.close();
		*/
		
		return cfroles;
	}

}
