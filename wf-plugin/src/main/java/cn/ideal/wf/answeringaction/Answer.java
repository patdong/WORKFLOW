package cn.ideal.wf.answeringaction;

import java.util.Date;

import cn.ideal.wf.model.WorkflowUser;
/**
 * 应答对象
 * @author 郭佟燕
 * @version 2.0
 *
 */
public class Answer {
	
	private String content;
	private WorkflowUser user;
	private String userIds;
	private String userNames;
	private Date date;
	private Long typeId;
	private String type;
	private Long tbId;
	private Long bizId;
	private Long wfId;
	private Long dispenseId;
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public WorkflowUser getUser() {
		return user;
	}
	public void setUser(WorkflowUser user) {
		this.user = user;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Long getTypeId() {
		return typeId;
	}
	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}
	public Long getBizId() {
		return bizId;
	}
	public void setBizId(Long bizId) {
		this.bizId = bizId;
	}
	public Long getWfId() {
		return wfId;
	}
	public void setWfId(Long wfId) {
		this.wfId = wfId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUserIds() {
		return userIds;
	}
	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}
	public String getUserNames() {
		return userNames;
	}
	public void setUserNames(String userNames) {
		this.userNames = userNames;
	}
	public Long getDispenseId() {
		return dispenseId;
	}
	public void setDispenseId(Long dispenseId) {
		this.dispenseId = dispenseId;
	}
	public Long getTbId() {
		return tbId;
	}
	public void setTbId(Long tbId) {
		this.tbId = tbId;
	}
	
	
}
