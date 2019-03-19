package cn.ideal.wfpf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import cn.ideal.wfpf.model.SysConfiguration;

@Mapper
public interface SysMapper {
	
	List<SysConfiguration> findAll();
	
}
