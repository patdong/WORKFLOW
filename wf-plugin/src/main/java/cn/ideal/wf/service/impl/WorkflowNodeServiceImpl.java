package cn.ideal.wf.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.ideal.wf.cache.factory.WorkflowNodeCacheFactory;
import cn.ideal.wf.common.WFConstants;
import cn.ideal.wf.dao.WorkflowNodeMapper;
import cn.ideal.wf.flowchat.draw.FlowChatService;
import cn.ideal.wf.model.FlowChatNode;
import cn.ideal.wf.model.Workflow;
import cn.ideal.wf.model.WorkflowAction;
import cn.ideal.wf.model.WorkflowFlow;
import cn.ideal.wf.model.WorkflowNode;
import cn.ideal.wf.model.WorkflowRole;
import cn.ideal.wf.model.WorkflowUser;
import cn.ideal.wf.service.WorkflowFlowService;
import cn.ideal.wf.service.WorkflowNodeService;
import cn.ideal.wf.service.WorkflowWFService;

@Service
public class WorkflowNodeServiceImpl implements WorkflowNodeService{

	@Autowired
	private WorkflowNodeMapper workflowNodeMapper;
	@Autowired
	private WorkflowFlowService workflowFlowService;
	@Autowired
	@Qualifier("richFlowChatService")
	private FlowChatService flowChatService;
	@Autowired
	private WorkflowNodeCacheFactory workflowNodeCacheFactory;
	@Autowired
	private WorkflowWFService workflowFWService;
	
	@Override
	public List<WorkflowNode> findNextNodes(Long nodeId) {
		return null;
	}

	/**
	 * 通过缓存获取节点信息
	 */
	@Override
	public List<WorkflowNode> findNextNodes(String nodeName, Long wfId,Long bizId) {
		if(nodeName == null) return new ArrayList<WorkflowNode>();
		if(wfId == null) return new ArrayList<WorkflowNode>();		
		if(nodeName.equals(WFConstants.WF_NODE_STRAT)) {
			List<WorkflowNode> wfns = new ArrayList<WorkflowNode>();
			//从缓存中获取
			WorkflowNode node = workflowNodeCacheFactory.getFirstNode(wfId);
			if(node == null){
				//从数据库中获取
				List<WorkflowNode> nodes = this.findAll(wfId);
				if(nodes != null){
					for(WorkflowNode item : nodes){
						if(item.getPreNodes() == null || item.getPreNodes().size() == 0) {
							wfns.add(item);
							return wfns;
						}else{
							boolean hasPreNode = false;
							for(WorkflowNode obj : item.getPreNodes()){
								if(obj.getType()!= null && obj.getType().equals("直接连接")) hasPreNode = true;
							}
							if(!hasPreNode) {
								wfns.add(item);
								return wfns;
							}
						}
					}
				}
			}
			else wfns.add(node);
			return wfns;
		}
		else {			
			List<WorkflowNode> res = workflowNodeCacheFactory.getNextNode(nodeName, wfId);
			if(res == null ){
				WorkflowNode wfn = workflowNodeMapper.findByNodeName(nodeName, wfId);
				res = workflowNodeMapper.findSufNode(wfn.getNodeId());											
			}
			
			//多节点时需判断是否必经节点已经办理过
			if(res.size() > 1 && bizId != null){
				for(WorkflowNode node : res){
					if(node.getNecessary() != null && node.getNecessary().equals("是")){
						List<WorkflowFlow> wffs = workflowFlowService.findWorkflowWithSteps(bizId, wfId, node.getNodeName());
						if(wffs == null || wffs.size() == 0) {
							res.clear();
							res.add(node);
							break;
						}
					}
				}
			}
			return res;
		}
	}

	@Override
	public List<WorkflowUser> findNodeUsers(Long nodeId) {
		return workflowNodeMapper.findNodeUsers(nodeId);
	}

	@Override
	public List<WorkflowUser> findNodeUsers(String nodeName , Long wfId) {
		return workflowNodeMapper.findNodeUsersByNodeName(nodeName,wfId);
	}

	@Override
	public WorkflowNode save(WorkflowNode node) {
		int idx = workflowNodeMapper.save(node);
		if(idx == 1) return node;
		return null;
	}

	@Override
	public List<WorkflowNode> findAll(Long wfId) {
		return workflowNodeMapper.findAll(wfId);
	}
	
	@Override
	public WorkflowNode findNode(Long nodeId) {
		return workflowNodeMapper.find(nodeId);
	}

	@Override
	public WorkflowNode findNode(String nodeName, Long wfId) {
		//对于创建节点的处理
		if(nodeName.equals(WFConstants.WF_NODE_START)) {
			WorkflowNode wfn = new WorkflowNode();
			wfn.setNodeName(WFConstants.WF_NODE_START);
			wfn.setnType(WFConstants.WF_NODE_TYPE_SINGLE);
			return wfn;
		}
		else return workflowNodeMapper.findByNodeName(nodeName, wfId);
		
	}

	@Override
	public List<WorkflowAction> findButtonsByNodeName(String nodeName, Long wfId) {
		return workflowNodeMapper.findButtonsByNodeName(nodeName, wfId);
	}

	@Override
	public WorkflowNode findFirstNode(Long wfId) {
		if(wfId == null) return null;
		WorkflowNode node = workflowNodeCacheFactory.getFirstNode(wfId);
		
		return node;
	}

	@Override
	public List<WorkflowNode> findRelSufNodes(Long nodeId, Long wfId) {
		List<WorkflowNode> nodes = this.findAll(wfId);
		for(int i=0;i<nodes.size();i++){
			if(nodes.get(i).getNodeId().compareTo(nodeId) == 0){				
				nodes.remove(i);
				break;
			}
		}
		
		return nodes;
	}
	
	@Override
	public List<FlowChatNode> findAllForFlowChat(long wfId){
		return workflowNodeMapper.findAllForFlowChat(wfId);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Workflow clone(Long wfId,Map<Long,List<WorkflowUser>> users, Map<Long,WorkflowRole> roles) {
		//新建一个流程
		Workflow wf = workflowFWService.find(wfId);
		if(wf == null) return null;
		wf.setWfName(wf.getWfName()+"_自定义");
		wf.setType("自定义级");
		wf = workflowFWService.save(wf);
		//克隆一份流程节点
		List<WorkflowNode> wfNodes = workflowNodeMapper.findAll(wfId);
		Map<Long,WorkflowNode> nodeIdMapping = new HashMap<Long,WorkflowNode>();
		for(WorkflowNode node : wfNodes){
			WorkflowNode newNode = new WorkflowNode();
			newNode.setNodeName(node.getNodeName());
			newNode.setTimeLimit(node.getTimeLimit());
			newNode.setuType(node.getuType());
			newNode.setnType(node.getnType());
			newNode.setStatus(node.getStatus());
			newNode.setCreatedDate(new Date());
			newNode.setModifiedDate(new Date());
			newNode.setWfId(wf.getWfId());
			newNode = this.save(newNode);
			//建立克隆前后节点的编号
			nodeIdMapping.put(node.getNodeId(), newNode);			
		}		
		//克隆一份流程节点间关系
		for(WorkflowNode node : wfNodes){
			List<WorkflowNode> wfNNodes = node.getSufNodes();
			if(wfNNodes.size() > 0){
				WorkflowNode newNode = nodeIdMapping.get(node.getNodeId());
				newNode.setSufNodes(wfNNodes);
				for(WorkflowNode pnode : wfNNodes){				
					pnode.setNodeId(nodeIdMapping.get(pnode.getNodeId()).getNodeId());
					pnode.setCreatedDate(new Date());
				}			
				workflowNodeMapper.saveNodeNodes(newNode);
			}
		}
		//克隆一份流程节点上的行为
		for(WorkflowNode node : wfNodes){
			WorkflowAction action = node.getAction();
			if(action != null){
				WorkflowNode newNode = nodeIdMapping.get(node.getNodeId());
				newNode.setAction(action);
				action.setCreatedDate(new Date());
				workflowNodeMapper.saveNodeAction(newNode);
			}
		}
		for(WorkflowNode node : wfNodes){
			List<WorkflowAction> buttons = node.getButtons();
			if(buttons.size() > 0){
				WorkflowNode newNode = nodeIdMapping.get(node.getNodeId());
				newNode.setButtons(buttons);
				workflowNodeMapper.saveNodeButton(newNode);
			}
		}
		//重置流程节点办理人员
		for(WorkflowNode node : wfNodes){
			WorkflowNode newNode = nodeIdMapping.get(node.getNodeId());
			if(users.get(node.getNodeId()) != null){
				//取用户				
				newNode.setUsers(users.get(node.getNodeId()));
				workflowNodeMapper.saveUser(newNode);
			}else if(roles.get(node.getNodeId()) != null){
				//取用户不成功取角色
				newNode.setRole(roles.get(node.getNodeId()));
				workflowNodeMapper.saveRole(newNode);
			}else{
				//取角色不成功取默认的配置
				node.setNodeId(newNode.getNodeId());
				if(node.getUsers() != null && node.getUsers().size() > 0) workflowNodeMapper.saveUser(node);
				else if(node.getRole() != null) workflowNodeMapper.saveRole(node);
			}
		}
		return wf;
	}

	@Override
	public WorkflowAction findButton(String actionCodeName) {
		return workflowNodeMapper.findButton(actionCodeName);
	}
}
