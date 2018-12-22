package cn.ideal.wf.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import cn.ideal.wf.model.Node;

@Mapper
public interface NodeMapper {
	
	int save(Node node);
	
	int update(Node node);
	
	int updateStatus(Node node);
	
	int saveNodeNodes(Node node);
	
	int saveUser(Node node);
	
	int saveRole(Node node);
	
	void deleteUser(Long nodeId);
	
	void deleteRole(Long nodeId);
	
	Node find(Long nodeId);
	
	List<Node> findAll(Long wfId);
	
	List<Node> findAllOnlyNode(Long wfId);
	
}
