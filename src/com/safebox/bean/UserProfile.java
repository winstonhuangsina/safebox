package com.safebox.bean;

public class UserProfile {
	private String username, password, email;

	public UserProfile(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public UserProfile(String username, String password, String email){
		this.username = username;
		this.password = password;
		this.email = email;
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
	
	public String getEmail(){
		return this.email;
	}
	
	public void setEmail(String email){
		this.email = email;
	}
	
}
