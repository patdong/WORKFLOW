package cn.ideal.wf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.ideal.wf.model.FlowChatNode;
import cn.ideal.wf.model.WorkflowAction;
import cn.ideal.wf.model.WorkflowNode;
import cn.ideal.wf.model.WorkflowUser;

@Mapper
public interface WorkflowNodeMapper {
	
	List<WorkflowNode> findNextNodes(WorkflowNode wfn);
	
	List<WorkflowUser> findNodeUsers(Long nodeId);
	
	List<WorkflowUser> findNodeUsersByNodeName(@Param("nodeName") String nodeName,@Param("wfId") Long wfId);

	List<WorkflowNode> findAll(Long wfId);
	
	WorkflowNode find(Long nodeId);
	
	WorkflowNode findByNodeName(@Param("nodeName") String nodeName, @Param("wfId") Long wfId);
	
	List<WorkflowAction> findButtonsByNodeName(@Param("nodeName") String nodeName, @Param("wfId") Long wfId);
	
	List<FlowChatNode> findAllForFlowChat(long wfId);
	
	int save(WorkflowNode wfn);
	
	int saveNodeNodes(WorkflowNode wfn);
	
	int saveNodeAction(WorkflowNode wfn);
	
	int saveNodeButton(WorkflowNode wfn);
	
	int saveUser(WorkflowNode wfn);
	
	int saveRole(WorkflowNode wfn);
	
	WorkflowAction findButton(@Param("actionCodeName") String actionCodeName);
	
	List<WorkflowNode> findSufNode(Long nodeId);

}
