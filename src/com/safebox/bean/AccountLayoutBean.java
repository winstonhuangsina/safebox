package com.safebox.bean;

public class AccountLayoutBean {
	private AccountInfo accountInfo;
	private String account_type;
	public AccountLayoutBean(AccountInfo accountInfo, String account_type){
		this.accountInfo = accountInfo;
		this.account_type = account_type;
	}
	
	public AccountInfo getAccountInfo() {
		return accountInfo;
	}
	public void setAccountInfo(AccountInfo accountInfo) {
		this.accountInfo = accountInfo;
	}
	public String getAccount_type() {
		return account_type;
	}
	public void setAccount_type(String account_type) {
		this.account_type = account_type;
	}

}
