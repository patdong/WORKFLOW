package cn.ideal.wf.processor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.ideal.wf.data.analyzer.ParameterAnalyzer;
import cn.ideal.wf.data.analyzer.Storage;
import cn.ideal.wf.data.analyzer.StorageAnalyzer;
import cn.ideal.wf.flowchat.draw.FlowChatService;
import cn.ideal.wf.jdbc.dao.SQLConnector;
import cn.ideal.wf.model.Workflow;
import cn.ideal.wf.model.WorkflowBrief;
import cn.ideal.wf.model.WorkflowNode;
import cn.ideal.wf.model.WorkflowRole;
import cn.ideal.wf.model.WorkflowStep;
import cn.ideal.wf.model.WorkflowTableBrief;
import cn.ideal.wf.model.WorkflowTableElement;
import cn.ideal.wf.model.WorkflowTableLayout;
import cn.ideal.wf.model.WorkflowTableSummary;
import cn.ideal.wf.model.WorkflowTableUserDefination;
import cn.ideal.wf.model.WorkflowUser;
import cn.ideal.wf.page.ListModel;
import cn.ideal.wf.page.Page;
import cn.ideal.wf.page.PageModel;
import cn.ideal.wf.service.PlatformService;
import cn.ideal.wf.service.WorkflowBriefService;
import cn.ideal.wf.service.WorkflowFlowService;
import cn.ideal.wf.service.WorkflowNodeService;
import cn.ideal.wf.service.WorkflowStepService;
import cn.ideal.wf.service.WorkflowTableService;
import cn.ideal.wf.service.WorkflowWFService;
import cn.ideal.wf.service.impl.JdbcSQLExecutor;
import cn.ideal.wf.table.draw.PureTableService;

import com.alibaba.druid.util.StringUtils;

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
	@Autowired
	private WorkflowStepService wfStepService;
	
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
			storage = parameterAnalyzer.dataAnalyze(request, tbId,null);
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
			}else if(StringUtils.isEmpty(selectedScope) || selectedScope.equals("approve")){
				Long count = jdbcSQLExecutor.queryWorkflowAll(user.getUserId());
		        Page<Map<String,Object>> page = new Page<Map<String,Object>>(count,pageNumber);
		        page.setPageList(jdbcSQLExecutor.queryWorkflowPage(user.getUserId(),pageNumber,pageSize));
		        listModel.setPage(page);
			}else if(StringUtils.isEmpty(selectedScope) || selectedScope.equals("approved")){
				Long count = jdbcSQLExecutor.queryWorkedflowAll(user.getUserId());
		        Page<Map<String,Object>> page = new Page<Map<String,Object>>(count,pageNumber);
		        page.setPageList(jdbcSQLExecutor.queryWorkedflowPage(user.getUserId(),pageNumber,pageSize));
		        listModel.setPage(page);
			}else if(StringUtils.isEmpty(selectedScope) || selectedScope.equals("monitor")){
				Long count = jdbcSQLExecutor.queryWorkedflowAll();
		        Page<Map<String,Object>> page = new Page<Map<String,Object>>(count,pageNumber);
		        page.setPageList(jdbcSQLExecutor.queryWorkedflowPage(pageNumber,pageSize));
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
	public PageModel getPageModel(HttpServletRequest request,Long tbId, Long bizId){
		PageModel pageModel = new PageModel();			
		WorkflowUser user = platformService.getWorkflowUser(request);
		WorkflowTableSummary wfts = wfTableService.findTableSummary(tbId,bizId);
		pageModel.setWfts(wfts);
		
		Long wfId = wfts.getWfId();		
		pageModel.setBizId(bizId);
		if(wfts.getDefId() != null) {
			WorkflowTableBrief wfTb = wfTableService.findDefinationTableBrief(wfts.getDefId());
			pageModel.setWftb(wfTb);			
		}
		//用户自定义流程配置
		if(pageModel.getWftb() == null) pageModel.setWftb(wfTableService.findDefinationTableBrief(tbId, wfId));
		//默认流程配置
		if(pageModel.getWftb() == null) pageModel.setWftb(wfTableService.findByIds(tbId, wfId));
		
		//和流程相关		
		if(wfId != null){
			pageModel.setNodeName(wfProcessor.findNodeName(wfId,bizId,user));
			if(pageModel.getNodeName() != null) {
				pageModel.setNextNodes(workflowNodeService.findNextNodes(pageModel.getNodeName(), wfId));	
				if(bizId != null){
					WorkflowBrief wfbf = wfBriefService.find(bizId, wfId);
					if(wfbf != null && wfbf.getFinishedDate() == null){
						pageModel.setButtons(workflowNodeService.findButtonsByNodeName(wfbf.getNodeName(), wfId));
					}
				}
			}			
						
			//流程图		
			pageModel.setFlowChat(flowChatService.draw(wfId,bizId).toString());	
		}
		//表单
		pageModel.setTable(plattenTableService.draw(tbId,wfId,wfts.getDefId(),bizId).toString());		
		pageModel.setMenu(wfTableService.findAllBlindTable());
		
		return pageModel;
	}
	
	/**
	 * 获得初始化业务数据
	 * @param request
	 * @param tbId
	 * @param wfId
	 * @return
	 */
	public PageModel getInitPageModel(HttpServletRequest request,Long tbId,Long wfId,Long defId){
		PageModel pageModel = new PageModel();				
		WorkflowUser user = platformService.getWorkflowUser(request);
		if(defId != null) {
			WorkflowTableBrief wfTb = wfTableService.findDefinationTableBrief(defId);
			pageModel.setWftb(wfTb);
			if(wfTb != null) wfId = wfTb.getWfId();
		}
		//用户自定义流程配置
		if(pageModel.getWftb() == null) pageModel.setWftb(wfTableService.findDefinationTableBrief(tbId, wfId));
		//默认流程配置
		if(pageModel.getWftb() == null) pageModel.setWftb(wfTableService.findByIds(tbId, wfId));
		
		//和流程相关
		if(wfId != null){
			pageModel.setNodeName(wfProcessor.findNodeName(wfId,null,user));
			if(pageModel.getNodeName() != null) {
				pageModel.setNextNodes(workflowNodeService.findNextNodes(pageModel.getNodeName(), wfId));				
			}			
			pageModel.setNodes(workflowNodeService.findAll(wfId));	
			//流程图		
			pageModel.setFlowChat(flowChatService.draw(wfId,null).toString());	
		}
		//表单
		pageModel.setTable(plattenTableService.draw(tbId,wfId,defId,null).toString());		
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
	 * 页面更新保存操作
	 * @param request
	 * @param tbId
	 * @param bizId
	 * @return
	 */
	public Long update(HttpServletRequest request,Long tbId,Long bizId){
		try{
			WorkflowTableSummary wfts = wfTableService.findTableSummary(tbId,bizId);
			Storage storage = storageAnalyzer.dataAnalyze(request, wfts.getTbId(),wfts.getWfId());		
			//获取运行系统的当前登录用户
			storage.setUser(platformService.getWorkflowUser(request));			
			storage.setBizId(wfts.getBizId());
			Map<String,Object> obj = wfTableService.updateDataValueForTable(storage);
			
			if(obj.get("ID") != null) {
				if(obj.get("ID") instanceof Long) return (Long)obj.get("ID");
				if(obj.get("ID") instanceof BigDecimal) return Long.parseLong(obj.get("ID").toString());
				if(obj.get("ID") instanceof Integer) return Long.parseLong(obj.get("ID").toString());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 页面保存操作
	 * @param request
	 * @param tbId
	 * @param wfId
	 * @return
	 */
	public Long save(HttpServletRequest request,Long tbId,Long wfId){
		try{
			Storage storage = storageAnalyzer.dataAnalyze(request, tbId,wfId);
			//获取运行系统的当前登录用户
			storage.setUser(platformService.getWorkflowUser(request));				
			Map<String,Object> obj = wfTableService.saveDataValueForTable(storage);
			
			if(obj.get("ID") != null) {		
				if(obj.get("ID") instanceof Long) return (Long)obj.get("ID");
				if(obj.get("ID") instanceof BigDecimal) return Long.parseLong(obj.get("ID").toString());
				if(obj.get("ID") instanceof Integer) return Long.parseLong(obj.get("ID").toString());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获得业务分类
	 * @return
	 */
	public List<WorkflowTableBrief> getSortedBiz(){
		return wfTableService.findAllSortedTable();
	}
	
	/**
	 * 创建用户自定义流程
	 * @param request
	 * @param tbId
	 * @param users
	 * @param roles
	 * @return
	 */
	public Long createUserDefination(HttpServletRequest request,Long tbId,Map<Long,List<WorkflowUser>> users, Map<Long,WorkflowRole> roles){			
		WorkflowTableBrief wftb = wfTableService.find(tbId);
		if(wftb.getWfId() == null) return null;
		List<WorkflowNode> wfNodes = workflowNodeService.findAll(wftb.getWfId());
		Map<Long,List<WorkflowUser>> newUsers = new HashMap<Long,List<WorkflowUser>>();
		Map<Long,WorkflowRole> newRoles = new HashMap<Long,WorkflowRole>();
		Long idx = 1l;
		for(WorkflowNode node: wfNodes){
			if(users != null && users.get(idx) != null){
				newUsers.put(node.getNodeId(), users.get(idx));
			}
			if(roles != null && roles.get(idx) != null){
				newRoles.put(node.getNodeId(), roles.get(idx));
			}
			idx++;
		}
		Workflow wf = workflowNodeService.clone(wftb.getWfId(), newUsers, newRoles);
		
		if(wf == null) return null;
		return wf.getWfId();
	}
	
	/**
	 * 设置用户自定义的表单相关属性
	 * @param def
	 * @return
	 */
	public WorkflowTableUserDefination setTableUserDefination(HttpServletRequest request){
		WorkflowUser user = platformService.getWorkflowUser(request);
		WorkflowTableUserDefination def = new WorkflowTableUserDefination();
		if(!StringUtils.isEmpty(request.getParameter("defId"))) def.setDefId(Long.parseLong(request.getParameter("defId")));
		def.setTbId(Long.parseLong(request.getParameter("tbId")));
		if(!StringUtils.isEmpty(request.getParameter("wfId"))) def.setWfId(Long.parseLong(request.getParameter("wfId")));
		def.setUserId(user.getUserId());
		def.setCreatedDate(new Date());
		def.setTableName(request.getParameter("tableName"));
		def.setAction1(request.getParameter("action1"));
		def.setAction2(request.getParameter("action2"));
		def.setAction3(request.getParameter("action3"));
		def.setNotification1(request.getParameter("notification1"));
		def.setNotification2(request.getParameter("notification2"));
		def.setNotification3(request.getParameter("notification3"));
		def.setType(request.getParameter("type"));
		
		WorkflowTableBrief wftb = wfTableService.findDefinationTableBrief(def.getTbId(), def.getWfId());		
		if(wftb == null) return wfTableService.saveTableUserDefination(def);
		else return wfTableService.updateTableUserDefination(def);
	}
	
	/**
	 * 获取用户自定义的表单流程信息
	 * @param userId
	 * @param type
	 * @param tbId
	 * @return
	 */
	public List<WorkflowTableUserDefination> getUserDefinations(Long userId, String type, Long tbId){
		return wfTableService.findDefinations(userId, type, tbId);
	}
	
	/**
	 * 逻辑删除业务数据，仅对tabl-summary表中的status重置成无效
	 * @param summaryId
	 * @return
	 */
	public boolean deleteSummary(Long summaryId){
		return wfTableService.deleteTableSummary(summaryId);
	}
	
	/**
	 * 获得当前节点未办理的流程
	 * @param bizId
	 * @param wfId
	 * @return
	 */
	public List<WorkflowStep> getUnfinishedWorkflowStepsOnNode(Long bizId,Long wfId){
		return wfStepService.findUNFinshedWrokflowStep(bizId, wfId);
	}
	
	/**
	 * 调度节点上的办理人，仅对未办理完毕的流程有效
	 * @param stepId
	 * @param user
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public boolean changeWorkflowStep(Long stepId, WorkflowUser user){
		return wfStepService.setWorkflowStepUser(stepId, user);
	}
	
	/**
	 * 批量做模板授权
	 * @param tbIds
	 * @param userIds
	 * @return
	 */
	public boolean setAuthority(String tbIds, String[] userIds){
		return wfTableService.setAuthority(tbIds, userIds);
	}
	
	public ListModel getAuthorities(Long pageNumber,HttpServletRequest request){
		ListModel listModel = new ListModel();		
		try{			
			List<Map<String,Object>> res = wfTableService.findAuthorities();
	        Page<Map<String,Object>> page = new Page<Map<String,Object>>(new Long(res.size()),pageNumber);
	        page.setPageList(wfTableService.findAuthorities());	        
	        listModel.setPage(page);	        			
		}catch(Exception e){	
			e.printStackTrace();
		}
		
		return listModel;		
	}
	
	public Map<String,Object> getAuthority(Long userId){
		return wfTableService.findAuthority(userId);
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
