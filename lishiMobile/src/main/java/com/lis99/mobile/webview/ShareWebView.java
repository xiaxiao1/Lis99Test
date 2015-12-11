package com.lis99.mobile.webview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.choiceness.ActiveAllActivity;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.DialogManager;
import com.lis99.mobile.util.ShareManager;

/**
 * Created by yy on 15/11/17.
 * String url = "URL";
 */
public class ShareWebView extends LSBaseActivity {

    private WebView webView;

    private String url = "";

    private View titleLeft, tv_close, titleRight;

    private TextView title;

    private PopupWindow pop;

    protected TextView setTitle ( String text )
    {
        if ( title != null )
        title.setText(text);
        return title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.my_route_webview);

        url = getIntent().getStringExtra("URL");

        initViews();

        titleLeft = findViewById(R.id.titleLeft);
        titleRight = findViewById(R.id.titleRight);
        tv_close = findViewById(R.id.tv_close);
        title = (TextView) findViewById(R.id.title);

        titleLeft.setOnClickListener(this);
        titleRight.setOnClickListener(this);
        tv_close.setOnClickListener(this);


    }

    @Override
    protected void initViews() {


        webView = (WebView) findViewById(R.id.webView);

        WebSettings webSettings = webView.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        // 下面的一句话是必须的，必须要打开javaScript不然所做一切都是徒劳的
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);

        webView.setWebChromeClient(new MyWebChromeClient());

        webView.setWebViewClient(new MyWebClinet());

        webView.addJavascriptInterface(new MyJs(), "LS");


        webView.loadUrl(url);


    }

    class MyJs
    {
        public MyJs()
        {

        }


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

        @JavascriptInterface
        public void shareTo (final String title, final String content, final String image_url, final String url)
        {
            Common.log("title ="+title+"\n content="+content+"\n image_url="+image_url+"\n url="+url);

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    pop = ShareManager.getInstance().showPopWindoInWeb(title, content, image_url, url, webView);
                }
            });
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if ( pop != null && pop.isShowing() )
        {
            pop.dismiss();
            pop = null;
        }
    }

    class MyWebChromeClient extends WebChromeClient {

        public MyWebChromeClient() {
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            setTitle(title);

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
            setTitle(view.getTitle());
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


    @Override
    public void onClick(View arg0) {
        super.onClick(arg0);
        switch (arg0.getId())
        {
            case R.id.titleLeft:
                if ( webView.canGoBack() )
                {
                    webView.goBack();
                }
                else
                {
                    finish();
                }
                break;
            case R.id.titleRight:
                webView.reload();
                break;
            case R.id.tv_close:
                finish();
                break;
        }
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


}
