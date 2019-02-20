package cn.ideal.wfpf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import cn.ideal.wfpf.model.Node;

@Mapper
public interface NodeMapper {
	
	int save(Node node);
	
	int update(Node node);
	
	int updateStatus(Node node);
	
	int saveNodeNodes(Node node);
	
	int saveUser(Node node);
	
	int saveRole(Node node);
	
	int saveNodeAction(Node node);
	
	int saveNodeButton(Node node);
	
	void deleteUser(Long nodeId);
	
	void deleteRole(Long nodeId);
	
	void deleteNodeAction(Long nodeId);
	
	void deleteNodeButton(Long nodeId);
	
	Node find(Long nodeId);
	
	List<Node> findAll(Long wfId);
	
	List<Node> findAllOnlyNode(Long wfId);
	
	void deleteNodeNodes(Long nodeId);
	
	List<Node> findSufNode(Long nodeId);
	
	List<Node> findPreNode(Long nodeId);
	
	void deleteNode(Long nodeId);
		
}
