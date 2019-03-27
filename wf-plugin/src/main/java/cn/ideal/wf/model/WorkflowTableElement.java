package cn.ideal.wf.model;
import java.io.Serializable;
/**
 * 工作流表单元素pojo
 * @author 郭佟燕
 * @version 2.0
 */
import java.util.Date;

public class WorkflowTableElement implements Serializable{

	private static final long serialVersionUID = 3936610182338053299L;
	private Long id;
	private Long tbId;
	private Long emId;
	private String labelName;
	private String fieldName;
	private String newLabelName;
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
	private String status;
	private Date createdDate;
	private Object dataValue;
	private Long stbId;
	
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
	public String getNewLabelName() {
		return newLabelName;
	}
	public void setNewLabelName(String newLabelName) {
		this.newLabelName = newLabelName;
	}
	public String getNewFunctionName() {
		return newFunctionName;
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
	public Object getDataValue() {
		return dataValue;
	}
	public void setDataValue(Object dataValue) {
		this.dataValue = dataValue;
	}
	public String getFunctionBelongTo() {
		return functionBelongTo;
	}
	public void setFunctionBelongTo(String functionBelongTo) {
		this.functionBelongTo = functionBelongTo;
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
	public void setNewFunctionName(String newFunctionName) {
		this.newFunctionName = newFunctionName;
	}
	public Long getStbId() {
		return stbId;
	}
	public void setStbId(Long stbId) {
		this.stbId = stbId;
	}	
	
}
