package com.lis99.mobile.webview;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.PopupWindow;

import com.lis99.mobile.R;
import com.lis99.mobile.choiceness.ActiveAllActivity;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.LSClubTopicActivity;
import com.lis99.mobile.club.LSClubTopicNewActivity;
import com.lis99.mobile.club.newtopic.LSClubTopicActiveOffLine;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.DialogManager;
import com.lis99.mobile.util.LocationUtil;
import com.lis99.mobile.util.ShareManager;
import com.lis99.mobile.util.WebViewUtil;

/**
 * Created by yy on 15/11/6.
 */
public class MyWebViewTianJin extends LSBaseActivity {

    private WebView webView;

    private String url;

    //定位
    private LocationUtil location;

    private View layout_main;

    private PopupWindow pop;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_webview);


        initViews();

        init();

//        webView.setVisibility(View.GONE);

        setTitle("地区活动精选");

        loadInfo();



    }

    @Override
    protected void rightAction() {
        super.rightAction();
        webView.reload();
    }

    private void loadInfo ()
    {

        DialogManager.getInstance().startWaiting(this, null, "数据加载中...");
        location = LocationUtil.getinstance();
        location.setGlocation(new LocationUtil.getLocation() {

            @Override
            public void Location(double latitude, double longitude, String cityName) {
                // TODO Auto-generated method stub
                DialogManager.getInstance().stopWaitting();
                location.setGlocation(null);
                if (latitude == 0 || location == null) {
                    webView.setVisibility(View.GONE);
                    return;
                }
                LocationOk("" + latitude, "" + longitude);

                location.stopLocation();
                location = null;
            }
        });
        location.getLocation();

    }

    public void LocationOk (String lati, String longti)
    {
//        http://m.lis99.com/club/areaweb/destDetail?longitude=精度&latitude=纬度
        url = "http://m.lis99.com/club/areaweb/destDetail?longitude="+ longti +"&latitude=" + lati;
        webView.loadUrl(url);

    }

    class Nearby
    {
        /**
         * 			跳转到全部活动页
         */
        @JavascriptInterface
        public void  goActiveMain()
        {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(activity, ActiveAllActivity.class);
                    startActivity(intent);
                }
            });

        }

        /**
         * 		调用原生分享
         * @param title
         * @param content
         * @param image_url
         * @param url
         */
        @JavascriptInterface
        public void shareTo (final String title, final String content, final String image_url, final String url)
        {
            Common.log("title =" + title + "\n content=" + content + "\n image_url=" + image_url + "\n url=" + url);

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    pop = ShareManager.getInstance().showPopWindoInWeb(title, content, image_url, url, webView);
                }
            });
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
                            intent = new Intent(LSBaseActivity.activity, LSClubTopicActivity.class);
                            intent.putExtra("topicID", topic_id);
                            LSBaseActivity.activity.startActivity(intent);
                            break;
//            线下贴
                        case 1:
                            intent = new Intent(LSBaseActivity.activity, LSClubTopicActiveOffLine.class);
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

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if ( location != null )
            location.stopLocation();

        if ( pop != null && pop.isShowing() )
        {
            pop.dismiss();
            pop = null;
        }

    }

    private void init()
    {

        layout_main = findViewById(R.id.layout_main);
        layout_main.setOnClickListener(this);

        //搜索按钮
        setRightView(R.drawable.club_main_refresh);

        webView = (WebView) findViewById(R.id.webView);

        WebSettings webSettings = webView.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        // 下面的一句话是必须的，必须要打开javaScript不然所做一切都是徒劳的
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);

//        webView.setWebChromeClient(new MyWebChromeClient());

        webView.setWebViewClient(WebViewUtil.getInstance().getWebviewClient());

        // 看这里用到了 addJavascriptInterface 这就是我们的重点中的重点
        // 我们再看他的DemoJavaScriptInterface这个类。还要这个类一定要在主线程中
        webView.addJavascriptInterface(new Nearby(), "LS");

        webView.loadUrl(url);

//        webView.loadUrl("file:///android_asset/web/test.html");
    }



    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack())
        {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    final class MyWebChromeClient extends WebChromeClient {

        public MyWebChromeClient() {
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            setTitle(title);

        }


    }

    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        switch (arg0.getId())
        {
            case R.id.layout_main:
                if ( webView.getVisibility() == View.GONE )
                {
                    webView.setVisibility(View.VISIBLE);
//                  定位
                    loadInfo();
                }
                break;
        }
    }
}
