package com.safebox.action;

import intent.pack.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.safebox.DAO.AccountInfoDB;
import com.safebox.backup.TestShowAccountListActivity;
import com.safebox.msg.MsgString;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class ShowAccountListAction {

	AccountInfoDB accountInfoDB;
	private final static String ACCOUNT_NAME = MsgString.ACCOUNT_NAME, ACCOUNT_ID = MsgString.ACCOUNT_ID;
	private final static String ACCOUNT_PASSWORD = MsgString.ACCOUNT_PASSWORD, ACCOUNT_TYPE = MsgString.ACCOUNT_TYPE;
	private final static String SITE_NAME = MsgString.SITE_NAME;
	private Context con;
	public ShowAccountListAction(Context context) {
		con = context;
		accountInfoDB = new AccountInfoDB(con);
	}

	String result = "";

	public List<Map<String, Object>> query(int user_id) {
		
		return accountInfoDB.selectby(user_id);
	}
}
