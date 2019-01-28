package cn.ideal.wf.cache;

/**
 * 工作流缓存
 * @author 郭佟燕
 * @version 2.0
 */
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
import cn.ideal.wf.service.WorkflowWFService;

@Component
@Order(value=2)
public class WorkflowCache implements CommandLineRunner {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final String WF_KEY = "WF";
	@Autowired
    private RedisTemplate<String,Object> redisTemplate;	 
	@Autowired
    private WorkflowWFService workflowWFService;
	
	public static HashOperations<String, Long, Workflow> hashWorkflow;
	
	@Override
	public void run(String... args) throws Exception {		
		logger.info("启动时数据加载");
		hashWorkflow = redisTemplate.opsForHash();
		List<Workflow> wfs = workflowWFService.findAllBlindTable();
		for(Workflow wf : wfs){
			hashWorkflow.put(WF_KEY, wf.getWfId(), wf);
		}

	}
	
	public static Workflow getValue(Long wfId){
		return hashWorkflow.get(WF_KEY, wfId);
	}
	
	public static Map<Long,Workflow> getAll(){
		return hashWorkflow.entries(WF_KEY);
	}
	
	public static void save(Workflow wf) {
		hashWorkflow.put(WF_KEY, wf.getWfId(), wf);
	}

	public static void update(Workflow wf) {
		hashWorkflow.put(WF_KEY, wf.getWfId(), wf);
	}
 
	public static void delete(Long wfId) {
		hashWorkflow.delete(WF_KEY, wfId);
	}

}

