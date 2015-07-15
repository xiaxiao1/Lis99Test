package com.lis99.mobile.club.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lis99.mobile.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 			报名人信息列表
 * @author yy
 *
 */
public class LSClubapplyerInfoItemAdapter extends BaseAdapter{

	private ArrayList<HashMap<String, String>> list;
	private Context c;
	public LSClubapplyerInfoItemAdapter ( Context c, ArrayList<HashMap<String, String>> list )
	{
		this.c = c;
		this.list = list;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list == null ? null : list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder = null;
		if ( convertView == null )
		{
			holder = new Holder();
			convertView = View.inflate(c, R.layout.lsclub_apply_info_item, null);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.value = (TextView) convertView.findViewById(R.id.value);
			convertView.setTag(holder);
		}
		else
		{
			holder = (Holder) convertView.getTag();
		}
		
		HashMap<String, String> map = (HashMap<String, String>) getItem(position);
		
		if ( map == null ) return convertView;
		
		holder.name.setText(map.get("name"));
		holder.value.setText(map.get("value"));
		
		return convertView;
	}
	
	class Holder
	{
		TextView name, value;
	}

}
