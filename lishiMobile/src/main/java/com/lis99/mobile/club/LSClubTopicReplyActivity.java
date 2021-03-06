package com.lis99.mobile.club;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.ActivityPattern1;
import com.lis99.mobile.entry.application.DemoApplication;
import com.lis99.mobile.util.BitmapUtil;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.LSScoreManager;
import com.lis99.mobile.util.emotion.MyEmotionsUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * 				回复页
 * @author yy
 *
 */
public class LSClubTopicReplyActivity extends LSBaseActivity implements OnClickListener{
//标题
	private TextView title;
	
	//添加图片
	private RelativeLayout imagePanel;
	//回复引用
	private View  include;
	
	private EditText bodyView;
	//删除图片按钮
	private Button delButton;
	
	private LinearLayout bottomBar_img, bottomBar_emotion;
	
//	添加的图片
	private ImageView replyImageView;
	
	//=====引用=====
	//回复名称 @xxx
	private TextView tv_reply_body;
	//楼层
	private TextView tv_reply_floor;
	//回复的内容
	private TextView tv_reply_content;
//	引用的内容
	private String replyedName, replyedcontent, replyedfloor, replyedId;
	private String clubId, topicId;
	private Bitmap bitmap;
	
	private Button addImage, addEmotion;
	private RelativeLayout titleRight;
	int pageNo = -1;
	
	Drawable /*add, noadd,*/ emotionfocuse, emotionnofocuse;

	//=============================emotion===========

	private LinearLayout emoticonsCover;

	private RelativeLayout parentLayout;

	String url;

	private final static int PREVIEW = 1001;

//	private ImageView emoticonsButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lsclub_topic_reply);
		
		replyedName = getIntent().getStringExtra("replyedName");
		replyedcontent = getIntent().getStringExtra("replyedcontent");
		replyedfloor = getIntent().getStringExtra("replyedfloor");
		
		replyedId = getIntent().getStringExtra("replyedId");
		clubId = getIntent().getStringExtra("clubId");
		topicId = getIntent().getStringExtra("topicId");
		
//		noadd = getResources().getDrawable(R.drawable.reply_add_image_draw);
//		noadd.setBounds(0, 0, noadd.getIntrinsicWidth(), noadd.getIntrinsicHeight());
//		add = getResources().getDrawable(R.drawable.club_reply_chose_image);
//		add.setBounds(0, 0, add.getIntrinsicWidth(), add.getIntrinsicHeight());

		emotionfocuse = getResources().getDrawable(R.drawable.emotion_keybody);
		emotionnofocuse = getResources().getDrawable(R.drawable.emotion_face);
		emotionfocuse.setBounds(0,0,emotionfocuse.getIntrinsicWidth(), emotionfocuse.getIntrinsicHeight());
		emotionnofocuse.setBounds(0,0,emotionnofocuse.getIntrinsicWidth(), emotionnofocuse.getIntrinsicHeight());


		
		initViews();

		processImage(getIntent());
		
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		processImage(intent);
	}

	@Override
	protected void initViews() {
		// TODO Auto-generated method stub
		super.initViews();
		
		imagePanel = (RelativeLayout) findViewById(R.id.imagePanel);
		include = findViewById(R.id.include);
		bodyView = (EditText) findViewById(R.id.bodyView);
		replyImageView = (ImageView)findViewById(R.id.imageView);
		delButton = (Button) findViewById(R.id.delButton);
		delButton.setOnClickListener(this);

		replyImageView.setOnClickListener(this);

		bottomBar_img = (LinearLayout) findViewById(R.id.bottomBar_img);
		bottomBar_img.setOnClickListener(this);
		bottomBar_emotion = (LinearLayout) findViewById(R.id.bottomBar_emotion);

		tv_reply_body = (TextView) findViewById(R.id.tv_reply_body);
		tv_reply_floor = (TextView) findViewById(R.id.tv_reply_floor);
		tv_reply_content = (TextView) findViewById(R.id.tv_reply_content);
		addImage = (Button) findViewById(R.id.addImage);
		addImage.setOnClickListener(this);
		title = (TextView) findViewById(R.id.title);
		titleRight = (RelativeLayout) findViewById(R.id.titleRight);
		titleRight.setOnClickListener(this);
		
		title.setText("回复"+replyedName);
		//是否有引用内容
		if (replyedcontent == null || "".equals(replyedcontent) )
		{
			include.setVisibility(View.GONE);
		}
		else
		{
			tv_reply_body.setText("回复@ "+replyedName);
			tv_reply_floor.setText(replyedfloor+"楼");
			tv_reply_content.setText(replyedcontent);
			include.setVisibility(View.VISIBLE);
		}

		emoticonsCover = (LinearLayout) findViewById(R.id.footer_for_emoticons);

		parentLayout = (RelativeLayout) findViewById(R.id.list_parent);

//		emoticonsButton = (ImageView) findViewById(R.id.emoticons_button);

		addEmotion = (Button) findViewById(R.id.addEmotion);


		MyEmotionsUtil.getInstance().setVisibleEmotion(callBack);
		MyEmotionsUtil.getInstance().initView(this, bodyView, bottomBar_emotion, emoticonsCover, parentLayout);

	}
	
	@Override
		protected void rightAction()
		{
			// TODO Auto-generated method stub
			super.rightAction();

			publish();
//			Intent intent = new Intent();
//			intent.putExtra("lastPage", pageNo);
//			setResult(RESULT_OK, intent);
//			finish();
		}
	
	protected void publish()
	{

//		Spannable sp = content.getText();

		String body = bodyView.getText().toString().trim();

		if (TextUtils.isEmpty(body) )
		{
			if ( bitmap == null )
			{
				postMessage(POPUP_TOAST, "请填写回复内容");
				return;
			}
			else
			{
				body = "分享图片";
			}
		}

		String userID = DataManager.getInstance().getUser().getUser_id();

		AsyncHttpClient client = new AsyncHttpClient();

		RequestParams params = new RequestParams();
		params.put("title", "");
		params.put("content", body);
		params.put("category", 0);
		params.put("club_id", clubId);
		params.put("parentid", topicId);
		params.put("user_id", userID);
		if ( !"".equals(replyedId))
		{
			params.put("reply_id", replyedId);
		}
		if (bitmap != null)
			params.put(
					"thumb",
					new ByteArrayInputStream(BitmapUtil
							.bitampToByteArray(bitmap)), "image.jpg");

		client.post(C.CLUB_ADD_TOPIC, params, new JsonHttpResponseHandler()
		{

			@Override
			public void onStart()
			{
				postMessage(ActivityPattern1.POPUP_PROGRESS,
						getString(R.string.sending));
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response)
			{
				Common.log(response.toString());
				
				String errorCode = response.optString("status", "");
				if ("OK".equals(errorCode))
				{
					String data;
					try
					{
						data = response.getString("data");
						JSONObject j = new JSONObject(data);
						//最后一页
						pageNo = j.optInt("totpage");
					} catch (JSONException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					postMessage(POPUP_TOAST, "发布成功");
//					closeReplyPanel();
//					offset = 0;
//					loadTopicInfo2(true);

					if ( bitmap == null )
					{
						LSScoreManager.getInstance().sendScore(LSScoreManager.replytopicbynoimg, topicId);
					}
					else
					{
						LSScoreManager.getInstance().sendScore(LSScoreManager.replytopicbyimg, topicId);
					}

					Intent intent = new Intent();
					intent.putExtra("lastPage", pageNo);
					setResult(RESULT_OK, intent);
					finish();
				}
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONArray timeline)
			{

			}

			@Override
			public void onFinish()
			{
				postMessage(DISMISS_PROGRESS);
			}

		});
	}
	
	private void getImage ()
	{
		if (bitmap != null)
		{
			return;
		}
		postMessage(POPUP_DIALOG_LIST, "选择图片", R.array.select_head_items,
				new DialogInterface.OnClickListener()
				{

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						switch (which)
						{
							case 0:
								// 拍摄
								BitmapUtil
										.doTakePhoto(LSClubTopicReplyActivity.this);
								break;
							case 1:
								// 相册
								Intent intent = new Intent(LSClubTopicReplyActivity.this, LSImagePicker.class);
								intent.putExtra("isReply", true);
								startActivity(intent);

								break;
						}
					}
				});
	}

	private void processImage(Intent intent) {
		ArrayList<String> uris = (ArrayList<String>) intent.getStringArrayListExtra("uris");
		if (uris != null && uris.size() > 0) {
			url = uris.get(0);
			new LoadImageAsynTask().execute(url);
		}
	}

	public class LoadImageAsynTask extends AsyncTask<String, Integer, Bitmap> {
		@Override
		protected Bitmap doInBackground(String... params) {
			String url = params[0];
			if (url != null) {

				return ImageUtil.getUpdataBitmap(url);

//				BitmapFactory.Options options = new BitmapFactory.Options();
//				options.inJustDecodeBounds = true;
//				BitmapFactory.decodeFile(url, options);
//				int width = options.outWidth;
//				int height = options.outHeight;
//				double scaleWidth = width / 600;
//				double scaleHeight = height / 1000;
//				double maxScale = Math.max(scaleHeight, scaleWidth);
//
//				double scale = 1.0;
//				while (scale < maxScale) {
//					scale = scale * 2.0;
//				}
//
//				try {
//					options.inJustDecodeBounds = false;
//					options.inSampleSize = (int) scale;
//					options.inPreferredConfig = Bitmap.Config.RGB_565;
//					options.inInputShareable = true;
//					options.inPurgeable = true;
//					Bitmap bitmap = BitmapFactory.decodeFile(url, options);
//					return bitmap;
//
//
//				} catch (OutOfMemoryError e) {
//
//				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			super.onPostExecute(bitmap);
			if (bitmap != null) {
				LSClubTopicReplyActivity.this.bitmap = bitmap;
				imagePanel.setVisibility(View.VISIBLE);
				replyImageView.setImageBitmap(bitmap);
			}
			else
			{
				Common.toast("相片格式错误");
			}
		}
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		try
		{
			switch (requestCode)
			{
				case C.PHOTO_PICKED_WITH_DATA:
					Uri photo_uri = data.getData();
					bitmap = BitmapUtil.getThumbnail(photo_uri, this);
					imagePanel.setVisibility(View.VISIBLE);
					replyImageView.setImageBitmap(bitmap);
					break;
				case C.PICKED_WITH_DATA:
					break;
				case C.CAMERA_WITH_DATA:
					File file = new File(C.HEAD_IMAGE_PATH + "temp.jpg");
//					bitmap = BitmapUtil.getThumbnail(file, this);

					bitmap = ImageUtil.getUpdataBitmap(file.getAbsolutePath());

					imagePanel.setVisibility(View.VISIBLE);
					replyImageView.setImageBitmap(bitmap);
					break;
				case PREVIEW:
					if (data != null) {
						boolean delete = data.getBooleanExtra("delete", false);
						if (delete) {
							bitmap = null;
							replyImageView.setImageBitmap(null);
							imagePanel.setVisibility(View.GONE);
						}
					}
					break;
			}
//			changeButtonBg();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void changeButtonBg()
	{
//		if (bitmap == null)
//		{
//			addImage.setCompoundDrawables(add, null, null, null);
//		} else
//		{
//			addImage.setCompoundDrawables(noadd, null, null, null);
//		}
	}
	
	@Override
	public void onClick(View arg0)
	{
		// TODO Auto-generated method stub
		super.onClick(arg0);
		switch (arg0.getId())
		{
			case R.id.bottomBar_img:
			case R.id.addImage:
				getImage();
				break;
			case R.id.delButton:
				bitmap = null;
				replyImageView.setImageBitmap(null);
				imagePanel.setVisibility(View.GONE);
				changeButtonBg();
				break;
			case R.id.titleRight:
				rightAction();
				break;
			case R.id.imageView:
			{
				Intent intent = new Intent(this, LSPublishImagePreviewActivity.class);
				DemoApplication.publishBitmap = new WeakReference<Bitmap>(bitmap);
				startActivityForResult(intent, PREVIEW);
				return;
			}

		}
	}


	private CallBack callBack = new CallBack() {
		@Override
		public void handler(MyTask mTask) {
			if ( "GONE".equals(mTask.getresult()))
			{
				addEmotion.setCompoundDrawables(emotionnofocuse, null, null, null);
			}
			else {
				addEmotion.setCompoundDrawables(emotionfocuse, null, null, null);
			}
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ( MyEmotionsUtil.getInstance().onKeyDown(keyCode, event) )
		{
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
