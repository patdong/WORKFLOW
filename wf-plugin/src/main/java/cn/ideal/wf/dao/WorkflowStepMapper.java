package cn.ideal.wf.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import cn.ideal.wf.model.WorkflowStep;

@Mapper
public interface WorkflowStepMapper {
	
	/**
	 * 增加流程操作
	 */
	int addFlowStep(WorkflowStep workflowStep);
	
	/**
	 * 结束流程操作
	 */
	int endFlowStep(WorkflowStep flowStep);	
	
	/**
	 * 获得指定用户的操作流程
	 */
	WorkflowStep findWrokflowStep(Map<String,Object> conds);
	
	/**
	 * 获取指定业务的所有流程操作记录
	 */
	List<WorkflowStep> findAll(Long bizId);
	
	WorkflowStep find(Long stepId);
	
	List<WorkflowStep> findAllByFlowId(Long flowId);
	
	int setWorkflowStepUser(WorkflowStep workflowStep);
}
