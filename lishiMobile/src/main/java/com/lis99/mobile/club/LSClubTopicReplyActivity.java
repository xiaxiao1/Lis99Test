package com.lis99.mobile.club;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.lis99.mobile.entry.ActivityPattern1;
import com.lis99.mobile.util.BitmapUtil;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.LSScoreManager;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
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
	
	private LinearLayout bottomBar;
	
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
	
	private Button addImage;
	private RelativeLayout titleRight;
	int pageNo = -1;
	
	Drawable add, noadd;
	
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
		
		noadd = getResources().getDrawable(R.drawable.reply_add_image_draw);
		noadd.setBounds(0, 0, noadd.getIntrinsicWidth(), noadd.getIntrinsicHeight());
		add = getResources().getDrawable(R.drawable.club_reply_chose_image);
		add.setBounds(0, 0, add.getIntrinsicWidth(), add.getIntrinsicHeight());
		
		initViews();
		
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
		
		bottomBar = (LinearLayout) findViewById(R.id.bottomBar);
		bottomBar.setOnClickListener(this);
		
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
		String body = bodyView.getText().toString();

		if (TextUtils.isEmpty(body))
		{
			postMessage(POPUP_TOAST, "回复内容不能为空");
			return;
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
						LSScoreManager.getInstance().sendScore(LSScoreManager.replytopicbynoimg);
					}
					else
					{
						LSScoreManager.getInstance().sendScore(LSScoreManager.replytopicbyimg);
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
								BitmapUtil
										.doPickPhotoFromGallery(LSClubTopicReplyActivity.this);
								break;
						}
					}
				});
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
					bitmap = BitmapUtil.getThumbnail(file, this);
					imagePanel.setVisibility(View.VISIBLE);
					replyImageView.setImageBitmap(bitmap);
					break;
			}
			changeButtonBg();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void changeButtonBg()
	{
		if (bitmap == null)
		{
			addImage.setCompoundDrawables(add, null, null, null);
		} else
		{
			addImage.setCompoundDrawables(noadd, null, null, null);
		}
	}
	
	@Override
	public void onClick(View arg0)
	{
		// TODO Auto-generated method stub
		super.onClick(arg0);
		switch (arg0.getId())
		{
			case R.id.bottomBar:
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
		}
	}
	
	
}
