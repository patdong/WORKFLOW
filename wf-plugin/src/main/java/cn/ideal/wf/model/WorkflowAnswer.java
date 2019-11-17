package cn.ideal.wf.model;

/**
 * 应答pojo
 * @author 郭佟燕
 * @version 2.0
 */
import java.io.Serializable;
import java.util.Date;


public class WorkflowAnswer implements Serializable {
	private static final long serialVersionUID = -2546295384449824310L;
	private Long dispenseId;
	private Long typeId;
	private Long tbId;
	private Long wfId;
	private Long bizId;
	private Long dispenseUserId;
	private String dispenseUserName;
	private String receiveUserIds;
	private String receiveUserNames;
	private String content;
	private String type;
	private Long receiveUserId;
	private String receiveUserName;
	private Date createdDate;
	
	
	public Long getDispenseId() {
		return dispenseId;
	}
	public void setDispenseId(Long dispenseId) {
		this.dispenseId = dispenseId;
	}
	public Long getTypeId() {
		return typeId;
	}
	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}
	public Long getDispenseUserId() {
		return dispenseUserId;
	}
	public void setDispenseUserId(Long dispenseUserId) {
		this.dispenseUserId = dispenseUserId;
	}
	public String getReceiveUserIds() {
		return receiveUserIds;
	}
	public void setReceiveUserIds(String receiveUserIds) {
		this.receiveUserIds = receiveUserIds;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Long getWfId() {
		return wfId;
	}
	public void setWfId(Long wfId) {
		this.wfId = wfId;
	}
	public Long getBizId() {
		return bizId;
	}
	public void setBizId(Long bizId) {
		this.bizId = bizId;
	}
	public String getDispenseUserName() {
		return dispenseUserName;
	}
	public void setDispenseUserName(String dispenseUserName) {
		this.dispenseUserName = dispenseUserName;
	}
	public String getReceiveUserNames() {
		return receiveUserNames;
	}
	public void setReceiveUserNames(String receiveUserNames) {
		this.receiveUserNames = receiveUserNames;
	}
	public Long getReceiveUserId() {
		return receiveUserId;
	}
	public void setReceiveUserId(Long receiveUserId) {
		this.receiveUserId = receiveUserId;
	}
	public String getReceiveUserName() {
		return receiveUserName;
	}
	public void setReceiveUserName(String receiveUserName) {
		this.receiveUserName = receiveUserName;
	}
	public Long getTbId() {
		return tbId;
	}
	public void setTbId(Long tbId) {
		this.tbId = tbId;
	}
	
	
}
