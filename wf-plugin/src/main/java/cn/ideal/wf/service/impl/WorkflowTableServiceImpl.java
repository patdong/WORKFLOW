package cn.ideal.wf.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
	@Autowired
	private SQLExecutor mysqlExecutor;
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
		return mysqlExecutor.save(storage);			
	}

	@Override
	public List<WorkflowTableBrief> findAll() {
		return wfTableMapper.findAll();
	}

	@Override
	public Map<String, Object> updateDataValueForTable(Storage storage) throws Exception {		
		return mysqlExecutor.update(storage);
	}

	@Override
	public void synchTableSummary(WorkflowTableSummary wfts) {
		wfTableMapper.synchTableSummary(wfts);
	}

	@Override
	public void endTableSummary(WorkflowTableSummary wfts) {
		wfTableMapper.endTableSummary(wfts);
	}


}