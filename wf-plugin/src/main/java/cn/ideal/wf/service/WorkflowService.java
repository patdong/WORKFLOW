package cn.ideal.wf.service;

import java.util.List;

import cn.ideal.wf.model.Workflow;

public interface WorkflowService {

	Workflow save(Workflow obj);
	
	Workflow update(Workflow obj);
	
	List<Workflow> findAll();
	
	List<Workflow> findAll(Long pageNumber,Long pageSize);
	
	Workflow find(Long key);
}
