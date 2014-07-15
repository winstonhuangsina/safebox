package com.safebox.backup;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import intent.pack.R;

import java.util.*;

import com.safebox.action.ShowAccountListAction;
import com.safebox.activity.SaveAccountActivity;

import android.widget.AdapterView.OnItemClickListener;

public class TestShowAccountListActivity extends ListActivity {
	private final static String ACCOUNT_NAME = "account_name";
	private final static String ACCOUNT_PASSWORD = "account_password";
	private static String IMG = "img";
	private ListView listView = null;
	String[] vData = null;

	// ListView listView = (ListView)findViewById(R.id.main_login_btn);

	// private List<String> data = new ArrayList<String>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// getAccountList();
		// listView = (ListView) findViewById(R.id.lvchild);
		// listView.setOnItemClickListener(itemListener);
		/*
		 * SimpleAdapter adapter = new SimpleAdapter(this, getAccountList(),
		 * R.layout.accountlist, new String[] { ACCOUNT_NAME, ACCOUNT_PASSWORD,
		 * IMG }, new int[] { R.id.account_name, R.id.account_password, R.id.img
		 * }); setListAdapter(adapter);
		 */

		// 要做为ArrayAdapter的资料来源
		vData = new String[] { "足球", "棒球", "篮球" };

		// 建立"阵列接收器"
		ArrayAdapter<String> arrayData = new ArrayAdapter<String>(this,
				R.layout.accountlist, vData);

		// 建立ListView 物件
		ListView lv = new ListView(this);

		// 设定ListView 的接收器, 做为选项的来源
		lv.setAdapter(arrayData);

		// ListView 设定Trigger
		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Log.v("----","");
				setTitle(getResources().getString(R.string.app_name) + ": "
						+ vData[arg2]);
			}
		});

		// 设定ListView 为ContentView
		setContentView(lv);

	}

	OnItemClickListener itemListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			Intent intent = new Intent();
			/*TextView account_name = (TextView) view
					.findViewById(R.id.account_name_list);
			toastShow("学号:" + account_name.getText().toString());*/
			// intent.putExtra("account_name", account_name_string);
			// intent.putExtra("account_password", account_password_string);
			intent.setClass(TestShowAccountListActivity.this,
					SaveAccountActivity.class);
			startActivity(intent);
			// 这里的view是我们在list.xml中定义的LinearLayout对象.
			// 所以可以通过findViewById方法可以找到list.xml中定义的它的子对象,如下:
			/*
			 * TextView stuId = (TextView) view.findViewById(R.id.idTo);
			 * TextView stuName = (TextView) view.findViewById(R.id.nameTo);
			 * TextView stuAge = (TextView) view.findViewById(R.id.ageTo);
			 * 
			 * toastShow("学号:" + stuId.getText().toString() + "; 姓名:" +
			 * stuName.getText().toString() + "; 年龄:" +
			 * stuAge.getText().toString());
			 */
		}
	};

	public void toastShow(String text) {
		Toast.makeText(TestShowAccountListActivity.this, text, 1000).show();
	}

	/*
	 * @Override public void onClick(View v) { int viewId = v.getId(); switch
	 * (viewId) {
	 * 
	 * 
	 * 
	 * 
	 * case R.id.login_reback_btn://返回按钮
	 * LoginActivity.this.finish();//关闭这个Activity 返回上一个Activity break; case
	 * R.id.login_login_btn://点击登录按钮 进行判断 用户名和密码是否为空 String userEditStr =
	 * userEdit.getText().toString().trim(); String passwdEditStr =
	 * passwdEdit.getText().toString().trim(); if(("".equals(userEditStr) ||
	 * null == userEditStr) || ("".equals(passwdEditStr) || null ==
	 * passwdEditStr)){//只要用户名和密码有一个为空 new
	 * AlertDialog.Builder(LoginActivity.this)
	 * .setIcon(getResources().getDrawable(R.drawable.login_error_icon))
	 * .setTitle("登录失败") .setMessage("微信账号或密码不能为空，请输入微信账号或密码") .create().show();
	 * } break; case R.id.forget_passwd://点击 “忘记密码” 这个文本
	 * forgetPasswdBtn.setTextColor(Color.RED);//文本变成红色 View view =
	 * LayoutInflater.from(LoginActivity.this).inflate(R.layout.login_dialog,
	 * null); popup = new PopupWindow(view,
	 * AbsListView.LayoutParams.FILL_PARENT,
	 * AbsListView.LayoutParams.WRAP_CONTENT);
	 * popup.showAsDropDown(forgetPasswdBtn); popup.setFocusable(false);
	 * popup.setOutsideTouchable(true); popup.showAtLocation(forgetPasswdBtn,
	 * Gravity.CENTER, 0, 0); popup.update();
	 * loginLayout.setBackgroundColor(Color.GRAY);
	 * forgetPasswdBtn.setBackgroundColor(Color.GRAY);
	 * forgetPasswdBtn.setEnabled(false); break; } }
	 */
	/*
	 * @Override public boolean onTouchEvent(MotionEvent event) { if(popup!=
	 * null && popup.isShowing()){ popup.dismiss();
	 * loginLayout.setBackgroundColor(Color.WHITE);
	 * forgetPasswdBtn.setBackgroundColor(Color.WHITE);
	 * forgetPasswdBtn.setEnabled(true); } return super.onTouchEvent(event); }
	 */
	// private List<Map<String, Object>> getData() {
	//
	//
	//
	// List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	//
	// Map<String, Object> map = new HashMap<String, Object>();
	// map.put(TITLE, "G1");
	// map.put("info", "google 1");
	// map.put("img", R.drawable.play);
	// list.add(map);
	//
	// map = new HashMap<String, Object>();
	// map.put(TITLE, "G2");
	// map.put("info", "google 2");
	// map.put("img", R.drawable.play);
	// list.add(map);
	//
	// map = new HashMap<String, Object>();
	// map.put(TITLE, "G3");
	// map.put("info", "google 3");
	// map.put("img", R.drawable.play);
	// list.add(map);
	// //getAccountList();
	// return list;
	// }

	private List<Map<String, Object>> getAccountList() {
		ShowAccountListAction showAccountList = new ShowAccountListAction(
				TestShowAccountListActivity.this);
		return showAccountList.query(0);
	}

}