package cn.ideal.wf.cache.factory;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cn.ideal.wf.model.Workflow;

@Component
public class WorkflowCacheFactory {
	@Autowired
	private cn.ideal.wf.cache.redis.WorkflowCache redisCache;
	@Autowired
	private cn.ideal.wf.cache.WorkflowCache memoryCache;
	@Value("${data.memory.cache}")
	boolean dataMemoryCache;
	
	public Workflow getValue(Long wfId){
		if(dataMemoryCache){
			if(redisCache.isCacheAvaliable()) return redisCache.getValue(wfId);
			return memoryCache.getValue(wfId);
		}
		return null;
	}
	
	public Map<Long,Workflow> getAll(){
		if(dataMemoryCache){
			if(redisCache.isCacheAvaliable()) return redisCache.getAll();
			return memoryCache.getAll();
		}
		return null;
	}
	
	public void save(Workflow wftb) {
		if(dataMemoryCache){
			if(redisCache.isCacheAvaliable()) redisCache.put(wftb);
			memoryCache.put(wftb);
		}
	}

	public void update(Workflow wftb) {
		if(dataMemoryCache){
			if(redisCache.isCacheAvaliable()) redisCache.put(wftb);
			memoryCache.put(wftb);
		}
	}
 
	public void delete(Long wfId) {
		if(dataMemoryCache){
			if(redisCache.isCacheAvaliable()) redisCache.delete(wfId);
			memoryCache.delete(wfId);
		}
	}
	
	public void put(Workflow wftb){
		if(dataMemoryCache){
			if(redisCache.isCacheAvaliable()) redisCache.put(wftb);
			memoryCache.put(wftb);	
		}
	}
}
