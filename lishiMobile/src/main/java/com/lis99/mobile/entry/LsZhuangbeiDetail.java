package com.lis99.mobile.entry;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.CommentBean;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.application.data.ZhuangbeiBean;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.view.AsyncLoadImageView;
import com.lis99.mobile.mine.LSLoginActivity;
import com.lis99.mobile.newhome.NewHomeActivity;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.RequestParamUtil;
import com.lis99.mobile.util.SharedPreferencesHelper;
import com.lis99.mobile.util.StatusUtil;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.tauth.Tencent;

import java.util.HashMap;
import java.util.List;

public class LsZhuangbeiDetail extends ActivityPattern implements
		IWXAPIEventHandler {

	ImageView iv_back, iv_home, iv_like, iv_share;
	AsyncLoadImageView ls_zhuangbei_detail_pic;
	// TextView tv_comment;
	// LinearLayout ls_zhuangbei_item_star;
	// WebView ls_zhuangbei_content;
	private static final int SHOW_ZHUANGBEI_CONTENT = 202;
	private static final int SHOW_COMMENTLIST = 203;
	IWeiboShareAPI mWeiboShareAPI;
	private String targetUrl = "http://www.qq.com";
	public static String mAppid;
	private Tencent tencent;
	// AutoResizeListView lv_commentlist;
	CommentListAdapter adapter;
	WebView webview;
	String id;

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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ls_zhuangbei_detail);
		StatusUtil.setStatusBar(this);
		initWeibo(savedInstanceState);
		// 通过WXAPIFactory工厂，获取IWXAPI的实例
		tencent = Tencent.createInstance(C.TENCENT_APP_ID, this);
		id = getIntent().getStringExtra("id");
		setView();
		setListener();
		postMessage(POPUP_PROGRESS, getString(R.string.sending));
		getZhuangbeiDetail(id);
	}

	private void getZhuangbeiDetail(String id) {
		String url = C.ZHUANGBEI_DETAIL_URL + id;
		Task task = new Task(null, url, null, "ZHUANGBEI_DETAIL_URL", this);
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
						.equals("ZHUANGBEI_DETAIL_URL")) {
					parserZhuangbeiDetailList(result);
				} else if (((String) task.getParameter())
						.equals("MAIN_COMMENTLIST_URL")) {
					parserCommentList(result);
				} else if (((String) task.getParameter())
						.equals("MAIN_ADDLIKE_URL")) {
					parserDoLike(result);
				}
			}
			break;
		default:
			break;
		}

	}

	List<CommentBean> list_cb;

	private void parserCommentList(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if ("OK".equals(result)) {
				list_cb = (List<CommentBean>) DataManager.getInstance()
						.jsonParse(params, DataManager.TYPE_MAIN_COMMENTLIST);
				postMessage(SHOW_COMMENTLIST);
			} else {
				postMessage(DISMISS_PROGRESS);
			}
		} else {
			postMessage(DISMISS_PROGRESS);
		}
	}

	private void parserDoLike(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if ("OK".equals(result)) {
				postMessage(POPUP_TOAST, "成功添加至‘我喜欢的’");
			} else {
				postMessage(POPUP_TOAST, "操作失败");
			}
		}
		postMessage(DISMISS_PROGRESS);
	}

	ZhuangbeiBean zb1;

	private void parserZhuangbeiDetailList(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if ("OK".equals(result)) {
				zb1 = (ZhuangbeiBean) DataManager.getInstance().jsonParse(
						params, DataManager.TYPE_ZHUANGBEI_DETAIL);
				postMessage(SHOW_ZHUANGBEI_CONTENT);
			} else {
				postMessage(DISMISS_PROGRESS);
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
		case SHOW_ZHUANGBEI_CONTENT:
			ls_zhuangbei_detail_pic.setImage(zb1.getImage(), null, null);
			/*
			 * ls_zhuangbei_detail_pic.setImage(zb1.getImage(), null, null);
			 * ls_zhuangbei_detail_title.setText(zb1.getTitle());
			 * ls_zhuangbei_item_score.setText(zb1.getScore());
			 * ls_zhuangbei_detail_like.setText(zb1.getLike()+"人喜欢"); int score
			 * = (int)(Float.parseFloat(zb1.getScore())); for(int
			 * i=0;i<score;i++){ ImageView iv = new ImageView(this);
			 * iv.setImageResource(R.drawable.ls_zhuangbei_star_icon);
			 * LinearLayout.LayoutParams ll = new
			 * LinearLayout.LayoutParams(LayoutParams
			 * .WRAP_CONTENT,LayoutParams.WRAP_CONTENT); iv.setLayoutParams(ll);
			 * ls_zhuangbei_item_star.addView(iv); } String html =
			 * "<html><head><style type=\"text/css\"><!--body {margin: 0px;}--></style></head><body>"
			 * +zb1.getContent()+"</body></html>";
			 * ls_zhuangbei_content.getSettings
			 * ().setDefaultTextEncodingName("utf-8");
			 * ls_zhuangbei_content.getSettings().setJavaScriptEnabled(true);
			 * //settings.setUseWideViewPort(true);
			 * //settings.setLoadWithOverviewMode(true);
			 * //news_webview.setInitialScale(150);// 初始显示比例100%
			 * ls_zhuangbei_content
			 * .getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
			 * ls_zhuangbei_content
			 * .getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
			 * ls_zhuangbei_content.loadDataWithBaseURL(null, html, "text/html",
			 * "UTF-8", null); //ls_zhuangbei_content.loadData(html,
			 * "text/html", "utf-8"); getCommentList(id);
			 */

			WebSettings settings = webview.getSettings();
			settings.setJavaScriptEnabled(true);
			// settings.setUseWideViewPort(true);
			// settings.setLoadWithOverviewMode(true);
			// news_webview.setInitialScale(150);// 初始显示比例100%
			/*
			 * settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
			 * settings.setSupportZoom(true);
			 * settings.setBuiltInZoomControls(true);
			 * settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
			 */
			webview.loadUrl(zb1.getWap_url());
			postMessage(DISMISS_PROGRESS);
			break;
		case SHOW_COMMENTLIST:
			/*
			 * if(list_cb !=null && list_cb.size()>0){ adapter = new
			 * CommentListAdapter(); lv_commentlist.setAdapter(adapter);
			 * tv_comment.setText("查看所有评论");
			 * //setAdapterHeightBasedOnChildren1(lv_commentlist); }else{
			 * tv_comment.setText("发表评论"); }
			 */
			postMessage(DISMISS_PROGRESS);
			break;
		}
		return true;
	}

	private void getCommentList(String id) {
		String url = C.MAIN_COMMENTLIST_URL + C.MODULE_TYPE_ZHUANGBEI + "/"
				+ id + "/" + "0" + "/" + 3 + "/" + "desc";
		Task task = new Task(null, url, null, "MAIN_COMMENTLIST_URL", this);
		publishTask(task, IEvent.IO);

	}

	private void setView() {
		iv_home = (ImageView) findViewById(R.id.iv_home);
		iv_home.setVisibility(View.GONE);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		ls_zhuangbei_detail_pic = (AsyncLoadImageView) findViewById(R.id.ls_zhuangbei_detail_pic);
		// ls_zhuangbei_detail_title = (TextView)
		// findViewById(R.id.ls_zhuangbei_detail_title);
		// ls_zhuangbei_item_score = (TextView)
		// findViewById(R.id.ls_zhuangbei_item_score);
		// ls_zhuangbei_detail_like = (TextView)
		// findViewById(R.id.ls_zhuangbei_detail_like);
		// ls_zhuangbei_item_star = (LinearLayout)
		// findViewById(R.id.ls_zhuangbei_item_star);
		// ls_zhuangbei_content = (WebView)
		// findViewById(R.id.ls_zhuangbei_content);
		// tv_comment = (TextView) findViewById(R.id.tv_comment);
		// lv_commentlist = (AutoResizeListView)
		// findViewById(R.id.lv_commentlist);
		iv_like = (ImageView) findViewById(R.id.iv_like);
		iv_share = (ImageView) findViewById(R.id.iv_share);
		webview = (WebView) findViewById(R.id.zhuangbei_webview);
	}

	private static final String SDCARD_ROOT = Environment
			.getExternalStorageDirectory().getAbsolutePath();

	private static class ViewHolder {
		AsyncLoadImageView item_comment_head;
		TextView item_comment_nickname, item_comment_date, item_comment;
	}

	private class CommentListAdapter extends BaseAdapter {

		LayoutInflater inflater;

		public CommentListAdapter() {
			inflater = LayoutInflater.from(LsZhuangbeiDetail.this);
		}

		@Override
		public int getCount() {
			return list_cb.size();
		}

		@Override
		public Object getItem(int arg0) {
			return list_cb.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			CommentBean cb = list_cb.get(position);
			final int pos = position;
			if (convertView == null) {
				convertView = inflater.inflate(
						R.layout.ls_zhuangbei_comment_item, null);
				holder = new ViewHolder();
				holder.item_comment_head = (AsyncLoadImageView) convertView
						.findViewById(R.id.item_comment_head);
				holder.item_comment_nickname = (TextView) convertView
						.findViewById(R.id.item_comment_nickname);
				holder.item_comment_date = (TextView) convertView
						.findViewById(R.id.item_comment_date);
				holder.item_comment = (TextView) convertView
						.findViewById(R.id.item_comment);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.item_comment_head.setImage(cb.getHeadicon(), null, null,
					"zhuangbei_detail");
			holder.item_comment_nickname.setText(cb.getNickname());
			holder.item_comment_date.setText(cb.getCreatedate());
			holder.item_comment.setText(cb.getComment());
			return convertView;
		}

	}

	private void setListener() {
		iv_back.setOnClickListener(this);
//		iv_home.setOnClickListener(this);
		// tv_comment.setOnClickListener(this);
		iv_like.setOnClickListener(this);
		iv_share.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == iv_home.getId()) {
			Intent intent = new Intent(this, NewHomeActivity.class);
//			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		} else if (v.getId() == iv_back.getId()) {
			finish();
		}/*
		 * else if(v.getId() == tv_comment.getId()){
		 * if(DataManager.getInstance()
		 * .getUser().getUser_id()!=null&&!"".equals(
		 * DataManager.getInstance().getUser().getUser_id())){ Intent intent =
		 * new Intent(this,ZhuangbeiCommentActivity.class);
		 * intent.putExtra("id", id); startActivity(intent); }else{
		 * postMessage(POPUP_TOAST,"请先登录"); Intent intent = new Intent(this,
		 * LSLoginActivity.class); intent.putExtra("unlogin", "unlogin");
		 * startActivity(intent); } }
		 */else if (v.getId() == iv_like.getId()) {
			if (DataManager.getInstance().getUser().getUser_id() != null
					&& !"".equals(DataManager.getInstance().getUser()
							.getUser_id())) {
				postMessage(POPUP_PROGRESS, getString(R.string.sending));
				doLikeTask();
			} else {
				postMessage(POPUP_TOAST, "请先登录");
				Intent intent = new Intent(this, LSLoginActivity.class);
				intent.putExtra("unlogin", "unlogin");
				startActivity(intent);
			}
		} else if (v.getId() == iv_share.getId()) {
//			showShareList();
			if ( zb1 == null ) return;
//			ShareManager.getInstance().showShareList(this, zb1.getTitle(), zb1.getShare_url(), zb1.getImage());
		}
	}

//	private void showShareList() {
//		postMessage(POPUP_DIALOG_LIST, "分享到", R.array.share_items,
//				new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						switch (which) {
//						case 0:
//							String shareSinaText = "推荐个好东西给大家［"
//									+ zb1.getTitle() + "］" + zb1.getShare_url()
//									+ "－分享自@砾石帮你选 Android版";
//							try {
//					    		ComponentName cmp = new ComponentName("com.sina.weibo","com.sina.weibo.EditActivity");
//					    		Intent intent = new Intent(Intent.ACTION_SEND);
//					        	intent.setType("image/*");   
////					          intent.putExtra(Intent.EXTRA_SUBJECT, "分享123");
//					            intent.putExtra(Intent.EXTRA_TEXT, shareSinaText);
//					            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					        	intent.setComponent(cmp);
//					        	startActivity(intent); 
//							} catch (Exception e) {
//								// TODO: handle exception
////								Toast.makeText(getApplicationContext(), "123123123231231231", Toast.LENGTH_LONG).show();
//								//如果没有安装客户端， 调用内置SDK分享
//								LsWeiboSina.getInstance(LsZhuangbeiDetail.this)
//								.share(shareSinaText);
//							}
//							
//							
//							
//							break;
//						case 1:
//							String shareWx1Text = zb1.getShare_url();
//							String title1 = zb1.getTitle();
//							String desc1 = "分享个好东西给你【" + zb1.getTitle() + "】";
//							Bitmap bmp1 = ImageCacheManager.getInstance()
//									.getBitmapFromCache(zb1.getImage());
//							LsWeiboWeixin
//									.getInstance(LsZhuangbeiDetail.this)
//									.getApi()
//									.handleIntent(getIntent(),
//											LsZhuangbeiDetail.this);
//							LsWeiboWeixin.getInstance(LsZhuangbeiDetail.this)
//									.share(shareWx1Text, title1, desc1, bmp1,
//											SendMessageToWX.Req.WXSceneSession);
//							break;
//						case 2:
//							String shareWx2Text = zb1.getShare_url();
//							String title2 = zb1.getTitle();
//							String desc2 = "分享＃装备＃【" + zb1.getTitle() + "】";
//							Bitmap bmp2 = ImageCacheManager.getInstance()
//									.getBitmapFromCache(zb1.getImage());
//							LsWeiboWeixin
//									.getInstance(LsZhuangbeiDetail.this)
//									.getApi()
//									.handleIntent(getIntent(),
//											LsZhuangbeiDetail.this);
//							LsWeiboWeixin
//									.getInstance(LsZhuangbeiDetail.this)
//									.share(shareWx2Text, title2, desc2, bmp2,
//											SendMessageToWX.Req.WXSceneTimeline);
//							break;
//						case 3:
//							final Bundle params = new Bundle();
//							params.putString(QzoneShare.SHARE_TO_QQ_TITLE,
//									zb1.getTitle());
//							params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY,
//									"砾石帮你选上看到［" + zb1.getTitle() + "］不错，推荐给大家。");
//							params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL,
//									zb1.getShare_url());
//							ArrayList<String> imageUrls = new ArrayList<String>();
//							//图片URL
//							if ( null != zb1.getImage() ){imageUrls.add(zb1.getImage());}
//							
//							params.putStringArrayList(
//									QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
//							// 支持传多个imageUrl
//							tencent.shareToQzone(LsZhuangbeiDetail.this,
//									params, new IUiListener() {
//
//										@Override
//										public void onCancel() {
//											Util2.toastMessage(
//													LsZhuangbeiDetail.this,
//													"onCancel: ");
//										}
//
//										@Override
//										public void onComplete(
//												Object response) {
//											// TODO Auto-generated method stub
//											Util2.toastMessage(
//													LsZhuangbeiDetail.this,
//													"onComplete: ");
//										}
//
//										@Override
//										public void onError(UiError e) {
//											// TODO Auto-generated method stub
//											Util2.toastMessage(
//													LsZhuangbeiDetail.this,
//													"onComplete: "
//															+ e.errorMessage,
//													"e");
//										}
//
//									});
//							break;
//						case 4:
//							String shareWx4Text = "推荐个好东西给大家【" + zb1.getTitle()
//									+ "】" + zb1.getShare_url()
//									+ "－分享自@砾石帮你选 Android版";
//							LsWeiboTencent.getInstance(LsZhuangbeiDetail.this)
//									.share(shareWx4Text);
//							break;
//						}
//					}
//				});
//	}

	private static final int THUMB_SIZE = 150;

	private void doLikeTask() {
		String url = C.MAIN_ADDLIKE_URL;
		Task task = new Task(null, url, null, "MAIN_ADDLIKE_URL", this);
		task.setPostData(getDoLikeParams().getBytes());
		publishTask(task, IEvent.IO);
	}

	private String getDoLikeParams() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("module", "zhuangbei");
		params.put("user_id", DataManager.getInstance().getUser().getUser_id());
		return RequestParamUtil.getInstance(this).getRequestParams(params);
	}

//	@Override
//	public void onResponse(BaseResponse arg0) {
//		System.out.println("onResponse==" + arg0);
//		switch (arg0.errCode) {
//		case WBConstants.ErrorCode.ERR_OK:
//			Toast.makeText(this, "分享成功", Toast.LENGTH_LONG).show();
//			break;
//		case WBConstants.ErrorCode.ERR_CANCEL:
//			Toast.makeText(this, "取消分享", Toast.LENGTH_LONG).show();
//			break;
//		case WBConstants.ErrorCode.ERR_FAIL:
//			Toast.makeText(this, "分享失败" + "Error Message: " + arg0.errMsg,
//					Toast.LENGTH_LONG).show();
//			break;
//		}
//	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		mWeiboShareAPI.handleWeiboResponse(intent, this);
		setIntent(intent);
	}

	@Override
	public void onReq(BaseReq arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResp(BaseResp arg0) {
		// TODO Auto-generated method stub

	}

}
