package cn.ideal.wfpf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.ideal.wfpf.model.WFPFWorkflow;

@Mapper
public interface WorkflowMapper {

	int save(WFPFWorkflow obj);
	
	int update(WFPFWorkflow obj);
	
	List<WFPFWorkflow> findAll();
	
	WFPFWorkflow find(Long wfId);
	
	List<WFPFWorkflow> findAPage(@Param("recordNumber") Long recordNumber,@Param("recordLastNumber") Long recordLastNumber,@Param("pageSize") Long  pageSize);
	
	List<WFPFWorkflow> findAllBlindTable();
	
	int removeBinding(Long wfId);
	
	void delete(Long wfId);
	
	void deleteTableElementOnNode(Long wfId);
	
	List<WFPFWorkflow> findByWFName(@Param("wfId") Long wfId,@Param("wfName") String wfName);
}
