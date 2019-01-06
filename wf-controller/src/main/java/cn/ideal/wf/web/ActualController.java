package cn.ideal.wf.web;
/**
 * 流程配置中心的首页
 * @author 郭佟燕
 * @version 2.0
 * */

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.ideal.wf.model.TableBrief;
import cn.ideal.wf.model.TableElement;
import cn.ideal.wf.model.Workflow;
import cn.ideal.wf.service.TableService;
import cn.ideal.wf.service.WorkflowService;

@Controller
@RequestMapping("/app")
public class ActualController {
	@Autowired
	private WorkflowService workflowService;	
	@Autowired
	private TableService tableService;
	/**
	 * 实战首页
	 * */
	@GetMapping("/actualcenter")
    public ModelAndView homePage(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("app/actualCenter");
		mav.addObject("wfs", workflowService.findAllBlindTable());
        return mav;
    }
	
	/**
	 * 获取指定流程的列表信息
	 * 
	 */
	@GetMapping("/list/{wfId}")
    public ModelAndView getList(@PathVariable Long wfId, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("app/list");
		Workflow wf = workflowService.find(wfId);
		mav.addObject("wfs", workflowService.findAllBlindTable());
		mav.addObject("wf",wf);
		mav.addObject("tableList", tableService.findTableList(wf.getTableId()));
        return mav;
    }
	
	/**
	 * 新建一个指定业务
	 * @param request
	 * @return
	 */
	@GetMapping("/table/{wfId}")
    public ModelAndView newTable(@PathVariable Long wfId, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("app/table");
		Workflow wf = workflowService.find(wfId);
		List<TableElement> headLst = tableService.findAllTableElements(wf.getTableId(),"head");
		List<TableElement> bodyLst = tableService.findAllTableElements(wf.getTableId(),"body");		
		List<TableElement> footLst = tableService.findAllTableElements(wf.getTableId(),"foot");
		TableBrief tb = tableService.find(wf.getTableId());		
		
		if(bodyLst.size() % tb.getCols() != 0){
			for(int i=0; i< bodyLst.size() % tb.getCols(); i++){
				bodyLst.add(new TableElement());
			}
		}
		
		mav.addObject("headList",headLst);
		mav.addObject("bodyList",bodyLst);
		mav.addObject("footList",footLst);
		mav.addObject("brief",tb);
		mav.addObject("wfs", workflowService.findAllBlindTable());
		mav.addObject("wf",wf);
        return mav;
    }
}
