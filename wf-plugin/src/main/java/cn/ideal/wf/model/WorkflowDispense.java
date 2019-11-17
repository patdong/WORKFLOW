package cn.ideal.wf.model;

/**
 * 传阅
 * @author 郭佟燕
 * @version 2.0
 */
import java.io.Serializable;
import java.util.Date;


public class WorkflowDispense implements Serializable {
	private static final long serialVersionUID = -2546295384449824310L;
	private Long dispenseId;
	private Long typeId;
	private Long dispenseUserId;
	private String receiveUserIds;
	private String type;
	private String content;
	private Date createDate;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
