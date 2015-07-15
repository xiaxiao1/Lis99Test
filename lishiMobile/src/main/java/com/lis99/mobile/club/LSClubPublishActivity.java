package com.lis99.mobile.club;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.util.BitmapUtil;
import com.lis99.mobile.util.C;

import java.io.File;

public class LSClubPublishActivity extends LSBaseActivity {
	
	TextView titleView;
	TextView bodyView;
	Bitmap bitmap;
	ImageView imageView;
	View imagePanel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lsclub_publish);
		initViews();
		setTitle("发布话题");
		
	}
	
	
	@Override
	protected void rightAction() {
		
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.addImage) {
			addImage();
			return;
		} else if (v.getId() == R.id.delButton) {
			bitmap = null;
			imageView.setImageBitmap(null);
			imagePanel.setVisibility(View.GONE);
			return;
		}
		super.onClick(v);
	}
	
	@Override
	protected void initViews() {
		super.initViews();
		titleView = (TextView) findViewById(R.id.titleView);
		bodyView = (TextView) findViewById(R.id.bodyView);
		
		imageView = (ImageView) findViewById(R.id.imageView);
		imagePanel = findViewById(R.id.imagePanel);
		
	}
	
	
	private void addImage() {
		postMessage(POPUP_DIALOG_LIST, "选择图片", R.array.select_head_items,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							// 拍摄
							BitmapUtil.doTakePhoto(LSClubPublishActivity.this);
							break;
						case 1:
							// 相册
							BitmapUtil
									.doPickPhotoFromGallery(LSClubPublishActivity.this);
							break;
						}
					}
				});
	}
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			switch (requestCode) {
			case C.PHOTO_PICKED_WITH_DATA:
				Uri photo_uri = data.getData();
				bitmap = BitmapUtil.getThumbnail(photo_uri, this);
				imagePanel.setVisibility(View.VISIBLE);
				imageView.setImageBitmap(bitmap);
				
				break;
			case C.PICKED_WITH_DATA:
				break;
			case C.CAMERA_WITH_DATA:
				File file = new File(C.HEAD_IMAGE_PATH + "temp.jpg");
				bitmap = BitmapUtil.getThumbnail(file, this);
				imagePanel.setVisibility(View.VISIBLE);
				imageView.setImageBitmap(bitmap);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
