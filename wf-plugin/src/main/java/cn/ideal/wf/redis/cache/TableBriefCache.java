package cn.ideal.wf.redis.cache;

/**
 * 工作流表单元素缓存
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

import cn.ideal.wf.model.WorkflowTableBrief;
import cn.ideal.wf.service.WorkflowTableService;

@Component
@Order(value=2)
public class TableBriefCache implements CommandLineRunner {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private static final String TB_KEY = "TB";
	@Autowired
    private RedisTemplate<String,Object> redisTemplate;	 
	@Autowired
    private WorkflowTableService tbService;
	
	public static HashOperations<String, Long, WorkflowTableBrief> hashTableBrief;
	
	@Override
	public void run(String... args) throws Exception {		
		logger.info("启动时数据加载");
		hashTableBrief = redisTemplate.opsForHash();
		List<WorkflowTableBrief> wftbs = tbService.findAll();
		for(WorkflowTableBrief wftb : wftbs){
			hashTableBrief.put(TB_KEY, wftb.getTbId(), wftb);
		}
	}
	
	public static WorkflowTableBrief getValue(Long tbId){
		return hashTableBrief.get(TB_KEY,tbId);
	}
	
	public static Map<Long,WorkflowTableBrief> getAll(){
		return hashTableBrief.entries(TB_KEY);
	}
	
	public static void save(WorkflowTableBrief wftb) {
		hashTableBrief.put(TB_KEY, wftb.getTbId(), wftb);
	}

	public static void update(WorkflowTableBrief wftb) {
		hashTableBrief.put(TB_KEY,wftb.getTbId(), wftb);
	}
 
	public static void delete(Long wfId) {
		hashTableBrief.delete(TB_KEY, wfId);
	}
	
	public static void put(WorkflowTableBrief wftb){
		hashTableBrief.put(TB_KEY,wftb.getTbId(), wftb);
	}
}

