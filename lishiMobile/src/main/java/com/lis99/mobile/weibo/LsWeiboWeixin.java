package com.lis99.mobile.weibo;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.util.BitmapUtil;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.ImageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class LsWeiboWeixin {


    //私有的默认构造子
    private LsWeiboWeixin() {
    }

    //注意，这里没有final      
    private static LsWeiboWeixin single = null;

    private static Context context;

//    private String shareContent;
//    private Bitmap shareBitmap;

    private static IWXAPI api;

    //静态工厂方法
    public synchronized static LsWeiboWeixin getInstance(Context ct) {
        context = ct;
        if (single == null) {
            single = new LsWeiboWeixin();
        }
        if (api == null) {
            regToWx();
        }
        return single;
    }

    public void share(String shareText, String title, String description, int type) {
        share(shareText, title, description, null, type);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public void share1(String shareText, String title, String description, String img_url, final int type) {
//		shareContent = shareText;
//		shareBitmap = bitmap;
        //WXTextObject textObj = new WXTextObject();
        //textObj.text = shareText;
        WXWebpageObject webo = new WXWebpageObject();
        webo.webpageUrl = shareText;
        //WXImageObject imgObj = new WXImageObject(bitmap);
        final WXMediaMessage msg = new WXMediaMessage(webo);
        msg.title = title;
        msg.description = description;
        //msg.description = shareText;
        //msg.title = shareText;
//	    if ( bitmap != null )
//	    {
//	    	Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
//			if ( thumbBmp == null )
//			{
//				Common.log("thumbbmp == null ");
//			}
//			else
//			{
//				Common.log("thumbbmp =!==========!!!!!!!!!!= null ");
//			}
//	    	msg.thumbData = BitmapUtil.bitampToByteArray(thumbBmp);
//	    }
//		else
//		{
//			Bitmap bmp2 = ImageUtil.drawableToBitmap(LSBaseActivity.activity.getResources().getDrawable(R.drawable.logo100));
//			msg.thumbData = BitmapUtil.bitampToByteArray(bmp2);
//		}
        if (TextUtils.isEmpty(img_url))
        {
            Bitmap bmp2 = ImageUtil.drawableToBitmap(LSBaseActivity.activity.getResources().getDrawable(R.drawable.icon_ls));
            msg.thumbData = BitmapUtil.bitampToByteArray(bmp2);
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = String.valueOf(System.currentTimeMillis());
            req.message = msg;
            req.scene = type;
            api.sendReq(req);

            return;
        }

        ImageLoader.getInstance().displayImage(img_url, new ImageView(LSBaseActivity.activity), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

                Bitmap bmp2 = ImageUtil.drawableToBitmap(LSBaseActivity.activity.getResources().getDrawable(R.drawable.icon_ls));
                msg.thumbData = BitmapUtil.bitampToByteArray(bmp2);
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = String.valueOf(System.currentTimeMillis());
                req.message = msg;
                req.scene = type;
                api.sendReq(req);
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, 100, 100, true);

                msg.thumbData = BitmapUtil.bitampToByteArray(thumbBmp);

                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = String.valueOf(System.currentTimeMillis());
                req.message = msg;
                req.scene = type;
                api.sendReq(req);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
//		SendMessageToWX.Req req = new SendMessageToWX.Req();
//		req.transaction = String.valueOf(System.currentTimeMillis());
//		req.message = msg;
//		req.scene = type;
//		api.sendReq(req);
    }

    public void share(String shareText, String title, String description, Bitmap bitmap, final int type) {
//		shareContent = shareText;
//		shareBitmap = bitmap;
        //WXTextObject textObj = new WXTextObject();
        //textObj.text = shareText;
        WXWebpageObject webo = new WXWebpageObject();
        webo.webpageUrl = shareText;
        //WXImageObject imgObj = new WXImageObject(bitmap);
        final WXMediaMessage msg = new WXMediaMessage(webo);
        msg.title = title;
        msg.description = description;
        //msg.description = shareText;
        //msg.title = shareText;
	    if ( bitmap != null )
	    {
	    	Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
			if ( thumbBmp == null )
			{
				Common.log("thumbbmp == null ");
			}
			else
			{
				Common.log("thumbbmp =!==========!!!!!!!!!!= null ");
			}
	    	msg.thumbData = BitmapUtil.bitampToByteArray(thumbBmp);
	    }
		else
		{
			Bitmap bmp2 = ImageUtil.drawableToBitmap(LSBaseActivity.activity.getResources().getDrawable(R.drawable.icon_ls));
			msg.thumbData = BitmapUtil.bitampToByteArray(bmp2);
		}
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.message = msg;
		req.scene = type;
		api.sendReq(req);
    }

    private static void regToWx() {
        api = WXAPIFactory.createWXAPI(context, C.WEIXIN_APP_ID, true);
        api.registerApp(C.WEIXIN_APP_ID);
    }

    public static IWXAPI getApi() {
        return api;
    }


}
