package com.safebox.backup;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseOpenHelper extends SQLiteOpenHelper {

	/*
	 * // ��û��ʵ����,�ǲ����������๹�����Ĳ���,��������Ϊ��̬ private static String dbname = "test2";
	 * private static int version = 3;
	 * 
	 * public DataBaseOpenHelper(Context context) { // ��һ��������Ӧ�õ������� //
	 * �ڶ���������Ӧ�õ����ݿ����� //
	 * ����������CursorFactoryָ����ִ�в�ѯʱ���һ���α�ʵ���Ĺ�����,����Ϊnull,����ʹ��ϵͳĬ�ϵĹ����� //
	 * ���ĸ����������ݿ�汾�������Ǵ���0��int�����Ǹ����� super(context, dbname, null, version);
	 * Log.v("---------create db ", dbname); // TODO Auto-generated constructor
	 * stub }
	 * 
	 * public DataBaseOpenHelper(Context context, String name, CursorFactory
	 * factory, int version) { super(context, name, factory, version); // TODO
	 * Auto-generated constructor stub }
	 * 
	 * @Override public void onCreate(SQLiteDatabase db) {
	 * Log.v("---------create table ", "person"); db.execSQL(
	 * "CREATE TABLE IF NOT EXISTS person (personid integer primary key autoincrement, name varchar(20), age INTEGER)"
	 * ); }
	 */

	// onUpgrade()���������ݿ�汾ÿ�η����仯ʱ������û��ֻ��ϵ����ݿ��ɾ����Ȼ�������´�����
	// һ����ʵ����Ŀ���ǲ����������ģ���ȷ���������ڸ������ݿ��ṹʱ����Ҫ�����û���������ݿ��е����ݲ��ᶪʧ,�Ӱ汾�����µ��汾����
	/*
	 * @Override public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
	 * db.execSQL("DROP TABLE IF EXISTS person"); onCreate(db); }
	 */

	// ��û��ʵ����,�ǲ����������๹�����Ĳ���,��������Ϊ��̬
	private static String dbname = "accountDB";
	private static int version = 2;
	//private String create_user_table = " person (personid integer primary key autoincrement, name varchar(20), age INTEGER)";
	private String user_table = "account (account_id integer primary key autoincrement, account_name varchar(15), account_password varchar(15))";
	private String table_name = "account "; // private String table_name;

	public DataBaseOpenHelper(Context context) { // ��һ��������Ӧ�õ������� //
		// �ڶ���������Ӧ�õ����ݿ����� //
		// ����������CursorFactoryָ����ִ�в�ѯʱ���һ���α�ʵ���Ĺ�����,����Ϊnull,����ʹ��ϵͳĬ�ϵĹ����� //
		// ���ĸ����������ݿ�汾�������Ǵ���0��int�����Ǹ�����
		super(context, dbname, null, version); 
		Log.v("---------create db ", dbname); //
		// TODO Auto-generated constructor stub
	}

	public DataBaseOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version); // TODO
		// Auto-generated constructor stub
	}

	public void setTableName(String table_name) {
		this.table_name = table_name;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String create_table = "CREATE TABLE IF NOT EXISTS " + user_table;
		Log.v("---------create table ", "");
		db.execSQL(create_table);
	}

	// onUpgrade()���������ݿ�汾ÿ�η����仯ʱ������û��ֻ��ϵ����ݿ��ɾ����Ȼ�������´����� //
	// һ����ʵ����Ŀ���ǲ����������ģ���ȷ���������ڸ������ݿ��ṹʱ����Ҫ�����û���������ݿ��е����ݲ��ᶪʧ,�Ӱ汾�����µ��汾����

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS " + table_name);
		onCreate(db);
	}

}
