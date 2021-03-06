package com.safebox.backup;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseOpenHelper extends SQLiteOpenHelper {

	/*
	 * // 类没有实例化,是不能用作父类构造器的参数,必须声明为静态 private static String dbname = "test2";
	 * private static int version = 3;
	 * 
	 * public DataBaseOpenHelper(Context context) { // 第一个参数是应用的上下文 //
	 * 第二个参数是应用的数据库名字 //
	 * 第三个参数CursorFactory指定在执行查询时获得一个游标实例的工厂类,设置为null,代表使用系统默认的工厂类 //
	 * 第四个参数是数据库版本，必须是大于0的int（即非负数） super(context, dbname, null, version);
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

	// onUpgrade()方法在数据库版本每次发生变化时都会把用户手机上的数据库表删除，然后再重新创建。
	// 一般在实际项目中是不能这样做的，正确的做法是在更新数据库表结构时，还要考虑用户存放于数据库中的数据不会丢失,从版本几更新到版本几。
	/*
	 * @Override public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
	 * db.execSQL("DROP TABLE IF EXISTS person"); onCreate(db); }
	 */

	// 类没有实例化,是不能用作父类构造器的参数,必须声明为静态
	private static String dbname = "accountDB";
	private static int version = 2;
	//private String create_user_table = " person (personid integer primary key autoincrement, name varchar(20), age INTEGER)";
	private String user_table = "account (account_id integer primary key autoincrement, account_name varchar(15), account_password varchar(15))";
	private String table_name = "account "; // private String table_name;

	public DataBaseOpenHelper(Context context) { // 第一个参数是应用的上下文 //
		// 第二个参数是应用的数据库名字 //
		// 第三个参数CursorFactory指定在执行查询时获得一个游标实例的工厂类,设置为null,代表使用系统默认的工厂类 //
		// 第四个参数是数据库版本，必须是大于0的int（即非负数）
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

	// onUpgrade()方法在数据库版本每次发生变化时都会把用户手机上的数据库表删除，然后再重新创建。 //
	// 一般在实际项目中是不能这样做的，正确的做法是在更新数据库表结构时，还要考虑用户存放于数据库中的数据不会丢失,从版本几更新到版本几。

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS " + table_name);
		onCreate(db);
	}

}
