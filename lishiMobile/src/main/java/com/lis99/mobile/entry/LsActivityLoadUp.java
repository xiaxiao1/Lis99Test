package com.lis99.mobile.entry;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.application.data.ImageBean;
import com.lis99.mobile.application.data.UserDraftBean;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.view.AsyncLoadImageView;
import com.lis99.mobile.util.BitmapUtil;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.RequestParamUtil;
import com.lis99.mobile.util.StatusUtil;
import com.lis99.mobile.util.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LsActivityLoadUp extends ActivityPattern1 {

	Bitmap bitmap;
	TextView tv_cancel, tv_submit, tv_cate;
	LinearLayout ll_cate, ll_images;
	ImageView iv_add;
	EditText et_content;
	private Bundle bundle;
	int pos = -1, album_id, id;
	private static final int SHOW_UPLOAD_PIC = 200;
	private static final int SHOW_SEND_SUCCESS = 201;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ls_shai_upload);

		StatusUtil.setStatusBar(this);

		bundle = this.getIntent().getExtras();
		album_id = bundle.getInt("albumid");
		id = bundle.getInt("id");
		Log.i("aa",id+"");
		setView(photo_uri);
		setListener();
	}

	private void setListener() {
		tv_cancel.setOnClickListener(this);
		tv_submit.setOnClickListener(this);
		iv_add.setOnClickListener(this);
	}

	private void setView(Uri photourl) {
		tv_cancel = (TextView) findViewById(R.id.tv_cancel);
		tv_submit = (TextView) findViewById(R.id.tv_submit);
		iv_add = (ImageView) findViewById(R.id.iv_add);
		if (photourl == null) {
		com.lis99.mobile.entry.view.AsyncLoadImageView ii
		 = new AsyncLoadImageView(this);
		LinearLayout.LayoutParams ll1 = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		ll1.width =StringUtil.dip2px(this, 40);
		ll1.height = StringUtil.dip2px(this, 40);
		ii.setScaleType(ScaleType.FIT_XY);
		//TOD url 设置 ? 
		
		ii.setImage(String.valueOf(photourl), null, null);
		ll_images.addView(ii);
		}
		
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v.getId() == tv_cancel.getId()) {
			finish();
		} else if (v.getId() == tv_submit.getId()) {
			// TODO
			sendTask();
		} else if (v.getId() == iv_add.getId()) {
			// TODO
			setUserPic();
		}
	}
	List<String> images = new ArrayList<String>();
	private void setUserPic() {
		postMessage(POPUP_DIALOG_LIST, "选择图片", R.array.select_head_items,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							// 拍摄
							BitmapUtil.doTakePhoto(LsActivityLoadUp.this);
							break;
						case 1:
							// 相册
							BitmapUtil
									.doPickPhotoFromGallery(LsActivityLoadUp.this);
							break;
						}
					}
				});
	}

	private void sendTask() {
		String url = C.ACTIVITY_SHAITUS_URL + id;
		Task task = new Task(null, url, null, "ACTIVITY_SHAITUS_URL", this);
		task.setPostData(getSendParams().getBytes());
		publishTask(task, IEvent.IO);
	}

	private String getSendParams() {
		HashMap<String, Object> params = new HashMap<String, Object>();

		params.put("user_id", DataManager.getInstance().getUser().getUser_id());
		params.put("album_id", album_id);
		params.put("id", id);
		// TODO 這裡是要提交的圖片的標誌
		for (int i = 0; i < images.size(); i++) {
			params.put("images[]", images.get(i));
		}
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
				if (((String) task.getParameter())
						.equals("ACTIVITY_SHAITUS_URL")) {
					parserSend(result);
				}
			}
			break;
		default:
			break;
		}
	}

	private void parserSend(String params) {
		String result = DataManager.getInstance().validateResult(params);
		if (result != null) {
			if ("OK".equals(result)) {
				DataManager.getInstance().jsonParse(params,
						DataManager.TYPE_SHAITU_CREATE);
				postMessage(SHOW_SEND_SUCCESS);
			} else {
				postMessage(POPUP_TOAST, result);
			}
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		if (super.handleMessage(msg))
			return true;
		switch (msg.what) {
		case SHOW_SEND_SUCCESS:
			postMessage(DISMISS_PROGRESS);
			Toast.makeText(this, "提交成功", 0).show();
			finish();
			break;
		case LOGIN_SUCCESS:
			com.lis99.mobile.entry.view.AsyncLoadImageView ii = new AsyncLoadImageView(
					this);
			LinearLayout.LayoutParams ll1 = new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			ll1.width = StringUtil.dip2px(this, 40);
			ll1.height = StringUtil.dip2px(this, 40);
			ii.setLayoutParams(ll1);
			ii.setScaleType(ScaleType.FIT_XY);
			ii.setImage(ib.getShowimg(), null, null);
			ll_images.addView(ii);
			String imagestr = ib.getImgurl();
			image_url = ib.getShowimg();
			images.add(imagestr);
			postMessage(DISMISS_PROGRESS);
			break;
		}
		return true;
	}

	Bitmap bm;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			switch (requestCode) {
			case C.PHOTO_PICKED_WITH_DATA:
				photo_uri = data.getData();
				
				bm = BitmapUtil.getThumbnail(photo_uri, LsActivityLoadUp.this);
//				postMessage(POPUP_PROGRESS, getString(R.string.sending));
//				new GetDataTask(bm).execute();
				setView(photo_uri);

				break;
			case C.CAMERA_WITH_DATA:
				File file = new File(C.HEAD_IMAGE_PATH + "temp.jpg");
				bm = BitmapUtil.getThumbnail(file, LsActivityLoadUp.this);
				postMessage(POPUP_PROGRESS, getString(R.string.sending));
				new GetDataTask(bm).execute();
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		Bitmap aaa;

		public GetDataTask(Bitmap bm) {
			aaa = bm;
		}

		@Override
		protected String[] doInBackground(Void... params) {
			
			
			
			
//			String[] netResult = new String[10];
//			String savePath = Environment.getExternalStorageDirectory()
//					+ "/temp.jpg";
//			ImageUtil.savePic(savePath, aaa, 100);
//			PostMethod filePost = new PostMethod(C.ACTIVITY_SHAITUS_URL);
//			try {
//				// 组拼post数据的实体
//				// image参数
//				File file = new File(savePath);
//				FilePart f_part = new FilePart("image", file);
//				f_part.setCharSet("utf-8");
//				f_part.setContentType("image/jpeg");
//				// user_id参数
//				StringPart s_part = new StringPart("user_id", DataManager
//						.getInstance().getUser().getUser_id(), "utf-8");
//				StringPart album_id =new StringPart("albumid",String.valueOf(bundle.getInt("albumid")), "utf-8");
//				Part[] parts = { s_part, f_part,album_id};
//				filePost.setRequestEntity(new MultipartRequestEntity(parts,
//						filePost.getParams()));
//				HttpClient client = new HttpClient();
//				client.getHttpConnectionManager().getParams()
//						.setConnectionTimeout(60000);
//				// 完成文件上的post请求
//				client.executeMethod(filePost);
//				String receiveMsg = filePost.getResponseBodyAsString();
//				netResult[0] = receiveMsg;
//			} catch (Exception e) {
//				e.printStackTrace();
//			} finally {
//				filePost.releaseConnection();
//			}
			
			return null ;
//			return netResult;
		}

		@Override
		protected void onPostExecute(String[] result) {
			// mWaittingDialog.dismiss();
			String result1 = DataManager.getInstance()
					.validateResult(result[0]);
			if (result1 != null) {
				if ("OK".equals(result1)) {
					// TODO
					ib = (ImageBean) DataManager.getInstance().jsonParse(
							result[0], DataManager.TYPE_WENDA_UPLOAD);
					postMessage(LOGIN_SUCCESS);
				} else {
					postMessage(POPUP_TOAST, result);
				}
			}

		}
	}

	private static final int LOGIN_SUCCESS = 213;
	String image_url;
	ImageBean ib;
	private UserDraftBean lll;
	private Uri photo_uri;
}
