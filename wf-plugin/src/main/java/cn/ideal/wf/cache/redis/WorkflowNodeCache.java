package cn.ideal.wf.cache.redis;

/**
 * 工作流节点缓存
 * @author 郭佟燕
 * @version 2.0
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class WorkflowNodeCache {//implements CommandLineRunner {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final String WF_KEY = "WFN";	
	private Boolean cacheAvailable;
    private RedisTemplate<String,Object> redisTemplate;	 
	@Autowired
    private WorkflowNodeService workflowNodeService;
	@Autowired
    private WorkflowWFService workflowWFService;
	
	private HashOperations<String, String, List<WorkflowNode>> hashWorkflowNode;
	
	
	private void init() {		
		logger.info("--Redis--加载工作流节点数据");
		
		hashWorkflowNode = redisTemplate.opsForHash();
		List<Workflow> wfs = workflowWFService.findHavingBindTable();
		for(Workflow wf : wfs){
			List<WorkflowNode> wfns = workflowNodeService.findAll(wf.getWfId());
			hashWorkflowNode.put(WF_KEY, wf.getWfId().toString(), wfns);			
		}
	}
	
	public List<WorkflowNode> getValue(Long wfId){
		if(hashWorkflowNode == null) init();
		return hashWorkflowNode.get(WF_KEY, wfId.toString());
	}
	
	public Map<Long,List<WorkflowNode>> getAll(){
		if(hashWorkflowNode == null) init();
		Map<String,List<WorkflowNode>> wfns = hashWorkflowNode.entries(WF_KEY);
		Map<Long,List<WorkflowNode>> newWfns = new HashMap<Long,List<WorkflowNode>>();
		for(String key : wfns.keySet()){
			newWfns.put(Long.parseLong(key), wfns.get(key));
		}
		
		return newWfns;
	}
	
	public void save(List<WorkflowNode> wfns,Long wfId) {
		if(hashWorkflowNode == null) init();
		hashWorkflowNode.put(WF_KEY, wfId.toString(), wfns);
	}

	public void update(List<WorkflowNode> wfns,Long wfId) {
		if(hashWorkflowNode == null) init();
		hashWorkflowNode.put(WF_KEY,wfId.toString(), wfns);
	}
 
	public void delete(Long wfId) {
		if(hashWorkflowNode == null) init();
		hashWorkflowNode.delete(WF_KEY, wfId.toString());
	}

	public WorkflowNode getFirstNode(Long wfId){
		if(hashWorkflowNode == null) init();
		List<WorkflowNode> wfns = hashWorkflowNode.get(WF_KEY, wfId.toString());
		if(wfns != null){
			for(WorkflowNode wfn : wfns){
				if(wfn.getPreNodes() == null || wfn.getPreNodes().size() == 0) return wfn;
			}
		}
		return null;
	}
	
	public List<WorkflowNode> getNextNode(String curNodeName,Long wfId){
		if(hashWorkflowNode == null) init();
		List<WorkflowNode> wfns = hashWorkflowNode.get(WF_KEY, wfId.toString());
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
	
	private void getCacheAvaliable() {		
		try{
			if(redisTemplate.getConnectionFactory().getConnection().ping() == null) cacheAvailable = new Boolean(false);
			else cacheAvailable = new Boolean (true);
		}catch(Exception e){
			cacheAvailable = new Boolean (false);
		}
	}
	
	public boolean isCacheAvaliable(){
		if(cacheAvailable == null) {
			getCacheAvaliable();
			return cacheAvailable;
		}
		else return cacheAvailable;
	}
}

