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


public interface WorkflowTableService {
	
	WorkflowTableBrief find(Long tbId);	
	
	List<WorkflowTableBrief> findAll();	
	
	List<WorkflowTableElement> findAllTableElementsWithScope(Long tbId,String scope);
	
	List<WorkflowTableElement> findAllTableElements(Long tbId);
	
	List<WorkflowTableElement> findElementsOnList(Long tbId);
	
	Map<String,Object> saveDataValueForTable(Storage storage) throws Exception;
	
	Map<String,Object> updateDataValueForTable(Storage storage) throws Exception;
}
