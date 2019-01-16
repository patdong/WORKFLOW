package cn.ideal.wf.model;

/**
 * 工作流pojo
 * @author 郭佟燕
 * @version 2.0
 */
import java.io.Serializable;
import java.util.Date;


public class Workflow implements Serializable {
	private static final long serialVersionUID = -2546295384449824310L;
	private Long wfId;
	private String wfName;
	private Long tableId;
	private String status;
	private Date createdDate;	
	
	private WorkflowTableBrief wftableBrief;
	
	public Long getWfId() {
		return wfId;
	}
	public void setWfId(Long wfId) {
		this.wfId = wfId;
	}
	public String getWfName() {
		return wfName;
	}
	public void setWfName(String wfName) {
		this.wfName = wfName;
	}
	
	public Long getTableId() {
		return tableId;
	}
	public void setTableId(Long tableId) {
		this.tableId = tableId;
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
	public WorkflowTableBrief getWftableBrief() {
		return wftableBrief;
	}
	public void setWftableBrief(WorkflowTableBrief wftableBrief) {
		this.wftableBrief = wftableBrief;
	}
	
	
}
