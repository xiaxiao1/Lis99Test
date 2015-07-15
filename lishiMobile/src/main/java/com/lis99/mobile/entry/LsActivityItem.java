package com.lis99.mobile.entry;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.myactivty.BaseInfoBean;
import com.lis99.mobile.myactivty.ItemInfoBean;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.SharedPreferencesHelper;
import com.lis99.mobile.util.StatusUtil;
import com.lis99.mobile.util.Util2;
import com.lis99.mobile.util.Utility;
import com.lis99.mobile.weibo.LsWeiboSina;
import com.lis99.mobile.weibo.LsWeiboTencent;
import com.lis99.mobile.weibo.LsWeiboWeixin;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler.Response;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.constant.WBConstants;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;
import java.util.List;

public class LsActivityItem extends ActivityPattern1 implements
		IWXAPIEventHandler {

	private Button bt_hd, bt_jg, bt_bj, bt_xa, bt_sy, bt_cc;
	private ImageView iv_back, iv_share, iv_header;
	private TextView wv_content;
	private ListView mlistview;
	private String nurl = "http://www.lis99.com/yyyguide.php";
	IWeiboShareAPI mWeiboShareAPI;
	// IWXAPI 是第三方app和微信通信的openapi接口
	private IWXAPI api;
	public static String mAppid;
	private Tencent tencent;
	private int weixin = 1;
	ItemInfoAdapter adapter;
	private int id = 1;
	private String[] areas = new String[] { "52", "311", "244", "211" };
	
	Handler handler;

	private static final int SHOW_EEROR = 213;
	private static final int SHOW_BASE_INFO = 214;
	private static final int SHOW_ITEM_INFO = 215;

	private void initWeibo(Bundle savedInstanceState) {
		// 创建微博 SDK 接口实例
		mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, C.SINA_APP_KEY);
		if (savedInstanceState != null) {
			mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
		}
		mTencent = Tencent.createInstance(C.TENCENT_APP_ID,
				this.getApplicationContext());
		mTencent.setOpenId(SharedPreferencesHelper.getValue(this,
				C.CONFIG_FILENAME, Context.MODE_PRIVATE, C.TENCENT_OPEN_ID));
		String expire = SharedPreferencesHelper.getValue(this,
				C.CONFIG_FILENAME, Context.MODE_PRIVATE, C.TENCENT_EXPIRES_IN);
		if (expire == null || "".equals(expire)) {
			expire = "0";
		}
		mTencent.setAccessToken(SharedPreferencesHelper
				.getValue(this, C.CONFIG_FILENAME, Context.MODE_PRIVATE,
						C.TENCENT_ACCESS_TOKEN), expire);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
//		mWeiboShareAPI.handleWeiboResponse(intent, wh);
//		setIntent(intent);
//		api.handleIntent(intent, this);
		// 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
		// 来接收微博客户端返回的数据；执行成功，返回 true，并调用
		// {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
	}

	protected ImageLoader imageLoader = ImageLoader.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xxl_activity_item);

		StatusUtil.setStatusBar(this);

		handler = new Handler();
		
//		imageLoader.init(ImageLoaderConfiguration.createDefault(this));

		initWeibo(savedInstanceState);
		tencent = Tencent.createInstance(C.TENCENT_APP_ID, this);
		api = WXAPIFactory.createWXAPI(this, C.WEIXIN_APP_ID, true);
		api.registerApp(C.WEIXIN_APP_ID);
		api.handleIntent(getIntent(), this);

		setview();
		setlistener();
		changeColor(currentBtn);
		getActivityList(id);
		getActivityLists(currentBtn);
	}

	int offset = 0;
	int limit = 10;
	int currentBtn;

	private void getActivityLists(int position) {
		postMessage(POPUP_PROGRESS,getString(R.string.sending));
		String url = C.ACTIVITY_ITEM_URL + "?areaid=" + areas[position]
				+ "&offset=" + offset + "&limit=" + limit;
		Task task = new Task(null, url, null, "ACTIVITY_ITEM_URL", this);
		Log.d("liuzb", "liuzb url = " + url);
		publishTask(task, IEvent.IO);
	}

	private void getActivityList(int id) {
		String url = C.ACTIVITY_XXL_URL + "?id=" + id;
		Task task = new Task(null, url, null, "ACTIVITY_XXL_URL", this);
		publishTask(task, IEvent.IO);
	}

	@Override
	public void handleTask(int initiator, Task task, int operation) {
		super.handleTask(initiator, task, operation);
		String result;
		switch (initiator) {
		case IEvent.IO:
			if (task.getData() instanceof byte[]) {
				result = new String((byte[]) task.getData());
				if (((String) task.getParameter()).equals("ACTIVITY_XXL_URL")) {
					parserBaseInfoList(result);
				} else if (((String) task.getParameter())
						.equals("ACTIVITY_ITEM_URL")) {
					parserItemInfoList(result);
				}
			}
			break;
		default:
			break;
		}
	}

	List<BaseInfoBean> list = new ArrayList<BaseInfoBean>();

	private void parserBaseInfoList(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if ("OK".equals(result)) {
				list = (List<BaseInfoBean>) DataManager.getInstance()
						.jsonParse(params, DataManager.TYPE_BASE_INFO);

				Log.i("aa", list.toString());

				postMessage(SHOW_BASE_INFO);
			}
		}
	}

	List<ItemInfoBean> list2 = new ArrayList<ItemInfoBean>();
	List<ItemInfoBean> temp = new ArrayList<ItemInfoBean>();

	private void parserItemInfoList(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if ("OK".equals(result)) {
				temp = ((List<ItemInfoBean>) DataManager.getInstance()
						.jsonParse(params, DataManager.TYPE_ITEM_INFO));
				if (temp != null && temp.size() == 0) {
					handler.post(new Runnable() {
						@Override
						public void run() {
							tv_more.setText("没有更多了");
						}
					});
					
				} else {
					handler.post(new Runnable() {
						@Override
						public void run() {
							tv_more.setText("点击加载更多");
						}
					});
				}
				list2.addAll(temp);
//				for (int i = 0; i < temp.size(); i++) {
//					list2.add(temp.get(i));
//				}
//				offset = list2.size();
				postMessage(SHOW_ITEM_INFO);
			} else {
				postMessage(POPUP_TOAST, result);
				postMessage(SHOW_EEROR);
			}
		} 
		postMessage(DISMISS_PROGRESS);
	}

	@Override
	public boolean handleMessage(Message msg) {
		if (super.handleMessage(msg))
			return true;
		switch (msg.what) {
		case SHOW_BASE_INFO:
			if (list != null && list.size() > 0) {
				String url = list.get(0).getImage_url();
				setVisiableHeight();
				imageLoader.displayImage(url, iv_header, options);
				wv_content.setText("     " + list.get(0).getBase_info());
			}
			break;
		case SHOW_ITEM_INFO:
				offset++;
				adapter.notifyDataSetChanged();
				mlistview.post(new Runnable() {
					@Override
					public void run() {
						Utility.setListViewHeightBasedOnChildren(mlistview);
					}
				});
			break;
		case SHOW_EEROR:
			postMessage(DISMISS_PROGRESS);
			break;
		}
		return true;
	}

	private void setVisiableHeight() {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

		LinearLayout.LayoutParams iv = (LinearLayout.LayoutParams) iv_header
				.getLayoutParams();
		iv.height = displayMetrics.widthPixels / 3;
		iv_header.setLayoutParams(iv);
	}

	private void setlistener() {
		iv_back.setOnClickListener(this);
		bt_hd.setOnClickListener(this);
		bt_jg.setOnClickListener(this);
		bt_bj.setOnClickListener(this);
		bt_xa.setOnClickListener(this);
		bt_sy.setOnClickListener(this);
		bt_cc.setOnClickListener(this);
		iv_share.setOnClickListener(this);

		mlistview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(LsActivityItem.this,
						LsActivityLines.class);
				Bundle bundle = new Bundle();
				bundle.putInt("id", list2.get(position).getId());
				intent.putExtras(bundle);
				LsActivityItem.this.startActivity(intent);
			}
		});

	}

	TextView tv_more;
	SparseArray<Button> array=new SparseArray<Button>();
	private void setview() {
		bt_hd = (Button) findViewById(R.id.bt_hd);
		bt_jg = (Button) findViewById(R.id.bt_jg);
		bt_bj = (Button) findViewById(R.id.bt_bj);
		bt_xa = (Button) findViewById(R.id.bt_xa);
		bt_sy = (Button) findViewById(R.id.bt_sy);
		bt_cc = (Button) findViewById(R.id.bt_cc);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_share = (ImageView) findViewById(R.id.iv_share);
		mlistview = (ListView) findViewById(R.id.mlistview);
		iv_header = (ImageView) findViewById(R.id.iv_header_pic);
		wv_content = (TextView) findViewById(R.id.wv_content);
		
		array.put(0, bt_bj);
		array.put(1, bt_xa);
		array.put(2, bt_sy);
		array.put(3, bt_cc);
		adapter = new ItemInfoAdapter();
		mlistview.setAdapter(adapter);
		mlistview.setFocusable(false);
		tv_more = (TextView) findViewById(R.id.tv_more);
		tv_more.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == iv_back.getId()) {
			finish();
		} else if (v.getId() == bt_hd.getId()) {
			Intent intent = new Intent(this, LsActivityRules.class);
			Bundle b = new Bundle();
			String xurl = list.get(0).getFull_info();
			b.putString("xurl", xurl);
			intent.putExtras(b);
			startActivity(intent);
		} else if (v.getId() == bt_jg.getId()) {
			Intent intent = new Intent(this, LsActivityResult.class);
			startActivity(intent);
		} else if (v.getId() == bt_bj.getId()) {
			if (currentBtn != 0) {
				offset=0;
				list2.clear();
				changeColor(0);
				getActivityLists(0);
				currentBtn=0;
			}
		} else if (v.getId() == bt_xa.getId()) {
			if (currentBtn != 1) {
				offset=0;
				list2.clear();
				changeColor(1);
				getActivityLists(1);
				currentBtn=1;
			}

		} else if (v.getId() == bt_sy.getId()) {
			if (currentBtn != 2) {
				offset=0;
				changeColor(2);
				list2.clear();
				getActivityLists(2);
				currentBtn=2;
			}

		} else if (v.getId() == bt_cc.getId()) {
			if (currentBtn != 3) {
				offset=0;
				changeColor(3);
				list2.clear();
				getActivityLists(3);
				currentBtn=3;
			}

		} else if (v.getId() == iv_share.getId()) {
			showShareList();
		} else if (v.getId() == tv_more.getId()) {
			getActivityLists(currentBtn);
		}
	}
private void changeColor(int position){
	for(int i=0;i<array.size();i++){
		if(i==position){
			array.get(i).setBackgroundResource(R.drawable.button_press);
//			array.get(i).setBackgroundColor(getResources().getColor(R.color.blue));
			array.get(i).setTextColor(getResources().getColor(R.color.white));
		}else{
			array.get(i).setBackgroundColor(getResources().getColor(R.color.white));
			array.get(i).setTextColor(getResources().getColor(R.color.common_black));
		}
	}
}
	
	private static class ViewHolder {
		ImageView iv_toux;
		TextView tv_zan, tv_number, tv_detail, tv_name;
	}

	private class ItemInfoAdapter extends BaseAdapter {

		LayoutInflater inflater;

		public ItemInfoAdapter() {
			inflater = LayoutInflater.from(LsActivityItem.this);
		}

		@Override
		public int getCount() {
			return list2.size();
		}

		@Override
		public Object getItem(int position) {
			return list2.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.xxl_activity, null);
				holder = new ViewHolder();
				holder.tv_number = (TextView) convertView
						.findViewById(R.id.tv_number);
				holder.tv_detail = (TextView) convertView
						.findViewById(R.id.tv_detail);
				holder.tv_name = (TextView) convertView
						.findViewById(R.id.tv_name);
				holder.tv_zan = (TextView) convertView
						.findViewById(R.id.tv_zan);
				holder.iv_toux = (ImageView) convertView
						.findViewById(R.id.iv_toux);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			imageLoader.displayImage(list2.get(position).getUserinfo()
					.getHeadicon(), holder.iv_toux, options);
			holder.tv_number.setText("第" + (position + 1) + "位 ");
			holder.tv_detail.setText(list2.get(position).getTitle());
			holder.tv_name.setText(list2.get(position).getUserinfo()
					.getNickname());
			holder.tv_zan.setText("人气值  " + list2.get(position).getRenqizhi());
			return convertView;
		}
	}

	private void showShareList() {
		postMessage(POPUP_DIALOG_LIST, "分享到", R.array.share_items,
				new DialogInterface.OnClickListener() {
					private Bitmap bp;
					private String shareUrl = "http://m.lis99.com/activity/getTow";
					private String SinaWeiboShareUrl = "http://www.lis99.com/huodong/ruleDetail/";
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							// 新浪微博分享
							String shareText = "我在参加＃新雪丽基金领队评选＃活动，快来给我发起的活动点赞吧！活动链接是…… 你也可以来参加的哟！"
									+ SinaWeiboShareUrl;
							bp = BitmapFactory.decodeResource(getResources(),
									R.drawable.ls_activity_share_weibo);
							LsWeiboSina.getInstance(LsActivityItem.this)
									.shares(shareText, bp, 0);
//							LsWeiboSina.getInstance(LsActivityItem.this)
//							.share(shareText);
							break;
						case 1:
							// 微信好友分享
							bp = BitmapFactory.decodeResource(getResources(),
									R.drawable.ls_activity_share);
							String shareWx1Text = shareUrl;
							String title1 = "我在参加＃新雪丽基金领队评选＃活动，快来给我发起的活动点赞吧！活动链接是…… 你也可以来参加的哟！";
							String desc1 = "我在参加＃新雪丽基金领队评选＃活动，快来给我发起的活动点赞吧！活动链接是…… 你也可以来参加的哟！";
							LsWeiboWeixin
									.getInstance(LsActivityItem.this)
									.getApi()
									.handleIntent(getIntent(),
											LsActivityItem.this);
							LsWeiboWeixin.getInstance(LsActivityItem.this)
									.share(shareWx1Text, title1, desc1, bp,
											SendMessageToWX.Req.WXSceneSession);
							weixin = 2;
							break;
						case 2:
							// 微信朋友圈分享
							bp = BitmapFactory.decodeResource(getResources(),
									R.drawable.ls_activity_share);
							weixin = 3;
							String shareWx2Text =  shareUrl;
							String title2 = "我在参加＃新雪丽基金领队评选＃活动，快来给我发起的活动点赞吧！活动链接是…… 你也可以来参加的哟！";
							String desc2 = "我在参加＃新雪丽基金领队评选＃活动，快来给我发起的活动点赞吧！活动链接是…… 你也可以来参加的哟！";
							LsWeiboWeixin
									.getInstance(LsActivityItem.this)
									.getApi()
									.handleIntent(getIntent(),
											LsActivityItem.this);
							LsWeiboWeixin
									.getInstance(LsActivityItem.this)
									.share(shareWx2Text, title2, desc2, bp,
											SendMessageToWX.Req.WXSceneTimeline);

							break;
						case 3:
							final Bundle params = new Bundle();
							params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "摇一摇");
							params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY,
									"我在参加＃新雪丽基金领队评选＃活动，快来给我发起的活动点赞吧！活动链接是…… 你也可以来参加的哟！");
							params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL,
									shareUrl);
							ArrayList<String> imageUrls = new ArrayList<String>();
							params.putStringArrayList(
									QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
							tencent.shareToQzone(LsActivityItem.this, params,
									new IUiListener() {

										@Override
										public void onCancel() {
											Util2.toastMessage(
													LsActivityItem.this,
													"onCancel: ");
										}


										@Override
										public void onError(UiError e) {
											Util2.toastMessage(
													LsActivityItem.this,
													"onComplete: "
															+ e.errorMessage,
													"e");
										}

										@Override
										public void onComplete(Object arg0) {
											Util2.toastMessage(
													LsActivityItem.this,
													"分享成功");
											
										}
									});
							break;

						case 4:
							String shareWx4Text = "我在参加＃新雪丽基金领队评选＃活动，快来给我发起的活动点赞吧！活动链接是…… 你也可以来参加的哟！"
									+ shareUrl;
							LsWeiboTencent.getInstance(LsActivityItem.this)
									.share(shareWx4Text, bp);
							break;
						}
					}
				});
	}

	private Response wh = new Response() {
		@Override
		public void onResponse(BaseResponse arg0) {
			switch (arg0.errCode) {
			case WBConstants.ErrorCode.ERR_OK:
				Toast.makeText(LsActivityItem.this, "分享成功", Toast.LENGTH_LONG)
						.show();
				break;
			case WBConstants.ErrorCode.ERR_CANCEL:
				Toast.makeText(LsActivityItem.this, "取消分享", Toast.LENGTH_LONG)
						.show();
				break;
			case WBConstants.ErrorCode.ERR_FAIL:
				Toast.makeText(LsActivityItem.this,
						"分享失败" + "Error Message: " + arg0.errMsg,
						Toast.LENGTH_LONG).show();
				break;
			}
		}
	};

	@Override
	public void onReq(BaseReq req) {
		switch (req.getType()) {
		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
			System.out.println("COMMAND_GETMESSAGE_FROM_WX");
			break;
		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
			System.out.println("COMMAND_SHOWMESSAGE_FROM_WX");
			break;
		default:
			break;
		}
	}

	@Override
	public void onResp(BaseResp resp) {
		System.out.println("获取到微信消息了...");
		String result = "";

		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result = "发送成功";
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = "取消发送";
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = "AUTH_DENIED";
			break;
		default:
			result = "未知错误";
			break;
		}

		Toast.makeText(this, result, Toast.LENGTH_LONG).show();

	}
}
