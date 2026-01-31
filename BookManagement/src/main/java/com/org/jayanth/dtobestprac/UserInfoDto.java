package com.org.jayanth.dtobestprac;


public class UserInfoDto {

	private Long id;
	private String email;
	private String role;
	private boolean firstLogin;
	private String name;
	public UserInfoDto(Long id, String email, String role, boolean firstLogin,String name) {
		super();
		this.id = id;
		this.email = email;
		this.role = role;
		this.firstLogin = firstLogin;
		this.name=name;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public boolean isFirstLogin() {
		return firstLogin;
	}
	public void setFirstLogin(boolean firstLogin) {
		this.firstLogin = firstLogin;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "UserInfoDto [id=" + id + ", email=" + email + ", role=" + role + ", firstLogin=" + firstLogin
				+ ", name=" + name + "]";
	}
	
	
}
