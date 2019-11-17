package cn.ideal.wf.model;
/**
 * 工作流操作pojo
 * @author 郭佟燕
 * @version 2.0
 */
import java.io.Serializable;
import java.util.Date;

public class WorkflowStep implements Serializable{

	private static final long serialVersionUID = 2395968189996894332L;

	private Long stepId;
	private Long flowId;
	private Long dispatchUserId;
	private String dispatchUserName;
	private Long unitId;
	private String unitName;
	private String actionName;
	private String status;
	private Date createdDate;
	private Date finishedDate;
	private Long timeDiffer;
	private Long serial;
	private Long executeUserId;
	private String executeUserName;
	private String nodeName;
	private String reason;
	
	public WorkflowStep(){}
	
	public WorkflowStep(Long flowId,Long stepId){
		this.setFlowId(flowId);
		this.setStepId(stepId);
	}
	
	public Long getStepId() {
		return stepId;
	}
	public void setStepId(Long stepId) {
		this.stepId = stepId;
	}
	public Long getFlowId() {
		return flowId;
	}
	public void setFlowId(Long flowId) {
		this.flowId = flowId;
	}
	public Long getDispatchUserId() {
		return dispatchUserId;
	}
	public void setDispatchUserId(Long dispatchUserId) {
		this.dispatchUserId = dispatchUserId;
	}
	public String getDispatchUserName() {
		return dispatchUserName;
	}
	public void setDispatchUserName(String dispatchUserName) {
		this.dispatchUserName = dispatchUserName;
	}
	public Long getUnitId() {
		return unitId;
	}
	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
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
	
	public Long getSerial() {
		return serial;
	}

	public void setSerial(Long serial) {
		this.serial = serial;
	}

	public Long getExecuteUserId() {
		return executeUserId;
	}
	public void setExecuteUserId(Long executeUserId) {
		this.executeUserId = executeUserId;
	}
	public String getExecuteUserName() {
		return executeUserName;
	}
	public void setExecuteUserName(String executeUserName) {
		this.executeUserName = executeUserName;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	
	
}
