package cn.ideal.wf.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ideal.wf.dao.WorkflowNodeMapper;
import cn.ideal.wf.model.WorkflowNode;
import cn.ideal.wf.model.WorkflowUser;
import cn.ideal.wf.service.WorkflowNodeService;

@Service
public class WorkflowNodeServiceImpl implements WorkflowNodeService{

	@Autowired
	private WorkflowNodeMapper workflowNodeMapper;
	
	@Override
	public List<WorkflowNode> findNextNodes(Long nodeId) {
		return null;
	}

	@Override
	public List<WorkflowNode> findNextNodes(String nodeName, Long moduleId) {
		if(nodeName == null) return new ArrayList<WorkflowNode>();
		if(moduleId == null) return new ArrayList<WorkflowNode>();
		WorkflowNode wfn = new WorkflowNode();
		wfn.setNodeName(nodeName);
		wfn.setModuleId(moduleId);
		return workflowNodeMapper.findNextNodes(wfn);
	}

	@Override
	public List<WorkflowUser> findNodeUsers(Long nodeId) {
		return workflowNodeMapper.findNodeUsers(nodeId);
	}

	@Override
	public List<WorkflowNode> findSeqNodes(Long moduleId) {
		return workflowNodeMapper.findSeqNodes(moduleId);
	}

	@Override
	public List<WorkflowNode> findSeqNodes(Long moduleId, Long bizId) {
		Map<String,Long> conds = new HashMap<String,Long>();
		conds.put("moduleId", moduleId);
		conds.put("bizId", bizId);
		return workflowNodeMapper.findSeqNodesWithFlow(conds);
	}

	@Override
	public List<WorkflowUser> findNodeUsers(String nodeName) {
		return workflowNodeMapper.findNodeUsersByNodeName(nodeName);
	}

	@Override
	public WorkflowNode save(WorkflowNode node) {
		// TODO Auto-generated method stub
		return null;
	}

}
