package cn.ideal.wf.model;
import java.io.Serializable;
/**
 * 工作流表单简述pojo
 * @author 郭佟燕
 * @version 2.0
 */
import java.util.Date;

public class WorkflowTableBrief implements Serializable{
	
	private static final long serialVersionUID = -3396093209656466960L;
	private Long tbId;
	private String name;
	private String tableName;
	private String template;
	private Long wfId;
	private Long cols;
	private String status;
	private Date createdDate;
	
	public Long getTbId() {
		return tbId;
	}
	public void setTbId(Long tbId) {
		this.tbId = tbId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public Long getCols() {
		return cols;
	}
	public void setCols(Long cols) {
		this.cols = cols;
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
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	public Long getWfId() {
		return wfId;
	}
	public void setWfId(Long wfId) {
		this.wfId = wfId;
	}
	
}
