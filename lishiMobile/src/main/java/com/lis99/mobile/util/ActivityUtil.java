package com.lis99.mobile.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.lis99.mobile.choiceness.SpecialInfoActivity;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.LSClubDetailActivity;
import com.lis99.mobile.club.destination.DestinationActivity;
import com.lis99.mobile.club.newtopic.series.ApplyManagerSeries;
import com.lis99.mobile.webview.MyActivityWebView;

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
    public static void goDestinationInfo ( int tagId, int desId )
    {
        Intent intent = new Intent(LSBaseActivity.activity, DestinationActivity.class);
        intent.putExtra("destID", desId);
        intent.putExtra("tagID", tagId);
        LSBaseActivity.activity.startActivity(intent);

    }
//  跳转专栏
    public static void goSpecialInfoActivity (Context c, int tagid )
    {
        Intent intent = new Intent(c, SpecialInfoActivity.class);
        intent.putExtra("tagid", tagid);
        c.startActivity(intent);
    }

    /**
     *      跳转WebView
     */
    public static void getURLActivity (Context c, String url, String title, String imageUrl, int topicId)
    {
        Intent intent = new Intent(c, MyActivityWebView.class);
        Bundle bundle = new Bundle();
        bundle.putString("URL", url);
        bundle.putString("TITLE", title);
        bundle.putString("IMAGE_URL", imageUrl);
//        bundle.putInt("TOPIC_ID", 0);
        bundle.putInt("TOPIC_ID", topicId);
        intent.putExtras(bundle);
        c.startActivity(intent);
    }

    /**
     *  跳转俱乐部详情
     * @param c
     * @param clubId
     */
    public static void goClubInfo ( Context c, int clubId)
    {
        Intent intent = new Intent(c, LSClubDetailActivity.class);
        intent.putExtra("clubID", clubId);
        c.startActivity(intent);
    }

}
