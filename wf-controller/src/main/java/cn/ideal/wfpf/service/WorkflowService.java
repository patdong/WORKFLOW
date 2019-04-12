package cn.ideal.wfpf.service;

import java.util.List;

import cn.ideal.wfpf.model.WFPFWorkflow;

public interface WorkflowService extends PageService<WFPFWorkflow> {

	WFPFWorkflow save(WFPFWorkflow obj);
	
	WFPFWorkflow update(WFPFWorkflow obj);	
	
	WFPFWorkflow find(Long key);
	
	List<WFPFWorkflow> findAllBlindTable();
	
	boolean removeBinding(Long wfId);
	
	boolean setStatus(Long wfId, boolean status);
	
	boolean delete(Long wfId);
}
