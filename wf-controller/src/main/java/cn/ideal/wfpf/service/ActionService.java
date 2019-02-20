package cn.ideal.wfpf.service;

import java.util.List;

import cn.ideal.wf.action.Action;

public interface ActionService {

	List<Action> findWfAll();
	
	List<Action> findBtnAll();
}
