package cn.ideal.wf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.ideal.wf.model.WorkflowTableBrief;
import cn.ideal.wf.model.WorkflowTableElement;

@Mapper
public interface WorkflowTableMapper {
	
	WorkflowTableBrief find(Long tbId);	
	
	List<WorkflowTableBrief> findAll();
	
	List<WorkflowTableElement> findAllTableElements(@Param("tbId") Long tbId,@Param("scope") String scope);
	
	List<WorkflowTableElement> findElementsOnList(Long tbId);
}
