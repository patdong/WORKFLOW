package cn.ideal.wf.processor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;

import cn.ideal.wf.data.analyzer.ParameterAnalyzer;
import cn.ideal.wf.data.analyzer.Storage;
import cn.ideal.wf.data.analyzer.StorageAnalyzer;
import cn.ideal.wf.flowchat.draw.FlowChatService;
import cn.ideal.wf.jdbc.dao.SQLConnector;
import cn.ideal.wf.model.WorkflowBrief;
import cn.ideal.wf.model.WorkflowTableBrief;
import cn.ideal.wf.model.WorkflowTableElement;
import cn.ideal.wf.model.WorkflowTableLayout;
import cn.ideal.wf.model.WorkflowUser;
import cn.ideal.wf.page.ListModel;
import cn.ideal.wf.page.Page;
import cn.ideal.wf.page.PageModel;
import cn.ideal.wf.service.PlatformService;
import cn.ideal.wf.service.WorkflowBriefService;
import cn.ideal.wf.service.WorkflowFlowService;
import cn.ideal.wf.service.WorkflowNodeService;
import cn.ideal.wf.service.WorkflowTableService;
import cn.ideal.wf.service.WorkflowWFService;
import cn.ideal.wf.service.impl.JdbcSQLExecutor;
import cn.ideal.wf.table.draw.PureTableService;

@Service
public class BusinessProcessor {
	@Autowired
	private WorkflowWFService wfService;	
	@Autowired
	private WorkflowTableService wfTableService;
	@Autowired
	private WorkflowFlowService wfFlowService;	
	@Autowired
	private ParameterAnalyzer parameterAnalyzer;
	@Autowired
	private StorageAnalyzer storageAnalyzer;
	@Autowired
	private WorkflowBriefService wfBriefService;
	@Autowired
	private Processor wfProcessor;	
	@Autowired
	private WorkflowNodeService workflowNodeService;
	@Autowired
	@Qualifier("richFlowChatService")
	private FlowChatService flowChatService;
	@Autowired
	private PureTableService plattenTableService;
	@Autowired
	private JdbcSQLExecutor jdbcSQLExecutor;
	@Autowired
	private PlatformService platformService;
	
	/**
	 * 获取列表页面信息
	 * @param tbId       业务编号
	 * @param pageNumber  列表页码
	 * @param selectedScope   列表下拉框选项
	 * @return
	 */
	public ListModel getListModel(Long tbId, Long pageNumber,String selectedScope,HttpServletRequest request){
		ListModel listModel = new ListModel();		
		try{
			Storage storage = null;
			storage = parameterAnalyzer.dataAnalyze(request, tbId);
			storage.setUser(platformService.getWorkflowUser(request));
			storage.setBeginNumberWithPageNumber(pageNumber);
			//下拉列表处理
			listModel.setScope("approve");
			if(selectedScope != null){
				storage.getParameters().put("scope", selectedScope);
				listModel.setScope(selectedScope);
			}
			Long count = jdbcSQLExecutor.queryAll(storage);
	        Page<Map<String,Object>> page = new Page<Map<String,Object>>(count,pageNumber);
	        page.setPageList(jdbcSQLExecutor.queryPage(storage));
	        page.setUrl("/app/list/"+tbId);
	        listModel.setPage(page);
	        
	        listModel.setWftb(wfTableService.find(tbId));
			listModel.setTableList(wfTableService.findElementsOnList(tbId));
			listModel.setMenu(wfTableService.findAllBlindTable());
			
		}catch(Exception e){	
			e.printStackTrace();
		}
		
		return listModel;
	}

	
	/**
	 * 获取列表页面信息
	 * @param tbId       业务编号
	 * @param pageNumber  列表页码
	 * @param selectedScope   列表下拉框选项
	 * @return
	 */
	public ListModel getListAll(Long pageNumber,Long pageSize,String selectedScope,HttpServletRequest request){
		ListModel listModel = new ListModel();		
		try{
			WorkflowUser user = platformService.getWorkflowUser(request);
			if(StringUtils.isEmpty(selectedScope) || selectedScope.equals("apply")){
				Long count = jdbcSQLExecutor.queryAll(user.getUserId());
		        Page<Map<String,Object>> page = new Page<Map<String,Object>>(count,pageNumber);
		        page.setPageList(jdbcSQLExecutor.queryPage(user.getUserId(),pageNumber,pageSize));
		        listModel.setPage(page);
			}else{
				Long count = jdbcSQLExecutor.queryWorkflowAll(user.getUserId());
		        Page<Map<String,Object>> page = new Page<Map<String,Object>>(count,pageNumber);
		        page.setPageList(jdbcSQLExecutor.queryWorkflowPage(user.getUserId(),pageNumber,pageSize));
		        listModel.setPage(page);
			}
		}catch(Exception e){	
			e.printStackTrace();
		}
		
		return listModel;
	}
	
	/**
	 * 获取表单展示页面信息
	 * @param tbId
	 * @param bizId
	 * @return
	 */
	public PageModel getPageModel(Long tbId, Long bizId){
		PageModel pageModel = new PageModel();		
		Long wfId = null;
		
		pageModel.setBizId(bizId);		
		pageModel.setWftb(wfTableService.find(tbId));					
		if(pageModel.getWftb() == null) pageModel.setWftb(wfTableService.find(tbId));		
		//和流程相关
		wfId = pageModel.getWftb().getWfId();
		if(wfId != null){
			pageModel.setNodeName(wfProcessor.findNodeName(wfId,bizId));
			pageModel.setNextNodes(workflowNodeService.findNextNodes(pageModel.getNodeName(), wfId));		
			if(bizId != null){
				WorkflowBrief wfbf = wfBriefService.find(bizId, wfId);
				if(wfbf != null){
					pageModel.setButtons(workflowNodeService.findButtonsByNodeName(wfbf.getNodeName(), wfId));
				}
			}
						
			//流程图		
			pageModel.setFlowChat(flowChatService.draw(wfId,bizId).toString());	
		}
		//表单
		pageModel.setTable(plattenTableService.draw(tbId,bizId).toString());		
		pageModel.setMenu(wfTableService.findAllBlindTable());
		
		return pageModel;
	}
	
	/**
	 * 返回页面的字段值
	 * 
	 * key: 表 (主表)
	 *      subTable (表外部子表）
	 *      子表编号  (表内部子表）
	 * @param tbId
	 * @param bizId
	 * @return
	 */
	public Map<String,List<Map<String,Object>>> getFieldsValue(Long tbId, Long bizId){
		Map<String,List<Map<String,Object>>> fieldsValueMap = new HashMap<String,List<Map<String,Object>>>();
		WorkflowTableBrief tb = wfTableService.find(tbId);
		List<Map<String,Object>> res = SQLConnector.getSQLExecutor().query("select * from "+tb.getName()+" where id = "+bizId);
		fieldsValueMap.put("表", res);
		tb = wfTableService.findSubTable(tbId,"表体");
		Long stbId = null;
		if(tb != null){
			res = SQLConnector.getSQLExecutor().query("select * from "+tb.getName()+" where id in (select skey from table_keys where zkey= "+bizId+") order by id");
			fieldsValueMap.put("subTable", res);
			stbId = tb.getTbId();
		}		
		List<WorkflowTableBrief> tbLst = wfTableService.findAllSubTables(tbId);
		for(WorkflowTableBrief obj : tbLst){	
			if(stbId != null && obj.getTbId().equals(stbId)) continue;
			res = SQLConnector.getSQLExecutor().query("select * from "+obj.getName()+" where id in (select skey from table_keys where zkey= "+bizId+") order by id");
			fieldsValueMap.put(obj.getTbId().toString(), res);
		}		
		
		return fieldsValueMap;
	}
	
	/**
	 * 页面保存
	 * @param request
	 * @param tbId
	 * @param bizId
	 * @return
	 */
	public Long save(HttpServletRequest request,Long tbId,Long bizId){
		try{
			Storage storage = storageAnalyzer.dataAnalyze(request, tbId);		
			//获取运行系统的当前登录用户
			storage.setUser(platformService.getWorkflowUser(request));	
			Map<String,Object> obj;
			if(bizId != null) {
				storage.setBizId(bizId);
				obj = wfTableService.updateDataValueForTable(storage);
			}
			else obj = wfTableService.saveDataValueForTable(storage);
			
			if(obj.get("ID") != null) {
				if(obj.get("ID") instanceof Long) return (Long)obj.get("ID");
				if(obj.get("ID") instanceof BigDecimal) return Long.parseLong(obj.get("ID").toString());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 返回页面字段
	 * 
	 * key: 表头
	 *      表体
	 *      表尾
	 *      subTable (表外部子表）
	 *      tableName
	 *      bizId
	 *      子表编号  (表内部子表）
	 * @param tbId
	 * @return
	 */
	public Map<String,List<List<WorkflowTableElement>>> getFields(Long tbId,Long bizId){
		Map<String,List<List<WorkflowTableElement>>> fieldsMap = new HashMap<String,List<List<WorkflowTableElement>>>();	
		
		List<WorkflowTableLayout> layouts = wfTableService.findTableLayout(tbId);
		WorkflowTableBrief wftb = wfTableService.find(tbId);
		for(WorkflowTableLayout tl : layouts){
			List<List<WorkflowTableElement>> f = new ArrayList<List<WorkflowTableElement>>();
			f.add(wfTableService.findTableAllElements(tbId,tl.getScope()));
			fieldsMap.put(tl.getScope(), f);
			if(bizId != null){				
				List<Map<String,Object>> res = SQLConnector.getSQLExecutor().query("select * from "+wftb.getName()+" where id = "+bizId);
				Map<String,Object> resMap = res.get(0);
				for(WorkflowTableElement em : fieldsMap.get(tl.getScope()).get(0)){
					if(em.getFieldName() != null) em.setDataValue(resMap.get(em.getFieldName()));
					if(em.getStbId() != null){
						if(em.getStbId() != null){
							if(bizId != null){
								fieldsMap.put(em.getStbId().toString(),getSubFields(tl.getStbId(),bizId));
							}else{
								List<List<WorkflowTableElement>> sf = new ArrayList<List<WorkflowTableElement>>();
								sf.add(wfTableService.findTableAllElements(tl.getStbId(),null));
								fieldsMap.put(em.getStbId().toString(),sf);
							}							
						}
					}
				}
			}
			
			if(tl.getStbId() != null){				
				if(bizId != null){
					fieldsMap.put("subTable",getSubFields(tl.getStbId(),bizId));
				}else{
					List<List<WorkflowTableElement>> sf = new ArrayList<List<WorkflowTableElement>>();
					sf.add(wfTableService.findTableAllElements(tl.getStbId(),null));
					fieldsMap.put("subtable",sf);
					
				}
			}						
		}
		
		List<List<WorkflowTableElement>> basicLst = new ArrayList<List<WorkflowTableElement>>();
		List<WorkflowTableElement> basic = new ArrayList<WorkflowTableElement>();
		basic.add(new WorkflowTableElement("tableName",wftb.getTableName()));	
		basicLst.add(basic);
		fieldsMap.put("tableName", basicLst);
		basicLst = new ArrayList<List<WorkflowTableElement>>();
		basic = new ArrayList<WorkflowTableElement>();
		basic.add(new WorkflowTableElement("bizId",bizId.toString()));		
		basicLst.add(basic);
		fieldsMap.put("bizId", basicLst);
		
		return fieldsMap;		
	}
	
	private List<List<WorkflowTableElement>> getSubFields(Long stbId, Long bizId){
		List<List<WorkflowTableElement>> fieldsLst = new ArrayList<List<WorkflowTableElement>>();
		WorkflowTableBrief swftb = wfTableService.find(stbId);
		List<Map<String,Object>> sres = SQLConnector.getSQLExecutor().query("select * from "+swftb.getName()+" where id in (select skey from table_keys where zkey= "+bizId+") order by id");
		if(sres.size() == 0) fieldsLst.add(wfTableService.findTableAllElements(stbId,null));
		for(int i=0;i<sres.size();i++){
			List<WorkflowTableElement> lst = wfTableService.findTableAllElements(stbId,null);
			Map<String,Object> resMap = sres.get(i);
			for(WorkflowTableElement em : lst){
				if(em.getFieldName() != null) em.setDataValue(resMap.get(em.getFieldName()));
			}
			WorkflowTableElement id = new WorkflowTableElement();
			id.setDataValue(resMap.get("ID"));
			id.setNewHiddenFieldName("ID");
			id.setTableName(swftb.getName());
			fieldsLst.add(lst);
		}
		return fieldsLst;
	}
}
