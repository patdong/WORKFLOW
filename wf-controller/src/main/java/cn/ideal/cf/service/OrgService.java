package cn.ideal.cf.service;

import java.util.List;

import cn.ideal.cf.model.CFOrg;

public interface OrgService {
	
	List<CFOrg> findOrgs(String orgname);
}
