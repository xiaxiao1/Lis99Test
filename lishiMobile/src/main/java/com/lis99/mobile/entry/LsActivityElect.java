package com.lis99.mobile.entry;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.myactivty.LingduiInfoBean;
import com.lis99.mobile.myactivty.LingduiLineBean;
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

public class LsActivityElect extends ActivityPattern1 implements
		IWXAPIEventHandler {

	private ImageView iv_back, iv_share, iv_header, iv_pic;
	private ListView mlistview;
	private TextView detail, name, zuobiao;
	IWeiboShareAPI mWeiboShareAPI;
	// IWXAPI 是第三方app和微信通信的openapi接口
	private IWXAPI api;
	public static String mAppid;
	private Tencent tencent;
	private String nurl = "http://www.lis99.com/yyyguide.php";

	private int weixin = 1;
	IineInfoAdapter adapter;

	private static final int SHOW_EEROR = 413;
	private static final int SHOW_LINGDUI_INFO = 414;
	private static final int SHOW_ITEM_INFO = 415;

	private void initWeibo(Bundle savedInstanceState) {
		// 创建微博 SDK 接口实例
		mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, C.SINA_APP_KEY);
		if (savedInstanceState != null) {
			mWeiboShareAPI.handleWeiboResponse(getIntent(), wh);
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
		mWeiboShareAPI.handleWeiboResponse(intent, wh);
		setIntent(intent);
		api.handleIntent(intent, this);
		// 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
		// 来接收微博客户端返回的数据；执行成功，返回 true，并调用
		// {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
	}

	protected ImageLoader imageLoader = ImageLoader.getInstance();

	private Bundle bundle;
	int uid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.xxl_leader_elect);

		StatusUtil.setStatusBar(this);

		initWeibo(savedInstanceState);
		tencent = Tencent.createInstance(C.TENCENT_APP_ID, this);
		api = WXAPIFactory.createWXAPI(this, C.WEIXIN_APP_ID, true);
		api.registerApp(C.WEIXIN_APP_ID);
		api.handleIntent(getIntent(), this);

		setview();
		setlistener();
		bundle = this.getIntent().getExtras();
		uid = bundle.getInt("uid");
		Log.i("aa", uid + "333");
		getLeaderInfo(uid);
		getLeaderLine(uid);
	}

	int offset = 1;

	private void getLeaderLine(int uid) {
		String url = C.ACTIVITY_LDLINE_URL + uid;
		Task task = new Task(null, url, null, "ACTIVITY_LDLINE_URL", this);
		publishTask(task, IEvent.IO);
	}

	private void getLeaderInfo(int uid) {
		String url = C.ACTIVITY_LINGDUI_URL + uid;
		Task task = new Task(null, url, null, "ACTIVITY_LINGDUI_URL", this);
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
				if (((String) task.getParameter())
						.equals("ACTIVITY_LINGDUI_URL")) {
					parserLeaderInfoList(result);
				} else if(((String) task.getParameter())
                                .equals("ACTIVITY_LDLINE_URL")){
                              parserLeaderLineList(result);
                        }
                        break;
                  }
		default:
			break;
		}
	}

	List<LingduiLineBean> lists = new ArrayList<LingduiLineBean>();

	private void parserLeaderLineList(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if ("OK".equals(result)) {
				lists = (List<LingduiLineBean>) DataManager.getInstance()
						.jsonParse(params, DataManager.TYPE_LDLINE_INFO);
				postMessage(SHOW_ITEM_INFO);
			} else {
				postMessage(POPUP_TOAST, result);
				postMessage(SHOW_EEROR);
			}
		} else {
			postMessage(DISMISS_PROGRESS);
		}

	}

	List<LingduiInfoBean> list = new ArrayList<LingduiInfoBean>();

	private void parserLeaderInfoList(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if ("OK".equals(result)) {
				list = (List<LingduiInfoBean>) DataManager.getInstance()
						.jsonParse(params, DataManager.TYPE_LINGDUI_INFO);
				postMessage(SHOW_LINGDUI_INFO);
			} else {
				postMessage(POPUP_TOAST, result);
				postMessage(SHOW_EEROR);
			}
		} else {
			postMessage(DISMISS_PROGRESS);
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		if (super.handleMessage(msg))
			return true;
		switch (msg.what) {
		case SHOW_EEROR:
			postMessage(DISMISS_PROGRESS);
			break;
		case SHOW_LINGDUI_INFO:
			imageLoader
					.displayImage(list.get(0).getHeadicon(), iv_pic, options);
			name.setText(list.get(0).getNickname());
			if(list.get(0).getCity() != null && !"未知".equals(list.get(0).getCity())){
				zuobiao.setText(list.get(0).getCity());
			}

			if (!TextUtils.isEmpty(list.get(0).getNote())) {
				detail.setText(list.get(0).getNote());
			}else {
				detail.setText("这个人很懒 ，什么都没有留下");
			}
		case SHOW_ITEM_INFO:
			offset++;
			adapter.notifyDataSetChanged();
			Utility.setListViewHeightBasedOnChildren(mlistview);

		}
		return true;
	}

	private void setlistener() {
		iv_back.setOnClickListener(this);
		iv_share.setOnClickListener(this);
	}

	private void setview() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_pic = (ImageView) findViewById(R.id.iv_pic);
		iv_share = (ImageView) findViewById(R.id.iv_share);
		mlistview = (ListView) findViewById(R.id.mlistview);
		iv_header = (ImageView) findViewById(R.id.iv_header_pic);
		detail = (TextView) findViewById(R.id.detail);
		name = (TextView) findViewById(R.id.name);
		zuobiao = (TextView) findViewById(R.id.zuobiao);

		adapter = new IineInfoAdapter();
		mlistview.setAdapter(adapter);

		mlistview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(LsActivityElect.this,
						LsActivityLines.class);
				Bundle bundle = new Bundle();
				bundle.putInt("id", lists.get(position).getId());
				intent.putExtras(bundle);
				LsActivityElect.this.startActivity(intent);
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == iv_back.getId()) {
			finish();
		} else if (v.getId() == iv_share.getId()) {
			showShareList();
		}
	}

	private void showShareList() {
		postMessage(POPUP_DIALOG_LIST, "分享到", R.array.share_items,
				new DialogInterface.OnClickListener() {

					private Bitmap bp;

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							String shareText = "我在参加＃新雪丽基金领队评选＃活动，快来给我发起的活动点赞吧！活动链接是…… 你也可以来参加的哟！"
									+ nurl;
							bp = BitmapFactory.decodeResource(getResources(),
									R.drawable.ls_activity_share_weibo);
							LsWeiboSina.getInstance(LsActivityElect.this)
									.shares(shareText, bp, 0);
							break;
						case 1:
							bp = BitmapFactory.decodeResource(getResources(),
									R.drawable.ls_activity_share_weibo);
							String shareWx1Text = "我在参加＃新雪丽基金领队评选＃活动，快来给我发起的活动点赞吧！活动链接是…… 你也可以来参加的哟！"
									+ nurl;
							String title1 = "";
							String desc1 = "";
							LsWeiboWeixin
									.getInstance(LsActivityElect.this)
									.getApi()
									.handleIntent(getIntent(),
											LsActivityElect.this);
							LsWeiboWeixin.getInstance(LsActivityElect.this)
									.share(shareWx1Text, title1, desc1, bp,
											SendMessageToWX.Req.WXSceneSession);
							weixin = 2;
							break;
						case 2:
							bp = BitmapFactory.decodeResource(getResources(),
									R.drawable.ls_activity_share_weibo);
							weixin = 3;
							String shareWx2Text = "我在参加＃新雪丽基金领队评选＃活动，快来给我发起的活动点赞吧！活动链接是…… 你也可以来参加的哟！"
									+ nurl;
							String title2 = "123";
							String desc2 = "123";
							LsWeiboWeixin
									.getInstance(LsActivityElect.this)
									.getApi()
									.handleIntent(getIntent(),
											LsActivityElect.this);
							LsWeiboWeixin
									.getInstance(LsActivityElect.this)
									.share(shareWx2Text, title2, desc2, bp,
											SendMessageToWX.Req.WXSceneTimeline);

							break;
						case 3:
							final Bundle params = new Bundle();
							params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "摇一摇");
							params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY,
									"我在参加＃新雪丽基金领队评选＃活动，快来给我发起的活动点赞吧！活动链接是…… 你也可以来参加的哟！");
							params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL,
									nurl);
							ArrayList<String> imageUrls = new ArrayList<String>();
							params.putStringArrayList(
									QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
							tencent.shareToQzone(LsActivityElect.this, params,
									new IUiListener() {

										@Override
										public void onCancel() {
											Util2.toastMessage(
													LsActivityElect.this,
													"onCancel: ");
										}

										@Override
										public void onComplete(
												Object response) {
											Util2.toastMessage(
													LsActivityElect.this,
													"onComplete: ");
										}

										@Override
										public void onError(UiError e) {
											Util2.toastMessage(
													LsActivityElect.this,
													"onComplete: "
															+ e.errorMessage,
													"e");
										}
									});
							break;

						case 4:
							String shareWx4Text = "我在参加＃新雪丽基金领队评选＃活动，快来给我发起的活动点赞吧！活动链接是…… 你也可以来参加的哟！"
									+ nurl;
							LsWeiboTencent.getInstance(LsActivityElect.this)
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
				Toast.makeText(LsActivityElect.this, "分享成功", Toast.LENGTH_LONG)
						.show();
				break;
			case WBConstants.ErrorCode.ERR_CANCEL:
				Toast.makeText(LsActivityElect.this, "取消分享", Toast.LENGTH_LONG)
						.show();
				break;
			case WBConstants.ErrorCode.ERR_FAIL:
				Toast.makeText(LsActivityElect.this,
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

	class ViewHolder {
		ImageView iv;
		TextView ll;
	}

	class IineInfoAdapter extends BaseAdapter {
		private LayoutInflater inflater;

		public IineInfoAdapter() {
			inflater = LayoutInflater.from(LsActivityElect.this);
		}

		@Override
		public int getCount() {
			if (null != lists) {
				return lists.size();
			} else {
				return 0;
			}
		}

		@Override
		public Object getItem(int position) {
			return lists.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.xxl_leader_elect_item,
						null);
				holder = new ViewHolder();
				holder.iv = (ImageView) convertView.findViewById(R.id.iv);
				holder.ll = (TextView) convertView.findViewById(R.id.ll);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.ll.setText(lists.get(position).getTitle());
			imageLoader.displayImage(lists.get(position).getThumb(), holder.iv,options);
			return convertView;
		}
	}
}
