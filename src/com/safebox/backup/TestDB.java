package com.safebox.backup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.safebox.DAO.AccountInfoDB;
import com.safebox.action.RegisterAction;
import com.safebox.action.ShowAccountListAction;
import com.safebox.activity.RegisterActivity;
import com.safebox.bean.UserProfile;
import com.safebox.msg.MsgString;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import intent.pack.R;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TestDB extends SQLiteOpenHelper {

    private static final String DB_NAME = "testdb";
    private static final String TB_NAME = "test_table";
    private static final String COLUMNS_ID = "id";
    private static final String COLUMNS_INDEX = "indexed";
    private static final String COLUMNS_NAME = "name";
    private static final String COLUMNS_CHECK = "checked";

    public TestDB(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Attention:注意SQL语法，每个变量后需要有空格，否则不认识。
        String sql = "CREATE TABLE IF NOT EXISTS " + TB_NAME + " (" + COLUMNS_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMNS_INDEX
                + " INTEGER," +COLUMNS_NAME+ " TEXT," +COLUMNS_CHECK
                + " BOOLEAN)";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TB_NAME);
        onCreate(db);
    }
    /**
     * 返回所有数据
     * @return
     */
    public Cursor selectAll(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TB_NAME, null, null, null,null, null, "id desc");
        return cursor;
    }
    /**
     * 根据条件查询
     * @param columnsName
     * @return
     */
    public Cursor selectForChecked(boolean isChecked){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] cloumns = {COLUMNS_INDEX,COLUMNS_NAME,COLUMNS_CHECK};
        String where = COLUMNS_CHECK+"=?";
        int check = 0;
        if(isChecked){
         check = 1;
        }
        String[] whereValue={Integer.toString(check)};
        Cursor cursor = db.query(TB_NAME, null, where, whereValue,null, null, "id desc");
//        db.query(true, TB_NAME, columns, selection, selectionArgs, groupBy, having, orderBy, limit)
        return cursor;
    }
    
    public long insert(int index,String name,boolean checked){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMNS_INDEX, index);
        cv.put(COLUMNS_NAME, name);
        cv.put(COLUMNS_CHECK, checked);
        long row = db.insert(TB_NAME, null, cv);
        db.close();
        return row;
        
    }
    
    public void delete(int index)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        String where=COLUMNS_INDEX+"=?";
        String[] whereValue={Integer.toString(index)};
        db.delete(TB_NAME, where, whereValue);
    }
    
    public void update(int index,boolean check)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        String where=COLUMNS_INDEX+"=?";
        String[] whereValue={Integer.toString(index)};
        ContentValues cv=new ContentValues(); 
        cv.put(COLUMNS_CHECK, check);
        db.update(TB_NAME, cv, where, whereValue);
    }

}
