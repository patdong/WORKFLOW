package cn.ideal.wf.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;

import cn.ideal.wf.dao.WorkflowTableMapper;
import cn.ideal.wf.data.analyzer.Storage;
import cn.ideal.wf.jdbc.dao.SQLConnector;
import cn.ideal.wf.model.WorkflowTableBrief;
import cn.ideal.wf.model.WorkflowTableElement;
import cn.ideal.wf.model.WorkflowTableLayout;
import cn.ideal.wf.model.WorkflowTableSummary;
import cn.ideal.wf.model.WorkflowTableUserDefination;
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

	@Override
	public List<WorkflowTableBrief> findAllSortedTable() {
		return wfTableMapper.findAllSortedTable();
	}

	@Override
	public WorkflowTableUserDefination saveTableUserDefination(WorkflowTableUserDefination def) {
		int idx = wfTableMapper.saveTableUserDefination(def);
		if(idx == 1) return def;
		return null;
	}

	@Override
	public WorkflowTableUserDefination updateTableUserDefination(WorkflowTableUserDefination def) {
		int idx = wfTableMapper.updateTableUserDefination(def);
		if(idx == 1) return def;
		return null;
	}

	@Override
	public WorkflowTableSummary findTableSummary(Long tbId,Long bizId) {
		return wfTableMapper.findTableSummary(tbId,bizId);
	}

	@Override
	public WorkflowTableBrief findDefinationTableBrief(Long tbId, Long wfId) {
		if(wfId != null) return wfTableMapper.findDefinationTableBrief(tbId, wfId);
		else return wfTableMapper.find(tbId);
	}

	@Override
	public WorkflowTableBrief findDefinationTableBrief(Long defId) {
		return wfTableMapper.findDefinationTableBriefByDefId(defId);
	}

	@Override
	public WorkflowTableBrief findByIds(Long tbId, Long wfId) {
		return wfTableMapper.findByIds(tbId, wfId);
	}

	@Override
	public List<WorkflowTableUserDefination> findDefinations(Long userId,String type, Long tbId) {
		return wfTableMapper.findDefinations(userId, type, tbId);
	}

	@Override
	public WorkflowTableUserDefination findDefination(Long defId) {
		return wfTableMapper.findDefination(defId);
	}

	@Override
	public boolean deleteTableSummary(Long summaryId) {
		int idx = wfTableMapper.deleteTableSummary(summaryId);
		if(idx > 0)return true;
		return false;
	}

	@Override
	public boolean updateCurrentUser(WorkflowTableSummary wfts) {
		int idx = wfTableMapper.updateCurrentUser(wfts);
		if(idx > 0)return true;
		return false;
	}

	@Override
	public boolean setAuthority(String tbIds, String[] userIds) {
		List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
		for(String uid : userIds){
			wfTableMapper.deleteAuthority(Long.parseLong(uid));	
			String[] tbData = tbIds.split(",");
			String ids = ",";
			String names = "";
			for(String item :tbData){
				if(StringUtils.isEmpty(item)) continue;
				ids += item.substring(0,item.indexOf("-")) + ",";
				names += item.substring(item.indexOf("-")+1) + ",";
			}
			if(ids.length() > 0){				
				names = names.substring(0,names.length()-1);
				Map<String,Object> auth = new HashMap<String,Object>();
				auth.put("tbIds", ids);
				auth.put("tbNames", names);
				auth.put("userId", uid);
				auth.put("createdDate", new Date());
				data.add(auth);	
			}
		}
		
		int idx = wfTableMapper.setAuthority(data);
		if(idx > 0) return true;
		return false;
	}

	@Override
	public List<Map<String, Object>> findAuthorities() {
		return wfTableMapper.findAuthorities();
	}

	@Override
	public Map<String, Object> findAuthority(Long userId) {
		return wfTableMapper.findAuthority(userId);
	}
}
