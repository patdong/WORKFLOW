package cn.ideal.wfpf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.ideal.wf.model.Workflow;

@Mapper
public interface WorkflowMapper {

	int save(Workflow obj);
	
	int update(Workflow obj);
	
	List<Workflow> findAll();
	
	Workflow find(Long wfId);
	
	List<Workflow> findAPage(@Param("recordNumber") Long recordNumber,@Param("recordLastNumber") Long recordLastNumber,@Param("pageSize") Long  pageSize);
	
	List<Workflow> findAllBlindTable();
	
	int removeBinding(Workflow obj);
	
	void delete(Long wfId);
	
	void deleteTableElementOnNode(Long wfId);
	
}
