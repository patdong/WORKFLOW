package cn.ideal.wf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import cn.ideal.wf.model.Workflow;

@Mapper
public interface WorkflowWFMapper {
	
	Workflow find(Long key);
		
	List<Workflow> findHavingBindTable();

}
