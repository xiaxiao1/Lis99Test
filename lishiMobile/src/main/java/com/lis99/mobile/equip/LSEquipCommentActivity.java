package com.lis99.mobile.equip;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import com.fasterxml.jackson.databind.JsonNode;
import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.ActivityPattern1;
import com.lis99.mobile.newhome.LSFragment;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.RequestParamUtil;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;

public class LSEquipCommentActivity extends LSBaseActivity {

    RatingBar ratingBar;
    EditText commentText;
    int equipID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        equipID = getIntent().getIntExtra("equipID", 0);
        setContentView(R.layout.activity_lsequip_comment);
        initViews();
    }

    @Override
    protected void initViews() {
        super.initViews();
        ratingBar = (RatingBar) findViewById(R.id.ratingBar1);
        commentText = (EditText) findViewById(R.id.editText);

//        View titleRight = findViewById(R.id.titleRight);
//        titleRight.setOnClickListener(this);
    }

    @Override
    protected void rightAction() {
        addComment();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.addCommentButton || view.getId() == R.id.titleRightImage) {
            addComment();
            return;
        }
        super.onClick(view);
    }

    private void addComment(){
        if ( !Common.isLogin(activity) )
        {
            return;
        }

        int rate = (int)ratingBar.getRating();
        if (rate == 0) {
            Common.toast("请为装备打个分");
            return;
        }

        String comment = commentText.getText().toString();
        if (StringUtils.isEmpty(comment)) {
            Common.toast("请填写内容");
            return;
        }

        String url = C.ADD_COMMENT;

        String uid = DataManager.getInstance().getUser().getUser_id();

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("user_id", uid);
        params.put("zhuangbeiid", equipID);
        params.put("content", comment);
        params.put("star", rate);

        Task task = new Task(null, url, C.HTTP_POST, C.ADD_COMMENT,
                this);
        task.setPostData(RequestParamUtil.getInstance(this)
                .getRequestParams(params).getBytes());
        publishTask(task, IEvent.IO);

    }


    @Override
    public void handleTask(int initiator, Task task, int operation) {
        super.handleTask(initiator, task, operation);
        String result;
        switch (initiator) {
            case IEvent.IO:
                if (task.getData() instanceof byte[]) {
                    result = new String((byte[]) task.getData());
                    if (((String) task.getParameter()).equals(C.ADD_COMMENT)) {
                        parserAddInfo(result);
                    }
                }
                break;
            default:
                break;
        }
        postMessage(DISMISS_PROGRESS);
    }

    private void parserAddInfo(String result) {
        try {
            JsonNode root = LSFragment.mapper.readTree(result);
            String errCode = root.get("status").asText("");
            if (!"OK".equals(errCode)) {
                String errMsg = root.get("data").get("error").asText("");;
                postMessage(ActivityPattern1.POPUP_TOAST, errMsg);
                return;
            }

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                   Common.toast("评论已提交，审核通过后显示");
                    finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postMessage(ActivityPattern1.DISMISS_PROGRESS);
        }
    }


}
