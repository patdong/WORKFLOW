package cn.ideal.wf.cache.factory;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cn.ideal.wf.model.WorkflowNode;

@Component
public class WorkflowNodeCacheFactory {
	@Autowired
	private cn.ideal.wf.cache.redis.WorkflowNodeCache redisCache;
	@Autowired
	private cn.ideal.wf.cache.WorkflowNodeCache memoryCache;
	@Value("${data.memory.cache}")
	boolean dataMemoryCache;
	
	public List<WorkflowNode> getValue(Long wfId){
		if(dataMemoryCache){
			if(redisCache.isCacheAvaliable()) return redisCache.getValue(wfId);
			return memoryCache.getValue(wfId);
		}
		return null;
	}
	
	public Map<Long,List<WorkflowNode>> getAll(){
		if(dataMemoryCache){
			if(redisCache.isCacheAvaliable()) return redisCache.getAll();		
			return memoryCache.getAll();
		}
		return null;
	}
	
	public void save(List<WorkflowNode> wfns,Long wfId) {
		if(dataMemoryCache){
			if(redisCache.isCacheAvaliable()) redisCache.save(wfns, wfId);
			memoryCache.save(wfns, wfId);
		}		
	}

	public void update(List<WorkflowNode> wfns,Long wfId) {
		if(dataMemoryCache){
			if(redisCache.isCacheAvaliable()) redisCache.save(wfns, wfId);
			memoryCache.save(wfns, wfId);
		}
	}
 
	public void delete(Long wfId) {
		if(dataMemoryCache){
			if(redisCache.isCacheAvaliable()) redisCache.delete(wfId);
			memoryCache.delete(wfId);
		}
	}

	public WorkflowNode getFirstNode(Long wfId){
		if(dataMemoryCache){
			if(redisCache.isCacheAvaliable()) return redisCache.getFirstNode(wfId);
			return memoryCache.getFirstNode(wfId);
		}
		return null;
	}
	
	public List<WorkflowNode> getNextNode(String curNodeName,Long wfId){
		if(dataMemoryCache){
			if(redisCache.isCacheAvaliable()) return redisCache.getNextNode(curNodeName,wfId);
			return memoryCache.getNextNode(curNodeName,wfId);
		}
		return null;
	}
}
