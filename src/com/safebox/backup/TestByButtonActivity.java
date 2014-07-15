package com.safebox.backup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import intent.pack.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;



public class TestByButtonActivity extends Activity {
	ListView listview = null;
	SimpleAdapter adapter = null;
	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	ArrayList<String> list1 = new ArrayList<String>();
	private EditText userName, password;
	private Button login;
	private String userNameValue, passwordValue, result;
	String urlAddress = "http://10.0.2.2:8080/servertest/login.do";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		userName = (EditText) findViewById(R.id.login_username_input);
		password = (EditText) findViewById(R.id.login_password_input);
		login = (Button) findViewById(R.id.login_button);
		
		//result = HttpUtil.queryStringForPost("http://10.0.2.2:8080/servertest/loadMessage"); 
		
		
		login.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				userNameValue = userName.getText().toString();
				passwordValue = password.getText().toString();
				login(userNameValue, passwordValue);
				//toastShow(login(userNameValue, passwordValue));
			}
		});
	}

	private String login(String username, String password) {
		//String getUrl = urlAddress + "?username="+username+"&password="+password;
		HttpPost httpPost = new HttpPost(urlAddress);
		List params = new ArrayList();
		NameValuePair pair1 = new BasicNameValuePair("username", username);
		NameValuePair pair2 = new BasicNameValuePair("password", password);
		params.add(pair1);
		params.add(pair2);
		HttpEntity he;
		try {
			he = new UrlEncodedFormEntity(params, "gbk");
			httpPost.setEntity(he);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		
		
		HttpClient hc = new DefaultHttpClient();
		try {
			Log.v(" come to execute", username+password);
			HttpResponse ht = hc.execute(httpPost);
			if(ht.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				Log.v("the response code is ", ""+HttpStatus.SC_OK);
				HttpEntity het = ht.getEntity();
				InputStream is = het.getContent();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				String response = "";
				String readLine = null;
				while((readLine =br.readLine()) != null){
					//response = br.readLine();
					response = response + readLine;
					Log.v("the readLine string is ", readLine);
				}
				is.close();
				br.close();
				
				//String str = EntityUtils.toString(he);
				System.out.println("========="+response);
				return response;
			}else{
				return "error";
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "exception";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "exception";
		}
		
	
		
		
		/*
		// Ҫ���ʵ�HttpServlet
		String urlStr = "http://10.0.2.2:8080/servertest/login.do?";
		// Ҫ���ݵ�����
		String query = "username=" + username + "&password=" + password;
		urlStr += query;
		try {
			URL url = new URL(urlStr);
			// �������
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			Log.v("111", "111");
			if (true) {
				// ���������
				InputStream in = conn.getInputStream();
				Log.v("inputstream:", "");
				// ����һ�������ֽ���
				byte[] buffer = new byte[in.available()];
				// ���������ж�ȡ���ݲ���ŵ������ֽ�������
				in.read(buffer);
				Log.v("in.read:", "");
				// ���ֽ�ת�����ַ���
				String msg = new String(buffer);
				Log.v("��÷���:", msg);
				toastShow("��÷�����"+msg);
				in.close();// �ر�������
			} else {
				// ����͹ر�����
				conn.disconnect();
				Log.v("��÷���:", "");
				toastShow("����ʧ��");
			}
		} catch (Exception e) {
			toastShow("�쳣:"+e.getMessage());
		}*/
	}

	/*private void showDialog(String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(msg).setCancelable(false)
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int id) {
						// TODO Auto-generated method stub

					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}*/

	public void toastShow(String text) {
		Toast.makeText(this, text, 1000).show();
	}
}
