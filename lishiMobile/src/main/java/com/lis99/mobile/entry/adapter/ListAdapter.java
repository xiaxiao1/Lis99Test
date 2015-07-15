package com.lis99.mobile.entry.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class ListAdapter<T> extends BaseAdapter {
	
	
	protected List<T> dataList;
	protected Context context;
	protected LayoutInflater layoutInflater;
	
	public ListAdapter(Context context, List<T> dataList){
		this.context = context;
		this.dataList = dataList;
		this.layoutInflater = LayoutInflater.from(context);
	}
	

	@Override
	public int getCount() {
		return dataList == null ? 0 : dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataList == null ? null : dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}

}
