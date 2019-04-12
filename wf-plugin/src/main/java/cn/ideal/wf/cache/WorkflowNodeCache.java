package cn.ideal.wf.cache;

/**
 * 工作流节点缓存
 * @author 郭佟燕
 * @version 2.0
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import cn.ideal.wf.model.Workflow;
import cn.ideal.wf.model.WorkflowNode;
import cn.ideal.wf.service.WorkflowNodeService;
import cn.ideal.wf.service.WorkflowWFService;

@Component
@Order(value=2)
public class WorkflowNodeCache implements CommandLineRunner {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final String WF_KEY = "WFN";
	@Autowired
    private RedisTemplate<String,Object> redisTemplate;	 
	@Autowired
    private WorkflowNodeService workflowNodeService;
	@Autowired
    private WorkflowWFService workflowWFService;
	
	public static HashOperations<String, Long, List<WorkflowNode>> hashWorkflowNode;
	
	@Override
	public void run(String... args) throws Exception {		
		logger.info("启动时数据加载");
		hashWorkflowNode = redisTemplate.opsForHash();
		List<Workflow> wfs = workflowWFService.findHavingBindTable();
		for(Workflow wf : wfs){
			List<WorkflowNode> wfns = workflowNodeService.findAll(wf.getWfId());
			hashWorkflowNode.put(WF_KEY, wf.getWfId(), wfns);			
		}
	}
	
	public static List<WorkflowNode> getValue(Long wfId){
		return hashWorkflowNode.get(WF_KEY, wfId);
	}
	
	public static Map<Long,List<WorkflowNode>> getAll(){
		return hashWorkflowNode.entries(WF_KEY);
	}
	
	public static void save(List<WorkflowNode> wfns,Long wfId) {
		hashWorkflowNode.put(WF_KEY, wfId, wfns);
	}

	public static void update(List<WorkflowNode> wfns,Long wfId) {
		hashWorkflowNode.put(WF_KEY,wfId, wfns);
	}
 
	public static void delete(Long wfId) {
		hashWorkflowNode.delete(WF_KEY, wfId);
	}

	public static WorkflowNode getFirstNode(Long wfId){
		List<WorkflowNode> wfns = hashWorkflowNode.get(WF_KEY, wfId);
		if(wfns != null){
			for(WorkflowNode wfn : wfns){
				if(wfn.getPreNodes() == null || wfn.getPreNodes().size() == 0) return wfn;
			}
		}
		return null;
	}
	
	public static List<WorkflowNode> getNextNode(String curNodeName,Long wfId){
		List<WorkflowNode> wfns = hashWorkflowNode.get(WF_KEY, wfId);
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

