package cn.ideal.wf.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ideal.wf.dao.WorkflowAnswerMapper;
import cn.ideal.wf.model.WorkflowAnswer;
import cn.ideal.wf.service.WorkflowAnswerService;

@Service
public class WorkflowAnswerServiceImpl implements WorkflowAnswerService {

	@Autowired
	private WorkflowAnswerMapper workflowAnswerMapper;
	
	@Override
	public boolean dispense(WorkflowAnswer wfa) {
		int idx = workflowAnswerMapper.dispense(wfa);
		if(idx == 1) return true;
		return false;
	}

	@Override
	public boolean response(WorkflowAnswer wfa) {
		int idx = workflowAnswerMapper.response(wfa);
		if(idx == 1) return true;
		return false;
	}

}
