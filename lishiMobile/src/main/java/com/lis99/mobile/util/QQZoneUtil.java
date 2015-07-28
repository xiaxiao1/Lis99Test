package com.lis99.mobile.util;

import android.app.Activity;
import android.os.Bundle;

import com.lis99.mobile.club.LSBaseActivity;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.util.ArrayList;

public class QQZoneUtil {

    private static QQZoneUtil instance;

    public static Tencent mTencent;

    public static QQZoneUtil getInstance() {
        if (instance == null) instance = new QQZoneUtil();
        return instance;
    }

    public void sendQQZone(Activity activity, String title, String message, String url, String imgUrl) {
        mTencent = Tencent.createInstance(C.TENCENT_APP_ID, activity);

        //SSO登录检查
        if (!ThirdLogin.getInstance().QQInstalled(LSBaseActivity.activity)) {
            return;
        }

        final Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, message);
//            if (shareType != QzoneShare.SHARE_TO_QZONE_TYPE_APP) {
        //app分享不支持传目标链接
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, url);
//            }

        // 支持传多个imageUrl
        ArrayList<String> imageUrls = new ArrayList<String>();
        if (imgUrl != null) {
            imageUrls.add(imgUrl);
        }
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
//            doShareToQzone(params);
        mTencent.shareToQzone(activity, params, qZoneShareListener);
    }

    IUiListener qZoneShareListener = new IUiListener() {

        @Override
        public void onCancel() {
//	            Util.toastMessage(QZoneShareActivity.this, "onCancel: ");
        }

        @Override
        public void onError(UiError e) {
            // TODO Auto-generated method stub
//	            Util.toastMessage(QZoneShareActivity.this, "onError: " + e.errorMessage, "e");
        }

        @Override
        public void onComplete(Object response) {
            // TODO Auto-generated method stub
//				 Util.toastMessage(QZoneShareActivity.this, "onComplete: " + response.toString());
        }

    };

}
