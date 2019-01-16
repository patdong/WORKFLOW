package cn.ideal.wfpf.model;
/**
 * 工作流平台上可操作的用户
 * @author 郭佟燕
 * @version 2.0
 * */
import java.util.Set;

public class User {
	private Long id;
    private String username;
    private String password;
    private Set<Role> roles;
    
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
    
}
