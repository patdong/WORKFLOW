package cn.ideal.wf.data.analyzer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ideal.wf.model.WorkflowUser;

/**
 * 查询公共pojo，用于表单查询和表单入库
 * @author 郭佟燕
 * @version 2.0
 *
 */
public class Storage {
	
	private Long bizId;                                                               //业务编号
	private String tableName;                                                         //查询主表名
	private Long wfId;                                                                //流程编号
	private Long tbId;                                                                //表单编号
	private Long defId;                                                               //用户自定义编号
	private Map<String,String> fields;                                                //入库的字段集合
	private Map<String,List<Map<String,String>>> sFields;                             //入库的子表字段集合
	private Map<String,List<Long>> sIds;                                              //要删除的子表关键字集合
	private WorkflowUser user;                                                        //操作用户
	
	private Long beginNumber;                                                         //查询的指定记录开始下标
	private Long size = 10l;                                                          //查询的个数	
	private Map<String,Map<String,Object>> parameters;                                //查询条件集合
	private String scope;                                                             //查询范围：申请、审批
	
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
	public Map<String,String> getFields() {
		return fields;
	}
	public void setFields(Map<String,String> fields) {
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
		if(pageNumber == null) this.beginNumber = null;
		else this.beginNumber = (pageNumber-1) * size;
	}
	public Map<String,Map<String,Object>> getParameters() {
		return parameters;
	}
	public void setParameters(Map<String,Map<String,Object>> parameters) {
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
	public Map<String, List<Map<String,String>>> getsFields() {
		return sFields;
	}
	
	public List<Map<String,String>> getsFields(String stbname) {
		return sFields.get(stbname);
	}
	
	public void setsFields(String stbname, List<Map<String,String>> sFields) {
		if(this.sFields == null) this.sFields = new HashMap<String,List<Map<String,String>>>();
		this.sFields.put(stbname, sFields);
	}
	public List<Long> getsIds(String stbname) {
		if(this.sIds == null) sIds = new HashMap<String,List<Long>>();
		if(sIds.get(stbname) == null) sIds.put(stbname, new ArrayList<Long>());
		
		return sIds.get(stbname);
	}
	public void setsIds(String stbname,List<Long> sIds) {
		if(this.sIds == null) this.sIds = new HashMap<String,List<Long>>();
		if(this.sIds.get(stbname) == null) this.sIds.put(stbname, sIds);
		else this.sIds.put(stbname, sIds);
	}
	public Long getTbId() {
		return tbId;
	}
	public void setTbId(Long tbId) {
		this.tbId = tbId;
	}
	public Long getDefId() {
		return defId;
	}
	public void setDefId(Long defId) {
		this.defId = defId;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	
}
