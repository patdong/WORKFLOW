package cn.ideal.wfpf.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ideal.wf.action.Action;
import cn.ideal.wfpf.dao.ActionMapper;
import cn.ideal.wfpf.service.ActionService;

@Service
public class ActionServiceImpl implements ActionService {
	@Autowired
	private ActionMapper actionMapper;
	
	@Override
	public List<Action> findWfAll() {
		return actionMapper.findWfAction();
	}

	@Override
	public List<Action> findBtnAll() {
		return actionMapper.findBtnAction();
	}

}
