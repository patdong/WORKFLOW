package cn.ideal.wf.flowchat.draw;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.ideal.wf.model.FlowChatNode;
import cn.ideal.wf.model.WorkflowFlow;
import cn.ideal.wf.service.impl.WorkflowFlowServiceImpl;
import cn.ideal.wf.service.impl.WorkflowNodeServiceImpl;

@Service("richFlowChatService")
public class RichFlowChatServiceImpl implements FlowChatService {
	@Autowired
	private WorkflowNodeServiceImpl workflowNodeService;
	@Autowired
	private WorkflowFlowServiceImpl workflowFlowService;
	
	/*
	 * 垂直位置起始偏移量
	 */
	private int offset = 0;
	private int height = 0;
	private int depth = 0;
	private List<FlowChatNode> nodes;
	//渲染节点树
	private FlowChatNode[][] decorateNodeTree(Long wfId, Long bizId) {
		this.nodes = workflowNodeService.findAllForFlowChat(wfId);
		if(bizId != null){
			List<WorkflowFlow> wfflows = workflowFlowService.findAll(bizId,wfId);
			for(WorkflowFlow wf : wfflows){
				for(FlowChatNode node :nodes){
					if(wf.getNodeName().equals(node.getNodeName())) {
						//设置办理完毕的节点
						if(wf.getFinishedDate() != null) node.setPassed("passed");
						else node.setPassed("passing");
					}
				}
			}
		}
		//根节点设置
		FlowChatNode rootNode = null;
		if(nodes.size() > 0){
			rootNode = nodes.get(0);
		}else{
			//默认无根节点的情况，创建一个空的节点
			rootNode = new FlowChatNode();
			rootNode.setSufNodes(new ArrayList<FlowChatNode>());
			rootNode.setNodeName("新增");
			this.nodes.add(rootNode);
		}
		rootNode.setDepth(2l);
		rootNode.setHeight(0l);
		height=0;
		depth=0;
		decoratePosition(rootNode, rootNode.getSufNodes());
		
		return getTree();
	}
	
	/**
	 * 初值矩阵的位置，后由fixedPosition进行高度调优，让矩阵展示的更美观。
	 * @param nodes
	 * @param parentNode
	 * @param sufNodes
	 */
	private void decoratePosition(FlowChatNode parentNode,List<FlowChatNode> sufNodes){	
		Long iheight = parentNode.getHeight();
		if(parentNode.getHeight() > height) height = parentNode.getHeight().intValue();
		if(parentNode.getDepth() > depth) depth = parentNode.getDepth().intValue();
		
		if(sufNodes.size() == 0) return;		
		
		for(FlowChatNode childNode : sufNodes){								
			childNode = this.getNode(childNode.getNodeId());			
			if(childNode.getDepth() == null){
				childNode.setDepth(parentNode.getDepth() + 2);
				childNode.setHeight(iheight);	
			}else continue;
			
			decoratePosition(childNode,childNode.getSufNodes());
			if(childNode.getSufNodes().size() <= 1) iheight++;
			else iheight +=childNode.getSufNodes().size();
						
		}		
	}
	
	/**
	 * 获取集合中的有效节点
	 * @param nodeId
	 * @return
	 */
	private FlowChatNode getNode(Long nodeId){
		for(FlowChatNode node : this.nodes){
			if(node.getNodeId().compareTo(nodeId) == 0){
				return node;
			}
		}
		
		return null;
	}
	/**
	 * 对矩阵的高度做调优
	 * @param nodes
	 */
	private void fixedPosition(){
		List<FlowChatNode> abandonNodes = new ArrayList<FlowChatNode>();
		//过滤掉节点中没有链接的节点
		List<FlowChatNode> lostNodes = new ArrayList<FlowChatNode>(); 
		for(FlowChatNode node: this.nodes){	
			if(node.getHeight() == null || node.getDepth() == null){
				lostNodes.add(node);
			}
		}
		this.nodes.removeAll(lostNodes);		
		
		//初始化树矩阵
	    FlowChatNode[][] tree = new FlowChatNode[height+1][depth+1];	
	    //将节点安置到矩阵树内，过滤出废弃节点。
		for(FlowChatNode node: this.nodes){				
			if(tree[node.getHeight().intValue()][node.getDepth().intValue()] == null){
				tree[node.getHeight().intValue()][node.getDepth().intValue()] = node;
			}else{
				abandonNodes.add(node);
			}			
		}
		
		//根据同一行上前后节点比较，再一次过滤废弃节点。
		for(int i=0;i<tree.length;i++){
			for(int j=0;j<tree[i].length;j++){
				if((j+2) < tree[i].length){
					if(tree[i][j] != null && tree[i][j+2] != null){
						List<FlowChatNode> sufNodes = tree[i][j].getSufNodes();
						if(sufNodes.size()>0){
							if(sufNodes.get(0).getNodeId().compareTo(tree[i][j+2].getNodeId()) !=0){
								abandonNodes.add(tree[i][j]);
								tree[i][j] = null;
							}
						}else{
							abandonNodes.add(tree[i][j]);
							tree[i][j] = null;
						}
					}
				}
			}
		}
		
		//对废弃节点及其前面的列和行进行高度重置
		if(abandonNodes.size() > 0){
			for(FlowChatNode node : abandonNodes){
				height++;				
				for(int i=node.getHeight().intValue();i<tree.length;i++){
					for(int j=node.getDepth().intValue();j>0;j--){
						if(tree[i][j] != null)
						tree[i][j].setHeight(tree[i][j].getHeight()+1);
					}
				}
				node.setHeight(node.getHeight()+1);
			}			
		}
	}
	
	/**
	 * 将节点装饰成一棵有方向的多叉树
	 */
	private FlowChatNode[][] getTree(){		
		//修复节点高度
		fixedPosition();
		//初始化树矩阵
		FlowChatNode[][] tree = new FlowChatNode[height+1][depth+1];	
		//对树矩阵标方向
		int h=0;
		int d=0;
		///////////////////////////////////////////////////////////////////////////////
		int rowHeight=90,colWidth=120;     //行高，列宽定义
		int pixelX=6,pixelY=10+offset;   //其实（x,y）象素位置
		int arrowWidth=100,arrowHeight=1; //水平或垂直箭头的宽度，长度定义
		int halfHeight=60;               //转弯箭头处水平宽度为整个列宽度的一半
		int arrowPixelY=25;              //水平箭头的象素差距
		int deltaWidth=0;                //垂直位置间方向线的间距，0为重叠。
		int gapHeight=50;                //垂直位置一行的除节点外的高度差
		//////////////////////////////////////////////////////////////////////////////
		//对矩阵树填值
		for(FlowChatNode node: nodes){			
			tree[node.getHeight().intValue()][node.getDepth().intValue()] = node;
			node.setStyle("node");
			node.setLeft(pixelX+node.getDepth().intValue() * colWidth);
			node.setTop(pixelY+node.getHeight().intValue() * rowHeight);
		}
		for(FlowChatNode tnode : nodes){
			for(FlowChatNode snode : tnode.getSufNodes()){
				FlowChatNode node = this.getNode(snode.getNodeId());								
				h=node.getHeight().intValue()-tnode.getHeight().intValue();
				d=node.getDepth().intValue()-tnode.getDepth().intValue();
				FlowChatNode item = tree[tnode.getHeight().intValue()][tnode.getDepth().intValue()];
				FlowChatNode.Position[][] position = null;
				//深度处理
				if(h==0){
												
					if(d>0){
						int idx = getPositionIndex(item,'r');
						position=item.getrPositions();								
						FlowChatNode.Position pos = item.new Position();
						pos.top = tnode.getTop()+arrowPixelY;
						pos.left = tnode.getLeft()+colWidth;
						pos.width = colWidth*(d-2)+arrowWidth;
						pos.height = 1;
						pos.arrow = "rarrow";
						position[idx][0]= pos;
						item.setrPositions(position);								
					}
					if(d<0){
						int idx = getPositionIndex(item,'l');
						position=item.getlPositions();								
						FlowChatNode.Position pos = item.new Position();
						pos.top = tnode.getTop()+arrowPixelY;
						pos.width = colWidth*(d*(-1)-2)+arrowWidth;
						pos.left = tnode.getLeft()-colWidth*(d*(-1)-2)-arrowWidth-10;						
						pos.height = 1;
						pos.arrow = "larrow";
						position[idx][0]= pos;
						item.setlPositions(position);								
					}
					
					//待做：d<0的情况
				}else{
					//高度处理
					//节点下面,向下再向右														
					if(h>0){
						if(d>0){
							int idx = getPositionIndex(item,'d');
							position=item.getdPositions();
							FlowChatNode.Position pos = item.new Position();
							pos.top = tnode.getTop() + rowHeight - arrowPixelY;
							pos.left = tnode.getLeft()+halfHeight;
							pos.width = arrowHeight;
							pos.height = (h-1)*rowHeight+gapHeight;
							pos.arrow = "dline";
							position[idx][0]= pos;									
							FlowChatNode.Position pos2 = item.new Position();
							pos2.top = pos.top+pos.height;
							pos2.left = pos.left;
							pos2.width = colWidth*(d-2)+arrowWidth+halfHeight+5;
							pos2.height = arrowHeight;
							pos2.arrow = "rarrow";
							position[idx][1]= pos2;
							item.setdPositions(position);
						}
						if(d==0){
							int idx = getPositionIndex(item,'d');
							position=item.getdPositions();
							FlowChatNode.Position pos = item.new Position();
							pos.top = tnode.getTop();
							pos.left = tnode.getLeft()+halfHeight;
							pos.width = arrowHeight;
							pos.height = (h-1)*rowHeight+gapHeight;
							pos.arrow = "darrow";
							position[idx][0]= pos;					
						}
					}
					//向右再向上
					if(h<0){
						if(d>0){
							int idx = getPositionIndex(item,'r');
							position=item.getrPositions();
							
							FlowChatNode.Position pos = item.new Position();
							pos.top = tnode.getTop()+arrowPixelY;
							pos.left = tnode.getLeft()+colWidth;
							pos.width = colWidth*(d-1)+halfHeight;
							pos.height = arrowHeight;
							pos.arrow = "rline";
							position[idx][0]= pos;									
							FlowChatNode.Position pos2 = item.new Position();								
							pos2.left = pos.left+pos.width-deltaWidth;
							pos2.width = arrowHeight;
							pos2.height = (h*(-1)-1)*rowHeight+2*arrowPixelY+2;
							pos2.top = pos.top-(h*(-1)-1)*rowHeight-2*arrowPixelY;
							pos2.arrow = "uarrow";
							position[idx][1]= pos2;
							item.setrPositions(position);
						}
						if(d==0){
							int idx = getPositionIndex(item,'u');
							position=item.getuPositions();
							FlowChatNode.Position pos = item.new Position();							
							pos.top = tnode.getTop()- (h*(-1)-1)*rowHeight-arrowPixelY;
							pos.left = tnode.getLeft()+halfHeight;
							pos.width = arrowHeight;
							pos.height = (h*(-1)-1)*rowHeight+arrowPixelY-2;
							pos.arrow = "uarrow";
							position[idx][0]= pos;					
						}
						if(d<0){
							int idx = getPositionIndex(item,'l');
							position=item.getlPositions();
							
							FlowChatNode.Position pos = item.new Position();
							pos.top = tnode.getTop()+arrowPixelY;
							pos.width = colWidth*(d*(-1)-1)+halfHeight-d*(-1)-5;
							pos.left = tnode.getLeft()-(colWidth*(d*(-1)-1)+halfHeight)-deltaWidth;							
							pos.height = arrowHeight;
							pos.arrow = "lline";
							position[idx][0]= pos;									
							FlowChatNode.Position pos2 = item.new Position();								
							pos2.left = tnode.getLeft()-(colWidth*(d*(-1)-1)+halfHeight)-deltaWidth;
							pos2.width = arrowHeight;
							pos2.height = (h*(-1)-1)*rowHeight+2*arrowPixelY;
							pos2.top = pos.top-(h*(-1)-1)*rowHeight-2*arrowPixelY;
							pos2.arrow = "uarrow";
							position[idx][1]= pos2;
							item.setlPositions(position);
						}
					}	
				}					
				
			}
		}	
		
		
		//流程根节点的前置节点
		FlowChatNode userNode = new FlowChatNode();
		FlowChatNode.Position[][] rPosition = new FlowChatNode.Position[1][1];
		FlowChatNode.Position position = userNode.new Position();
		position.top = pixelY+arrowPixelY;
		position.left = pixelX+colWidth;
		position.width = arrowWidth;
		position.height = arrowHeight;
		position.arrow = "rarrow";
		rPosition[0][0]= position;
		userNode.setrPositions(rPosition);
		userNode.setStyle("user");
		userNode.setTop(12+offset);
		tree[0][0] = userNode;
		
		
		return tree;
	}

	
	/**
	 * 获得最后一个position位置
	 * @param positions
	 * @return
	 */
	private int getPositionIndex(FlowChatNode node, char pos){
		int idx = 0;
		FlowChatNode.Position[][] positions = null;
		if(pos == 'd') positions = node.getdPositions();
		if(pos == 'u') positions = node.getuPositions();
		if(pos == 'r') positions = node.getrPositions();
		if(pos == 'l') positions = node.getlPositions();
		if(positions == null){
			positions = new FlowChatNode.Position[10][2];
			if(pos == 'd') node.setdPositions(positions);
			if(pos == 'u') node.setuPositions(positions);
			if(pos == 'r') node.setrPositions(positions);
			if(pos == 'l') node.setlPositions(positions);
			
		}else{	
			for(int i=0;i<positions.length;i++){
				if(positions[i][0] == null) {
					idx=i;
					break;
				}
			}
		}
		
		return idx;
	}
	/**
	 * 绘制流程图
	 */
	@Override
	public StringBuffer draw(Long wfId) {
		if(wfId == null) return new StringBuffer();
		offset=50;
		FlowChatNode[][] tree = this.decorateNodeTree(wfId,null);
		
		StringBuffer buf = new StringBuffer();
		buf.append("<div style='display:inline-block;position:absoult;'>");
		for(int i=0;i<tree.length;i++){			
			for(int j=0;j<tree[0].length;j++){
				FlowChatNode node = tree[i][j];
				if(node == null) continue;
				if(node.getStyle().equals("user")){
					buf.append("<div class='circle-text' style='background:green;position: absolute;top: "+node.getTop()+"px;left: "+(node.getLeft()+20)+"px;' onclick=\"showPos(event,0,'','')\"><font style='font-size:15px'>&nbsp;创建&nbsp;</font></div>");					
				}
				if(node.getStyle().equals("node")){
					buf.append("<div class='circle-text' style='z-index:1;position: absolute;top: "+(node.getTop())+"px;left: "+(node.getLeft()-3)+"px;' onclick=\"showPos(event,"+node.getNodeId()+",'"+node.getNodeName()+"','"+node.getStatus()+"')\"><font style='font-size:15px'>&nbsp;"+node.getNodeName()+"	&nbsp;</font>");
					if(node.getRole() != null) buf.append("<hr class='circle-hr'></hr><span style='font-size:14px;cursor:pointer' title='"+node.getRole().getRoleName()+"'>角色</span>");
					if(node.getUsers() != null && node.getUsers().size() > 0) buf.append("<hr class='circle-hr'></hr><span style='font-size:14px;cursor:pointer;' title='"+node.getUserstoString()+"'>用户</span>");
					buf.append("</div>");					
				}
				this.drawPostion(node.getrPositions(),buf);
				this.drawPostion(node.getlPositions(),buf);
				this.drawPostion(node.getdPositions(),buf);
				this.drawPostion(node.getuPositions(),buf);				
			}
		}
		buf.append("</div>");
		return buf;
	}
	
	/**
	 * 绘制节点左右上下的关联连接
	 * @param position
	 * @param buf
	 */
	private void drawPostion(FlowChatNode.Position[][] position,StringBuffer buf){
		if(position == null) return;
		for(int l=0;l<position.length;l++){
			for(int p=0;p<position[l].length;p++){
				if(position[l][p] == null) continue;
				
				if(position[l][p].arrow.equals("rarrow")){
					buf.append("<a href='#' class='rightArrow' style='top: "+position[l][p].top+"px;left: "+position[l][p].left+"px;width:"+position[l][p].width+"px;height:"+position[l][p].height+"px;'></a>");
				}						
				if(position[l][p].arrow.equals("uarrow")){
					buf.append("<a href='#' class='topArrow' style='top: "+position[l][p].top+"px;left: "+position[l][p].left+"px;width:"+position[l][p].width+"px;height:"+position[l][p].height+"px;'></a>");
				}
				if(position[l][p].arrow.equals("darrow")){
					buf.append("<a href='#' class='buttomArrow' style='top: "+position[l][p].top+"px;left: "+position[l][p].left+"px;width:"+position[l][p].width+"px;height:"+position[l][p].height+"px;'></a>");
				}
				if(position[l][p].arrow.equals("larrow")){
					buf.append("<a href='#' class='leftArrow' style='top: "+position[l][p].top+"px;left: "+position[l][p].left+"px;width:"+position[l][p].width+"px;height:"+position[l][p].height+"px;'></a>");
				}
				
				if(position[l][p].arrow.equals("rline")){
					buf.append("<a href='#' class='horizontalline' style='top: "+position[l][p].top+"px;left: "+position[l][p].left+"px;width:"+position[l][p].width+"px;height:"+position[l][p].height+"px;'></a>");
				}
				if(position[l][p].arrow.equals("lline")){
					buf.append("<a href='#' class='horizontalline' style='top: "+position[l][p].top+"px;left: "+position[l][p].left+"px;width:"+position[l][p].width+"px;height:"+position[l][p].height+"px;'></a>");
				}
				if(position[l][p].arrow.equals("dline")){
					buf.append("<a href='#' class='vertialline' style='top: "+position[l][p].top+"px;left: "+position[l][p].left+"px;width:"+position[l][p].width+"px;height:"+position[l][p].height+"px;'></a>");
				}
			}
		}
	}

	@Override
	public StringBuffer draw(Long wfId, Long bizId) {
		if(wfId == null) return new StringBuffer();
		if(bizId == null) return drawNoSetting(wfId);
		
		offset=50;
		FlowChatNode[][] tree = this.decorateNodeTree(wfId,bizId);
		
		StringBuffer buf = new StringBuffer();
		String classCss = null;
		buf.append("<div style='display:inline-block;position:absoult;'>");
		for(int i=0;i<tree.length;i++){			
			for(int j=0;j<tree[0].length;j++){
				
				FlowChatNode node = tree[i][j];
				if(node == null) continue;
				if(node.getStyle().equals("user")){
					buf.append("<div class='circle-text' style='background:green;position: absolute;top: "+node.getTop()+"px;left: "+(node.getLeft()+20)+"px;'><font style='font-size:15px'>&nbsp;创建&nbsp;</font></div>");					
				}
				if(node.getStyle().equals("node")){
					if(node.getPassed() == null) classCss = "circle-text";
					else if(node.getPassed().equals("passed")) classCss = "circle-green-text";
					else if(node.getPassed().equals("passing")) classCss = "circle-blue-text";
					buf.append("<div class='"+classCss+"' style='position: absolute;top: "+(node.getTop())+"px;left: "+(node.getLeft()-3)+"px;' ><font style='font-size:15px'>&nbsp;"+node.getNodeName()+"	&nbsp;</font>");
					if(node.getRole() != null) buf.append("<hr class='circle-hr'></hr><span style='font-size:14px;cursor:pointer' title='"+node.getRole().getRoleName()+"'>角色</span>");
					if(node.getUsers() != null && node.getUsers().size() > 0) buf.append("<hr class='circle-hr'></hr><span style='font-size:14px;cursor:pointer;' title='"+node.getUserstoString()+"'>用户</span>");
					buf.append("</div>");
				}
				this.drawPostion(node.getrPositions(),buf);
				this.drawPostion(node.getlPositions(),buf);
				this.drawPostion(node.getdPositions(),buf);
				this.drawPostion(node.getuPositions(),buf);				
			}
		}
		buf.append("</div>");
		return buf;
	}
	
	/**
	 * 仅显示流程图，没有配置功能
	 * @param wfId
	 * @return
	 */
	private StringBuffer drawNoSetting(Long wfId) {
		if(wfId == null) return new StringBuffer();
		offset=50;
		FlowChatNode[][] tree = this.decorateNodeTree(wfId,null);
		
		StringBuffer buf = new StringBuffer();
		buf.append("<div style='display:inline-block;position:absoult;'>");
		for(int i=0;i<tree.length;i++){			
			for(int j=0;j<tree[0].length;j++){
				FlowChatNode node = tree[i][j];
				if(node == null) continue;
				if(node.getStyle().equals("user")){
					buf.append("<div class='circle-text' style='background:green;position: absolute;top: "+node.getTop()+"px;left: "+(node.getLeft()+20)+"px;' ><font style='font-size:15px'>&nbsp;创建&nbsp;</font></div>");					
				}
				if(node.getStyle().equals("node")){
					buf.append("<div class='circle-text' style='z-index:1;position: absolute;top: "+(node.getTop())+"px;left: "+(node.getLeft()-3)+"px;' ><font style='font-size:15px'>&nbsp;"+node.getNodeName()+"	&nbsp;</font>");
					if(node.getRole() != null) buf.append("<hr class='circle-hr'></hr><span style='font-size:14px;cursor:pointer' title='"+node.getRole().getRoleName()+"'>角色</span>");
					if(node.getUsers() != null && node.getUsers().size() > 0) buf.append("<hr class='circle-hr'></hr><span style='font-size:14px;cursor:pointer;' title='"+node.getUserstoString()+"'>用户</span>");
					buf.append("</div>");					
				}
				this.drawPostion(node.getrPositions(),buf);
				this.drawPostion(node.getlPositions(),buf);
				this.drawPostion(node.getdPositions(),buf);
				this.drawPostion(node.getuPositions(),buf);				
			}
		}
		buf.append("</div>");
		return buf;
	}
}
