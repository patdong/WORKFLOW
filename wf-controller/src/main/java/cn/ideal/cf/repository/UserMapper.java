package cn.ideal.cf.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.NoRepositoryBean;

import cn.ideal.cf.model.CFUser;


@Mapper
@NoRepositoryBean
public interface UserMapper {

	List<CFUser> findUsers(String userName);
	
	List<CFUser> findUsersWithPassword(String userName);
}
