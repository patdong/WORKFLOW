package cn.ideal.wfpf.model;

import java.util.Date;

/**
 * 元素库定义
 * @author 郭佟燕
 * @version 2.0
 *
 */
public class Element {

	private Long emId;
	private String labelName;
	private String fieldName;
	private String hiddenFieldName;
	private String functionName;
	private String status;
	private Date createdDate;
	private String level;
	private String fieldType;
	private String fieldDataType;
	private String dataContent;
	private Long length;
	
	private Long tbId;
	
	public Long getEmId() {
		return emId;
	}
	public void setEmId(Long emId) {
		this.emId = emId;
	}
	public String getLabelName() {
		return labelName;
	}
	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getHiddenFieldName() {
		return hiddenFieldName;
	}
	public void setHiddenFieldName(String hiddenFieldName) {
		this.hiddenFieldName = hiddenFieldName;
	}
	public String getFunctionName() {
		return functionName;
	}
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
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
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public Long getTbId() {
		return tbId;
	}
	public void setTbId(Long tbId) {
		this.tbId = tbId;
	}
	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	public String getFieldDataType() {
		return fieldDataType;
	}
	public void setFieldDataType(String fieldDataType) {
		this.fieldDataType = fieldDataType;
	}
	public String getDataContent() {
		return dataContent;
	}
	public void setDataContent(String dataContent) {
		this.dataContent = dataContent;
	}
	public Long getLength() {
		return length;
	}
	public void setLength(Long length) {
		this.length = length;
	}
	
	
}
