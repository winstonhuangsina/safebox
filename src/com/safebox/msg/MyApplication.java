package com.safebox.msg;

import java.util.LinkedList;
import java.util.List;

import com.safebox.network.UploadFile;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

public class MyApplication extends Application {
	private static MyApplication instance;
	private String username, user_password, user_email;
	private int user_id = 0;

	private List<Activity> mList = new LinkedList<Activity>(); 
    private static MyApplication myInstance; 
	
    
	
	/*public static MyApplication getInstance() {
		return instance;
	}*/
	
	public void cleanUsername(){
		this.username = null;
		this.user_password = null;
		this.user_email = null;
		this.user_id = 0;
	}
	
	
	public void setUserId(int user_id) {
		this.user_id = user_id;
	}

	public int getUserId() {
		return this.user_id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUserPassword(String user_password) {
		this.user_password = user_password;
	}

	public String getUserPassword() {
		return this.user_password;
	}
	
	public void setUserEmail(String user_email) {
		this.user_email = user_email;
	}

	public String getUserEmail() {
		return this.user_email;
	}
	

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		instance = this;
		
		CrashHandler crashHandler = CrashHandler.getInstance(); 
		//注册crashHandler 
		crashHandler.init(getApplicationContext()); 
		//发送以前没发送的报告(可选) 
		//crashHandler.sendPreviousReportsToServer(); 
		
		
		
	}
	
    
    public synchronized static MyApplication getInstance() { 
        if (null == instance) { 
            instance = new MyApplication(); 
        } 
        return instance; 
    } 
    // add Activity  
    public void addActivity(Activity activity) { 
        mList.add(activity); 
    } 
 
    public void exit() { 
        try { 
            for (Activity activity : mList) { 
                if (activity != null) 
                    activity.finish(); 
            } 
        } catch (Exception e) { 
            e.printStackTrace(); 
        } finally { 
            System.exit(0); 
        } 
    } 
    public void onLowMemory() { 
        super.onLowMemory();     
        System.gc(); 
    }  
    
    public String getGuestureLockKey(){
    	//instance = (MyApplication) activity.getApplication();
    	Log.v("getGuestureLockKey = ", MsgString.GESTURE_LOCK + String.valueOf(instance.getUserId()));
    	return MsgString.GESTURE_LOCK + String.valueOf(instance.getUserId());
    }
}