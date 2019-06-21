package cn.ideal.cf.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.NoRepositoryBean;

import cn.ideal.cf.model.CFOrg;

@Mapper
@NoRepositoryBean
public interface OrgMapper {
	
	List<CFOrg> findOrgs(String orgName);
}
