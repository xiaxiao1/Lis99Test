package com.lis99.mobile.newhome.sysmassage;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.model.BenefitListModel;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.DialogManager;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.LSRequestManager;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;

/**
 * Created by yy on 16/5/24.
 */
public class MyBenefitInfoActivity extends LSBaseActivity implements View.OnClickListener {


    private ImageView iv_title_bg;
    private ImageView titleLeftImage;
    private RelativeLayout titleLeft;
    private TextView titleRightText;
    private ImageView titleRightImage;
    private RelativeLayout titleRight;
    private TextView title;
    private RelativeLayout titlehead;
    private ImageView iv_icon;
    private TextView tv_title;
    private TextView tv_content;
    private Button btn_ok;
    private RelativeLayout view_bg;
    private TextView tv_readme;

    private Object info;
    private BenefitListModel.BenefitItem item;
//领取
    private boolean isGet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.my_benefit_info);

        info = getIntent().getSerializableExtra("OBJECT");

        initView();

        initViews();

    }


    @Override
    protected void initViews() {
        super.initViews();

        setTitle("我的福利");

        item = (BenefitListModel.BenefitItem) info;

        tv_title.setText(item.title);
        tv_content.setText(item.enddate+" 日前领取");
        tv_readme.setText(item.readme);


        switch (item.flag )
        {
            case 1:
                view_bg.setBackgroundResource(R.drawable.my_benefit_item_bg_pass);
                btn_ok.setBackgroundResource(R.drawable.btn_benefit_had);
                break;
            case 2:
                view_bg.setBackgroundResource(R.drawable.my_benefit_item_bg_pass);
                btn_ok.setBackgroundResource(R.drawable.btn_benefit_pass);
                break;
            case 3:
                view_bg.setBackgroundResource(R.drawable.my_benefit_item_bg);
                btn_ok.setBackgroundResource(R.drawable.btn_benefit_have);
                break;
        }

        switch ( item.type )
        {
            case 1:
                ImageLoader.getInstance().displayImage(item.thumb, iv_icon, ImageUtil.getDefultImageOptions());
                break;
            case 2:
                iv_icon.setImageResource(R.drawable.icon_benefit_integral);
                break;
            case 3:
                iv_icon.setImageResource(R.drawable.icon_benefit_coupon_out);
                break;
            case 4:
                iv_icon.setImageResource(R.drawable.icon_benefit_coupon_my);
                break;
            default:

                break;
        }



    }


    private void initView() {
        iv_title_bg = (ImageView) findViewById(R.id.iv_title_bg);
        titleLeftImage = (ImageView) findViewById(R.id.titleLeftImage);
        titleLeft = (RelativeLayout) findViewById(R.id.titleLeft);
        titleRightText = (TextView) findViewById(R.id.titleRightText);
        titleRightImage = (ImageView) findViewById(R.id.titleRightImage);
        titleRight = (RelativeLayout) findViewById(R.id.titleRight);
        title = (TextView) findViewById(R.id.title);
        titlehead = (RelativeLayout) findViewById(R.id.titlehead);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_content = (TextView) findViewById(R.id.tv_content);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        view_bg = (RelativeLayout) findViewById(R.id.view_bg);
        tv_readme = (TextView) findViewById(R.id.tv_readme);

        btn_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:

                if ( item.type == 2 && item.flag == 3 )
                {

                    HashMap<String, Object> map = new HashMap<>();
                    String userId = Common.getUserId();

                    map.put("user_id", userId);
                    map.put("welfare_id", item.welfare_id);
                    map.put("type", item.type);
                    map.put("platfrom", "Android");

                    LSRequestManager.getInstance().getIntegral(map, callBack);
                }


                break;
        }
    }



//    //        领取积分
//    private void getIntegral (final BenefitListModel.BenefitItem item )
//    {
//        String url = C.My_BENEFIT_INTEGRAL;
//
//        HashMap<String, Object> map = new HashMap<>();
//
//        String userId = Common.getUserId();
//
//        map.put("user_id", userId);
//        map.put("welfare_id", item.welfare_id);
//        map.put("type", item.type);
//        map.put("platfrom", "Android");
//
//        BaseModel model = new BaseModel();
//
//        MyRequestManager.getInstance().requestPost(url, map, model, );
//
//
//    }


    private CallBack callBack = new CallBack() {
        @Override
        public void handler(MyTask mTask) {

            if ( mTask  == null || mTask.getResultModel() == null )
            {
                Common.toast("抱歉，领取积分失败，请稍后重试");
                return;
            }

            String content = String.format("您已成功领取%s积分。", item.content);
            DialogManager.getInstance().startAlert(activity, "领取积分", content, true, "确定", new DialogInterface.OnClickListener() {


                @Override
                public void onClick(DialogInterface dialog, int which) {

                    item.flag = 1;

                    isGet = true;

                    view_bg.setBackgroundResource(R.drawable.my_benefit_item_bg_pass);
                    btn_ok.setBackgroundResource(R.drawable.btn_benefit_had);

                }
            }, false, "", null);


        }
    };

    @Override
    protected void leftAction() {

        onBackPressed();

    }

    @Override
    public void onBackPressed() {

        if ( isGet )
        setResult(RESULT_OK);
        finish();

        super.onBackPressed();

    }
}
