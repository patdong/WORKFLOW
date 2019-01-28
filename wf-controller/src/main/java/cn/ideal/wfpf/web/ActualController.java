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
import cn.ideal.wf.model.Workflow;
import cn.ideal.wf.model.WorkflowTableBrief;
import cn.ideal.wf.model.WorkflowTableElement;
import cn.ideal.wf.processor.Processor;
import cn.ideal.wf.service.WorkflowBriefService;
import cn.ideal.wf.service.WorkflowFlowService;
import cn.ideal.wf.service.WorkflowTableService;
import cn.ideal.wf.service.WorkflowWFService;
import cn.ideal.wfpf.model.Page;
import cn.ideal.wfpf.service.NodeService;


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
	private Processor actualService;	
	@Autowired
	private NodeService nodeService;
	/**
	 * 实战首页
	 * */
	@GetMapping("/actualcenter")
    public ModelAndView homePage(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("app/actualCenter");
		mav.addObject("wfs", wfService.findAllBlindTable());
        return mav;
    }
	
	/**
	 * 获取指定业务列表信息
	 * 
	 */
	@GetMapping(value={"/list/{wfId}/{scope}/{pageNumber}","/list/{wfId}/{pageNumber}"})
    public ModelAndView getList(@PathVariable Long wfId,@PathVariable Long pageNumber,@PathVariable Optional<String> scope, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("app/list");		
		Workflow wf = WorkflowCache.getValue(wfId);
		Storage storage = null;
		try{
			storage = parameterAnalyzer.dataAnalyze(request, wfId);
			storage.setUser(this.getWorkflowUser(request));
			storage.setBeginNumberWithPageNumber(pageNumber);
			if(scope.isPresent()){
				storage.getParameters().put("scope", scope.get());
			}
			Long count = queryPageExecutor.queryAll(storage);
	        Page<Map<String,Object>> page = new Page<Map<String,Object>>(count,pageNumber);
	        page.setPageList(queryPageExecutor.queryPage(storage));
	        page.setUrl("/app/list/"+wfId);
	        mav.addObject("page",page);
		}catch(Exception e){	
			e.printStackTrace();
		}
		        
		mav.addObject("wf",wf);
		mav.addObject("wfs", wfService.findAllBlindTable());
		mav.addObject("tableList", wftableService.findElementsOnList(wf.getTableId()));
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
		ModelAndView mav = new ModelAndView("redirect:/app/list/"+wfId+"/1");
		
		Storage storage = storageAnalyzer.dataAnalyze(request, wfId);		
		//获取运行系统的当前登录用户
		storage.setUser(this.getWorkflowUser(request));			
		if(bizId.isPresent()) {
			storage.setBizId(bizId.get());
			wftableService.updateDataValueForTable(storage);
		}
		else wftableService.saveDataValueForTable(storage);
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
		Workflow wf = WorkflowCache.getValue(wfId);
		WorkflowTableBrief tb = TableBriefCache.getValue(wf.getTableId());
		
		mav.addObject("brief",tb);
		mav.addObject("wfs", wfService.findAllBlindTable());		
		mav.addObject("wf",wf);
		mav.addObject("bizId",null);
		if(bizId.isPresent()) {
			mav.addObject("bizId",bizId.get());
			//获取办理节点			
			mav.addObject("nodename",actualService.findNodeName(wfId, bizId.get()));
			mav.addObject("nodetree",nodeService.getTreeNodes(wfId,bizId.get()));
		}else{
			mav.addObject("nodetree",nodeService.getTreeNodes(wfId));
		}
		
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
		Workflow wf = WorkflowCache.getValue(wfId);
		List<WorkflowTableElement> headLst = wftableService.findAllTableElementsWithScope(wf.getTableId(),"head");
		List<WorkflowTableElement> bodyLst = wftableService.findAllTableElementsWithScope(wf.getTableId(),"body");		
		List<WorkflowTableElement> footLst = wftableService.findAllTableElementsWithScope(wf.getTableId(),"foot");
		WorkflowTableBrief tb = TableBriefCache.getValue(wf.getTableId());
		
		if(bodyLst.size() % tb.getCols() != 0){
			for(int i=0; i< bodyLst.size() % tb.getCols(); i++){
				bodyLst.add(new WorkflowTableElement());
			}
		}
		
		mav.addObject("headList",headLst);
		mav.addObject("bodyList",bodyLst);
		mav.addObject("footList",footLst);
		mav.addObject("brief",tb);
		mav.addObject("wf",wf);		
		if(bizId.isPresent()){
			Storage storage = new Storage();
			storage.setBizId(bizId.get());
			storage.setTableName(tb.getName());
			mav.addObject("bizTable",queryPageExecutor.query(storage));
			mav.addObject("bizId",bizId.get());
		}
        return mav;
    }
	
	@PostMapping(value={"/passworkflow/{wfId}/{bizId}"})
	public ModelAndView passWorkflow(@PathVariable Long wfId, @PathVariable Long bizId,HttpServletRequest request) throws Exception{
		ModelAndView mav = new ModelAndView("redirect:/app/list/"+wfId+"/1");						
		actualService.flowPass(wfId, bizId, this.getWorkflowUser(request));
		
		return mav;
	}
}
