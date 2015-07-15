package com.huewu.pla.sample;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.huewu.pla.R;
import com.huewu.pla.lib.MultiColumnPullToRefreshListView;
import com.huewu.pla.lib.internal.PLA_AdapterView;

import java.util.Arrays;
import java.util.Random;

public class PullToRefreshSampleActivity extends Activity {
	private class MySimpleAdapter extends ArrayAdapter<View> {
		LayoutInflater inflater;
		public MySimpleAdapter(Context context, int layoutRes) {
			super(context, layoutRes, android.R.id.text1);
			this.inflater = LayoutInflater.from(PullToRefreshSampleActivity.this);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			return super.getView(position, convertView, parent);
		}
		
		
	}
	
	private class ZhuanjiAdapter extends BaseAdapter {
		

		
		public ZhuanjiAdapter() {
			this.inflater = LayoutInflater.from(PullToRefreshSampleActivity.this);;
		}

		LayoutInflater inflater;
		
		@Override
		public int getCount() {
			return 16;
		}

		@Override
		public Object getItem(int arg0) {
			return arg0;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
				//convertView = inflater.inflate(R.layout.ls_xuan_zhuangbei_item, null);
			//holder.ls_zhuanji_item_pic.setImage(zj.getImage(), null, null);
			return convertView;
		}
		
	}

	private MultiColumnPullToRefreshListView mAdapterView = null;
	private MySimpleAdapter mAdapter = null;
	private ZhuanjiAdapter mAdapter1 = null;
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sample_pull_to_refresh_act);
		//mAdapterView = (PLA_AdapterView<Adapter>) findViewById(R.id.list);
		mAdapterView = (MultiColumnPullToRefreshListView) findViewById(R.id.list);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, 1001, 0, "Load More Contents");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch(item.getItemId()){
		case 1001:
		{
			int startCount = mAdapter.getCount();
			for( int i = 0; i < 100; ++i){
				//generate 100 random items.

				StringBuilder builder = new StringBuilder();
				builder.append("Hello!![");
				builder.append(startCount + i);
				builder.append("] ");

				char[] chars = new char[mRand.nextInt(100)];
				Arrays.fill(chars, '1');
				builder.append(chars);
				//mAdapter.add(builder.toString());
			}
		}
		break;
		case 1002:
		{
			Intent intent = new Intent(this, PullToRefreshSampleActivity.class);
			startActivity(intent);
		}
		break;
		}
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		//initAdapter();
		mAdapter1 = new ZhuanjiAdapter();
		mAdapterView.setAdapter(mAdapter1);
		//mAdapterView.setAdapter(mAdapter);
	}

	private Random mRand = new Random();
	private void initAdapter() {
		mAdapter = new MySimpleAdapter(this, R.layout.ls_xuan_zhuangbei_item);
		for( int i = 0; i < 4; ++i){
			//generate 30 random items.

			/*StringBuilder builder = new StringBuilder();
			builder.append("Hello!![");
			builder.append(i);
			builder.append("] ");

			char[] chars = new char[mRand.nextInt(500)];
			Arrays.fill(chars, '1');
			builder.append(chars);*/
			//View view = inflater.inflate(R.layout.ls_xuan_zhuangbei_item, null);
			//mAdapter.add(view);
		}

	}

}//end of class
