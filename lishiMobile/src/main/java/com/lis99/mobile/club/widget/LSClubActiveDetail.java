package com.lis99.mobile.club.widget;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.LSClubTopicInfoLocation;
import com.lis99.mobile.club.model.ClubTopicDetailInfoOther;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.MyRequestManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class LSClubActiveDetail extends LSBaseActivity implements android.view.View.OnClickListener{

	private TextView tv_date, tv_location, tv_leader, tv_tel, tv_join, tv_info;
	
	private LinearLayout layout;
	private TextView tv_content;
	
	private ImageView iv_location, iv_info;
	
	private Button btn_info, btn_route, btn_zb, btn_mz, btn_fy, btn_zy;
	private Button currentB;
	
	private Drawable drawable;
	
	private ClubTopicDetailInfoOther clubmobel;
	
	private String topic_id;
	
	DisplayImageOptions options;
	
	private RelativeLayout layoutLocation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		topic_id = getIntent().getStringExtra("topic_id");
		setContentView(R.layout.club_active_detail);
		initViews();
		setTitle("详细信息");
		options = ImageUtil.getclub_topic_imageOptions();
		clubmobel = new ClubTopicDetailInfoOther();
		
		getInfo();
	}
	
	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		super.initViews();
		drawable = getResources().getDrawable(R.drawable.grid_tab_bottom);

		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		
		btn_info = (Button) findViewById(R.id.btn_info);
		btn_route = (Button) findViewById(R.id.btn_route);
		btn_zb = (Button) findViewById(R.id.btn_zb);
		btn_mz = (Button) findViewById(R.id.btn_mz);
		btn_fy = (Button) findViewById(R.id.btn_fy);
		btn_zy = (Button) findViewById(R.id.btn_zy);
		layoutLocation = (RelativeLayout) findViewById(R.id.layoutLocation);
		
		currentB = btn_info;
		
		btn_info.setOnClickListener(this);
		btn_route.setOnClickListener(this);
		btn_zb.setOnClickListener(this);
		btn_mz.setOnClickListener(this);
		btn_fy.setOnClickListener(this);
		btn_zy.setOnClickListener(this);
		layoutLocation.setOnClickListener(this);
		

		tv_date = (TextView) findViewById(R.id.tv_date);
		tv_location = (TextView) findViewById(R.id.tv_location);
		tv_leader = (TextView) findViewById(R.id.tv_leader);
		tv_tel = (TextView) findViewById(R.id.tv_tel);
		tv_join = (TextView) findViewById(R.id.tv_join);
		tv_info = (TextView) findViewById(R.id.tv_info);
		
		
		layout = (LinearLayout) findViewById(R.id.layout);
		tv_content = (TextView) findViewById(R.id.tv_content);

		iv_location = (ImageView) findViewById(R.id.iv_location);
		iv_info = (ImageView) findViewById(R.id.iv_info);
		
		
		iv_location.setOnClickListener(ClickMap);
		layoutLocation.setOnClickListener(ClickMap);
		
	}
	
	OnClickListener ClickMap = new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub
			toMap();
		}
	};
	
	private void toMap ()
	{
		//跳转地图
		Intent intent = new Intent(LSClubActiveDetail.this, LSClubTopicInfoLocation.class);
		Double latitude = Common.string2Double(clubmobel.gaodeLatitude);
		Double longtitude = Common.string2Double(clubmobel.gaodeLongitude);
		if ( latitude == -1 || longtitude == -1 )
		{
			Common.toast("暂时没集合地图位置");
			return;
		}
		intent.putExtra("latitude", latitude);
		intent.putExtra("longtitude", longtitude);
		startActivity(intent);
	}
	
	@Override
	public void onClick(View arg0) {
		setBtn((Button) arg0);
		if ( arg0.getId() == R.id.btn_info )
		{
			layout.setVisibility(View.VISIBLE);
			tv_content.setVisibility(View.GONE);
		}
		else
		{
			layout.setVisibility(View.GONE);
			tv_content.setVisibility(View.VISIBLE);
		}
		if ( clubmobel == null ) return;
		switch ( arg0.getId() )
		{
		case R.id.btn_info:
			break;
		case R.id.btn_route:
			tv_content.setText(clubmobel.detailTrip);
			break;
		case R.id.btn_zb:
			tv_content.setText(clubmobel.equipAdvise);
			break;
		case R.id.btn_mz:
			tv_content.setText(clubmobel.disclaimer);
			break;
		case R.id.btn_fy:
			tv_content.setText(clubmobel.constDesc);
			break;
		case R.id.btn_zy:
			tv_content.setText(clubmobel.cateMatter);
			break;
		}
		// TODO Auto-generated method stub
		super.onClick(arg0);
	}
	
	private void setBtn ( Button nbutton )
	{
		if ( currentB == nbutton ) return;
		currentB.setTextColor(getResources().getColor(R.color.color_text));
		currentB.setCompoundDrawables(null, null, null, null);
		
		nbutton.setTextColor(getResources().getColor(R.color.text_color_blue));
		nbutton.setCompoundDrawables(null, null, null, drawable);
		
		currentB = nbutton;
		
	}
	
	private void getInfo ()
	{
		///1 表示android客户端， 反回经纬度android用的百度地图， 服务端需要转换
		MyRequestManager.getInstance().requestGet(C.CLUBPTOPIC_DETAIL_INFO_OTHER + topic_id + "/1", clubmobel, call);
	}
	
	private CallBack call = new CallBack()
	{
		
		@Override
		public void handler(MyTask mTask)
		{
			// TODO Auto-generated method stub
			clubmobel = (ClubTopicDetailInfoOther) mTask.getResultModel();
			tv_date.setText(clubmobel.setTime);
			tv_location.setText(clubmobel.setaddress);
			tv_leader.setText(clubmobel.leader);
			String contact = null;
			if ( !TextUtils.isEmpty(clubmobel.mobile) )
			{
				contact = "手机 " + clubmobel.mobile;
			}
			if ( !TextUtils.isEmpty(clubmobel.qq) )
			{
				contact += " QQ " + clubmobel.qq;
			}
			if ( !TextUtils.isEmpty(clubmobel.qqgroup))
			{
				contact += " QQ群 " + clubmobel.qqgroup;
			}
			tv_tel.setText(contact);
			tv_join.setText(clubmobel.activeNum +"人 (已报名 " + clubmobel.baomingNum + " 人)");
			tv_info.setText(clubmobel.activeDesc);
			if ( !TextUtils.isEmpty(clubmobel.activeFileUrl))
			{
				ImageUtil.setImageWidthAndHeight(iv_info, clubmobel.activeFileUrl, options);
//				ImageLoader.getInstance().displayImage(clubmobel.activeFileUrl, iv_info, options);
			}
			//zhuyi
			if ( TextUtils.isEmpty(clubmobel.cateMatter) )
			{
				btn_zy.setVisibility(View.INVISIBLE);
			}
			else
			{
				btn_zy.setVisibility(View.VISIBLE);
			}
		}
	};
	
}
