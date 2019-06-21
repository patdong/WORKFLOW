package cn.ideal.wf.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ideal.wf.dao.WorkflowWFMapper;
import cn.ideal.wf.model.Workflow;
import cn.ideal.wf.service.WorkflowWFService;

@Service
public class WorkflowWFServiceImpl implements WorkflowWFService{
	
	@Autowired
	private WorkflowWFMapper workflowWFMapper;
	@Override
	public Workflow find(Long key) {
		return workflowWFMapper.find(key);
	}

	@Override
	public List<Workflow> findHavingBindTable() {
		return workflowWFMapper.findHavingBindTable();
	}

	@Override
	public Workflow save(Workflow obj) {
		int idx = workflowWFMapper.save(obj);
		if(idx == 1) return obj;
		return null;
	}

}
