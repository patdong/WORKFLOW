package cn.ideal.wfpf.model;

import java.util.Date;
import java.util.List;

import cn.ideal.wf.model.Workflow;

/**
 * 表单概述pojo
 * @author 郭佟燕
 * @vrsion 2.0
 *
 */
public class TableBrief {

	private Long tbId;
	private String name;
	private String tableName;
	private String template;
	private Long cols;
	private String status;
	private Date createdDate;
	private List<Workflow> wf;
	private Long productCount = 0l;
	
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
	public List<Workflow> getWf() {
		return wf;
	}
	public void setWf(List<Workflow> wf) {
		this.wf = wf;
	}
	public Long getProductCount() {
		return productCount;
	}
	public void setProductCount(Long productCount) {
		this.productCount = productCount;
	}
	
}
