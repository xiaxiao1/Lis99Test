package com.lis99.mobile.entry;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.application.data.UserMsgBean;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.view.AsyncLoadImageView;
import com.lis99.mobile.newhome.equip.LSEquipInfoActivity;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.RequestParamUtil;
import com.lis99.mobile.util.StatusUtil;

import java.util.HashMap;
import java.util.List;

public class LsUserMsgActivity extends ActivityPattern {

	ImageView iv_back;
	TextView bt_dangjiliuxing,bt_remenzhuanji;
	ListView lv_msg;
	private static final int SHOW_ZHUANGBEI_LIST = 200;
	private static final int SHOW_WENDA_LIST = 201;
	Bitmap bmp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ls_user_msg);
		StatusUtil.setStatusBar(this);
		Resources res = getResources();  
		bmp = BitmapFactory.decodeResource(res, R.drawable.ls_nologin_header_icon);
		setView();
		setListener();
		postMessage(POPUP_PROGRESS,getString(R.string.sending));
		getUserShaiList();
	}
	private void getUserShaiList() {
		String url = C.USER_NOTICE_URL+"0";
		Task task = new Task(null, url, null, "ZHUANGBEI_LIST_URL", this);
		task.setPostData(getSendParams().getBytes());
		publishTask(task, IEvent.IO);
	}
	private String getSendParams() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", DataManager.getInstance().getUser().getUser_id());
		return RequestParamUtil.getInstance(this).getRequestParams(params);
	}
	
	@Override
	public void handleTask(int initiator, Task task, int operation) {
		super.handleTask(initiator, task, operation);
		String result;
		switch (initiator) {
		case IEvent.IO:
			if (task.getData() instanceof byte[]) {
				result = new String((byte[]) task.getData());
				if (((String) task.getParameter()).equals("ZHUANGBEI_LIST_URL")) {
					parserZhuangbeiList(result);
				}else if(((String) task.getParameter()).equals("USER_WENDA_NOTICE_URL")) {
					parserWendaList(result);
				}
			}
			break;
		default:
			break;
		}
	}
	List<UserMsgBean> msgs;
	private void parserZhuangbeiList(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if("OK".equals(result)){
				msgs =(List<UserMsgBean>) DataManager.getInstance().jsonParse(params, DataManager.TYPE_USER_NOTICE);
				postMessage(SHOW_ZHUANGBEI_LIST);
			}else{
				postMessage(POPUP_TOAST, result);
				postMessage(DISMISS_PROGRESS);
			}
		}
		
	}
	private void parserWendaList(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if("OK".equals(result)){
				msgs =(List<UserMsgBean>) DataManager.getInstance().jsonParse(params, DataManager.TYPE_USER_WENDA_NOTICE);
				postMessage(SHOW_WENDA_LIST);
			}else{
				postMessage(POPUP_TOAST, result);
				postMessage(DISMISS_PROGRESS);
			}
		}
		
	}
	@Override
	public boolean handleMessage(Message msg) {
		if (super.handleMessage(msg))
			return true;
		switch (msg.what) {
		case SHOW_ZHUANGBEI_LIST:
			adapter = new DarenListAdapter();
			lv_msg.setAdapter(adapter);
			postMessage(DISMISS_PROGRESS);
			break;
		case SHOW_WENDA_LIST:
			adapter = new DarenListAdapter();
			lv_msg.setAdapter(adapter);
			postMessage(DISMISS_PROGRESS);
			break;
		}
		return true;
	}
	private static class ViewHolder{
		AsyncLoadImageView iv_pic,iv_image;
		TextView nickname,content,time;
	}
	DarenListAdapter adapter;
private class DarenListAdapter extends BaseAdapter {
		
		LayoutInflater inflater;

		public DarenListAdapter() {
			inflater = LayoutInflater.from(LsUserMsgActivity.this);
		}

		@Override
		public int getCount() {
			return msgs.size();
		}

		@Override
		public Object getItem(int arg0) {
			return msgs.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			UserMsgBean cb = msgs.get(position);
			final int pos = position;
			final String oid = cb.getObject_id();
			final String aid = cb.getWenda_id();
			final String typeid = cb.getType_id();
			if(convertView==null){
					convertView = inflater.inflate(R.layout.ls_user_msg_item, null);
					holder=new ViewHolder();
					holder.content = (TextView) convertView.findViewById(R.id.content);
					holder.time = (TextView) convertView.findViewById(R.id.time);
					holder.nickname = (TextView) convertView.findViewById(R.id.nickname);
					holder.iv_pic = (AsyncLoadImageView) convertView.findViewById(R.id.iv_pic);
					holder.iv_image = (AsyncLoadImageView) convertView.findViewById(R.id.iv_image);
					
					convertView.setTag(holder);
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			holder.content.setText(cb.getContent());
			if(cb.getNickname()!=null&&!"".equals(cb.getNickname())){
				holder.nickname.setText(cb.getNickname());
			}else{
				holder.nickname.setText("匿名者");
			}
			holder.time.setText(cb.getCreate_time());
			holder.iv_pic.setImage(cb.getHeadicon(), bmp, bmp,"zhuangbei_detail");
			holder.iv_image.setImage(cb.getImage_url(), null, null);
				convertView.setOnClickListener(new android.view.View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if("4".equals(typeid)){
							Intent intent = new Intent(LsUserMsgActivity.this, LsShaiDetailActivity.class);
							intent.putExtra("id", oid);
							LsUserMsgActivity.this.startActivity(intent);
						}else if("6".equals(typeid)){
//							Intent intent = new Intent(LsUserMsgActivity.this,LsZhuangbeiDetail.class);
							Intent intent = new Intent(LsUserMsgActivity.this,LSEquipInfoActivity.class);
							intent.putExtra("id", oid);
							LsUserMsgActivity.this.startActivity(intent);
						}else if("2".equals(typeid)||"8".equals(typeid)){
							Intent intent = new Intent(LsUserMsgActivity.this,LsAskDetailActivity.class);
							intent.putExtra("ask_id", oid);
							LsUserMsgActivity.this.startActivity(intent);
						}else if("3".equals(typeid)){
							Intent intent = new Intent(LsUserMsgActivity.this, LsAnswerDetailActivity.class);
							intent.putExtra("ask_id", aid);
							intent.putExtra("answer_id", oid);
							LsUserMsgActivity.this.startActivity(intent);
						}
					}
				});
			return convertView;
		}
		
	}

	private void setView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		//tv_clear = (TextView) findViewById(R.id.tv_clear);
		bt_dangjiliuxing = (TextView) findViewById(R.id.bt_dangjiliuxing);
		bt_remenzhuanji = (TextView) findViewById(R.id.bt_remenzhuanji);
		lv_msg = (ListView) findViewById(R.id.lv_msg);
	}
	private void setListener() {
		iv_back.setOnClickListener(this);
		//tv_clear.setOnClickListener(this);
		bt_dangjiliuxing.setOnClickListener(this);
		bt_remenzhuanji.setOnClickListener(this);
	}
	String type="1";
	@Override
	public void onClick(View v) {
		if(v.getId() == iv_back.getId()){
			finish();
		}/*else if(v.getId() == tv_clear.getId()){
			postMessage(POPUP_ALERT, null, "确定要全部删除吗？", true, "确定", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(LsUserMsgActivity.this, "数据为空", 0).show();
				}
			}, true, "取消", null);
		}*/else if(v.getId() == bt_dangjiliuxing.getId()){
			type = "1";
			bt_dangjiliuxing.setBackgroundResource(R.drawable.ls_xuan_navigation_bg_left_selected);
			bt_dangjiliuxing.setTextColor(0xffffffff);
			bt_remenzhuanji.setBackgroundResource(R.drawable.ls_xuan_navigation_bg_right_unselected);
			bt_remenzhuanji.setTextColor(0xff2acbc2);
			postMessage(POPUP_PROGRESS,getString(R.string.sending));
			getUserShaiList();
		}else if(v.getId() == bt_remenzhuanji.getId()){
			type = "2";
			bt_dangjiliuxing.setBackgroundResource(R.drawable.ls_xuan_navigation_bg_left_unselected);
			bt_dangjiliuxing.setTextColor(0xff2acbc2);
			bt_remenzhuanji.setBackgroundResource(R.drawable.ls_xuan_navigation_bg_right_selected);
			bt_remenzhuanji.setTextColor(0xffffffff);
			postMessage(POPUP_PROGRESS,getString(R.string.sending));
			getUserWendaList();
		}
	}
	private void getUserWendaList() {
		String url = C.USER_WENDA_NOTICE_URL+"0";
		Task task = new Task(null, url, null, "USER_WENDA_NOTICE_URL", this);
		task.setPostData(getWendaParams().getBytes());
		publishTask(task, IEvent.IO);
	}

	private String getWendaParams() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", DataManager.getInstance().getUser().getUser_id());
		return RequestParamUtil.getInstance(this).getRequestParams(params);
	}
}