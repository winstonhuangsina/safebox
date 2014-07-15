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
	// 这里是List Item内容，在这个例子中，将在后台任务中逐个加入
	private static String[] items = { "lorem", "ipsum", "dolor", "sit", "amet",
			"consectetuer", "adipiscing", "elit", "morbi", "vel", "ligula",
			"vitae", "arcu", "aliquet", "mollis", "etiam", "vel", "erat",
			"placerat", "ante", "porttitor", "sodales", "pellentesque",
			"augue", "purus" };

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.async_task_test);
		// 在这个例子中，我们一开始并没有导入items的数据，注意item数据为新建的ArrayList，即无内容
		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, new ArrayList<String>()));
		// 步骤5：创建后台任务的对象，并通过execute()启动后台线程，调用doInBackground()的代码，execute中的参数类型为参数1，这里我们不需要传递任何内容
		new AddStringTask().execute();
	}

	// 步骤1：创建AsyncTask子类,参数1是Void的范式类型，参数2是String的范式类型，参数3是Void
	// 。其中参数1：向后台任务的执行方法传递参数的类型；参数2：在后台任务执行过程中，要求主UI线程处理中间状态，通常是一些UI处理中传递的参数类型；参数3：后台任务执行完返回时的参数类型。
	private class AddStringTask extends AsyncTask<Void, String, Void> {
		// 我们加入一个检测信息的方法，打印当前在哪个线程执行的信息
		private void printInfo(String info) {
			Log.d("WEI", info + " : Tread is "
					+ Thread.currentThread().getName());
		}

		// 步骤2：实现抽象方法doInBackground()，代码将在后台线程中执行，由execute()触发，由于这个例子并不需要传递参数，使用Void...，具体书写方式为范式书写
		protected Void/* 参数3 */doInBackground(Void... params/* 参数1 */) {
			for (String item : items) {
				// 步骤3：通知UI主线程执行相关的操作（在onProgressUpdate中定义）
				publishProgress(item/* 参数2 */);
				printInfo("doInBackgound " + item);
				SystemClock.sleep(600);
			}
			return null;
		}

		// 步骤3：定义收到pushProgress()触发后，在UI主线程执行的内容，在本例，将item加入list中。方法中的参数为范式方式，实质为数组，由于我们只传递了item一个String，要获取，为values[0]
		@SuppressWarnings("unchecked")
		protected void onProgressUpdate(String... values/* 参数2 */) {
			printInfo("onProgressUpdate  get param " + values[0]);
			((ArrayAdapter<String>) getListAdapter()).add(values[0]);
		}

		// 步骤4：定义后台进程执行完后的处理，本例，采用Toast
		protected void onPostExecute(Void result/* 参数3 */) {
			printInfo("onPostExecute");
			Toast.makeText(AsyncTaskTestActivity.this, "Done!", Toast.LENGTH_SHORT)
					.show();
		}
	}
}
