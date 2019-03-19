package cn.ideal.wf.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import cn.ideal.wf.dao.WorkflowTableMapper;
import cn.ideal.wf.data.analyzer.Storage;
import cn.ideal.wf.jdbc.dao.SQLExecutor;
import cn.ideal.wf.model.WorkflowTableBrief;
import cn.ideal.wf.model.WorkflowTableElement;
import cn.ideal.wf.model.WorkflowTableSummary;
import cn.ideal.wf.service.WorkflowTableService;
import cn.ideal.wf.service.WorkflowWFService;

@Service
public class WorkflowTableServiceImpl implements WorkflowTableService {

	@Autowired
	private WorkflowTableMapper wfTableMapper;
	@Autowired
	private WorkflowWFService wfService;
	
	private SQLExecutor sqlExecutor;
	
	@Value("${workflow.wf.database.executor}")
    String executorName;
	
	@Autowired
    public void setSQLExecutor(ApplicationContext context) {
		sqlExecutor = (SQLExecutor) context.getBean(executorName);
    }
	
	@Override
	public WorkflowTableBrief find(Long tbId) {
		return wfTableMapper.find(tbId);
	}

	@Override
	public List<WorkflowTableElement> findAllTableElementsWithScope(Long tbId,String scope) {
		return wfTableMapper.findAllTableElements(tbId, scope);
	}

	@Override
	public List<WorkflowTableElement> findElementsOnList(Long tbId) {
		return wfTableMapper.findElementsOnList(tbId);
	}

	@Override
	public List<WorkflowTableElement> findAllTableElements(Long tbId) {
		return wfTableMapper.findAllTableElements(tbId, null);
	}

	@Override
	public Map<String, Object> saveDataValueForTable(Storage storage) throws Exception {		
		return sqlExecutor.save(storage);			
	}

	@Override
	public List<WorkflowTableBrief> findAll() {
		return wfTableMapper.findAll();
	}

	@Override
	public Map<String, Object> updateDataValueForTable(Storage storage) throws Exception {		
		return sqlExecutor.update(storage);
	}

	@Override
	public boolean synchTableSummary(WorkflowTableSummary wfts) {
		int idx = wfTableMapper.synchTableSummary(wfts);
		if(idx > 0) return true;
		return false;
	}

	@Override
	public boolean endTableSummary(WorkflowTableSummary wfts) {
		int idx = wfTableMapper.endTableSummary(wfts);
		if(idx > 0) return true;
		return false;
	}


}
