package com.lis99.mobile.club.topicstrimg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.PopWindowUtil;

/**
 * Created by yy on 16/3/29.
 */
public class LSTopicStringImageActivity extends LSBaseActivity {


    private RelativeLayout titlehead;
    private RelativeLayout titleLeft;
    private ImageView titleLeftImage;
    private RelativeLayout titleRight;
    private TextView button1;
    private TextView title;
    private ImageView dot;
    private ListView list;

    private int position = 0;

    private int clubID, topicId;

    private TopicStringImageAdapter adapter;

    private StringImageModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.topic_img_string_main);



        initViews();

        model = new StringImageModel();

        


    }


    @Override
    protected void initViews() {
        super.initViews();


        titlehead = (RelativeLayout) findViewById(R.id.titlehead);
        titleLeft = (RelativeLayout) findViewById(R.id.titleLeft);
        titleLeftImage = (ImageView) findViewById(R.id.titleLeftImage);
        titleRight = (RelativeLayout) findViewById(R.id.titleRight);
        button1 = (TextView) findViewById(R.id.button1);
        title = (TextView) findViewById(R.id.title);
        dot = (ImageView) findViewById(R.id.dot);
        list = (ListView) findViewById(R.id.list);

        title.setOnClickListener(this);


    }


    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        Intent intent = null;

        switch (arg0.getId())
        {
            case R.id.title:
                dot.setImageResource(R.drawable.topic_club_up_dot);
                PopWindowUtil.showTopicClub(position, title, new CallBack() {
                    @Override
                    public void handler(MyTask mTask) {

                        dot.setImageResource(R.drawable.topic_club_down_dot);

                        if (mTask == null) {
                            return;
                        }
                        String[] values = (String[]) mTask.getResultModel();

                        clubID = Common.string2int(values[1]);

                        title.setText(values[0]);

                        position = Integer.parseInt(mTask.getresult());

                    }
                });
                break;
        }



    }

//  结束前保存
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
//  打开后读取
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//    }
}
