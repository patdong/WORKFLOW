package cn.ideal.wf.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import cn.ideal.wf.model.WorkflowNode;
import cn.ideal.wf.model.WorkflowUser;

@Mapper
public interface WorkflowNodeMapper {
	
	List<WorkflowNode> findNextNodes(WorkflowNode wfn);
	
	List<WorkflowUser> findNodeUsers(Long nodeId);
	
	List<WorkflowUser> findNodeUsersByNodeName(String nodeName);

	List<WorkflowNode> findSeqNodes(Long moduleId);
	
	List<WorkflowNode> findSeqNodesWithFlow(Map<String,Long> conds);

}
