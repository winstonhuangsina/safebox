package com.safebox.network;

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
 
public class FileUploadActivity extends Activity {
    private String newName = "crash.log";
    private String fileName = "crash.log";
    private String filePath = "storage/sdcard/";
    private String uploadFile = filePath + fileName;
    MyApplication myApplication;
    private int userId = -1;
    DeviceInfo deviceInfo;
    //private String uploadFile = "storage/sdcard/crash-*.log";// Ҫ�ϴ����ļ�
    //private String actionUrl = "http://mysafeboxgit.duapp.com/UploadServlet";
    private String actionUrl = "http://10.0.2.2:8080/servertest/UploadServlet";
    
    private TextView mText1;
    private TextView mText2;
    private Button mButton;
    
    static final int SUCCESS = 1;
    static final int FAILURE = 0;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_file);
        initial();
        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	new Thread(queryRun).start();
            }
        });
    }
    
    
    public void initial(){
    	
    	myApplication = (MyApplication) this.getApplication();
    	userId = myApplication.getUserId();
        
        mText1 = (TextView) findViewById(R.id.myText2);
        // �ļ�·����
        mText1.setText(uploadFile);
        mText2 = (TextView) findViewById(R.id.myText3);
        // �ϴ���ַ��
        mText2.setText(actionUrl);
        /* ����mButton��onClick�¼����� */
        mButton = (Button) findViewById(R.id.myButton);
    }
 
    
    
    Runnable queryRun = new Runnable() {
		@Override
		public void run() {
			Looper.prepare();
			
			uploadFile(); 
			Looper.loop();
		}
	};
    
    /* �ϴ��ļ���Server�ķ��� */
    private void uploadFile() {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        try {
            URL url = new URL(actionUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            /* ����Input��Output����ʹ��Cache */
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
 
            // ����http��������
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
 
            // ȡ���ļ���FileInputStream
            FileInputStream fStream = new FileInputStream(uploadFile);
            /* ����ÿ��д��1024bytes */
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length = -1;
            /* ���ļ���ȡ������������ */
            while ((length = fStream.read(buffer)) != -1) {
                /* ������д��DataOutputStream�� */
                ds.write(buffer, 0, length);
            }
            ds.writeBytes(end);
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
 
            fStream.close();
            ds.flush();
            /* ȡ��Response���� */
            InputStream is = con.getInputStream();
            int ch;
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            System.out.println("b: " + b.toString());
            
    		handlerQuery.obtainMessage(SUCCESS, b.toString()).sendToTarget();
            /* ��Response��ʾ��Dialog */
            //showDialog("�ϴ��ɹ�" + b.toString().trim());
            /* �ر�DataOutputStream */
            ds.close();
        } catch (Exception e) {
        	e.printStackTrace();
            //showDialog("�ϴ�ʧ��" + e);
            handlerQuery.obtainMessage(FAILURE, e.toString()).sendToTarget();
        }
    }
 
    
    Handler handlerQuery = new Handler() {

		public void handleMessage(Message msg) {// �˷�����ui�߳�����
			switch (msg.what) {
			case SUCCESS:
				String success = (String) msg.obj;
				showDialog("�ϴ��ɹ�" + success.toString().trim());
				break;

			case FAILURE:
				String failure = (String) msg.obj;
				showDialog("�ϴ�ʧ��" + failure.toString().trim());
				break;
			}
		}
	};
    
    /* ��ʾDialog��method */
    private void showDialog(String mess) {
        new AlertDialog.Builder(FileUploadActivity.this).setTitle("Message")
                .setMessage(mess)
                .setNegativeButton("ȷ��", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }
}
