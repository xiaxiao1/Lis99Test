package com.lis99.mobile.entry;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.application.data.UserDraftBean;
import com.lis99.mobile.entry.view.AsyncLoadImageView;
import com.lis99.mobile.util.StatusUtil;

import java.util.List;

public class LsUserDraftActivity extends ActivityPattern {

	ImageView iv_back;
	TextView tv_clear;
	ListView lv_draft;
	private static final int SHOW_ZHUANGBEI_LIST = 200;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ls_user_draft);
		StatusUtil.setStatusBar(this);
		setView();
		setListener();
		postMessage(POPUP_PROGRESS,getString(R.string.sending));
		postMessage(SHOW_ZHUANGBEI_LIST);
	}
	List<UserDraftBean> lists;
	@Override
	public boolean handleMessage(Message msg) {
		if (super.handleMessage(msg))
			return true;
		switch (msg.what) {
		case SHOW_ZHUANGBEI_LIST:
			lists = DataManager.getInstance().getDrafts();
			adapter = new DraftListAdapter();
			lv_draft.setAdapter(adapter);
			postMessage(DISMISS_PROGRESS);
			break;
		}
		return true;
	}
	DraftListAdapter adapter;
	private static class ViewHolder{
		AsyncLoadImageView iv_pic;
		TextView tv_cate,tv_time,tv_title;
	}
	private class DraftListAdapter extends BaseAdapter {
		
		LayoutInflater inflater;

		public DraftListAdapter() {
			inflater = LayoutInflater.from(LsUserDraftActivity.this);
		}

		@Override
		public int getCount() {
			return lists.size();
		}

		@Override
		public Object getItem(int arg0) {
			return lists.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			UserDraftBean cb = lists.get(position);
			final int pos = position;
			final String type = cb.getType();
			if(convertView==null){
					convertView = inflater.inflate(R.layout.ls_draft_list_item, null);
					holder=new ViewHolder();
					holder.iv_pic = (AsyncLoadImageView) convertView.findViewById(R.id.iv_pic);
					holder.tv_cate = (TextView) convertView.findViewById(R.id.tv_cate);
					holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
					holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
					convertView.setTag(holder);
					if(cb.getImage()!=null&&!"".equals(cb.getImage())){
						holder.iv_pic.setVisibility(View.VISIBLE);
						holder.iv_pic.setImage(cb.getImage(), null, null);
					}else{
						holder.iv_pic.setVisibility(View.GONE);
					}
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			holder.tv_cate.setText(cb.getType());
			holder.tv_time.setText(cb.getTime());
			holder.tv_title.setText(cb.getTitle());
			convertView.setOnClickListener(new android.view.View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if("提问".equals(type)){
						Intent intent = new Intent(LsUserDraftActivity.this, LsTiwenActivity.class);
						intent.putExtra("pos", String.valueOf(pos));
						LsUserDraftActivity.this.startActivity(intent);
					}else if("晒装备".equals(type)){
						Intent intent = new Intent(LsUserDraftActivity.this, LsShaiUploadActivity.class);
						intent.putExtra("pos", String.valueOf(pos));
						LsUserDraftActivity.this.startActivity(intent);
					}
					
				}
			});
			return convertView;
		}
		
	}
	private void setView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_clear = (TextView) findViewById(R.id.tv_clear);
		lv_draft = (ListView) findViewById(R.id.lv_draft);
	}
	private void setListener() {
		iv_back.setOnClickListener(this);
		tv_clear.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == iv_back.getId()){
			finish();
		}else if(v.getId() == tv_clear.getId()){
			postMessage(POPUP_ALERT, null, "确定要全部删除吗？", true, "确定", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					DataManager.getInstance().getDrafts().clear();
					postMessage(POPUP_PROGRESS,getString(R.string.sending));
					postMessage(SHOW_ZHUANGBEI_LIST);
					Toast.makeText(LsUserDraftActivity.this, "删除成功", 0).show();
				}
			}, true, "取消", null);
		}
	}}
