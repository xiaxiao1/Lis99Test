package com.lis99.mobile.entry.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lis99.mobile.R;

import java.util.List;

public class DPAdapter<T> extends AdapterBase<T> {
	
	public String selectTitle;

	public DPAdapter(Activity act, List<T> list) {
		super(act, list);
	}
	
	private boolean equalString(String s1, String s2){
		if(s1 == null){
			return s2 == null;
		}
		return s1.equals(s2);
	}

	@Override
	protected View getExView(int position, View convertView, ViewGroup parent,
			LayoutInflater inflater) {
		convertView =inflater.inflate(R.layout.dianpu_list, null);
		String string = (String) getItem(position);
		TextView textView = (TextView)convertView.findViewById(R.id.tv_dianpu);
		if(equalString(string,selectTitle)){
			textView.setTextColor(Color.rgb(16, 199, 195));
		}else{
			textView.setTextColor(Color.BLACK);
		}
		textView.setText(string);
		return convertView;
	}

}
