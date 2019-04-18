package cn.ideal.wf.page;
/**
 * 前端从表单列表进入表单详情的参数信息
 * @author 郭佟燕
 * @version 2.0
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ideal.wf.model.WorkflowAction;
import cn.ideal.wf.model.WorkflowBrief;
import cn.ideal.wf.model.WorkflowNode;

public class PageModel extends Model{
	private Long bizId;                            //当前业务编号，初始为空		
	private String nodeName;                       //当前办理节点	
	private List<Map<String,Object>> nextNodes;    //当前办理节点的续办节点
	private int nextNodeSize;                      //当前办理节点的续办节点个数	
	private WorkflowBrief wfBrief;                 //流程概述
	private List<Map<String,String>> buttons;      //节点按钮
	
	private String table;                          //表单绘制
	private String flowChat;                       //流程图
	
	public Long getBizId() {
		return bizId;
	}

	public void setBizId(Long bizId) {
		this.bizId = bizId;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getFlowChat() {
		return flowChat;
	}

	public void setFlowChat(String flowChat) {
		this.flowChat = flowChat;
	}

	public WorkflowBrief getWfBrief() {
		return wfBrief;
	}

	public void setWfBrief(WorkflowBrief wfBrief) {
		this.wfBrief = wfBrief;
	}

	public List<Map<String,String>> getButtons() {
		return buttons;
	}

	public void setButtons(List<WorkflowAction> buttons) {
		this.buttons = new ArrayList<Map<String,String>>();
		for(WorkflowAction wfa : buttons){
			Map<String,String> button = new HashMap<String,String>();
			button.put("actionCodeName", wfa.getActionCodeName());
			button.put("actionName", wfa.getActionName());
			this.buttons.add(button);
		}
	}

	public List<Map<String,Object>> getNextNodes() {
		return nextNodes;
	}

	public void setNextNodes(List<WorkflowNode> nextNodes) {
		this.nextNodes = new ArrayList<Map<String,Object>>();
		for(WorkflowNode wfn : nextNodes){
			Map<String,Object> node = new HashMap<String,Object>();
			node.put("nodeId", wfn.getNodeId());
			node.put("nodeName", wfn.getNodeName());
			this.nextNodes.add(node);
		}
		if(nextNodes != null) this.nextNodeSize = nextNodes.size();
	}

	public int getNextNodeSize() {
		return nextNodeSize;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	
}
