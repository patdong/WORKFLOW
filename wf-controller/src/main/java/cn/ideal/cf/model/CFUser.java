package cn.ideal.cf.model;

public class CFUser {
	private Long userId;
	private String realName;
	private String currentOrgName;
	private String orgName;
	private Long currentOrgId;
	private String password;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getCurrentOrgName() {
		return currentOrgName;
	}
	public void setCurrentOrgName(String currentOrgName) {
		this.currentOrgName = currentOrgName;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public Long getCurrentOrgId() {
		return currentOrgId;
	}
	public void setCurrentOrgId(Long currentOrgId) {
		this.currentOrgId = currentOrgId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
