package com.safebox.msg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Properties;
import java.util.TreeSet;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

/**
 * UncaughtException������,��������Uncaught�쳣��ʱ��,�и��� ���ӹܳ���,����¼ ���ʹ��󱨸�.
 * 
 */
public class CrashHandler implements UncaughtExceptionHandler {
    /** �Ƿ�����־���,��Debug״̬�¿���, 
     * ��Release״̬�¹ر�����ʾ�������� 
     * */ 
    public static final boolean DEBUG = true;
	protected static final String TAG = "CrashHandler";  
    /** ϵͳĬ�ϵ�UncaughtException������ */ 
    private Thread.UncaughtExceptionHandler mDefaultHandler;  
    /** CrashHandlerʵ�� */ 
    private static CrashHandler INSTANCE;  
    /** �����Context���� */ 
//    private Context mContext;  
    /** ��ֻ֤��һ��CrashHandlerʵ�� */ 
    private CrashHandler() {}  
    /** ��ȡCrashHandlerʵ�� ,����ģʽ*/ 
    public static CrashHandler getInstance() {  
        if (INSTANCE == null) {  
            INSTANCE = new CrashHandler();  
        }  
        return INSTANCE;  
    }  
   
    /** 
     * ��ʼ��,ע��Context����, 
     * ��ȡϵͳĬ�ϵ�UncaughtException������, 
     * ���ø�CrashHandlerΪ�����Ĭ�ϴ����� 
     *  
     * @param ctx 
     */ 
    public void init(Context ctx) {  
//        mContext = ctx;  
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();  
        Thread.setDefaultUncaughtExceptionHandler(this);  
    }  
   
    /** 
     * ��UncaughtException����ʱ��ת��ú��������� 
     */ 
    @Override 
    public void uncaughtException(Thread thread, Throwable ex) {  
        if (!handleException(ex) && mDefaultHandler != null) {  
            //����û�û�д�������ϵͳĬ�ϵ��쳣������������  
            mDefaultHandler.uncaughtException(thread, ex);  
        } else {  //����Լ��������쳣���򲻻ᵯ������Ի�������Ҫ�ֶ��˳�app
            try {  
                Thread.sleep(3000);  
            } catch (InterruptedException e) {  
            }  
            android.os.Process.killProcess(android.os.Process.myPid());  
            System.exit(10);  
        }  
    }  
   
    /** 
     * �Զ��������,�ռ�������Ϣ 
     * ���ʹ��󱨸�Ȳ������ڴ����. 
     * �����߿��Ը����Լ���������Զ����쳣�����߼� 
     * @return 
     * true��������쳣�������������쳣��
     * false����������쳣(���Խ���log��Ϣ�洢����)Ȼ�󽻸��ϲ�(����͵���ϵͳ���쳣����)ȥ����
     * ����˵����true���ᵯ���Ǹ�������ʾ��false�ͻᵯ��
     */ 
    private boolean handleException(final Throwable ex) {  
        if (ex == null) {  
            return false;  
        }  
//        final String msg = ex.getLocalizedMessage();  
        final StackTraceElement[] stack = ex.getStackTrace();
        final String message = ex.getMessage();
        //ʹ��Toast����ʾ�쳣��Ϣ  
        new Thread() {  
            @Override 
            public void run() {  
                Looper.prepare();  
//                Toast.makeText(mContext, "���������:" + message, Toast.LENGTH_LONG).show();  
//                ����ֻ����һ���ļ����Ժ�ȫ��������appendȻ���ͣ������ͻ����ظ�����Ϣ�����˲��Ƽ�
                String fileName = "crash-" + System.currentTimeMillis()  + ".log";  
                Log.v(TAG, Environment.getExternalStorageDirectory().toString());
                File file = new File(Environment.getExternalStorageDirectory(), fileName);
                try {
                    FileOutputStream fos = new FileOutputStream(file,true);
                    fos.write(message.getBytes());
                    for (int i = 0; i < stack.length; i++) {
                        fos.write(stack[i].toString().getBytes());
                    }
                    fos.flush();
                    fos.close();
                } catch (Exception e) {
                }
                Looper.loop();  
            }  
   
        }.start();  
        return false;  
    }  
   
    // TODO ʹ��HTTP Post ���ʹ��󱨸浽������  ���ﲻ��׸��
//    private void postReport(File file) {  
//      ���ϴ���ʱ�򻹿��Խ���app��version�����ֻ��Ļ��͵���Ϣһ�����͵ķ�������
//      Android�ļ�����������֪�����Կ��ܴ�����ÿ���ֻ����ᱨ������������Ե�ȥdebug�ȽϺ�
//    } 
	
	
	}
