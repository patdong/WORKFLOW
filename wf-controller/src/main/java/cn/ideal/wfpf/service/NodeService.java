package cn.ideal.wfpf.service;

import java.util.List;

import cn.ideal.wfpf.model.Node;

public interface NodeService {

	Node save(Node node);
	
	Node update(Node node);
	
	Node setInvalid(Long nodeId);
	
	Node setValid(Long nodeId);
	
	void delete(Long nodeId);
	
	Node find(Long nodeId);
	
	List<Node> findAll(Long wfId);
	
	List<Node> findAllOnlyNode(Long wfId);
	
	Node setFrozeing(Long nodeId);
	
	Node saveNodeNode(Node node);
	
}
