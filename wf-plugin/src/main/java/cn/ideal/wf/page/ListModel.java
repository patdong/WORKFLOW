package cn.ideal.wf.page;

import java.util.List;
import java.util.Map;

import cn.ideal.wf.model.Workflow;
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
	private Workflow  wf;
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
	public Workflow getWf() {
		return wf;
	}
	public void setWf(Workflow wf) {
		this.wf = wf;
	}
	public List<WorkflowTableElement> getTableList() {
		return tableList;
	}
	public void setTableList(List<WorkflowTableElement> tableList) {
		this.tableList = tableList;
	}
	
}
