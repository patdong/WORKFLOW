package cn.ideal.wf.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.ideal.wf.dao.NodeMapper;
import cn.ideal.wf.model.Node;

@Service
public class NodeServiceImpl implements NodeService {

	@Autowired
	private NodeMapper nodeMapper;
	@Autowired
	private NodeTreeService singleChainNodeTreeService;
	
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
			
			return node;
		}
		return null;
	}

	@Override
	public void delete(Long nodeId) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Node> findAll(Long wfId) {
		return nodeMapper.findAll(wfId);
	}

	@Override
	public Node[][] getTreeNodes(Long wfId) {
		List<Node> nodes = this.findAll(wfId);
		return singleChainNodeTreeService.decorateNodeTree(nodes);		
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
