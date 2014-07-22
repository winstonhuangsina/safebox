package com.safebox.activity;

import intent.pack.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.safebox.msg.LockPatternView;
import com.safebox.msg.MsgString;
import com.safebox.msg.LockPatternView.Cell;
import com.safebox.msg.LockPatternView.DisplayMode;
import com.safebox.msg.MyApplication;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class LockSetupActivity extends Activity implements
        LockPatternView.OnPatternListener, OnClickListener {

    private static final String TAG = "LockSetupActivity";
    private LockPatternView lockPatternView;
    private Button leftButton;
    private Button rightButton;
    private MyApplication myApplication;

    private static final int STEP_1 = 1; // �?�?
    private static final int STEP_2 = 2; // �?�?次�?�置?????��?????
    private static final int STEP_3 = 3; // ???�?继续??????
    private static final int STEP_4 = 4; // �?�?次�?�置?????��?????
    // private static final int SETP_5 = 4; // ???�?认�?????

    private int step;

    private List<Cell> choosePattern;

    private boolean confirm = false;
    
    private boolean from_setting_of_lock, from_add_account, from_show_account_list, from_save_account, from_unlock;
    private final static String ACTIVITY_NAME = MsgString.FROM_LOCK_SETUP;
    private Class<?> lastActivity = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_setup);
        identifyFromWhichActivity();
        myApplication = (MyApplication) this.getApplication();
		myApplication.addActivity(this);
        lockPatternView = (LockPatternView) findViewById(R.id.lock_pattern);
        lockPatternView.setOnPatternListener(this);
        leftButton = (Button) findViewById(R.id.left_btn);
        rightButton = (Button) findViewById(R.id.right_btn);
        System.out.println("myApplication.getGuestureLockKey() = " + myApplication.getGuestureLockKey());
        step = STEP_1;
        updateView();
    }

    private void updateView() {
        switch (step) {
        case STEP_1:
        	System.out.println("step = " + step);
            leftButton.setText(R.string.cancel);
            rightButton.setText("");
            rightButton.setEnabled(false);
            choosePattern = null;
            confirm = false;
            lockPatternView.clearPattern();
            lockPatternView.enableInput();
            break;
        case STEP_2:
        	System.out.println("step = " + step);
            leftButton.setText(R.string.lockpattern_try_again);
            rightButton.setText(R.string.lockpattern_continue);
            rightButton.setEnabled(true);
            lockPatternView.disableInput();
            break;
        case STEP_3:
        	System.out.println("step = " + step);
            leftButton.setText(R.string.cancel);
            rightButton.setText("");
            rightButton.setEnabled(false);
            lockPatternView.clearPattern();
            lockPatternView.enableInput();
            break;
        case STEP_4:
        	System.out.println("step = " + step);
            leftButton.setText(R.string.cancel);
            if (confirm) {
            	System.out.println("step = " + step + " no input");
                rightButton.setText(R.string.confirm);
                rightButton.setEnabled(true);
                lockPatternView.disableInput();
            } else {
                rightButton.setText("");
                lockPatternView.setDisplayMode(DisplayMode.Wrong);
                lockPatternView.enableInput();
                rightButton.setEnabled(false);
            }

            break;

        default:
            break;
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        case R.id.left_btn:
        	System.out.println("step = " + step);
            if (step == STEP_1 || step == STEP_3 || step == STEP_4) {
                finish();
            } else if (step == STEP_2) {
                step = STEP_1;
                updateView();
            }
            break;

        case R.id.right_btn:
        	System.out.println("step = " + step);
            if (step == STEP_2) {
                step = STEP_3;
                updateView();
            } else if (step == STEP_4) {
                SharedPreferences preferences = getSharedPreferences(myApplication.getGuestureLockKey(), MODE_PRIVATE);
                preferences
                        .edit()
                        .putString(myApplication.getGuestureLockKey(),
                                LockPatternView.patternToString(choosePattern))
                        .commit();
                //Log.v("lock pattern encrypt", LockPatternView.patternToString(choosePattern));
                if(from_add_account){
                	Intent intent = getIntent(); 
                    intent.putExtra(MsgString.IS_LOCKED, false);// 放入返回值 
                    setResult(RESULT_OK, intent);// 放入回传的值,并添加一个Code,方便区分返回的数据 
                    finish();//
                    break;
                }else if(from_save_account){
                	Intent intent = getIntent(); 
                    setResult(RESULT_OK, intent);// 放入回传的值,并添加一个Code,方便区分返回的数据 
                    finish();
                    break;
                }
                else if(from_show_account_list){
                	toNextActivityWithIntent(SaveAccountActivity.class, MsgString.FORWARD, getIntent());
                }else {
                	toNextActivity(SettingOfLockActivity.class, MsgString.BACKWARD);
                }
            }

            break;

        default:
            break;
        }

    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

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
    }

    @Override
    public void onPatternDetected(List<Cell> pattern) {
        Log.d(TAG, "onPatternDetected");

        if (pattern.size() < LockPatternView.MIN_LOCK_PATTERN_SIZE) {
            Toast.makeText(this,
                    R.string.lockpattern_recording_incorrect_too_short,
                    Toast.LENGTH_LONG).show();
            lockPatternView.setDisplayMode(DisplayMode.Wrong);
            return;
        }

        if (choosePattern == null) {
            choosePattern = new ArrayList<Cell>(pattern);
 //           Log.d(TAG, "choosePattern = "+choosePattern.toString());
//            Log.d(TAG, "choosePattern.size() = "+choosePattern.size());
            Log.d(TAG, "choosePattern = "+Arrays.toString(choosePattern.toArray()));
         
            step = STEP_2;
            updateView();
            return;
        }
//[(row=1,clmn=0), (row=2,clmn=0), (row=1,clmn=1), (row=0,clmn=2)]
//[(row=1,clmn=0), (row=2,clmn=0), (row=1,clmn=1), (row=0,clmn=2)]    
        
        Log.d(TAG, "choosePattern = "+Arrays.toString(choosePattern.toArray()));
        Log.d(TAG, "pattern = "+Arrays.toString(pattern.toArray()));
        
        if (choosePattern.equals(pattern)) {
//            Log.d(TAG, "pattern = "+pattern.toString());
//            Log.d(TAG, "pattern.size() = "+pattern.size());
            Log.d(TAG, "pattern = "+Arrays.toString(pattern.toArray()));
           
            confirm = true;
        } else {
            confirm = false;
        }
      
        step = STEP_4;
        updateView();

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
   		Log.v(ACTIVITY_NAME, "identifyFromWhichActivity");
   		if(null != intent.getExtras()){
   			Bundle extras = intent.getExtras();
   			if(null != extras.getString(MsgString.FROM_SETTING_OF_LOCK)){
   				from_setting_of_lock = true;
   				lastActivity = SettingOfLockActivity.class;
   				Log.v(ACTIVITY_NAME, MsgString.FROM_SETTING_OF_LOCK);
   			}
   			if(null != extras.getString(MsgString.FROM_ADD_ACCOUNT)){
   				from_add_account = true;
   				lastActivity = AddAccountActivity.class;
   				Log.v(ACTIVITY_NAME, MsgString.FROM_ADD_ACCOUNT);
   			}
   			if(null != extras.getString(MsgString.FROM_SHOW_ACCOUNT_LIST)){
   				from_show_account_list = true;
   				lastActivity = ShowAccountListActivity.class;
   				Log.v(ACTIVITY_NAME, MsgString.FROM_SHOW_ACCOUNT_LIST);
   			}if(null != extras.getString(MsgString.FROM_SAVE_ACCOUNT)){
   				from_save_account = true;
   				lastActivity = SaveAccountActivity.class;
   				Log.v(ACTIVITY_NAME, MsgString.FROM_SAVE_ACCOUNT);
   			}
   			if(null != extras.getString(MsgString.FROM_UNLOCK)){
   				from_unlock = true;
   				lastActivity = UnLockActivity.class;
   				Log.v(ACTIVITY_NAME, MsgString.FROM_UNLOCK);
   			}
   		}
   	}
 
}
