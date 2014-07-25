package com.safebox.network;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import intent.pack.R;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.safebox.msg.DeviceInfo;
import com.safebox.msg.HttpClientToServer;
import com.safebox.msg.MsgString;
import com.safebox.msg.MyApplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class UploadFile {
	 private String newName = "crash.log";
	    private String fileName = "crash.log";
	    private String filePath = "storage/sdcard/";
	    private String uploadFile = filePath + fileName;
	    MyApplication myApplication;
	    private int userId = -1;
	    DeviceInfo deviceInfo;
	    //private String uploadFile = "storage/sdcard/crash-*.log";// 要上传的文件
	    //private String actionUrl = "http://mysafeboxgit.duapp.com/UploadServlet";
	    private String actionUrl = MsgString.UPLOAD_FILE_URL;
	    
	    private TextView mText1;
	    private TextView mText2;
	    private Button mButton;
	    private Context context;
	    static final int SUCCESS = 1;
	    static final int FAILURE = 0;
	
	    public UploadFile(Context context){
	    	this.context = context;
	    }
	    
	    public void upload(){
	    	new Thread(queryRun).start();
	    }
	
	 Runnable queryRun = new Runnable() {
			@Override
			public void run() {
				Looper.prepare();
				File file = new File(uploadFile);
				if (file.exists()){
					uploadFile(); 
				}
				Looper.loop();
			}
		};
	    
	    /* 上传文件至Server的方法 */
	    private void uploadFile() {
	        String end = "\r\n";
	        String twoHyphens = "--";
	        String boundary = "*****";
	        try {
	            URL url = new URL(actionUrl);
	            HttpURLConnection con = (HttpURLConnection) url.openConnection();
	            /* 允许Input、Output，不使用Cache */
	            con.setDoInput(true);
	            con.setDoOutput(true);
	            con.setUseCaches(false);
	 
	            // 设置http连接属性
	            con.setRequestMethod("POST");
	            con.setRequestProperty("Connection", "Keep-Alive");
	            con.setRequestProperty("Charset", "UTF-8");
	            con.setRequestProperty("Content-Type",
	                    "multipart/form-data;boundary=" + boundary);
	 
	            DataOutputStream ds = new DataOutputStream(con.getOutputStream());
	            ds.writeBytes(twoHyphens + boundary + end);
	            ds.writeBytes("Content-Disposition: form-data; "
	                    + "name=\"" + "crash-userId-" + userId + "\";filename=\"" + newName + "\"" + end);
	            
	            
	            ds.writeBytes(end);
	 
	            // 取得文件的FileInputStream
	            FileInputStream fStream = new FileInputStream(uploadFile);
	            /* 设置每次写入1024bytes */
	            int bufferSize = 1024;
	            byte[] buffer = new byte[bufferSize];
	            int length = -1;
	            /* 从文件读取数据至缓冲区 */
	            while ((length = fStream.read(buffer)) != -1) {
	                /* 将资料写入DataOutputStream中 */
	                ds.write(buffer, 0, length);
	            }
	            ds.writeBytes(end);
	            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
	 
	            fStream.close();
	            ds.flush();
	            /* 取得Response内容 */
	            InputStream is = con.getInputStream();
	            int ch;
	            StringBuffer b = new StringBuffer();
	            while ((ch = is.read()) != -1) {
	                b.append((char) ch);
	            }
	            System.out.println("b: " + b.toString());
	    		handlerQuery.obtainMessage(SUCCESS, b.toString()).sendToTarget();
	            /* 将Response显示于Dialog */
	            //showDialog("上传成功" + b.toString().trim());
	            /* 关闭DataOutputStream */
	            ds.close();
	            
	            File file = new File(uploadFile);
				if (file.exists())
					file.delete();
	            
	        } catch (Exception e) {
	        	e.printStackTrace();
	            //showDialog("上传失败" + e);
	            handlerQuery.obtainMessage(FAILURE, e.toString()).sendToTarget();
	        }
	    }
	 
	    
	    Handler handlerQuery = new Handler() {

			public void handleMessage(Message msg) {// 此方法在ui线程运行
				switch (msg.what) {
				case SUCCESS:
					String success = (String) msg.obj;
					//showDialog("上传成功" + success.toString().trim());
					break;

				case FAILURE:
					String failure = (String) msg.obj;
					//showDialog("上传失败" + failure.toString().trim());
					break;
				}
			}
		};
	    
		
		/*
	     显示Dialog的method 
	    private void showDialog(String mess) {
	        new AlertDialog.Builder(context).setTitle("Message")
	                .setMessage(mess)
	                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
	                    public void onClick(DialogInterface dialog, int which) {
	                    }
	                }).show();
	    }*/
}
