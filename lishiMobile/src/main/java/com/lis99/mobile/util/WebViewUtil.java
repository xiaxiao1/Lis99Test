package com.lis99.mobile.util;

import android.content.Intent;
import android.graphics.Bitmap;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.LSClubTopicActivity;
import com.lis99.mobile.club.LSClubTopicNewActivity;

/**
 * Created by yy on 15/11/6.
 */
public class WebViewUtil {


    private static WebViewUtil instance;

    public static WebViewUtil getInstance ()
    {
        if ( instance == null ) instance = new WebViewUtil();
        return instance;
    }

    public MyWebClinet getWebviewClient ()
    {
        return new MyWebClinet();
    }

    public TianJinJS getTianJinJS()
    {
        return new TianJinJS();
    }


































    class TianJinJS
    {
        public TianJinJS()
        {

        }

        //		跳转铁子
        @JavascriptInterface
        public void goTopicInfo ( final int topic_id, int type )
        {
            final int id = type;
            LSBaseActivity.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent intent = null;
                    switch (id) {
                        //            话题
                        case 0:
//            线下贴
                        case 1:
                            intent = new Intent(LSBaseActivity.activity, LSClubTopicActivity.class);
                            intent.putExtra("topicID", topic_id);
                            LSBaseActivity.activity.startActivity(intent);
                            break;
//            线上贴
                        case 2:
                            intent = new Intent(LSBaseActivity.activity, LSClubTopicNewActivity.class);
                            intent.putExtra("topicID", topic_id);
                            LSBaseActivity.activity.startActivity(intent);
                            break;
                    }
                }
            });
        }

        public void shareTo (String title, String content, String image_url, String url)
        {

        }


    }



    class MyWebClinet extends WebViewClient
    {

        @Override
        public void onLoadResource(WebView view, String url) {
            // TODO Auto-generated method stub
//			Toast.makeText(getActivity(), "WebViewClient.onLoadResource", Toast.LENGTH_SHORT).show();
//			Common.log("WebViewClient.onLoadResource="+url);
            super.onLoadResource(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
//			Toast.makeText(getActivity(), "WebViewClient.onPageFinished", Toast.LENGTH_SHORT).show();
//			Common.log("WebViewClient.onPageFinished=");
            DialogManager.getInstance().stopWaitting();
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
//			Toast.makeText(getActivity(), "WebViewClient.onPageStarted", Toast.LENGTH_SHORT).show();
//			Common.log("WebViewClient.onPageStarted=");
            DialogManager.getInstance().startWaiting(LSBaseActivity.activity, null, "数据加载中...");
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            view.loadUrl(url);
            return super.shouldOverrideUrlLoading(view, url);
        }

    }

}
