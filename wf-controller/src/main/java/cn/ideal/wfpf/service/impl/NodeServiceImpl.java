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
		nodeMapper.deleteNodeNodes(nodeId);
		nodeMapper.deleteNode(nodeId);
		if(sufNodes.size() > 0){			
			for(Node node : sufNodes){
				delete(node.getNodeId());
			}
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

	
}
