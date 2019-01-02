package cn.ideal.wf.web;

/**
 * 流程中心入口
 * @author 郭佟燕
 * @version 2.0
 * */
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.ideal.wf.model.CertificationOrg;
import cn.ideal.wf.model.CertificationRole;
import cn.ideal.wf.model.CertificationUser;
import cn.ideal.wf.model.Node;
import cn.ideal.wf.model.Page;
import cn.ideal.wf.model.Workflow;
import cn.ideal.wf.service.CertificationService;
import cn.ideal.wf.service.NodeService;
import cn.ideal.wf.service.TableService;
import cn.ideal.wf.service.WorkflowService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/wf")
public class WfConfigurationController {
	@Autowired
	private CertificationService certificationService;
	@Autowired
	private NodeService nodeService;
	@Autowired
	private WorkflowService workflowService;
	@Autowired
	private TableService tableService;
	/**
	 * 工作流定义中心
	 * */
	@GetMapping("/workflowcenter")
    public ModelAndView enterWorkflowCenter(HttpServletRequest request) {
        return new ModelAndView("redirect:/wf/workflowcenter/1");
    }
	
	
	@GetMapping("/workflowcenter/{pageNumber}")
    public ModelAndView enterWorkflowCenterWithPage( @PathVariable Long pageNumber,HttpServletRequest request) {		
        ModelAndView mav = new ModelAndView("config/workflowCenter");
        List<Workflow> wfLst = workflowService.findAll();
        Page<Workflow> page = new Page<Workflow>(new Long(wfLst.size()),pageNumber);
        page.setPageList(workflowService.findAll(page));
        mav.addObject("page",page);
        mav.addObject("tbLst", tableService.findAll());
        return mav;
    }
	
	/**
	 * 创建一个新的工作流
	 * */
	@GetMapping("/newworkflow")
    public ModelAndView newWorkflow(HttpServletRequest request) {		
        Workflow wf = workflowService.save(new Workflow());
        
        return  new ModelAndView("redirect:/wf/workflowdefination/"+wf.getWfId());
    }
	
	/**
	 * 设置工作流的名称
	 * */
	@GetMapping("/setWfName/{wfId}")
    public @ResponseBody boolean setWfName(ModelMap map, @PathVariable Long wfId, HttpServletRequest request) {	
		Workflow wf = new Workflow();
		wf.setWfId(wfId);
		wf.setWfName(request.getParameter("wfName"));
        wf = workflowService.update(wf);
        
        if(wf != null) return true;
        return false;
    }
	
	/**
	 * 定义工作流
	 * */
	@GetMapping("/workflowdefination/{wfId}")
    public ModelAndView defineWorkflow(ModelMap map,@PathVariable Long wfId, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("config/workflowDefination");
		try {
			List<CertificationRole> roles = certificationService.findRoles();
			mav.addObject("roles", roles);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Node node = new Node();
		node.setWfId(wfId);
		mav.addObject("node" , node);
		List<Node> nodes = nodeService.findAllOnlyNode(wfId);
		mav.addObject("nodeset",nodes);
		mav.addObject("nodetree",nodeService.getTreeNodes(wfId));
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			mav.addObject("nodes",mapper.writeValueAsString(nodes));
		} catch (JsonProcessingException e) {			
			e.printStackTrace();
		}
        return mav;
    }
	
	/**
	 * 选择指定用户信息
	 * */
	@GetMapping("/selectUsers")
    public @ResponseBody List<CertificationUser> selectUsers(HttpServletRequest request) {	
		String userName = request.getParameter("userName");
		List<CertificationUser> users = null;
		try {
			users = certificationService.findUsers(userName);
		} catch (Exception e) {	
			e.printStackTrace();
		}
		
		return users;		
    }
	
	/**
	 * 选择指定单位信息
	 * */
	@GetMapping("/selectOrgs")
    public @ResponseBody List<CertificationOrg> selectOrg(HttpServletRequest request) {	
		String orgName = request.getParameter("orgName");
		List<CertificationOrg> orgs = null;
		try {
			orgs = certificationService.findOrgs(orgName);
		} catch (Exception e) {	
			e.printStackTrace();
		}
		
		return orgs;		
    }
	
	/**
	 * 设置表单绑定
	 * */
	@GetMapping("/setbinding/{wfId}")
    public @ResponseBody boolean setBinding(@PathVariable Long wfId, @RequestParam("tbId") Long tbId, HttpServletRequest request) {	
		Workflow wf = new Workflow();
		wf.setWfId(wfId);
		wf.setTableId(tbId);
        wf = workflowService.update(wf);
        
        if(wf != null) return true;
        return false;
    }
	
	/**
	 * 取消表单绑定
	 * */
	@GetMapping("/removebinding/{wfId}")
    public @ResponseBody boolean removeBinding(@PathVariable Long wfId, HttpServletRequest request) {	
		Workflow wf = new Workflow();
		wf.setWfId(wfId);
        wf = workflowService.removeBinding(wf);
        
        if(wf != null) return true;
        return false;
    }
}
