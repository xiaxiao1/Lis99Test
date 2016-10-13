package com.lis99.mobile.newhome.activeline;

import android.os.Bundle;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;

/**
 * Created by yy on 16/10/11.
 *      本地俱乐部
 */
public class NativeClubActivity extends LSBaseActivity {

    private String cityName = "北京", locationCityName = "", locationCityId = "";

    //    本地活动
    private LSActiveLineNewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.native_active_activity);

    }

    @Override
    protected void initViews() {
        super.initViews();
    }


    private void getList ()
    {

    }

    private void cleanList ()
    {

    }

}
