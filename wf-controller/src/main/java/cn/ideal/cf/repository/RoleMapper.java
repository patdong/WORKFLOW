package cn.ideal.cf.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.NoRepositoryBean;

import cn.ideal.cf.model.CFRole;

@Mapper
@NoRepositoryBean
public interface RoleMapper {

	List<CFRole> findRoles();
}
