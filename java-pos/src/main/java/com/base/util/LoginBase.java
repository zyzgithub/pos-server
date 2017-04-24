package com.base.util;


public class LoginBase{
	private String username;
	private String password;
	
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

	public LoginBase(){
	}
	
	public LoginBase(String username, String password){
		this.username = username;
		this.password = password;
	}

	public boolean check(){
		if(username == null || username.length() < 1 || password == null || password.length() < 1){
			return false;
		}
		return true;
	}
	
	public boolean isDebug(){
		int index = username.indexOf(":debug");
		if(index != -1){
			username = username.substring(0,index);
			return true;
		}else{
			return false;
		}
	}
}
