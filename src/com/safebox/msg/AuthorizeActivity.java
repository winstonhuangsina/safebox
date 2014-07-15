package com.safebox.msg;
/*
 * Copyright 2011 Sina.
 *
 * Licensed under the Apache License and Weibo License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.open.weibo.com
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import intent.pack.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.weibo.net.AccessToken;
import com.weibo.net.DialogError;
import com.weibo.net.Weibo;
import com.weibo.net.WeiboDialogListener;
import com.weibo.net.WeiboException;

/**
 * Sample code for testing weibo APIs.
 * 
 * @author ZhangJie (zhangjie2@staff.sina.com.cn)
 */

public class AuthorizeActivity extends Activity {
	/** Called when the activity is first created. */
	private Button mLogin;
	private TextView mToken;

	private static final String URL_ACTIVITY_CALLBACK = "weiboandroidsdk://TimeLineActivity";
	private static final String FROM = "xweibo";
	
	// ����appkey��appsecret����λ�ȡ����΢��appkey��appsecret�������ѯ�����Ϣ���˴���������
	private static final String CONSUMER_KEY = "3252079783";// �滻Ϊ�����ߵ�appkey������"1646212960";
	private static final String CONSUMER_SECRET = "fdc92d92578e60471cb9f29cecd1c77d";// �滻Ϊ�����ߵ�appkey������"94098772160b6f8ffc1315374d8861f9";
	
	private String username = "";
	private String password = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.authorize_test);
		mToken = (TextView) this.findViewById(R.id.show_authorize_text);
		mLogin = (Button) this.findViewById(R.id.authorize_button);
		mLogin.setText("oauth!");
		mLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (v == mLogin) {
					Weibo weibo = Weibo.getInstance();
					weibo.setupConsumerConfig(CONSUMER_KEY, CONSUMER_SECRET);

					// Oauth2.0
					// ��ʽ��Ȩ��֤��ʽ
					weibo.setRedirectUrl("http://www.cetetek.com");// �˴��ص�ҳ����Ӧ���滻Ϊ��appkey��Ӧ��Ӧ�ûص�ҳ
					// ��Ӧ��Ӧ�ûص�ҳ���ڿ����ߵ�½����΢������ƽ̨֮��
					// �����ҵ�Ӧ��--Ӧ������--Ӧ����Ϣ--�߼���Ϣ--��Ȩ����--Ӧ�ûص�ҳ�������úͲ鿴��
					// Ӧ�ûص�ҳ����Ϊ��

					weibo.authorize(AuthorizeActivity.this,
							new AuthDialogListener());

					// try {
					// // Oauth2.0 ��֤��ʽ
					// Weibo.setSERVER("https://api.weibo.com/2/");
					// Oauth2AccessToken at =
					// weibo.getOauth2AccessToken(AuthorizeActivity.this,
					// Weibo.getAppKey(), Weibo.getAppSecret(), username,
					// password);
					// // xauth��֤��ʽ
					// /*
					// * Weibo.setSERVER("http://api.t.sina.com.cn/");
					// * AccessToken at =
					// * weibo.getXauthAccessToken(TextActivity.this,
					// * Weibo.APP_KEY, Weibo.APP_SECRET, "", "");
					// * mToken.setText(at.getToken());
					// */
					// RequestToken requestToken =
					// weibo.getRequestToken(AuthorizeActivity.this,
					// Weibo.getAppKey(), Weibo.getAppSecret(),
					// AuthorizeActivity.URL_ACTIVITY_CALLBACK);
					// mToken.setText(requestToken.getToken());
					// Uri uri =
					// Uri.parse(AuthorizeActivity.URL_ACTIVITY_CALLBACK);
					// startActivity(new Intent(Intent.ACTION_VIEW, uri));
					//
					// } catch (WeiboException e) {
					// e.printStackTrace();
					// } // mToken.setText(at.getToken());
					//

				}
			}
		});

	}

	public void onResume() {
		super.onResume();
	}

	class AuthDialogListener implements WeiboDialogListener {

		@Override
		public void onComplete(Bundle values) {
			String token = values.getString("access_token");
			String expires_in = values.getString("expires_in");
			String uid = values.getString("uid");
			String content = "access_token : " + token + "  expires_in: "
			+ expires_in + " uid: " + uid;
			
			mToken.setText(content);
			AccessToken accessToken = new AccessToken(token, CONSUMER_SECRET);
			accessToken.setExpiresIn(expires_in);
			Weibo.getInstance().setAccessToken(accessToken);
/*			Intent intent = new Intent();
			intent.setClass(AuthorizeActivity.this, AuthorizeActivity.class);
			intent.putExtra("access_token", token);
			intent.putExtra("uid", uid);
			startActivity(intent);*/
		}

		@Override
		public void onError(DialogError e) {
			Toast.makeText(getApplicationContext(),
					"Auth error : " + e.getMessage(), Toast.LENGTH_LONG).show();
		}

		@Override
		public void onCancel() {
			Toast.makeText(getApplicationContext(), "Auth cancel",
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(getApplicationContext(),
					"Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}

	}

}