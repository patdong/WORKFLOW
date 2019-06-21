package cn.ideal.wf.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.ideal.wf.model.WorkflowBrief;

@Mapper
public interface WorkflowBriefMapper {
	
	int addFlowBrief(WorkflowBrief wfb);
	
	int updateFlowBrief(WorkflowBrief wfb);
	
	int endFlowBrief(WorkflowBrief wfb);
	
	WorkflowBrief find(@Param("bizId") Long bizId,@Param("wfId") Long wfId);
	
	int updateStatusFlowBrief(WorkflowBrief wfb);
	
	WorkflowBrief findDoingFlow(@Param("bizId") Long bizId,@Param("wfId") Long wfId);
	
	int updateDispatchedUser(WorkflowBrief wfb);
}
