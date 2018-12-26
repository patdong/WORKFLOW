package cn.ideal.wf.model;

import java.util.Date;

/**
 * 表单元素概述pojo
 * @author 郭佟燕
 * @vrsion 2.0
 *
 */
public class TableElement {

	private Long tbId;
	private Long emId;
	private String labelNewName;
	private String functionNewName;
	private Long rowes = 1l;
	private Long cols = 1l;
	private String scope;
	private Long width;
	private Long seq;
	private String status;
	private Date createdDate;
	
	private String labelName;
	private String fieldType;
	private String dataContent;
	
	public Long getTbId() {
		return tbId;
	}
	public void setTbId(Long tbId) {
		this.tbId = tbId;
	}
	public Long getEmId() {
		return emId;
	}
	public void setEmId(Long emId) {
		this.emId = emId;
	}
	public Long getRowes() {
		return rowes;
	}
	public void setRowes(Long rowes) {
		this.rowes = rowes;
	}
	public Long getCols() {
		return cols;
	}
	public void setCols(Long cols) {
		this.cols = cols;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public Long getWidth() {
		return width;
	}
	public void setWidth(Long width) {
		this.width = width;
	}
	public Long getSeq() {
		return seq;
	}
	public void setSeq(Long seq) {
		this.seq = seq;
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
	public String getLabelName() {
		return labelName;
	}
	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}
	public String getLabelNewName() {
		return labelNewName;
	}
	public void setLabelNewName(String labelNewName) {
		this.labelNewName = labelNewName;
	}
	public String getFunctionNewName() {
		return functionNewName;
	}
	public void setFunctionNewName(String functionNewName) {
		this.functionNewName = functionNewName;
	}
	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	public String getDataContent() {
		return dataContent;
	}
	public void setDataContent(String dataContent) {
		this.dataContent = dataContent;
	}
	
	
}
