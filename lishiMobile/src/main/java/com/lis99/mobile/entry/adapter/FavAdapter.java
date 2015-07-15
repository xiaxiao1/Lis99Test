package com.lis99.mobile.entry.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.entity.item.FavItem;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;


public class FavAdapter<T> extends AdapterBase<T>{
private ImageLoader imageLoader = ImageLoader.getInstance();
	public FavAdapter(Activity act, List<T> list) {
		super(act, list);
		
		// TODO Auto-generated constructor stub
	}

	@Override
	protected View getExView(int position, View convertView, ViewGroup parent,
			LayoutInflater inflater) {
		// TODO Auto-generated method stub
		convertView = inflater.inflate(R.layout.ls_buy_list, null);
		FavItem favItem = (FavItem)getItem(position);
		ImageView iv_shop =(ImageView)convertView.findViewById(R.id.iv_shop);
		TextView tv_dp =(TextView)convertView.findViewById(R.id.tv_dp);
		TextView tv_dz =(TextView)convertView.findViewById(R.id.tv_dz);
		TextView tv_dis =(TextView)convertView.findViewById(R.id.tv_dis);
		ImageView iv_star1=(ImageView)convertView.findViewById(R.id.iv_star1);
		ImageView iv_star2=(ImageView)convertView.findViewById(R.id.iv_star2);
		ImageView iv_star3=(ImageView)convertView.findViewById(R.id.iv_star3);
		ImageView iv_star4=(ImageView)convertView.findViewById(R.id.iv_star4);
		ImageView iv_star5=(ImageView)convertView.findViewById(R.id.iv_star5);
		if(favItem!=null){
			tv_dp.setText(favItem.getTitle());
			tv_dz.setText(favItem.getAddress());
			int i=(int)(Double.parseDouble(favItem.getDistance()));
			if(i<1000){
				tv_dis.setText(i+"m");
			}else{
				tv_dis.setText(i/1000+"km");
			}			
			imageLoader.displayImage(favItem.getImg(), iv_shop);
			String s =(String)favItem.getStar();
			float f =Float.parseFloat(s);
			if(f>=3){
				iv_star1.setImageResource(R.drawable.hwd_large_star_s);
				iv_star2.setImageResource(R.drawable.hwd_large_star_s);
				iv_star3.setImageResource(R.drawable.hwd_large_star_s);
				if(f==3.5){
					iv_star4.setImageResource(R.drawable.hwd_large_star_b);
				}
				if(f==4){
					iv_star4.setImageResource(R.drawable.hwd_large_star_s);
				}
				if(f==4.5){
					iv_star4.setImageResource(R.drawable.hwd_large_star_s);
					iv_star5.setImageResource(R.drawable.hwd_large_star_b);
				}
				if(f==5){
					iv_star4.setImageResource(R.drawable.hwd_large_star_s);
					iv_star5.setImageResource(R.drawable.hwd_large_star_s);
				}
				
			}else if(f>=2){
				iv_star1.setImageResource(R.drawable.hwd_large_star_s);
				iv_star2.setImageResource(R.drawable.hwd_large_star_s);
				if(f==2.5){
					iv_star3.setImageResource(R.drawable.hwd_large_star_b);
				}else if(f>=1){
					iv_star1.setImageResource(R.drawable.hwd_large_star_s);
					if(f==1.5){
						iv_star2.setImageResource(R.drawable.hwd_large_star_b);
					}
				}else if(f==0.5){
					iv_star1.setImageResource(R.drawable.hwd_large_star_b);
				}
			}
		}
		return convertView;
	}

	
	

}
