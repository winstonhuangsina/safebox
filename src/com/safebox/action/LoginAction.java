package com.safebox.action;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import com.safebox.DAO.UserInfoDB;
import com.safebox.bean.UserProfile;

public class LoginAction {
	private UserInfoDB userInfoDB;
	private String username, password;
	private Context con;
	public LoginAction(UserProfile userProfile, Context context) {
		username = userProfile.getUsername();
		password = userProfile.getPassword();
		con = context;
		userInfoDB = new UserInfoDB(con);
	}

	public boolean validateNamePsw(String username, String password){
		boolean if_exist = false;
		Cursor cursor = userInfoDB.findBy(username, password);
		if (cursor.moveToNext()) {
			if_exist = true;
		}
		cursor.close();
		return if_exist;
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
