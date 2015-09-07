package com.lis99.mobile.newhome;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lis99.mobile.LsBuyActivity;
import com.lis99.mobile.LsEquiCateActivity;
import com.lis99.mobile.R;
import com.lis99.mobile.ShopDetailActivity;
import com.lis99.mobile.club.LSClubTopicActivity;
import com.lis99.mobile.club.model.EquipAppraiseModel.Profilelist;
import com.lis99.mobile.club.model.EquipRecommendModel;
import com.lis99.mobile.club.model.EquipTypeModel;
import com.lis99.mobile.club.model.NearbyModel.Shoplist;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.constens;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class LSSelectAdapter extends BaseAdapter {
	
	public static interface OnSelectItemClickListener{
		public void onSelectItemClick(LSSelectContent content, LSSelectItem item);
	}
	
	class LSSelectItemClickLIstener implements OnClickListener{
		LSSelectContent content;
		LSSelectItem item;
		
		public LSSelectItemClickLIstener(LSSelectContent content, LSSelectItem item){
			this.content = content;
			this.item = item;
		}

		@Override
		public void onClick(View v) {
			if(onSelectItemListener != null){
				onSelectItemListener.onSelectItemClick(content, item);
			}
		}
	}
	
	
	private OnSelectItemClickListener onSelectItemListener;

	private Context c;
	

	List<LSSelectContent> contents;
	
	public List<LSSelectContent> getContents() {
		return contents;
	}

	public void setContents(List<LSSelectContent> contents) {
		this.contents = contents;
	}

	private final static int FOUR_TYPE = 0; 
	private final static int THREE_TYPE = 1;
	private final static int DISCOUNT_TYPE = 2;
	private final static int BANNER_TYPE = 3;
	//=====3.1.0=====
	public final static int EQUIP_TYEP = 4;
	//附近的店铺
	public final static int NEARBY_TYPE = 5;
	//装备评测
	public final static int EQUIP_APPRAISE_TYPE = 6;
	LayoutInflater inflater;
	
	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	DisplayImageOptions bannerOptions;
	
	private void buildOptions() {
		options = new DisplayImageOptions.Builder()
//				.showImageOnLoading(R.drawable.select_item_default)
//				.showImageForEmptyUri(R.drawable.select_item_default)
//				.showImageOnFail(R.drawable.select_item_default)
				.cacheInMemory(true)
				.cacheOnDisk(true) 
				.build();
		bannerOptions = new DisplayImageOptions.Builder()
//		.showImageOnLoading(R.drawable.select_banner_default)
//		.showImageForEmptyUri(R.drawable.select_banner_default)
//		.showImageOnFail(R.drawable.select_banner_default)
		.cacheInMemory(true)
		.cacheOnDisk(true) 
		.build();
	}
	
	public LSSelectAdapter(Context context, List<LSSelectContent> contents){
		this.contents = contents;
		inflater = LayoutInflater.from(context);
		c = context;
		buildOptions();
	}
	
	@Override
	public int getItemViewType(int position) {
		if ( position == 0 )
		{
			return EQUIP_TYEP;
		}
		
		LSSelectContent content = (LSSelectContent) getItem(position);
		
		if ( content.equipType == NEARBY_TYPE )
		{
			return NEARBY_TYPE;
		}
		else if ( content.equipType == EQUIP_APPRAISE_TYPE )
		{
			return EQUIP_APPRAISE_TYPE;
		}
		
		
		if(content.isBanner){
			return BANNER_TYPE;
		}
		switch (content.getTemplate_id()) {
		case 1:
		case 5:
			return FOUR_TYPE;
		case 2:
		case 4:
			return THREE_TYPE;
		default:
			return DISCOUNT_TYPE;
		}
    }

	@Override
    public int getViewTypeCount() {
        return 7;
    }

	@Override
	public int getCount() {
		return contents == null ? 0 : contents.size();
	}

	@Override
	public Object getItem(int position) {
		return contents.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		int type = getItemViewType(position);
		LSSelectContent content = (LSSelectContent) getItem(position);
		
		switch (type) {
		case FOUR_TYPE:
			return configureFourView(content, convertView, parent);
		case THREE_TYPE:
			return configureThreeView(content, convertView, parent, false);
		case DISCOUNT_TYPE:
			return configureThreeView(content, convertView, parent, true);
		//装备head
		case EQUIP_TYEP:
			return getEquipView(content, convertView);
		//附近的户外店
		case NEARBY_TYPE:
			return getNearbyView(content, convertView);
			//装备评测
		case EQUIP_APPRAISE_TYPE:
			return getEquipAppraiseView(content, convertView);
		default:
			return configureBannerView(content, convertView, parent);
		}
	}
	//装备
	private View getEquipView ( LSSelectContent info, View view )
	{
		EquipHolder holder = null;
		if ( view == null )
		{
			view = inflater.inflate(R.layout.equip_title_chose, null);
			holder = new EquipHolder();
			holder.iv_1 = (ImageView) view.findViewById(R.id.iv_1);
			holder.iv_2 = (ImageView) view.findViewById(R.id.iv_2);
			
			holder.tv_1 = (TextView) view.findViewById(R.id.tv_1);
			holder.tv_2 = (TextView) view.findViewById(R.id.tv_2);
			holder.tv_3 = (TextView) view.findViewById(R.id.tv_3);
			holder.tv_4 = (TextView) view.findViewById(R.id.tv_4);
			holder.tv_5 = (TextView) view.findViewById(R.id.tv_5);
			holder.tv_6 = (TextView) view.findViewById(R.id.tv_6);
			holder.tv_7 = (TextView) view.findViewById(R.id.tv_7);
			holder.tv_8 = (TextView) view.findViewById(R.id.tv_8);
			holder.tv_9 = (TextView) view.findViewById(R.id.tv_9);
			
			view.setTag(holder);
		}
		else
		{
			holder = (EquipHolder) view.getTag();
		}
		
		EquipTypeModel model = info.EquipTypeItem;
		EquipRecommendModel modelHot = info.RecommendItem;

		if ( modelHot == null || modelHot.zhuangbeilist == null || modelHot.zhuangbeilist.size() == 0 )
			return view;
		
		holder.tv_1.setText(modelHot.zhuangbeilist.get(0).catename);
		holder.tv_2.setText(modelHot.zhuangbeilist.get(1).catename);
		holder.tv_3.setText(modelHot.zhuangbeilist.get(2).catename);
		holder.tv_4.setText(modelHot.zhuangbeilist.get(3).catename);
		holder.tv_5.setText(modelHot.zhuangbeilist.get(4).catename);
//		if ( modelHot.zhuangbeilist.size() > 5 )
//		holder.tv_6.setText(modelHot.zhuangbeilist.get(5).catename);
		
		
		if ( model == null || model.accesslist == null || model.accesslist.size() == 0 )
			return view;
		
		holder.tv_7.setText(model.accesslist.get(0).title);
		holder.tv_8.setText(model.accesslist.get(1).title);
		holder.tv_9.setText(model.accesslist.get(2).title);
		
		holder.tv_1.setOnClickListener( new EquipClick(model, modelHot));
		holder.tv_2.setOnClickListener( new EquipClick(model, modelHot));
		holder.tv_3.setOnClickListener( new EquipClick(model, modelHot));
		holder.tv_4.setOnClickListener( new EquipClick(model, modelHot));
		holder.tv_5.setOnClickListener( new EquipClick(model, modelHot));
//		if ( modelHot.zhuangbeilist.size() > 5 )
		holder.tv_6.setOnClickListener( new EquipClick(model, modelHot));
		holder.tv_7.setOnClickListener( new EquipClick(model, modelHot));
		holder.tv_8.setOnClickListener( new EquipClick(model, modelHot));
		holder.tv_9.setOnClickListener( new EquipClick(model, modelHot));
		
		return view;
	}
	//附近店铺
	private View getNearbyView ( LSSelectContent info, View view )
	{
		NearbyHolder holder = null;
		if ( view == null )
		{
			view = inflater.inflate(R.layout.equip_nearby_shop, null);
			
			holder = new NearbyHolder();
			holder.iv_title = (ImageView) view.findViewById(R.id.iv_title);
			holder.layout_info = (RelativeLayout) view.findViewById(R.id.layout_info);
			holder.iv_icon = (RoundedImageView) view.findViewById(R.id.iv_icon);
			holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
			holder.tv_info = (TextView) view.findViewById(R.id.tv_info);
			holder.tv_distance = (TextView) view.findViewById(R.id.tv_distance);
			holder.tv_all = (TextView) view.findViewById(R.id.tv_all);
			holder.line_all = view.findViewById(R.id.line_all);
			holder.line_half = view.findViewById(R.id.line_half);
			holder.view_line = view.findViewById(R.id.view_line);
			holder.iv_load = (ImageView) view.findViewById(R.id.iv_load);
			view.setTag(holder);
		}
		else
		{
			holder = (NearbyHolder) view.getTag();
		}
		
		Shoplist item = info.NearbyItem;
		
		if ( item.isfirst )
		{
			holder.iv_title.setVisibility(View.VISIBLE);
			holder.view_line.setVisibility(View.VISIBLE);
		}
		else
		{
			holder.iv_title.setVisibility(View.GONE);
			holder.view_line.setVisibility(View.GONE);
		}
		
		if ( !item.isLast )
		{
			holder.layout_info.setVisibility(View.VISIBLE);
			holder.tv_name.setText(item.title);
			holder.tv_info.setText(item.address);
			holder.tv_distance.setText("距离我"+Common.float2km(item.distance));
			imageLoader.getInstance().displayImage(item.img, holder.iv_icon, ImageUtil.getclub_topic_imageOptions(), ImageUtil.getImageLoading(holder.iv_load, holder.iv_icon));
			holder.tv_all.setVisibility(View.GONE);
		}
		else
		{
			holder.layout_info.setVisibility(View.GONE);
			holder.tv_all.setVisibility(View.VISIBLE);
		}
		//横线
		if ( !item.line_all )
		{
			holder.line_half.setVisibility(View.VISIBLE);
			holder.line_all.setVisibility(View.GONE);
		}
		else
		{
			holder.line_half.setVisibility(View.GONE);
			holder.line_all.setVisibility(View.VISIBLE);
		}
		
		holder.layout_info.setOnClickListener(new NearbyClick(item));
		holder.tv_all.setOnClickListener(new NearbyClick(item));
		
		return view;
	}
	
	private View getEquipAppraiseView ( LSSelectContent info, View view )
	{
		EquipAppraiseHolder holder = null;
		if ( view == null )
		{
			holder = new EquipAppraiseHolder();
			view = inflater.inflate(R.layout.equip_appraise, null);
			
			holder.iv_title = (ImageView) view.findViewById(R.id.iv_title);
			holder.iv_icon1 = (RoundedImageView) view.findViewById(R.id.iv_icon1);
			holder.iv_icon2 = (RoundedImageView) view.findViewById(R.id.iv_icon2);
			holder.layout1 = (LinearLayout) view.findViewById(R.id.layout1);
			holder.layout2 = (LinearLayout) view.findViewById(R.id.layout2);
			holder.tv_title1 = (TextView) view.findViewById(R.id.tv_title1);
			holder.tv_title2 = (TextView) view.findViewById(R.id.tv_title2);
			holder.view_line = view.findViewById(R.id.view_line);
			holder.iv_load = (ImageView) view.findViewById(R.id.iv_load);
			holder.iv_load1 = (ImageView) view.findViewById(R.id.iv_load1);


			view.setTag(holder);
		}
		else
		{
			holder = (EquipAppraiseHolder) view.getTag();
		}
		
		ArrayList<Profilelist> item = info.AppraiseItem;
		
		holder.tv_title1.setText(item.get(0).title);
		ImageLoader.getInstance().displayImage(item.get(0).image, holder.iv_icon1, ImageUtil.getImageLoading(holder.iv_load, holder.iv_icon1));

		holder.layout1.setOnClickListener( new AppraiseClick(item.get(0)));
		
		if ( item.size() >= 1 )
		{
			holder.layout2.setVisibility(View.VISIBLE);
			holder.layout2.setOnClickListener(new AppraiseClick(item.get(1)));
			if ( item.get(0).first )
			{
				holder.iv_title.setVisibility(View.VISIBLE);
				holder.view_line.setVisibility(View.VISIBLE);
			}
			else
			{
				holder.iv_title.setVisibility(View.GONE);
				holder.view_line.setVisibility(View.GONE);
			}
			
		}
		if ( item.size() >= 2 )
		{
			holder.tv_title2.setText(item.get(1).title);
			ImageLoader.getInstance().displayImage(item.get(1).image, holder.iv_icon2, ImageUtil.getImageLoading(holder.iv_load1, holder.iv_icon2));
		}
		else
		{
			holder.layout2.setVisibility(View.GONE);
		}
		
		
		return view;
	}
	
	private View configureBannerView(LSSelectContent content,View convertView,ViewGroup parent){
		if(convertView == null){
			convertView = inflater.inflate(R.layout.select_banner_item, null);
		}
		
		ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView1);
		ImageView iv_load = (ImageView) convertView.findViewById(R.id.iv_load);
		imageLoader.displayImage(content.getBanner_image(), imageView,
				bannerOptions, ImageUtil.getImageLoading(iv_load, imageView));
		return convertView;
	}
	
	private View configureThreeView(LSSelectContent content,View convertView,ViewGroup parent,boolean discount){
		ViewHolder viewHolder = null;
		if(convertView == null){
			if(discount)
				convertView = inflater.inflate(R.layout.select_discount_item, null);
			else
				convertView = inflater.inflate(R.layout.select_three_item, null);
			
			viewHolder = new FourViewHolder();
			fillViewHolder(viewHolder, convertView);
			
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		List<LSSelectItem> items = content.getItems();
		if(items != null && items.size() == 3){
			setViewHolderContent(viewHolder,content,items);
		}
		
		return convertView;
	}
	
	private void fillViewHolder(ViewHolder viewHolder,View convertView){
		viewHolder.titleView = (TextView) convertView.findViewById(R.id.title);
		viewHolder.descView = (TextView) convertView.findViewById(R.id.desc);
		
		viewHolder.itemImageView1 = (ImageView) convertView.findViewById(R.id.imageView1);
		viewHolder.itemNameView1 = (TextView) convertView.findViewById(R.id.nameView1);
		viewHolder.itemDescView1 = (TextView) convertView.findViewById(R.id.descView1);
		
		viewHolder.itemImageView2 = (ImageView) convertView.findViewById(R.id.imageView2);
		viewHolder.itemNameView2 = (TextView) convertView.findViewById(R.id.nameView2);
		viewHolder.itemDescView2 = (TextView) convertView.findViewById(R.id.descView2);
		
		viewHolder.itemImageView3 = (ImageView) convertView.findViewById(R.id.imageView3);
		viewHolder.itemNameView3 = (TextView) convertView.findViewById(R.id.nameView3);
		viewHolder.itemDescView3 = (TextView) convertView.findViewById(R.id.descView3);
		
		viewHolder.itemPanel1 = convertView.findViewById(R.id.itemPanel1);
		viewHolder.itemPanel2 = convertView.findViewById(R.id.itemPanel2);
		viewHolder.itemPanel3 = convertView.findViewById(R.id.itemPanel3);

		viewHolder.iv_load = (ImageView) convertView.findViewById(R.id.iv_load);
		viewHolder.iv_load1 = (ImageView) convertView.findViewById(R.id.iv_load1);
		viewHolder.iv_load2 = (ImageView) convertView.findViewById(R.id.iv_load2);
		viewHolder.iv_load3 = (ImageView) convertView.findViewById(R.id.iv_load3);

	}
	
	private void setViewHolderContent(ViewHolder viewHolder,LSSelectContent content,List<LSSelectItem> items){
		viewHolder.titleView.setText(content.getTitle());
		viewHolder.descView.setText(content.getDescript());
		
		LSSelectItem item = items.get(0);
		imageLoader.displayImage(item.getImageUrl(), viewHolder.itemImageView1,
				options, ImageUtil.getImageLoading(viewHolder.iv_load, viewHolder.itemImageView1));
		viewHolder.itemNameView1.setText(item.getName());
		viewHolder.itemDescView1.setText(item.getDesc());
		viewHolder.itemPanel1.setOnClickListener(new LSSelectItemClickLIstener(content,item));
		
		item = items.get(1);
		imageLoader.displayImage(item.getImageUrl(), viewHolder.itemImageView2,
				options, ImageUtil.getImageLoading(viewHolder.iv_load1, viewHolder.itemImageView2));
		viewHolder.itemNameView2.setText(item.getName());
		viewHolder.itemDescView2.setText(item.getDesc());
		viewHolder.itemPanel2.setOnClickListener(new LSSelectItemClickLIstener(content,item));
		
		item = items.get(2);
		imageLoader.displayImage(item.getImageUrl(), viewHolder.itemImageView3,
				options, ImageUtil.getImageLoading(viewHolder.iv_load2, viewHolder.itemImageView3));
		viewHolder.itemNameView3.setText(item.getName());
		viewHolder.itemDescView3.setText(item.getDesc());
		viewHolder.itemPanel3.setOnClickListener(new LSSelectItemClickLIstener(content,item));
		
		
		
	}
	
	private View configureFourView(LSSelectContent content,View convertView,ViewGroup parent){
		FourViewHolder viewHolder = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.select_four_item, null);
			viewHolder = new FourViewHolder();
			
			fillViewHolder(viewHolder, convertView);
			
			viewHolder.itemPanel4 = convertView.findViewById(R.id.itemPanel4);
			viewHolder.itemImageView4 = (ImageView) convertView.findViewById(R.id.imageView4);
			viewHolder.itemNameView4 = (TextView) convertView.findViewById(R.id.nameView4);
			viewHolder.itemDescView4 = (TextView) convertView.findViewById(R.id.descView4);
			
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (FourViewHolder) convertView.getTag();
		}
		
		List<LSSelectItem> items = content.getItems();
		if(items != null && items.size() == 4){
			setViewHolderContent(viewHolder,content,items);
			LSSelectItem item = items.get(3);
			imageLoader.displayImage(item.getImageUrl(), viewHolder.itemImageView4,
					options, ImageUtil.getImageLoading(viewHolder.iv_load3, viewHolder.itemImageView4));
			viewHolder.itemNameView4.setText(item.getName());
			viewHolder.itemDescView4.setText(item.getDesc());
			viewHolder.itemPanel4.setOnClickListener(new LSSelectItemClickLIstener(content,item));
		}
		
		return convertView;
	}
	
	static class ViewHolder{
		TextView titleView;
		TextView descView;
		
		ImageView itemImageView1;
		TextView itemNameView1;
		TextView itemDescView1;
		
		ImageView itemImageView2;
		TextView itemNameView2;
		TextView itemDescView2;
		
		ImageView itemImageView3;
		TextView itemNameView3;
		TextView itemDescView3;
		
		View itemPanel1;
		View itemPanel2;
		View itemPanel3;

		ImageView iv_load, iv_load1, iv_load2, iv_load3;
	}
	
	static class FourViewHolder extends ViewHolder{
		ImageView itemImageView4;
		TextView itemNameView4;
		TextView itemDescView4;
		
		View itemPanel4;
	}
	
	class EquipHolder
	{
		ImageView iv_1, iv_2;
		TextView tv_1, tv_2, tv_3, tv_4, tv_5, tv_6, tv_7, tv_8, tv_9;
	}
	
	class NearbyHolder
	{
		ImageView iv_title, iv_load;
		RelativeLayout layout_info;
		RoundedImageView iv_icon;
		TextView tv_name, tv_info, tv_distance, tv_all;
		View line_all, line_half, view_line;
	}
	
	class EquipAppraiseHolder
	{
		ImageView iv_title, iv_load, iv_load1;
		LinearLayout layout1, layout2;
		RoundedImageView iv_icon1, iv_icon2;
		TextView tv_title1, tv_title2;
		View view_line;
	}
	
	public OnSelectItemClickListener getOnSelectItemListener() {
		return onSelectItemListener;
	}

	public void setOnSelectItemListener(
			OnSelectItemClickListener onSelectItemListener) {
		this.onSelectItemListener = onSelectItemListener;
	}
	
	protected class AppraiseClick implements OnClickListener
	{
		Profilelist item;
		
		public AppraiseClick ( Profilelist item )
		{
			this.item = item;
		}

		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub
			Intent intent = new Intent(c, LSClubTopicActivity.class);
			intent.putExtra("topicID", item.topic_id);
			c.startActivity(intent);
		}
	}
	
	protected class NearbyClick implements OnClickListener
	{
		Shoplist item;
		
		public NearbyClick( Shoplist item )
		{
			this.item = item;
		}
		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub
			switch ( v.getId() )
			{
				case R.id.layout_info:
					goShop();
					break;
				case R.id.tv_all:
					Intent intent = new Intent(c,LsBuyActivity.class);
					c.startActivity(intent);
					break;
			}
		}
		
		public void goShop ()
		{
			Intent intent = new Intent(c,
					ShopDetailActivity.class);
//			String oid = shop.getOid();
//			item.
			intent.putExtra(constens.OID, ""+item.id);
			intent.putExtra("fav", "ls");
			intent.putExtra("dis", ""+item.distance);
			c.startActivity(intent);
		}
		
		
	}

	protected class EquipClick implements OnClickListener
	{
		
		EquipTypeModel model;
		EquipRecommendModel modelHot;
		
		public EquipClick( EquipTypeModel model, EquipRecommendModel modelHot )
		{
			this.model = model;
			this.modelHot = modelHot;
		}
		
		int id, type;
		String title;
		
		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub
			switch ( v.getId() )
			{
				case R.id.tv_1:
					id = modelHot.zhuangbeilist.get(0).id;
					type = modelHot.zhuangbeilist.get(0).type;
					title = modelHot.zhuangbeilist.get(0).catename;
					goEquipInfo(id, title, type);
					break;
				case R.id.tv_2:
					id = modelHot.zhuangbeilist.get(1).id;
					type = modelHot.zhuangbeilist.get(1).type;
					title = modelHot.zhuangbeilist.get(1).catename;
					goEquipInfo(id, title, type);
					break;
				case R.id.tv_3:
					id = modelHot.zhuangbeilist.get(2).id;
					type = modelHot.zhuangbeilist.get(2).type;
					title = modelHot.zhuangbeilist.get(2).catename;
					goEquipInfo(id, title, type);
					break;
				case R.id.tv_4:
					id = modelHot.zhuangbeilist.get(3).id;
					type = modelHot.zhuangbeilist.get(3).type;
					title = modelHot.zhuangbeilist.get(3).catename;
					goEquipInfo(id, title, type);
					break;
				case R.id.tv_5:
					id = modelHot.zhuangbeilist.get(4).id;
					type = modelHot.zhuangbeilist.get(4).type;
					title = modelHot.zhuangbeilist.get(4).catename;
					goEquipInfo(id, title, type);
					break;
				case R.id.tv_6:
//					id = modelHot.zhuangbeilist.get(5).id;
//					type = modelHot.zhuangbeilist.get(5).type;
//					title = modelHot.zhuangbeilist.get(5).catename;
//					goEquipInfo(id, title, type);
					Intent intent = new Intent(c, LSEquiFragment.class);
					c.startActivity(intent);
					break;
				case R.id.tv_7:
					gotoShop(model.accesslist.get(0).url);
					break;
				case R.id.tv_8:
					gotoShop(model.accesslist.get(1).url);
					break;
				case R.id.tv_9:
					gotoShop(model.accesslist.get(2).url);
					break;
			}
		}
		/**0 户外， 1 商场， 2 雪具店， "" 全部*/
		public void gotoShop(String shopType){
			
			String type = "";
			if ( "outdoor_shop".equals(shopType))
			{
				type = "0";
			}
			else if ( "outdoor_mall".equals(shopType))
			{
				type = "1";
			}
			else if ( "outdoor_all".equals(shopType))
			{
				type = "";
			}
			else if ( "outdoor_snowshop".equals(shopType))
			{
				type = "2";
			}
			// ski 雪场
			else if ( "ski".equals(shopType))
			{
				//单独跳转
				c.startActivity( new Intent(c, LSSkiingParkActivity.class));
				return;
			}
			
			Intent intent = new Intent(c,LsBuyActivity.class);
			intent.putExtra("shoptype", type);
			c.startActivity(intent);
		}
		
		public void goEquipInfo ( int id, String title, int type )
		{
			Intent intent = new Intent(c,LsEquiCateActivity.class);
			if(type == 2 ){
				intent.putExtra("cate", id);
			}else{
				intent.putExtra("sport", id);
			}
			intent.putExtra("title", title);
			c.startActivity(intent);
		}
		
	}
	
}
