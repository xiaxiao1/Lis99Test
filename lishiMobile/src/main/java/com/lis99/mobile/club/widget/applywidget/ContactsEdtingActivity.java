package com.lis99.mobile.club.widget.applywidget;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.model.IdModel;
import com.lis99.mobile.club.model.NewApplyUpData;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.ContactsUtil;

import java.util.HashMap;

/**
 * Created by yy on 16/8/8.
 */
public class ContactsEdtingActivity extends LSBaseActivity {

    private LinearLayout layoutName;
    private LinearLayout layoutSex;
    private RadioGroup radioGroup;
    private RadioButton radioMan;
    private RadioButton radioWoman;
    private LinearLayout layoutPhone;
    private LinearLayout layoutIdcode;

    private EditText nameView, phoneView, idNumView;
    private CheckBox checkbox;

    private NewApplyUpData info;

    private boolean isNew = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.contacts_eding_info);

        info = (NewApplyUpData) getIntent().getSerializableExtra("INFO");

        initViews();

        if ( info == null )
        {
            info = new NewApplyUpData();
            isNew = true;
            return;
        }

        nameView.setText(info.name);
        phoneView.setText(info.mobile);
        idNumView.setText(info.credentials);
        checkbox.setChecked("1".equals(info.is_default));

        if ( !TextUtils.isEmpty(info.sex) )
        {
            if ( "男".equals(info.sex))
            {
                radioGroup.check(R.id.radioMan);
            }
            else if ( "女".equals(info.sex))
            {
                radioGroup.check(R.id.radioWoman);
            }
        }
        else
        {
            radioGroup.check(R.id.radioMan);
            info.sex = "男";
        }


    }


    @Override
    protected void initViews() {
        super.initViews();

        layoutName = (LinearLayout) findViewById(R.id.layout_name);
        layoutSex = (LinearLayout) findViewById(R.id.layout_sex);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioMan = (RadioButton) findViewById(R.id.radioMan);
        radioWoman = (RadioButton) findViewById(R.id.radioWoman);
        layoutPhone = (LinearLayout) findViewById(R.id.layout_phone);
        layoutIdcode = (LinearLayout) findViewById(R.id.layout_idcode);

        nameView = (EditText) findViewById(R.id.nameView);
        phoneView = (EditText) findViewById(R.id.phoneView);
        idNumView = (EditText) findViewById(R.id.idNumView);
        checkbox = (CheckBox) findViewById(R.id.checkbox);

        findViewById(R.id.btn_ok).setOnClickListener(this);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioMan) {
                    // Common.log("man");
//                    sex = "1";
                    info.sex = "男";
                } else {
                    // Common.log("Woman");
//                    sex = "0";
                    info.sex = "女";
                }
            }
        });

        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( isChecked )
                {
                    info.is_default = "1";
                }
                else
                {
                    info.is_default = "0";
                }
            }
        });

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ok:
                //TODO implement
                if ( isNew )
                {
                    addNewInfo();
                }
                else
                {
                    updataInfo();
                }


                break;
        }
    }

    private HashMap<String, Object> getMap ()
    {
        HashMap<String, Object> map = new HashMap<>();
        String mobile = phoneView.getText().toString();
        String name = nameView.getText().toString();
        String idNum = idNumView.getText().toString();
        if ( TextUtils.isEmpty(name) )
        {
            Common.toast("姓名不能为空");
            return null;
        }
        else if ( mobile.length() != 11 )
        {
            Common.toast("手机号码格式错误");
            return null;
        }
        else if (!idNum.matches("[0-9]{18}") && !idNum.matches("[0-9]{17}(X|x)"))
        {
            Common.toast("身份证号格式不正确");
            return null;
        }

        String userid = Common.getUserId();
        map.put("user_id", userid);
        map.put("name", name);
        map.put("sex", info.sex);
        map.put("mobile", mobile);
        map.put("credentials", idNum);
        map.put("is_default", info.is_default);

        return map;
    }

    private void updataInfo ()
    {
        HashMap<String, Object> map = getMap();

        if ( map == null )
        {
            return;
        }

        map.put("id", info.id);

        ContactsUtil.getInstance().updataContacts(map, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                Common.toast("修改成功");
                sendResult();
            }
        });
    }

    private void addNewInfo ()
    {
        HashMap<String, Object> map = getMap();

        if ( map == null )
        {
            return;
        }

        ContactsUtil.getInstance().addContacts(map, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                IdModel model = (IdModel) mTask.getResultModel();
                if ( model == null ) return;
                if ( !"0".equals(model.id))
                {
                    Common.toast("添加成功");
                    sendResult();
                }
            }
        });
    }



    private void sendResult ()
    {
        setResult(RESULT_OK);
        finish();
    }

}
