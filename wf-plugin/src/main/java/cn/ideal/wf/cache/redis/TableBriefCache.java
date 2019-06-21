package cn.ideal.wf.cache.redis;

/**
 * 工作流表单元素缓存
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

import cn.ideal.wf.model.WorkflowTableBrief;
import cn.ideal.wf.service.WorkflowTableService;

@Component
@Order(value=2)
public class TableBriefCache {//implements CommandLineRunner {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final String TB_KEY = "TB";
	private Boolean cacheAvailable;
    private RedisTemplate<String,Object> redisTemplate;	 
	@Autowired
    private WorkflowTableService tbService;
		
	private HashOperations<String, String, WorkflowTableBrief> hashTableBrief;
		
	private void init() {						
		logger.info("--Redis--加载表单数据");
		
		hashTableBrief = redisTemplate.opsForHash();
		List<WorkflowTableBrief> wftbs = tbService.findAll();
		for(WorkflowTableBrief wftb : wftbs){
			hashTableBrief.put(TB_KEY, wftb.getTbId().toString(), wftb);
		}
	}
	
	public WorkflowTableBrief getValue(Long tbId){
		if(hashTableBrief == null) init();
		return hashTableBrief.get(TB_KEY,tbId.toString());
	}
	
	public Map<Long,WorkflowTableBrief> getAll(){
		if(hashTableBrief == null) init();
		Map<String,WorkflowTableBrief> wftbs =  hashTableBrief.entries(TB_KEY);
		Map<Long,WorkflowTableBrief> newWftbs = new HashMap<Long,WorkflowTableBrief>();
		for(String key : wftbs.keySet()){
			newWftbs.put(Long.parseLong(key), wftbs.get(key));
		}
		
		return newWftbs;
	}
	
	public void save(WorkflowTableBrief wftb) {
		if(hashTableBrief == null) init();
		hashTableBrief.put(TB_KEY, wftb.getTbId().toString(), wftb);
	}

	public void update(WorkflowTableBrief wftb) {
		if(hashTableBrief == null) init();
		hashTableBrief.put(TB_KEY,wftb.getTbId().toString(), wftb);
	}
 
	public void delete(Long wfId) {
		if(hashTableBrief == null) init();
		hashTableBrief.delete(TB_KEY, wfId.toString());
	}
	
	public void put(WorkflowTableBrief wftb){
		if(hashTableBrief == null) init();
		hashTableBrief.put(TB_KEY,wftb.getTbId().toString(), wftb);
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

