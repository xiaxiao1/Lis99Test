package com.lis99.mobile.entry;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.lis99.mobile.R;
import com.lis99.mobile.entry.mypic.GroupAdapter;
import com.lis99.mobile.entry.mypic.ImageBean;
import com.lis99.mobile.entry.mypic.ShowImageActivity;
import com.lis99.mobile.util.StatusUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LsActivityLoadUp1 extends ActivityPattern1 {
	private HashMap<String, List<String>> mGruopMap = new HashMap<String, List<String>>();
	private List<ImageBean> list = new ArrayList<ImageBean>();
	private final static int SCAN_OK = 1;
	protected static final int SEND_CODE = 330;
	private ProgressDialog mProgressDialog;
	private GroupAdapter adapter;
	private GridView mGroupGridView;

	private Bundle bundle;
	int album_id;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SCAN_OK:
				mProgressDialog.dismiss();
				adapter = new GroupAdapter(LsActivityLoadUp1.this,
						list = subGroupOfImage(mGruopMap), mGroupGridView);
				mGroupGridView.setAdapter(adapter);
				break;
			}
		}

	};
      private int id;

      @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aaaactivity_main);

		  StatusUtil.setStatusBar(this);

		bundle = this.getIntent().getExtras();
		album_id = bundle.getInt("albumid",1);
            id = bundle.getInt("id",1);
		mGroupGridView = (GridView) findViewById(R.id.main_grid);

		getImages();
		mGroupGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				List<String> childList = mGruopMap.get(list.get(position)
						.getFolderName());

				Intent mIntent = new Intent(LsActivityLoadUp1.this,
						ShowImageActivity.class);
				mIntent.putStringArrayListExtra("data",
						(ArrayList<String>) childList);
				mIntent.putExtra("album_id", album_id);
                        mIntent.putExtra("id",LsActivityLoadUp1.this.id);
				startActivity(mIntent);
//				startActivityForResult(mIntent, SEND_CODE);

			}
		});
	}

	private void getImages() {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			Toast.makeText(this, "sd卡空间不足", Toast.LENGTH_SHORT).show();
			return;
		}
		mProgressDialog = ProgressDialog.show(this, null, "稍后- -");
		new Thread(new Runnable() {

			@Override
			public void run() {
				Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver mContentResolver = LsActivityLoadUp1.this
						.getContentResolver();

				Cursor mCursor = mContentResolver.query(mImageUri, null,
						MediaStore.Images.Media.MIME_TYPE + "=? or "
								+ MediaStore.Images.Media.MIME_TYPE + "=?",
						new String[] { "image/jpeg", "image/png" },
						MediaStore.Images.Media.DATE_MODIFIED);

				while (mCursor.moveToNext()) {
					String path = mCursor.getString(mCursor
							.getColumnIndex(MediaStore.Images.Media.DATA));

					String parentName = new File(path).getParentFile()
							.getName();

					if (!mGruopMap.containsKey(parentName)) {
						List<String> chileList = new ArrayList<String>();
						chileList.add(path);
						mGruopMap.put(parentName, chileList);
					} else {
						mGruopMap.get(parentName).add(path);
					}
				}

				mCursor.close();

				mHandler.sendEmptyMessage(SCAN_OK);

			}
		}).start();

	}

	private List<ImageBean> subGroupOfImage(
			HashMap<String, List<String>> mGruopMap) {
            List<ImageBean> list = new ArrayList<ImageBean>();
            if (mGruopMap.size() == 0) {
                  return list;
            }

		Iterator<Map.Entry<String, List<String>>> it = mGruopMap.entrySet()
				.iterator();
		while (it.hasNext()) {
			Map.Entry<String, List<String>> entry = it.next();
			ImageBean mImageBean = new ImageBean();
			String key = entry.getKey();
			List<String> value = entry.getValue();

			mImageBean.setFolderName(key);
			mImageBean.setImageCounts(value.size());
			mImageBean.setTopImagePath(value.get(0));

			list.add(mImageBean);
		}
		return list;
	}

//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		if (requestCode == SEND_CODE && resultCode == 100) {
//			ArrayList<String> listData = new ArrayList<String>() ;
//			Intent intent = new Intent();
//			intent.putStringArrayListExtra("mData", listData);
//			setResult(SEND_CODE, intent);
//			finish();
//		}
//	}
	
}
