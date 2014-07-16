package com.safebox.activity;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.safebox.action.LoginAction;
import com.safebox.action.RegisterAction;
import com.safebox.action.ShowAccountListAction;
import com.safebox.backup.TestByButtonActivity;
import com.safebox.backup.TestNetworkActivity;
import com.safebox.backup.UserManager;
import com.safebox.bean.UserProfile;
import com.safebox.msg.AuthorizeActivity;
import com.safebox.msg.CommonUI;
import com.safebox.msg.HttpClientToServer;
import com.safebox.msg.MsgString;
import com.safebox.msg.MyApplication;
import com.safebox.thread.LoginThread;

import intent.pack.R;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.weibo.net.AccessToken;
import com.weibo.net.DialogError;
import com.weibo.net.Weibo;
import com.weibo.net.WeiboDialogListener;
import com.weibo.net.WeiboException;

import android.app.ProgressDialog;



public class LoginActivity extends Activity {

	private EditText userName, password;
	private TextView register_link;
	private Button weibo_login;
	private Button login;
	private ProgressDialog progressDialog;
	
	private String userNameString, psdString;
	private boolean queryRomoteDBResult = false;
	private LoginAction loginAction;
	private UserProfile userProfile;
	private String inproper_username_psw, type_number_char;
	private String user_name_psw_incorrect, login_success, network_not_available;
	private long exitTime = 0;
	public MyApplication myApplication;
	private CheckBox remember_password_check = null, auto_login_check = null;
	private SharedPreferences sp;
	private final static String REM_PSW_IS_CHECK = MsgString.REM_PSW_IS_CHECK;
	private final static String AUTO_LOGIN_IS_CHECK = MsgString.AUTO_LOGIN_IS_CHECK;
	private final static String USERNAME_LOGIN = MsgString.USERNAME_LOGIN;
	private final static String PASSWORD_LOGIN = MsgString.PASSWORD_LOGIN;
	private final static String ACTIVITY_NAME = MsgString.FROM_LOGIN;
	
	private static final String URL_ACTIVITY_CALLBACK = "weiboandroidsdk://TimeLineActivity";
	private static final String FROM = "xweibo";
	
	// 设置appkey及appsecret，如何获取新浪微博appkey和appsecret请另外查询相关信息，此处不作介绍
	private static final String CONSUMER_KEY = "3252079783";// 替换为开发者的appkey，例如"1646212960";
	private static final String CONSUMER_SECRET = "fdc92d92578e60471cb9f29cecd1c77d";// 替换为开发者的appkey，例如"94098772160b6f8ffc1315374d8861f9";
	private CommonUI commUI;
	private LoginThread loginThread;
	
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(null != savedInstanceState){
			System.out.println("#########login savedInstanceState ################");

		}
		setContentView(R.layout.login);
		initial();// initial all variate within the intial();
		autoLoginAndRememberPsw(); // click the text "login" to the login activity
	}
	
	

	@Override 
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		String toast_twice_press = this.getString(R.string.toast_twice_press);
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTime) > 1000) {
				Toast.makeText(getApplicationContext(), toast_twice_press,
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			} else {
				myApplication.exit();
				System.exit(0);
			}
			return true;
		}

		return super.onKeyDown(keyCode, event);
	} 
	
	private void initial() {
		myApplication = (MyApplication) this.getApplication();
		myApplication.addActivity(this);
		userName = (EditText) findViewById(R.id.login_username_input);
		password = (EditText) findViewById(R.id.login_password_input);
		login = (Button) findViewById(R.id.login_button);
		register_link = (TextView) findViewById(R.id.register_link);
		remember_password_check = (CheckBox)findViewById(R.id.remember_password_check);
		auto_login_check = (CheckBox)findViewById(R.id.auto_login_check);
		//weibo_login = (Button) findViewById(R.id.weibo_login);
		commUI = new CommonUI(LoginActivity.this);
		
		// validation msg
		inproper_username_psw = this.getString(R.string.inproper_username_psw);
		type_number_char = this.getString(R.string.type_number_char);
		// valication msg for login
		user_name_psw_incorrect = this
				.getString(R.string.user_name_psw_incorrect);
		login_success = this
				.getString(R.string.login_success);
		network_not_available = this
				.getString(R.string.network_not_available);
		
		// identify if user login, yes to show account page.
		register_link.setClickable(true);
		register_link.setOnClickListener(listener);
		login.setOnClickListener(listener);
		//weibo_login.setOnClickListener(listener);
	}

	private void autoLoginAndRememberPsw() {
		sp = this.getSharedPreferences("userInfo", MODE_PRIVATE); 
		 //判断记住密码多选框的状态  
		if (sp.getBoolean(REM_PSW_IS_CHECK, false)) {
			// 设置默认是记录密码状态
			remember_password_check.setChecked(true);
			userName.setText(sp.getString(USERNAME_LOGIN, ""));
			password.setText(sp.getString(PASSWORD_LOGIN, ""));
			userNameString = userName.getText().toString();
			psdString = password.getText().toString();
			// 判断自动登陆多选框状态
			if (sp.getBoolean(AUTO_LOGIN_IS_CHECK, false)) {
				// 设置默认是自动登录状态
				auto_login_check.setChecked(true);
				// 跳转界面
				validateNamePswExist();
				/*if(validateNamePswExist()){
					clickToAccountList();
				}*/
			}else{
				auto_login_check.setChecked(false);
			}
		}else{
			remember_password_check.setChecked(false);
		}
	      
	    //监听记住密码多选框按钮事件  
	      remember_password_check.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
	            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {  
	                if (remember_password_check.isChecked()) {  
	                    sp.edit().putBoolean(REM_PSW_IS_CHECK, true).commit();  
	                      
	                }else {  
	                    sp.edit().putBoolean(REM_PSW_IS_CHECK, false).commit();  
	                      
	                }  
	  
	            }  
	        });  
	          
	        //监听自动登录多选框事件  
	        auto_login_check.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
	            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {  
	                if (auto_login_check.isChecked()) {  
	                    sp.edit().putBoolean(AUTO_LOGIN_IS_CHECK, true).commit();  
	  
	                } else {  
	                    sp.edit().putBoolean(AUTO_LOGIN_IS_CHECK, false).commit();  
	                }  
	            }  
	        }); 
		
	}
	
	
	private OnClickListener listener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.register_link:
				Intent intent = new Intent(LoginActivity.this,
						RegisterActivity.class);
				startActivity(intent);
				break;
			case R.id.login_button:
				// get the string value to validate
				userNameString = userName.getText().toString();
				psdString = password.getText().toString();
				Log.v("onClick username = ", userNameString);
		        Log.v("onClick password = ", psdString);
				userProfile = new UserProfile(userNameString, psdString);
				//loginAction = new LoginAction(userProfile, LoginActivity.this);
				if (validateInput()) {
					validateNamePswExist();
				}
				break;
			/*case R.id.weibo_login:
				loginByWeibo();
				break;*/
			default: 
				break;
			}
		}
	};
	
	
	
	private void loginByWeibo(){

		Weibo weibo = Weibo.getInstance();
		weibo.setupConsumerConfig(CONSUMER_KEY, CONSUMER_SECRET);

		// Oauth2.0
		// 隐式授权认证方式
		weibo.setRedirectUrl("http://www.cetetek.com");// 此处回调页内容替换为与appkey对应的应用回调页

		weibo.authorize(LoginActivity.this,
				new AuthDialogListener());

	}
	
	
	class AuthDialogListener implements WeiboDialogListener {

		@Override
		public void onComplete(Bundle values) {
			String token = values.getString("access_token");
			String expires_in = values.getString("expires_in");
			String uid = values.getString("uid");
			String content = "access_token : " + token + "  expires_in: "
			+ expires_in + " uid: " + uid;
			
			userName.setText(content);
			AccessToken accessToken = new AccessToken(token, CONSUMER_SECRET);
			accessToken.setExpiresIn(expires_in);
			Weibo.getInstance().setAccessToken(accessToken);
			Toast.makeText(getApplicationContext(),
					"Auth successfully : " + content, Toast.LENGTH_LONG).show();
		}

		@Override
		public void onError(DialogError e) {
			Toast.makeText(getApplicationContext(),
					"Auth error : " + e.getMessage(), Toast.LENGTH_LONG).show();
		}

		@Override
		public void onCancel() {
			Toast.makeText(getApplicationContext(), "Auth cancel",
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(getApplicationContext(),
					"Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}

	
		}
	
	private boolean validateInput() {
		Pattern pattern = Pattern.compile("[0-9a-zA-Z\u4E00-\u9FA5]+");
		Matcher username_matcher = pattern.matcher(userNameString);
		if (null == userNameString || userNameString.length() <= 5
				|| null == psdString || psdString.length() <= 5) {
			commUI.toastShow(LoginActivity.this, inproper_username_psw);
			return false;
		} else if (!username_matcher.matches()) {
			commUI.toastShow(LoginActivity.this, type_number_char);
			return false;
		}  else {
			return true;
		}
	}


	private void validateNamePswExist() {
        commUI.showProgressDialog();
        Log.v("validateNamePswExist username = ", userNameString);
        Log.v("validateNamePswExist password = ", psdString);
        loginThread = new LoginThread(LoginActivity.this);
        loginThread.setRunnableParams(userNameString, psdString, MsgString.PARAMS_QUERY);
        loginThread.setHandleMsg(login_success, user_name_psw_incorrect, network_not_available);
        loginThread.setHandleUIParams(remember_password_check, commUI);
        loginThread.setHandleParams(myApplication);
        loginThread.startToRun();
	}
	
	
}