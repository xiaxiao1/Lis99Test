package com.lis99.mobile.entry;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.AnswerBean;
import com.lis99.mobile.application.data.DarenDetailBean;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.application.data.ShaiDateBean;
import com.lis99.mobile.application.data.ShaiYearBean;
import com.lis99.mobile.application.data.ShaituDateBean;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.view.AsyncLoadImageView;
import com.lis99.mobile.entry.view.AutoResizeGridView;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.RequestParamUtil;
import com.lis99.mobile.util.StatusUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LsDarenDetailActivity extends ActivityPattern {

	
	ImageView iv_back,user_vip;
	TextView tv_title,user_name,user_city,tv_guanzhu,user_guangzhu,user_desc,user_answernum,answer_title,item_zan,item_content,user_shainum;
	
	AutoResizeGridView gv_shaitu;
	RelativeLayout bt_tiwen;
	LinearLayout ll_guangzhu,ll_zans,ll_huida,ll_shaitu;
	AsyncLoadImageView user_head;
	String userid;
	private static final int SHOW_NEW_LIST = 200;
	private static final int SHOW_NEW_LIST1 = 201;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ls_daren_detail);
		StatusUtil.setStatusBar(this);
		userid = getIntent().getStringExtra("userid");
		setView();
		setListener();
		postMessage(POPUP_PROGRESS,getString(R.string.sending));
		getDarenDetailList();
	}
	
	private void getDarenDetailList() {
		String url = C.DAREN_DETAIL_URL+userid;
		Task task = new Task(null, url, null, "USER_DAREN_URL", this);
		task.setPostData(getLoginParams().getBytes());
		publishTask(task, IEvent.IO);		
	}
	private String getLoginParams() {
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
				if (((String) task.getParameter()).equals("USER_DAREN_URL")) {
					parserZhuangbeiList(result);
				}else if (((String) task.getParameter()).equals("USER__URL")) {
					parserzzList(result);
				}
			}
			break;
		default:
			break;
		}
	}
	DarenDetailBean ddb;
	private void parserzzList(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if("OK".equals(result)){
				postMessage(SHOW_NEW_LIST1);
			}else{
				postMessage(POPUP_TOAST, result);
				postMessage(DISMISS_PROGRESS);
			}
		}
		
	}
	private void parserZhuangbeiList(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if("OK".equals(result)){
				ddb = (DarenDetailBean) DataManager.getInstance().jsonParse(params, DataManager.TYPE_DAREN_DETAIL);
				postMessage(SHOW_NEW_LIST);
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
		case SHOW_NEW_LIST:
			tv_title.setText(ddb.getUser().getNickname()+"信息");
			user_name.setText(ddb.getUser().getNickname());
			if("1".equals(ddb.getUser().getIs_vip())){
				user_vip.setVisibility(View.VISIBLE);
			}else{
				user_vip.setVisibility(View.GONE);
			}
			user_city.setText(ddb.getUser().getCity());
			if("0".equals(ddb.getRelation()) || "1".equals(ddb.getRelation()) || "2".equals(ddb.getRelation())){
				tv_guanzhu.setText("已关注");
			}else{
				tv_guanzhu.setText("+关注");
			}
			user_guangzhu.setText(ddb.getFollowing_num());
			user_desc.setText(ddb.getUser().getNote());
			user_answernum.setText("回答的问题（"+ddb.getHuida_num()+"）");
			user_head.setImage(ddb.getUser().getHeadicon(), null, null,"zhuangbei_detail");
			try{
				if(Integer.parseInt(ddb.getHuida_num())>0){
					AnswerBean ab = ddb.getAnswers().get(0);
					answer_title.setText(ab.getTitle());
					item_zan.setText(ab.getLikenum());
					item_content.setText(ab.getContent());
					ll_zans.setVisibility(View.VISIBLE);
				}else{
					answer_title.setText("");
					item_zan.setText("");
					item_content.setText("");
					ll_zans.setVisibility(View.GONE);
				}
			}catch (Exception e) {
			}
			
			user_shainum.setText("晒装备（"+ddb.getShaitu_num()+"）");
			try{
				if(Integer.parseInt(ddb.getShaitu_num())>0){
					sts = new ArrayList<String>();
					for(ShaiYearBean syb:ddb.getShaiyears()){
						for(ShaiDateBean ddd:syb.getDateBean()){
							for(ShaituDateBean sdb:ddd.getDateBean()){
								//if(sts.size()<15){
									sts.add(sdb.getImage_url());
								//}else{
								//	break;
								//}
							}
							
						}
						
					}
					adapter = new YearAdapter();
					gv_shaitu.setAdapter(adapter);
				}else{
					
				}
			}catch (Exception e) {
			}
			postMessage(DISMISS_PROGRESS);
			break;
		case SHOW_NEW_LIST1:
			Toast.makeText(this, "操作成功", 0).show();
			postMessage(DISMISS_PROGRESS);
			break;
		}
		return true;
	}
	List<String>  sts;
	private static class YearHolder{
		AsyncLoadImageView iv_image;
	}
	YearAdapter adapter;
	private class YearAdapter extends BaseAdapter {
		
		LayoutInflater inflater;

		public YearAdapter() {
			inflater = LayoutInflater.from(LsDarenDetailActivity.this);
		}

		@Override
		public int getCount() {
			return sts.size();
		}

		@Override
		public Object getItem(int arg0) {
			return sts.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			YearHolder holder;
			final int pos = position;
			String sy = sts.get(position);
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.ls_daren_shai_item, null);
				holder = new YearHolder();
				holder.iv_image = (AsyncLoadImageView) convertView.findViewById(R.id.iv_image);
				convertView.setTag(holder);
			} else {
				holder = (YearHolder) convertView.getTag();
			}
			holder.iv_image.setImage(sy, null, null);
			return convertView;
		}
		
	}
	private void setView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		user_name = (TextView) findViewById(R.id.user_name);
		user_vip = (ImageView) findViewById(R.id.user_vip);
		user_city = (TextView) findViewById(R.id.user_city);
		tv_guanzhu = (TextView) findViewById(R.id.tv_guanzhu);
		user_guangzhu = (TextView) findViewById(R.id.user_guangzhu);
		user_desc = (TextView) findViewById(R.id.user_desc);
		user_answernum = (TextView) findViewById(R.id.user_answernum);
		answer_title = (TextView) findViewById(R.id.answer_title);
		item_zan = (TextView) findViewById(R.id.item_zan);
		item_content = (TextView) findViewById(R.id.item_content);
		user_shainum = (TextView) findViewById(R.id.user_shainum);
		gv_shaitu = (AutoResizeGridView) findViewById(R.id.gv_shaitu);
		bt_tiwen = (RelativeLayout) findViewById(R.id.bt_tiwen);
		ll_guangzhu = (LinearLayout) findViewById(R.id.ll_guangzhu);
		user_head = (AsyncLoadImageView) findViewById(R.id.user_head);
		ll_zans =  (LinearLayout) findViewById(R.id.ll_zans);
		ll_huida =  (LinearLayout) findViewById(R.id.ll_huida);
		ll_shaitu =  (LinearLayout) findViewById(R.id.ll_shaitu);
		
	}
	private void setListener() {
		iv_back.setOnClickListener(this);
		bt_tiwen.setOnClickListener(this);
		ll_guangzhu.setOnClickListener(this);
		ll_huida.setOnClickListener(this);
		ll_shaitu.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == iv_back.getId()){
			finish();
		}else if(v.getId() == bt_tiwen.getId()){
			Intent intent = new Intent(LsDarenDetailActivity.this, LsDarenTiwenActivity.class);
			intent.putExtra("id", ddb.getUser().getUser_id());
			intent.putExtra("nickname", ddb.getUser().getNickname());
			startActivity(intent);
		}else if(v.getId() == ll_huida.getId()){
			try{
				if(Integer.parseInt(ddb.getHuida_num())>0){
					Intent intent = new Intent(LsDarenDetailActivity.this, LsDarenHuidaActivity.class);
					intent.putExtra("id", ddb.getUser().getUser_id());
					startActivity(intent);
				}
			}catch(Exception e){
				
			}
			
		}else if(v.getId() == ll_shaitu.getId()){
			try{
				if(Integer.parseInt(ddb.getShaitu_num())>0){
					Intent intent = new Intent(this, LsUserShaiActivity.class);
					intent.putExtra("id", ddb.getUser().getUser_id());
					startActivity(intent);
				}
			}catch(Exception e){
				
			}
			
		}else if(v.getId() == ll_guangzhu.getId()){
			postMessage(POPUP_PROGRESS,getString(R.string.sending));
			getGuanzhuList();
		}
	}

	private void getGuanzhuList() {
		String url = C.MAIN_FRIEND_URL;
		Task task = new Task(null, url, null, "USER__URL", this);
		task.setPostData(getGuanzhuParams().getBytes());
		publishTask(task, IEvent.IO);	
	}

	private String getGuanzhuParams() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("user_id", DataManager.getInstance().getUser().getUser_id());
		params.put("id", ddb.getUser().getUser_id());
		params.put("action", "add");
		return RequestParamUtil.getInstance(this).getRequestParams(params);
	}
}
