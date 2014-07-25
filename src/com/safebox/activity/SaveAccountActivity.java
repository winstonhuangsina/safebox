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
import com.safebox.msg.CommonUI;
import com.safebox.msg.HttpClientToServer;
import com.safebox.msg.MsgString;
import com.safebox.msg.MyApplication;
import com.safebox.network.UploadFile;

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
import android.view.View.OnClickListener;
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

public class SaveAccountActivity extends Activity {
	
	private final static String ACCOUNT_NAME = MsgString.ACCOUNT_NAME, ACCOUNT_ID = MsgString.ACCOUNT_ID;
	private final static String ACCOUNT_PASSWORD = MsgString.ACCOUNT_PASSWORD, ACCOUNT_TYPE = MsgString.ACCOUNT_TYPE;
	private final static String SITE_NAME = MsgString.SITE_NAME, IS_LOCKED = MsgString.IS_LOCKED;
	private final static String AUTO_LOGIN_IS_CHECK = MsgString.AUTO_LOGIN_IS_CHECK;
	private EditText account_name, account_password, site_name;
	private Button save_account, del_account;
	private CheckBox checkbox_gesture_password = null;
	private String site_name_string, account_name_string, account_password_string, account_type_string;
	private String account_info_imcomplete, spin_account_type_hint, same_lock_as_before, del_account_successfully;
	private String account_id_string;
	private SaveAccountAction saveAccountAction;
	private AccountInfo accountInfo = new AccountInfo();
	private Spinner spin_account_type = null;
	ArrayAdapter<String> account_type_adapter = null;
	private String[] account_type_array = null; 
	MyApplication myApplication;
	private SharedPreferences sp;
	CommonUI commUI;
	boolean is_locked = false;
	
	private final static String ACTIVITY_NAME = MsgString.FROM_SAVE_ACCOUNT;
	private boolean from_setting_of_lock, from_add_account, from_show_account_list, from_save_account, from_unlock;
	private Class<?> lastActivity = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.saveaccount);
		identifyFromWhichActivity();
		initial();
		
		if (isIntentAvailable(SaveAccountActivity.this, getIntent())) {
			setAccountInfoOnTextEdit();
		}
		accountTypeSpin();
		
		//clickToSaveAccountInfo();
		//clickToDelAccount();
	}
	
	private void initial(){
		commUI = new CommonUI(this);
		myApplication = (MyApplication) this.getApplication();
		myApplication.addActivity(this);
		account_name = (EditText) findViewById(R.id.account_name);
		account_password = (EditText) findViewById(R.id.account_password);
		site_name = (EditText) findViewById(R.id.site_name);
		save_account = (Button) findViewById(R.id.save_account);
		del_account = (Button) findViewById(R.id.del_account);
		checkbox_gesture_password = (CheckBox) findViewById(R.id.checkbox_gesture_password);
		account_info_imcomplete = this.getString(R.string.account_info_imcomplete);
		same_lock_as_before = this.getString(R.string.same_lock_as_before);
		del_account_successfully = this.getString(R.string.del_account_successfully);
		save_account.setOnClickListener(listener);
		del_account.setOnClickListener(listener);
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menuonactionbar, menu);
		
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
	
	
private String getRunningActivityName(){
        ActivityManager activityManager=(ActivityManager) getSystemService(this.ACTIVITY_SERVICE);
        String runningActivity=activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
        //Log.i(ACTIVITY_NAME,"runningActivity:"+runningActivity);
        return runningActivity;
    }
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//Intent intent = new Intent();
		MyApplication myApplication = (MyApplication) getApplication();
		switch (item.getItemId()) {
		case android.R.id.home:
			toNextActivity(ShowAccountListActivity.class, MsgString.BACKWARD);
            break;
		case R.id.add_account:
			toNextActivity(AddAccountActivity.class, MsgString.FORWARD);
			break;
		case R.id.menu_settings:
			Log.v("to setting of lock activity", "");
			toNextActivity(SettingOfLockActivity.class, MsgString.FORWARD);
			break;
		case R.id.logout:
			myApplication.cleanUsername();
			sp = this.getSharedPreferences("userInfo", MODE_PRIVATE);
			sp.edit().putBoolean(AUTO_LOGIN_IS_CHECK, false).commit();
			toNextActivity(LoginActivity.class, MsgString.BACKWARD);
			break;
		case R.id.exit:
			myApplication.exit();
			System.exit(0);
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
		account_type_adapter = new ArrayAdapter<String>(SaveAccountActivity.this,
                android.R.layout.simple_spinner_item, account_type_array);
		spin_account_type.setAdapter(account_type_adapter);
		Bundle extras = getIntent().getExtras();
		
		if(null != extras && null != extras.getString(ACCOUNT_TYPE)){
			account_type_string = extras.getString(ACCOUNT_TYPE);
			spin_account_type.setSelection(getSelectPositionByAccountType(extras.getString(ACCOUNT_TYPE)), true);
		}
		account_type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
		
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
	
	private int getSelectPositionByAccountType(String account_type){
		int position = 0;
		for(int i=0; i< account_type_array.length; i++){
			if(account_type_array[i].equals(account_type)){
				position = i;
			}
		}
		return position;
	}
	
	private void setAccountInfoOnTextEdit(){
		if (getIntent().getExtras() != null) {
			Bundle extras = getIntent().getExtras();
			
			String site_name_bundle = extras.getString(MsgString.SITE_NAME);
			String account_name_bundle = extras.getString(MsgString.ACCOUNT_NAME);
			String account_password_bundle = extras
					.getString(MsgString.ACCOUNT_PASSWORD);
			boolean is_locked_bundle = extras.getBoolean(MsgString.IS_LOCKED);
			is_locked = is_locked_bundle;
			account_id_string = extras
					.getString(MsgString.ACCOUNT_ID);
			Log.v("in SaveAccountInfoActivity, come to setAccountInfoOnTextEdit() account_id_string = ",
					account_id_string);
			if (null != account_name_bundle)
				account_name.setText(account_name_bundle);
			if(is_locked_bundle)
				checkbox_gesture_password.setChecked(is_locked_bundle);
			if(null != account_password_bundle)
				account_password.setText(account_password_bundle);
			if(null != site_name_bundle){
				site_name.setText(site_name_bundle);
			}
			clickToSetLock();
		}
	}
	
	public static boolean isIntentAvailable(Context context, Intent intent) {
	    final PackageManager packageManager = context.getPackageManager();
	    List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
	            PackageManager.GET_ACTIVITIES);
	    return list.size() > 0;
	}
	
	
	private OnClickListener listener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.save_account:

				if (validateAccountInfo()) {
					// pass context to the next activity.
					// it is to edit the account then save the change.
					if(is_locked){
						
						SharedPreferences preferences = getSharedPreferences(myApplication.getGuestureLockKey(),
				                MODE_PRIVATE);
				        String patternString = preferences.getString(myApplication.getGuestureLockKey(),
				                null);
				        if (patternString == null) {
				        	Log.v(ACTIVITY_NAME, "NO lock pattern");
				        	ToNextActivityWithValueReturn(LockSetupActivity.class, 0);
				        	return;
				        }else{
				        	commUI.toastShow(same_lock_as_before);
				        	updateAccountInfo();
							toNextActivity(ShowAccountListActivity.class, MsgString.BACKWARD);
							return;
				        }
					}else{
						updateAccountInfo();
						toNextActivity(ShowAccountListActivity.class, MsgString.BACKWARD);
						return;
					}
				}else{
					commUI.toastShow(account_info_imcomplete);
				}
			
				break;
			case R.id.del_account:
				Log.v("in SaveAccountInfoActivity, come to clickToDelAccountInfo() now del account id =",
						account_id_string);
				if (null != account_id_string) {
					delAccountInfoById();
					toNextActivity(ShowAccountListActivity.class, MsgString.BACKWARD);
				}
				
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
				updateAccountInfo();
				toNextActivity(ShowAccountListActivity.class, MsgString.BACKWARD);
			} 
	    }
	
	 
	private void clickToSetLock(){
		checkbox_gesture_password.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {  
                if (checkbox_gesture_password.isChecked()) {  
                	accountInfo.setIs_locked(true);
                	is_locked = true;
                }else {  
                	accountInfo.setIs_locked(false);
                	is_locked = false;
                }  
  
            }  
        });
	}

	private boolean validateAccountInfo(){
		
		if(null != site_name.getText())
			System.out.println(site_name.getText().toString());
		if(null != account_name.getText()){
			System.out.println(account_name.getText().toString());
		}
		if(null != account_password.getText()){
			System.out.println(account_password.getText().toString());
		}
		if(null != account_type_string){
			System.out.println(account_type_string);
		}
		if(!account_type_string.equals(spin_account_type_hint)){
			System.out.println(account_type_string);
		}
		
		if (null != site_name.getText() && null != account_name.getText()
				&& null != account_password.getText() && null != account_type_string && !account_type_string.equals(spin_account_type_hint)) {
			site_name_string = site_name.getText().toString();
			account_name_string = account_name.getText().toString();
			account_password_string = account_password.getText()
					.toString();
			return true;
		}else {
			return false;
		}
	}
	
	public void updateAccountInfo(){
		accountInfo = new AccountInfo(account_id_string, site_name_string, account_name_string,
				account_password_string, account_type_string, is_locked, myApplication.getUserId());
		//accountInfo.setIs_locked(checkbox_gesture_password.isChecked());
		saveAccountAction = new SaveAccountAction(accountInfo,
				SaveAccountActivity.this);
		saveAccountAction.updateAccountInfo();
	}
	
	public void delAccountInfoById() {

		accountInfo = new AccountInfo(account_id_string);
		saveAccountAction = new SaveAccountAction(accountInfo,
				SaveAccountActivity.this);
		saveAccountAction.delAccountInfoById(account_id_string);
		commUI.toastShow(del_account_successfully);
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

	
	private void ToNextActivityWithValueReturn(Class<?> nextActivity, int requestCode){
		//Intent intent = new Intent();
		Intent intent = new Intent(this,
				nextActivity);
		intent.putExtra(ACTIVITY_NAME, ACTIVITY_NAME);
		startActivityForResult(intent, requestCode);
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
