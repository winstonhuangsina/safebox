package com.safebox.backup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.safebox.msg.MsgString;

import android.util.Log;

public class HttpClientToServer {
	
	
	//String urlAddress = "http://mysafeboxgit.duapp.com/login.do";
	private String urlAddress;
	
	List<BasicNameValuePair> listOfParams = new ArrayList<BasicNameValuePair>();
	public HttpClientToServer(){
		initialServerURL();
	}
	
	
	public HttpClientToServer(String username, String password, String action){
		initialServerURL();
		listOfParams.add(new BasicNameValuePair(MsgString.PARAMS_USERNAME, username));
		listOfParams.add(new BasicNameValuePair(MsgString.PARAMS_PASSWORD, password));
		listOfParams.add(new BasicNameValuePair(MsgString.PARAMS_ACTION, action));
	}
		
	private void initialServerURL(){
		if(MsgString.LOCAL_SERVER_URL_TRIGGER){
			urlAddress = MsgString.LOCAL_SERVER_URL;
		}else{
			urlAddress = MsgString.REMOTE_SERVER_URL;
		}
	}
	
	
	public String doGet(String username,String password){
		
		String getUrl = urlAddress + "?username="+username+"&password="+password;
		System.out.println("URL = "+getUrl);
		HttpGet httpGet = new HttpGet(getUrl);
		//HttpParams hp = httpGet.getParams();
		//hp.
		//httpGet.setp
		HttpClient hc = new DefaultHttpClient();
		try {
			HttpResponse ht = hc.execute(httpGet);
			if(ht.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				HttpEntity he = ht.getEntity();
				InputStream is = he.getContent();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				String response = "";
				String readLine = null;
				while((readLine =br.readLine()) != null){
					//response = br.readLine();
					response = response + readLine;
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
		
		
		
		
	}
	
	public String doPost(){
		//String getUrl = urlAddress + "?username="+username+"&password="+password;
		HttpPost httpPost = new HttpPost(urlAddress);
		List<BasicNameValuePair> params = listOfParams;
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
		
	}
	
	
}
