package cn.ideal.wfpf.model;

import java.util.Date;
import java.util.List;

import cn.ideal.wfpf.model.WFPFWorkflow;

/**
 * 表单概述pojo
 * @author 郭佟燕
 * @version 2.0
 *
 */
public class TableBrief {

	private Long tbId;
	private String name;
	private String tableName;
	private String template;          //表单样式类型
	private Long cols;
	private String status;
	private Date createdDate;
	private Long productCount = 0l;   //统计该表在生产中已经产生的数据
	private List<TableLayout> layout;
	private Long wfId;
	private WFPFWorkflow wf;
	private String templateName;      //业务模板名称
	private String alias;             //表单别名
	
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
	public WFPFWorkflow getWf() {
		return wf;
	}
	public void setWf(WFPFWorkflow wf) {
		this.wf = wf;
	}
	public Long getProductCount() {
		return productCount;
	}
	public void setProductCount(Long productCount) {
		this.productCount = productCount;
	}
	public List<TableLayout> getLayout() {
		return layout;
	}
	public void setLayout(List<TableLayout> layout) {
		this.layout = layout;
	}
	public Long getWfId() {
		return wfId;
	}
	public void setWfId(Long wfId) {
		this.wfId = wfId;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	
}
