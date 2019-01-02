package cn.ideal.wf.service;

import java.util.List;

import cn.ideal.wf.model.Workflow;

public interface WorkflowService extends PageService<Workflow> {

	Workflow save(Workflow obj);
	
	Workflow update(Workflow obj);	
	
	Workflow find(Long key);
	
	List<Workflow> findAllBlindTable();
	
	Workflow removeBinding(Workflow obj);
}
