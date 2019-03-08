package cn.ideal.wfpf.model;
import java.util.Date;
/**
 * 流程节点
 * @author 郭佟燕
 * @version 2.0
 */
import java.util.List;

public class Node {

	private Long nodeId;                                 //节点序号
	private String nodename;                             //节点名称
	private List<CertificationUser> users;               //节点办理人
	private CertificationRole role;                      //节点办理角色
	private CertificationOrg org;                        //节点办理单位
	private Action nodeAction;                           //节点行为
	private List<Action> buttons;                        //节点按钮
	private String nType;                                //节点属性
	private String uType;                                //节点可操作人的属性
	private Long timeLimit;                              //节点办理时效
	private String status;                               //节点有效性
	private Date createdDate;                            //节点创建时间
	private Date modifiedDate;                           //节点修改时间
	private Long wfId;                                   //模块编号
	
	private List<Node> preNodes;                         //前置节点
	private List<Node> sufNodes;                         //后置节点
	private String type;                                 //节点是否是可被流程可见（此字段只适用于节点关系表中）
	public Long getNodeId() {
		return nodeId;
	}
	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}	
	
	public String getNodename() {
		return nodename;
	}
	public void setNodename(String nodename) {
		this.nodename = nodename;
	}
	public List<CertificationUser> getUsers() {
		return users;
	}
	public void setUsers(List<CertificationUser> users) {
		this.users = users;
	}
	public CertificationRole getRole() {
		return role;
	}
	public void setRole(CertificationRole role) {
		this.role = role;
	}
	public CertificationOrg getOrg() {
		return org;
	}
	public void setOrg(CertificationOrg org) {
		this.org = org;
	}
	
	public String getnType() {
		return nType;
	}
	public void setnType(String nType) {
		this.nType = nType;
	}
	public String getuType() {
		return uType;
	}
	public void setuType(String uType) {
		this.uType = uType;
	}
	
	public Long getTimeLimit() {
		return timeLimit;
	}
	public void setTimeLimit(Long timeLimit) {
		this.timeLimit = timeLimit;
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
	
	public List<Node> getPreNodes() {
		return preNodes;
	}
	public void setPreNodes(List<Node> preNodes) {
		this.preNodes = preNodes;
	}
	public List<Node> getSufNodes() {
		return sufNodes;
	}
	public void setSufNodes(List<Node> sufNodes) {
		this.sufNodes = sufNodes;
	}
	
	public Action getNodeAction() {
		return nodeAction;
	}
	public void setNodeAction(Action nodeAction) {
		this.nodeAction = nodeAction;
	}
	public List<Action> getButtons() {
		return buttons;
	}
	public void setButtons(List<Action> buttons) {
		this.buttons = buttons;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
}
