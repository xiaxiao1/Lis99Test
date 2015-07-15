package com.lis99.mobile.entry.adapter;

import android.app.Activity;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.entity.item.CXinfo;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class DZCXAdapter<T> extends AdapterBase<T>{

private ImageLoader imageLoader = ImageLoader.getInstance();
	public DZCXAdapter(Activity act, List<T> list) {
		super(act, list);
		// TODO Auto-generated constructor stub
		
	}

	@Override
	protected View getExView(int position, View convertView, ViewGroup parent,
			LayoutInflater inflater) {
		// TODO Auto-generated method stub
		convertView =inflater.inflate(R.layout.dzcx_list,null);
		CXinfo CXinfo =(CXinfo)getItem(position);
		TextView textView1 =(TextView)convertView.findViewById(R.id.textView1);
		TextView tv_pr1 =(TextView)convertView.findViewById(R.id.tv_pr1);
		TextView tv_pr =(TextView)convertView.findViewById(R.id.tv_pr);
		ImageView iv_pr =(ImageView)convertView.findViewById(R.id.iv_pr);
		TextView tv_listdz=(TextView)convertView.findViewById(R.id.tv_listdz);
		tv_pr1.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );
		tv_pr1.setText("￥"+CXinfo.getMarketPrice().toString());
		tv_pr.setText("￥"+CXinfo.getSalePrice().toString());
		tv_listdz.setText(CXinfo.getDiscount().toString()+"折");
		textView1.setText(CXinfo.getTitle().toString());
		imageLoader.displayImage("http://i1.lis99.com"+CXinfo.getImage(), iv_pr);
		return convertView;
	}
	
}
