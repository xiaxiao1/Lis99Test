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
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.LSClubTopicActivity;
import com.lis99.mobile.club.LSClubTopicNewActivity;
import com.lis99.mobile.club.model.ShareModel;
import com.lis99.mobile.club.newtopic.LSClubNewTopicListMain;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.DialogManager;
import com.lis99.mobile.util.ShareManager;

/**
 * Created by yy on 15/11/17.
 */
public class ChoicenessRouteWebView extends LSBaseActivity {

    private WebView webView;

    private final String url = "http://m.lis99.com/club/areaweb/destlist";

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
    protected void onDestroy() {
        super.onDestroy();
        if ( pop != null && pop.isShowing() )
        {
            pop.dismiss();
            pop = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.my_route_webview);

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

        webView.addJavascriptInterface(new TianJinJS(), "LS");


        webView.loadUrl(url);


    }

    class TianJinJS
    {
        public TianJinJS()
        {

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
                    ShareModel model = new ShareModel();
                    model.title = title;
                    model.shareTxt = content;
                    model.imageUrl = image_url;
                    model.shareUrl = url;
                    pop = ShareManager.getInstance().showPopWindoInWeb(model, webView);
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
                        //						新版活动
                        case 5:
//                            intent = new Intent(LSBaseActivity.activity, LSClubTopicActiveOffLine.class);
//                            intent.putExtra("topicID", topic_id);
//                            LSBaseActivity.activity.startActivity(intent);

                            Common.goTopic(activity, 4, topic_id);

                            break;
                        //						新版话题
                        case 6:
                            intent = new Intent(LSBaseActivity.activity, LSClubNewTopicListMain.class);
                            intent.putExtra("TOPICID", ""+topic_id);
                            startActivity(intent);
                            break;
                    }
                }
            });
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
