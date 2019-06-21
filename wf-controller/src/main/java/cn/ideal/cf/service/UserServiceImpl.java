package cn.ideal.cf.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ideal.cf.model.CFUser;
import cn.ideal.cf.repository.UserMapper;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserMapper userMapper;
	
	public List<CFUser> findUsers(String userName) {
		return userMapper.findUsers(userName);
	}

	@Override
	public List<CFUser> findUsersWithPassword(String userName) {
		return userMapper.findUsersWithPassword(userName);
	}

}
