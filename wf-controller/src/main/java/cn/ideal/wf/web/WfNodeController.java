package cn.ideal.wf.web;

/**
 * 流程节点处理
 * @author 郭佟燕
 * @version 2.0
 */
import java.util.ArrayList;
import java.util.List;

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

import cn.ideal.wf.model.Node;
import cn.ideal.wf.service.NodeService;

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
		if(node.getNodeId() == null) node = nodeService.save(node);
		else node = nodeService.update(node);
        return new ModelAndView("redirect:/wf/workflowdefination/"+node.getWfId());
    }
	
	@GetMapping("/delNode/{nodeId}")
    public @ResponseBody boolean delNode(@PathVariable Long nodeId, HttpServletRequest request) {
		nodeService.setInvalid(nodeId);		
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
}
