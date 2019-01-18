package cn.ideal.wf.model;

import java.io.Serializable;
import java.util.Date;

public class WorkflowTableSummary implements Serializable{

	private static final long serialVersionUID = -8754844461503568953L;
	private Long bizId;
	private Long wfId;
	private String title;
	private Long createdUserId;
	private String createdUserName;
	private Long curUserId;
	private Long curUserName;
	private Long unitId;
	private String unitName;
	private Date createdDate;
	private Date finishedDate;
	private Date modifiedDate;
	private String status;
	private String atcion;
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Long getCreatedUserId() {
		return createdUserId;
	}
	public void setCreatedUserId(Long createdUserId) {
		this.createdUserId = createdUserId;
	}
	public String getCreatedUserName() {
		return createdUserName;
	}
	public void setCreatedUserName(String createdUserName) {
		this.createdUserName = createdUserName;
	}
	public Long getCurUserId() {
		return curUserId;
	}
	public void setCurUserId(Long curUserId) {
		this.curUserId = curUserId;
	}
	public Long getCurUserName() {
		return curUserName;
	}
	public void setCurUserName(Long curUserName) {
		this.curUserName = curUserName;
	}
	public Long getUnitId() {
		return unitId;
	}
	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getFinishedDate() {
		return finishedDate;
	}
	public void setFinishedDate(Date finishedDate) {
		this.finishedDate = finishedDate;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAtcion() {
		return atcion;
	}
	public void setAtcion(String atcion) {
		this.atcion = atcion;
	}
	
	
}
