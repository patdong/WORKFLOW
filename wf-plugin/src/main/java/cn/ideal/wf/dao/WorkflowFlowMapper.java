package cn.ideal.wf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import cn.ideal.wf.model.WorkflowFlow;
import cn.ideal.wf.model.WorkflowUser;

@Mapper
public interface WorkflowFlowMapper {
	
	/**
	 * 流程已启动，增加新的流程
	 */
	int addFlow(WorkflowFlow workflow);

	/**
	 * 结束指定的流程
	 */
	int endFlow(WorkflowFlow workflow);
	
	/**
	 * 结束业务流程
	 */
	int endFlow(Long bizId);
	
	/**
	 * 获得未完成的流程
	 */
	WorkflowFlow findDoingFlow(Long bizId);
	
	/**
	 * 获得所有已完成的流程
	 */
	List<WorkflowFlow> findDongFlow(Long bizId);
	
	/**
	 * 获取指定业务的所有流程记录
	 */
	List<WorkflowFlow> findAll(Long bizId);
	
	/**
	 * 获取指定的业务流程
	 */
	WorkflowFlow find(Long flowId);

	List<WorkflowUser> findWorkflowUsers(Long bizId);
	
	List<WorkflowFlow> findWorkflowWithSteps(Long bizId);
}
