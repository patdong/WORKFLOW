package cn.ideal.wf.model;
import java.io.Serializable;
/**
 * 流程行为的模型
 * @author 郭佟燕
 * @version 2.0
 */
import java.util.Date;

public class WorkflowAction implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3291812435207600034L;
	private Long actionId;                   //行为编号
	private String actionName;               //行为名称
	private String actionCodeName;           //行为代码名称
	private String type;                     //类型（流程可见，按钮可见）
	private String status;                   //状态有效性
	private Date createdDate;                //创建时间
	
	public Long getActionId() {
		return actionId;
	}
	public void setActionId(Long actionId) {
		this.actionId = actionId;
	}
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	public String getActionCodeName() {
		return actionCodeName;
	}
	public void setActionCodeName(String actionCodeName) {
		this.actionCodeName = actionCodeName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	
	
}
