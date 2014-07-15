package com.safebox.DAO;

import com.safebox.msg.EncryptService;
import com.safebox.msg.MsgString;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class UserInfoDB {

	private final static String USER_INFO_TABLE = MsgString.USER_INFO_TABLE;
	public final static String USER_ID = MsgString.USER_ID;
	public final static String USER_PASSWORD = MsgString.USER_PASSWORD;
	public final static String USER_NAME = MsgString.USER_NAME;

	private PublicSQLiteHelper dbHelper;
	EncryptService encryptService;
	
	public UserInfoDB(Context context) {
		//super(context, DATABASE_NAME, null, DATABASE_VERSION);
		dbHelper = new PublicSQLiteHelper(context);
		encryptService = new EncryptService();
	}

	public Cursor select() {
		Log.v("registerDB select", USER_INFO_TABLE);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db
				.query(USER_INFO_TABLE, null, null, null, null, null, null);
		return cursor;
	}

	public Cursor findBy(String username) {
		String encrypt_username = null;
		try {
			encrypt_username = encryptService.Encrypt(username.toString().trim());
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sql = "select * from " + USER_INFO_TABLE + " where user_name = ? ";
		Log.v("findby username = ", encrypt_username);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		return db.rawQuery(sql, new String[] { encrypt_username });
	}
	
	public Cursor findBy(String username, String user_password) {
		Log.v("findby not yet encrypt_username = ", username);
		String encrypt_username = null, encrypt_password = null;
		try {
			encrypt_username = encryptService.Encrypt(username.toString().trim());
			encrypt_password = encryptService.Encrypt(user_password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String sql = "select * from " + USER_INFO_TABLE + " where user_name = ? and user_password = ? ";
		Log.v("findby encrypt_username = ", encrypt_username);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		return db.rawQuery(sql, new String[] { encrypt_username.toString().trim(), encrypt_password.toString().trim()});
	}

	// 增加操作
	public long insert(String username, String user_password) {
		
		String encrypt_username = null, encrypt_password = null;
		try {
			encrypt_username = encryptService.Encrypt(username);
			encrypt_password = encryptService.Encrypt(user_password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.v("registerDB insert", USER_INFO_TABLE);
		//toastShow(TABLE_NAME+"----insert-----"+username);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (!db.isOpen())
			Log.v("db is not open", USER_INFO_TABLE);
		/* ContentValues */
		ContentValues cv = new ContentValues();
		cv.put(USER_NAME, encrypt_username);
		cv.put(USER_PASSWORD, encrypt_password);
		long row = db.insert(USER_INFO_TABLE, null, cv);
		return row;
	}

	// 删除操作
	public void delete(int id) {
		Log.v("registerDB delete", USER_INFO_TABLE);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String where = USER_ID + " = ?";
		String[] whereValue = { Integer.toString(id) };
		db.delete(USER_INFO_TABLE, where, whereValue);
	}

	// 修改操作
	public void update(int id, String username, String user_password) {
		Log.v("registerDB update", USER_INFO_TABLE);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String where = USER_ID + " = ?";
		String[] whereValue = { Integer.toString(id) };

		ContentValues cv = new ContentValues();
		cv.put(USER_NAME, username);
		cv.put(USER_PASSWORD, user_password);
		db.update(USER_INFO_TABLE, cv, where, whereValue);
	}
}