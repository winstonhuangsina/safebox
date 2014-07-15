package com.safebox.activity;

import intent.pack.R;

import java.util.List;

import com.safebox.action.LoginAction;
import com.safebox.bean.UserProfile;
import com.safebox.msg.LockPatternView;
import com.safebox.msg.MsgString;
import com.safebox.msg.MyApplication;
import com.safebox.msg.LockPatternView.Cell;
import com.safebox.msg.LockPatternView.DisplayMode;

import android.os.Bundle;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;



public class UnLockActivity extends Activity implements
        LockPatternView.OnPatternListener {
    private static final String TAG = "LockActivity";

    private List<Cell> lockPattern;
    private LockPatternView lockPatternView;
    SharedPreferences sp;
    private MyApplication myApplication;
    private boolean from_add_account, from_show_account_list, from_save_account, from_unlock, from_setting_of_lock;
    private String ACTIVITY_NAME = MsgString.FROM_UNLOCK;
    private Class<?> lastActivity = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock);
        myApplication = (MyApplication) this.getApplication();
        identifyFromWhichActivity();
        SharedPreferences preferences = getSharedPreferences(myApplication.getGuestureLockKey(), MODE_PRIVATE);
        String patternString = preferences.getString(myApplication.getGuestureLockKey(),
                null);
        if (patternString == null) {
        	
        	if(from_add_account || from_show_account_list || from_setting_of_lock){
        		toNextActivityWithIntent(LockSetupActivity.class, MsgString.FORWARD, getIntent());
        	}
            return;
        }
        lockPattern = LockPatternView.stringToPattern(patternString);
        
        lockPatternView = (LockPatternView) findViewById(R.id.lock_pattern);
        lockPatternView.setOnPatternListener(this);
        TextView unlock_cancel = (TextView) findViewById(R.id.unlock_cancel_link);
        unlock_cancel.setOnClickListener(listener);
        /*Button unlock_continue = (Button) findViewById(R.id.unlock_continue);
    	unlock_continue.setVisibility(android.view.View.INVISIBLE);*/
    	TextView lockpattern_forget = (TextView) findViewById(R.id.lockpattern_forget_link);
    	lockpattern_forget.setClickable(true);
    	lockpattern_forget.setOnClickListener(listener);
    	TextView lockpattern_old_lock = (TextView) findViewById(R.id.enter_lock_pattern);
    	String enter_old_lock = this.getString(R.string.enter_old_lock_pattern);
    	if(from_setting_of_lock)
    		lockpattern_old_lock.setText(enter_old_lock);
    	sp = this.getSharedPreferences("userInfo", MODE_PRIVATE);
    	
    }
    
private String getRunningActivityName(){
        ActivityManager activityManager=(ActivityManager) getSystemService(this.ACTIVITY_SERVICE);
        String runningActivity=activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
        //Log.i(ACTIVITY_NAME,"runningActivity:"+runningActivity);
        return runningActivity;
    }

	@Override
	public void  onStart(){
		super.onStart();
		System.out.println("#########onStart() = "+getRunningActivityName());
	}
	@Override
	public void onRestart(){
		super.onRestart();
		System.out.println("#########OnRestart() = "+getRunningActivityName());
	}
	
	@Override
	public void onResume(){
		super.onResume();
		System.out.println("#########onResume() = "+getRunningActivityName());
	}
	
	@Override
	public void onStop(){
		super.onStop();
		System.out.println("#########onStop() = "+getRunningActivityName());
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		System.out.println("#########onDestroy() = "+getRunningActivityName());
	}

    @Override
    public void onPatternStart() {
        Log.d(TAG, "onPatternStart");
    }

    @Override
    public void onPatternCleared() {
        Log.d(TAG, "onPatternCleared");
    }

    @Override
    public void onPatternCellAdded(List<Cell> pattern) {
        Log.d(TAG, "onPatternCellAdded");
        Log.e(TAG, LockPatternView.patternToString(pattern));
        // Toast.makeText(this, LockPatternView.patternToString(pattern),
        // Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPatternDetected(List<Cell> pattern) {
        Log.d(TAG, "onPatternDetected");

        if (pattern.equals(lockPattern)) {
        	unLockToNextActivity();
        	/*Button unlock_continue = (Button) findViewById(R.id.unlock_continue);
        	unlock_continue.setVisibility(android.view.View.VISIBLE);
        	unlock_continue.setOnClickListener(listener);*/
            //finish();
        } else {
            lockPatternView.setDisplayMode(DisplayMode.Wrong);
            Toast.makeText(this, R.string.lockpattern_error, Toast.LENGTH_LONG)
                    .show();
        }

    }
    
    
    private OnClickListener listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			
			//String from_reset_lock = null;
			
			/*
			if(null !=intent.getExtras()){
				from_reset_lock = intent.getExtras().getString(MsgString.FROM_RESET_LOCK);
			}*/
			
			
			switch (v.getId()) {
			case R.id.unlock_cancel_link:
				Intent intent = getIntent();
				Log.v("unlock cancel","to show list activity");
				if(from_setting_of_lock){
					toNextActivity(SettingOfLockActivity.class, MsgString.BACKWARD);
					break;
				}
				toNextActivityWithIntent(ShowAccountListActivity.class, MsgString.BACKWARD, intent);
				break;
			case R.id.lockpattern_forget_link:
				Editor editor = sp.edit();  
		          editor.putString(MsgString.USERNAME_LOGIN, null);  
		          editor.putString(MsgString.PASSWORD_LOGIN,null);
		          editor.putBoolean(MsgString.REM_PSW_IS_CHECK, false);
		          editor.putBoolean(MsgString.AUTO_LOGIN_IS_CHECK, false);
		          editor.commit();  
				toNextActivity(LockForgetByLoginActivity.class, MsgString.BACKWARD);
				break;
			default: 
				break;
			}
		}
	};
    
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if(from_show_account_list)
				toNextActivity(ShowAccountListActivity.class, MsgString.BACKWARD);
			if(from_setting_of_lock)
				toNextActivity(SettingOfLockActivity.class, MsgString.BACKWARD);
            break;
			
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void unLockToNextActivity(){
		Intent intent = getIntent();
		if(from_setting_of_lock){
			Log.v("unlock done","to reset the lock activity");
			SharedPreferences preferences = getSharedPreferences(myApplication.getGuestureLockKey(), MODE_PRIVATE);
			preferences.edit().putString(myApplication.getGuestureLockKey(), null)
					.commit();
			String patternString = preferences.getString(myApplication.getGuestureLockKey(), null);
			if (patternString == null) {
				MsgString.toastShow(getBaseContext(),
						"clean the lock pattern sucessfully");
			}
			toNextActivity(LockSetupActivity.class, MsgString.FORWARD);
		}else if(from_show_account_list){
			Log.v("unlock continue","to save account activity");
			toNextActivityWithIntent(SaveAccountActivity.class, MsgString.FORWARD, intent);
		}
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
	
	private void toNextActivityWithIntent(Class<?> nextActivity, String pending, Intent intent_of_last_activity){
    	Intent intent = intent_of_last_activity;
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
  			if(null != extras.getString(MsgString.FROM_ADD_ACCOUNT)){
  				from_add_account = true;
  				lastActivity = AddAccountActivity.class;
  			}
  			if(null != extras.getString(MsgString.FROM_SHOW_ACCOUNT_LIST)){
  				from_show_account_list = true;
  				lastActivity = ShowAccountListActivity.class;
  			}if(null != extras.getString(MsgString.FROM_SAVE_ACCOUNT)){
  				from_save_account = true;
  				lastActivity = SaveAccountActivity.class;
  			}
  			if(null != extras.getString(MsgString.FROM_UNLOCK)){
  				from_unlock = true;
  				lastActivity = UnLockActivity.class;
  			}
  		}
  	}
   /*@Override
   public void onBackPressed() {
   	// 仅适用于2.0或更新版的sdk
   	toNextActivity(backToLastActivity(), MsgString.BACKWARD);
   }
   private Class<?> backToLastActivity(){
   	return lastActivity;
   }*/
	
}
