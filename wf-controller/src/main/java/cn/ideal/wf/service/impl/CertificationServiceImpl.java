package cn.ideal.wf.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import cn.ideal.wf.model.CertificationOrg;
import cn.ideal.wf.model.CertificationRole;
import cn.ideal.wf.model.CertificationUser;
import cn.ideal.wf.model.Role;
import cn.ideal.wf.model.User;
import cn.ideal.wf.service.CertificationService;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CertificationServiceImpl implements CertificationService {
	@Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public User findUser(String username) {
		User user = new User();
		user.setId(1l);
		user.setUsername("admin");
		user.setPassword("1");
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		Role role = new Role();
		role.setId(1l);
		role.setName("ROLE_ADMIN");
		Set<Role> roles = new HashSet<Role>();
		roles.add(role);
		user.setRoles(roles);
		return user;
	}

	@Override
	public List<CertificationUser> findUsers(String username) throws Exception{		
		String url = "http://localhost:8081/certification/findusers?username=";
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
	        	users.add(user);
        	}
        	
        }
            
        in.close();

		return users;
	}

	@Override
	public List<CertificationOrg> findOrgs(String orgname) throws Exception {
		String url = "http://localhost:8081/certification/findorgs?orgname=";
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

		return orgs;
	}

	@Override
	public List<CertificationRole> findRoles() throws Exception {
		String url = "http://localhost:8081/certification/findroles";		
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

		return roles;
	}

}
