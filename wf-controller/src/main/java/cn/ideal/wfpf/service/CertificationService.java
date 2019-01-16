package cn.ideal.wfpf.service;

import java.util.List;

import cn.ideal.wfpf.model.CertificationOrg;
import cn.ideal.wfpf.model.CertificationRole;
import cn.ideal.wfpf.model.CertificationUser;
import cn.ideal.wfpf.model.User;

public interface CertificationService {
	User findUser(String username);

	List<CertificationUser> findUsers(String username) throws Exception;
	
	List<CertificationOrg> findOrgs(String orgname) throws Exception;
	
	List<CertificationRole> findRoles() throws Exception;
}
