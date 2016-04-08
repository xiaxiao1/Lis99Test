package com.lis99.mobile.entry;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.AnswerBean;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.application.data.QuestionBean;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.view.AsyncLoadImageView;
import com.lis99.mobile.entry.view.AutoResizeListView;
import com.lis99.mobile.mine.LSLoginActivity;
import com.lis99.mobile.util.BitmapUtil;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.RequestParamUtil;
import com.lis99.mobile.util.SharedPreferencesHelper;
import com.lis99.mobile.util.StatusUtil;
import com.lis99.mobile.util.Util2;
import com.lis99.mobile.weibo.LsWeiboSina;
import com.lis99.mobile.weibo.LsWeiboTencent;
import com.lis99.mobile.weibo.LsWeiboWeixin;
import com.sina.weibo.sdk.api.share.BaseResponse;
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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LsAskDetailActivity extends ActivityPattern  implements IWXAPIEventHandler{

	ImageView iv_back,iv_share,et_camre;
	TextView tv_title,tv_nickname,tv_conntent,tv_cate,tv_newwen_answernum,tv_guanzhu;
	AsyncLoadImageView iv_head;
	AutoResizeListView lv_answer_list;
	GridView gv_pic;
	EditText et_comment;
	Button bt_answer;
	String askId;
	private static final int SHOW_QUESTION = 200;
	private static final int REFRESH = 201;
	Bitmap bmp;
	IWeiboShareAPI mWeiboShareAPI;
	// IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;
	private void initWeibo(Bundle savedInstanceState) {
		 // 创建微博 SDK 接口实例
     mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(this, C.SINA_APP_KEY);
     if (savedInstanceState != null) {
         mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
     }
     mTencent = Tencent.createInstance(C.TENCENT_APP_ID, this.getApplicationContext());
		mTencent.setOpenId(SharedPreferencesHelper.getValue(this, C.CONFIG_FILENAME, Context.MODE_PRIVATE, C.TENCENT_OPEN_ID));
		String expire = SharedPreferencesHelper.getValue(this, C.CONFIG_FILENAME, Context.MODE_PRIVATE, C.TENCENT_EXPIRES_IN);
		if(expire ==null || "".equals(expire)){
			expire = "0";
		}
		mTencent.setAccessToken(SharedPreferencesHelper.getValue(this, C.CONFIG_FILENAME, Context.MODE_PRIVATE, C.TENCENT_ACCESS_TOKEN),expire );
	}
	@Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
        // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
        // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
        mWeiboShareAPI.handleWeiboResponse(intent, this);
        setIntent(intent);
        api.handleIntent(intent, this);
    }
	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
	private String targetUrl = "http://www.qq.com";
	public static String mAppid;
	private Tencent tencent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ls_ask_detail);

		StatusUtil.setStatusBar(this);
		initWeibo(savedInstanceState);
		tencent = Tencent.createInstance(C.TENCENT_APP_ID, this);
    	api = WXAPIFactory.createWXAPI(this, C.WEIXIN_APP_ID,true);
    	api.registerApp(C.WEIXIN_APP_ID);
    	api.handleIntent(getIntent(), this);
		Resources res = getResources();
		bmp = BitmapFactory.decodeResource(res, R.drawable.ls_nologin_header_icon);
		askId = getIntent().getStringExtra("ask_id");
		setView();
		setListener();
		postMessage(POPUP_PROGRESS,getString(R.string.sending));
		getList();
	}
	private void getList() {
		String url = C.WENDA_QUESTION_URL+askId;
		Task task = new Task(null, url, null, "WENDA_QUESTION_URL", this);
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
				if(((String) task.getParameter()).equals("WENDA_QUESTION_URL")){
					parserList(result);
				}else if(((String) task.getParameter()).equals("MAIN_ADDLIKE_URL")){
					parserDoLike(result);
				}else if(((String) task.getParameter()).equals("WENDA_REPLY_URL")){
					parserReply(result);
				}
			}
			break;
		default:
			break;
		}

	}
	private void parserReply(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if("OK".equals(result)){
				postMessage(REFRESH);
			}else{
				postMessage(POPUP_TOAST, result);
			}
		}else{
			postMessage(DISMISS_PROGRESS);
		}
	}
	private void parserDoLike(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if("OK".equals(result)){
				if(guangzhu){
					postMessage(POPUP_TOAST, "关注成功");
				}else{
					postMessage(POPUP_TOAST, "已取消关注");
				}
			}else{
				postMessage(POPUP_TOAST, "操作失败");
			}
		}
		postMessage(DISMISS_PROGRESS);
	}
	QuestionBean qb;
	private void parserList(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if("OK".equals(result)){
				qb = (QuestionBean) DataManager.getInstance().jsonParse(params, DataManager.TYPE_WENDA_QUESTION);
				ansList =qb.getAnswers();
				postMessage(SHOW_QUESTION);
			}else{
				postMessage(POPUP_TOAST, result);
			}
		}else{
			postMessage(DISMISS_PROGRESS);
		}
	}
	List<AnswerBean> ansList;
	@Override
	public boolean handleMessage(Message msg) {
		if (super.handleMessage(msg))
			return true;
		switch (msg.what) {
		case SHOW_QUESTION:
			String nickname;
			if(qb.getAuthor().getNickname().equals(DataManager.getInstance().getUser().getNickname())){
				nickname = "我";
			}else{
				nickname = qb.getAuthor().getNickname();
			}
			tv_title.setText(nickname+"的提问");
			tv_nickname.setText(nickname);
			tv_conntent.setText(qb.getTitle());
			tv_cate.setText("其他装备");
			tv_newwen_answernum.setText(qb.getAnswernum());
			if("0".equals(qb.getLike_status())){
				tv_guanzhu.setText("关注");
				tv_guanzhu.setBackgroundResource(R.drawable.ls_guanzhu_bg);
				tv_guanzhu.setTextColor(0xffffffff);
			}else{
				tv_guanzhu.setText("取消关注");
				tv_guanzhu.setBackgroundResource(R.drawable.ls_guanzhu_bg1);
				tv_guanzhu.setTextColor(0xff999999);
			}
			if((qb.getAuthor().getUser_id().equals(DataManager.getInstance().getUser().getUser_id()))){
				tv_guanzhu.setText("取消关注");
				tv_guanzhu.setBackgroundResource(R.drawable.ls_guanzhu_bg1);
				tv_guanzhu.setTextColor(0xff999999);
			}
			iv_head.setImage(qb.getAuthor().getHeadicon(), bmp, bmp, "zhuangbei_detail");
			gv_pic.setAdapter(new WenPicAdapter(qb.getImages()));
			adapter = new CommentListAdapter();
			lv_answer_list.setAdapter(adapter);
			lv_answer_list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Intent intent = new Intent(LsAskDetailActivity.this, LsAnswerDetailActivity.class);
					intent.putExtra("ask_id", ansList.get(arg2).getWenda_id());
					intent.putExtra("answer_id", ansList.get(arg2).getId());
					LsAskDetailActivity.this.startActivity(intent);

				}
			});
			postMessage(DISMISS_PROGRESS);
			break;
		case REFRESH:
			postMessage(DISMISS_PROGRESS);
			postMessage(POPUP_PROGRESS,getString(R.string.sending));
			getList();
			break;
		}
		return true;
	}
	CommentListAdapter adapter;
	private static class ViewHolder{
		AsyncLoadImageView item_comment_head;
		TextView item_comment_nickname,item_comment_date,item_comment;
	}
	private class CommentListAdapter extends BaseAdapter {

		LayoutInflater inflater;

		public CommentListAdapter() {
			inflater = LayoutInflater.from(LsAskDetailActivity.this);
		}

		@Override
		public int getCount() {
			return ansList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return ansList.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			AnswerBean cb = ansList.get(position);
			final int pos = position;
			if(convertView==null){
					convertView = inflater.inflate(R.layout.ls_zhuangbei_comment_item, null);
					holder=new ViewHolder();
					holder.item_comment_head = (AsyncLoadImageView) convertView.findViewById(R.id.item_comment_head);
					holder.item_comment_nickname = (TextView) convertView.findViewById(R.id.item_comment_nickname);
					holder.item_comment_date = (TextView) convertView.findViewById(R.id.item_comment_date);
					holder.item_comment = (TextView) convertView.findViewById(R.id.item_comment);
					convertView.setTag(holder);
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			holder.item_comment_head.setImage(cb.getHeadicon(), bmp, bmp,"zhuangbei_detail");
			holder.item_comment_nickname.setText(cb.getNickname());
			holder.item_comment_date.setVisibility(View.GONE);
			holder.item_comment.setText(cb.getContent());
			return convertView;
		}

	}
	private void setView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_share = (ImageView) findViewById(R.id.iv_share);
		et_camre = (ImageView) findViewById(R.id.et_camre);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_nickname = (TextView) findViewById(R.id.tv_nickname);
		tv_conntent = (TextView) findViewById(R.id.tv_conntent);
		tv_cate = (TextView) findViewById(R.id.tv_cate);
		tv_newwen_answernum = (TextView) findViewById(R.id.tv_newwen_answernum);
		tv_guanzhu = (TextView) findViewById(R.id.tv_guanzhu);
		iv_head = (AsyncLoadImageView) findViewById(R.id.iv_head);
		lv_answer_list = (AutoResizeListView) findViewById(R.id.lv_answer_list);
		et_comment = (EditText) findViewById(R.id.et_comment);
		bt_answer = (Button) findViewById(R.id.bt_answer);
		gv_pic = (GridView) findViewById(R.id.gv_pic);
	}
	private static class ViewPicHolder{
		AsyncLoadImageView iv_pic;
	}
private class WenPicAdapter extends BaseAdapter {

		LayoutInflater inflater;
		List<String>   strList;

		public WenPicAdapter(List<String> list) {
			inflater = LayoutInflater.from(LsAskDetailActivity.this);
			strList = list;
		}

		@Override
		public int getCount() {
			return strList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return strList.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewPicHolder holder;
			String cb = strList.get(position);
			final int pos = position;
			if(convertView==null){
					convertView = inflater.inflate(R.layout.ls_wen_pic_item, null);
					holder=new ViewPicHolder();
					holder.iv_pic = (AsyncLoadImageView) convertView.findViewById(R.id.iv_pic);
					convertView.setTag(holder);
			}else{
				holder=(ViewPicHolder) convertView.getTag();
			}
				holder.iv_pic.setImage(cb, null, null);

			return convertView;
		}

	}
	private void setListener() {
		iv_back.setOnClickListener(this);
		iv_share.setOnClickListener(this);
		et_camre.setOnClickListener(this);
		bt_answer.setOnClickListener(this);
		tv_guanzhu.setOnClickListener(this);
	}
	boolean guangzhu = false;
	@SuppressLint("ShowToast")
	@Override
	public void onClick(View v) {
		if(v.getId() == iv_back.getId()){
			finish();
		}else if(v.getId() == iv_share.getId()){
			showShareList();
		}else if(v.getId() == et_camre.getId()){
			selectPic();
		}else if(v.getId() == bt_answer.getId()){
			if(!"".equals(et_comment.getText().toString().trim())){
				if(DataManager.getInstance().getUser().getUser_id()!=null&&!"".equals(DataManager.getInstance().getUser().getUser_id())){
					postMessage(POPUP_PROGRESS,getString(R.string.sending));
					doCommentTask();
				}else{
					postMessage(POPUP_TOAST,"请先登录");
					Intent intent = new Intent(this, LSLoginActivity.class);
					intent.putExtra("unlogin", "unlogin");
					startActivity(intent);
				}
			}else{
				Toast.makeText(this, "回答不能为空", Toast.LENGTH_SHORT).show();
			}
		}else if(v.getId() == tv_guanzhu.getId()){
			if(DataManager.getInstance().getUser().getUser_id()!=null&&!"".equals(DataManager.getInstance().getUser().getUser_id())){
				postMessage(POPUP_PROGRESS,getString(R.string.sending));
				if(guangzhu){
					/*tv_guanzhu.setText("关注");
					tv_guanzhu.setBackgroundResource(R.drawable.ls_guanzhu_bg);
					guangzhu = false;*/
				}else{
					tv_guanzhu.setText("取消关注");
					tv_guanzhu.setBackgroundResource(R.drawable.ls_guanzhu_bg1);
					guangzhu = true;
				}
				//doLikeTask();
				postMessage(DISMISS_PROGRESS);
			}else{
				postMessage(POPUP_TOAST,"请先登录");
				Intent intent = new Intent(this, LSLoginActivity.class);
				intent.putExtra("unlogin", "unlogin");
				startActivity(intent);
			}

		}
	}
	private void doCommentTask() {
		String url = C.WENDA_REPLY_URL;
		Task task = new Task(null, url, null, "WENDA_REPLY_URL", this);
		task.setPostData(getCommentParams().getBytes());
		publishTask(task, IEvent.IO);
	}
	private String getCommentParams() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("wenda_id", qb.getId());
		params.put("content", et_comment.getText().toString().trim());
		params.put("user_id", DataManager.getInstance().getUser().getUser_id());
		return RequestParamUtil.getInstance(this).getRequestParams(params);
	}


	private void doLikeTask() {
		String url = C.MAIN_ADDLIKE_URL;
		Task task = new Task(null, url, null, "MAIN_ADDLIKE_URL", this);
		task.setPostData(getDoLikeParams().getBytes());
		publishTask(task, IEvent.IO);
	}
	private String getDoLikeParams() {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("id", qb.getId());
		params.put("module", "wenda");
		params.put("user_id", DataManager.getInstance().getUser().getUser_id());
		return RequestParamUtil.getInstance(this).getRequestParams(params);
	}



	private void selectPic() {
		postMessage(POPUP_DIALOG_LIST, "选择图片",
				R.array.select_head_items,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							// 拍摄
							BitmapUtil.doTakePhoto(LsAskDetailActivity.this);
							break;
						case 1:
							// 相册
							BitmapUtil.doPickPhotoFromGallery(LsAskDetailActivity.this);
							break;
						}
					}
				});
	}
	Bitmap bitmap;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//super.onActivityResult(requestCode, resultCode, data);
		System.out.println(requestCode+"==="+resultCode);
		try {
			switch (requestCode) {
			case C.PHOTO_PICKED_WITH_DATA:
				Uri photo_uri = data.getData();
				bitmap = BitmapUtil.getThumbnail(photo_uri,LsAskDetailActivity.this);
				//BitmapUtil.doCropPhoto(bitmap, LsShaiActivity.this);
				break;
			case C.PICKED_WITH_DATA:
				//Bitmap bt = data.getParcelableExtra("data");
				/*postMessage(POPUP_PROGRESS, getString(ResourceUtil.getStringId(this,"sending")));
				uploadHeadTask(bt);*/
				break;
			case C.CAMERA_WITH_DATA:
				File file = new File(C.HEAD_IMAGE_PATH + "temp.jpg");
				bitmap =  BitmapUtil.getThumbnail(file,LsAskDetailActivity.this);
				//BitmapUtil.doCropPhoto(bitmap, LsShaiActivity.this);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showShareList() {
		postMessage(POPUP_DIALOG_LIST, "分享到",
				R.array.share_items,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							String shareText = "提问［"+qb.getTitle()+"］，求答案！"+qb.getShare_url()+"－分享自@砾石帮你选 Android版";
							LsWeiboSina.getInstance(LsAskDetailActivity.this).share(shareText);
							break;
						case 1:
							Resources res = getResources();
							Bitmap bmp1 = BitmapFactory.decodeResource(res, R.drawable.icon_ls);
							String shareWx1Text = qb.getShare_url();
							String title1 = qb.getTitle();
							String desc1 = qb.getDesc();
							LsWeiboWeixin.getInstance(LsAskDetailActivity.this).getApi().handleIntent(getIntent(), LsAskDetailActivity.this);
							LsWeiboWeixin.getInstance(LsAskDetailActivity.this).share(shareWx1Text,title1,desc1, bmp1, SendMessageToWX.Req.WXSceneSession);
							break;
						case 2:
							Resources res1 = getResources();
							Bitmap bmp2 = BitmapFactory.decodeResource(res1, R.drawable.icon_ls);
							String shareWx2Text = qb.getShare_url();
							String title2 = qb.getTitle();
							String desc2 = qb.getDesc();
							LsWeiboWeixin.getInstance(LsAskDetailActivity.this).getApi().handleIntent(getIntent(), LsAskDetailActivity.this);
							LsWeiboWeixin.getInstance(LsAskDetailActivity.this).share(shareWx2Text,title2,desc2, bmp2, SendMessageToWX.Req.WXSceneTimeline);
							break;
						case 3:
							final Bundle params = new Bundle();
							params.putString(QzoneShare.SHARE_TO_QQ_TITLE, qb.getTitle());
							params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, "砾石帮你选上看到［"+qb.getTitle()+"］这个问题，谁来回答下呀？ ");
							params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, qb.getShare_url());
							ArrayList<String> imageUrls = new ArrayList<String>();
							params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
							// 支持传多个imageUrl
							tencent.shareToQzone(LsAskDetailActivity.this, params, new IUiListener() {

								@Override
								public void onCancel() {
									Util2.toastMessage(LsAskDetailActivity.this, "onCancel: ");
								}

								@Override
								public void onComplete(Object response) {
									// TODO Auto-generated method stub
									Util2.toastMessage(LsAskDetailActivity.this, "onComplete: ");
								}

								@Override
								public void onError(UiError e) {
									// TODO Auto-generated method stub
									Util2.toastMessage(LsAskDetailActivity.this, "onComplete: " + e.errorMessage, "e");
								}

							});
							break;
						case 4:
							String shareWx4Text = "提问［"+qb.getTitle()+"］，求答案！"+qb.getShare_url()+"－分享自@砾石帮你选 Android版" ;
							LsWeiboTencent.getInstance(LsAskDetailActivity.this).share(shareWx4Text);
							break;
						}
					}
				});
	}

	@Override
	public void onResponse(BaseResponse arg0) {
		 switch (arg0.errCode) {
	        case WBConstants.ErrorCode.ERR_OK:
	            Toast.makeText(this, "分享成功", Toast.LENGTH_LONG).show();
	            break;
	        case WBConstants.ErrorCode.ERR_CANCEL:
	            Toast.makeText(this, "取消分享", Toast.LENGTH_LONG).show();
	            break;
	        case WBConstants.ErrorCode.ERR_FAIL:
	            Toast.makeText(this,
	                    "分享失败" + "Error Message: " + arg0.errMsg,
	                    Toast.LENGTH_LONG).show();
	            break;
	        }
	}
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
