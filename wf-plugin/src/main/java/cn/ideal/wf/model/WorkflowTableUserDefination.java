package cn.ideal.wf.model;
import java.io.Serializable;
/**
 * 用户自定义表单简述pojo
 * @author 郭佟燕
 * @version 2.0
 */
import java.util.Date;

public class WorkflowTableUserDefination implements Serializable{
	
	private static final long serialVersionUID = -3396093209656466960L;
	private Long defId;
	private Long tbId;
	private Long wfId;
	private Long userId;
	private String tableName;	
	private Date createdDate;
	private String notification1;
	private String notification2;
	private String notification3;
	private String action1;
	private String action2;
	private String action3;
	private String type;
	
	public Long getDefId() {
		return defId;
	}
	public void setDefId(Long defId) {
		this.defId = defId;
	}
	
	public Long getTbId() {
		return tbId;
	}
	public void setTbId(Long tbId) {
		this.tbId = tbId;
	}
	public Long getWfId() {
		return wfId;
	}
	public void setWfId(Long wfId) {
		this.wfId = wfId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getNotification1() {
		return notification1;
	}
	public void setNotification1(String notification1) {
		this.notification1 = notification1;
	}
	public String getNotification2() {
		return notification2;
	}
	public void setNotification2(String notification2) {
		this.notification2 = notification2;
	}
	public String getNotification3() {
		return notification3;
	}
	public void setNotification3(String notification3) {
		this.notification3 = notification3;
	}
	public String getAction1() {
		return action1;
	}
	public void setAction1(String action1) {
		this.action1 = action1;
	}
	public String getAction2() {
		return action2;
	}
	public void setAction2(String action2) {
		this.action2 = action2;
	}
	public String getAction3() {
		return action3;
	}
	public void setAction3(String action3) {
		this.action3 = action3;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
