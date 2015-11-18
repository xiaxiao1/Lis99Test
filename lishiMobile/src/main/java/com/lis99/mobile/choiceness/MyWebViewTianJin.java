package com.lis99.mobile.choiceness;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.util.DialogManager;
import com.lis99.mobile.util.LocationUtil;
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
            public void Location(double latitude, double longitude) {
                // TODO Auto-generated method stub
                DialogManager.getInstance().stopWaitting();
                location.setGlocation(null);
                if (latitude == 0 || location == null ) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if ( location != null )
            location.stopLocation();
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
        webView.addJavascriptInterface(WebViewUtil.getInstance().getTianJinJS(), "LS");

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
