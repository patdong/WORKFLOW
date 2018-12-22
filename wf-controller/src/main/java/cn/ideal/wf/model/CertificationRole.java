package cn.ideal.wf.model;

/**
 * 统一认证的角色
 * @author 郭佟燕
 * @version 2.0
 *
 */
public class CertificationRole {
	private Long roleId;
    private String roleName;
    
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
    
    
}
