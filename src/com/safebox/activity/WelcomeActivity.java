package com.safebox.activity;
import com.safebox.msg.MyApplication;

import intent.pack.R;
import android.app.Activity;  
import android.content.Intent;  
import android.os.Bundle;  
import android.os.Handler;  
import android.util.Log;  
import android.view.Window;  
import android.view.WindowManager;

public class WelcomeActivity extends Activity {  
    private final int SPLASH_DELAY_TIME = 1000 ;  
    private String Tag = "WelcomeActivity" ;  
    public MyApplication myApplication;
    
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        Log.i(Tag , "onCreate()" );  
        super.onCreate(savedInstanceState);  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   WindowManager.LayoutParams.FLAG_FULLSCREEN);  
        requestWindowFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.welcome);  
        myApplication = (MyApplication) this.getApplication();
		myApplication.addActivity(this);  
        new Handler().postDelayed(  
                new Runnable()  
                {  
                    @Override  
                    public void run() {  
                        // TODO Auto-generated method stub  
                        startActivity(new Intent(WelcomeActivity.this , LoginActivity.class));  
                        WelcomeActivity.this.finish();  
                    }  
                      
                }  
        , SPLASH_DELAY_TIME);  
    }  
}  