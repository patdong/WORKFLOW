package cn.ideal.wf.service;

import java.util.List;

import cn.ideal.wf.model.CertificationOrg;
import cn.ideal.wf.model.CertificationRole;
import cn.ideal.wf.model.CertificationUser;
import cn.ideal.wf.model.User;

public interface CertificationService {
	User findUser(String username);

	List<CertificationUser> findUsers(String username) throws Exception;
	
	List<CertificationOrg> findOrgs(String orgname) throws Exception;
	
	List<CertificationRole> findRoles() throws Exception;
}
