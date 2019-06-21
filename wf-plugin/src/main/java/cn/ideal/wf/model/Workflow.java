package cn.ideal.wf.model;

/**
 * 工作流pojo
 * @author 郭佟燕
 * @version 2.0
 */
import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class Workflow implements Serializable {
	private static final long serialVersionUID = -2546295384449824310L;
	private Long wfId;
	private String wfName;
	private Long tbId;
	private String status;
	private Date createdDate;
	private String type;
	
	private List<WorkflowTableBrief> wftableBrief;
	
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
	public Long getTbId() {
		return tbId;
	}
	public void setTbId(Long tbId) {
		this.tbId = tbId;
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
	public List<WorkflowTableBrief> getWftableBrief() {
		return wftableBrief;
	}
	public void setWftableBrief(List<WorkflowTableBrief> wftableBrief) {
		this.wftableBrief = wftableBrief;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	
}
