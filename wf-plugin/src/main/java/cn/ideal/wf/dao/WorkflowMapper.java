package cn.ideal.wf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.ideal.wf.model.Workflow;

@Mapper
public interface WorkflowMapper {

	int save(Workflow obj);
	
	int update(Workflow obj);
	
	List<Workflow> findAll();
	
	Workflow find(Long key);
	
	List<Workflow> findAPage(@Param("recordNumber") Long recordNumber,@Param("pageSize") Long  pageSize);
}
