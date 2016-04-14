package com.lis99.mobile.club.topicstrimg;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.LSImagePicker;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.PopWindowUtil;
import com.lis99.mobile.util.dbhelp.StringImageChildModel;
import com.lis99.mobile.util.dbhelp.StringImageModel;

import java.util.ArrayList;

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


        model = (StringImageModel) getIntent().getSerializableExtra("DATA_MODEL");

        if ( model == null && savedInstanceState != null )
        {
            model = (StringImageModel) savedInstanceState.getSerializable("DATA_MODEL");
        }

        if ( model == null )
        {
            model = new StringImageModel();
        }


        if ( model.item == null || model.item.size() == 0 )
        {
            model.item = new ArrayList<>();
            //title
            StringImageChildModel item = new StringImageChildModel();
            model.item.add(item);
            //图文
            item = new StringImageChildModel();
            model.item.add(item);
        }

        initViews();

    }

//    发布
    @Override
    protected void rightAction() {
        super.rightAction();
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

        adapter = new TopicStringImageAdapter(activity, model.item);

        adapter.setMain(this);

        list.setAdapter(adapter);

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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ArrayList<String> uris = (ArrayList<String>) intent.getStringArrayListExtra("uris");
        if (uris != null && uris.size() > 0) {
//            url = uris.get(0);
            if ( model.item != null && index != -1 && adapter != null )
            {
//              设置图片地址
                String uri = uris.get(0);

                model.item.get(index).img = ImageUtil.saveTopicImg(this, uri);

//                model.item.get(index).img = uri;
//              增加一条
                StringImageChildModel item = new StringImageChildModel();
                model.item.add(item);

                adapter.notifyDataSetChanged();
            }
        }

    }

    private int index = -1;

    /**
     *      添加图片
     */
    protected void addImage ( int index )
    {
        this.index = index;
        // 相册
        Intent intent = new Intent(activity, LSImagePicker.class);
        intent.putExtra("CLASSNAME", this.getComponentName().getClassName());
        startActivity(intent);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            return true;
        }


        return super.onKeyDown(keyCode, event);
    }

    //  结束前保存
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("DATA_MODEL", model);
    }

//  打开后读取
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//    }
}
