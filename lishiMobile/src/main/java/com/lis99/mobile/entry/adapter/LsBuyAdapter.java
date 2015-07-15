package com.lis99.mobile.entry.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.entity.item.Shop;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.List;

public class LsBuyAdapter<T> extends AdapterBase<T> {
	
	private ImageLoader imageLoader = ImageLoader.getInstance();
	
	protected DisplayImageOptions options;
	
	Activity act;

	public LsBuyAdapter(Activity act, List<T> list) {
		super(act, list);
		this.act = act;
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.shop_list_default)
		.showImageForEmptyUri(R.drawable.shop_list_default)
		.showImageOnFail(R.drawable.shop_list_default).cacheInMemory(false)
		.cacheOnDisk(true).displayer(new SimpleBitmapDisplayer())
		.build();
	}

	@Override
	protected View getExView(int position, View convertView, ViewGroup parent,
			LayoutInflater inflater) {
		convertView = inflater.inflate(R.layout.ls_buy_list, null);
		Shop shop = (Shop) getItem(position);
		ImageView iv_shop = (ImageView) convertView.findViewById(R.id.iv_shop);
		TextView tv_dp = (TextView) convertView.findViewById(R.id.tv_dp);
		
		if(shop.isDiscount()){
			Drawable drawable=  act.getResources().getDrawable(R.drawable.icon_discount);
			/// 这一步必须要做,否则不会显示.
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			tv_dp.setCompoundDrawables(null,null,drawable,null);
		}else{
			tv_dp.setCompoundDrawables(null,null,null,null);
		}
		
		TextView tv_dz = (TextView) convertView.findViewById(R.id.tv_dz);
		TextView tv_dis = (TextView) convertView.findViewById(R.id.tv_dis);
		ImageView iv_star1 = (ImageView) convertView
				.findViewById(R.id.iv_star1);
		ImageView iv_star2 = (ImageView) convertView
				.findViewById(R.id.iv_star2);
		ImageView iv_star3 = (ImageView) convertView
				.findViewById(R.id.iv_star3);
		ImageView iv_star4 = (ImageView) convertView
				.findViewById(R.id.iv_star4);
		ImageView iv_star5 = (ImageView) convertView
				.findViewById(R.id.iv_star5);
		if (shop != null) {
			tv_dp.setText(shop.getTitle());
			tv_dz.setText(shop.getAddress());
			int i = (int) (Double.parseDouble(shop.getDistance() == null ? "0" : shop.getDistance()));
			if (i < 1000) {
				tv_dis.setText(i + "m");
			} else {
				tv_dis.setText(i / 1000 + "km");
			}
			imageLoader.displayImage(shop.getImg(), iv_shop, options);
			String s = (String) shop.getStar();
			float f = Float.parseFloat(s);
			if (f >= 3) {
				iv_star1.setImageResource(R.drawable.hwd_large_star_s);
				iv_star2.setImageResource(R.drawable.hwd_large_star_s);
				iv_star3.setImageResource(R.drawable.hwd_large_star_s);
				if (f == 3.5) {
					iv_star4.setImageResource(R.drawable.hwd_large_star_b);
				}
				if (f == 4) {
					iv_star4.setImageResource(R.drawable.hwd_large_star_s);
				}
				if (f == 4.5) {
					iv_star4.setImageResource(R.drawable.hwd_large_star_s);
					iv_star5.setImageResource(R.drawable.hwd_large_star_b);
				}
				if (f == 5) {
					iv_star4.setImageResource(R.drawable.hwd_large_star_s);
					iv_star5.setImageResource(R.drawable.hwd_large_star_s);
				}

			} else if (f >= 2) {
				iv_star1.setImageResource(R.drawable.hwd_large_star_s);
				iv_star2.setImageResource(R.drawable.hwd_large_star_s);
				if (f == 2.5) {
					iv_star3.setImageResource(R.drawable.hwd_large_star_b);
				} else if (f >= 1) {
					iv_star1.setImageResource(R.drawable.hwd_large_star_s);
					if (f == 1.5) {
						iv_star2.setImageResource(R.drawable.hwd_large_star_b);
					}
				} else if (f == 0.5) {
					iv_star1.setImageResource(R.drawable.hwd_large_star_b);
				}
			}
		}
		return convertView;
	}

}
