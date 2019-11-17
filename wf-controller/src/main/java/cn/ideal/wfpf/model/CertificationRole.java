package cn.ideal.wfpf.model;

/**
 * 统一认证的角色
 * @author 郭佟燕
 * @version 2.0
 *
 */
public class CertificationRole {
	private Long roleId;
    private String roleName;
    private String currentOrgName;     //用户隶属组织
    private Long currentOrgId;
    
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
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
	
	
}
