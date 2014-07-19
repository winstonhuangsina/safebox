package com.safebox.activity;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.safebox.action.LoginAction;
import com.safebox.action.RegisterAction;
import com.safebox.action.ShowAccountListAction;
import com.safebox.backup.TestByButtonActivity;
import com.safebox.bean.UserProfile;
import com.safebox.msg.CommonUI;
import com.safebox.msg.HttpClientToServer;
import com.safebox.msg.MsgString;
import com.safebox.msg.MyApplication;

import intent.pack.R;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import android.widget.TextView;
import android.widget.Toast;

public class LockForgetByLoginActivity extends Activity {

	private EditText userName, password;
	TextView register_link;
	private Button lock_forget_login, lock_forget_cancel_button;
	private String userNameString, psdString;

	//private LoginAction loginAction;
	private UserProfile userProfile;
	private String inproper_username_psw, type_number_char;
	private String user_name_exist, user_name_psw_incorrect, clean_lock_successfully;
	public MyApplication myApplication;
	private boolean from_add_account, from_show_account_list, from_setting_of_lock, from_lock_setup, from_save_account, from_unlock;
	private Class<?> lastActivity = null;
	private boolean from_action_clear, from_action_reset;
	
	final static int SUCCESS = 1;
	final static int FAILURE = 0;
	CommonUI commUI;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lock_forget_by_login);
		identifyFromWhichActivity();
		initial();// initial all variate within the intial();
	}

	/**/	
	private void initial() {
		
		userName = (EditText) findViewById(R.id.lock_forget_login_username_input);
		password = (EditText) findViewById(R.id.lock_forget_login_password_input);
		lock_forget_login = (Button) findViewById(R.id.lock_forget_login_button);
		lock_forget_cancel_button = (Button) findViewById(R.id.lock_forget_cancel_button);
		
		commUI = new CommonUI(LockForgetByLoginActivity.this);
		myApplication = (MyApplication) getApplication();
		
		// validation msg
		inproper_username_psw = this.getString(R.string.inproper_username_psw);
		type_number_char = this.getString(R.string.type_number_char);
		clean_lock_successfully = this.getString(R.string.clean_lock_successfully);

		// valication msg for login
		user_name_psw_incorrect = this
				.getString(R.string.user_name_psw_incorrect);

		lock_forget_login.setOnClickListener(listener);
		lock_forget_cancel_button.setOnClickListener(listener);
	}

	private OnClickListener listener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.lock_forget_login_button:
				// get the string value to validate
				userNameString = userName.getText().toString();
				psdString = password.getText().toString();
				userProfile = new UserProfile(userNameString, psdString);
				//loginAction = new LoginAction(userProfile, LockForgetByLoginActivity.this);
				if (validateInput()) {
					commUI.showProgressDialog();
					validateNamePswExist();
				}
				break;
			case R.id.lock_forget_cancel_button:
				// get the string value to validate
				if(from_unlock)
					toNextActivity(UnLockActivity.class, MsgString.FORWARD);
				if(from_setting_of_lock)
					toNextActivity(SettingOfLockActivity.class, MsgString.FORWARD);
				break;
			default: 
				break;
			}
		}
	};

	

	private boolean validateInput() {
		Pattern pattern = Pattern.compile("[0-9a-zA-Z\u4E00-\u9FA5]+");
		Matcher username_matcher = pattern.matcher(userNameString);
		if (null == userNameString || userNameString.length() <= 5
				|| null == psdString || psdString.length() <= 5) {
			commUI.toastShow(inproper_username_psw);
			return false;
		} else if (!username_matcher.matches()) {
			commUI.toastShow(type_number_char);
			return false;
		} else {
			return true;
		}
	}
	
	private void validateNamePswExist() {
		new Thread(queryRun).start();
	}
	
	
	Runnable queryRun = new Runnable() {
		@Override
		public void run() {
			Looper.prepare();
			usernameExist(); 
			Looper.loop();
		}
	};
	private void usernameExist() {
		HttpClientToServer httpClientToServer = new HttpClientToServer(
				userNameString, psdString, MsgString.PARAMS_QUERY);
		String response = httpClientToServer.doPost();
		
		if (response.equals(MsgString.FAILED)) {
			handlerQuery.obtainMessage(0, response).sendToTarget();
		} else {
			handlerQuery.obtainMessage(1, response).sendToTarget();
		}
		
	}
	Handler handlerQuery = new Handler() {

		public void handleMessage(Message msg) {// 此方法在ui线程运行
			switch (msg.what) {
			case SUCCESS:
				commUI.dismissProgressDialog();
				
				String user_id = String.valueOf(myApplication.getUserId());
				String user_id_from_response = (String)msg.obj;
				if(user_id.equals(user_id_from_response)){
					clickToCleanLock();
				}else{
					commUI.toastShow(user_name_psw_incorrect);
				}
				break;

			case FAILURE:
				commUI.dismissProgressDialog();
				commUI.toastShow(user_name_psw_incorrect);
				break;
			}
		}
	};

	private void clickToCleanLock() {
		//System.out.println("myApplication.getGuestureLockKey() = " + myApplication.getGuestureLockKey());
		SharedPreferences preferences = getSharedPreferences(myApplication.getGuestureLockKey(), MODE_PRIVATE);
		preferences.edit().putString(myApplication.getGuestureLockKey(), null)
				.commit();
		String patternString = preferences.getString(
				myApplication.getGuestureLockKey(), null);
		if (patternString == null) {
			commUI.toastShow(clean_lock_successfully);
		}
		if(from_setting_of_lock){
			//toNextActivity(SettingOfLockActivity.class, MsgString.FORWARD);
			toNextActivityReturnResult(SettingOfLockActivity.class, MsgString.BACKWARD);
		}
		else {
			toNextActivityWithIntent(LockSetupActivity.class, MsgString.FORWARD, getIntent());
		}
		
	}

	
	private void toNextActivityReturnResult(Class<?> nextActivity, String pending){
		Intent intent = new Intent();
		intent.setClass(this, nextActivity);
		//startActivity(intent);
		setResult(RESULT_OK, intent);  
		if(pending.equals(MsgString.BACKWARD)){
			overridePendingTransition(R.anim.callback_in_from_left,
					R.anim.callback_out_to_right);
		}else if (pending.equals(MsgString.FORWARD)){
			overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
		}
		
		finish();
	}
	
	private void toNextActivity(Class<?> nextActivity, String pending){
    	Intent intent = new Intent();
		intent.setClass(this, nextActivity);
		startActivity(intent);
		if(pending.equals(MsgString.BACKWARD)){
			overridePendingTransition(R.anim.callback_in_from_left,
					R.anim.callback_out_to_right);
		}else if (pending.equals(MsgString.FORWARD)){
			overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
		}
		
		finish();
    }
	
	
	private void toNextActivityWithIntent(Class<?> nextActivity, String pending, Intent intent_from_last_activity){
    	Intent intent = intent_from_last_activity;
		intent.setClass(this, nextActivity);
		startActivity(intent);
		if(pending.equals(MsgString.BACKWARD)){
			overridePendingTransition(R.anim.callback_in_from_left,
					R.anim.callback_out_to_right);
		}else if (pending.equals(MsgString.FORWARD)){
			overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
		}
		
		finish();
    }
	
	private void identifyFromWhichActivity(){
   	 	from_setting_of_lock = false;
        from_add_account = false;
        from_show_account_list = false;
        from_save_account = false;
        from_unlock = false;
		Intent intent = getIntent();
		if(null != intent.getExtras()){
			Bundle extras = intent.getExtras();
			if(null != extras.getString(MsgString.FROM_SETTING_OF_LOCK)){
				from_setting_of_lock = true;
				lastActivity = SettingOfLockActivity.class;
			}
			else if(null != extras.getString(MsgString.FROM_ADD_ACCOUNT)){
				from_add_account = true;
				lastActivity = AddAccountActivity.class;
			}
			else if(null != extras.getString(MsgString.FROM_SHOW_ACCOUNT_LIST)){
				from_show_account_list = true;
				lastActivity = ShowAccountListActivity.class;
			}else if(null != extras.getString(MsgString.FROM_SAVE_ACCOUNT)){
				from_save_account = true;
				lastActivity = SaveAccountActivity.class;
			}
			else if(null != extras.getString(MsgString.FROM_UNLOCK)){
				from_unlock = true;
				lastActivity = UnLockActivity.class;
			}
			else 
				lastActivity = ShowAccountListActivity.class;
		}
	}
	
}