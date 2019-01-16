package cn.ideal.wf.service;

/**
 * 工作流服务接口
 * @author 郭佟燕
 * @version 2.0
 */
import java.util.List;

import cn.ideal.wf.model.Workflow;

public interface WorkflowWFService{

	Workflow find(Long key);
	
	List<Workflow> findAllBlindTable();	

}
