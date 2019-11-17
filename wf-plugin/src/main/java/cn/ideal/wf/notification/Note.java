package cn.ideal.wf.notification;

import java.util.Date;
import java.util.List;

import cn.ideal.wf.model.WorkflowUser;

/**
 * 通知器对象
 * @author 郭佟燕
 * @version 2.0
 *
 */
public class Note {

	private WorkflowUser sender;
	private List<WorkflowUser> receiver;
	private String content;
	private Long bizId;
	private Long tbId;
	private Date createdDate;
	private String nodeName;
	private Long summaryId;
	
	public WorkflowUser getSender() {
		return sender;
	}
	public void setSender(WorkflowUser sender) {
		this.sender = sender;
	}
	
	public List<WorkflowUser> getReceiver() {
		return receiver;
	}
	public void setReceiver(List<WorkflowUser> receiver) {
		this.receiver = receiver;
	}
	public Long getBizId() {
		return bizId;
	}
	public void setBizId(Long bizId) {
		this.bizId = bizId;
	}
	public Long getTbId() {
		return tbId;
	}
	public void setTbId(Long tbId) {
		this.tbId = tbId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public Long getSummaryId() {
		return summaryId;
	}
	public void setSummaryId(Long summaryId) {
		this.summaryId = summaryId;
	}
	
	
}
