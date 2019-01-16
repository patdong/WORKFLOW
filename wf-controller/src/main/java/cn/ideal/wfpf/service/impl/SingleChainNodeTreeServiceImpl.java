package cn.ideal.wfpf.service.impl;

/**
 * 生成单向链节点树
 * 支持节点水平位置的单进、单出
 *  
 *  --->O--->P-->Z
 *       --->X-->   
 *  
 * @author 郭佟燕
 * @version 2.0
 */

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import cn.ideal.wfpf.model.Node;
import cn.ideal.wfpf.service.NodeTreeService;

@Service
public class SingleChainNodeTreeServiceImpl implements NodeTreeService {

	@Override
	public Node[][] decorateNodeTree(List<Node> nodes) {
		setDepth(nodes, 2l, null);
		setHeight(nodes);
	
		return decorateTree(nodes);
	}
	
	/**
	 * 设置树的深度
	 * @param nodes
	 * @param depth
	 * @param sufNodes
	 * @return
	 */
	private void setDepth(List<Node> nodes, Long depth, List<Node> sufNodes){	
		//如果没有节点，则默认创建一个空节点。
		if(nodes.size() == 0) {
			Node node = new Node();
			node.setStyle("node");
			//设置初始值，方便前端页面控制
			node.setNodeId(0l);
			node.setSufNodes(new ArrayList<Node>());
			node.setPreNodes(new ArrayList<Node>());
			node.setDepth(2l);
			nodes.add(node);			
		}
		if(sufNodes == null){
			for(Node node : nodes){	
				//根节点的处理
				if(node.getPreNodes() != null && node.getPreNodes().size() == 0) {
					node.setDepth(depth);
					node.setHeight(0l);
					node.setInnerHeight(0l);					
					setDepth(nodes, depth+2,node.getSufNodes());
				}
			}			
		}else{
			//非根节点处理
			for(Node node : nodes){	
				for(Node sufNode : sufNodes){
					if(node.getNodeId().equals(sufNode.getNodeId())){
						node.setDepth(depth);
						setDepth(nodes, depth+2,node.getSufNodes());
					}
				}
			}
		}
		
	}

	/**
	 * 设置树的高度
	 * @param nodes
	 * @return
	 */
	private void setHeight(List<Node> nodes){
		Long depth = 0l;
		//获取节点最大深度
		for(Node node : nodes){
			if(node.getDepth().compareTo(depth) > 0 ) depth = node.getDepth();
		}
		//根据深度遍历
		for(int i=0; i<=depth; i++){
			Long height = 0l;
			for(Node node : nodes){
				//获取相同深度的节点
				if(node.getDepth().intValue() == i){
					//获取当前深度的所有节点，并对其子节点做高度设置
					Long innerHeight = 0l;
					//末梢节点无子节点
					if(node.getSufNodes() != null && node.getSufNodes().size() == 0) {
						if(node.getInnerHeight() == null) node.setInnerHeight(innerHeight);						
						if(node.getHeight() == null) node.setHeight(height);
					}
					//当前节点的后置节点的第一个height总是等于当前节点的height
					height = node.getHeight();
					for(Node snode : node.getSufNodes()){
						for(Node outernode : nodes){
							if(snode.getNodeId().equals(outernode.getNodeId())){
								if(outernode.getInnerHeight() == null) outernode.setInnerHeight(innerHeight);						
								if(outernode.getHeight() == null) outernode.setHeight(height);								
								//出现多个子节点时，需要修复前置节点的高度
								if(innerHeight > 0) mendFrontNodeHeight(nodes,i,height.intValue());
								height++;
								innerHeight++;
							}
						}
					}
				}
			}
		}		
	}
	
	/**
	 * 修复前置节点的高度
	 * @param nodes
	 * @param depth 当前深度之前的节点
	 * @param height 当前高度之后的节点
	 */
	private void mendFrontNodeHeight(List<Node> nodes , int depth, int height){
		for(int i = depth; i>0; i--){
			for(Node node : nodes){
				//获取相同深度的节点
				if(node.getDepth().intValue() == i){
					if(node.getHeight() >= height){
						//每个节点增加一个高度
						node.setHeight(node.getHeight()+1);
					}					
				}
			}
		}
	}
	
	
	/**
	 * 将节点装饰成一棵有方向的多叉树
	 */
	private Node[][] decorateTree(List<Node> nodes){
		Long depth = 0l;
		//获取树的宽度
		for(Node node : nodes){
			if(node.getDepth().compareTo(depth) > 0 ) depth = node.getDepth();
		}
		Long height = 0l;
		//获取树的深度
		for(Node node : nodes){
			if(node.getHeight().compareTo(height) > 0 ) height = node.getHeight();
		}
		
		//初始化树矩阵
		Node[][] tree = new Node[height.intValue()+1][depth.intValue()+1];
		for(int i=0;i<tree.length;i++){
			for(int j=0;j<tree[i].length;j++){
				tree[i][j] = new Node();
			}
		}
		//设置流程节点位置，设置流程节点前一个方向位
		for(int i=0;i<tree.length;i++){
			for(int j=0;j<tree[i].length;j++){
				for(Node node: nodes){
					if(node.getHeight().intValue() == i && node.getDepth().intValue() == j){
						tree[i][j] = node;
						node.setStyle("node");						
						if(node.getInnerHeight().intValue() == 0){
							//设置流程节点
							Node pointer = new Node();								
							pointer.setStyle("pointer");
							tree[i][j-1] = pointer;
						}else{
							//设置流程节点的前一个方向位
							Node pointer = new Node();								
							pointer.setStyle("lpointer");
							tree[i][j-1] = pointer;								
						}
						
					}
				}
			}
		}
		
		//设置流程节点后一个方向位
		for(int i=0;i<tree.length;i++){
			for(int j=0;j<tree[i].length;j++){
				for(Node node: nodes){
					if(node.getHeight().intValue() == i && node.getDepth().intValue() == j){
						if(j+1 < tree[i].length){
							Node rNode = tree[i][j+1];
							if(rNode.getStyle().equals("^")){
								if(node.getSufNodes() != null ){
									for(Node snode : node.getSufNodes()){
										for(Node tnode : nodes){
											if(snode.getNodeId().equals(tnode.getNodeId())){
												//设置流程节点的最后一个方向位
												if(tnode.getDepth() - node.getDepth() == 2){
													tree[i][j+1].setStyle("rpointer");
												}				
											}
										}
									}								
								}
							}
						}
					}
					
				}
			}
		}
		
		//流程根节点的前置节点
		Node userNode = tree[0][0];
		userNode.setStyle("user");
		
		
		return tree;
	}

}
