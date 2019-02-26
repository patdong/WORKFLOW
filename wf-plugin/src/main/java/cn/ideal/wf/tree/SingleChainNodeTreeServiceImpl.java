package cn.ideal.wf.tree;

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

import cn.ideal.wf.model.WorkflowNode;

@Service
public class SingleChainNodeTreeServiceImpl implements NodeTreeService {

	@Override
	public WorkflowNode[][] decorateNodeTree(List<WorkflowNode> nodes) {
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
	private void setDepth(List<WorkflowNode> nodes, Long depth, List<WorkflowNode> sufNodes){	
		//如果没有节点，则默认创建一个空节点。
		if(nodes.size() == 0) {
			WorkflowNode node = new WorkflowNode();
			node.setStyle("node");
			//设置初始值，方便前端页面控制
			node.setNodeId(0l);
			node.setSufNodes(new ArrayList<WorkflowNode>());
			node.setPreNodes(new ArrayList<WorkflowNode>());
			node.setDepth(2l);
			nodes.add(node);			
		}
		if(sufNodes == null){
			for(WorkflowNode node : nodes){	
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
			for(WorkflowNode node : nodes){	
				for(WorkflowNode sufNode : sufNodes){
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
	private void setHeight(List<WorkflowNode> nodes){
		Long depth = 0l;
		//获取节点最大深度
		for(WorkflowNode node : nodes){
			if(node.getDepth().compareTo(depth) > 0 ) depth = node.getDepth();
		}
		//根据深度遍历
		for(int i=0; i<=depth; i++){
			Long height = 0l;
			for(WorkflowNode node : nodes){
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
					for(WorkflowNode snode : node.getSufNodes()){
						for(WorkflowNode outernode : nodes){
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
	private void mendFrontNodeHeight(List<WorkflowNode> nodes , int depth, int height){
		for(int i = depth; i>0; i--){
			for(WorkflowNode node : nodes){
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
	private WorkflowNode[][] decorateTree(List<WorkflowNode> nodes){
		Long depth = 0l;
		//获取树的宽度
		for(WorkflowNode node : nodes){
			if(node.getDepth().compareTo(depth) > 0 ) depth = node.getDepth();
		}
		Long height = 0l;
		//获取树的深度
		for(WorkflowNode node : nodes){
			if(node.getHeight().compareTo(height) > 0 ) height = node.getHeight();
		}
		
		//初始化树矩阵
		WorkflowNode[][] tree = new WorkflowNode[height.intValue()+1][depth.intValue()+1];
		for(int i=0;i<tree.length;i++){
			for(int j=0;j<tree[i].length;j++){
				tree[i][j] = new WorkflowNode();
			}
		}
		
		//对树矩阵填值
		for(WorkflowNode node: nodes){
			tree[node.getHeight().intValue()][node.getDepth().intValue()] = node;
			node.setStyle("node");
		}
		
		//对树矩阵标方向
		int h=0;
		for(WorkflowNode tnode : nodes){
			for(WorkflowNode snode : tnode.getSufNodes()){			
				for(WorkflowNode node : nodes){
					if(snode.getNodeId().equals(node.getNodeId())){						
						h=node.getHeight().intValue()-tnode.getHeight().intValue();
						
						//深度处理
						if(h==0){
							for(int i=tnode.getDepth().intValue()+1;i<node.getDepth().intValue()-1;i++){
								tree[tnode.getHeight().intValue()][i].setStyle("line");
							}
							tree[tnode.getHeight().intValue()][node.getDepth().intValue()-1].setStyle("pointer");
						}else{
							//高度处理
							//节点下面
							for(int i=tnode.getDepth().intValue()+1;i<node.getDepth().intValue();i++){
								if(tree[tnode.getHeight().intValue()][i].getStyle().equals("^"))
								tree[tnode.getHeight().intValue()][i].setStyle("line");
							}
							if(h>0){
								for(int i=tnode.getHeight().intValue()+1;i<node.getHeight().intValue();i++){
									if(tree[i][tnode.getDepth().intValue()+1].getStyle().equals("^"))
									tree[i][tnode.getDepth().intValue()+1].setStyle("lline");
								}
								tree[node.getHeight().intValue()][tnode.getDepth().intValue()+1].setStyle("lpointer");
							}
							if(h<0){
								for(int i=tnode.getHeight().intValue()-1;i>node.getHeight().intValue();i--){
									if(tree[i][node.getDepth().intValue()].getStyle().equals("^"))
									tree[i][node.getDepth().intValue()].setStyle("rline");
								}
								tree[tnode.getHeight().intValue()][node.getDepth().intValue()].setStyle("rpointer");
							}	
						}					
					}
				}
			}
		}	
		
		
		//流程根节点的前置节点
		WorkflowNode userNode = tree[0][0];
		userNode.setStyle("user");
		userNode = tree[0][1];
		userNode.setStyle("pointer");
		
		return tree;
	}

}
