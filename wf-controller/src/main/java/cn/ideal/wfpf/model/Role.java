package cn.ideal.wfpf.model;

/**
 * 工作流平台操作用户的权限
 * @author 郭佟燕
 * @version 2.0
 * */
import java.util.Set;

public class Role {
	private Long id;
    private String name;
    private Set<User> users;
    
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Set<User> getUsers() {
		return users;
	}
	public void setUsers(Set<User> users) {
		this.users = users;
	}

    
}
