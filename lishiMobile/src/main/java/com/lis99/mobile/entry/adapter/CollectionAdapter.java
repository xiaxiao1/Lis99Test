package com.lis99.mobile.entry.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.entity.bean.LSCollection;
import com.lis99.mobile.entity.bean.LSCollectionItemInfo;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class CollectionAdapter extends ListAdapter<LSCollection> {

	DisplayImageOptions headerOptions;
	DisplayImageOptions goodsOptions;

	private void buildOptions() {
		headerOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.collection_list_default)
				.showImageForEmptyUri(R.drawable.collection_list_default)
				.showImageOnFail(R.drawable.collection_list_default)
				.cacheInMemory(false).cacheOnDisk(true).build();

		goodsOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.collection_goods_default)
				.showImageForEmptyUri(R.drawable.collection_goods_default)
				.showImageOnFail(R.drawable.collection_goods_default)
				.cacheInMemory(false).cacheOnDisk(true).build();
	}

	public CollectionAdapter(Context context, List<LSCollection> dataList) {
		super(context, dataList);
		buildOptions();
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		LSCollection collection = (LSCollection) getItem(position);
		return collection.getShop_info().getCount() == 0 ? 0 : 1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		LSCollection collection = (LSCollection) getItem(position);
		int type = getItemViewType(position);
		if (type == 0) {
			if (convertView == null) {
				convertView = layoutInflater.inflate(R.layout.collection_nodynamic_item, null);
				holder = new ViewHolder();
				holder.headerView = (ImageView) convertView.findViewById(R.id.headerView);
				holder.nameView = (TextView) convertView.findViewById(R.id.nameView);
				//holder.infoView = (TextView) convertView.findViewById(R.id.infoView);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			ImageLoader.getInstance().displayImage(collection.getShop_info().getThumb(), holder.headerView, headerOptions);
			holder.nameView.setText(collection.getShop_info().getTitle());
		} else {
			if (convertView == null) {
				convertView = layoutInflater.inflate(R.layout.collection_item, null);
				holder = new ViewHolder();
				holder.headerView = (ImageView) convertView.findViewById(R.id.headerView);
				holder.nameView = (TextView) convertView.findViewById(R.id.nameView);
				holder.infoView = (TextView) convertView.findViewById(R.id.infoView);
				holder.timeView = (TextView) convertView.findViewById(R.id.timeView);
				
				holder.goodsImageView1 = (ImageView) convertView.findViewById(R.id.goodsImageView1);
				holder.saleView1 = (TextView) convertView.findViewById(R.id.saleView1);
				holder.goodsPriceView1 = (TextView) convertView.findViewById(R.id.goodsPriceView1);
				
				holder.goodsImageView2 = (ImageView) convertView.findViewById(R.id.goodsImageView2);
				holder.saleView2 = (TextView) convertView.findViewById(R.id.saleView2);
				holder.goodsPriceView2 = (TextView) convertView.findViewById(R.id.goodsPriceView2);
				
				holder.goodsImageView3 = (ImageView) convertView.findViewById(R.id.goodsImageView3);
				holder.saleView3 = (TextView) convertView.findViewById(R.id.saleView3);
				holder.goodsPriceView3 = (TextView) convertView.findViewById(R.id.goodsPriceView3);
				
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			ImageLoader.getInstance().displayImage(collection.getShop_info().getThumb(), holder.headerView, headerOptions);
			holder.nameView.setText(collection.getShop_info().getTitle());
			holder.infoView.setText("新上了" + collection.getShop_info().getCount() + "个商品");
			
			List<LSCollectionItemInfo> items = collection.getSale_item();
			int count = items == null ? 0 : items.size();
			if (count > 3) {
				count = 3;
			}
			
			if (count == 1) {
				holder.goodsImageView1.setVisibility(View.VISIBLE);
				holder.goodsPriceView1.setVisibility(View.VISIBLE);
				
				if (items.get(0).getDiscount() > 0 && items.get(0).getDiscount() < 10) {
					holder.saleView1.setVisibility(View.VISIBLE);
				} else {
					holder.saleView1.setVisibility(View.GONE);
				}
				
				holder.goodsImageView2.setVisibility(View.GONE);
				holder.goodsPriceView2.setVisibility(View.GONE);
				holder.saleView2.setVisibility(View.GONE);
				
				holder.goodsImageView3.setVisibility(View.GONE);
				holder.goodsPriceView3.setVisibility(View.GONE);
				holder.saleView3.setVisibility(View.GONE);
			}
			
			if (count == 2) {
				holder.goodsImageView1.setVisibility(View.VISIBLE);
				holder.goodsPriceView1.setVisibility(View.VISIBLE);
				
				if (items.get(0).getDiscount() > 0 && items.get(0).getDiscount() < 10) {
					holder.saleView1.setVisibility(View.VISIBLE);
				} else {
					holder.saleView1.setVisibility(View.GONE);
				}
				
				holder.goodsImageView2.setVisibility(View.VISIBLE);
				holder.goodsPriceView2.setVisibility(View.VISIBLE);
				
				if (items.get(1).getDiscount() > 0 && items.get(1).getDiscount() < 10) {
					holder.saleView2.setVisibility(View.VISIBLE);
				} else {
					holder.saleView2.setVisibility(View.GONE);
				}
				
				holder.goodsImageView3.setVisibility(View.GONE);
				holder.goodsPriceView3.setVisibility(View.GONE);
				holder.saleView3.setVisibility(View.GONE);
			}
			
			if (count == 3) {
				holder.goodsImageView1.setVisibility(View.VISIBLE);
				holder.goodsPriceView1.setVisibility(View.VISIBLE);
				
				if (items.get(0).getDiscount() > 0 && items.get(0).getDiscount() < 10) {
					holder.saleView1.setVisibility(View.VISIBLE);
				} else {
					holder.saleView1.setVisibility(View.GONE);
				}
				
				holder.goodsImageView2.setVisibility(View.VISIBLE);
				holder.goodsPriceView2.setVisibility(View.VISIBLE);
				
				if (items.get(1).getDiscount() > 0 && items.get(1).getDiscount() < 10) {
					holder.saleView2.setVisibility(View.VISIBLE);
				} else {
					holder.saleView2.setVisibility(View.GONE);
				}
				
				holder.goodsImageView3.setVisibility(View.VISIBLE);
				holder.goodsPriceView3.setVisibility(View.VISIBLE);
				
				if (items.get(2).getDiscount() > 0 && items.get(2).getDiscount() < 10) {
					holder.saleView3.setVisibility(View.VISIBLE);
				} else {
					holder.saleView3.setVisibility(View.GONE);
				}
			}
			
			
			if (count >= 1) {
				LSCollectionItemInfo item = items.get(0);
				ImageLoader.getInstance().displayImage(item.getThumb(), holder.goodsImageView1, goodsOptions);
				holder.goodsPriceView1.setText("￥" + item.getSalePrice());
				
				if (item.getDiscount() > 0 && item.getDiscount() < 10) {
					holder.saleView1.setText(item.getDiscount() + "折");
				} 
			}
			
			if (count >= 2) {
				LSCollectionItemInfo item = items.get(1);
				ImageLoader.getInstance().displayImage(item.getThumb(), holder.goodsImageView2, goodsOptions);
				holder.goodsPriceView2.setText("￥" + item.getSalePrice());
				
				if (item.getDiscount() > 0 && item.getDiscount() < 10) {
					holder.saleView2.setText(item.getDiscount() + "折");
				} 
			}
			
			if (count >= 3) {
				LSCollectionItemInfo item = items.get(2);
				ImageLoader.getInstance().displayImage(item.getThumb(), holder.goodsImageView3, goodsOptions);
				holder.goodsPriceView3.setText("￥" + item.getSalePrice());
				
				if (item.getDiscount() > 0 && item.getDiscount() < 10) {
					holder.saleView3.setText(item.getDiscount() + "折");
				} 
			}
		}

		return convertView;
	}

	static class ViewHolder {
		ImageView headerView;
		TextView nameView;
		TextView infoView;
		TextView timeView;

		ImageView goodsImageView1;
		TextView saleView1;
		TextView goodsPriceView1;

		ImageView goodsImageView2;
		TextView saleView2;
		TextView goodsPriceView2;

		ImageView goodsImageView3;
		TextView saleView3;
		TextView goodsPriceView3;

	}

}
