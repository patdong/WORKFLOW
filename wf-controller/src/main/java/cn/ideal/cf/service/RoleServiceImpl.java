package cn.ideal.cf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ideal.cf.model.CFRole;
import cn.ideal.cf.repository.RoleMapper;

@Service
public class RoleServiceImpl implements RoleService {
	@Autowired
	private RoleMapper roleMapper;
	public List<CFRole> findRoles() {
		return roleMapper.findRoles();
	}

}
