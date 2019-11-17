package cn.ideal.wf.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
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
import cn.ideal.wf.model.WorkflowTableUserDefination;
import cn.ideal.wf.service.WorkflowTableService;
import cn.ideal.wf.service.WorkflowWFService;

import com.alibaba.druid.util.StringUtils;

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

	@Override
	public List<List<Map<String,Object>>> findTableSimpleElementsForMobile(Long tbId,Long bizId,Long wfId,String nodeName) throws Exception{				
		if(bizId == null) return Arrays.asList(match(wfTableMapper.findTableSimpleElementsForMobile(tbId,wfId,nodeName)));
		else {
			List<List<Map<String,Object>>> fieldWithValues = new LinkedList<List<Map<String,Object>>>();
			WorkflowTableBrief tb = this.find(tbId);
			List<Map<String,Object>> res = SQLConnector.getSQLExecutor().query("select * from "+tb.getName()+" where id = "+bizId);
			List<Map<String,Object>> fields = match(wfTableMapper.findTableSimpleElementsForMobile(tbId,wfId,nodeName));
			if(res.size() > 0){
				for(Map<String,Object> field : fields){					
					if(field.get("NEWFIELDNAME") != null ) {
						//处理审批意见数据
						if(field.get("NEWFIELDTYPE").equals("审批意见")){
							List<Map<String,Object>> comments = SQLConnector.getSQLExecutor().query("select * from workflow_comment where bizId = "+bizId+" and tbId="+tbId+" and upper(fieldname)='"+field.get("NEWFIELDNAME").toString().toUpperCase()+"' order by remarkdate");
							field.put("VALUE",comments);
						}else{
							field.put("VALUE", res.get(0).get(((String)field.get("NEWFIELDNAME")).toUpperCase()));
						}
						
						if(field.get("VALUE") == null) field.put("VALUE","");
					}
					if(field.get("NEWHIDDENFIELDNAME") != null ) {	
						field.put("HIDDENVALUE", res.get(0).get(((String)field.get("NEWHIDDENFIELDNAME")).toUpperCase()));
						if(field.get("HIDDENVALUE") == null) field.put("HIDDENVALUE","");
					}
				}
			}
			fieldWithValues.add(fields);
			List<WorkflowTableBrief> tbs = this.findAllSubTables(tbId);
			for(WorkflowTableBrief item : tbs){				
				res = SQLConnector.getSQLExecutor().query("select * from "+item.getName()+" where id in (select skey from table_keys where zkey= "+bizId+" and stablename='"+item.getName()+"') order by id");							
				for(Map<String,Object> values : res){
					fields = match(wfTableMapper.findTableSimpleElementsForMobile(item.getTbId(),wfId,nodeName));					
					for(Map<String,Object> field : fields){					
						if(field.get("NEWFIELDNAME") != null ) {						
							field.put("VALUE", values.get(((String)field.get("NEWFIELDNAME")).toUpperCase()));
							if(field.get("VALUE") == null) field.put("VALUE","");
						}
					}
					fieldWithValues.add(fields);
				}
				if(res.size() == 0) fieldWithValues.add(match(wfTableMapper.findTableSimpleElementsForMobile(item.getTbId(),wfId,nodeName)));
			}
			return fieldWithValues;
		}
	}
	
	/**
	 * 计算标签和控件一对一组合 ，为移动专门做的处理
	 * @param fields
	 * @return
	 */
	private List<Map<String,Object>> match(List<Map<String,Object>> fields){				
		String item = null;
		for(Map<String,Object> field : fields){
			switch((String)field.get("NEWFIELDTYPE")){
			case "标签":				
				item = (String)field.get("NEWLABELNAME");
				break;
			default:
				if(item == null) item = (String)field.get("NEWLABELNAME");
				field.put("NEWLABELNAME", item);				
				break;
			}
			item = null;
		}  
		Iterator<Map<String,Object>> iters =  fields.iterator();
		while(iters.hasNext()){
			Map<String,Object> field = iters.next();
			if(((String)field.get("NEWFIELDTYPE")).equals("标签")) iters.remove();
		}
		return fields;
	}

	@Override
	public List<Map<String, Object>> findAllSortedTableWithBizCountByCreatedUser(Long userId) {
		return wfTableMapper.findAllSortedTableWithBizCountByCreatedUser(userId);
	}
}
