package cn.ideal.wf.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ideal.wf.dao.WorkflowMapper;
import cn.ideal.wf.model.Workflow;
import cn.ideal.wf.service.WorkflowService;

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
	public List<Workflow> findAll() {
		return workflowMapper.findAll();
	}

	@Override
	public Workflow find(Long key) {
		return workflowMapper.find(key);
	}

	@Override
	public List<Workflow> findAll(Long pageNumber,Long pageSize) {
		return workflowMapper.findAPage((pageNumber-1)*pageSize, pageSize);
	}

}
