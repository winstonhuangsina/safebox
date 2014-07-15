package com.safebox.msg;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;

public class ExitApp {
	
	/* �ڵ���exitDialog��activity��ʵ����δ��롣
	@Override  
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {  
	        ExitDialog(MainActivity.this).show();  
	        return true;  
	    }  
	      
	    return super.onKeyDown(keyCode, event);  
	}  */

	
	public void exitDialog(Context context, final MyApplication myApplication){
		AlertDialog.Builder alertbBuilder = new AlertDialog.Builder(context);
		alertbBuilder
				.setTitle("������������Title")
				.setMessage("������������Message")
				.setPositiveButton("ȷ��",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								myApplication.exit();
							}
						})
				.setNegativeButton("ȡ��",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						}).create();
		alertbBuilder.show();
	}
	
}
