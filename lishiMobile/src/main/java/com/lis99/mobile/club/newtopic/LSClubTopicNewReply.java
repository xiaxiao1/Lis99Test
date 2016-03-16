package com.lis99.mobile.club.newtopic;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.entry.ActivityPattern1;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.DeviceInfo;
import com.lis99.mobile.util.LSScoreManager;
import com.lis99.mobile.util.emotion.MyEmotionsUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yy on 16/3/11.
 */
public class LSClubTopicNewReply extends LSBaseActivity {

    private TextView tvReplyBody;
    private TextView tvReplyContent;
    private EditText bodyView;
    private View btn_emotion;

    private View layoutMain;
    private LinearLayout footerForEmoticons;

    private String replyedName, replyedcontent, replyedfloor, replyedId;
    private String clubId, topicId;

    private View layout_reply_quote;

    private int pageNo = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.club_new_topic_list_reply);

        replyedName = getIntent().getStringExtra("replyedName");
        replyedcontent = getIntent().getStringExtra("replyedcontent");
        replyedfloor = getIntent().getStringExtra("replyedfloor");

        replyedId = getIntent().getStringExtra("replyedId");
        clubId = getIntent().getStringExtra("clubId");
        topicId = getIntent().getStringExtra("topicId");

        layoutMain = findViewById(R.id.layoutMain);

        layout_reply_quote = findViewById(R.id.layout_reply_quote);

        footerForEmoticons = (LinearLayout) findViewById(R.id.footer_for_emoticons);

        tvReplyBody = (TextView) findViewById(R.id.tv_reply_body);
        tvReplyContent = (TextView) findViewById(R.id.tv_reply_content);

        findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                发表回复
                publish();
            }
        });

        layoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bodyView = (EditText) findViewById(R.id.bodyView);
        btn_emotion = findViewById(R.id.btn_emotion);


        MyEmotionsUtil.getInstance().initView(this, bodyView, btn_emotion, footerForEmoticons,
                layoutMain);


        //是否有引用内容
        if ( TextUtils.isEmpty(replyedName) )
        {
            layout_reply_quote.setVisibility(View.GONE);
        }
        else
        {
            tvReplyBody.setText("回复@ "+replyedName);
//            replyedfloor.setText(replyedfloor+"楼");
            tvReplyContent.setText(replyedcontent);
            layout_reply_quote.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ( MyEmotionsUtil.getInstance().onKeyDown(keyCode, event) )
        {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void publish()
    {

//		Spannable sp = content.getText();

        String body = bodyView.getText().toString().trim();

        if (TextUtils.isEmpty(body))
        {
            Common.toast("内容不能为空");
            return;
        }

        String userID = DataManager.getInstance().getUser().getUser_id();

        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();

        params.put("topics_id", topicId);
        params.put("source", "1");
        params.put("version", DeviceInfo.CLIENTVERSIONCODE);

        params.put("title", "");
        params.put("content", body);
        params.put("category", 0);
        params.put("club_id", clubId);
        params.put("parentid", topicId);
        params.put("user_id", userID);



        if ( !TextUtils.isEmpty(replyedId))
        {
            params.put("reply_id", replyedId);
        }
//        if (bitmap != null)
//            params.put(
//                    "thumb",
//                    new ByteArrayInputStream(BitmapUtil
//                            .bitampToByteArray(bitmap)), "image.jpg");

        client.post(C.CLUB_ADD_TOPIC_REPLY, params, new JsonHttpResponseHandler()
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

//                    if ( bitmap == null )
//                    {
                        LSScoreManager.getInstance().sendScore(LSScoreManager.replytopicbynoimg, topicId);
//                    }
//                    else
//                    {
//                        LSScoreManager.getInstance().sendScore(LSScoreManager.replytopicbyimg, topicId);
//                    }

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


    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(0, R.anim.activity_close_up_down);
    }
}
