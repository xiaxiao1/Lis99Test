package com.lis99.mobile.club.newtopic;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.util.emotion.MyEmotionsUtil;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.club_new_topic_list_reply);

        layoutMain = findViewById(R.id.layoutMain);

        footerForEmoticons = (LinearLayout) findViewById(R.id.footer_for_emoticons);

        tvReplyBody = (TextView) findViewById(R.id.tv_reply_body);
        tvReplyContent = (TextView) findViewById(R.id.tv_reply_content);

        findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        bodyView = (EditText) findViewById(R.id.bodyView);
        btn_emotion = findViewById(R.id.btn_emotion);


        MyEmotionsUtil.getInstance().initView(this, bodyView, btn_emotion, footerForEmoticons,
                layoutMain);



    }



}
