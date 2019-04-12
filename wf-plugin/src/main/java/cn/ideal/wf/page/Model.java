package cn.ideal.wf.page;

import java.util.List;

import cn.ideal.wf.model.WorkflowTableBrief;

public class Model {
	private List<WorkflowTableBrief> menu;
	private WorkflowTableBrief wftb;                 //表单概述
	
	public List<WorkflowTableBrief> getMenu() {
		return menu;
	}

	public void setMenu(List<WorkflowTableBrief> menu) {
		this.menu = menu;
	}

	public WorkflowTableBrief getWftb() {
		return wftb;
	}

	public void setWftb(WorkflowTableBrief wftb) {
		this.wftb = wftb;
	}

	
}
