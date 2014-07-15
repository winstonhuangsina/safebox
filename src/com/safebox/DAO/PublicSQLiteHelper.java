package com.safebox.DAO;

import com.safebox.msg.MsgString;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PublicSQLiteHelper extends SQLiteOpenHelper {
	private final static String DATABASE_NAME = MsgString.DATABASE_NAME;
	private final static int DATABASE_VERSION = MsgString.DATABASE_VERSION;

	// account info table
	private final static String ACCOUNT_INFO_TABLE = MsgString.ACCOUNT_INFO_TABLE;
	public final static String ACCOUNT_ID = MsgString.ACCOUNT_ID;
	public final static String SITE_NAME = MsgString.SITE_NAME;
	public final static String ACCOUNT_PASSWORD = MsgString.ACCOUNT_PASSWORD;
	public final static String ACCOUNT_NAME = MsgString.ACCOUNT_NAME;
	public final static String ACCOUNT_TYPE = MsgString.ACCOUNT_TYPE;
	
	// user info table
	private final static String USER_INFO_TABLE = MsgString.USER_INFO_TABLE;
	public final static String USER_ID = MsgString.USER_ID;
	public final static String USER_PASSWORD = MsgString.USER_PASSWORD;
	public final static String USER_NAME = MsgString.USER_NAME;
	public final static String IS_LOCKED = MsgString.IS_LOCKED;
	
	

	public PublicSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// ´´½¨table
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.v("AccountInfoDB oncreate", ACCOUNT_INFO_TABLE);
		String account_info = "CREATE TABLE " + ACCOUNT_INFO_TABLE + " (" + ACCOUNT_ID
				+ " INTEGER primary key autoincrement, " + ACCOUNT_NAME + " , "
				+ ACCOUNT_PASSWORD + " , "+ACCOUNT_TYPE + " , "+SITE_NAME +" , "+IS_LOCKED +" , "+ USER_ID +" );";
		db.execSQL(account_info);
		

		Log.v("registerDB oncreate", USER_INFO_TABLE);
		String user_info = "CREATE TABLE " + USER_INFO_TABLE + " (" + USER_ID
				+ " INTEGER primary key autoincrement, " + USER_NAME + ", "
				+ USER_PASSWORD + ");";
		db.execSQL(user_info);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("ALTER TABLE ACCOUNT_INFO_TABLE ADD IS_LOCKED varchar(20)");
		/*Log.v("AccountInfoDB on upgrade", TABLE_NAME);
		String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
		db.execSQL(sql);
		onCreate(db);*/
	}

	
}
