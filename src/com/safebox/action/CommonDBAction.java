package com.safebox.action;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import com.safebox.DAO.AccountInfoDB;
import com.safebox.DAO.UserInfoDB;
import com.safebox.bean.AccountInfo;
import com.safebox.bean.UserProfile;

public class CommonDBAction {
	private UserInfoDB userInfoDB;
	private AccountInfoDB accountInfoDB;
	private AccountInfo accountInfo;
	private String username, password, email;
	private Context con;
	public CommonDBAction(UserProfile userProfile, Context context) {
		username = userProfile.getUsername();
		password = userProfile.getPassword();
		if(null!=userProfile.getEmail()){
			email = userProfile.getEmail();
		}
		con = context;
		userInfoDB = new UserInfoDB(con);
	}
	
	public CommonDBAction(Context context){
		con = context;
		accountInfoDB = new AccountInfoDB(con);
	} 

	public void addUsernameIntoDB() {
		Log.v("come to insert now","");
		Toast.makeText(con, "Add Successed!", Toast.LENGTH_SHORT).show();
	}
	public boolean usernameExist(String username){
		boolean if_exist = false;
		Cursor cursor = userInfoDB.findBy(username);
		if (cursor.moveToNext()) {
			if_exist = true;
		}
		cursor.close();
		return if_exist;
	}
	
	public void cleanAllLock(){
		accountInfoDB.update(false);
	}
	
}
