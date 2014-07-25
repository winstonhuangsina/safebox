package com.safebox.backup;

import com.safebox.activity.LoginActivity;
import com.safebox.msg.HttpClientToServer;
import com.safebox.msg.MsgString;

import intent.pack.R;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TestNetworkActivity extends Activity {
    /** Called when the activity is first created. */
	
	private EditText username;
	private EditText password;
	private TextView tv;
	private Button login;
	private Button reset;
	UserManager userManager;
	private String response = "";
	private static final int SUCCESS = 1;
	private static final int FAILURE = 0;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        /*StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);*/
        username = (EditText)findViewById(R.id.login_username_input);
        password = (EditText)findViewById(R.id.login_password_input);
        //tv = (TextView)findViewById(R.id.login_user_label);
        
        login = (Button)findViewById(R.id.login_button);
        //reset = (Button)findViewById(R.id.reset);
        userManager = new UserManager();
        login.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				new Thread(downloadRun).start();
			}
			Runnable downloadRun = new Runnable(){
	        	  @Override
	        	  public void run() {
	        		  HttpClientToServer httpClientToServer = new HttpClientToServer(username.getText().toString(), password.getText().toString(), MsgString.PARAMS_QUERY);
	        		  String response = httpClientToServer.doPost();
	        		  Log.v("response is ", response);
	        		  if(response.equals(MsgString.SUCCESS)){
	        			  handler.obtainMessage(1).sendToTarget();
	        		  }else{
	        			  handler.obtainMessage(0).sendToTarget();
	        		  }
	        	  }
	        };
		});
        
    }
    
    Handler handler= new Handler(){
        	
        	public void handleMessage (Message msg) {//此方法在ui线程运行
                switch(msg.what) {
                case SUCCESS:
                	toastShow(MsgString.SUCCESS);
                    break;

                case FAILURE:
                	toastShow(MsgString.FAILED);
                    break;
                }
        }
    };
    
    private void toastShow(String text) {
		Toast.makeText(TestNetworkActivity.this, text, 1000).show();
	}
    
}