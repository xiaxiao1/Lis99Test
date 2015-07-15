package com.lis99.mobile.entry.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.entity.item.ComItem;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class DisAdapter<T> extends AdapterBase<T>{
	private ImageLoader imageLoader = ImageLoader.getInstance();
private ImageView iv_star11;
private ImageView iv_star12;
private ImageView iv_star13;
private ImageView iv_star14;
private ImageView iv_star15;
private TextView data_pinglun;
private TextView tv_user;
private TextView tv_time;
private ImageView iv_user;
	public DisAdapter(Activity act, List<T> list) {
		super(act, list);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected View getExView(int position, View convertView, ViewGroup parent,
			LayoutInflater inflater) {
		// TODO Auto-generated method stub
		convertView=inflater.inflate(R.layout.dis_list, null);
		iv_star11=(ImageView)convertView.findViewById(R.id.iv_star11);
		iv_star12=(ImageView)convertView.findViewById(R.id.iv_star12);
		iv_star13=(ImageView)convertView.findViewById(R.id.iv_star13);
		iv_star14=(ImageView)convertView.findViewById(R.id.iv_star14);
		iv_star15=(ImageView)convertView.findViewById(R.id.iv_star15);
		data_pinglun=(TextView)convertView.findViewById(R.id.data_pinglun);
		tv_user=(TextView)convertView.findViewById(R.id.tv_user);
		tv_time=(TextView)convertView.findViewById(R.id.tv_time);
		iv_user =(ImageView)convertView.findViewById(R.id.iv_user);
		ComItem comItem = (ComItem)getItem(position);
		float f = Float.parseFloat(comItem.getStar());
		if (f >= 3) {
			iv_star11
					.setImageResource(R.drawable.hwd_large_star_s);
			iv_star12
					.setImageResource(R.drawable.hwd_large_star_s);
			iv_star13
					.setImageResource(R.drawable.hwd_large_star_s);
			if (f == 3.5) {
				iv_star14
						.setImageResource(R.drawable.hwd_large_star_b);
			}
			if (f == 4) {
				iv_star14
						.setImageResource(R.drawable.hwd_large_star_s);
			}
			if (f == 4.5) {
				iv_star14
						.setImageResource(R.drawable.hwd_large_star_s);
				iv_star15
						.setImageResource(R.drawable.hwd_large_star_b);
			}
			if (f == 5) {
				iv_star14
						.setImageResource(R.drawable.hwd_large_star_s);
				iv_star15
						.setImageResource(R.drawable.hwd_large_star_s);
			}

		} else if (f >= 2) {
			iv_star11
					.setImageResource(R.drawable.hwd_large_star_s);
			iv_star12
					.setImageResource(R.drawable.hwd_large_star_s);
			if (f == 2.5) {
				iv_star13
						.setImageResource(R.drawable.hwd_large_star_b);
			} else if (f >= 1) {
				iv_star11
						.setImageResource(R.drawable.hwd_large_star_s);
				if (f == 1.5) {
					iv_star12
							.setImageResource(R.drawable.hwd_large_star_b);
				}
			} else if (f == 0.5) {
				iv_star11
						.setImageResource(R.drawable.hwd_large_star_b);
			}
		}
		data_pinglun.setText(comItem.getComment());
		tv_user.setText(comItem.getNickname());
		tv_time.setText(comItem.getCreatedate());
		if(comItem.getHeadicon()!=""){
		imageLoader.displayImage(comItem.getHeadicon(),
				iv_user);}
		
		return convertView;
	}

}
