package cn.ideal.wf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.ideal.wf.model.WorkflowTableBrief;
import cn.ideal.wf.model.WorkflowTableElement;
import cn.ideal.wf.model.WorkflowTableLayout;
import cn.ideal.wf.model.WorkflowTableSummary;

@Mapper
public interface WorkflowTableMapper {
	
	WorkflowTableBrief find(Long tbId);	
	
	List<WorkflowTableBrief> findAll();
	
	List<WorkflowTableElement> findAllTableElements(@Param("tbId") Long tbId,@Param("scope") String scope);
	
	List<WorkflowTableElement> findElementsOnList(Long tbId);
	
	/**
	 * 更新业务附表信息
	 * @param wfts
	 * @return
	 */
	int synchTableSummary(WorkflowTableSummary wfts);
	
	/**
	 * 结束业务附表
	 * @param wfts
	 * @return
	 */
	int endTableSummary(WorkflowTableSummary wfts);
	
	/**
	 * 获得子表
	 * @param tbId
	 * @param scope
	 * @return
	 */
	WorkflowTableBrief findSubTable(@Param("tbId") Long tbId,@Param("scope") String scope);
	
	/**
	 * 获得对应区域的所有字段
	 * @param tbId
	 * @param scope
	 * @return
	 */
	List<WorkflowTableElement> findTableAllElements(@Param("tbId") Long tbId,@Param("scope") String scope);
	
	WorkflowTableLayout findTableLayoutWithScope(@Param("tbId") Long tbId,@Param("scope") String scope);
	
	List<WorkflowTableLayout> findTableLayout(Long tbId);
}
