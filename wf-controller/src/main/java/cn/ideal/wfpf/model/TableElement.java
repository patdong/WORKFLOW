package cn.ideal.wfpf.model;

import java.util.Date;

/**
 * 表单元素概述pojo
 * @author 郭佟燕
 * @vrsion 2.0
 *
 */
public class TableElement {

	private Long id;
	private Long tbId;
	private Long emId;
	private String newLabelName;
	private String position;
	private String newFieldName;
	private String newUnit;
	private String newFunctionName;
	private String functionBelongTo;
	private String newHiddenFieldName;
	private String newFieldDataType;
	private String newFieldType;
	private String newDataContent;
	private Long rowes = 1l;
	private Long cols = 1l;
	private String scope;
	private Long width;
	private Long seq;
	private String formula;
	private String list;
	private String constraint;
	private Long newLength;
	private String defaultValue;
	private String defaultValueFrom;
	private String status;
	private Date createdDate;
	private Long stbId;
	private String tableName;
	
	private boolean readOnly = false;
	private String required = "否";
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	public String getNewLabelName() {
		return newLabelName;
	}
	public void setNewLabelName(String newLabelName) {
		this.newLabelName = newLabelName;
	}
	public String getNewFieldName() {
		return newFieldName;
	}
	public void setNewFieldName(String newFieldName) {
		this.newFieldName = newFieldName;
	}
	public String getNewFunctionName() {
		return newFunctionName;
	}
	public void setNewFunctionName(String newFunctionName) {
		this.newFunctionName = newFunctionName;
	}
	public String getNewHiddenFieldName() {
		return newHiddenFieldName;
	}
	public void setNewHiddenFieldName(String newHiddenFieldName) {
		this.newHiddenFieldName = newHiddenFieldName;
	}
	public String getNewFieldDataType() {
		return newFieldDataType;
	}
	public void setNewFieldDataType(String newFieldDataType) {
		this.newFieldDataType = newFieldDataType;
	}
	public String getNewFieldType() {
		return newFieldType;
	}
	public void setNewFieldType(String newFieldType) {
		this.newFieldType = newFieldType;
	}
	public String getNewDataContent() {
		return newDataContent;
	}
	public void setNewDataContent(String newDataContent) {
		this.newDataContent = newDataContent;
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
	public String getFormula() {
		return formula;
	}
	public void setFormula(String formula) {
		this.formula = formula;
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
	public String getList() {
		return list;
	}
	public void setList(String list) {
		this.list = list;
	}
	public String getConstraint() {
		return constraint;
	}
	public void setConstraint(String constraint) {
		this.constraint = constraint;
	}
	public Long getNewLength() {
		return newLength;
	}
	public void setNewLength(Long newLength) {
		this.newLength = newLength;
	}
	public String getFunctionBelongTo() {
		return functionBelongTo;
	}
	public void setFunctionBelongTo(String functionBelongTo) {
		this.functionBelongTo = functionBelongTo;
	}
	public boolean isReadOnly() {
		return readOnly;
	}
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getDefaultValueFrom() {
		return defaultValueFrom;
	}
	public void setDefaultValueFrom(String defaultValueFrom) {
		this.defaultValueFrom = defaultValueFrom;
	}
	public Long getStbId() {
		return stbId;
	}
	public void setStbId(Long stbId) {
		this.stbId = stbId;
	}
	public String getRequired() {
		return required;
	}
	public void setRequired(String required) {
		this.required = required;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getNewUnit() {
		return newUnit;
	}
	public void setNewUnit(String newUnit) {
		this.newUnit = newUnit;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	
	
}
