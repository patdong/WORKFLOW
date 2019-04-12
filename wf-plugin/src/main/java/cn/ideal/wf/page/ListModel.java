package cn.ideal.wf.page;

import java.util.List;
import java.util.Map;

import cn.ideal.wf.model.WorkflowTableElement;

/**
 * 前端列表页面需要的各参数信息
 * @author 郭佟燕
 * @version 2.0
 *
 */
public class ListModel extends Model{
	private String scope;
	private Page<Map<String,Object>> page;
	private List<WorkflowTableElement> tableList;
	
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public Page<Map<String,Object>> getPage() {
		return page;
	}
	public void setPage(Page<Map<String,Object>> page) {
		this.page = page;
	}
		
	public List<WorkflowTableElement> getTableList() {
		return tableList;
	}
	public void setTableList(List<WorkflowTableElement> tableList) {
		this.tableList = tableList;
	}
	
}
