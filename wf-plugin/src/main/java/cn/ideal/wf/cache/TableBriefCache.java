package cn.ideal.wf.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import cn.ideal.wf.model.WorkflowTableBrief;
import cn.ideal.wf.service.WorkflowTableService;
@Component("TableBriefCache1")
public class TableBriefCache implements ApplicationContextAware{

	private static Map<Long, WorkflowTableBrief> hashTableBrief;
	private static ApplicationContext context;

    public static ApplicationContext getApplicationContext() {
        return context;
    }
    @Override
    public void setApplicationContext(ApplicationContext ac)throws BeansException {
        context = ac;
    }
	
	
	public static void init()  {
		WorkflowTableService tbService = context.getBean(WorkflowTableService.class);
		
		List<WorkflowTableBrief> wftbs = tbService.findAll();
		hashTableBrief = new HashMap<Long, WorkflowTableBrief>();
		for(WorkflowTableBrief wftb : wftbs){
			hashTableBrief.put(wftb.getTbId(), wftb);
		}		
	}
	
	public static WorkflowTableBrief getValue(Long tbId){
		if(hashTableBrief == null) {
			init();
		}

		return hashTableBrief.get(tbId);
	}
	
	public static Map<Long,WorkflowTableBrief> getAll(){
		if(hashTableBrief == null) {
			init();
		}
		return hashTableBrief;
	}
	
	public static void save(WorkflowTableBrief wftb) {
		if(hashTableBrief == null) {
			init();
		}
		hashTableBrief.put(wftb.getTbId(), wftb);
	}

	public static void update(WorkflowTableBrief wftb) {
		if(hashTableBrief == null) {
			init();
		}
		hashTableBrief.put(wftb.getTbId(), wftb);
	}
 
	public static void delete(Long wfId) {
		if(hashTableBrief == null) {
			init();
		}
		hashTableBrief.remove(wfId);
	}
	
	public static void put(WorkflowTableBrief wftb){
		if(hashTableBrief == null) {
			init();
		}
		hashTableBrief.put(wftb.getTbId(), wftb);
	}
}
