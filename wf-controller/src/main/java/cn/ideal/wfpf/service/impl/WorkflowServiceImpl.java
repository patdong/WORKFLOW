package cn.ideal.wfpf.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ideal.wf.model.Workflow;
import cn.ideal.wfpf.dao.WorkflowMapper;
import cn.ideal.wfpf.model.Page;
import cn.ideal.wfpf.service.WorkflowService;

@Service
public class WorkflowServiceImpl implements WorkflowService{

	@Autowired
	private WorkflowMapper workflowMapper;
	
	@Override
	public Workflow save(Workflow obj) {
		obj.setStatus("有效");
		obj.setCreatedDate(new Date());
		int idx = workflowMapper.save(obj);
		if(idx == 1) return obj;
		return null;
	}

	@Override
	public Workflow update(Workflow obj) {
		int idx = workflowMapper.update(obj);
		if(idx > 0) return obj;
		return null;
	}	

	@Override
	public Workflow find(Long key) {
		return workflowMapper.find(key);
	}	

	@Override
	public List<Workflow> findAll() {
		return workflowMapper.findAll();
	}

	@Override
	public List<Workflow> findAll(Page<Workflow> page) {
		return workflowMapper.findAPage(page.getCurFirstRecord(),Page.pageSize);
	}

	@Override
	public List<Workflow> findAllBlindTable() {
		return workflowMapper.findAllBlindTable();
	}

	@Override
	public Workflow removeBinding(Workflow obj) {
		int idx = workflowMapper.removeBinding(obj);
		if(idx > 0) return this.find(obj.getWfId());
		return null;
	}
}
