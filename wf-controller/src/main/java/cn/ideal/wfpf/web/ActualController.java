package cn.ideal.wfpf.web;
/**
 * 流程配置中心的首页
 * @author 郭佟燕
 * @version 2.0
 * */

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.ideal.wf.answeringaction.Answer;
import cn.ideal.wf.common.WFConstants;
import cn.ideal.wf.model.WorkflowUser;
import cn.ideal.wf.page.ListModel;
import cn.ideal.wf.page.PageModel;
import cn.ideal.wf.processor.BusinessProcessor;
import cn.ideal.wf.processor.Processor;
import cn.ideal.wf.service.PlatformService;
import cn.ideal.wf.service.WorkflowTableService;

import com.google.gson.Gson;


@Controller
@RequestMapping("/app")
@PropertySource("classpath:application.properties")
public class ActualController{
	@Autowired
	private WorkflowTableService wfTableService;	
	@Autowired
	private Processor wfProcessor;
	@Autowired
	private BusinessProcessor businessProcessor;
	@Autowired
	private PlatformService platformService;
	/**
	 * 实战首页
	 * */
	@GetMapping("/actualcenter")
    public ModelAndView homePage(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("app/actualCenter");
		ListModel listModel = new ListModel();
		mav.addObject("model", listModel);		
		listModel.setMenu(wfTableService.findAllBlindTable());
        return mav;
    }
	
	/**
	 * 获取指定业务列表信息
	 * 此方法是采用流程驱动方式
	 */
	
	@GetMapping(value={"/list/{tbId}/{pageNumber}"})
    public ModelAndView getList(@PathVariable Long tbId,@PathVariable Long pageNumber, HttpServletRequest request) throws Exception{
		ModelAndView mav = new ModelAndView("app/list");
		String scope = "apply";
		
		ListModel listModel = businessProcessor.getListModel(tbId, pageNumber, scope, request);
		listModel.getPage().setUrl("/app/list/"+tbId);
		mav.addObject("model", listModel);
        return mav;
    }
	
	@GetMapping(value={"/list/{tbId}/{scope}/{pageNumber}"})
    public ModelAndView getList(@PathVariable Long tbId,@PathVariable Long pageNumber,@PathVariable String scope, HttpServletRequest request)throws Exception {
		ModelAndView mav = new ModelAndView("app/list");
		ListModel listModel = businessProcessor.getListModel(tbId, pageNumber, scope, request);
		listModel.getPage().setUrl("/app/list/"+tbId);
		mav.addObject("model", listModel);
        return mav;
    }
	/**
	 * 通过列表选中某一条记录，包括新建业务功能
	 * @param wfId
	 * @param bizId
	 * @param request
	 * @return
	 */
	
	@GetMapping(value={"/showtable/{tbId}/{bizId}"})
    public ModelAndView showTable(@PathVariable Long tbId, @PathVariable Long bizId, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("app/table");		
		PageModel pageModel = businessProcessor.getPageModel(request,tbId,bizId);		
		
		mav.addObject("model",pageModel);
		Gson gson = new Gson();
		mav.addObject("modeljson",gson.toJson(pageModel, PageModel.class));
        return mav;
    }
	
	@GetMapping(value={"/showinittable/{tbId}/{wfId}"})
    public ModelAndView showIntTable(@PathVariable Long tbId, @PathVariable Long wfId, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("app/table");
		PageModel pageModel = businessProcessor.getInitPageModel(request,tbId, wfId,null);		
			
		mav.addObject("model",pageModel);
		Gson gson = new Gson();
		mav.addObject("modeljson",gson.toJson(pageModel, PageModel.class));
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
	
	@PostMapping(value={"/save/{tbId}/{wfId}"})
	public ModelAndView saveTableData(@PathVariable Long tbId,@PathVariable Long wfId,HttpServletRequest request) throws Exception{		
		Long bizId = businessProcessor.save(request, tbId, wfId);
		ModelAndView mav = new ModelAndView("redirect:/app/showtable/"+tbId+"/"+bizId);
		return mav;
	}
	
	@PostMapping(value={"/update/{tbId}/{bizId}"})
	public ModelAndView updateTableData(@PathVariable Long tbId,@PathVariable Long bizId,HttpServletRequest request) throws Exception{				
		bizId = businessProcessor.update(request, tbId,bizId);
		ModelAndView mav = new ModelAndView("redirect:/app/showtable/"+tbId+"/"+bizId);
		return mav;
	}
	
	/**
	 * 流程操作
	 * 续办节点从流程设置中获取
	 * @param wfId
	 * @param bizId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	
	@PostMapping(value={"/doaction/{tbId}/{bizId}/{wfId}"})
	public ModelAndView doaction(@PathVariable Long tbId, @PathVariable Long bizId,@PathVariable Long wfId, HttpServletRequest request) throws Exception{
		ModelAndView mav = new ModelAndView("redirect:/app/list/"+tbId+"/1");	
		Long id = businessProcessor.update(request, tbId, bizId);
		if(id > 0){
			wfProcessor.doAction(tbId, bizId, wfId,platformService.getWorkflowUser(request),null);
		}
		return mav;
	}
	
	/**
	 * 流程操作
	 * 续办节点从前端传入
	 * 续办节点的办理人从前端传入
	 * @param tbId
	 * @param bizId
	 * @param nodeId
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value={"/doaction/{tbId}/{bizId}/{wfId}/{nodeId}"})
	public ModelAndView doaction(@PathVariable Long tbId, @PathVariable Long bizId,@PathVariable Long wfId,@PathVariable Long nodeId, HttpServletRequest request) throws Exception{
		ModelAndView mav = new ModelAndView("redirect:/app/list/"+tbId+"/1");
		Long id = businessProcessor.update(request, tbId, bizId);
		if(id > 0){
			String userIds = request.getParameter("selectedUserIds");
			List<WorkflowUser> wfUsers = platformService.getWorkflowUsers(userIds);
			if(wfUsers == null)wfProcessor.doAction(tbId, bizId, wfId,platformService.getWorkflowUser(request), nodeId,null);
			else wfProcessor.doAction(tbId, bizId,wfId, platformService.getWorkflowUser(request), nodeId,null,wfUsers.toArray(new WorkflowUser[wfUsers.size()]));
		}
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
	@PostMapping(value={"/dobutton/{tbId}/{bizId}/{wfId}/{buttonName}"})
	public ModelAndView dobutton(@PathVariable Long tbId, @PathVariable Long bizId,@PathVariable Long wfId,@PathVariable String buttonName,HttpServletRequest request) throws Exception{
		ModelAndView mav = new ModelAndView("redirect:/app/list/"+tbId+"/1");						
		
		WorkflowUser user = platformService.getWorkflowUser(request);		
		String actionName = request.getParameter("hidActionName");
		
		switch(actionName){
		case WFConstants.WF_BUTTON_ACTION_PUSH:
			wfProcessor.doButton(tbId, bizId, wfId,user,buttonName,null);
			break;
		case WFConstants.WF_BUTTON_ACTION_ANSWER:
			Answer asw = new Answer();
			asw.setBizId(bizId);
			asw.setWfId(wfId);
			asw.setContent(request.getParameter("content"));
			asw.setUserIds(request.getParameter("receiveUserId"));
			asw.setUserNames(request.getParameter("receiveUserId_names"));
			asw.setType(WFConstants.WF_BUTTON_ACTION_FLOW);
			asw.setUser(user);
			wfProcessor.doAnswer(asw, user,buttonName, WFConstants.WF_MSG_DISPENSE);			
			break;
		case WFConstants.WF_BUTTON_ACTION_PUSHWITHMSG:
			String msg = request.getParameter("content");
			wfProcessor.doButton(tbId, bizId,wfId, user,buttonName,msg,null);
			break;
		}	
		
		return mav;
	}
}
