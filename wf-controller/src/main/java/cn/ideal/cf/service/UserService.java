package cn.ideal.cf.service;

import java.util.List;

import cn.ideal.cf.model.CFUser;

public interface UserService {

	List<CFUser> findUsers(String username);
	
	List<CFUser> findUsersWithPassword(String username);
}
