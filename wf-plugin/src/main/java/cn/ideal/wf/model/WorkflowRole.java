package cn.ideal.wf.model;
/**
 * 工作流角色pojo
 * @author 郭佟燕
 * @version 2.0
 */
import java.io.Serializable;

public class WorkflowRole  implements Serializable{

	private static final long serialVersionUID = -1804339235116924346L;
	
	private Long roleId;
	private String roleName;
	private Long unitId;
	private String unitName;
	
	public WorkflowRole(){}
	
	public WorkflowRole(Long roleId,Long unitId){
		this.setRoleId(roleId);
		this.setUnitId(unitId);
	}
	
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
