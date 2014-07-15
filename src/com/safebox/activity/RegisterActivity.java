package com.safebox.activity;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.safebox.action.RegisterAction;
import com.safebox.action.ShowAccountListAction;
import com.safebox.backup.HttpClientToServer;
import com.safebox.backup.TestByButtonActivity;
import com.safebox.bean.UserProfile;
import com.safebox.msg.CommonUI;
import com.safebox.msg.MsgString;
import com.safebox.msg.MyApplication;

import intent.pack.R;
import android.app.Activity;
import android.content.ComponentName;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class RegisterActivity extends Activity {

	private EditText userName, password, password_reinput;
	private Button register;
	private String userNameString, psdString, psdString_reinput;

	private RegisterAction registerAction;
	private UserProfile userProfile;
	private String inproper_username_psw, type_number_char;
	private String user_name_exist;
	private String password_diff;
	public MyApplication myApplication;
	private long exitTime = 0;
	String response_status = "";
	SharedPreferences sp;
	private CheckBox remember_password_check = null, auto_login_check = null;
	private final static String REM_PSW_IS_CHECK = MsgString.REM_PSW_IS_CHECK;
	private final static String AUTO_LOGIN_IS_CHECK = MsgString.AUTO_LOGIN_IS_CHECK;
	private final static String USERNAME_LOGIN = MsgString.USERNAME_LOGIN;
	private final static String PASSWORD_LOGIN = MsgString.PASSWORD_LOGIN;
	
	private static final int SUCCESS = 1;
	private static final int FAILURE = 0;

	private CommonUI commUI;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		initial();// initial all variate within the intial();

		clickToRegister(); // click the text "login" to the login activity
		autoLoginAndRememberPsw();
		register(); // click register button to validate, register and to
					// accountlist activity
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

		userName = (EditText) findViewById(R.id.register_username_input);
		password = (EditText) findViewById(R.id.register_password_input);
		password_reinput = (EditText) findViewById(R.id.register_password_re_input);
		register = (Button) findViewById(R.id.register_button);
		commUI = new CommonUI(RegisterActivity.this);
		// validation msg
		inproper_username_psw = this.getString(R.string.inproper_username_psw);
		type_number_char = this.getString(R.string.type_number_char);
		user_name_exist = this.getString(R.string.user_name_exist);
		password_diff = this.getString(R.string.password_diff);

		//
		remember_password_check = (CheckBox) findViewById(R.id.remember_password_check_register);
		auto_login_check = (CheckBox) findViewById(R.id.auto_login_check_register);

		if (getIntent().getExtras() != null) {
			Intent intent = new Intent();
			intent.setClass(RegisterActivity.this,
					ShowAccountListActivity.class);
			startActivity(intent);
		}

	}

	private void clickToRegister() {
		TextView txtViewRegister = (TextView) findViewById(R.id.login_link);
		txtViewRegister.setClickable(true);
		txtViewRegister.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RegisterActivity.this,
						LoginActivity.class);
				startActivity(intent);

			}
		});
	}

	private void register() {
		register.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// get the string value to validate
				userNameString = userName.getText().toString();
				psdString = password.getText().toString();
				psdString_reinput = password_reinput.getText().toString();
				userProfile = new UserProfile(userNameString, psdString);
				registerAction = new RegisterAction(userProfile,
						RegisterActivity.this);
				if (validateInput()) {
					commUI.showProgressDialog();
					new Thread(queryRun).start();
				}
			}
		});
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
		Log.v(" username exist() response is ", response);
		
		if (response.equals(MsgString.FAILED)) {
			handlerQuery.obtainMessage(0).sendToTarget();
		} else {
			handlerQuery.obtainMessage(1).sendToTarget();
		}
		
	}
	Handler handlerQuery = new Handler() {

		public void handleMessage(Message msg) {// 此方法在ui线程运行
			switch (msg.what) {
			case SUCCESS:
				commUI.dismissProgressDialog();
				commUI.toastShow(user_name_exist);
				break;

			case FAILURE:
				commUI.dismissProgressDialog();
				//commUI.toastShow(MsgString.FAILED);
				new Thread(insertRun).start();
				break;
			}
		}
	};
	
	Runnable insertRun = new Runnable() {
		@Override
		public void run() {
			Looper.prepare();
			
			if (remember_password_check.isChecked()) {
				// 记住用户名、密码、
				setEditor();
			}
			insertUsername();
			Looper.loop();
		}
	};
	
	private void insertUsername(){
		HttpClientToServer httpClientToServer = new HttpClientToServer(
				userNameString, psdString, MsgString.PARAMS_INSERT);
		String response = httpClientToServer.doPost();
		Log.v("response is ", response);
		if (response.equals(MsgString.SUCCESS)) {
			handlerInsert.obtainMessage(1).sendToTarget();
		} else {
			handlerInsert.obtainMessage(0).sendToTarget();
		}
	}
	
	
	Handler handlerInsert = new Handler() {
		public void handleMessage(Message msg) {// 此方法在ui线程运行
			switch (msg.what) {
			case SUCCESS:
				commUI.dismissProgressDialog();
				commUI.toastShow(MsgString.SUCCESS);
				//registerIntoDB();// register username and password into
				setMyApplication();
				clickToAccountList();// to next activity
				break;

			case FAILURE:
				commUI.dismissProgressDialog();
				commUI.toastShow(MsgString.FAILED);
				break;
			}
		}
	};

	
	


	public void setEditor(){
		Editor editor = sp.edit();
		editor.putString(USERNAME_LOGIN, userNameString);
		editor.putString(PASSWORD_LOGIN, psdString);
		editor.commit();
	}
	
	public void setMyApplication(){
		myApplication = (MyApplication) getApplication();
		myApplication.setUsername(userNameString);
		myApplication.setUserPassword(psdString);
		myApplication.setUserId(getUserId());
	}
	

	private boolean validateInput() {
		Pattern pattern = Pattern.compile("[0-9a-zA-Z\u4E00-\u9FA5]+");
		Matcher username_matcher = pattern.matcher(userNameString);
		if (null == userNameString || userNameString.length() <= 5
				|| null == psdString || psdString.length() <= 5) {
			toastShow(inproper_username_psw);
			return false;
		} else if (!username_matcher.matches()) {
			toastShow(type_number_char);
			return false;
		} /*else if (usernameExist()) {
			toastShow(user_name_exist);
			return false;
		}*/ else if (!psdString_reinput.equals(psdString)) {
			toastShow(password_diff);
			return false;
		} else {
			return true;
		}
	}

	/*private void registerIntoDB() {
		// safeBoxDB = new SafeBoxDB(this);
		registerAction.addUsernameIntoDB();
		// Toast.makeText(this, "Add Successed!", Toast.LENGTH_SHORT).show();
	}*/

	private void clickToAccountList() {
		// pass context to the next activity.
		Intent intent = new Intent();
		intent.putExtra("userName", userNameString);
		intent.putExtra("password", psdString);
		intent.setClass(RegisterActivity.this, ShowAccountListActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
	}

	private void toastShow(String text) {
		Toast.makeText(RegisterActivity.this, text, 1000).show();
	}

	
	
	private void autoLoginAndRememberPsw() {
		sp = this.getSharedPreferences("userInfo", MODE_PRIVATE);

		// 监听记住密码多选框按钮事件
		remember_password_check
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (remember_password_check.isChecked()) {
							sp.edit().putBoolean(REM_PSW_IS_CHECK, true)
									.commit();

						} else {
							sp.edit().putBoolean(REM_PSW_IS_CHECK, false)
									.commit();

						}

					}
				});

		// 监听自动登录多选框事件
		auto_login_check
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (auto_login_check.isChecked()) {
							sp.edit().putBoolean(AUTO_LOGIN_IS_CHECK, true)
									.commit();

						} else {
							sp.edit().putBoolean(AUTO_LOGIN_IS_CHECK, false)
									.commit();
						}
					}
				});
	}

	private int getUserId() {
		return registerAction.getUserId(userNameString, psdString);
	}

}