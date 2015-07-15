package com.lis99.mobile.entry;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.lis99.mobile.R;
import com.lis99.mobile.entry.adapter.GalleryAdapter;
import com.lis99.mobile.myactivty.ShowPic;
import com.lis99.mobile.util.StatusUtil;

import java.util.ArrayList;

import java.util.ArrayList;

public class LsLineGalleryActivity extends ActivityPattern1 {
	
	ArrayList<ShowPic> photos;
	View iv_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ls_line_gallery);

		StatusUtil.setStatusBar(this);
		photos = (ArrayList <ShowPic>)getIntent().getSerializableExtra("photos");
		initViews();
	}
	
	@Override
	public void finish() {
		super.finish();
	}
	
	protected void initViews() {
		iv_back = findViewById(R.id.iv_back);
		iv_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		GridView grid = (GridView) findViewById(R.id.gridView);
		GalleryAdapter adapter = new GalleryAdapter(photos, this);
		grid.setAdapter(adapter);
		
		grid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(LsLineGalleryActivity.this, LsLineBigImageActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("photos", photos);
				bundle.putInt("page", position);
				intent.putExtras(bundle);
				startActivity(intent);
				
			}
		});
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
	
	//static class GalleryAdapter extend
	

}
