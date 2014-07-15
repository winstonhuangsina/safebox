package com.safebox.backup;

import intent.pack.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SQLiteDatabaseDemo extends Activity implements
		AdapterView.OnItemClickListener {
	private BooksDB mBooksDB;
	private Cursor mCursor;
	private EditText BookName;
	private EditText BookAuthor;
	private ListView BooksList;

	// private EditText userName, password;
	private Button registertest;
	TextView bookstring;
	private static final String TAG = "MyActivity";

	private int BOOK_ID = 0;
	protected final static int MENU_ADD = Menu.FIRST;
	protected final static int MENU_DELETE = Menu.FIRST + 1;
	protected final static int MENU_UPDATE = Menu.FIRST + 2;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tempdb);
		setUpViews();
	}

	public void setUpViews() {
		mBooksDB = new BooksDB(this);
		mCursor = mBooksDB.select();

		BookName = (EditText) findViewById(R.id.bookname);
		BookAuthor = (EditText) findViewById(R.id.author);
		BooksList = (ListView) findViewById(R.id.bookslist);

		// BooksList.setAdapter(new BooksListAdapter(this, mCursor));
		// BooksList.setOnItemClickListener(this);

		// userName = (EditText) findViewById(R.id.userName);
		// password = (EditText) findViewById(R.id.password);
		registertest = (Button) findViewById(R.id.registertest);
		registertest.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String bookName = BookName.getText().toString();
				String bookAuthor = BookAuthor.getText().toString();
				add();
				query();
				// To save username and password into the database.
				// save

				// pass context to the next activity.
				// Intent intent = new Intent();
				// intent.putExtra("userName", userNameString);
				// intent.putExtra("password", psdString);
				// intent.setClass(FirstActivity.this, SecondActivity.class);
				// startActivity(intent);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(Menu.NONE, MENU_ADD, 0, "ADD");
		menu.add(Menu.NONE, MENU_DELETE, 0, "DELETE");
		menu.add(Menu.NONE, MENU_DELETE, 0, "UPDATE");
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case MENU_ADD:
			add();
			break;
		case MENU_DELETE:
			delete();
			break;
		case MENU_UPDATE:
			update();
			break;
		}
		return true;
	}

	public void add() {
		String bookname = BookName.getText().toString();
		String author = BookAuthor.getText().toString();
		// 书名和作者都不能为空，或者退出
		if (bookname.equals("") || author.equals("")) {
			return;
		}
		mBooksDB.insert(bookname, author);
		mCursor.requery();
		BooksList.invalidateViews();
		BookName.setText("");
		BookAuthor.setText("");
		Toast.makeText(this, "Add Successed!", Toast.LENGTH_SHORT).show();
	}

	public void delete() {
		if (BOOK_ID == 0) {
			return;
		}
		mBooksDB.delete(BOOK_ID);
		mCursor.requery();
		BooksList.invalidateViews();
		BookName.setText("");
		BookAuthor.setText("");
		Toast.makeText(this, "Delete Successed!", Toast.LENGTH_SHORT).show();
	}

	public void update() {
		String bookname = BookName.getText().toString();
		String author = BookAuthor.getText().toString();
		// 书名和作者都不能为空，或者退出
		if (bookname.equals("") || author.equals("")) {
			return;
		}
		mBooksDB.update(BOOK_ID, bookname, author);
		mCursor.requery();
		BooksList.invalidateViews();
		BookName.setText("");
		BookAuthor.setText("");
		Toast.makeText(this, "Update Successed!", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		mCursor.moveToPosition(position);
		BOOK_ID = mCursor.getInt(0);
		BookName.setText(mCursor.getString(1));
		BookAuthor.setText(mCursor.getString(2));

	}

	public class BooksListAdapter extends BaseAdapter {
		private Context mContext;
		private Cursor mCursor;

		public BooksListAdapter(Context context, Cursor cursor) {

			mContext = context;
			mCursor = cursor;
		}

		@Override
		public int getCount() {
			return mCursor.getCount();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView mTextView = new TextView(mContext);
			mCursor.moveToPosition(position);
			mTextView.setText(mCursor.getString(1) + "___"
					+ mCursor.getString(2));
			return mTextView;
		}

	}

	public void query() {
		// SQLiteDatabase db = helper.getReadableDatabase();

		// 执行原始查询, 得到一个Cursor(类似ResultSet)
		Cursor c = mBooksDB.getWritableDatabase().rawQuery(
				"SELECT book_id, book_name, book_author FROM books_table", null);
		// Person p = null;
		String books = "";
		String[] bookArray;
		// 循环判断Cursor是否有下一条记录
		while (c.moveToNext()) {
			books = books + c.getString(1);
			// for (int i = 0; i < bookArray.length; i++) {
			// Log.v("--------111-----------------------", bookArray[i]);
			// }
			if (-1 != c.getColumnIndex("book_name"))
				Log.v("------c.getString(1)----------", c.getString(1));
			if (-1 != c.getColumnIndex("book_author"))
				Log.v("------c.getString(2)----------", c.getString(2));
			if (null != String.valueOf(c.getInt(0)).toString())
				Log.v("------c.getString(0)----------",
						String.valueOf(c.getInt(0)).toString());
			Log.v("--------books-----------------------", books);
			// Log.v("-------222-------------------------",
			// c.getString(2).toString());
		}

		// 释放资源
		c.close();
		mBooksDB.close();

		bookstring = (TextView) findViewById(R.id.bookstring);
		Log.v("--------------------------------", books);
		Log.v("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~", books.toString());
		bookstring.setText(books.toString());
	}

	public void cursorQuery() {
		Cursor cs = mBooksDB.select();
		while (cs.moveToNext()) {
			// cs.getI
		}

	}

}