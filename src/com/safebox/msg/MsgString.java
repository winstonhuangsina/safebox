package com.safebox.msg;

import android.content.Context;
import android.widget.Toast;

import com.safebox.activity.AddAccountActivity;

public class MsgString {
	public final static String DATABASE_NAME = "SAFEBOX.db";
	public final static int DATABASE_VERSION = 3;

	//ACCOUNT数据库常量
	public final static String ACCOUNT_INFO_TABLE = "account_info_table";
	public final static String ACCOUNT_ID = "account_id";
	public final static String SITE_NAME = "site_name";
	public final static String ACCOUNT_PASSWORD = "account_password";
	public final static String ACCOUNT_NAME = "account_name";
	public final static String ACCOUNT_TYPE = "account_type";
	public final static String IS_LOCKED = "is_locked";
	
	
	//用户名数据库常量
	public final static String USER_INFO_TABLE = "user_info_table";
	public final static String USER_ID = "user_id";
	public final static String USER_PASSWORD = "user_password";
	public final static String USER_NAME = "user_name";
	
	//Login page 
	public final static String REM_PSW_IS_CHECK = "REM_PSW_IS_CHECK";
	public final static String AUTO_LOGIN_IS_CHECK = "AUTO_LOGIN_IS_CHECK";
	public final static String USERNAME_LOGIN = "USERNAME_LOGIN";
	public final static String PASSWORD_LOGIN = "PASSWORD_LOGIN";
	
	//Show account list activity
	public final static String IS_LOCK_TRUE = "true";
	public final static String IS_LOCK_FALSE = "false";
	public final static String SHOW_ACCOUNT_TYPE = "type";
	public final static String SHOW_ACCOUNT_ITEM = "item";
	
	
	// Gesture lock
	public final static String GESTURE_LOCK = "GESTURE_LOCK";
	public final static String GESTURE_UNLOCK = "GESTURE_UNLOCK";
	public final static String GESTURE_LOCK_KEY = "GESTURE_LOCK_KEY";
	
	//Setting of Lock
	public final static String FROM_LOCK_SETUP = "FROM_LOCK_SETUP";	
	public final static String FROM_ADD_ACCOUNT = "FROM_ADD_ACCOUNT";	
	public final static String FROM_SAVE_ACCOUNT = "FROM_SAVE_ACCOUNT";	
	public final static String FROM_SHOW_ACCOUNT_LIST = "FROM_SHOW_ACCOUNT_LIST";
	public final static String FROM_UNLOCK = "FROM_UNLOCK";
	public final static String FROM_SETTING_OF_LOCK = "FROM_SETTING_OF_LOCK";
	public final static String FROM_LOGIN = "FROM_LOGIN";
	
	public final static String CLEAR_OR_RESET_LOCK = "CLEAR_OR_RESET_LOCK";
	public final static String RESET_LOCK = "RESET_LOCK";
	public final static String CLEAR_LOCK = "CLEAR_LOCK";
	
	//Penging override 
	public final static String BACKWARD = "BACKWARD";
	public final static String FORWARD = "FORWARD";
	
	
	//URL on HttpClientToServer
	public final static boolean LOCAL_SERVER_URL_TRIGGER = true;
	public final static String LOCAL_SERVER_URL = "http://10.0.2.2:8080/servertest/login.do";
	public final static String REMOTE_SERVER_URL = "http://mysafeboxgit.duapp.com/login.do";
	
	public final static String PARAMS_USERNAME = "PARAMS_USERNAME";
	public final static String PARAMS_PASSWORD = "PARAMS_PASSWORD";
	public final static String PARAMS_EXCEPTION = "PARAMS_EXCEPTION";
	public final static String PARAMS_ACTION = "PARAMS_ACTION";
	public final static String PARAMS_QUERY = "PARAMS_QUERY";
	public final static String PARAMS_INSERT = "PARAMS_INSERT";
	public final static String PARAMS_SEND_EXCEPTION = "PARAMS_SEND_EXCEPTION";
	
	public final static String SUCCESS = "SUCCESS";
	public final static String FAILED = "FAILED";
	
	public final static String PARAMS_LOCATION = "PARAMS_LOCATION";
	public final static String PARAMS_DEVICE = "PARAMS_DEVICE";
	
	
	//Upload exception file to server
	public final static String UPLOAD_FILE_URL =  "http://10.0.2.2:8080/servertest/UploadServlet";
	
	
	public final static String DO_POST_CONN_EXCEPTION = "exception";
	//Shared Preferences
	
	//
	public final static void toastShow(Context context, String text){
		Toast.makeText(context, text, 1000).show();
	}
	
	
}
