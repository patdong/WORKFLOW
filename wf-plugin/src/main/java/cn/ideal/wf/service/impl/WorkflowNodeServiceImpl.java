package cn.ideal.wf.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ideal.wf.cache.WorkflowNodeCache;
import cn.ideal.wf.common.WFConstants;
import cn.ideal.wf.dao.WorkflowNodeMapper;
import cn.ideal.wf.model.WorkflowAction;
import cn.ideal.wf.model.WorkflowFlow;
import cn.ideal.wf.model.WorkflowNode;
import cn.ideal.wf.model.WorkflowUser;
import cn.ideal.wf.service.WorkflowFlowService;
import cn.ideal.wf.service.WorkflowNodeService;
import cn.ideal.wf.tree.NodeTreeService;

@Service
public class WorkflowNodeServiceImpl implements WorkflowNodeService{

	@Autowired
	private WorkflowNodeMapper workflowNodeMapper;
	@Autowired
	private WorkflowFlowService workflowFlowService;
	@Autowired
	private NodeTreeService singleChainNodeTreeService;
	
	@Override
	public List<WorkflowNode> findNextNodes(Long nodeId) {
		return null;
	}

	/**
	 * 通过缓存获取节点信息
	 */
	@Override
	public List<WorkflowNode> findNextNodes(String nodeName, Long wfId) {
		if(nodeName == null) return new ArrayList<WorkflowNode>();
		if(wfId == null) return new ArrayList<WorkflowNode>();
		WorkflowNode wfn = new WorkflowNode();
		wfn.setNodeName(nodeName);
		wfn.setWfId(wfId);
		if(nodeName.equals(WFConstants.WF_NODE_STRAT)) {
			List<WorkflowNode> wfns = new ArrayList<WorkflowNode>();
			wfns.add(WorkflowNodeCache.getFirstNode(wfId));
			return wfns;
		}
		else return WorkflowNodeCache.getNextNode(nodeName, wfId);
	}

	@Override
	public List<WorkflowUser> findNodeUsers(Long nodeId) {
		return workflowNodeMapper.findNodeUsers(nodeId);
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

	@Override
	public List<WorkflowNode> findAll(Long wfId) {
		return workflowNodeMapper.findAll(wfId);
	}

	@Override
	public WorkflowNode[][] getTreeNodes(Long wfId, Long bizId) {
		List<WorkflowNode> nodes = this.findAll(wfId);
		List<WorkflowFlow> wfflows = workflowFlowService.findAll(bizId,wfId);
		for(WorkflowFlow wf : wfflows){
			for(WorkflowNode node :nodes){
				if(wf.getNodeName().equals(node.getNodeName())) {
					//设置办理完毕的节点
					if(wf.getFinishedDate() != null) node.setPassed("passed");
					else node.setPassed("passing");
				}
			}
		}
		return singleChainNodeTreeService.decorateNodeTree(nodes);
	}
	
	@Override
	public WorkflowNode[][] getTreeNodes(Long wfId) {
		List<WorkflowNode> nodes = this.findAll(wfId);
		return singleChainNodeTreeService.decorateNodeTree(nodes);		
	}

	@Override
	public WorkflowNode findNode(Long nodeId) {
		return workflowNodeMapper.find(nodeId);
	}

	@Override
	public WorkflowNode findNode(String nodeName, Long wfId) {
		return workflowNodeMapper.findByNodeName(nodeName, wfId);
	}

	@Override
	public List<WorkflowAction> findButtonsByNodeName(String nodeName, Long wfId) {
		return workflowNodeMapper.findButtonsByNodeName(nodeName, wfId);
	}

	@Override
	public WorkflowNode findFirstNode(Long wfId) {
		return WorkflowNodeCache.getFirstNode(wfId);
	}

	@Override
	public List<WorkflowNode> findRelSufNodes(Long nodeId, Long wfId) {
		WorkflowNode[][] wfNodes = this.getTreeNodes(wfId);
		List<WorkflowNode> wfns = new ArrayList<WorkflowNode>();
		WorkflowNode node = null;
		boolean outer = false;
		for(int i=0;i<wfNodes.length;i++){
			for(int j=0;j<wfNodes[i].length;j++){
				if(wfNodes[i][j].getStyle() != null && wfNodes[i][j].getStyle().equals("node")){
					if(wfNodes[i][j].getNodeId().compareTo(nodeId) == 0){
						node = wfNodes[i][j];
						outer = true;
						break;
					}
				}
			}
			if(outer) break;
		}
		
		for(int j=node.getDepth().intValue()+1;j<wfNodes[0].length;j++){
			for(int i=node.getHeight().intValue();i>=0;i--){			
				if(wfNodes[i][j].getStyle().equals("node")){
					wfns.add(wfNodes[i][j]);
					break;
				}
			}
		}
		return wfns;
	}
}
