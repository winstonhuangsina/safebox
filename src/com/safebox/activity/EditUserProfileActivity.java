package com.safebox.activity;

import intent.pack.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import com.safebox.action.LoginAction;
import com.safebox.action.RegisterAction;
import com.safebox.action.ShowAccountListAction;
import com.safebox.backup.TestByButtonActivity;
import com.safebox.bean.UserProfile;
import com.safebox.msg.MyApplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;

public class EditUserProfileActivity extends Activity {

	private EditText username_editText, password_editText, email_editText;
	private Button save_button;
	
	
	private String userNameString, psdString, emailString;

	//private LoginAction loginAction;
	private UserProfile userProfile;
	private String inproper_username_psw, type_number_char;
	private String user_name_exist, user_name_psw_incorrect;
	private String password_diff;
	private String validate_email_addr, save_success;
	private long exitTime = 0;
	public MyApplication myApplication;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userprofile);

		initial();// initial all variate within the intial();

		// register(); //click register button to validate, register and to
		// accountlist activity
	}
	
	
	
	private void initial() {
		
		myApplication = (MyApplication) getApplication();
		myApplication.addActivity(this);
		
		username_editText = (EditText) findViewById(R.id.edit_username_editview);
		password_editText = (EditText) findViewById(R.id.edit_password_editview);
		email_editText = (EditText) findViewById(R.id.edit_email_editview);
		
		save_button = (Button) findViewById(R.id.login_button);

		
		// validation msg
		inproper_username_psw = this.getString(R.string.inproper_username_psw);
		type_number_char = this.getString(R.string.type_number_char);
		user_name_exist = this.getString(R.string.user_name_exist);
		password_diff = this.getString(R.string.password_diff);
		validate_email_addr = this.getString(R.string.validate_email_addr);
		save_success = this.getString(R.string.save_success);
		/*// validation msg
		inproper_username_psw = this.getString(R.string.inproper_username_psw);
		type_number_char = this.getString(R.string.type_number_char);
		// user_name_exist = this.getString(R.string.user_name_exist);
		// password_diff = this.getString(R.string.password_diff);

		// valication msg for login
		user_name_psw_incorrect = this
				.getString(R.string.user_name_psw_incorrect);

		// identify if user login, yes to show account page.
		ifLogin();*/
		
		
		aotuFillEditText();
		
		save_userprofile();
	}

	// identify if user login.
	private void aotuFillEditText(){
		myApplication = (MyApplication) getApplication();
		String username = myApplication.getUsername();
		String password = myApplication.getUserPassword();
		String email = myApplication.getUserEmail();
		if (null != username) {
			username_editText.setText(username);
		}
		if(null != password){
			password_editText.setText(password);
		}
		if(null != email){
			email_editText.setText(email);
		}
		
	}
	
	

	private void save_userprofile() {
		save_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// get the string value to validate
				userNameString = username_editText.getText().toString();
				psdString = password_editText.getText().toString();
				emailString = email_editText.getText().toString();
				
				userProfile = new UserProfile(userNameString, psdString, emailString);
				//loginAction = new LoginAction(userProfile, LoginActivity.this);
				myApplication = (MyApplication) getApplication();
				myApplication.setUsername(userNameString);
				myApplication.setUserPassword(psdString);
				myApplication.setUserEmail(emailString);
				if (validateInput()) {
					// registerIntoDB();// register username and password into
					// database
					//commonDBAction.addUsernameIntoDB();
					toastShow(save_success);
					
				} else
					;
			}
		});
	}

	private boolean validateInput() {
		Pattern email_pattern = Pattern.compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$", Pattern.CASE_INSENSITIVE); 
		Matcher email_matcher = email_pattern.matcher(emailString);
		if (null == psdString || psdString.length() <= 5) {
			toastShow(inproper_username_psw);
			return false;
		} else if (!email_matcher.matches()) {
			toastShow(validate_email_addr);
			return false;
		} else {
			return true;
		}
	}

	/*private void clickToAccountList() {
		// pass context to the next activity.
		Intent intent = new Intent();
		intent.putExtra("userName", userNameString);
		intent.putExtra("password", psdString);
		Log.v("--- RegisterActivity---onClick()---after registerIntoDB()---",
				"to next activity");
		intent.setClass(LoginActivity.this, ShowAccountListActivity.class);
		startActivity(intent);
	}*/

	private void toastShow(String text) {
		Toast.makeText(EditUserProfileActivity.this, text, 1000).show();
	}
}
