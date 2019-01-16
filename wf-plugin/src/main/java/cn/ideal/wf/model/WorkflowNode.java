package cn.ideal.wf.model;
/**
 * 工作流节点pojo
 * @author 郭佟燕
 * @version 2.0
 */
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

public class WorkflowNode implements Serializable{

	private static final long serialVersionUID = 4306784132063585168L;

	private Long nodeId;
	private String nodeName;
	private Long timeLimit;
	private String uType;
	private String nType;
	private WorkflowRole role;
	private Collection<WorkflowUser> user;	
	private String status;
	private Date createdDate;
	private Date modifiedDate;
	private Long moduleId;
	private Long seq;
	private boolean highLight;
	
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
	public Collection<WorkflowUser> getUser() {
		return user;
	}
	public void setUser(Collection<WorkflowUser> user) {
		this.user = user;
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
	public Long getModuleId() {
		return moduleId;
	}
	public void setModuleId(Long moduleId) {
		this.moduleId = moduleId;
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
	
	
}
