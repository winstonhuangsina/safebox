package com.safebox.msg;

import intent.pack.R;

import java.io.InputStream;
import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;

public class GetContacts {
	Context mContext = null;

    /**��ȡ��Phon���ֶ�**/
    private static final String[] PHONES_PROJECTION = new String[] {
	    Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID,Phone.CONTACT_ID };
   
    /**��ϵ����ʾ����**/
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;
    
    /**�绰����**/
    private static final int PHONES_NUMBER_INDEX = 1;
    
    /**ͷ��ID**/
    private static final int PHONES_PHOTO_ID_INDEX = 2;
   
    /**��ϵ�˵�ID**/
    private static final int PHONES_CONTACT_ID_INDEX = 3;
    

    /**��ϵ������**/
    private ArrayList<String> mContactsName = new ArrayList<String>();
    
    /**��ϵ��ͷ��**/
    private ArrayList<String> mContactsNumber = new ArrayList<String>();

    /**��ϵ��ͷ��**/
    private ArrayList<Bitmap> mContactsPhonto = new ArrayList<Bitmap>();
    
	public GetContacts(){
		getPhoneContacts();
	}

	  
	
	
	/**�õ��ֻ�ͨѶ¼��ϵ����Ϣ**/
	private void getPhoneContacts() {
		ContentResolver resolver = mContext.getContentResolver();

		// ��ȡ�ֻ���ϵ��
		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,
				PHONES_PROJECTION, null, null, null);

		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {

				// �õ��ֻ�����
				String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
				// ���ֻ�����Ϊ�յĻ���Ϊ���ֶ� ������ǰѭ��
				if (TextUtils.isEmpty(phoneNumber))
					continue;

				// �õ���ϵ������
				String contactName = phoneCursor
						.getString(PHONES_DISPLAY_NAME_INDEX);

				// �õ���ϵ��ID
				Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);

				// �õ���ϵ��ͷ��ID
				Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);

				// �õ���ϵ��ͷ��Bitamp
				Bitmap contactPhoto = null;

				// photoid ����0 ��ʾ��ϵ����ͷ�� ���û�и���������ͷ�������һ��Ĭ�ϵ�
				/*
				 * if(photoid > 0 ) { Uri uri
				 * =ContentUris.withAppendedId(ContactsContract
				 * .Contacts.CONTENT_URI,contactid); InputStream input =
				 * ContactsContract
				 * .Contacts.openContactPhotoInputStream(resolver, uri);
				 * contactPhoto = BitmapFactory.decodeStream(input); }else {
				 * contactPhoto = BitmapFactory.decodeResource(getResources(),
				 * R.drawable.locked); }
				 */

				mContactsName.add(contactName);
				mContactsNumber.add(phoneNumber);
				// mContactsPhonto.add(contactPhoto);
			}

			phoneCursor.close();
		}
	}
	
	
	
}
