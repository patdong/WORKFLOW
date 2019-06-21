package cn.ideal.wf.model;
/**
 * 工作流节点pojo
 * @author 郭佟燕
 * @version 2.0
 */
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public class FlowChatNode implements Serializable{

	private static final long serialVersionUID = 4306784132063585168L;

	private Long nodeId;
	private String nodeName;
	private String status;
	private Long wfId;
	private Long seq;
	private boolean highLight;
	private String passed;                                       //节点是否已经流转到
	private List<FlowChatNode> preNodes;                         //前置节点
	private List<FlowChatNode> sufNodes;                         //后置节点
	
	private Long height;                                         //节点高度，从上至下，从0开始
	private Long depth;                                          //节点深度，从左至右， 从0开始	
	private String style = "^";                                  //节点页面展示样式 ，不属于数据库字段。包括：user、node、pointer、lpointer、rpointer
	
	private Position[][] lPositions;
	private Position[][] rPositions;
	private Position[][] uPositions;
	private Position[][] dPositions;
	private int left;
	private int top;
	
	private WorkflowRole role;
	private Collection<WorkflowUser> users;
	
	public FlowChatNode(){}
	public FlowChatNode(Long nodeId){
		this.setNodeId(nodeId);
	}
	public Long getNodeId() {
		return nodeId;
	}
	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	
	public Long getWfId() {
		return wfId;
	}
	public void setWfId(Long wfId) {
		this.wfId = wfId;
	}
	public Long getSeq() {
		return seq;
	}
	public void setSeq(Long seq) {
		this.seq = seq;
	}
	public boolean isHighLight() {
		return highLight;
	}
	public void setHighLight(boolean highLight) {
		this.highLight = highLight;
	}
	public List<FlowChatNode> getPreNodes() {
		return preNodes;
	}
	public void setPreNodes(List<FlowChatNode> preNodes) {
		this.preNodes = preNodes;
	}
	public List<FlowChatNode> getSufNodes() {
		return sufNodes;
	}
	public void setSufNodes(List<FlowChatNode> sufNodes) {
		this.sufNodes = sufNodes;
	}
	public String getPassed() {
		return passed;
	}
	public void setPassed(String passed) {
		this.passed = passed;
	}
	public Long getHeight() {
		return height;
	}
	public void setHeight(Long height) {
		this.height = height;
	}
	public Long getDepth() {
		return depth;
	}
	public void setDepth(Long depth) {
		this.depth = depth;
	}
	
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	
	public Position[][] getlPositions() {
		return lPositions;
	}
	public void setlPositions(Position[][] lPositions) {
		this.lPositions = lPositions;
	}
	public Position[][] getrPositions() {
		return rPositions;
	}
	public void setrPositions(Position[][] rPositions) {
		this.rPositions = rPositions;
	}
	public Position[][] getuPositions() {
		return uPositions;
	}
	public void setuPositions(Position[][] uPositions) {
		this.uPositions = uPositions;
	}
	public Position[][] getdPositions() {
		return dPositions;
	}
	public void setdPositions(Position[][] dPositions) {
		this.dPositions = dPositions;
	}
	
	public int getLeft() {
		return left;
	}
	public void setLeft(int left) {
		this.left = left;
	}
	public int getTop() {
		return top;
	}
	public void setTop(int top) {
		this.top = top;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public WorkflowRole getRole() {
		return role;
	}
	public void setRole(WorkflowRole role) {
		this.role = role;
	}
	public Collection<WorkflowUser> getUsers() {
		return users;
	}
	public void setUsers(Collection<WorkflowUser> users) {
		this.users = users;
	}
	
	public String getUserstoString(){
		String user = "";
		if(this.users != null){
			user = "[";
			for(WorkflowUser ur : this.users){
				user += ur.getUserName()+",";
			}
			if(user.length() > 1) user = user.substring(0,user.length()-1);
			user += "]";
		}
		
		return user;
	}
	public int hashCode() {
		return nodeId.intValue();
	}
	public boolean equals(Object obj) {
		if(obj instanceof FlowChatNode){
			FlowChatNode wfn = (FlowChatNode)obj;
			return this.nodeId.equals(wfn.getNodeId());
		}
		return false;
	}
	
	public class Position{
		public int top;
		public int left;
		public int width;
		public int height;
		public String arrow;
	}
}

