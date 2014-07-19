package com.safebox.bean;


public class AccountInfo {
	private String account_id, site_name, account_name, account_password, account_type;
	private int user_id;
	private boolean is_locked = false;

	public AccountInfo(){
	}
	
	public AccountInfo(String account_id, String site_name, String account_name, String account_password, String account_type, boolean is_locked, int user_id){
		this.account_id = account_id;
		this.site_name = site_name;
		this.account_name = account_name;
		this.account_password = account_password;
		this.account_type = account_type;
		this.user_id = user_id;
		this.is_locked = is_locked;
	}
	/*public AccountInfo(String account_id, String site_name, String account_name, String account_password, String account_type){
		this.account_id = account_id;
		this.site_name = site_name;
		this.account_name = account_name;
		this.account_password = account_password;
		this.account_type = account_type;
	}*/
	
	public AccountInfo(String account_id){
		this.account_id = account_id;
	}
	/*public AccountInfo(String site_name, String account_name, String account_password, String account_type){
		this.account_name = account_name;
		this.account_password = account_password;
		this.account_type = account_type;
	}*/
	
	public String getAccount_id() {
		return account_id;
	}

	public void setAccount_id(String account_id) {
		this.account_id = account_id;
	}
	
	public String getSite_name(){
		return this.site_name;
	}
	
	public void setSite_name(String site_name){
		this.site_name = site_name;
	}
	
	public String getAccount_name() {
		return account_name;
	}

	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}

	public String getAccount_password() {
		return account_password;
	}

	public void setAccount_password(String account_password) {
		this.account_password = account_password;
	}
	
	public String getAccount_type() {
		return account_type;
	}

	public void setAccount_type(String account_type) {
		this.account_type = account_type;
	}
	
	public boolean getIs_locked() {
		return is_locked;
	}

	public void setIs_locked(boolean is_locked) {
		this.is_locked = is_locked;
	}
	
	public void setUser_Id(int user_id){
		this.user_id = user_id;
	}
	
	public int getUser_Id(){
		return user_id;
	}

}
