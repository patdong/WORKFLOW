package cn.ideal.wf.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import cn.ideal.wf.model.Workflow;
import cn.ideal.wf.service.WorkflowWFService;

@Component("WorkflowCache1")
public class WorkflowCache implements ApplicationContextAware{
	private static Map<Long, Workflow> hashWorkflow;
	private static ApplicationContext context;

    public static ApplicationContext getApplicationContext() {
        return context;
    }
    @Override
    public void setApplicationContext(ApplicationContext ac)throws BeansException {
        context = ac;
    }
	
	public static void init() {	
		WorkflowWFService workflowWFService = context.getBean(WorkflowWFService.class);
		
		List<Workflow> wfs = workflowWFService.findHavingBindTable();
		hashWorkflow = new HashMap<Long, Workflow>();
		for(Workflow wf : wfs){
			hashWorkflow.put(wf.getWfId(), wf);
		}
			
	}
	
	public static Workflow getValue(Long wfId){
		if(hashWorkflow == null) {
			init();
		}
		return hashWorkflow.get(wfId);		
	}
	
	public static Map<Long,Workflow> getAll(){
		if(hashWorkflow == null) {
			init();
		}
		return hashWorkflow;
	}
	
	public static void save(Workflow wf) {
		if(hashWorkflow == null) {
			init();
		}
		hashWorkflow.put(wf.getWfId(), wf);
	}

	public static void update(Workflow wf) {
		if(hashWorkflow == null) {
			init();
		}
		hashWorkflow.put(wf.getWfId(), wf);
	}
 
	public static void delete(Long wfId) {
		if(hashWorkflow == null) {
			init();
		}
		hashWorkflow.remove(wfId);
	}
	
	public static void put(Workflow wf){
		if(hashWorkflow == null) {
			init();
		}
		hashWorkflow.put(wf.getWfId(), wf);
	}

}
