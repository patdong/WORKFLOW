package cn.ideal.wf.data.analyzer;

import java.util.List;
import java.util.Map;

import cn.ideal.wf.model.WorkflowTableElement;
import cn.ideal.wf.model.WorkflowUser;

/**
 * 查询公共pojo，用于表单查询和表单入库
 * @author 郭佟燕
 * @version 2.0
 *
 */
public class Storage {
	
	private Long bizId;                                 //业务编号
	private String tableName;                          //查询主表名
	private Long wfId;                                 //流程编号
	private List<WorkflowTableElement> fields;         //入库的字段集合
	private WorkflowUser user;                         //操作用户
	
	private Long beginNumber;                          //查询的指定记录开始下标
	private Long size = 10l;                           //查询的个数	
	private Map<String,Object> parameters;             //查询条件集合
	
	public Long getBizId() {
		return bizId;
	}
	public void setBizId(Long bizId) {
		this.bizId = bizId;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public List<WorkflowTableElement> getFields() {
		return fields;
	}
	public void setFields(List<WorkflowTableElement> fields) {
		this.fields = fields;
	}
	
	public Long getBeginNumber() {
		return beginNumber;
	}
	public void setBeginNumber(Long beginNumber) {
		this.beginNumber = beginNumber;
	}
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
	
	public void setBeginNumberWithPageNumber(Long pageNumber){
		this.beginNumber = (pageNumber-1) * size;
	}
	public Map<String, Object> getParameters() {
		return parameters;
	}
	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}
	public WorkflowUser getUser() {
		return user;
	}
	public void setUser(WorkflowUser user) {
		this.user = user;
	}
	public Long getWfId() {
		return wfId;
	}
	public void setWfId(Long wfId) {
		this.wfId = wfId;
	}
	
}
