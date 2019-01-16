package cn.ideal.wfpf.model;

/**
 * 统一认证的组织机构
 * @author 郭佟燕
 * @version 2.0
 *
 */
public class CertificationOrg {
	private Long orgId;
    private String orgName;
    private String currentOrgName;
    
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getCurrentOrgName() {
		return currentOrgName;
	}
	public void setCurrentOrgName(String currentOrgName) {
		this.currentOrgName = currentOrgName;
	}
    
    
}
