package cn.ideal.wf.model;

/**
 * 工作流pojo
 * @author 郭佟燕
 * @version 2.0
 */
import java.util.Date;

public class Workflow {
	private Long wfId;
	private String wfName;
	private Long wfTableId;
	private String status;
	private Date createdDate;	
	
	
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
	public Long getWfTableId() {
		return wfTableId;
	}
	public void setWfTableId(Long wfTableId) {
		this.wfTableId = wfTableId;
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
	
}
