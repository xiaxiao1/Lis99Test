package com.lis99.mobile.club.topicstrimg;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.LSImagePicker;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.ActivityPattern1;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.DeviceInfo;
import com.lis99.mobile.util.FileUtil;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.PopWindowUtil;
import com.lis99.mobile.util.dbhelp.DataHelp;
import com.lis99.mobile.util.dbhelp.StringImageChildModel;
import com.lis99.mobile.util.dbhelp.StringImageModel;
import com.lis99.mobile.util.emotion.MyEmotionsUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.view.KeyEvent.KEYCODE_BACK;

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
    protected ListView list;

    private int position = 0;

    private int clubID, topicId;

    private String clubName;

    private TopicStringImageAdapter adapter;

    private StringImageModel model;

    //表情
    protected ImageView addEmotion;
    protected View bottomBar_emotion;

    protected LinearLayout emoticonsCover;

    protected View parentLayout;

    private boolean isAdd;

    private String titleInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.topic_img_string_main);


        model = (StringImageModel) getIntent().getSerializableExtra("DATA_MODEL");

        if ( savedInstanceState != null )
        {
            model = (StringImageModel) savedInstanceState.getSerializable("DATA_MODEL");
        }

//        发帖
        if ( model == null )
        {
            model = new StringImageModel();

            clubID = getIntent().getIntExtra("clubID", 48);
            clubName = getIntent().getStringExtra("clubName");

            isAdd = getIntent().getBooleanExtra("ADD", false);
            titleInfo = getIntent().getStringExtra("TITLE");

            if (TextUtils.isEmpty(clubName))
            {
                clubName = "户外范";
            }

            //        添加
            model.clubId = ""+clubID;
            model.clubName = clubName;
            model.isAdd = isAdd;

            DataHelp.getInstance().addDraft(model);

        }
        else
        {
            clubID = Common.string2int(model.clubId);
            clubName = model.clubName;
            isAdd = model.isAdd;
        }

//        Common.log("parentId="+model.id);
//      查找刚刚添加的
//        model = DataHelp.getInstance().getDraftsAt();

//        Common.log("parentId="+model.id);


        model.item = DataHelp.getInstance().searchItemInDraft(model);

        if ( model.item == null || model.item.size() == 0 )
        {
            model.item = new ArrayList<>();
            //title
            StringImageChildModel item = new StringImageChildModel();
            item.parentId = model.id;

            if ( !TextUtils.isEmpty(titleInfo) )
            {
                item.content = titleInfo;
            }

            model.item.add(item);
            //图文
            item = new StringImageChildModel();
            item.parentId = model.id;
            model.item.add(item);
        }


//        最后一条， 图片应该是空的， 不是的话， 增加一条空的
        if ( model.item != null && model.item.size() != 0 )
        {
//            有添加过数据， 清空图片不存在的项目
            if ( model.item.size() > 2 )
            {
                for ( int i = 1; i < model.item.size() - 1; i++ )
                {
                    StringImageChildModel item = model.item.get(i);
                    if ( TextUtils.isEmpty(item.img) )
                    {
                        model.item.remove(item);
                        DataHelp.getInstance().removeItem(item);
                    }

                }
            }
//          判断最后一项是否为空， 不是添加空
            if ( model.item.size() == 1 || !TextUtils.isEmpty(model.item.get(model.item.size() - 1).img) )
            {
                //图文
                StringImageChildModel item = new StringImageChildModel();
                item.parentId = model.id;
                model.item.add(item);
            }

        }

        initViews();

//        getListHeight();

        setTitle(model.clubName);

    }
//    是否为追加
    protected boolean getIsAdd ()
    {
        return isAdd;
    }

//    发布
    @Override
    protected void rightAction() {
        super.rightAction();

        String title = model.title;//titleView.getText().toString().trim();
//        String body = bodyView.getText().toString().trim();

        if (TextUtils.isEmpty(title) && model.item != null && model.item.size() != 0 ) {

            title = model.item.get(0).content;
        }

        if (TextUtils.isEmpty(title)) {
            postMessage(POPUP_TOAST, "标题不能为空");
            return;
        }

//        if (TextUtils.isEmpty(body)) {
//            postMessage(POPUP_TOAST, "正文不能为空");
//            return;
//        }

        if ( !Common.isLogin(this))
        {
            postMessage(POPUP_TOAST, "请先登录");
            return;
        }

        String userID = DataManager.getInstance().getUser().getUser_id();

        List<String> contents = new ArrayList<>();

        List<String> files = new ArrayList<>();
        List<String> fileName = new ArrayList<>();

        for ( int i = 1; i < model.item.size(); i++ )
        {
            StringImageChildModel item = model.item.get(i);


            if ( !TextUtils.isEmpty(item.img))
            {
                String path = item.img.replace("file://", "");
                File file = new File(path);

                if ( file.exists() )
                {
                    files.add(FileUtil.readFileByBytes(path));
                    String name = item.img.substring(item.img.lastIndexOf("."), item.img.length());
                    fileName.add(name);

                }
                else
                {
                    Common.log("not found path = " + path);
                    files.add("");
                    fileName.add("");
                }



            }
            else
            {
//                File file = new File("");
                files.add("");
                fileName.add("");
            }

            contents.add(item.content);

        }


        AsyncHttpClient client = new AsyncHttpClient();


        RequestParams params = new RequestParams();
        params.put("club_id", clubID);
        params.put("user_id", userID);
        params.put("title", title);
        params.put("source", DeviceInfo.PLATFORM);
        params.put("content", contents);
        params.put("thumb", files);
        params.put("ext", fileName);

//        if ( files.size() != 0 )
//        {
//            Bitmap b = BitmapFactory.decodeByteArray(files.get(0), 0, files.get(0).length);
//            if ( b == null )
//            {
//                Common.toast("b ==== null");
//                return;
//            }
//            iv.setImageBitmap(b);
//            Common.toast("b !!!!!!!!==== null");
//        }
//
//        if ( true) return;

//        params.put("content", body);
//        params.put("category", 0);
//        params.put("parentid", 0);
//        if (bitmap != null)
//            params.put("thumb", new ByteArrayInputStream(BitmapUtil.bitampToByteArray(bitmap)), "image.jpg");

        client.post(C.REPLY_NEW_TOPIC_STRING_IMAGE, params, new JsonHttpResponseHandler() {

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

                    for (StringImageChildModel info : model.item )
                    {
                        DataHelp.getInstance().removeItem(info);
                    }
                    DataHelp.getInstance().removeDraft(model);

//					LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(LSClubPublish2Activity.this);
//					Intent intent = new Intent(LSClubDetailActivity.CLUB_TOPIC_CHANGE);
//					lbm.sendBroadcast(intent);

                    String data = response.optString("data", "");
                    if (!TextUtils.isEmpty(data)) {
                        try {
                            JSONObject j = new JSONObject(data);

                            int category = j.optInt("category", -1);
                            topicId = j.optInt("topicid", -1);
                            if (category != -1 && topicId != -1) {
//                                if (category == 2) {
//                                    Intent in = new Intent(activity, LSClubTopicNewActivity.class);
//                                    in.putExtra("topicID", topicId);
//                                    startActivity(in);
//                                } else {
//                                    Intent in = new Intent(activity, LSClubTopicActivity.class);
//                                    in.putExtra("topicID", topicId);
//                                    startActivity(in);
//                                }
                                Common.goTopic(activity, category, topicId);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    setResult(RESULT_OK);
                    finish();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString,
                                  Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                Common.log("Http Post Fail responseString="+responseString);

            }

            @Override
            public void onFinish() {
                postMessage(DISMISS_PROGRESS);
            }

        });

    }

    @Override
    protected void initViews() {
        super.initViews();

        //表情

        addEmotion = (ImageView) findViewById(R.id.addEmotion);

        bottomBar_emotion = findViewById(R.id.bottomBar_emotion);

        bottomBar_emotion.setOnClickListener(this);

        emoticonsCover = (LinearLayout) findViewById(R.id.footer_for_emoticons);

        parentLayout = findViewById(R.id.list_parent);

        bottomBar_emotion.setVisibility(View.GONE);


        titlehead = (RelativeLayout) findViewById(R.id.titlehead);
        titleLeft = (RelativeLayout) findViewById(R.id.titleLeft);
        titleLeftImage = (ImageView) findViewById(R.id.titleLeftImage);
        titleRight = (RelativeLayout) findViewById(R.id.titleRight);
        button1 = (TextView) findViewById(R.id.button1);
        title = (TextView) findViewById(R.id.title);
        dot = (ImageView) findViewById(R.id.dot);
        list = (ListView) findViewById(R.id.list);

        adapter = new TopicStringImageAdapter(activity, model.item);

        adapter.setMain(this);

        list.setAdapter(adapter);

//        标题不可更改
        if ( isAdd )
        {
            dot.setVisibility(View.GONE);
        }
        else
        {
            title.setOnClickListener(this);
        }

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

                        clubName = values[0];

                        title.setText(clubName);

                        position = Integer.parseInt(mTask.getresult());

                        model.clubName = clubName;
                        model.clubId = ""+clubID;

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

                StringImageChildModel childModel = model.item.get(index);

                childModel.img = ImageUtil.saveTopicImg(this, uri);
//                保存上一条图片到数据库, 条件可编辑状态的
                StringImageChildModel childAdd = model.item.get(index-1);
                if ( childAdd != null && childAdd.isEditing == 0 )
                {
//                    DataHelp.getInstance().addChild(childAdd);

                    DataHelp.getInstance().addItem(childAdd);

                    String title = model.item.get(0).content;
                    String content = model.item.get(1).content;

                    if ( !TextUtils.isEmpty(title))
                    {
                        model.title = title;
                    }
                    if ( !TextUtils.isEmpty(content))
                    {
                        model.content = content;
                    }
                    DataHelp.getInstance().addDraft(model);

                }

//                model.item.get(index).img = uri;
//              增加一条
                StringImageChildModel item = new StringImageChildModel();
                item.parentId = model.id;
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

//  保存所有数据
    private void saveAll ()
    {
        if ( model.item != null && model.item.size() >= 2 )
        {
            String title = model.item.get(0).content;
            String content = model.item.get(1).content;

            if ( !TextUtils.isEmpty(title))
            {
                model.title = title;
            }
            if ( !TextUtils.isEmpty(content))
            {
                model.content = content;
            }
            DataHelp.getInstance().addDraft(model);
        }

        DataHelp.getInstance().addItemAll(model.item);
    }

    @Override
    protected void leftAction() {
//        Common.toast("内容保存草稿箱");
//        sendResult();

        onKeyDown(KeyEvent.KEYCODE_BACK, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (MyEmotionsUtil.getInstance().onKeyDown(keyCode, event)) {
            return true;
        }

        Common.hideSoftInput(activity);

        if (keyCode == KEYCODE_BACK) {
            sendResult();
            Common.toast("内容保存草稿箱");
            return super.onKeyDown(keyCode, event);
        }


        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        super.onStop();
//      保存所有数据
//        saveAll();

    }

    //  结束前保存
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("DATA_MODEL", model);
    }

    private void sendResult()
    {
        saveAll();
        setResult(RESULT_OK);
        finish();
    }

    protected void visibleEmotionBar ( boolean b )
    {
        if ( b )
        bottomBar_emotion.setVisibility(View.VISIBLE);
        else
            bottomBar_emotion.setVisibility(View.GONE);
    }

    //  打开后读取
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//    }
}
