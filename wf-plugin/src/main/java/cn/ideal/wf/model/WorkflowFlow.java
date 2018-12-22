package cn.ideal.wf.model;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class WorkflowFlow implements Serializable{

	private static final long serialVersionUID = 2892574253156984807L;

	private Long flowId;
	private Long flowParentId;
	private String nodeName;
	private String actionName;
	private String status;
	private Date createdDate;
	private Date finishedDate;
	private Long timeDiffer;
	private Long bizId;
	private Long wfId;
	private List<WorkflowStep> workflowSteps = new LinkedList<WorkflowStep>();
	
	public Long getFlowId() {
		return flowId;
	}
	public void setFlowId(Long flowId) {
		this.flowId = flowId;
	}
	public Long getFlowParentId() {
		return flowParentId;
	}
	public void setFlowParentId(Long flowParentId) {
		this.flowParentId = flowParentId;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
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
	public Date getFinishedDate() {
		return finishedDate;
	}
	public void setFinishedDate(Date finishedDate) {
		this.finishedDate = finishedDate;
	}
	public Long getTimeDiffer() {
		return timeDiffer;
	}
	public void setTimeDiffer(Long timeDiffer) {
		this.timeDiffer = timeDiffer;
	}
	public Long getBizId() {
		return bizId;
	}
	public void setBizId(Long bizId) {
		this.bizId = bizId;
	}
	public Long getWfId() {
		return wfId;
	}
	public void setWfId(Long wfId) {
		this.wfId = wfId;
	}
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	public List<WorkflowStep> getWorkflowSteps() {
		return workflowSteps;
	}
	public void setWorkflowSteps(List<WorkflowStep> workflowSteps) {
		this.workflowSteps = workflowSteps;
	}
	
	
}
