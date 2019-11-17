package cn.ideal.wf.dao;

import org.apache.ibatis.annotations.Mapper;

import cn.ideal.wf.model.WorkflowAnswer;

@Mapper
public interface WorkflowAnswerMapper {
	
	/**
	 * 分发
	 * @param wfa
	 * @return
	 */
	int dispense(WorkflowAnswer wfa);
	
	/**
	 * 应答
	 * @param wfa
	 * @return
	 */
	int response(WorkflowAnswer wfa);
}
