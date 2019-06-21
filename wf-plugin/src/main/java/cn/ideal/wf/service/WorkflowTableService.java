package cn.ideal.wf.service;

/**
 * 工作流表单服务接口
 * @author 郭佟燕
 * @version 2.0
 */
import java.util.List;
import java.util.Map;

import cn.ideal.wf.data.analyzer.Storage;
import cn.ideal.wf.model.WorkflowTableBrief;
import cn.ideal.wf.model.WorkflowTableElement;
import cn.ideal.wf.model.WorkflowTableLayout;
import cn.ideal.wf.model.WorkflowTableSummary;
import cn.ideal.wf.model.WorkflowTableUserDefination;


public interface WorkflowTableService {
	
	WorkflowTableBrief find(Long tbId);	
	
	List<WorkflowTableBrief> findAll();	
	
	List<WorkflowTableElement> findTableFields(Long tbId);
	
	List<String> findTableFieldNames(Long tbId);
	
	List<WorkflowTableElement> findElementsOnList(Long tbId);
	
	Map<String,Object> saveDataValueForTable(Storage storage) throws Exception;
	
	Map<String,Object> updateDataValueForTable(Storage storage) throws Exception;
	
	/**
	 * 更新业务附表信息
	 * @param wfts
	 * @return
	 */
	boolean synchTableSummary(WorkflowTableSummary wfts);
	
	/**
	 * 结束业务附表
	 * @param wfts
	 * @return
	 */
	boolean endTableSummary(WorkflowTableSummary wfts);
	
	
	WorkflowTableSummary findTableSummary(Long tbId,Long bizId);
	/**
	 * 根据指定的主表获得旗下的所有子表
	 * @param tbId
	 * @return
	 */
	List<WorkflowTableBrief> findAllSubTables(Long tbId);
	
	List<WorkflowTableBrief> findAllBlindTable();
	
	List<WorkflowTableLayout> findTableLayout(Long tbId);
	
	List<WorkflowTableElement> findTableAllElements(Long tbId,String scope);
	
	List<WorkflowTableElement> findTableAllElements(Long tbId,String scope,Long wfId, String nodeName);
	
	WorkflowTableLayout findTableLayoutWithScope(Long tbId, String scope);
	
	WorkflowTableBrief findSubTable(Long tbId, String scope);
	
	/**
	 * 获得被业务分类的所有表单
	 * @return
	 */
	List<WorkflowTableBrief> findAllSortedTable();
	
	WorkflowTableUserDefination saveTableUserDefination(WorkflowTableUserDefination def);
	
	WorkflowTableUserDefination updateTableUserDefination(WorkflowTableUserDefination def);
	
	WorkflowTableBrief findDefinationTableBrief(Long tbId, Long wfId);
	
	WorkflowTableBrief findByIds(Long tbId, Long wfId);
	
	WorkflowTableBrief findDefinationTableBrief(Long defId);
	
	List<WorkflowTableUserDefination> findDefinations(Long userId, String type, Long tbId);
	
	WorkflowTableUserDefination findDefination(Long defId);
	
	boolean deleteTableSummary(Long summaryId);
	
	boolean updateCurrentUser(WorkflowTableSummary wfts);
	
	boolean setAuthority(String tbIds, String[] userIds);
	
	List<Map<String,Object>> findAuthorities();
	
	Map<String,Object> findAuthority(Long userId);

}
