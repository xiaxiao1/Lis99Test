package com.lis99.mobile.util;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.model.BaseModel;
import com.lis99.mobile.club.model.ShareInterface;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.weibo.LsWeiboSina;
import com.lis99.mobile.weibo.LsWeiboWeixin;
import com.lis99.mobile.wxapi.WXEntryActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

import java.util.HashMap;

/**
 * 分享类
 * 
 * @author Administrator
 * 
 */
public class ShareManager
{
//	朋友
	public static final int wechat = 0;
//	朋友圈
	public static final int wechat_friends = 1;

	public static int state = wechat;

	private static ShareManager Instance;

	public static String topicid;

	public static ShareManager getInstance()
	{
		if (Instance == null)
			Instance = new ShareManager();
		return Instance;
	}

	/** http://club.lis99.com/actives/detail/帖子id */
	//俱乐部用到
	private static String shareUrlMain = "http://club.lis99.com/actives/detail/";
	private static String shareTextMain = "砾石，最好玩儿的户外运动社区，我的户外大本营";
	private static PopupWindow pop;

//	public PopupWindow showPopWindowInShare(
//			final ShareInterface clubhead, View parent,
//			final CallBack listener )
//	{
//		return showPopWindowInShare(clubhead, clubhead.getClubId(), clubhead.getImageUrl(), clubhead.getTitle(), clubhead.getShareTxt(), clubhead.getTopicId(), parent, listener, "" );
//	}

	/**
	 * 			网页分享调用
	 * @param layout_main
	 * @return
	 */
	public PopupWindow showPopWindoInWeb ( ShareInterface share, View layout_main )
	{
		return showPopWindowInShare(share, layout_main, null);
	}

	/**
	 * 分享 需要在界面关闭的时候， 调用dismiss
	 ** 			分享popwindow
	 * @param clubhead			帖子对象
	 * @param parent			父View
	 * @param listener			点击监听， 管理时用到, 删除，置顶
	 */
	public PopupWindow showPopWindowInShare(
			final ShareInterface clubhead, View parent,
			final CallBack listener ) {
		if (pop != null && pop.isShowing()) {
			pop.dismiss();
			return pop;
		}

		String sharedUrl = clubhead.getShareUrl();

		if (TextUtils.isEmpty(sharedUrl))
		{
			sharedUrl = shareUrlMain;
		}

//		String content = shareTxt;
		String shareTxt1 = clubhead.getShareTxt();

		if ( TextUtils.isEmpty(shareTxt1) )
		{
			shareTxt1 = this.shareTextMain;
		}

//		this.topicid = clubhead.getTopicId();

//		Common.log("activity=" + LSBaseActivity.activity.getClass().getName());

		// final Tencent tencent = Tencent.createInstance(C.TENCENT_APP_ID,
		// LSBaseActivity.activity);

		final IWXAPIEventHandler handler = new IWXAPIEventHandler() {

			@Override
			public void onResp(BaseResp arg0) {
				// TODO Auto-generated method stub
				Common.log("BaseResp = "+arg0.transaction);
			}

			@Override
			public void onReq(BaseReq arg0) {
				// TODO Auto-generated method stub
				Common.log("BaseReq = "+arg0.getType());
			}
		};

		View view = View.inflate(LSBaseActivity.activity,
				R.layout.club_topic_share, null);
		Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
		TextView iv_sina = (TextView) view.findViewById(R.id.iv_sina);
		TextView iv_qzone = (TextView) view.findViewById(R.id.iv_qzone);
		TextView iv_wechat = (TextView) view.findViewById(R.id.iv_wechat);
		TextView iv_friend = (TextView) view.findViewById(R.id.iv_friend);
		TextView iv_delete = (TextView) view.findViewById(R.id.iv_delete);
		final ImageView iv_top = (ImageView) view.findViewById(R.id.iv_top);
		final TextView tv_top = (TextView) view.findViewById(R.id.tv_top);
		final TextView iv_manager_apply = (TextView) view.findViewById(R.id.iv_manager_apply);
		LinearLayout layoutmanager = (LinearLayout) view
				.findViewById(R.id.layoutmanager);

		View layoutmanager1 = view
				.findViewById(R.id.layoutmanager1);

		final TextView iv_manager_apply1 = (TextView) view.findViewById(R.id.iv_manager_apply1);


		layoutmanager.setVisibility(View.GONE);
		layoutmanager1.setVisibility(View.GONE);

		if (clubhead != null)
		{

			//			显示删除, 置顶， 管理菜单  1 只有创始人, 新版活动帖没有
			if ( Common.clubDelete(clubhead.getIsJoin()) && TextUtils.isEmpty(clubhead.getNewActive()))
			{
				layoutmanager.setVisibility(View.VISIBLE);
//				置顶
				if ("2".equals(clubhead.getStick()))
				{
					iv_top.setImageResource(R.drawable.top);
					tv_top.setText("置顶");
				} else
				{
					iv_top.setImageResource(R.drawable.remove);
					tv_top.setText("解除");
				}
//				是否为活动帖
				if ( Common.visibleApplyManager(clubhead))
				{
					iv_manager_apply.setVisibility(View.VISIBLE);
				}
				else
				{
					iv_manager_apply.setVisibility(View.INVISIBLE);
				}

			}
			else
			{
				//  隐藏置顶
				layoutmanager.setVisibility(View.GONE);
//				管理员只显示 ， 管理报名, 创始人管理报名
				if ( "2".equals(clubhead.getIsJoin()) || "1".equals(clubhead.getIsJoin()) )
				{
					//话题贴不显示 1,线下， 2线上活动, 新版//				是否为活动帖
					if (Common.visibleApplyManager(clubhead))
					{
						layoutmanager1.setVisibility(View.VISIBLE);
					}
					else
					{
						layoutmanager1.setVisibility(View.GONE);
					}
				}
			}

		}
		final String shareText = shareTxt1;
		final String title = clubhead.getTitle();
		final String finalSharedUrl = sharedUrl;
		OnClickListener click = new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				switch (v.getId())
				{
					case R.id.btn_cancel:
						pop.dismiss();
						break;
					case R.id.iv_sina:

						String shareSinaText = title + finalSharedUrl + shareText;

						Bitmap bitmap = ImageLoader.getInstance().loadImageSync(clubhead.getImageUrl());
						LsWeiboSina.getInstance(LSBaseActivity.activity).share(
								shareSinaText, bitmap, listener);
						Common.toast("正在分享中...");

						break;
					case R.id.iv_qzone:

						String shareWx4Text = shareText; //+" "+ finalSharedUrl + ""
						QQZoneUtil.getInstance().setCallBack(listener);

						String shareQQText = finalSharedUrl;

						QQZoneUtil.getInstance().sendQQZone(
								LSBaseActivity.activity, title, shareWx4Text,
								shareQQText, clubhead.getImageUrl());
						break;
					case R.id.iv_wechat:
						state = wechat;
						WXEntryActivity.callBack = listener;
						String shareWx1Text = finalSharedUrl;

						String title1 = title;
						String desc1 = shareText;// + finalSharedUrl + "" + topicId;
						LsWeiboWeixin.getInstance(LSBaseActivity.activity)
								.share1(shareWx1Text, title1, desc1, clubhead.getImageUrl(),
										SendMessageToWX.Req.WXSceneSession);
						break;
					case R.id.iv_friend:
						WXEntryActivity.callBack = listener;
						state = wechat_friends;
						String shareWx2Text = finalSharedUrl;

						String title2 = title;
						String desc2 = shareText;// + finalSharedUrl + "" + topicId;
						LsWeiboWeixin.getInstance(LSBaseActivity.activity)
								.share1(shareWx2Text, title2, desc2, clubhead.getImageUrl(),
										SendMessageToWX.Req.WXSceneTimeline);
						break;
					case R.id.iv_delete:
						LSRequestManager.getInstance().mClubTopicReplyDelete(
								clubhead.getClubId(), clubhead.getTopicId(), new CallBack()
								{

									@Override
									public void handler(MyTask mTask)
									{
										// TODO Auto-generated method stub
										pop.dismiss();
										mTask.result = "deleteOk";
										listener.handler(mTask);
									}

								});
						break;
					case R.id.iv_top:
						if ( clubhead == null )
						{
							Common.toast("置顶数据获取失败");
							return;
						}
						if ("2".equals(clubhead.getStick()))
						{
							topTopic(clubhead.getClubId(), clubhead.getTopicId(), new CallBack()
							{

								@Override
								public void handler(MyTask mTask)
								{
									// TODO Auto-generated method stub
									Common.toast("置顶成功");
//									clubhead.stick = "1";
									clubhead.setStick("1");
									mTask.result = "topTopic";
									listener.handler(mTask);
									iv_top.setImageResource(R.drawable.remove);
									tv_top.setText("解除");
								}
							});
						}
							else
						{
							cancelTopTopic(clubhead.getClubId(), clubhead.getTopicId(), new CallBack()
							{

								@Override
								public void handler(MyTask mTask)
								{
									// TODO Auto-generated method stub
									Common.toast("取消置顶成功");
//									clubhead.stick = "2";
									clubhead.setStick("2");
									iv_top.setImageResource(R.drawable.top);
									tv_top.setText("置顶");
									mTask.result = "topTopic";
									listener.handler(mTask);
								}
							});
						}
						break;
					case R.id.iv_manager_apply:
					case R.id.iv_manager_apply1:

						ActivityUtil.goActiveManager(Common.string2int(clubhead.getTopicId()), Common.string2int(clubhead.getClubId()));
////						系列活动
//						if ( "999".equals(clubhead.getCategory()))
//						{
//							Intent i = new Intent(LSBaseActivity.activity, ApplyManagerSeries.class);
//							i.putExtra("topicID", Common.string2int(clubhead.getTopicId()));
//							i.putExtra("clubID", Common.string2int(clubhead.getClubId()));
//							i.putExtra("NEWACTIVE", !TextUtils.isEmpty(clubhead.getNewActive()));
//
//							LSBaseActivity.activity.startActivity(i);
//						}
//						else
//						{
//							Intent i = new Intent(LSBaseActivity.activity, ApplyManager.class);
//							i.putExtra("topicID", Common.string2int(clubhead.getTopicId()));
//							i.putExtra("clubID", Common.string2int(clubhead.getClubId()));
//							i.putExtra("NEWACTIVE", !TextUtils.isEmpty(clubhead.getNewActive()));
//
//							LSBaseActivity.activity.startActivity(i);
//						}

						break;
				}
				// TODO Auto-generated method stub
				// listener.onSuccess(null, 0, ((Button)
				// v).getText().toString());
				// pop.dismiss();
			}
		};
		btn_cancel.setOnClickListener(click);
		iv_sina.setOnClickListener(click);
		iv_qzone.setOnClickListener(click);
		iv_wechat.setOnClickListener(click);
		iv_friend.setOnClickListener(click);
		iv_delete.setOnClickListener(click);
		iv_top.setOnClickListener(click);
		iv_manager_apply.setOnClickListener(click);
		iv_manager_apply1.setOnClickListener(click);

		pop = new PopupWindow(view, LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		pop.setOutsideTouchable(true);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		// pop.showAsDropDown(parent);
		pop.showAtLocation(parent, Gravity.BOTTOM, 0, 0);

		pop.setOnDismissListener(new OnDismissListener()
		{

			@Override
			public void onDismiss()
			{
				// TODO Auto-generated method stub
				WindowManager.LayoutParams lp = LSBaseActivity.activity
						.getWindow().getAttributes();
				lp.alpha = 1.0f;
				LSBaseActivity.activity.getWindow().setAttributes(lp);
			}
		});

		WindowManager.LayoutParams lp = LSBaseActivity.activity.getWindow()
				.getAttributes();
		lp.alpha = 0.5f;
		LSBaseActivity.activity.getWindow().setAttributes(lp);

		return pop;
	}

	public void share2Weichat ( ShareInterface share, CallBack callBack )
	{
		WXEntryActivity.callBack = callBack;

		String shareWx1Text = share.getShareUrl();



		String title1 = share.getTitle();
		String desc1 = share.getShareTxt();

		if ( TextUtils.isEmpty(desc1) )
		{
			desc1 = this.shareTextMain;
		}

		LsWeiboWeixin.getInstance(LSBaseActivity.activity)
				.share1(shareWx1Text, title1, desc1, share.getImageUrl(),
						SendMessageToWX.Req.WXSceneSession);
	}

	public void share2Friend ( ShareInterface share, CallBack callBack )
	{
		WXEntryActivity.callBack = callBack;
		state = wechat_friends;
		String shareWx2Text = share.getShareUrl();

		String title2 = share.getTitle();
		String desc2 = share.getShareTxt();

		if ( TextUtils.isEmpty(desc2) )
		{
			desc2 = this.shareTextMain;
		}

//		title2 = "地地人人地人夫地人夫地会人地地人人地人夫地人夫地会人地地人人地人夫地人夫地会人地地人人地人夫地人夫地会人地地人人地人夫地人夫地会人地地人人地人夫地人夫地会人地地人人地人夫地人夫地会人";

		LsWeiboWeixin.getInstance(LSBaseActivity.activity)
				.share1(shareWx2Text, title2, desc2, share.getImageUrl(),
						SendMessageToWX.Req.WXSceneTimeline);
	}

	/***
	 * 置顶
	 * 
	 * @param clubId
	 * @param topicId
	 */
	private void topTopic(String clubId, String topicId, CallBack call)
	{
		BaseModel model = new BaseModel();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("topicid", topicId);
		map.put("club_id", clubId);
		map.put("user_id", DataManager.getInstance().getUser().getUser_id());
		map.put("token", SharedPreferencesHelper.getLSToken());
		MyRequestManager.getInstance().requestPost(C.CLUB_TOPIC_INFO_TOP, map,
				model, call);
	}

	private void cancelTopTopic(String clubId, String topicId, CallBack call)
	{
		BaseModel model = new BaseModel();
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("topicid", topicId);
		map.put("club_id", clubId);
		map.put("user_id", DataManager.getInstance().getUser().getUser_id());
		map.put("token", SharedPreferencesHelper.getLSToken());
		MyRequestManager.getInstance().requestPost(C.CLUB_TOPIC_INFO_CANCELTOP,
				map, model, call);
	}

}
