package cn.ideal.wf.model;

import java.io.Serializable;

public class WorkflowUser  implements Serializable{

	private static final long serialVersionUID = -1804339235116924346L;
	
	private Long userId;
	private String userName;
	private Long unitId;
	private String unitName;
	
	public WorkflowUser(){}
	
	public WorkflowUser(Long userId,Long unitId){
		this.setUserId(userId);
		this.setUnitId(unitId);
	}
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
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

}
