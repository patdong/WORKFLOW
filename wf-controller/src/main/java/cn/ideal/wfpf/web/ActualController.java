package cn.ideal.wfpf.web;
/**
 * 流程配置中心的首页
 * @author 郭佟燕
 * @version 2.0
 * */

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.ideal.wf.cache.TableBriefCache;
import cn.ideal.wf.cache.WorkflowCache;
import cn.ideal.wf.data.analyzer.ParameterAnalyzer;
import cn.ideal.wf.data.analyzer.Storage;
import cn.ideal.wf.data.analyzer.StorageAnalyzer;
import cn.ideal.wf.data.query.QueryPageExecutor;
import cn.ideal.wf.flowchat.draw.FlowChatService;
import cn.ideal.wf.model.WorkflowTableBrief;
import cn.ideal.wf.model.WorkflowTableElement;
import cn.ideal.wf.page.ListModel;
import cn.ideal.wf.page.Page;
import cn.ideal.wf.page.PageModel;
import cn.ideal.wf.processor.Processor;
import cn.ideal.wf.service.WorkflowBriefService;
import cn.ideal.wf.service.WorkflowFlowService;
import cn.ideal.wf.service.WorkflowNodeService;
import cn.ideal.wf.service.WorkflowTableService;
import cn.ideal.wf.service.WorkflowWFService;
import cn.ideal.wf.table.draw.PureTableService;


@Controller
@RequestMapping("/app")
public class ActualController extends PlatformFundation{
	@Autowired
	private WorkflowWFService wfService;	
	@Autowired
	private WorkflowTableService wftableService;
	@Autowired
	private WorkflowFlowService wfFlowService;
	@Autowired
	private QueryPageExecutor queryPageExecutor;
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
	
	/**
	 * 实战首页
	 * */
	@GetMapping("/actualcenter")
    public ModelAndView homePage(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("app/actualCenter");
		ListModel listModel = new ListModel();
		mav.addObject("model", listModel);
		listModel.setMenu(wfService.findAllBlindTable());
        return mav;
    }
	
	/**
	 * 获取指定业务列表信息
	 * 
	 */
	@GetMapping(value={"/list/{wfId}/{scope}/{pageNumber}","/list/{wfId}/{pageNumber}"})
    public ModelAndView getList(@PathVariable Long wfId,@PathVariable Long pageNumber,@PathVariable Optional<String> scope, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("app/list");
		ListModel listModel = new ListModel();
		mav.addObject("model", listModel);
		try{
			Storage storage = null;
			storage = parameterAnalyzer.dataAnalyze(request, wfId);
			storage.setUser(this.getWorkflowUser(request));
			storage.setBeginNumberWithPageNumber(pageNumber);
			//下拉列表处理
			listModel.setScope("approve");
			if(scope.isPresent()){
				storage.getParameters().put("scope", scope.get());
				listModel.setScope(scope.get());
			}
			Long count = queryPageExecutor.queryAll(storage);
	        Page<Map<String,Object>> page = new Page<Map<String,Object>>(count,pageNumber);
	        page.setPageList(queryPageExecutor.queryPage(storage));
	        page.setUrl("/app/list/"+wfId);
	        listModel.setPage(page);
		}catch(Exception e){	
			e.printStackTrace();
		}
		
		listModel.setWf(WorkflowCache.getValue(wfId));
		listModel.setTableList(wftableService.findElementsOnList(listModel.getWf().getTbId()));
		listModel.setMenu(wfService.findAllBlindTable());
		
        return mav;
    }	
	
	/**
	 * 通过列表选中某一条记录，包括新建业务功能
	 * @param wfId
	 * @param bizId
	 * @param request
	 * @return
	 */
	@GetMapping(value={"/showtable/{wfId}/{bizId}/{style}","/showtable/{wfId}/{style}"})
    public ModelAndView showTable(@PathVariable Long wfId, @PathVariable Optional<Long> bizId,@PathVariable String style,HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("app/table");
		PageModel pageModel = new PageModel();
		mav.addObject("model",pageModel);
		if(bizId.isPresent()) pageModel.setBizId(bizId.get());
		pageModel.setWf(WorkflowCache.getValue(wfId));		
		pageModel.setMenu(wfService.findAllBlindTable());
		pageModel.setNodeName(wfProcessor.findNodeName(wfId,pageModel.getBizId()));
		pageModel.setNextNodes(workflowNodeService.findNextNodes(pageModel.getNodeName(), wfId));
		if(bizId.isPresent()) {			
			pageModel.setFlowChat(flowChatService.draw(wfId,bizId.get()).toString());
			pageModel.setWfBrief(wfBriefService.find(bizId.get(), wfId));
		}else{
			pageModel.setFlowChat(flowChatService.draw(wfId).toString());			
		}
		
		if(pageModel.getWfBrief() != null){
			pageModel.setButtons(workflowNodeService.findButtonsByNodeName(pageModel.getWfBrief().getNodeName(), wfId));
		}
		pageModel.setTable(plattenTableService.draw(pageModel.getTableBrief().getTbId()).toString());
        return mav;
    }
	
	/**
	 * 表单内容因格式的不同做不同的展示
	 * @param wfId
	 * @param bizId
	 * @param style
	 * @param request
	 * @return
	 */
	@GetMapping(value = {"/showcontent/{wfId}/{style}", "/showcontent/{wfId}/{bizId}/{style}"})
    public ModelAndView showContent(@PathVariable Long wfId, @PathVariable Optional<Long> bizId, @PathVariable String style, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("app/content_div"); 
		PageModel pageModel = new PageModel();
		mav.addObject("model",pageModel);
		pageModel.setWf(WorkflowCache.getValue(wfId));
		
		Long tableId = pageModel.getWf().getTbId();
		List<WorkflowTableElement> headLst = wftableService.findAllTableElementsWithScope(tableId,"head");
		List<WorkflowTableElement> bodyLst = wftableService.findAllTableElementsWithScope(tableId,"body");		
		List<WorkflowTableElement> footLst = wftableService.findAllTableElementsWithScope(tableId,"foot");
		WorkflowTableBrief tb = TableBriefCache.getValue(tableId);
		
		if(bodyLst.size() % tb.getCols() != 0){
			for(int i=0; i< bodyLst.size() % tb.getCols(); i++){
				bodyLst.add(new WorkflowTableElement());
			}
		}
		pageModel.setTableBrief(tb);
		pageModel.setHeadLst(headLst);
		pageModel.setBodyLst(bodyLst);
		pageModel.setFootLst(footLst);

		if(bizId.isPresent()){
			Storage storage = new Storage();
			storage.setBizId(bizId.get());
			storage.setTableName(tb.getName());
			pageModel.setBizTable(queryPageExecutor.query(storage));
			pageModel.setBizId(bizId.get());
		}
        return mav;
    }
	
	/**
	 * 保存表单信息
	 * 固定的变量的命名：$BIZID - 业务编号
	 *                $TABLENAME - 操作的表名
	 * @param wfId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value={"/save/{wfId}","/save/{wfId}/{bizId}"})
	public ModelAndView saveTableData(@PathVariable Long wfId, @PathVariable Optional<Long> bizId,HttpServletRequest request) throws Exception{		
		Storage storage = storageAnalyzer.dataAnalyze(request, wfId);		
		//获取运行系统的当前登录用户
		storage.setUser(this.getWorkflowUser(request));	
		Map<String,Object> obj;
		if(bizId.isPresent()) {
			storage.setBizId(bizId.get());
			obj = wftableService.updateDataValueForTable(storage);
		}
		else obj = wftableService.saveDataValueForTable(storage);
		
		ModelAndView mav = new ModelAndView("redirect:/app/showtable/"+wfId+"/"+obj.get("ID")+"/div");
		return mav;
	}
	
	/**
	 * 流程操作
	 * @param wfId
	 * @param bizId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value={"/doaction/{wfId}/{bizId}","/doaction/{wfId}/{bizId}/{nodeId}"})
	public ModelAndView doaction(@PathVariable Long wfId, @PathVariable Long bizId,@PathVariable Optional<Long> nodeId, HttpServletRequest request) throws Exception{
		ModelAndView mav = new ModelAndView("redirect:/app/list/"+wfId+"/1");	
		if(nodeId.isPresent()){			
			wfProcessor.doAction(wfId, bizId, this.getWorkflowUser(request), workflowNodeService.findNode(nodeId.get()));
		}else wfProcessor.doAction(wfId, bizId, this.getWorkflowUser(request));
		
		return mav;
	}
	
	/**
	 * 流程按钮操作
	 * @param wfId
	 * @param bizId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value={"/dobutton/{wfId}/{bizId}/{buttonName}"})
	public ModelAndView dobutton(@PathVariable Long wfId, @PathVariable Long bizId,@PathVariable String buttonName,HttpServletRequest request) throws Exception{
		ModelAndView mav = new ModelAndView("redirect:/app/list/"+wfId+"/1");						
		wfProcessor.doButton(wfId, bizId, this.getWorkflowUser(request),buttonName);
		
		return mav;
	}
}
