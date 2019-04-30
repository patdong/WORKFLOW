package cn.ideal.wf.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ideal.wf.dao.WorkflowTableMapper;
import cn.ideal.wf.data.analyzer.Storage;
import cn.ideal.wf.jdbc.dao.SQLConnector;
import cn.ideal.wf.model.WorkflowTableBrief;
import cn.ideal.wf.model.WorkflowTableElement;
import cn.ideal.wf.model.WorkflowTableLayout;
import cn.ideal.wf.model.WorkflowTableSummary;
import cn.ideal.wf.service.WorkflowTableService;
import cn.ideal.wf.service.WorkflowWFService;

@Service
public class WorkflowTableServiceImpl implements WorkflowTableService {

	@Autowired
	private WorkflowTableMapper wfTableMapper;
	@Autowired
	private WorkflowWFService wfService;
		
	@Override
	public WorkflowTableBrief find(Long tbId) {
		return wfTableMapper.find(tbId);
	}

	@Override
	public List<WorkflowTableElement> findElementsOnList(Long tbId) {
		return wfTableMapper.findElementsOnList(tbId);
	}

	@Override
	public List<WorkflowTableElement> findTableFields(Long tbId) {
		return wfTableMapper.findTableFields(tbId);
	}

	@Override
	public Map<String, Object> saveDataValueForTable(Storage storage) throws Exception {		
		return SQLConnector.getSQLExecutor().save(storage);			
	}

	@Override
	public List<WorkflowTableBrief> findAll() {
		return wfTableMapper.findAll();
	}

	@Override
	public Map<String, Object> updateDataValueForTable(Storage storage) throws Exception {		
		return SQLConnector.getSQLExecutor().update(storage);
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

	@Override
	public List<WorkflowTableBrief> findAllSubTables(Long tbId) {
		return wfTableMapper.findAllSubTables(tbId);
	}

	@Override
	public List<String> findTableFieldNames(Long tbId) {
		return wfTableMapper.findTableFieldNames(tbId);
	}

	@Override
	public List<WorkflowTableBrief> findAllBlindTable() {
		return wfTableMapper.findAllBlindTable();
	}

	@Override
	public List<WorkflowTableLayout> findTableLayout(Long tbId) {
		return wfTableMapper.findTableLayout(tbId);
	}

	@Override
	public List<WorkflowTableElement> findTableAllElements(Long tbId,String scope) {
		return wfTableMapper.findTableAllElements(tbId, scope);
	}

	@Override
	public List<WorkflowTableElement> findTableAllElements(Long tbId,String scope,Long wfId, String nodeName) {
		if(wfId == null || nodeName == null) return wfTableMapper.findTableAllElements(tbId, scope);	
		return wfTableMapper.findTableAllElementsWithWorkflow(tbId, scope, wfId, nodeName);				
	}
	
	@Override
	public WorkflowTableLayout findTableLayoutWithScope(Long tbId, String scope) {
		return wfTableMapper.findTableLayoutWithScope(tbId, scope);
	}

	@Override
	public WorkflowTableBrief findSubTable(Long tbId, String scope) {
		return wfTableMapper.findSubTable(tbId, scope);
	}


}
