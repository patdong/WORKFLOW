package cn.ideal.wfpf.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.ideal.wfpf.dao.NodeMapper;
import cn.ideal.wfpf.model.Node;
import cn.ideal.wfpf.service.NodeService;

@Service
public class NodeServiceImpl implements NodeService {

	@Autowired
	private NodeMapper nodeMapper;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Node save(Node node) {
		node.setCreatedDate(new Date());
		node.setModifiedDate(new Date());
		node.setStatus("有效");
		if(node.getUsers() != null && node.getUsers().size() > 0 )node.setuType("用户");
		if(node.getRole() != null && node.getRole().getRoleId() != null)node.setuType("角色");
		if(node.getuType() == null) node.setuType("用户");
		int idx = nodeMapper.save(node);
		if(idx > 0 ) {
			if(node.getPreNodes() != null && node.getPreNodes().size() > 0) nodeMapper.saveNodeNodes(node);
			if(node.getUsers() != null && node.getUsers().size() > 0 ) nodeMapper.saveUser(node);
			if(node.getRole() != null && node.getRole().getRoleId() != null) nodeMapper.saveRole(node);
			if(node.getNodeAction() != null) nodeMapper.saveNodeAction(node);
			if(node.getButtons() != null) nodeMapper.saveNodeButton(node);
			return node;
		}
		return null;
	}

	/**
	 * 删除节点及后续节点，删除节点的所有关联关系
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void delete(Long nodeId) {
		List<Node> sufNodes = nodeMapper.findSufNode(nodeId);			
		//如果删除节点的直接后续节点对应一个父节点，则递归删除
		if(sufNodes.size() > 1){			
			for(Node node : sufNodes){
				delete(node.getNodeId());
				nodeMapper.deleteNodeNodes(nodeId);
				nodeMapper.deleteNode(nodeId);
			}
		}else if(sufNodes.size() == 1){
			//否则只删除当前最近的关系，循环其实至多只有一条记录			
			for(Node node : sufNodes){
				List<Node> preNodes = nodeMapper.findPreNode(node.getNodeId());
				if(preNodes.size() == 0 || preNodes.size() == 1) {
					delete(node.getNodeId());
					nodeMapper.deleteNodeNodes(nodeId);
					nodeMapper.deleteNode(nodeId);
				}
				else{
					for(Node preNode : preNodes){				
						if(preNode.getNodeId().compareTo(nodeId) == 0){
							nodeMapper.deleteNodeNode(nodeId,node.getNodeId());	
							nodeMapper.deleteNodeNodes(nodeId);
							nodeMapper.deleteNode(nodeId);
						}
					}
				}
				
			}
		}else if(sufNodes.size() == 0){
			nodeMapper.deleteNodeNodes(nodeId);
			nodeMapper.deleteNode(nodeId);
		}
		
	}

	@Override
	public List<Node> findAll(Long wfId) {
		return nodeMapper.findAll(wfId);
	}

	@Override
	public List<Node> findAllOnlyNode(Long wfId) {
		return nodeMapper.findAllOnlyNode(wfId);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Node update(Node node) {
		if(node.getUsers() != null && node.getUsers().size() > 0)node.setuType("用户");
		if(node.getRole() != null && node.getRole().getRoleId() != null)node.setuType("角色");
		if(node.getuType() == null) node.setuType("用户");
		int idx = nodeMapper.update(node);
		if(idx > 0 ) {
			node.setCreatedDate(new Date());
			if(node.getUsers() != null && node.getUsers().size() > 0) {
				nodeMapper.deleteUser(node.getNodeId());
				nodeMapper.saveUser(node);
			}
			if(node.getRole() != null && node.getRole().getRoleId() != null) {
				nodeMapper.deleteRole(node.getNodeId());
				nodeMapper.saveRole(node);
			}
			
			if(node.getButtons() != null && node.getButtons().size() > 0) {
				nodeMapper.deleteNodeButton(node.getNodeId());
				nodeMapper.saveNodeButton(node);
			}
			
			nodeMapper.deleteNodeAction(node.getNodeId());
			nodeMapper.saveNodeAction(node);
			
			return node;
		}
		return null;
	}

	@Override
	public Node find(Long nodeId) {
		return nodeMapper.find(nodeId);
	}

	/**
	 * 删除指定节点
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public Node setInvalid(Long nodeId) {
		Node node = new Node();
		node.setNodeId(nodeId);
		node.setStatus("无效");
		int idx = nodeMapper.updateStatus(node);
		if(idx > 0) return this.find(nodeId);
		return null;
	}
	
	@Override
	public Node setValid(Long nodeId) {
		Node node = new Node();
		node.setNodeId(nodeId);
		node.setStatus("有效");
		int idx = nodeMapper.updateStatus(node);
		if(idx > 0) return this.find(nodeId);
		return null;
	}

	@Override
	public Node setFrozeing(Long nodeId) {
		Node node = new Node();
		node.setNodeId(nodeId);
		node.setStatus("冻结");
		int idx = nodeMapper.updateStatus(node);
		if(idx > 0) return this.find(nodeId);
		return null;
	}

	@Override
	public Node saveNodeNode(Node node) {
		int idx = nodeMapper.saveNodeNodes(node);
		if(idx > 0){
			return nodeMapper.find(node.getNodeId());
		}
		
		return null;
	}
}
