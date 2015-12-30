package com.lis99.mobile.club;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class LSClubPublish2Activity extends LSBaseActivity {
    EditText titleView;
    EditText bodyView;
    Bitmap bitmap;
    ImageView imageView;
    View imagePanel;
    View mainView;
    int clubID;
    String url;
    int topicid;

    private final static int PREVIEW = 1001;

//	Drawable add, noadd;

    //====
    ImageView addImage, addEmotion;

    View bottomBar_img, bottomBar_emotion;

    private LinearLayout emoticonsCover;

    private View parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clubID = getIntent().getIntExtra("clubID", 0);
        setContentView(R.layout.activity_lsclub_publish2);

//		noadd = getResources().getDrawable(R.drawable.reply_add_image_draw);
//		noadd.setBounds(0, 0, noadd.getIntrinsicWidth(), noadd.getIntrinsicHeight());
//		add = getResources().getDrawable(R.drawable.club_reply_chose_image);
//		add.setBounds(0, 0, add.getIntrinsicWidth(), add.getIntrinsicHeight());

        initViews();
        setTitle("发布话题");

        processImage(getIntent());

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
//					options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//					options.inInputShareable = true;
//					options.inPurgeable = true;
//					Bitmap bitmap = BitmapFactory.decodeFile(url, options);
//					return bitmap;
//
//
//				}
//				catch (OutOfMemoryError e) {
//
//				}
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                LSClubPublish2Activity.this.bitmap = bitmap;
                imagePanel.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(bitmap);
            } else {
                Common.toast("相片格式错误");
            }
        }
    }

    @Override
    protected void rightAction() {
        String title = titleView.getText().toString().trim();
        String body = bodyView.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            postMessage(POPUP_TOAST, "标题不能为空");
            return;
        }

        if (TextUtils.isEmpty(body)) {
            postMessage(POPUP_TOAST, "正文不能为空");
            return;
        }

        String userID = DataManager.getInstance().getUser().getUser_id();

        AsyncHttpClient client = new AsyncHttpClient();


        RequestParams params = new RequestParams();
        params.put("title", title);
        params.put("content", body);
        params.put("category", 0);
        params.put("club_id", clubID);
        params.put("parentid", 0);
        params.put("user_id", userID);
        if (bitmap != null)
            params.put("thumb", new ByteArrayInputStream(BitmapUtil.bitampToByteArray(bitmap)), "image.jpg");

        client.post(C.CLUB_ADD_TOPIC, params, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                postMessage(ActivityPattern1.POPUP_PROGRESS,
                        getString(R.string.sending));
            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                String errorCode = response.optString("status", "");
                if ("OK".equals(errorCode)) {
                    postMessage(POPUP_TOAST, "发布成功");

//					LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(LSClubPublish2Activity.this);
//					Intent intent = new Intent(LSClubDetailActivity.CLUB_TOPIC_CHANGE);
//					lbm.sendBroadcast(intent);

                    String data = response.optString("data", "");
                    if (!TextUtils.isEmpty(data)) {
                        try {
                            JSONObject j = new JSONObject(data);

                            int category = j.optInt("category", -1);
                            topicid = j.optInt("topicid", -1);
                            if (category != -1 && topicid != -1) {
                                if (category == 2) {
                                    Intent in = new Intent(activity, LSClubTopicNewActivity.class);
                                    in.putExtra("topicID", topicid);
                                    startActivity(in);
                                } else {
                                    Intent in = new Intent(activity, LSClubTopicActivity.class);
                                    in.putExtra("topicID", topicid);
                                    startActivity(in);
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    setresult();
                    finish();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {

            }

            @Override
            public void onFinish() {
                postMessage(DISMISS_PROGRESS);
            }

        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        processImage(intent);
    }

    private void setresult() {
        LSScoreManager.getInstance().sendScore(LSScoreManager.pubtalktopic, ""+topicid);
        setResult(RESULT_OK);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.addImage || v.getId() == R.id.bottomBar_img) {
            addImage();
            return;
        } else if (v.getId() == R.id.delButton) {
            bitmap = null;
            imageView.setImageBitmap(null);
            imagePanel.setVisibility(View.GONE);
//			changeButtonBg();
            return;
        } else if (v.getId() == imageView.getId()) {
            Intent intent = new Intent(this, LSPublishImagePreviewActivity.class);
            DemoApplication.publishBitmap = new WeakReference<Bitmap>(bitmap);
            startActivityForResult(intent, PREVIEW);
            return;
        }

        super.onClick(v);
    }

    @Override
    protected void initViews() {
        super.initViews();
        titleView = (EditText) findViewById(R.id.titleView);
        bodyView = (EditText) findViewById(R.id.bodyView);

        titleView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    MyEmotionsUtil.getInstance().dismissPopupWindow();
                    bottomBar_emotion.setVisibility(View.GONE);
                }
            }
        });

        bodyView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    bottomBar_emotion.setVisibility(View.VISIBLE);
                }
            }
        });


        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnClickListener(this);
        imagePanel = findViewById(R.id.imagePanel);

        mainView = findViewById(R.id.main);

        addImage = (ImageView) findViewById(R.id.addImage);

        addEmotion = (ImageView) findViewById(R.id.addEmotion);

        bottomBar_img = findViewById(R.id.bottomBar_img);

        bottomBar_emotion = findViewById(R.id.bottomBar_emotion);

        bottomBar_img.setOnClickListener(this);
        bottomBar_emotion.setOnClickListener(this);

        emoticonsCover = (LinearLayout) findViewById(R.id.footer_for_emoticons);

        parentLayout = findViewById(R.id.list_parent);


        MyEmotionsUtil.getInstance().setVisibleEmotion(callBack);
        MyEmotionsUtil.getInstance().initView(this, bodyView, bottomBar_emotion, emoticonsCover, parentLayout);

        bottomBar_emotion.setVisibility(View.GONE);

//		mainView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//
//            @Override
//            public void onGlobalLayout() {
//                
//
//            }
//        });

    }


    private void addImage() {
        if (bitmap != null) {
            return;
        }
        postMessage(POPUP_DIALOG_LIST, "选择图片", R.array.select_head_items,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                // 拍摄
                                BitmapUtil.doTakePhoto(LSClubPublish2Activity.this);
                                break;
                            case 1:
                                // 相册
                                //BitmapUtil
                                //		.doPickPhotoFromGallery(LSClubPublish2Activity.this);


                                Intent intent = new Intent(LSClubPublish2Activity.this, LSImagePicker.class);
                                startActivity(intent);

                                break;
                        }
                    }
                });
    }

//	private void changeButtonBg() {
//		if (bitmap == null) {
//			b.setCompoundDrawables(add, null, null, null);
//		} else {
//			b.setCompoundDrawables(noadd, null, null, null);
//		}
//	}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            switch (requestCode) {
                case C.PHOTO_PICKED_WITH_DATA:
                    Uri photo_uri = data.getData();
                    bitmap = BitmapUtil.getThumbnail(photo_uri, this);
                    imagePanel.setVisibility(View.VISIBLE);
                    imageView.setImageBitmap(bitmap);
                    break;
                case C.PICKED_WITH_DATA:
                    break;
                case C.CAMERA_WITH_DATA:
                    File file = new File(C.HEAD_IMAGE_PATH + "temp.jpg");

                    bitmap = ImageUtil.getUpdataBitmap(file.getAbsolutePath());

//				bitmap = BitmapUtil.getThumbnail(file, this);
                    imagePanel.setVisibility(View.VISIBLE);
                    imageView.setImageBitmap(bitmap);
                    break;
                case PREVIEW:
                    if (data != null) {
                        boolean delete = data.getBooleanExtra("delete", false);
                        if (delete) {
                            bitmap = null;
                            imageView.setImageBitmap(null);
                            imagePanel.setVisibility(View.GONE);
                        }
                    }
                    break;
            }
//			changeButtonBg();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private CallBack callBack = new CallBack() {
        @Override
        public void handler(MyTask mTask) {
            if ("GONE".equals(mTask.getresult())) {
                addEmotion.setImageResource(R.drawable.emotion_face);
            } else {
                addEmotion.setImageResource(R.drawable.emotion_keybody);
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (MyEmotionsUtil.getInstance().onKeyDown(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
