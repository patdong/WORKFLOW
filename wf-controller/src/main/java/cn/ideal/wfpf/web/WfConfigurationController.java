package cn.ideal.wfpf.web;

/**
 * 流程中心入口
 * @author 郭佟燕
 * @version 2.0
 * */
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.ideal.wf.flowchat.draw.FlowChatService;
import cn.ideal.wfpf.model.WFPFWorkflow;
import cn.ideal.wf.model.WorkflowNode;
import cn.ideal.wf.service.WorkflowNodeService;
import cn.ideal.wfpf.model.CertificationOrg;
import cn.ideal.wfpf.model.CertificationRole;
import cn.ideal.wfpf.model.CertificationUser;
import cn.ideal.wfpf.model.FMsg;
import cn.ideal.wfpf.model.Node;
import cn.ideal.wfpf.model.Page;
import cn.ideal.wfpf.model.TableElement;
import cn.ideal.wfpf.service.ActionService;
import cn.ideal.wfpf.service.CertificationService;
import cn.ideal.wfpf.service.NodeService;
import cn.ideal.wfpf.service.TableService;
import cn.ideal.wfpf.service.WorkflowService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.jdbc.StringUtils;

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
	@Autowired
	private WorkflowNodeService workflowNodeService;
	@Autowired
	private ActionService actionService;
	@Autowired
	@Qualifier("richFlowChatService")
	private FlowChatService flowChatService;
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
        List<WFPFWorkflow> wfLst = workflowService.findAll();
        Page<WFPFWorkflow> page = new Page<WFPFWorkflow>(new Long(wfLst.size()),pageNumber);
        page.setPageList(workflowService.findAll(page));
        page.setUrl("/wf/workflowcenter");
        mav.addObject("page",page);
        mav.addObject("tbLst", tableService.findAllWithTableNameNoRelated());
        return mav;
    }
	
	/**
	 * 创建一个新的工作流
	 * */
	@GetMapping("/newworkflow")
    public ModelAndView newWorkflow(HttpServletRequest request) {		
        WFPFWorkflow wf = workflowService.save(new WFPFWorkflow());
        
        return  new ModelAndView("redirect:/wf/workflowdefination/"+wf.getWfId());
    }
	
	/**
	 * 设置工作流的名称
	 * */
	@GetMapping("/setWfName/{wfId}")
    public @ResponseBody boolean setWfName(ModelMap map, @PathVariable Long wfId, HttpServletRequest request) {	
		WFPFWorkflow wf = new WFPFWorkflow();
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
		mav.addObject("wf",workflowService.find(wfId));					
		mav.addObject("flowchat",flowChatService.draw(wfId).toString());
		mav.addObject("actions",actionService.findWfAll());
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			List<Node> nodes = nodeService.findAllOnlyNode(wfId);
			mav.addObject("nodes",mapper.writeValueAsString(nodes));
			mav.addObject("buttons",mapper.writeValueAsString(actionService.findBtnAll()));
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
		WFPFWorkflow wf = new WFPFWorkflow();
		wf.setWfId(wfId);
		wf.setTbId(tbId);
        wf = workflowService.update(wf);
        
        if(wf != null) return true;
        return false;
    }
	
	/**
	 * 取消表单绑定
	 * */
	@GetMapping("/removebinding/{wfId}")
    public @ResponseBody boolean removeBinding(@PathVariable Long wfId, HttpServletRequest request) {			
        return workflowService.removeBinding(wfId);        
    }
	
	/**
	 * 获得当前节点可建立关联的后续节点集
	 * @param request
	 * @return
	 */
	@GetMapping("/getsufnodes/{nodeId}/{wfId}")
    public @ResponseBody List<WorkflowNode> getRelSufNodes(@PathVariable Long nodeId,@PathVariable Long wfId, HttpServletRequest request) {	
		List<WorkflowNode> wfns = workflowNodeService.findRelSufNodes(nodeId, wfId);
		return wfns;
    }

	/**
	 * 获得流程节点的表字段设置信息
	 * @param nodeId
	 * @param tbId
	 * @param request
	 * @return
	 */
	@GetMapping("/gettableelements/{wfId}/{nodeId}/{tbId}")
    public @ResponseBody List<TableElement> getTableElements(@PathVariable Long wfId,@PathVariable Long nodeId,@PathVariable Long tbId, HttpServletRequest request) {	
		List<TableElement> elements = tableService.findTableAllFields(tbId);
		List<TableElement> nodeElements = tableService.findTableAllElementsOnNode(wfId, nodeId, tbId);
		for(TableElement element : elements){
			for(TableElement nelement : nodeElements){
				if(element.getEmId().equals(nelement.getEmId())) {
					element.setReadOnly(true);
					break;
				}
			}
		}
		return elements;
    }
	
	/**
	 * 设置节点表字段是否可操作
	 * @param wfId
	 * @param nodeId
	 * @param emIds
	 * @param request
	 * @return
	 */
	@GetMapping("/settableelements/{wfId}/{nodeId}")
    public @ResponseBody FMsg setTableElements(@PathVariable Long wfId,@PathVariable Long nodeId,@RequestParam String ids, HttpServletRequest request) {
		FMsg fmsg = null;
		try{
			if(!StringUtils.isNullOrEmpty(ids)){
				List<Long> emIdLst = new ArrayList<Long>();
				for(String id: ids.split(",")){
					emIdLst.add(Long.parseLong(id));
				}
				boolean res = tableService.setTableFieldsOnNode(wfId,nodeId,emIdLst.toArray(new Long[emIdLst.size()]));
				fmsg = new FMsg(res);
			}
			
		}catch(Exception e){
			fmsg = new FMsg(FMsg.ERROR,e.getMessage());
		}
		
		return fmsg;
    }
	
	/**
	 * 启用流程
	 * @param wfId
	 * @param request
	 * @return
	 */
	@GetMapping("/startup/{wfId}")
    public @ResponseBody boolean startUp(@PathVariable Long wfId, HttpServletRequest request) {			
        return workflowService.setStatus(wfId, true);        
    }
	
	/**
	 * 停用流程
	 * @param wfId
	 * @param request
	 * @return
	 */
	@GetMapping("/shutdown/{wfId}")
    public @ResponseBody boolean shutdown(@PathVariable Long wfId, HttpServletRequest request) {			
        return workflowService.setStatus(wfId, false);        
    }
	
	@GetMapping("/remove/{wfId}")
    public @ResponseBody boolean remove(@PathVariable Long wfId, HttpServletRequest request) {			
        return workflowService.delete(wfId);        
    }
}
