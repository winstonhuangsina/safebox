package com.safebox.activity;

import intent.pack.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import com.safebox.action.ShowAccountListAction;
import com.safebox.adapter.ShowAccountListAdapter;
import com.safebox.msg.ExitApp;
import com.safebox.msg.MsgString;
import com.safebox.msg.MyApplication;

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
	private SharedPreferences sp;
	
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
		if(null != savedInstanceState){
			System.out.println("#########showaccount list savedInstanceState ################");

		}
		
		setContentView(R.layout.testlistview);
		
		myApplication = (MyApplication) getApplication();
		myApplication.addActivity(this);
		mListView = (ListView) findViewById(R.id.listview);
		if (mThread == null) {
			mThread = new Thread(runnable);
			mThread.start();

		} else {
			Toast.makeText(getApplication(),

			"test",

			Toast.LENGTH_LONG).show();
		}

		
        
		clickToEdit();
		
	}

	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		// 重写handleMessage()方法，此方法在UI线程运行
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_SUCCESS:
				
				listForAdapter= (ArrayList<AccountLayoutBean>) msg.obj;
				Toast.makeText(getApplication(),
						getApplication().getString(R.string.get_layoutbeanlist_success),
						Toast.LENGTH_LONG).show();
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
		// 重写run()方法，此方法在新的线程中运行
		@Override
		public void run() {
			ArrayList<AccountLayoutBean> layoutBeanList = new ArrayList<AccountLayoutBean>();
			try {
				layoutBeanList = getAccountList();
				//System.out.println("######## ShowAccountListActivity  Runnable() #####");
			} catch (Exception e) {
				mHandler.obtainMessage(MSG_FAILURE).sendToTarget();// 获取图片失败
				return;
			}
			// 获取图片成功，向UI线程发送MSG_SUCCESS标识和bitmap对象
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
	public void  onStart(){
		super.onStart();
		//System.out.println("#########onStart() = "+getRunningActivityName());
	}
	@Override
	public void onRestart(){
		super.onRestart();
		listAdapter.notifyDataSetChanged();
		//System.out.println("#########OnRestart() = "+getRunningActivityName());
		mListView.setAdapter(listAdapter);
	}
	
	@Override
	public void onResume(){
		super.onResume();
		//System.out.println("#########onResume() = "+getRunningActivityName());
	}
	
	@Override
	public void onStop(){
		super.onStop();
		//System.out.println("#########onStop() = "+getRunningActivityName());
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		//System.out.println("#########onDestroy() = "+getRunningActivityName());
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
				
				Log.v("account name = ", position+"-----" + accountList.get(position).getAccountInfo().getAccount_name());
				Log.v("account id = ", accountList.get(position).getAccountInfo().getAccount_id());
				
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
				//Log.v("###### = ", ""+is_locked);
				if(null!= account_name && null!=account_password&&null != account_id && null != site_name){
					Intent intent = new Intent();
					intent.putExtra(SITE_NAME, site_name);
					intent.putExtra(ACCOUNT_ID, account_id);
					intent.putExtra(ACCOUNT_NAME, account_name.toString());
					intent.putExtra(ACCOUNT_PASSWORD, account_password.toString());
					intent.putExtra(ACCOUNT_TYPE, account_type.toString());
					intent.putExtra(IS_LOCKED, is_locked);
					
					intent.putExtra(MsgString.FROM_SHOW_ACCOUNT_LIST, MsgString.FROM_SHOW_ACCOUNT_LIST);
					if(is_locked && lockHasSetUp()){
						Log.v("is_locked = ", ""+is_locked);
						Log.v("site_name = ", ""+site_name);
						Log.v("account_name = ", ""+account_name.toString());
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
		
		   String share_type = this.getString(R.string.share_type);
		   String share_extra_text = this.getString(R.string.share_extra_text);
		   
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
			sp.edit().putBoolean(AUTO_LOGIN_IS_CHECK, false).commit();
			
			//Intent intent = new Intent();
            intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            // 反方向
            overridePendingTransition(R.anim.callback_in_from_left, R.anim.callback_out_to_right);
            break;
		case R.id.add_account:
			Toast.makeText(this, "Menu Item add account selected",
					Toast.LENGTH_SHORT).show();
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
		case R.id.menu_share:
			
			Toast.makeText(this, "Menu Item menu_share selected", Toast.LENGTH_SHORT)
					.show();
			break;
		case R.id.logout:
			Toast.makeText(this, "Menu Item logout selected", Toast.LENGTH_SHORT)
			.show();
			sp = this.getSharedPreferences("userInfo", MODE_PRIVATE);
			sp.edit().putBoolean(AUTO_LOGIN_IS_CHECK, false).commit();
			myApplication.cleanUsername();
			toNextActivity(LoginActivity.class, MsgString.FORWARD);
			break;
		case R.id.exit:
			myApplication.exit();
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
		
		//System.out.println("######## ShowAccountListActivity  getAccountList() before  query() user id = " + myApplication.getUserId());
		List<Map<String, Object>> list = showAccountListAction.query(myApplication.getUserId());
		
		//System.out.println("######## ShowAccountListActivity  getAccountList() after  query() #####");
		for(int i = 0; i < list.size(); i++){
			Map<String, Object> map = (Map<String, Object>) list.get(i);
			String account_id = (String) map.get(ACCOUNT_ID);
			String account_name = (String) map.get(ACCOUNT_NAME);
			String account_password = (String) map.get(ACCOUNT_PASSWORD);
			String account_type = (String) map.get(ACCOUNT_TYPE);
			String site_name = (String) map.get(SITE_NAME);
			String is_locked = (String) map.get(IS_LOCKED);
			if(null!= account_type && !account_type.equals(diff_type)){
				//additemType
				//System.out.println("######## ShowAccountListActivity  getAccountList() add item type #####");
				AccountInfo accountInfo = new AccountInfo(account_id, site_name, account_name, account_password, account_type, myApplication.getUserId());
				AccountLayoutBean layoutBean = new AccountLayoutBean(accountInfo, "type");
				accountList.add(layoutBean);
				diff_type = account_type;
				i--;
			}else{
				//additem
				//System.out.println("######## ShowAccountListActivity  getAccountList() add item #####");
				AccountInfo accountInfo = new AccountInfo(account_id, site_name, account_name, account_password, account_type, myApplication.getUserId());
				if(is_locked.equals("1"))
					accountInfo.setIs_locked(true);
				else 
					accountInfo.setIs_locked(false);
				AccountLayoutBean layoutBean = new AccountLayoutBean(accountInfo, "item");
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