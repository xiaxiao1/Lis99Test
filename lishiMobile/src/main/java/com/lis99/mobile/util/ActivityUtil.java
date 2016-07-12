package com.lis99.mobile.util;

import android.content.Intent;

import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.newtopic.series.ApplyManagerSeries;

/**
 * Created by yy on 16/6/7.
 */
public class ActivityUtil {

    /**跳转管理报名界面*/
    public static void goActiveManager ( int topicId, int clubId )
    {
        Intent intent = new Intent(LSBaseActivity.activity, ApplyManagerSeries.class);
        intent.putExtra("topicID", topicId);
        intent.putExtra("clubID", clubId);
        LSBaseActivity.activity.startActivity(intent);
    }
//  跳转目的地详情
    public static void goDestinationInfo (  int desId )
    {
        Intent intent = new Intent(LSBaseActivity.activity, ApplyManagerSeries.class);
        intent.putExtra("topicID", desId);
        LSBaseActivity.activity.startActivity(intent);
    }

}
