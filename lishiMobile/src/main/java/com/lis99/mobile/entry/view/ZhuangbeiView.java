package com.lis99.mobile.entry.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.cache.ImageCacheManager;
import com.lis99.mobile.entry.LsZhuangbeiDetail;
import com.lis99.mobile.util.StringUtil;

public class ZhuangbeiView extends LinearLayout implements View.OnClickListener{

	

	public ZhuangbeiView(Context c, AttributeSet attrs) {
		super(c, attrs);
		inflater = LayoutInflater.from(c);
		this.context = c;
		init();	
	}
	/**
	 * 回收内存
	 *//*
	public void recycle() {
		ls_zhuangbei_item_pic.recycle();
		if ((this.bitmap == null) || (this.bitmap.isRecycled()))
			return;
		this.bitmap.recycle();
		this.bitmap = null;
	}*/
	LayoutInflater inflater;
	View convertView;
	private FlowTag flowTag;
	private Context context;
	public Bitmap bitmap;
	// private ImageLoaderTask task;
	private int columnIndex;// 图片属于第几列
	private int rowIndex;// 图片属于第几行
	private Handler viewHandler;
	FlowView ls_zhuangbei_item_pic;
	TextView ls_zhuangbei_item_title,ls_zhuangbei_item_score;
	LinearLayout ls_zhuangbei_item_star;
	
	public FlowView getLs_zhuangbei_item_pic() {
		return ls_zhuangbei_item_pic;
	}

	public void setLs_zhuangbei_item_pic(FlowView ls_zhuangbei_item_pic) {
		this.ls_zhuangbei_item_pic = ls_zhuangbei_item_pic;
	}

	public TextView getLs_zhuangbei_item_title() {
		return ls_zhuangbei_item_title;
	}

	public void setLs_zhuangbei_item_title(TextView ls_zhuangbei_item_title) {
		this.ls_zhuangbei_item_title = ls_zhuangbei_item_title;
	}

	public TextView getLs_zhuangbei_item_score() {
		return ls_zhuangbei_item_score;
	}

	public void setLs_zhuangbei_item_score(TextView ls_zhuangbei_item_score) {
		this.ls_zhuangbei_item_score = ls_zhuangbei_item_score;
	}

	public LinearLayout getLs_zhuangbei_item_star() {
		return ls_zhuangbei_item_star;
	}

	public void setLs_zhuangbei_item_star(LinearLayout ls_zhuangbei_item_star) {
		this.ls_zhuangbei_item_star = ls_zhuangbei_item_star;
	}
	
	/**
	 * 加载图片
	 */
	public void LoadImage() {
		if (getFlowTag() != null) {
			

			//setImageBitmap(bitmap);
			ls_zhuangbei_item_pic.setImage(flowTag.getFileName(), null, null);
			
			//new LoadImageThread().start();
		}
	}


	public FlowTag getFlowTag() {
		return flowTag;
	}

	public void setFlowTag(FlowTag flowTag) {
		this.flowTag = flowTag;
		ls_zhuangbei_item_pic.setFlowTag(flowTag);
	}

	public int getColumnIndex() {
		return columnIndex;
	}

	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
		ls_zhuangbei_item_pic.setColumnIndex(columnIndex);
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
		ls_zhuangbei_item_pic.setRowIndex(rowIndex);
	}

	public Handler getViewHandler() {
		return viewHandler;
	}

	public void setViewHandler(Handler viewHandler) {
		this.viewHandler = viewHandler;
		ls_zhuangbei_item_pic.setViewHandler(viewHandler);
	}

	public ZhuangbeiView(Context c) {
		super(c);
		inflater = LayoutInflater.from(c);
		this.context = c;
		init();
	}

	private void init() {
		this.setOnClickListener(this);
		convertView = inflater.inflate(R.layout.ls_xuan_zhuangbei_item, null);
		ls_zhuangbei_item_pic = (FlowView) convertView.findViewById(R.id.ls_zhuangbei_item_pic);
		ls_zhuangbei_item_title = (TextView) convertView.findViewById(R.id.ls_zhuangbei_item_title);
		ls_zhuangbei_item_score = (TextView) convertView.findViewById(R.id.ls_zhuangbei_item_score);
		ls_zhuangbei_item_star = (LinearLayout) convertView.findViewById(R.id.ls_zhuangbei_item_star);
		ls_zhuangbei_item_pic.setZhuangbeiView(this);
		ls_zhuangbei_item_pic.setContext(context);
		LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		ll.width = StringUtil.getXY((Activity)context)[0]/2;
		convertView.setLayoutParams(ll);
		addView(convertView);
	}
	
	public void Reload() {
		//if (this.bitmap == null && getFlowTag() != null) {
			ls_zhuangbei_item_pic.setImage(flowTag.getFileName(), null, null);
			//new ReloadImageThread().start();
		//}
	}
	@Override
	public void onClick(View arg0) {
		Log.d("zhuangbei", "Click");
		Intent intent = new Intent(context,LsZhuangbeiDetail.class);
		intent.putExtra("id", String.valueOf(flowTag.getFlowId()));
		context.startActivity(intent);
	}

	public void recycle() {
		ls_zhuangbei_item_pic.recycle();
		ImageCacheManager.getInstance().removeBitmapFromCache(flowTag.getFileName());
		if ((this.bitmap == null) || (this.bitmap.isRecycled()))
			return;
		this.bitmap.recycle();
		this.bitmap = null;
	}
}
