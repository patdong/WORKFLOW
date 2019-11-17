package cn.ideal.wf.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.ideal.wf.model.WorkflowTableBrief;
import cn.ideal.wf.model.WorkflowTableElement;
import cn.ideal.wf.model.WorkflowTableLayout;
import cn.ideal.wf.model.WorkflowTableSummary;
import cn.ideal.wf.model.WorkflowTableUserDefination;

@Mapper
public interface WorkflowTableMapper {
	
	WorkflowTableBrief find(Long tbId);	
	
	List<WorkflowTableBrief> findAll();
	
	List<WorkflowTableElement> findTableFields(@Param("tbId") Long tbId);
	
	List<String> findTableFieldNames(@Param("tbId") Long tbId);
	
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
	 * 获得业务附表信息
	 * @param summaryId
	 * @return
	 */
	WorkflowTableSummary findTableSummary(@Param("tbId") Long tbId, @Param("bizId") Long bizId);
	
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
	
	List<WorkflowTableElement> findTableAllElementsWithWorkflow(@Param("tbId") Long tbId,@Param("scope") String scope,@Param("wfId") Long wfId,@Param("nodeName") String nodeName);
	
	WorkflowTableLayout findTableLayoutWithScope(@Param("tbId") Long tbId,@Param("scope") String scope);
	
	List<WorkflowTableLayout> findTableLayout(Long tbId);	
	
	List<WorkflowTableBrief> findAllSubTables(Long tbId); 
	
	List<WorkflowTableBrief> findAllBlindTable();
	
	/**
	 * 获得被业务分类的表单
	 * @return
	 */
	List<WorkflowTableBrief> findAllSortedTable();
	
	List<Map<String,Object>> findAllSortedTableWithBizCountByCreatedUser(Long userId);
	
	int saveTableUserDefination(WorkflowTableUserDefination def);
	
	int updateTableUserDefination(WorkflowTableUserDefination def);
	
	/**
	 * 获得用户自定义的表单信息
	 * @param tbId
	 * @param wfId
	 * @return
	 */
	WorkflowTableBrief findDefinationTableBrief(@Param("tbId") Long tbId,@Param("wfId") Long wfId);
	
	WorkflowTableBrief findDefinationTableBriefByDefId(@Param("defId") Long defId);
	
	WorkflowTableBrief findByIds(@Param("tbId") Long tbId,@Param("wfId") Long wfId);
	
	/**
	 * 获取指定用户定义的自定义业务表单信息
	 * @param userId
	 * @param type
	 * @return
	 */
	List<WorkflowTableUserDefination> findDefinations(@Param("userId") Long userId, @Param("type") String type, @Param("tbId") Long tbId);
	
	WorkflowTableUserDefination findDefination(Long defId);
	
	int deleteTableSummary(Long summaryId);
	
	int updateCurrentUser(WorkflowTableSummary wfts);
	
	int setAuthority(List<Map<String,Object>> auth);
	
	void deleteAuthority(Long userId);
	
	List<Map<String,Object>> findAuthorities();
	
	Map<String,Object> findAuthority(Long userId);
	
	List<Map<String,Object>> findTableSimpleElementsForMobile(@Param("tbId") Long tbId,@Param("wfId") Long wfId, @Param("nodeName") String nodeName);
	
	List<Map<String,Object>> findTableSimpleElementsWithValueForMobile(@Param("tbId") Long tbId, @Param("bizId") Long bizId);
}
