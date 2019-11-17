package cn.ideal.wf.model;
/**
 * 工作流节点pojo
 * @author 郭佟燕
 * @version 2.0
 */
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class WorkflowNode implements Serializable{

	private static final long serialVersionUID = 4306784132063585168L;

	private Long nodeId;
	private String nodeName;
	private Long timeLimit;
	private String uType;
	private String nType;
	private WorkflowRole role;
	private Collection<WorkflowUser> users;	
	private WorkflowAction action;                     //节点行为
	private List<WorkflowAction> buttons;              //节点按钮
	private String status;
	private Date createdDate;
	private Date modifiedDate;
	private Long wfId;
	private Long seq;
	private boolean highLight;
	private String passed;                             //节点是否已经流转到
	private List<WorkflowNode> preNodes;               //前置节点
	private List<WorkflowNode> sufNodes;               //后置节点
    private String type;                               //节点关联类型 
    private String necessary;                          //路径必经节点
    private boolean backup = false;                    //是否后备用户（即节点没有指定办理人和办理角色）
    
	public WorkflowNode(){}
	public WorkflowNode(Long nodeId){
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
	public Long getTimeLimit() {
		return timeLimit;
	}
	public void setTimeLimit(Long timeLimit) {
		this.timeLimit = timeLimit;
	}
	public String getuType() {
		return uType;
	}
	public void setuType(String uType) {
		this.uType = uType;
	}
	public String getnType() {
		return nType;
	}
	public void setnType(String nType) {
		this.nType = nType;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
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
	public List<WorkflowNode> getPreNodes() {
		return preNodes;
	}
	public void setPreNodes(List<WorkflowNode> preNodes) {
		this.preNodes = preNodes;
	}
	public List<WorkflowNode> getSufNodes() {
		return sufNodes;
	}
	public void setSufNodes(List<WorkflowNode> sufNodes) {
		this.sufNodes = sufNodes;
	}
	public String getPassed() {
		return passed;
	}
	public void setPassed(String passed) {
		this.passed = passed;
	}
	
	public WorkflowAction getAction() {
		return action;
	}
	public void setAction(WorkflowAction action) {
		this.action = action;
	}
	
	public List<WorkflowAction> getButtons() {
		return buttons;
	}
	public void setButtons(List<WorkflowAction> buttons) {
		this.buttons = buttons;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public boolean isBackup() {
		return backup;
	}
	public void setBackup(boolean backup) {
		this.backup = backup;
	}
	
	public String getNecessary() {
		return necessary;
	}
	public void setNecessary(String necessary) {
		this.necessary = necessary;
	}
	public int hashCode() {
		return nodeId.hashCode();
	}
	public boolean equals(Object obj) {
		if(obj instanceof WorkflowNode){
			WorkflowNode wfn = (WorkflowNode)obj;
			return wfn.getNodeId().equals(obj);
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

