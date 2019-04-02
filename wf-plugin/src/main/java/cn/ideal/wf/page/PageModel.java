package cn.ideal.wf.page;
/**
 * 前端从表单列表进入表单详情的参数信息
 * @author 郭佟燕
 * @version 2.0
 */
import java.util.List;
import java.util.Map;

import cn.ideal.wf.model.Workflow;
import cn.ideal.wf.model.WorkflowAction;
import cn.ideal.wf.model.WorkflowBrief;
import cn.ideal.wf.model.WorkflowNode;
import cn.ideal.wf.model.WorkflowTableBrief;
import cn.ideal.wf.model.WorkflowTableElement;

public class PageModel extends Model{
	private Long bizId;                            //当前业务编号，初始为空
	private Workflow wf;                           //流程概述
	private String nodeName;                       //当前办理节点
	private List<WorkflowNode> nextNodes;          //当前办理节点的续办节点
	private int nextNodeSize;                      //当前办理节点的续办节点个数
	private WorkflowTableBrief tableBrief;         //表单概述
	private WorkflowBrief wfBrief;                 //流程
	private String table;                    //表单
	private List<WorkflowTableElement> headLst;    //表单头
	private List<WorkflowTableElement> bodyLst;    //表单体
	private List<WorkflowTableElement> footLst;    //表单尾
	private List<WorkflowAction> buttons;          //节点按钮
	private Map<String, Object> bizTable;          //表单数据
	private String flowChat;                       //流程图
	public Long getBizId() {
		return bizId;
	}

	public void setBizId(Long bizId) {
		this.bizId = bizId;
	}

	public Workflow getWf() {
		return wf;
	}

	public void setWf(Workflow wf) {
		this.wf = wf;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public WorkflowTableBrief getTableBrief() {
		return tableBrief;
	}

	public void setTableBrief(WorkflowTableBrief tableBrief) {
		this.tableBrief = tableBrief;
	}

	public List<WorkflowTableElement> getHeadLst() {
		return headLst;
	}

	public void setHeadLst(List<WorkflowTableElement> headLst) {
		this.headLst = headLst;
	}

	public List<WorkflowTableElement> getBodyLst() {
		return bodyLst;
	}

	public void setBodyLst(List<WorkflowTableElement> bodyLst) {
		this.bodyLst = bodyLst;
	}

	public List<WorkflowTableElement> getFootLst() {
		return footLst;
	}

	public void setFootLst(List<WorkflowTableElement> footLst) {
		this.footLst = footLst;
	}

	public Map<String, Object> getBizTable() {
		return bizTable;
	}

	public void setBizTable(Map<String, Object> bizTable) {
		this.bizTable = bizTable;
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

	public List<WorkflowAction> getButtons() {
		return buttons;
	}

	public void setButtons(List<WorkflowAction> buttons) {
		this.buttons = buttons;
	}

	public List<WorkflowNode> getNextNodes() {
		return nextNodes;
	}

	public void setNextNodes(List<WorkflowNode> nextNodes) {
		this.nextNodes = nextNodes;
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
