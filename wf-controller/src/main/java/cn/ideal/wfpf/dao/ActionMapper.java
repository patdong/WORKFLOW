package cn.ideal.wfpf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import cn.ideal.wf.action.Action;

@Mapper
public interface ActionMapper {
	
	List<Action> findWfAction();
	
	List<Action> findBtnAction();	
	
}
