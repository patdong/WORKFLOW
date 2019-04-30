package cn.ideal.wf.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import cn.ideal.wf.model.Workflow;
import cn.ideal.wf.model.WorkflowNode;
import cn.ideal.wf.service.WorkflowNodeService;
import cn.ideal.wf.service.WorkflowWFService;

@Component("WorkflowNodeCache1")
public class WorkflowNodeCache implements ApplicationContextAware{
	private static Map<Long, List<WorkflowNode>> hashWorkflowNode = null;
	private static ApplicationContext context;

    public static ApplicationContext getApplicationContext() {
        return context;
    }
    @Override
    public void setApplicationContext(ApplicationContext ac)throws BeansException {
        context = ac;
    }
    
	public static void init() {
		WorkflowNodeService workflowNodeService = context.getBean(WorkflowNodeService.class);
		WorkflowWFService workflowWFService = context.getBean(WorkflowWFService.class);
		
		List<Workflow> wfs = workflowWFService.findHavingBindTable();
		hashWorkflowNode = new HashMap<Long,List<WorkflowNode>>();
		for(Workflow wf : wfs){
			List<WorkflowNode> wfns = workflowNodeService.findAll(wf.getWfId());
			hashWorkflowNode.put(wf.getWfId(), wfns);			
		}
		
	}
	
	public static List<WorkflowNode> getValue(Long wfId){
		if(hashWorkflowNode == null) {
			init();
		}
		return hashWorkflowNode.get( wfId);
	}
	
	public static Map<Long,List<WorkflowNode>> getAll(){
		if(hashWorkflowNode == null) {
			init();
		}
		return hashWorkflowNode;
	}
	
	public static void save(List<WorkflowNode> wfns,Long wfId) {
		if(hashWorkflowNode == null) {
			init();
		}
		hashWorkflowNode.put(wfId, wfns);
	}

	public static void update(List<WorkflowNode> wfns,Long wfId) {
		if(hashWorkflowNode == null) {
			init();
		}
		hashWorkflowNode.put(wfId, wfns);
	}
 
	public static void delete(Long wfId) {
		if(hashWorkflowNode == null) {
			init();
		}
		hashWorkflowNode.remove(wfId);
	}

	public static WorkflowNode getFirstNode(Long wfId){
		if(hashWorkflowNode == null) {
			init();
		}
		List<WorkflowNode> wfns = hashWorkflowNode.get(wfId);
		if(wfns != null){
			for(WorkflowNode wfn : wfns){
				if(wfn.getPreNodes() == null || wfn.getPreNodes().size() == 0) return wfn;
			}
		}
		return null;
	}
	
	public static List<WorkflowNode> getNextNode(String curNodeName,Long wfId){
		if(hashWorkflowNode == null) {
			init();
		}
		List<WorkflowNode> wfns = hashWorkflowNode.get(wfId);
		List<WorkflowNode> res = new ArrayList<WorkflowNode>();
		
		if(wfns != null){
			for(WorkflowNode wfn : wfns){
				if(wfn.getNodeName().equals(curNodeName)) {
					res = wfn.getSufNodes();
					break;
				}
			}
		}
		
		for(int i=0;i<res.size();i++){
			for(WorkflowNode wfn : wfns){
				if(res.get(i).getNodeId().compareTo(wfn.getNodeId()) == 0) res.set(i, wfn);
			}
			
		}
		
		return res.size() > 0 ? res : null;
	}
}
