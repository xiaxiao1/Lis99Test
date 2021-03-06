package com.lis99.mobile.mine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.JsonNode;
import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.application.data.UserBean;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.engine.base.IEvent;
import com.lis99.mobile.engine.base.Task;
import com.lis99.mobile.entry.ActivityPattern1;
import com.lis99.mobile.newhome.LSFragment;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.LSRequestManager;
import com.lis99.mobile.util.LSScoreManager;
import com.lis99.mobile.util.RequestParamUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;

public class LSWeixinLoginActivity extends LSBaseActivity {

    DisplayImageOptions options;

    String nickName;
    int sex;
    String headerUrl;
    String openID;
    String unionid;

    ImageView headerView;
    ImageView clear_img;
    EditText nickNameView;

    private static final int LOGIN_SUCCESS = 200;

    private View iv_back;


    private void buildOptions(){
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ls_nologin_header_icon)
                .showImageForEmptyUri(R.drawable.ls_nologin_header_icon)
                .showImageOnFail(R.drawable.ls_nologin_header_icon)
                .cacheInMemory(false).cacheOnDisk(true).build();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nickName = getIntent().getStringExtra("nickName");
        openID = getIntent().getStringExtra("openID");
        headerUrl = getIntent().getStringExtra("headerUrl");
        sex = getIntent().getIntExtra("sex", 1);
        unionid = getIntent().getStringExtra("unionid");

        setContentView(R.layout.activity_ls_weixin_login);

        buildOptions();

        initViews();
    }

    @Override
    protected void initViews() {
        super.initViews();

        headerView = (ImageView) findViewById(R.id.headerView);
        nickNameView = (EditText) findViewById(R.id.nickNameView);
        clear_img=(ImageView)findViewById(R.id.clear_textview_img);
        ImageLoader.getInstance().displayImage(headerUrl, headerView, options);
        nickNameView.setText(nickName);
        nickNameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (count>0) {
                    clear_img.setVisibility(View.VISIBLE);
                }else{
                    clear_img.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count>0) {
                    clear_img.setVisibility(View.VISIBLE);
                }else{
                    clear_img.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!nickNameView.getText().toString().equals("")) {
                    clear_img.setVisibility(View.VISIBLE);
                }else{
                    clear_img.setVisibility(View.INVISIBLE);
                }
            }
        });
        clear_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nickNameView.setText("");
                clear_img.setVisibility(View.INVISIBLE);
            }
        });
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    private void login() {
        String nickName = nickNameView.getText().toString();
        if (nickName == null || "".equals(nickName)) {
            postMessage(POPUP_TOAST, "昵称不能为空");
            return;
        }
        postMessage(POPUP_PROGRESS, getString(R.string.sending));
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("openid", openID);
        params.put("nickname", nickName);
        params.put("sex", sex);
        params.put("headimgurl", headerUrl);
        params.put("unionid", unionid);

        Task task = new Task(null, C.WEIXIN_LOGIN, C.HTTP_POST, C.WEIXIN_LOGIN,
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
                    String param = (String) task.getParameter();
                    result = new String((byte[]) task.getData());
                    if (param.equals(C.WEIXIN_LOGIN)) {
                        parserLoginInfoList(result);
                    }
                }
                break;
            default:
                break;
        }
        postMessage(DISMISS_PROGRESS);
    }


    private void parserLoginInfoList(String result) {
        try {
            JsonNode root = LSFragment.mapper.readTree(result);
            String errCode = root.get("status").asText("");
            JsonNode data = root.get("data");
            if (!"OK".equals(errCode))
            {
                String error = data.get("error").asText();
                postMessage(ActivityPattern1.POPUP_TOAST, error);
                return;
            }

            UserBean u = new UserBean();
            u.setUser_id(data.get("user_id").asText());
            u.setHeadicon(headerUrl);
            nickName = data.get("nickname").asText();
            u.setNickname(nickName);
            DataManager.getInstance().setUser(u);
            DataManager.getInstance().setLogin_flag(true);

            String is_new = data.get("is_new").asText();
//            微信登陆
            if ( "1".equals(is_new))
            {
                LSScoreManager.getInstance().sendScore(LSScoreManager.register, "0");
            }

            postMessage(LOGIN_SUCCESS);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            postMessage(ActivityPattern1.DISMISS_PROGRESS);
        }

    }

    public boolean handleMessage(Message msg) {
        if (msg.what == LOGIN_SUCCESS) {
            Toast.makeText(this, "登录成功", Toast.LENGTH_LONG).show();
            LSRequestManager.getInstance().upDataInfo();
            Intent intent = new Intent();
            intent.putExtra("login", true);
            setResult(RESULT_OK, intent);
            finish();
        }
        return super.handleMessage(msg);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ls_login_bt) {
            login();
            return;
        }
        super.onClick(v);
    }
}
