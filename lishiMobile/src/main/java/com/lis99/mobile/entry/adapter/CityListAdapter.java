package com.lis99.mobile.entry.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.entity.item.Hotcitys;

import java.util.List;

public class CityListAdapter<T> extends AdapterBase<T>{
private TextView textView1;
	public CityListAdapter(Activity act, List<T> list) {
		super(act, list);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected View getExView(int position, View convertView, ViewGroup parent,
			LayoutInflater inflater) {
		// TODO Auto-generated method stub
		convertView=inflater.inflate(R.layout.ctlist, null);
		Hotcitys hotcitys =(Hotcitys)getItem(position);
		textView1=(TextView)convertView.findViewById(R.id.textView1);
		textView1.setText(hotcitys.getName());
		return convertView;
	}

}
