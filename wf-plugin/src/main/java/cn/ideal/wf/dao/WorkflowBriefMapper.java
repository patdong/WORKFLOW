package cn.ideal.wf.dao;

import org.apache.ibatis.annotations.Mapper;

import cn.ideal.wf.model.WorkflowBrief;

@Mapper
public interface WorkflowBriefMapper {
	
	int addFlowBrief(WorkflowBrief wfb);
	
	int updateFlowBrief(WorkflowBrief wfb);
	
	int endFlowBrief(WorkflowBrief wfb);
	
	WorkflowBrief find(Long bizId);
	
	int updateStatusFlowBrief(WorkflowBrief wfb);
	
	WorkflowBrief findDoingFlow(Long bizId);
}
