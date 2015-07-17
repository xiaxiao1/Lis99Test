package com.lis99.mobile.entry;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.application.data.UserBean;
import com.lis99.mobile.club.model.BaseModel;
import com.lis99.mobile.club.model.HeadMobel;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.util.BitmapUtil;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.RequestParamUtil;
import com.lis99.mobile.util.SharedPreferencesHelper;
import com.lis99.mobile.util.StatusUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.HashMap;

public class LsSettingInfoActivity extends ActivityPattern {

    ImageView iv_back;
    TextView tv_email, tv_city, tv_save;
    RoundedImageView roundedImageView1;
    EditText tv_nickname;
    private static final int LOGIN_SUCCESS = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ls_setting_info);
        StatusUtil.setStatusBar(this);
        setView();
        setListener();
        postMessage(POPUP_PROGRESS, getString(R.string.sending));
        getUserInfoTask();
    }

    private void getUserInfoTask() {
        Task task = new Task(null, C.USER_INFO_URL + DataManager.getInstance().getUser().getUser_id(), null, "USER_INFO_URL", this);
        task.setPostData(getLoginParams().getBytes());
        publishTask(task, IEvent.IO);
    }

    private String getLoginParams() {
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("user_id", DataManager.getInstance().getUser().getUser_id());
        return RequestParamUtil.getInstance(this).getRequestParams(params);
    }

    @Override
    public void handleTask(int initiator, Task task, int operation) {
        super.handleTask(initiator, task, operation);
        String result;
        switch (initiator) {
            case IEvent.IO:
                if (task.getData() instanceof byte[]) {
                    result = new String((byte[]) task.getData());
                    if (((String) task.getParameter()).equals("USER_INFO_URL")) {
                        parserInfo(result);
                    }
                }
                break;
            default:
                break;
        }
        postMessage(DISMISS_PROGRESS);
    }

    private void parserInfo(String params) {
        String result = DataManager.getInstance().validateResult(params);
        if (result != null) {
            if ("OK".equals(result)) {
                DataManager.getInstance().jsonParse(params, DataManager.TYPE_USER_INFO);
                postMessage(LOGIN_SUCCESS);
            } else {
                postMessage(POPUP_TOAST, result);
            }
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (super.handleMessage(msg))
            return true;
        switch (msg.what) {
            case LOGIN_SUCCESS:
                UserBean user = DataManager.getInstance().getUser();
                tv_email.setText(user.getEmail());
                tv_nickname.setText(user.getNickname());
                tv_city.setText(user.getCity());
//			user_head.setImage(user.getHeadicon(), null, null, "zhuangbei_detail");
                ImageLoader.getInstance().displayImage(user.getHeadicon(), roundedImageView1, ImageUtil.getclub_topic_headImageOptions());
                break;
        }
        return true;
    }

    private void setView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_email = (TextView) findViewById(R.id.tv_email);
        tv_nickname = (EditText) findViewById(R.id.tv_nickname);
        tv_city = (TextView) findViewById(R.id.tv_city);
        roundedImageView1 = (RoundedImageView) findViewById(R.id.roundedImageView1);
        tv_save = (TextView) findViewById(R.id.tv_save);

    }

    private void setListener() {
        iv_back.setOnClickListener(this);
        tv_save.setOnClickListener(this);
        roundedImageView1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == iv_back.getId()) {
            finish();
        } else if (v.getId() == roundedImageView1.getId()) {
            addImage();
        } else if (v.getId() == tv_save.getId()) {
            Common.hideSoftInput(this);
            upName();
//			upHead();
        }
    }

    String userId;
    Bitmap bitmap;

    public void upName() {
        BaseModel model = new BaseModel();
        String url = C.USER_SAVE_NICKNAME_URL;
        final String nickName = tv_nickname.getText().toString().trim();
        if (TextUtils.isEmpty(nickName)) {
            Common.toast("昵称不能为空");
            return;
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        userId = DataManager.getInstance().getUser().getUser_id();

        map.put("user_id", userId);
        map.put("nickname", nickName);

        MyRequestManager.getInstance().requestPost(url, map, model, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                SharedPreferencesHelper.savenickname(nickName);
                DataManager.getInstance().getUser().setNickname(nickName);
                upHead();
            }
        });
    }

    public void upHead() {
        if (bitmap == null) {
            Common.toast("保存成功");
            return;
        }
        if (userId == null)
            userId = DataManager.getInstance().getUser().getUser_id();
        HeadMobel model = new HeadMobel();

        String url = C.USER_SAVEPHOTO_URL;

        HashMap<String, Object> map = new HashMap<String, Object>();

        map.put("user_id", userId);
        map.put("photo", BitmapUtil.bitampToByteArray(bitmap));

        MyRequestManager.getInstance().requestImage(url, map, model, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                HeadMobel model = (HeadMobel) mTask.getResultModel();
                if (model != null) {
                    String img = model.img;
                    if (!TextUtils.isEmpty(img)) {
                        SharedPreferencesHelper.saveheadicon(img);
                        DataManager.getInstance().getUser().setHeadicon(img);
                    }
                }
                Common.toast("保存成功!");
            }
        });
    }

    private void addImage() {
//		if (bitmap != null) {
//			return;
//		}
        postMessage(POPUP_DIALOG_LIST, "选择图片", R.array.select_head_items,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                // 拍摄
                                BitmapUtil.doTakePhoto(LsSettingInfoActivity.this);
                                break;
                            case 1:
                                // 相册
                                BitmapUtil
                                        .doPickPhotoFromGallery(LsSettingInfoActivity.this);
                                break;
                        }
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            switch (requestCode) {
                case C.PHOTO_PICKED_WITH_DATA:
                    Uri photo_uri = data.getData();
                    bitmap = Bitmap.createScaledBitmap(BitmapUtil.getThumbnail(photo_uri, this), 200, 200, true);
                    break;
                case C.PICKED_WITH_DATA:
                    break;
                case C.CAMERA_WITH_DATA:
                    File file = new File(C.HEAD_IMAGE_PATH + "temp.jpg");
                    bitmap = Bitmap.createScaledBitmap(BitmapUtil.getThumbnail(file, this), 200, 200, true);
                    break;
            }
            setImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setImage() {
        roundedImageView1.setImageBitmap(bitmap);
    }


}
