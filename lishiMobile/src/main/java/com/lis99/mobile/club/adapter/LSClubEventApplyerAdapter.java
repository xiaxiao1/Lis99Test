package com.lis99.mobile.club.adapter;

import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.LSClubApplyListActivity;
import com.lis99.mobile.club.model.ClubApplyManagerListModel;
import com.lis99.mobile.club.model.ClubApplyManagerListModel.ApplyList;
import com.lis99.mobile.club.model.ClubTopicApplyPass;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.DialogManager;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.SharedPreferencesHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

public class LSClubEventApplyerAdapter extends BaseAdapter {
	
	public ArrayList<ApplyList> list;
	
	public ClubApplyManagerListModel mobel;
	
	LayoutInflater inflater;

	ImageLoader imageLoader = ImageLoader.getInstance();
	DisplayImageOptions options;
	
	private LSClubApplyListActivity activity;
	
	
	private void buildOptions() {
		options = ImageUtil.getclub_topic_headImageOptions();
	}
	
	public LSClubEventApplyerAdapter(LSClubApplyListActivity activity, ClubApplyManagerListModel mobel){
		this.mobel = mobel;
		this.list = mobel.list;
		inflater = LayoutInflater.from(activity);
		buildOptions();
		this.activity = activity;
	}
	
	public void addList (ArrayList<ApplyList> list)
	{
		if ( list == null ) return;
		this.list.addAll(list);
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		return list == null ? null : list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.club_apply_item, null);
			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView.findViewById(R.id.roundedImageView1);
			holder.nickNameView = (TextView) convertView.findViewById(R.id.nickNameView);
			
			
			holder.iv_apply_state = (ImageView) convertView.findViewById(R.id.iv_apply_state);
			holder.dateView = (TextView) convertView.findViewById(R.id.dateView);
			holder.list_apply_info = (ListView) convertView.findViewById(R.id.list_apply_info);
			holder.btn_confirm = (Button) convertView.findViewById(R.id.btn_confirm);
			holder.btn_decline = (Button) convertView.findViewById(R.id.btn_decline);
			holder.vipStar = (ImageView) convertView.findViewById(R.id.vipStar);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
//		LSClubEventApplyer applyer = data.get(position);
		
		final ApplyList item = (ApplyList) getItem(position);
		
		imageLoader.displayImage(item.headicon, holder.imageView, options);
		holder.nickNameView.setText(item.nickname);
		//
		if ("1".equals(item.is_vip))
		{
			holder.vipStar.setVisibility(View.VISIBLE);
		}
		else
		{
			holder.vipStar.setVisibility(View.GONE);
		}
		//已通过
		if ( "1".equals(item.flag))
		{
			holder.iv_apply_state.setImageResource(R.drawable.club_apply_state_enter);
			holder.btn_confirm.setVisibility(View.GONE);
			holder.btn_decline.setVisibility(View.VISIBLE);
		}
//		待审核
		else if ( "2".equals(item.flag))
		{
			holder.iv_apply_state.setImageResource(R.drawable.club_apply_state_wait);
			holder.btn_confirm.setVisibility(View.VISIBLE);
			holder.btn_decline.setVisibility(View.VISIBLE);
		}
//		拒绝
		else if ( "-1".equals(item.flag) )
		{
			holder.iv_apply_state.setImageResource(R.drawable.club_apply_state_reject);
			holder.btn_decline.setVisibility(View.GONE);
			holder.btn_confirm.setVisibility(View.GONE);
		}
		
		holder.dateView.setText("报名时间：" + item.createdate);
		
		ArrayList<HashMap<String, String>> alist = getListInfo(item);
		
		LSClubapplyerInfoItemAdapter itemAdapter = new LSClubapplyerInfoItemAdapter(activity, alist);
		holder.list_apply_info.setAdapter(itemAdapter);
		
		holder.btn_confirm.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				activity.applyPass();
				applyPass(item);
			}
		});
		
		holder.btn_decline.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DialogManager.getInstance().startAlert(activity, "提示", "拒绝报名的操作不可撤销。确定要拒绝这个报名信息吗？", true, "拒绝报名", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						//拒绝请求
						applyReject(item);
//						activity.applyReject();
						
					}} , true, "取消", null);
			}
		});
		
		
		return convertView;
	}
	
	static class ViewHolder{
		//头像
		ImageView imageView;
		//昵称
		TextView nickNameView;
		//========
		ImageView iv_apply_state;
//		报名日期
		TextView dateView;
		ListView list_apply_info;
		//拒绝。确认
		Button btn_decline, btn_confirm;
		//VIP
		ImageView vipStar;
		
	}
	//获取报名者信息
	private ArrayList<HashMap<String, String>> getListInfo ( ApplyList item )
	{
		ArrayList<HashMap<String, String>> alist = new ArrayList<HashMap<String,String>>();
		
		HashMap<String, String> map = new HashMap<String, String>();
		if ( !TextUtils.isEmpty(item.names) )
		{
			String sex = item.sex;
			if ( TextUtils.isEmpty(sex))
			{
				sex = "(男)";
			}
			else
			{
				sex = "(" + sex + ")";
			}
			map.put("name", item.names);
			map.put("value", item.name + sex);
			alist.add(map);
		}
		if ( !TextUtils.isEmpty(item.credentialss) )
		{
			map = new HashMap<String, String>();
			map.put("name", item.credentialss);
			map.put("value", item.credentials);
			alist.add(map);
		}
		if ( !TextUtils.isEmpty(item.mobiles) )
		{
			map = new HashMap<String, String>();
			map.put("name", item.mobiles);
			map.put("value", item.mobile);
			alist.add(map);
		}
		if ( !TextUtils.isEmpty(item.phones) )
		{
			map = new HashMap<String, String>();
			map.put("name", item.phones);
			map.put("value", item.phone);
			alist.add(map);
		}
		
		if ( !TextUtils.isEmpty(item.qqs) )
		{
			map = new HashMap<String, String>();
			map.put("name", item.qqs);
			map.put("value", item.qq);
			alist.add(map);
		}
		
		if ( !TextUtils.isEmpty(item.willnums) )
		{
			map = new HashMap<String, String>();
			map.put("name", item.willnums);
			map.put("value", ""+item.willnum);
			alist.add(map);
		}
		
		if ( !TextUtils.isEmpty(item.costs) )
		{
			double d = Common.string2Double(item.cost);
			int num = Common.string2int(item.willnum);
			double result = d;
			if ( num != -1 )
			{
				result = d * num;
			}
			map = new HashMap<String, String>();
			map.put("name", item.costs);
			map.put("value", ""+result);
			alist.add(map);
		}
		
		if ( !TextUtils.isEmpty(item.postaladdresss) )
		{
			map = new HashMap<String, String>();
			map.put("name", item.postaladdresss);
			map.put("value", item.postaladdress);
			alist.add(map);
		}
		
		if ( !TextUtils.isEmpty(item.addresss) )
		{
			map = new HashMap<String, String>();
			map.put("name", item.addresss);
			map.put("value", item.address);
			alist.add(map);
		}
		
		return alist;
	}
	private ClubTopicApplyPass mobelpass;
	private void applyPass ( final ApplyList item )
	{
		String userId = DataManager.getInstance().getUser().getUser_id();
		mobelpass = new ClubTopicApplyPass();
		String Url = C.CLUB_TOPIC_MANAGER_APPLY_PASS;
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("topicid", mobel.topic_id);
		map.put("club_id", mobel.club_id);
		map.put("user_id", userId);
		map.put("id", item.applyid);
		map.put("token", SharedPreferencesHelper.getLSToken());
		
		MyRequestManager.getInstance().requestPost(Url, map, mobelpass, new CallBack()
		{
			
			@Override
			public void handler(MyTask mTask)
			{
				// TODO Auto-generated method stub
//				Common.toast("通过");
//				holder.iv_apply_state.setImageResource(R.drawable.club_apply_state_enter);
//				holder.btn_confirm.setVisibility(View.GONE);
				item.flag = "1";
				LSClubEventApplyerAdapter.this.notifyDataSetChanged();
			}
		});
		
	}
	
	private ClubTopicApplyPass mobelreject;
	private void applyReject (final ApplyList item)
	{
		String userId = DataManager.getInstance().getUser().getUser_id();
		mobelreject = new ClubTopicApplyPass();
		String Url = C.CLUB_TOPIC_MANAGER_APPLY_REJECT;
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("topicid", mobel.topic_id);
		map.put("club_id", mobel.club_id);
		map.put("user_id", userId);
		map.put("id", item.applyid);
		map.put("token", SharedPreferencesHelper.getLSToken());
		
		MyRequestManager.getInstance().requestPost(Url, map, mobelreject, new CallBack()
		{
			
			@Override
			public void handler(MyTask mTask)
			{
				// TODO Auto-generated method stub
//				Common.toast("");
//				holder.iv_apply_state.setImageResource(R.drawable.club_apply_state_enter);
//				holder.btn_confirm.setVisibility(View.GONE);
				item.flag = "-1";
				LSClubEventApplyerAdapter.this.notifyDataSetChanged();
			}
		});
	}

}
