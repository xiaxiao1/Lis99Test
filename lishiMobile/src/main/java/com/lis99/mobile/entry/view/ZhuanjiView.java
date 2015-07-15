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
import com.lis99.mobile.entry.LsZhuanjiDetail;
import com.lis99.mobile.util.StringUtil;

public class ZhuanjiView extends LinearLayout implements View.OnClickListener{
	
	public ZhuanjiView(Context c, AttributeSet attrs) {
		super(c, attrs);
		inflater = LayoutInflater.from(c);
		this.context = c;
		init();	
	}

	LayoutInflater inflater;
	View convertView;
	private FlowTag flowTag;
	private Context context;
	public Bitmap bitmap;
	// private ImageLoaderTask task;
	private int columnIndex;// 图片属于第几列
	private int rowIndex;// 图片属于第几行
	private Handler viewHandler;
	FlowView1 ls_zhuanji_item_pic;
	TextView ls_zhuanji_item_title;
	
	public FlowView1 getLs_zhuanji_item_pic() {
		return ls_zhuanji_item_pic;
	}


	public void setLs_zhuanji_item_pic(FlowView1 ls_zhuanji_item_pic) {
		this.ls_zhuanji_item_pic = ls_zhuanji_item_pic;
	}


	public TextView getLs_zhuanji_item_title() {
		return ls_zhuanji_item_title;
	}


	public void setLs_zhuanji_item_title(TextView ls_zhuanji_item_title) {
		this.ls_zhuanji_item_title = ls_zhuanji_item_title;
	}


	/**
	 * 加载图片
	 */
	public void LoadImage() {
		if (getFlowTag() != null) {
			

			//setImageBitmap(bitmap);
			ls_zhuanji_item_pic.setImage(flowTag.getFileName(), null, null);
			
			//new LoadImageThread().start();
		}
	}

	/**
	 * 回收内存
	 *//*
	public void recycle() {
		ls_zhuanji_item_pic.recycle();
		if ((this.bitmap == null) || (this.bitmap.isRecycled()))
			return;
		this.bitmap.recycle();
		this.bitmap = null;
	}*/
	public FlowTag getFlowTag() {
		return flowTag;
	}

	public void setFlowTag(FlowTag flowTag) {
		this.flowTag = flowTag;
		ls_zhuanji_item_pic.setFlowTag(flowTag);
	}

	public int getColumnIndex() {
		return columnIndex;
	}

	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
		ls_zhuanji_item_pic.setColumnIndex(columnIndex);
	}

	public int getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
		ls_zhuanji_item_pic.setRowIndex(rowIndex);
	}

	public Handler getViewHandler() {
		return viewHandler;
	}

	public void setViewHandler(Handler viewHandler) {
		this.viewHandler = viewHandler;
		ls_zhuanji_item_pic.setViewHandler(viewHandler);
	}

	public ZhuanjiView(Context c) {
		super(c);
		inflater = LayoutInflater.from(c);
		this.context = c;
		init();
	}

	private void init() {
		this.setOnClickListener(this);
		convertView = inflater.inflate(R.layout.ls_xuan_zhuanji_item, null);
		
		
		ls_zhuanji_item_pic = (FlowView1) convertView.findViewById(R.id.ls_zhuanji_item_pic);
		ls_zhuanji_item_title = (TextView) convertView.findViewById(R.id.ls_zhuanji_item_title);
		ls_zhuanji_item_pic.setZhuanjiView(this);
		ls_zhuanji_item_pic.setContext(context);
		LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		ll.width = StringUtil.getXY((Activity)context)[0];
		convertView.setLayoutParams(ll);
		addView(convertView);
	}
	
	public void Reload() {
		//if (this.bitmap == null && getFlowTag() != null) {
			ls_zhuanji_item_pic.setImage(flowTag.getFileName(), null, null);
			//new ReloadImageThread().start();
		//}
	}
	@Override
	public void onClick(View arg0) {
		Log.d("zhuangbei", "Click");
		Intent intent = new Intent(context,LsZhuanjiDetail.class);
		intent.putExtra("id", String.valueOf(flowTag.getFlowId()));
		context.startActivity(intent);
	}
}
