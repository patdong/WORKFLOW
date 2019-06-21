package cn.ideal.wf.cache.redis;

/**
 * 工作流缓存
 * @author 郭佟燕
 * @version 2.0
 */
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
import cn.ideal.wf.service.WorkflowWFService;

@Component
@Order(value=2)
public class WorkflowCache {//implements CommandLineRunner {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final String WF_KEY = "WF";
	private Boolean cacheAvailable;
	
    private RedisTemplate<String,Object> redisTemplate;	 
	@Autowired
    private WorkflowWFService workflowWFService;
	
	private HashOperations<String, String, Workflow> hashWorkflow;
		
	private void init(){						
		logger.info("--Redis--加载工作流数据");
		
		hashWorkflow = redisTemplate.opsForHash();
		List<Workflow> wfs = workflowWFService.findHavingBindTable();
		for(Workflow wf : wfs){
			hashWorkflow.put(WF_KEY, wf.getWfId().toString(), wf);
		}

	}
	
	public Workflow getValue(Long wfId){
		if(hashWorkflow == null) init();
		return hashWorkflow.get(WF_KEY, wfId.toString());		
	}
	
	public Map<Long,Workflow> getAll(){
		if(hashWorkflow == null) init();
		Map<String,Workflow> wfs = hashWorkflow.entries(WF_KEY);
		Map<Long,Workflow> newWfs = new HashMap<Long,Workflow>();
		for(String key : wfs.keySet()){
			newWfs.put(Long.parseLong(key), wfs.get(key));
		}
		
		return newWfs;
	}
	
	public void save(Workflow wf) {
		if(hashWorkflow == null) init();
		hashWorkflow.put(WF_KEY, wf.getWfId().toString(), wf);
	}

	public void update(Workflow wf) {
		if(hashWorkflow == null) init();
		hashWorkflow.put(WF_KEY, wf.getWfId().toString(), wf);
	}
 
	public void delete(Long wfId) {
		if(hashWorkflow == null) init();
		hashWorkflow.delete(WF_KEY, wfId.toString());
	}
	
	public void put(Workflow wf){
		if(hashWorkflow == null) init();
		hashWorkflow.put(WF_KEY, wf.getWfId().toString(), wf);
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

