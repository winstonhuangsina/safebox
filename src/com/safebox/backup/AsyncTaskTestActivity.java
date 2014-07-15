package com.safebox.backup;

import java.util.ArrayList;

import intent.pack.R;
import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class AsyncTaskTestActivity extends ListActivity {
	// ������List Item���ݣ�����������У����ں�̨�������������
	private static String[] items = { "lorem", "ipsum", "dolor", "sit", "amet",
			"consectetuer", "adipiscing", "elit", "morbi", "vel", "ligula",
			"vitae", "arcu", "aliquet", "mollis", "etiam", "vel", "erat",
			"placerat", "ante", "porttitor", "sodales", "pellentesque",
			"augue", "purus" };

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.async_task_test);
		// ����������У�����һ��ʼ��û�е���items�����ݣ�ע��item����Ϊ�½���ArrayList����������
		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, new ArrayList<String>()));
		// ����5��������̨����Ķ��󣬲�ͨ��execute()������̨�̣߳�����doInBackground()�Ĵ��룬execute�еĲ�������Ϊ����1���������ǲ���Ҫ�����κ�����
		new AddStringTask().execute();
	}

	// ����1������AsyncTask����,����1��Void�ķ�ʽ���ͣ�����2��String�ķ�ʽ���ͣ�����3��Void
	// �����в���1�����̨�����ִ�з������ݲ��������ͣ�����2���ں�̨����ִ�й����У�Ҫ����UI�̴߳����м�״̬��ͨ����һЩUI�����д��ݵĲ������ͣ�����3����̨����ִ���귵��ʱ�Ĳ������͡�
	private class AddStringTask extends AsyncTask<Void, String, Void> {
		// ���Ǽ���һ�������Ϣ�ķ�������ӡ��ǰ���ĸ��߳�ִ�е���Ϣ
		private void printInfo(String info) {
			Log.d("WEI", info + " : Tread is "
					+ Thread.currentThread().getName());
		}

		// ����2��ʵ�ֳ��󷽷�doInBackground()�����뽫�ں�̨�߳���ִ�У���execute()����������������Ӳ�����Ҫ���ݲ�����ʹ��Void...��������д��ʽΪ��ʽ��д
		protected Void/* ����3 */doInBackground(Void... params/* ����1 */) {
			for (String item : items) {
				// ����3��֪ͨUI���߳�ִ����صĲ�������onProgressUpdate�ж��壩
				publishProgress(item/* ����2 */);
				printInfo("doInBackgound " + item);
				SystemClock.sleep(600);
			}
			return null;
		}

		// ����3�������յ�pushProgress()��������UI���߳�ִ�е����ݣ��ڱ�������item����list�С������еĲ���Ϊ��ʽ��ʽ��ʵ��Ϊ���飬��������ֻ������itemһ��String��Ҫ��ȡ��Ϊvalues[0]
		@SuppressWarnings("unchecked")
		protected void onProgressUpdate(String... values/* ����2 */) {
			printInfo("onProgressUpdate  get param " + values[0]);
			((ArrayAdapter<String>) getListAdapter()).add(values[0]);
		}

		// ����4�������̨����ִ�����Ĵ�������������Toast
		protected void onPostExecute(Void result/* ����3 */) {
			printInfo("onPostExecute");
			Toast.makeText(AsyncTaskTestActivity.this, "Done!", Toast.LENGTH_SHORT)
					.show();
		}
	}
}
