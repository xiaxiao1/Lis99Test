package com.lis99.mobile.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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
import com.lis99.mobile.application.cache.ImageCacheManager;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.apply.ApplyManager;
import com.lis99.mobile.club.model.BaseModel;
import com.lis99.mobile.club.model.ShareInterface;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.weibo.LsWeiboSina;
import com.lis99.mobile.weibo.LsWeiboTencent;
import com.lis99.mobile.weibo.LsWeiboWeixin;
import com.lis99.mobile.wxapi.WXEntryActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;
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

	public static ShareManager getInstance()
	{
		if (Instance == null)
			Instance = new ShareManager();
		return Instance;
	}

	/**
	 * 分享
	 * 
	 * @param a
	 * @param title
	 *            分享内容
	 * @param Content_url
	 *            分享的url地址
	 * @param Image_Url
	 *            分享图片地址
	 */
	public void showShareList(final Activity a, final String title,
			final String Content_url, final String Image_Url)
	{

		final Tencent tencent = Tencent.createInstance(C.TENCENT_APP_ID, a);

		final IWXAPIEventHandler handler = new IWXAPIEventHandler()
		{

			@Override
			public void onResp(BaseResp arg0)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void onReq(BaseReq arg0)
			{
				// TODO Auto-generated method stub

			}
		};

		DialogManager.getInstance().showDialogList(a, "分享到",
				R.array.share_items, new DialogInterface.OnClickListener()
				{

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						switch (which)
						{
							case 0:
								String shareSinaText = "推荐个好东西给大家［" + title
										+ "］" + Content_url
										+ "－分享自@砾石 Android版";
								try
								{
									ComponentName cmp = new ComponentName(
											"com.sina.weibo",
											"com.sina.weibo.EditActivity");
									Intent intent = new Intent(
											Intent.ACTION_SEND);
									intent.setType("image/*");
									// intent.putExtra(Intent.EXTRA_SUBJECT,
									// "分享123");
									intent.putExtra(Intent.EXTRA_TEXT,
											shareSinaText);
									intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									intent.setComponent(cmp);
									a.startActivity(intent);
								} catch (Exception e)
								{
									// TODO: handle exception
									// Toast.makeText(getApplicationContext(),
									// "123123123231231231",
									// Toast.LENGTH_LONG).show();
									// 如果没有安装客户端， 调用内置SDK分享
									LsWeiboSina.getInstance(a).share(
											shareSinaText);
								}

								break;
							case 1:
								String shareWx1Text = Content_url;
								String title1 = title;
								String desc1 = "分享个好东西给你【" + title + "】";
								Bitmap bmp1 = ImageCacheManager.getInstance()
										.getBitmapFromCache(Image_Url);
								LsWeiboWeixin.getInstance(a).getApi()
										.handleIntent(a.getIntent(), handler);
								LsWeiboWeixin.getInstance(a).share1(
										shareWx1Text, title1, desc1, Image_Url,
										SendMessageToWX.Req.WXSceneSession);
								break;
							case 2:
								String shareWx2Text = Content_url;
								String title2 = title;
								String desc2 = "分享＃装备＃【" + title + "】";
								Bitmap bmp2 = ImageCacheManager.getInstance()
										.getBitmapFromCache(Image_Url);
								LsWeiboWeixin.getInstance(a).getApi()
										.handleIntent(a.getIntent(), handler);
								LsWeiboWeixin.getInstance(a).share1(
										shareWx2Text, title2, desc2, Image_Url,
										SendMessageToWX.Req.WXSceneTimeline);
								break;
							case 3:
								final Bundle params = new Bundle();
								params.putString(QzoneShare.SHARE_TO_QQ_TITLE,
										title);
								params.putString(
										QzoneShare.SHARE_TO_QQ_SUMMARY,
										"砾石上看到［" + title + "］不错，推荐给大家。");
								params.putString(
										QzoneShare.SHARE_TO_QQ_TARGET_URL,
										Content_url);
								ArrayList<String> imageUrls = new ArrayList<String>();
								// 图片URL
								// if ( null != Image_Url
								// ){imageUrls.add(Image_Url);}

								params.putStringArrayList(
										QzoneShare.SHARE_TO_QQ_IMAGE_URL,
										imageUrls);
								// 支持传多个imageUrl
								tencent.shareToQzone(a, params,
										new IUiListener()
										{

											@Override
											public void onCancel()
											{
												Util2.toastMessage(a,
														"onCancel: ");
											}

											@Override
											public void onComplete(
													Object response)
											{
												// TODO Auto-generated method
												// stub
												Util2.toastMessage(a,
														"onComplete: ");
											}

											@Override
											public void onError(UiError e)
											{
												// TODO Auto-generated method
												// stub
												Util2.toastMessage(
														a,
														"onComplete: "
																+ e.errorMessage,
														"e");
											}

										});
								break;
							case 4:
								String shareWx4Text = "推荐个好东西给大家【" + title
										+ "】" + Content_url
										+ "－分享自@砾石 Android版";
								LsWeiboTencent.getInstance(a).share(
										shareWx4Text);
								break;
						}
					}
				});
	}

	/** http://club.lis99.com/actives/detail/帖子id */
	//俱乐部用到
	private static String shareUrl = "http://club.lis99.com/actives/detail/";
	private static String shareText = "砾石，最好玩儿的户外运动社区，我的户外大本营";
	private static PopupWindow pop;

	public PopupWindow showPopWindowInShare(
			final ShareInterface clubhead, final String clubId,
			final String Image_Url, final String title, final String shareTxt,
			final String topicId, boolean b, View parent,
			final CallBack listener )
	{
		return showPopWindowInShare(clubhead, clubId, Image_Url, title, "", topicId, b, parent, listener, "" );
	}

	/**
	 * 			网页分享调用
	 * @param title
	 * @param content
	 * @param image_url
	 * @param url
	 * @param layout_main
	 * @return
	 */
	public PopupWindow showPopWindoInWeb ( String title, String content, String image_url, String url, View layout_main )
	{
		return showPopWindowInShare(null, "",
				image_url, title, content,
				"", false, layout_main, null, url);
	}

	/**
	 * 分享 需要在界面关闭的时候， 调用dismiss
	 ** 			分享popwindow
	 * @param clubhead			帖子对象
	 * @param clubId			俱乐部Id（管理帖子用到， 删除，置顶）
	 * @param Image_Url			图片的Url地址
	 * @param title				展示的标题
	 * @param shareTxt			分享文本
	 * @param topicId			帖子Id
	 * @param b					是否展示管理选项
	 * @param parent			父View
	 * @param listener			点击监听， 管理时用到, 删除，置顶
	 */
	public PopupWindow showPopWindowInShare(
			final ShareInterface clubhead, final String clubId,
			final String Image_Url, final String title, String shareTxt,
			final String topicId, boolean b, View parent,
			final CallBack listener, String sharedUrl ) {
		if (pop != null && pop.isShowing()) {
			pop.dismiss();
			return pop;
		}

		if (TextUtils.isEmpty(sharedUrl))
		{
			sharedUrl = shareUrl;
		}

//		String content = shareTxt;

		if ( TextUtils.isEmpty(shareTxt) )
		{
			shareTxt = this.shareText;
		}


		Common.log("activity=" + LSBaseActivity.activity.getClass().getName());

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
		if (clubhead != null)
		{
			if ("2".equals(clubhead.getStick()))
			{
				iv_top.setImageResource(R.drawable.top);
				tv_top.setText("置顶");
			} else
			{
				iv_top.setImageResource(R.drawable.remove);
				tv_top.setText("解除");
			}
		}

		LinearLayout layoutmanager = (LinearLayout) view
				.findViewById(R.id.layoutmanager);
		if (b)
		{
			layoutmanager.setVisibility(View.VISIBLE);
		} else
		{
			layoutmanager.setVisibility(View.GONE);
		}

		//话题贴不显示
		if ( clubhead != null && "1".equals(clubhead.getCategory()))
		{
			iv_manager_apply.setVisibility(View.VISIBLE);
		}
		else
		{
			iv_manager_apply.setVisibility(View.INVISIBLE);
		}

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
						String shareSinaText = title + finalSharedUrl + "" + topicId
								+ shareText;
						Bitmap bitmap = ImageLoader.getInstance().loadImageSync(Image_Url);
						LsWeiboSina.getInstance(LSBaseActivity.activity).share(
								shareSinaText, bitmap, listener);

						break;
					case R.id.iv_qzone:

						String shareWx4Text = shareText; //+" "+ finalSharedUrl + ""
						QQZoneUtil.getInstance().setCallBack(listener);
						QQZoneUtil.getInstance().sendQQZone(
								LSBaseActivity.activity, title, shareWx4Text,
								finalSharedUrl + topicId, Image_Url);
						break;
					case R.id.iv_wechat:
						state = wechat;
						WXEntryActivity.callBack = listener;
						String shareWx1Text = finalSharedUrl + topicId;
						String title1 = title;
						String desc1 = shareText;// + finalSharedUrl + "" + topicId;
						LsWeiboWeixin.getInstance(LSBaseActivity.activity)
								.share1(shareWx1Text, title1, desc1, Image_Url,
										SendMessageToWX.Req.WXSceneSession);
						break;
					case R.id.iv_friend:
						WXEntryActivity.callBack = listener;
						state = wechat_friends;
						String shareWx2Text = finalSharedUrl + topicId;
						String title2 = title;
						String desc2 = shareText;// + finalSharedUrl + "" + topicId;
						LsWeiboWeixin.getInstance(LSBaseActivity.activity)
								.share1(shareWx2Text, title2, desc2, Image_Url,
										SendMessageToWX.Req.WXSceneTimeline);
						break;
					case R.id.iv_delete:
						LSRequestManager.getInstance().mClubTopicReplyDelete(
								clubId, topicId, new CallBack()
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
						if ("2".equals(clubhead.getStick()))
						{
							topTopic(clubId, topicId, new CallBack()
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
						} else
						{
							cancelTopTopic(clubId, topicId, new CallBack()
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
						Intent i = new Intent(LSBaseActivity.activity, ApplyManager.class);
						i.putExtra("topicID", Common.string2int(topicId));
						i.putExtra("clubID", Common.string2int(clubId));
						LSBaseActivity.activity.startActivity(i);
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

	public void share2Weichat ( String topicId, String Image_Url, String title, CallBack callBack )
	{
		WXEntryActivity.callBack = callBack;
		String shareWx1Text = shareUrl + topicId;
		String title1 = title;
		String desc1 = shareText;// + finalSharedUrl + "" + topicId;
		LsWeiboWeixin.getInstance(LSBaseActivity.activity)
				.share1(shareWx1Text, title1, desc1, Image_Url,
						SendMessageToWX.Req.WXSceneSession);
	}

	public void share2Friend ( String topicId, String Image_Url, String title, CallBack callBack )
	{
		WXEntryActivity.callBack = callBack;
		state = wechat_friends;
		String shareWx2Text = shareUrl + topicId;
		String title2 = title;
		String desc2 = shareText;// + finalSharedUrl + "" + topicId;
		LsWeiboWeixin.getInstance(LSBaseActivity.activity)
				.share1(shareWx2Text, title2, desc2, Image_Url,
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
