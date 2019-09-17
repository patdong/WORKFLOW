package cn.ideal.wf.cache.factory;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import cn.ideal.wf.model.WorkflowTableBrief;

@Component
public class TableBriefCacheFactory {
	@Autowired
	private cn.ideal.wf.cache.redis.TableBriefCache redisCache;
	@Autowired
	private cn.ideal.wf.cache.TableBriefCache memoryCache;
	@Autowired
	private cn.ideal.wf.service.WorkflowTableService wfTableService;
	@Value("${data.memory.cache}")
	boolean dataMemoryCache;
	
	public WorkflowTableBrief getValue(Long tbId){
		if(dataMemoryCache){
			if(redisCache.isCacheAvaliable()) return redisCache.getValue(tbId);
			return memoryCache.getValue(tbId);
		}
		return wfTableService.find(tbId);
	}
	
	public Map<Long,WorkflowTableBrief> getAll(){
		if(dataMemoryCache){
			if(redisCache.isCacheAvaliable()) return redisCache.getAll();
			return memoryCache.getAll();
		}
		return null;
	}
	
	public void save(WorkflowTableBrief wftb) {
		if(dataMemoryCache){
			if(redisCache.isCacheAvaliable()) redisCache.put(wftb);
			memoryCache.put(wftb);
		}
	}

	public void update(WorkflowTableBrief wftb) {
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
	
	public void put(WorkflowTableBrief wftb){
		if(dataMemoryCache){
			if(redisCache.isCacheAvaliable()) redisCache.put(wftb);
			memoryCache.put(wftb);	
		}
	}
}
