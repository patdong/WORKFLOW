package cn.ideal.wfpf.web;
/**
 * 流程配置中心的首页
 * @author 郭佟燕
 * @version 2.0
 * */

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
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
import cn.ideal.wf.model.Workflow;
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
@PropertySource("classpath:application.properties")
public class ActualController extends PlatformFundation{
	@Autowired
	private WorkflowWFService wfService;	
	@Autowired
	private WorkflowTableService wftableService;
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
	
	@Value("${workflow.wf.database.querypageexecutor}")
	private String queryPageExecutorName;

	private QueryPageExecutor queryPageExecutor;
	
	@Autowired
    public void setQueryPageExecutor(ApplicationContext context) {
		queryPageExecutor = (QueryPageExecutor) context.getBean(queryPageExecutorName);
    }
	/**
	 * 实战首页
	 * */
	@GetMapping("/actualcenter")
    public ModelAndView homePage(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("app/actualCenter");
		ListModel listModel = new ListModel();
		mav.addObject("model", listModel);		
		listModel.setMenu(wftableService.findAllBlindTable());
        return mav;
    }
	
	/**
	 * 获取指定业务列表信息
	 * 此方法是采用流程驱动方式
	 */
	@GetMapping(value={"/list/{tbId}/{scope}/{pageNumber}","/list/{tbId}/{pageNumber}"})
    public ModelAndView getList(@PathVariable Long tbId,@PathVariable Long pageNumber,@PathVariable Optional<String> scope, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("app/list");
		ListModel listModel = new ListModel();
		mav.addObject("model", listModel);
		try{
			Storage storage = null;
			storage = parameterAnalyzer.dataAnalyze(request, tbId);
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
	        page.setUrl("/app/list/"+tbId);
	        listModel.setPage(page);
		}catch(Exception e){	
			e.printStackTrace();
		}
		
		listModel.setWftb(TableBriefCache.getValue(tbId));
		listModel.setTableList(wftableService.findElementsOnList(tbId));
		listModel.setMenu(wftableService.findAllBlindTable());
		
        return mav;
    }	
	
	/**
	 * 通过列表选中某一条记录，包括新建业务功能
	 * @param wfId
	 * @param bizId
	 * @param request
	 * @return
	 */
	@GetMapping(value={"/showtable/{tbId}/{bizId}","/showtable/{tbId}"})
    public ModelAndView showTable(@PathVariable Long tbId, @PathVariable Optional<Long> bizId, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("app/table");
		PageModel pageModel = new PageModel();
		mav.addObject("model",pageModel);
		if(bizId.isPresent()) pageModel.setBizId(bizId.get());
		pageModel.setWftb(TableBriefCache.getValue(tbId));
		Workflow wf = WorkflowCache.getValue(pageModel.getWftb().getWfId());
		pageModel.setWf(wf);		
		pageModel.setMenu(wftableService.findAllBlindTable());
		pageModel.setNodeName(wfProcessor.findNodeName(wf.getWfId(),pageModel.getBizId()));
		pageModel.setNextNodes(workflowNodeService.findNextNodes(pageModel.getNodeName(), wf.getWfId()));
		if(bizId.isPresent()) {
			pageModel.setFlowChat(flowChatService.draw(wf.getWfId(),bizId.get()).toString());
			pageModel.setWfBrief(wfBriefService.find(bizId.get(), wf.getWfId()));
			pageModel.setTable(plattenTableService.draw(pageModel.getWftb().getTbId(),bizId.get()).toString());
		}else{
			pageModel.setFlowChat(flowChatService.draw(wf.getWfId()).toString());
			pageModel.setTable(plattenTableService.draw(pageModel.getWftb().getTbId()).toString());
		}
		
		if(pageModel.getWfBrief() != null){
			pageModel.setButtons(workflowNodeService.findButtonsByNodeName(pageModel.getWfBrief().getNodeName(), wf.getWfId()));
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
	@PostMapping(value={"/save/{tbId}","/save/{tbId}/{bizId}"})
	public ModelAndView saveTableData(@PathVariable Long tbId, @PathVariable Optional<Long> bizId,HttpServletRequest request) throws Exception{		
		Storage storage = storageAnalyzer.dataAnalyze(request, tbId);		
		//获取运行系统的当前登录用户
		storage.setUser(this.getWorkflowUser(request));	
		Map<String,Object> obj;
		if(bizId.isPresent()) {
			storage.setBizId(bizId.get());
			obj = wftableService.updateDataValueForTable(storage);
		}
		else obj = wftableService.saveDataValueForTable(storage);
		
		ModelAndView mav = new ModelAndView("redirect:/app/showtable/"+tbId+"/"+obj.get("ID"));
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
	@PostMapping(value={"/doaction/{tbId}/{bizId}","/doaction/{tbId}/{bizId}/{nodeId}"})
	public ModelAndView doaction(@PathVariable Long tbId, @PathVariable Long bizId,@PathVariable Optional<Long> nodeId, HttpServletRequest request) throws Exception{
		ModelAndView mav = new ModelAndView("redirect:/app/list/"+tbId+"/1");	
		if(nodeId.isPresent()){			
			wfProcessor.doAction(tbId, bizId, this.getWorkflowUser(request), workflowNodeService.findNode(nodeId.get()));
		}else wfProcessor.doAction(tbId, bizId, this.getWorkflowUser(request));
		
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
	@PostMapping(value={"/dobutton/{tbId}/{bizId}/{buttonName}"})
	public ModelAndView dobutton(@PathVariable Long tbId, @PathVariable Long bizId,@PathVariable String buttonName,HttpServletRequest request) throws Exception{
		ModelAndView mav = new ModelAndView("redirect:/app/list/"+tbId+"/1");						
		wfProcessor.doButton(tbId, bizId, this.getWorkflowUser(request),buttonName);
		
		return mav;
	}
}
