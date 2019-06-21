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
	
	private Map<Long, WorkflowTableBrief> hashTableBrief;
	private ApplicationContext context;

    public ApplicationContext getApplicationContext() {
        return context;
    }
    @Override
    public void setApplicationContext(ApplicationContext ac)throws BeansException {
        context = ac;
    }	
	
	private void init()  {
		System.out.println("内存加载表单数据");
		
		WorkflowTableService tbService = context.getBean(WorkflowTableService.class);
		
		List<WorkflowTableBrief> wftbs = tbService.findAll();
		hashTableBrief = new HashMap<Long, WorkflowTableBrief>();
		for(WorkflowTableBrief wftb : wftbs){
			hashTableBrief.put(wftb.getTbId(), wftb);
		}		
	}
	
	public WorkflowTableBrief getValue(Long tbId){
		if(hashTableBrief == null) {
			init();
		}

		return hashTableBrief.get(tbId);
	}
	
	public Map<Long,WorkflowTableBrief> getAll(){
		if(hashTableBrief == null) {
			init();
		}
		return hashTableBrief;
	}
	
	public void save(WorkflowTableBrief wftb) {
		if(hashTableBrief == null) {
			init();
		}
		hashTableBrief.put(wftb.getTbId(), wftb);
	}

	public void update(WorkflowTableBrief wftb) {
		if(hashTableBrief == null) {
			init();
		}
		hashTableBrief.put(wftb.getTbId(), wftb);
	}
 
	public void delete(Long wfId) {
		if(hashTableBrief == null) {
			init();
		}
		hashTableBrief.remove(wfId);
	}
	
	public void put(WorkflowTableBrief wftb){
		if(hashTableBrief == null) {
			init();
		}
		hashTableBrief.put(wftb.getTbId(), wftb);
	}
}
