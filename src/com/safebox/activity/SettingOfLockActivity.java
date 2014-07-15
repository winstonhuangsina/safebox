package com.safebox.activity;

import intent.pack.R;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.safebox.action.CommonDBAction;
import com.safebox.action.LoginAction;
import com.safebox.action.RegisterAction;
import com.safebox.backup.AsyncTaskTestActivity;
import com.safebox.backup.OneButtonTestActivity;
import com.safebox.bean.UserProfile;
import com.safebox.msg.AuthorizeActivity;
import com.safebox.msg.DeviceLocation;
import com.safebox.msg.MsgString;
import com.safebox.msg.MyApplication;

public class SettingOfLockActivity  extends Activity {

	private EditText userName, password;
	private Button clear_lock_button, reset_lock_button, test_button, test_authorize_button;
	private String userNameString, psdString;
	private RegisterAction registerAction;
	private CommonDBAction commonDBAction;
	private UserProfile userProfile;
	private String inproper_username_psw, type_number_char;
	private String user_name_exist, user_name_psw_incorrect;
	private String password_diff;

	public MyApplication myApplication;
	private final static String ACTIVITY_NAME = MsgString.FROM_SETTING_OF_LOCK;
	private boolean from_setting_of_lock, from_add_account, from_show_account_list, from_save_account, from_unlock;
	private Class<?> lastActivity = null;
	private String set_lock_text, clear_lock_text;
	private String patternString; 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*if(null != savedInstanceState){
			System.out.println("#########setting of lock savedInstanceState ################");

		}*/
		setContentView(R.layout.setting_of_lock_activity);
		identifyFromWhichActivity();
		initial();// initial all variate within the intial();
	}
	
	
private String getRunningActivityName(){
        
        ActivityManager activityManager=(ActivityManager) getSystemService(this.ACTIVITY_SERVICE);
        String runningActivity=activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
        return runningActivity;
}
	


	@Override
	public void  onStart(){
		super.onStart();
	}
	@Override
	public void onRestart(){
		super.onRestart();
	}
	
	@Override
	public void onResume(){
		super.onResume();
	}
	
	@Override
	public void onStop(){
		super.onStop();
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
	}

	private void initial() {
		clear_lock_button = (Button) findViewById(R.id.clear_lock_button);
		reset_lock_button = (Button) findViewById(R.id.reset_lock_button);
		test_authorize_button = (Button) findViewById(R.id.test_authorize_button);
		
		test_button = (Button) findViewById(R.id.test_button);
		//login();
		clear_lock_button.setOnClickListener(listener);
		reset_lock_button.setOnClickListener(listener);
		test_button.setOnClickListener(listener);
		test_authorize_button.setOnClickListener(listener);
		
		set_lock_text = this.getString(R.string.set_lock_pattern);
		clear_lock_text = this.getString(R.string.clear_lock_pattern);
		myApplication = (MyApplication) this.getApplication();
		SharedPreferences preferences = this.getSharedPreferences(myApplication.getGuestureLockKey(), MODE_PRIVATE);
		if (preferences != null) {
			patternString = preferences.getString(myApplication.getGuestureLockKey(), null);
			if (patternString != null) {
				clear_lock_button.setText(clear_lock_text);
			} else
				clear_lock_button.setText(set_lock_text);
		}
	}
	
	private OnClickListener listener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.clear_lock_button:
				if(clear_lock_button.getText().equals(clear_lock_text))
					ToNextActivityWithValueReturn(LockForgetByLoginActivity.class, 0);
				else if(clear_lock_button.getText().toString().equals(set_lock_text))
					toNextActivity(LockSetupActivity.class, MsgString.FORWARD);
				break;
			case R.id.reset_lock_button:
				toNextActivity(UnLockActivity.class, MsgString.FORWARD);
				break;
			case R.id.test_button:
					toNextActivity(DeviceLocation.class, MsgString.FORWARD);
				break;
			case R.id.test_authorize_button:
					toNextActivity(AuthorizeActivity.class, MsgString.FORWARD);
				break;
			default:
				break;
			}
		}
	};
	
	 @Override 
	    /**
	     * 当跳转的activity(被激活的activity)使用完毕,销毁的时候调用该方法
	     */ 
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
	        super.onActivityResult(requestCode, resultCode, data); 
	        if(resultCode == Activity.RESULT_OK && data != null){ 
	        	commonDBAction = new CommonDBAction(SettingOfLockActivity.this);
				commonDBAction.cleanAllLock();
				clear_lock_button.setText(set_lock_text);
			} 
	    }
	
	private void ToNextActivityWithValueReturn(Class<?> nextActivity, int requestCode){
		Intent intent = new Intent(this,
				nextActivity);
		intent.putExtra(ACTIVITY_NAME, ACTIVITY_NAME);
		startActivityForResult(intent, requestCode);
	}
	
	
	
	private void toNextActivity(Class<?> nextActivity, String pending){
    	Intent intent = new Intent();
    	intent.putExtra(ACTIVITY_NAME, ACTIVITY_NAME);
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




