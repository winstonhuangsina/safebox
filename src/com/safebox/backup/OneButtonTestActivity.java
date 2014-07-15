package com.safebox.backup;

import intent.pack.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.safebox.action.LoginAction;
import com.safebox.action.RegisterAction;
import com.safebox.activity.LoginActivity;
import com.safebox.activity.RegisterActivity;
import com.safebox.activity.ShowAccountListActivity;
import com.safebox.bean.UserProfile;
import com.safebox.msg.MyApplication;

public class OneButtonTestActivity extends Activity {

	private EditText userName, password;
	private Button login;
	private String userNameString, psdString;
	private RegisterAction registerAction;
	private LoginAction loginAction;
	private UserProfile userProfile;
	private String inproper_username_psw, type_number_char;
	private String user_name_exist, user_name_psw_incorrect;
	private String password_diff;

	public MyApplication myApplication;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.testbybutton);

		initial();// initial all variate within the intial();

		// register(); //click register button to validate, register and to
		// accountlist activity
	}

	private void initial() {
		//userName = (EditText) findViewById(R.id.login_username_input);
		//password = (EditText) findViewById(R.id.login_password_input);
		login = (Button) findViewById(R.id.test_by_button);

		login();
		
		
	}
	private void login() {
		login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// get the string value to validate
				/*userNameString = userName.getText().toString();
				psdString = password.getText().toString();
				userProfile = new UserProfile(userNameString, psdString);
				loginAction = new LoginAction(userProfile, OneButtonTestActivity.this);
				myApplication = (MyApplication) getApplication();
				myApplication.setUsername(userNameString);
				myApplication.setUserPassword(psdString);*/
				userNameString = "winstonregister";
				psdString = "winston";
				userProfile = new UserProfile(userNameString, psdString);
				registerAction = new RegisterAction(userProfile,
						OneButtonTestActivity.this);
				//registerAction.usernameExist(userNameString);
				registerAction.testAccountDB();
				
			}
		});
	}

}
