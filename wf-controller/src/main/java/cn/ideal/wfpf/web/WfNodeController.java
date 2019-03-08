package cn.ideal.wfpf.web;

/**
 * 流程节点处理
 * @author 郭佟燕
 * @version 2.0
 */
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import cn.ideal.wfpf.model.Node;
import cn.ideal.wfpf.service.NodeService;

@Controller
@RequestMapping("/wfnode")
public class WfNodeController {

	@Autowired
	private NodeService nodeService;
	/**
	 * 节点保存
	 * 
	 */
	@PostMapping("/savenode")
    public ModelAndView saveNode(@ModelAttribute("node") Node node, 
    		@RequestParam(value = "preNodeId") Long preNodeId,
    		HttpServletRequest request) {	
		if(node.getTimeLimit() == null) node.setTimeLimit(0l);
		if(preNodeId != null){
			Node preNode = new Node();
			preNode.setNodeId(preNodeId);
			List<Node> preNodes = new ArrayList<Node>();
			preNodes.add(preNode);
			node.setPreNodes(preNodes);
		}
		node.setType("直接连接");
		if(node.getNodeId() == null) node = nodeService.save(node);
		else node = nodeService.update(node);
        return new ModelAndView("redirect:/wf/workflowdefination/"+node.getWfId());
    }
	
	@GetMapping(value={"/delNode/{nodeId}","/delNode/{nodeId}/{delegationNodeId}"})
    public @ResponseBody boolean delNode(@PathVariable Long nodeId, @PathVariable Optional<Long> delegationNodeId, HttpServletRequest request) {
		if(delegationNodeId.isPresent()) nodeService.setDelegationNode(nodeId, delegationNodeId.get());
		else nodeService.delete(nodeId);		
		return true;
    }
	
	@GetMapping("/frozenNode/{nodeId}")
    public @ResponseBody boolean frozenNode(@PathVariable Long nodeId, HttpServletRequest request) {
		nodeService.setFrozeing(nodeId);		
		return true;
    }
	
	@GetMapping("/unfrozenNode/{nodeId}")
    public @ResponseBody boolean unfrozenNode(@PathVariable Long nodeId, HttpServletRequest request) {
		nodeService.setValid(nodeId);		
		return true;
    }
	
	/**
	 * 节点关系配置
	 * @param request
	 * @return
	 */
	@PostMapping("/savesufnode/{wfId}")
    public ModelAndView saveSufNode(@PathVariable Long wfId,HttpServletRequest request) {		
		Node node = new Node();
		node.setNodeId(Long.parseLong(request.getParameter("sufNodeId")));
		node.setCreatedDate(new Date());
		List<Node> preNodes = new ArrayList<Node>();
		Node preNode = new Node();
		preNode.setNodeId(Long.parseLong(request.getParameter("nodeNodeId")));
		preNodes.add(preNode);
		node.setPreNodes(preNodes);
		node.setType("直接连接");
		node = nodeService.saveNodeNode(node);
        return new ModelAndView("redirect:/wf/workflowdefination/"+wfId);
    }
	
	/**
	 * 删除节点连接
	 * @param nodeId
	 * @param delegationNodeId
	 * @param request
	 * @return
	 */
	@GetMapping(value={"/delNodeLink/{nodeId}/{lineNodeIds}","/delNodeLink/{nodeId}"})
    public @ResponseBody boolean delNodeLink(@PathVariable Long nodeId, @PathVariable Optional<String> lineNodeIds, HttpServletRequest request) {
		if(!lineNodeIds.isPresent()) nodeService.deleteLink(nodeId, new Long[]{0l});
		else{
			String[] ary = lineNodeIds.get().split(",");
			Long[] lary = new Long[ary.length];
			for(int i=0;i<lary.length;i++){
				lary[i] = Long.parseLong(ary[i]);
			}
			
			nodeService.deleteLink(nodeId, lary);
		}
		return true;
    }
}
