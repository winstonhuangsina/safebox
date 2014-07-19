package com.safebox.activity;

import intent.pack.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import com.safebox.action.ShowAccountListAction;
import com.safebox.adapter.ShowAccountListAdapter;
import com.safebox.msg.CommonUI;
import com.safebox.msg.ExitApp;
import com.safebox.msg.HttpClientToServer;
import com.safebox.msg.MsgString;
import com.safebox.msg.MyApplication;
import com.safebox.network.UploadFile;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListActivity;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.safebox.bean.*;



public class ShowAccountListActivity extends Activity{
	ShowAccountListAdapter listAdapter;
	/** Called when the activity is first created. */
	
	private final static String ACCOUNT_NAME = MsgString.ACCOUNT_NAME, ACCOUNT_ID = MsgString.ACCOUNT_ID;
	private final static String ACCOUNT_PASSWORD = MsgString.ACCOUNT_PASSWORD, ACCOUNT_TYPE = MsgString.ACCOUNT_TYPE;
	private final static String SITE_NAME = MsgString.SITE_NAME, IS_LOCKED = MsgString.IS_LOCKED;
	private final static String AUTO_LOGIN_IS_CHECK = MsgString.AUTO_LOGIN_IS_CHECK;
	String logout_success, share_type, share_extra_text; 
	private SharedPreferences sp;
	private CommonUI commUI;
	SimpleAdapter testListAdapter;
	ShowAccountListAction showAccountListAction;
	private TreeSet<Integer> titleSet = new TreeSet<Integer>();; 
	private ListView mListView;
	private long exitTime = 0;
	public MyApplication myApplication;
	ArrayList<AccountLayoutBean> accountList = new ArrayList<AccountLayoutBean>();
	private Thread mThread;
	private static final int MSG_SUCCESS = 0;
	private static final int MSG_FAILURE = 1;
	private ArrayList<AccountLayoutBean> listForAdapter;
	private static final String ACTIVITY_NAME = MsgString.FROM_SHOW_ACCOUNT_LIST;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.testlistview);
		logout_success = this.getString(R.string.logout_success);
		share_type = this.getString(R.string.share_type);
		share_extra_text = this.getString(R.string.share_extra_text);
		
		commUI = new CommonUI(ShowAccountListActivity.this);
		myApplication = (MyApplication) getApplication();
		myApplication.addActivity(this);
		mListView = (ListView) findViewById(R.id.listview);
		
		new Thread(runnable).start();
		clickToEdit();
	}

	@Override
	public void onResume(){
		super.onResume();
		uploadFile();
		//System.out.println("#########onResume() = "+getRunningActivityName());
	}
	
	private void uploadFile(){
		UploadFile uploadFile = new UploadFile(this);
		HttpClientToServer httpClientToServer = new HttpClientToServer();
		if(httpClientToServer.isNetworkAvailable(this)){
			uploadFile.upload();
		}
	}
	
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		// ��дhandleMessage()�������˷�����UI�߳�����
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_SUCCESS:
				
				listForAdapter= (ArrayList<AccountLayoutBean>) msg.obj;
				/*Toast.makeText(getApplication(),
						getApplication().getString(R.string.get_layoutbeanlist_success),
						Toast.LENGTH_LONG).show();*/
				listAdapter = new ShowAccountListAdapter(ShowAccountListActivity.this, listForAdapter);
		        listAdapter.notifyDataSetChanged();
				mListView.setAdapter(listAdapter);
				titleSet = listAdapter.getTitlePosition();
				break;
			case MSG_FAILURE:
				Toast.makeText(getApplication(),
						getApplication().getString(R.string.get_layoutbeanlist_failure),
						Toast.LENGTH_LONG).show();
				break;
			}
		}
	};

	Runnable runnable = new Runnable() {
		// ��дrun()�������˷������µ��߳�������
		@Override
		public void run() {
			ArrayList<AccountLayoutBean> layoutBeanList = new ArrayList<AccountLayoutBean>();
			try {
				layoutBeanList = getAccountList();
			} catch (Exception e) {
				mHandler.obtainMessage(MSG_FAILURE).sendToTarget();// ��ȡͼƬʧ��
				return;
			}
			// ��ȡͼƬ�ɹ�����UI�̷߳���MSG_SUCCESS��ʶ��bitmap����
			mHandler.obtainMessage(MSG_SUCCESS, layoutBeanList).sendToTarget();
		}
	};
	
	
	private String getRunningActivityName(){
        
        ActivityManager activityManager=(ActivityManager) getSystemService(this.ACTIVITY_SERVICE);
        String runningActivity=activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
//        Log.i(ACTIVITY_NAME,"runningActivity:"+runningActivity);
        return runningActivity;
    }
	
	
	@Override
	public void onRestart(){
		super.onRestart();
		listAdapter.notifyDataSetChanged();
		mListView.setAdapter(listAdapter);
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
	
	
	private void clickToEdit() {
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> arg0, View current_view,
					int position, long arg3) {
				
				if(titleSet.contains(position)){
					current_view.setClickable(false);
				}else{
				
					AccountInfo accountInfo = accountList.get(position).getAccountInfo();
				String account_name = accountInfo.getAccount_name();
				String account_password = accountInfo.getAccount_name();
				String account_id = accountInfo.getAccount_id();
				String site_name = accountInfo.getSite_name();
				String account_type = accountInfo.getAccount_type();
				boolean is_locked = accountInfo.getIs_locked();
				if(null!= account_name && null!=account_password&&null != account_id && null != site_name){
					Intent intent = new Intent();
					intent.putExtra(MsgString.SITE_NAME, site_name);
					intent.putExtra(MsgString.ACCOUNT_ID, account_id);
					intent.putExtra(MsgString.ACCOUNT_NAME, account_name.toString());
					intent.putExtra(MsgString.ACCOUNT_PASSWORD, account_password.toString());
					intent.putExtra(MsgString.ACCOUNT_TYPE, account_type.toString());
					intent.putExtra(MsgString.IS_LOCKED, is_locked);
					
					intent.putExtra(MsgString.FROM_SHOW_ACCOUNT_LIST, MsgString.FROM_SHOW_ACCOUNT_LIST);
					if(is_locked && lockHasSetUp()){
						intent.setClass(ShowAccountListActivity.this,
								UnLockActivity.class);
						startActivity(intent);
						overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
						//finish();
						return;
					}
					//Log.v("#####-------is_locked = ", ""+is_locked);
					intent.setClass(ShowAccountListActivity.this,
							SaveAccountActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
				}
				}
			}
		});
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menuonactionbar, menu);

		//menu.findItem(R.id.menu_settings).setVisible(false);
		// Get the ActionProvider         
		ShareActionProvider provider = (ShareActionProvider) menu.findItem(R.id.menu_share)  
                .getActionProvider();  
        // Initialize the share intent  
        Intent intent = new Intent(Intent.ACTION_SEND);  
        intent.setType(share_type);  
        intent.putExtra(Intent.EXTRA_TEXT, share_extra_text);  
        provider.setShareIntent(intent);  
        
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent();
		MyApplication myApplication = (MyApplication) getApplication();
		switch (item.getItemId()) {
		case android.R.id.home:
			sp = this.getSharedPreferences("userInfo", MODE_PRIVATE);
			sp.edit().putBoolean(MsgString.AUTO_LOGIN_IS_CHECK, false).commit();
			
			//Intent intent = new Intent();
            intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            // ������
            overridePendingTransition(R.anim.callback_in_from_left, R.anim.callback_out_to_right);
            break;
		case R.id.add_account:
			toNextActivity(AddAccountActivity.class, MsgString.FORWARD);
			break;
			
		case R.id.menu_settings:
			intent.setClass(ShowAccountListActivity.this,
					SettingOfLockActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
			//finish();
			//toNextActivity(SettingOfLockActivity.class, MsgString.FORWARD);
			break;
		case R.id.logout:
			commUI.toastShow(logout_success);
			sp = this.getSharedPreferences("userInfo", MODE_PRIVATE);
			sp.edit().putBoolean(MsgString.AUTO_LOGIN_IS_CHECK, false).commit();
			myApplication.cleanUsername();
			toNextActivity(LoginActivity.class, MsgString.FORWARD);
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

	
	
	
	private ArrayList<AccountLayoutBean> getAccountList() {
		
		String diff_type = "";
		
		
		showAccountListAction = new ShowAccountListAction(
				ShowAccountListActivity.this);
		List<Map<String, Object>> list = showAccountListAction.query(myApplication.getUserId());
		for(int i = 0; i < list.size(); i++){
			Map<String, Object> map = (Map<String, Object>) list.get(i);
			String account_id = (String) map.get(MsgString.ACCOUNT_ID);
			Log.v("show acount list activity account id = ", account_id);
			String account_name = (String) map.get(MsgString.ACCOUNT_NAME);
			String account_password = (String) map.get(MsgString.ACCOUNT_PASSWORD);
			String account_type = (String) map.get(MsgString.ACCOUNT_TYPE);
			String site_name = (String) map.get(MsgString.SITE_NAME);
			String is_locked = (String) map.get(MsgString.IS_LOCKED);
			//Ĭ������is_locked Ϊfalse, �������жϡ�
			AccountInfo accountInfo = new AccountInfo(account_id, site_name, account_name, account_password, account_type, false, myApplication.getUserId());
			
			if(null!= account_type && !account_type.equals(diff_type)){
				//additemType
				AccountLayoutBean layoutBean = new AccountLayoutBean(accountInfo, MsgString.SHOW_ACCOUNT_TYPE);
				accountList.add(layoutBean);
				diff_type = account_type;
				i--;
			}else{
				//additem
				//AccountInfo accountInfo = new AccountInfo(account_id, site_name, account_name, account_password, account_type, myApplication.getUserId());
				if(is_locked.equals(MsgString.IS_LOCK_TRUE))
					accountInfo.setIs_locked(true);
				else 
					accountInfo.setIs_locked(false);
				AccountLayoutBean layoutBean = new AccountLayoutBean(accountInfo, MsgString.SHOW_ACCOUNT_ITEM);
				accountList.add(layoutBean);
			}
		}
		return accountList;
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
    }
	
	
	private boolean lockHasSetUp() {
		SharedPreferences preferences = getSharedPreferences(
				myApplication.getGuestureLockKey(), MODE_PRIVATE);
		String patternString = preferences.getString(
				myApplication.getGuestureLockKey(), null);
		if (null == patternString)
			return false;
		else
			return true;
	}

}