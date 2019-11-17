package cn.ideal.wfpf.model;

/**
 * 统一认证的用户
 * @author 郭佟燕
 * @version 2.0
 *
 */
public class CertificationUser {
	private Long userId;
    private String userName;
    private String currentOrgName;        //用户隶属组织
    private String orgName;         //用户隶属组织的组织
    private Long currentOrgId;
    
    
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
	public String getCurrentOrgName() {
		return currentOrgName;
	}
	public void setCurrentOrgName(String currentOrgName) {
		this.currentOrgName = currentOrgName;
	}
	public Long getCurrentOrgId() {
		return currentOrgId;
	}
	public void setCurrentOrgId(Long currentOrgId) {
		this.currentOrgId = currentOrgId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
}
