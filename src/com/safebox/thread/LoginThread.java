package com.safebox.thread;

import intent.pack.R;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.CheckBox;

import com.safebox.action.LoginAction;
import com.safebox.activity.LoginActivity;
import com.safebox.activity.ShowAccountListActivity;
import com.safebox.msg.CommonUI;
import com.safebox.msg.HttpClientToServer;
import com.safebox.msg.MsgString;
import com.safebox.msg.MyApplication;

public class LoginThread {

	ProgressDialog progressDialog;
	String username, password, action, user_name_psw_incorrect, succMsg, network_not_available, network_exception;
	String user_id_from_query;
	Context context;
	boolean queryRomoteDBResult = false;
	CommonUI commUI;
	MyApplication myApplication;
	private SharedPreferences sp;
	CheckBox remember_password_check;
	private static final int SUCCESS = 1;
	private static final int FAILURE = 0;
	
	//private LoginAction loginAction;
	
	public LoginThread(Context context){
		this.context = (Activity) context;
		sp = context.getSharedPreferences("userInfo", android.content.Context.MODE_PRIVATE); 
	}
	
	
	public void setRunnableParams(String username, String password, String action){
		this.username = username;
		this.password = password;
		this.action = action;
	}
	
	public void startToRun(){
		new Thread(connThread).start();
	}
	
	Runnable connThread = new Runnable(){
	  	  @Override
		public void run() {
			Looper.prepare();
			Log.v("Runnable username = ", username);
			Log.v("Runnable password = ", password);
			//TODO if network is ok.
			//else 
    			
			HttpClientToServer httpClientToServer = new HttpClientToServer(
					username, password, MsgString.PARAMS_QUERY);
			if(httpClientToServer.isNetworkAvailable(context)){
				String response = httpClientToServer.doPost();
				Log.v("response is ", response);
				if (response.equals(MsgString.FAILED)) {
					handler.obtainMessage(FAILURE, user_name_psw_incorrect).sendToTarget();
				}else if(response.equals(MsgString.DO_POST_CONN_EXCEPTION)){
					handler.obtainMessage(FAILURE, network_exception).sendToTarget();
				}else {
					handler.obtainMessage(SUCCESS, response).sendToTarget();
					//handler.obtainMessage(1, ).sendToTarget();
				}
			}else{
				handler.obtainMessage(FAILURE, network_not_available).sendToTarget();
			}
			
			Looper.loop();
		}
	  };
		
	public void setHandleMsg(String succMsg, String user_name_psw_incorrect, String network_not_available, String network_exception){
		this.user_name_psw_incorrect = user_name_psw_incorrect;
		this.succMsg = succMsg;
		this.network_not_available = network_not_available;
		this.network_exception = network_exception;
	}
	
	public void setHandleParams(MyApplication myApplication){
		this.myApplication = myApplication;
	}
	
	public void setHandleUIParams(CheckBox remember_password_check, CommonUI commUI){
		this.remember_password_check = remember_password_check;
		this.commUI = commUI;
	}
	
	@SuppressLint("HandlerLeak")
	Handler handler= new Handler(){
	    	
	    	public void handleMessage (Message msg) {//此方法在ui线程运行
	            switch(msg.what) {
	            case SUCCESS:
	            	commUI.dismissProgressDialog();
	            	//commUI.toastShow(succMsg);
	            	user_id_from_query = (String)msg.obj;
	            	saveValueAndToListActivity();
	                break;

	            case FAILURE:
	            	commUI.dismissProgressDialog();
	            	//if queryExistOnly 
	            	String err = (String)msg.obj;
	            	commUI.toastShow(err);
	                break;
	            }
	    }
	};
	
	
	public void saveValueAndToListActivity(){
		if(remember_password_check.isChecked())  
        {  
         //记住用户名、密码、  
          Editor editor = sp.edit();  
          editor.putString(MsgString.USERNAME_LOGIN, username);  
          editor.putString(MsgString.PASSWORD_LOGIN,password);  
          editor.commit();  
        } 
		myApplication = (MyApplication) ((Activity)context).getApplication();
		myApplication.setUsername(username);
		myApplication.setUserPassword(password);
		myApplication.setUserId(getUserId());
		clickToAccountList();// to next activity
	}
	
	private void clickToAccountList() {
		// pass context to the next activity.
		Intent intent = new Intent();
		intent.putExtra("userName", username);
		intent.putExtra("password", password);
		Log.v("clickToAccountList username = ",username);
		intent.setClass(context, ShowAccountListActivity.class);
		context.startActivity(intent);
		((Activity)context).overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);  
	}
	
	private int getUserId() {
		
		return Integer.valueOf(user_id_from_query);
		//return Integer.valueOf("--");
		//return loginAction.getUserId(username, password);
	}
	
}
