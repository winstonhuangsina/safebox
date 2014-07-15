package com.safebox.action;

import com.safebox.DAO.AccountInfoDB;
import com.safebox.DAO.UserInfoDB;
import com.safebox.bean.UserProfile;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

public class RegisterAction {
	private UserInfoDB userInfoDB;
	private AccountInfoDB acctDBTest;
	private String username, password, email;
	private Context con;
	public RegisterAction(UserProfile userProfile, Context context) {
		username = userProfile.getUsername();
		password = userProfile.getPassword();
		if(null!=userProfile.getEmail()){
			email = userProfile.getEmail();
		}
		con = context;
		userInfoDB = new UserInfoDB(con);
		acctDBTest = new AccountInfoDB(con);
	}

	public void addUsernameIntoDB() {
		//add encrypted username and password
		
		
		
		Log.v("come to safeBoxDB now","");
		userInfoDB.insert(username, password);
		
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
	
	public Cursor testAccountDB(){
		/*Log.v("come to testAccountDB now","");
		Cursor cursor = acctDBTest.selectby();
		userInfoDB.findBy("winstonregister");
		userInfoDB.select();
		
		Log.v("come to testAccountDB now","");*/
		return null;
	}
	
	public int getUserId(String username, String user_password){
		int user_id = 0;
		Cursor cursor = userInfoDB.findBy(username, user_password);
		if(cursor.moveToNext()){
			user_id = Integer.valueOf(cursor.getString(0).toString().trim());
			Log.v("user_id = ", cursor.getString(0).toString().trim());
		}
		return user_id;
	}
}
