package com.safebox.adapter;

import intent.pack.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import com.safebox.activity.ShowAccountListActivity;
import com.safebox.bean.AccountLayoutBean;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowAccountListAdapter extends BaseAdapter{
	private static final int TYPE_ITEM = 0;  
    private static final int TYPE_SEPARATOR = 1;  
    private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;  
	private Context mContext;
	
	 private ArrayList mData = new ArrayList();
	 private TreeSet<Integer> titlePositionSet = new TreeSet<Integer>();
	
	private LayoutInflater mLayoutInflater;
	
	private ArrayList<AccountLayoutBean> mItemDataList;
	
	private int mCurSelect = -1;
	
	private String privious_account_type = "empty";
	
	public ShowAccountListAdapter(Context context, ArrayList<AccountLayoutBean> itemDatalist)
	{
		mContext = context;
		
		mLayoutInflater = LayoutInflater.from(context);
		
		mItemDataList = itemDatalist;
	}
	
	
	@Override
	public int getCount() {
		if (mItemDataList == null)
		{
			return 0;
		}
		
		return mItemDataList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void setSelect(int pos)
	{
		if (pos >= 0 && pos < mItemDataList.size())
		{
			mCurSelect = pos;
			notifyDataSetChanged();
		}
			
	}

	/*@Override  
    public int getViewTypeCount() {  
        return TYPE_MAX_COUNT;  
    }  */
	
	/*@Override  
    public int getItemViewType(int position) {  
        return mSeparatorsSet.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;  
    } */ 
	
	/*public void addItem(final String item) {  
        mData.add(item);  
        notifyDataSetChanged();  
    }  

    public void addSeparatorItem(final String item) {  
        mData.add(item);  
        // save separator position  
        mSeparatorsSet.add(mData.size() - 1);  
        notifyDataSetChanged();  
    }  
	*/
	
	public void setTitlePosition(int position){
		titlePositionSet.add(position);
	}
	public TreeSet<Integer> getTitlePosition(){
		return titlePositionSet;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	
		if (null != mItemDataList){
			
			AccountLayoutBean item = mItemDataList.get(position);
			if(item.getAccount_type().equals("type")){
				setTitlePosition(position);
				convertView = mLayoutInflater.inflate(R.layout.accountlisttype, null);
				TextView accounttype = (TextView) convertView.findViewById(R.id.accounttype);
				accounttype.setText(item.getAccountInfo().getAccount_type());
				convertView.setClickable(false);
			}else{
				convertView = mLayoutInflater.inflate(R.layout.accountlist, null);
				ImageView imageView_icon = (ImageView) convertView.findViewById(R.id.img_list);
				ImageView lock_account_icon = (ImageView) convertView.findViewById(R.id.lock_account_icon);
				if(item.getAccountInfo().getIs_locked())
					lock_account_icon.setVisibility(android.view.View.VISIBLE);
				else{
					lock_account_icon.setVisibility(android.view.View.INVISIBLE);
				}
				TextView textView_site_name = (TextView) convertView.findViewById(R.id.site_name_list);
				//TextView textView_password = (TextView) convertView.findViewById(R.id.account_password_list);
				textView_site_name.setText(item.getAccountInfo().getSite_name());
			}
		}
		
		
		return convertView;
	}

}
