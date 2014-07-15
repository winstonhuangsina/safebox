package com.safebox.msg;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.safebox.activity.LoginActivity;
import com.safebox.backup.HttpClientToServer;

public class CommonUI extends Thread{
	ProgressDialog progressDialog;
	String username, password, action, failureMsg, succMsg;
	Context context;
	boolean queryRomoteDBResult = false;
	
	public CommonUI(Context context){
		this.context = context;
	}
	
	public void showProgressDialog(){
	progressDialog = new ProgressDialog(context);  
    progressDialog.setMessage("正在向服务器提交请求");  
    progressDialog.setTitle("请稍候");  
    progressDialog.show(); 
	}
	
	public void dismissProgressDialog(){
		progressDialog.dismiss(); 
	}
	
	public void toastShow(Context context, String text) {
		Toast.makeText(context, text, 1000).show();
	}
	
	public void toastShow(String text) {
		Toast.makeText(context, text, 1000).show();
	}
	
	/*
	public void setRunnableParams(String username, String password, String action){
		this.username = username;
		this.password = password;
		this.action = action;
	}
	
	public void startToRun(){
		new Thread(downloadRun).start();
	}
	
	Runnable downloadRun = new Runnable(){
	  	  @Override
	  	  public void run() {
	  		 Looper.prepare();
	  		  HttpClientToServer httpClientToServer = new HttpClientToServer(username, password, MsgString.PARAMS_QUERY);
	  		  String response = httpClientToServer.doPost();
	  		  Log.v("response is ", response);
	  		  if(response.equals(MsgString.SUCCESS)){
	  			  handler.obtainMessage(1).sendToTarget();
	  		  }else{
	  			  handler.obtainMessage(0).sendToTarget();
	  		  }
	  		  Looper.loop();
	  	  }
	  };
		
	public void setHandleParams(String succMsg, String failureMsg){
		this.failureMsg = failureMsg;
		this.succMsg = succMsg;
	}
	Handler handler= new Handler(){
	    	
	    	public void handleMessage (Message msg) {//此方法在ui线程运行
	    		Log.v("handleMessage queryRomoteDBResult = ", ""+queryRomoteDBResult);
	            switch(msg.what) {
	            case 1:
	            	toastShow(context, succMsg);
	            	dismissProgressDialog();
	                break;

	            case 0:
	            	toastShow(context, failureMsg);
	            	dismissProgressDialog();
	                break;
	            }
	    }
	};
	
	public boolean handleResult(){
		Log.v("handleResult queryRomoteDBResult = ", ""+queryRomoteDBResult);
		return queryRomoteDBResult;
	}*/
	
	
	
	
}
