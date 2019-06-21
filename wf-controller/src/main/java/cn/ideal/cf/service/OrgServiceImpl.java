package cn.ideal.cf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ideal.cf.model.CFOrg;
import cn.ideal.cf.repository.OrgMapper;

@Service
public class OrgServiceImpl implements OrgService{
	
	@Autowired
	private OrgMapper orgMapper;
	
	public List<CFOrg> findOrgs(String orgname) {
		return orgMapper.findOrgs(orgname);
	}

}
