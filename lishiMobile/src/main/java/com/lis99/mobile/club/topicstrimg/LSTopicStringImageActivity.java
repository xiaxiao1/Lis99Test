package com.lis99.mobile.club.topicstrimg;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.lis99.mobile.entry.view.CustomProgressDialog;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.DeviceInfo;
import com.lis99.mobile.util.DialogManager;
import com.lis99.mobile.util.FileUtil;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.LSRequestManager;
import com.lis99.mobile.util.LSScoreManager;
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
//    加上标题一共6条数据
    private int max = 6;
//    等待提示
    private CustomProgressDialog customProgressDialog;

    private boolean isSending;

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
            topicId = getIntent().getIntExtra("topicId", 0);

            if (TextUtils.isEmpty(clubName))
            {
                clubName = "户外范";
            }

            //        添加
            model.clubId = ""+clubID;
            model.clubName = clubName;
            model.isAdd = isAdd;
            model.topicId = ""+topicId;

            DataHelp.getInstance().addDraft(model);

        }
        else
        {
            clubID = Common.string2int(model.clubId);
            clubName = model.clubName;
            isAdd = model.isAdd;
            topicId = Common.string2int(model.topicId);
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
            if ( model.item.size() < max && (model.item.size() == 1 || !TextUtils.isEmpty(model.item.get(model.item.size() - 1).img)) )
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
    synchronized protected void rightAction() {
        super.rightAction();

        String title = model.title;//titleView.getText().toString().trim();
//        String body = bodyView.getText().toString().trim();

        if (TextUtils.isEmpty(title) && model.item != null && model.item.size() != 0 ) {

            title = model.item.get(0).content;
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

        if ( isSending )
        {
            Common.toast("正在上传中...");
            return;
        }



        if (TextUtils.isEmpty(title)) {
            postMessage(POPUP_TOAST, "标题不能为空");
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
                    String info = FileUtil.readFileToString(path);
                    files.add(info);
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

            contents.add(TextUtils.isEmpty(item.content) ? "" : item.content);

        }
////      如果最后一条是空， 删除
//        if ( fileName.size() > 0 && contents.size() > 0 )
//        {
//            int fnum = fileName.size() - 1;
//            int cnum = contents.size() - 1;
//
//            if ( fnum == cnum )
//            {
//                String str = files.get(fnum) + fileName.get(fnum) + contents.get(fnum);
//                if ( TextUtils.isEmpty(str) )
//                {
//                    fileName.remove(fnum);
//                    files.remove(fnum);
//                    contents.remove(fnum);
//                }
//            }
//        }

        RequestParams params = null;

        if ( isAdd )
        {
            params = getAddTopic(userID, files, contents, fileName);
        }
        else
        {

//            如果发布图片没有正文补充文字“分享图片”如果没有图片也没有正文，提示编辑正文。
            String strc = contents.get(0);
            String imgPath = files.get(0);
            if ( TextUtils.isEmpty(imgPath) && TextUtils.isEmpty(strc) )
            {
                Common.toast("请编辑正文");
                return;
            }
            else if ( TextUtils.isEmpty(strc))
            {
                contents.set(0, "分享图片");
            }


            params = getNewTopic(title, userID, files, contents, fileName);
        }

        AsyncHttpClient client = new AsyncHttpClient();

        try {

            Common.log("params == "+params.toString());
        }catch (Exception e)
        {
            Common.log(""+e.toString());
        }


        String url = C.REPLY_NEW_TOPIC_STRING_IMAGE;
        if ( isAdd )
        {
            url = C.REPLY_NEW_TOPIC_STRING_IMAGE_ADD;
        }

        isSending = true;

//        超时时间
        client.setTimeout(20000);

        client.post(url,
                params,
                new JsonHttpResponseHandler()
                {

            @Override
            public void onStart() {
//                postMessage(ActivityPattern1.POPUP_PROGRESS,
//                        getString(R.string.sending));
                		if (customProgressDialog == null) {
			customProgressDialog = CustomProgressDialog.getInstance(activity);
		}
		if (customProgressDialog != null
				&& customProgressDialog.isShow() == false) {
			customProgressDialog.popup(this, activity, "提示", "正在上传...");
		}
            }


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                String errorCode = response.optString("status", "");

                if ( customProgressDialog != null && customProgressDialog.isShowing() )
                {
                    customProgressDialog.cancel();
                    customProgressDialog = null;
                }

                if ("OK".equals(errorCode)) {
                    postMessage(POPUP_TOAST, "发布成功");

                    Common.log("发布成功");

                    removeAll();
//                  加入俱乐部
                    LSRequestManager.getInstance().addClub(""+clubID, null);


                    String data = response.optString("data", "");
                    Common.log("result="+data);
                    if (!TextUtils.isEmpty(data)) {
                        try {
                            JSONObject j = new JSONObject(data);

                            topicId = j.optInt("topics_id", -1);
                            if ( topicId != -1 ) {
//                                增加积分
                                LSScoreManager.getInstance().sendScore(LSScoreManager.pubtopics, "" + topicId);
//                                追加不需要跳转
                                if ( !getIsAdd() )
                                Common.goTopic(activity, 3, topicId);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    setResult(RESULT_OK);
                    finish();
                }
                else
                {
                    Common.log("Post Error ="+response.toString());
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString,
                                  Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                if ( customProgressDialog != null && customProgressDialog.isShowing() )
                {
                    customProgressDialog.cancel();
                    customProgressDialog = null;
                }

                Common.log("Http Post Fail responseString="+responseString);

            }

                    @Override
            public void onFinish() {
//                postMessage(DISMISS_PROGRESS);
                        isSending = false;

            }

        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if ( customProgressDialog != null && customProgressDialog.isShowing() )
        {
            customProgressDialog.cancel();
            customProgressDialog = null;
        }
    }

    private RequestParams getNewTopic (String title, String userId, List<String> files, List<String> content, List<String> filename )
    {
        RequestParams params = new RequestParams();
        params.put("club_id", clubID);
        params.put("user_id", userId);
        params.put("title", title);
        params.put("source", DeviceInfo.PLATFORM);
        params.put("ext", filename);
        params.put("content", content);
        params.put("thumb", files);
        return params;
    }

    private RequestParams getAddTopic ( String userId, List<String> files, List<String> content, List<String> filename )
    {
        RequestParams params = new RequestParams();
        params.put("user_id", userId);
        params.put("topic_id", topicId);
        params.put("ext", filename);
        params.put("content", content);
        params.put("thumb", files);
        return params;
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
                    public void handler(com.lis99.mobile.engine.base.MyTask mTask) {

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

    //选择图片列表
    private ArrayList<String> uris;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        uris = (ArrayList<String>) intent.getStringArrayListExtra("uris");

        new MyTask().execute("");

    }
//  添加选择图片
    protected void addImage (  )
    {
        if (uris != null && uris.size() > 0 && model.item != null && index != -1 && adapter != null ) {
//            url = uris.get(0);

            for ( int i = 0 ; i < uris.size(); i++ )
            {
                if ( i == 0 )
                {
                    //              设置图片地址
                    String uri = uris.get(0);

                    StringImageChildModel childModel = model.item.get(index);

                    childModel.img = ImageUtil.saveTopicImg(this, uri);

                    //                保存上一条图片到数据库, 条件可编辑状态的
                    StringImageChildModel childAdd = model.item.get(index-1);
                    if ( childAdd != null && childAdd.isEditing == 0 )
                    {

                        DataHelp.getInstance().addItem(childAdd);
                    }
//                  保存本条数据
                    DataHelp.getInstance().addItem(childModel);

                }
                else
                {
                    //              设置图片地址
                    String uri = uris.get(i);

                    StringImageChildModel childModel = new StringImageChildModel();

                    childModel.img = ImageUtil.saveTopicImg(this, uri);

                    childModel.parentId = model.id;

                    model.item.add(childModel);
//                  保存本条数据
                    DataHelp.getInstance().addItem(childModel);

                }
            }

//            保存标题内容
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

//                isNeedAdd();

        }
    }

/**
 *  检查是否需要显示增加一条
  */
    protected void isNeedAdd ()
    {
        //                小于规定数量， 增加一条
        if ( model.item.size() < max && !TextUtils.isEmpty(model.item.get(model.item.size() - 1).img) )
        {
            StringImageChildModel item = new StringImageChildModel();
            item.parentId = model.id;
            model.item.add(item);
        }


        adapter.notifyDataSetChanged();
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
        //可选择的总数
        LSImagePicker.MAX_COUNT = max - index;
        startActivity(intent);
    }

    private void removeAll ()
    {
//        if ( model.item != null )
//        {
//            for (StringImageChildModel info : model.item )
//            {
//                DataHelp.getInstance().removeItem(info);
//            }
//        }
        DataHelp.getInstance().removeDraft(model);

        DataHelp.getInstance().removeItem(model);

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

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Common.hideSoftInput(activity);
//            sendResult();
            showSaveDialog();
            return super.onKeyDown(keyCode, event);
        }


        return super.onKeyDown(keyCode, event);
    }


    //  结束前保存
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("DATA_MODEL", model);
    }

    private void showSaveDialog ()
    {
        DialogManager.getInstance().startAlert(this, "保存", "内容是否保存草稿箱", true, "确定", new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendResult();
                Common.toast("内容保存草稿箱");
            }
        }, true, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeAll();
                setResult(RESULT_OK);
                finish();
            }
        });
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


    class MyTask extends AsyncTask
    {

        /**
         * Runs on the UI thread before {@link #doInBackground}.
         *
         * @see #onPostExecute
         * @see #doInBackground
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            DialogManager.getInstance().startWaiting(activity, null, "图片加载中...");
        }

        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         * <p/>
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param o The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            DialogManager.getInstance().stopWaitting();
            isNeedAdd();

        }

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p/>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected Object doInBackground(Object[] params) {

            addImage();
            return null;
        }
    }


}
