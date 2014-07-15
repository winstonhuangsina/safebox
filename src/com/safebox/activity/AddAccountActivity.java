package com.safebox.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.safebox.DAO.AccountInfoDB;
import com.safebox.action.RegisterAction;
import com.safebox.action.SaveAccountAction;
import com.safebox.backup.AccountDBImp;
import com.safebox.backup.TestShowAccountListActivity;
import com.safebox.bean.AccountInfo;
import com.safebox.bean.Person;
import com.safebox.bean.UserProfile;
import com.safebox.msg.MsgString;
import com.safebox.msg.MyApplication;

import intent.pack.R;
import android.os.Bundle;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ShareActionProvider;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class AddAccountActivity extends Activity {
	
	private final static String ACCOUNT_NAME = MsgString.ACCOUNT_NAME, ACCOUNT_ID = MsgString.ACCOUNT_ID;
	private final static String ACCOUNT_PASSWORD = MsgString.ACCOUNT_PASSWORD, ACCOUNT_TYPE = MsgString.ACCOUNT_TYPE;
	private final static String SITE_NAME = MsgString.SITE_NAME, IS_LOCKED = MsgString.IS_LOCKED;
	private final static String AUTO_LOGIN_IS_CHECK = MsgString.AUTO_LOGIN_IS_CHECK;
	
	private EditText account_name, account_password, site_name;
	private Button save_account, del_account;
	private CheckBox checkbox_gesture_password = null;
	private String site_name_string, account_name_string, account_password_string, account_type_string, account_info_imcomplete, spin_account_type_hint;
	private String account_id_string;
	private SaveAccountAction saveAccountAction;
	private AccountInfo accountInfo = new AccountInfo();
	private Spinner spin_account_type = null;
	ArrayAdapter<String> account_type_adapter = null;
	private String[] account_type_array = null; 
	MyApplication myApplication;
	private SharedPreferences sp;
	
	private final static String ACTIVITY_NAME = MsgString.FROM_ADD_ACCOUNT;
	private boolean from_setting_of_lock, from_add_account, from_show_account_list, from_save_account, from_unlock;
	private Class<?> lastActivity = null;
	
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.saveaccount);
		identifyFromWhichActivity();
		initial();
		accountTypeSpin();
		clickToSetLock();
		clickToAddAccountInfo();
		findViewById(R.id.del_account).setVisibility(android.view.View.INVISIBLE);
	}
	
	private void initial(){
		myApplication = (MyApplication) this.getApplication();
		myApplication.addActivity(this);
		account_name = (EditText) findViewById(R.id.account_name);
		account_password = (EditText) findViewById(R.id.account_password);
		site_name = (EditText) findViewById(R.id.site_name);
		save_account = (Button) findViewById(R.id.save_account);
		checkbox_gesture_password = (CheckBox) findViewById(R.id.checkbox_gesture_password);
		account_info_imcomplete = this.getString(R.string.account_info_imcomplete);
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menuonactionbar, menu);
		
		menu.findItem(R.id.add_account).setVisible(false);
		//menu.findItem(R.id.menu_settings).setVisible(false);
		
		// Get the ActionProvider         
		ShareActionProvider provider = (ShareActionProvider) menu.findItem(R.id.menu_share)  
                .getActionProvider();  
        // Initialize the share intent  
        Intent intent = new Intent(Intent.ACTION_SEND);  
        intent.setType("text/plain");  
        intent.putExtra(Intent.EXTRA_TEXT, "Text I want to share");  
        provider.setShareIntent(intent);  
        
		return true;
	}
	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent();
		//MyApplication myApplication = (MyApplication) getApplication();
		switch (item.getItemId()) {
		case android.R.id.home:
            intent = new Intent(this, ShowAccountListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            // 反方向
            overridePendingTransition(R.anim.callback_in_from_left, R.anim.callback_out_to_right);
            break;
		/*case R.id.add_account:
			Toast.makeText(this, "Menu Item add account selected",
					Toast.LENGTH_SHORT).show();
			intent.setClass(AddAccountInfoActivity.this,
					SaveAccountActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
			break;*/
		case R.id.menu_settings:
			
			toNextActivity(SettingOfLockActivity.class, MsgString.FORWARD);
			break;
		case R.id.logout:
			Toast.makeText(this, "Menu Item logout selected", Toast.LENGTH_SHORT)
			.show();
			myApplication.cleanUsername();
			sp = this.getSharedPreferences("userInfo", MODE_PRIVATE);
			sp.edit().putBoolean(AUTO_LOGIN_IS_CHECK, false).commit();
			
			intent.setClass(this,
					LoginActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.callback_in_from_left, R.anim.callback_out_to_right);
			break;
		case R.id.exit:
			myApplication.exit();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	
	private void accountTypeSpin(){
		account_type_array = this.getResources().getStringArray(R.array.account_type_array);
		spin_account_type_hint = account_type_array[0];
		spin_account_type = (Spinner)findViewById(R.id.spin_account_type);
		account_type_adapter = new ArrayAdapter<String>(AddAccountActivity.this,
                android.R.layout.simple_spinner_item, account_type_array);
		spin_account_type.setAdapter(account_type_adapter);
		//Bundle extras = getIntent().getExtras();
		
		/*if(null != extras && null != extras.getString(ACCOUNT_TYPE)){
			account_type_string = extras.getString(ACCOUNT_TYPE);
			spin_account_type.setSelection(getSelectPositionByAccountType(extras.getString(ACCOUNT_TYPE)), true);
		}else {*/
			spin_account_type.setSelection(0,true); 
		//} 
		account_type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
		
		//spin_account_type.setVisibility(View.VISIBLE); 
		spin_account_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
				account_type_string = spin_account_type.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {

            }

        });
		
	}
	
	private void clickToAddAccountInfo(){
		save_account.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (validateAccountInfo()) {

					// pass context to the next activity.
					
					SharedPreferences preferences = getSharedPreferences(
							myApplication.getGuestureLockKey(), MODE_PRIVATE);
					String patternString = preferences.getString(
							myApplication.getGuestureLockKey(), null);
					// it is to add new account, since no account id.
					if (checkbox_gesture_password.isChecked() && patternString == null) {
						// if not yet setup the lock pattern.
						Intent intent = new Intent();
						intent.putExtra(MsgString.FROM_ADD_ACCOUNT, MsgString.FROM_ADD_ACCOUNT);
						intent = new Intent(getBaseContext(),
								LockSetupActivity.class);
						startActivityForResult(intent, 0);
					} else {
						addAccountInfo();
						toNextActivity();
					}
				}else{
					MsgString.toastShow(getBaseContext(), account_info_imcomplete);
				}
			}
		});
	}
	
    @Override 
    /**
     * 当跳转的activity(被激活的activity)使用完毕,销毁的时候调用该方法
     */ 
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
        super.onActivityResult(requestCode, resultCode, data); 
		if (data != null) {
			boolean is_locked = data.getBooleanExtra(IS_LOCKED, false);
			System.out.println("get result from locksetup activity is_locked= " + is_locked);
			accountInfo.setIs_locked(checkbox_gesture_password.isChecked());
			addAccountInfo();
			toNextActivity();
		} 
    }
    
    private void toNextActivity(){
    	Intent intent = new Intent();
		intent.setClass(this, ShowAccountListActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.callback_in_from_left,
				R.anim.callback_out_to_right);
		finish();
    }
    
	
	private void clickToSetLock(){
		checkbox_gesture_password.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {  
                if (checkbox_gesture_password.isChecked()) {  
                    //sp.edit().putBoolean(REM_PSW_IS_CHECK, true).commit();  
                	accountInfo.setIs_locked(true);  
                }else {  
                    //sp.edit().putBoolean(REM_PSW_IS_CHECK, false).commit();
                	accountInfo.setIs_locked(false);
                }  
  
            }  
        });
	}
	
	private boolean validateAccountInfo(){
		
		if (null != site_name.getText() && null != account_name.getText()
				&& null != account_password.getText() && null != account_type_string && !account_type_string.equals(spin_account_type_hint)) {
			site_name_string = site_name.getText().toString();
			account_name_string = account_name.getText().toString();
			account_password_string = account_password.getText()
					.toString();
			return true;
		}else {
				//toastShow(account_info_imcomplete);
				return false;
			}
	}
	public void addAccountInfo() {
		System.out.println("######## AddAccountActivity  addAccountInfo() user id = " + myApplication.getUserId());
		accountInfo = new AccountInfo(null, site_name_string, account_name_string,
				account_password_string, account_type_string, myApplication.getUserId());
		
		saveAccountAction = new SaveAccountAction(accountInfo,
				AddAccountActivity.this);
		saveAccountAction.addAccountInfoIntoDB();
		
	}
	
	/*private void toastShow(String text) {
		Toast.makeText(AddAccountInfoActivity.this, text, 1000).show();
	}*/

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
		
		/*@Override
	    public void onBackPressed() {
	    	// 仅适用于2.0或更新版的sdk
	    	toNextActivity(backToLastActivity(), MsgString.BACKWARD);
	    }
		 private Class<?> backToLastActivity(){
		    	return lastActivity;
		}*/
	
}
