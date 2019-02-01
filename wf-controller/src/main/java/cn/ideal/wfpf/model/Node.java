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
	private String nType;                                //节点属性
	private String uType;                                //节点可操作人的属性
	private String action;                               //节点行为
	private Long timeLimit;                              //节点办理时效
	private String status;                               //节点有效性
	private Date createdDate;                            //节点创建时间
	private Date modifiedDate;                           //节点修改时间
	private Long wfId;                                   //模块编号
	
	private Long height;                                 //节点高度，从上至下，从0开始
	private Long depth;                                  //节点深度，从左至右， 从0开始
	private Long innerHeight;                            //具有相同前置节点的子节点具有内置高度，从上至下，从0开始；
	private List<Node> preNodes;                         //前置节点
	private List<Node> sufNodes;                         //后置节点
	private String style = "^";                          //节点页面展示样式 ，不属于数据库字段。包括：user、node、pointer、lpointer、rpointer
	
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
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
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
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public Long getInnerHeight() {
		return innerHeight;
	}
	public void setInnerHeight(Long innerHeight) {
		this.innerHeight = innerHeight;
	}
	
}
