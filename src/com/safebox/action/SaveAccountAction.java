package com.safebox.action;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.safebox.DAO.AccountInfoDB;
import com.safebox.bean.AccountInfo;

public class SaveAccountAction {
	private AccountInfoDB accountInfoDB;
	private String account_id, site_name, account_name, account_password, account_type;
	private int user_id;
	private boolean is_locked = false;
	private Context con;
	public SaveAccountAction(AccountInfo accountInfo, Context context) {
		Log.v("come to SaveAccountAction now","");
		if(null != accountInfo.getAccount_id())
			account_id = accountInfo.getAccount_id();
		site_name = accountInfo.getSite_name();
		account_name = accountInfo.getAccount_name();
		account_password = accountInfo.getAccount_password();
		account_type = accountInfo.getAccount_type();
		is_locked  = accountInfo.getIs_locked();
		user_id = accountInfo.getUser_Id();
		//is_checked= 
		con = context;
		accountInfoDB = new AccountInfoDB(con);
	}

	public void addAccountInfoIntoDB() {
		System.out.println("##########site_name = " + site_name);
		System.out.println("##########account_name = " + account_name);
		System.out.println("##########account_password = " + account_password);
		accountInfoDB.insert(site_name, account_name, account_password, account_type, is_locked, user_id);
		Toast.makeText(con, "Add account Successed!", Toast.LENGTH_SHORT).show();
	}
	
	public void updateAccountInfo(){
		accountInfoDB = new AccountInfoDB(con);
		accountInfoDB.update(Integer.parseInt(account_id), site_name, account_name, account_password, account_type, is_locked);
	}
	
	public void delAccountInfoById(String id) {
		accountInfoDB.delete(id);;
		Toast.makeText(con, "del account Successed!", Toast.LENGTH_SHORT).show();
	}
	
}
