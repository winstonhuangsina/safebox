package com.safebox.DAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.safebox.msg.EncryptService;
import com.safebox.msg.MsgString;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AccountInfoDB {

	private final static String TAG = "AccountInfoDB";
	
	
	private PublicSQLiteHelper dbHelper;
	EncryptService encryptService = new EncryptService();
	
	public AccountInfoDB(Context context) {
		// TODO Auto-generated constructor stub
		dbHelper = new PublicSQLiteHelper(context);
		
	}

	
	public List<Map<String, Object>> selectby(int user_id) {
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		String where = MsgString.USER_ID + " = ?";
		String[] whereValue = {"2"};
		//System.out.println("######## AccountInfoDB  selectBy() user id = " + user_id);
		//TODO ??
		//Cursor cs=db.rawQuery("select * from " + MsgString.ACCOUNT_INFO_TABLE + " where user_id =? " ,new String[]{Integer.toString(user_id)} );
		//Cursor cs=db.rawQuery("select * from account_info_table where user_id = ?", new String[]{"1"} );
		Cursor cs = db.query(MsgString.ACCOUNT_INFO_TABLE, null, null, null, null, null, MsgString.ACCOUNT_TYPE);
		//System.out.println("######## AccountInfoDB  selectBy() ##### getCount() = "+cs.getCount());
		String decrypt_username = null, decrypt_password = null;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		while (cs.moveToNext()){
			Map<String, Object> map = new HashMap<String, Object>();
			try {
				decrypt_username = encryptService.Decrypt(cs.getString(1).toString().trim());
				decrypt_password = encryptService.Decrypt(cs.getString(2).toString().trim());
			} catch (Exception e) {
				e.printStackTrace();
			}
			map.put(MsgString.ACCOUNT_ID, cs.getString(0));
			map.put(MsgString.SITE_NAME, cs.getString(4));
			map.put(MsgString.ACCOUNT_NAME, decrypt_username);
			map.put(MsgString.ACCOUNT_PASSWORD, decrypt_password);
			map.put(MsgString.ACCOUNT_TYPE, cs.getString(3));
			map.put(MsgString.IS_LOCKED, cs.getString(5));
			map.put(MsgString.USER_ID, cs.getString(6));
			Log.d(TAG, " MsgString.ACCOUNT_ID = "+cs.getString(0)+" MsgString.SITE_NAME = "+ cs.getString(4)+ " MsgString.ACCOUNT_NAME = " + decrypt_username + " IS_LOCKED = " +cs.getString(5));
			list.add(map);
		}
		cs.close();
		return list;
	}

	// 增加操作
	public long insert(String site_name, String account_name, String account_password, String account_type, boolean is_locked, int user_id) {
		String encrypt_account_name = null, encrypt_password = null;
		try {
			encrypt_account_name = encryptService.Encrypt(account_name.toString().trim());
			encrypt_password = encryptService.Encrypt(account_password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (!db.isOpen())
			Log.v("db is not open", MsgString.ACCOUNT_INFO_TABLE);
		/* ContentValues */
		ContentValues cv = new ContentValues();
		cv.put(MsgString.ACCOUNT_NAME, encrypt_account_name);
		cv.put(MsgString.ACCOUNT_PASSWORD, encrypt_password);
		cv.put(MsgString.ACCOUNT_TYPE, account_type);
		cv.put(MsgString.SITE_NAME, site_name);
		cv.put(MsgString.IS_LOCKED, is_locked);
		cv.put(MsgString.USER_ID, user_id);
		long row = db.insert(MsgString.ACCOUNT_INFO_TABLE, null, cv);
		if(db.isOpen())
			db.close();
		return row;
	}

	// 删除操作
	public void delete(String id) {
		Log.v("AccountInfoDB delete", MsgString.ACCOUNT_INFO_TABLE);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String where = MsgString.ACCOUNT_ID + " = ?";
		String[] whereValue = { id };
		db.delete(MsgString.ACCOUNT_INFO_TABLE, where, whereValue);
		if(db.isOpen())
			db.close();
	}

	// 修改操作
	public void update(int id, String site_name, String account_name, String account_password, String account_type, boolean is_locked) {
		String encrypt_account_name = null, encrypt_password = null;
		try {
			encrypt_account_name = encryptService.Encrypt(account_name.toString().trim());
			encrypt_password = encryptService.Encrypt(account_password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String where = MsgString.ACCOUNT_ID + " = ?";
		String[] whereValue = { Integer.toString(id) };

		ContentValues cv = new ContentValues();
		cv.put(MsgString.ACCOUNT_NAME, encrypt_account_name);
		cv.put(MsgString.ACCOUNT_PASSWORD, encrypt_password);
		cv.put(MsgString.ACCOUNT_TYPE, account_type);
		cv.put(MsgString.SITE_NAME, site_name);
		cv.put(MsgString.IS_LOCKED, is_locked);
		db.update(MsgString.ACCOUNT_INFO_TABLE, cv, where, whereValue);
		if(db.isOpen())
			db.close();
	}
	
	// clean all lock
	public void update(boolean set_unlock){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(MsgString.IS_LOCKED, false);
		db.update(MsgString.ACCOUNT_INFO_TABLE, cv, null, null);
		if(db.isOpen())
			db.close();
	}
	
	
	
}