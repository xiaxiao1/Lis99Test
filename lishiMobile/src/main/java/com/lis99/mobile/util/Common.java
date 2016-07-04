package com.lis99.mobile.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lis99.mobile.BuildConfig;
import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.LSClubTopicActivity;
import com.lis99.mobile.club.LSClubTopicNewActivity;
import com.lis99.mobile.club.model.ShareInterface;
import com.lis99.mobile.club.newtopic.LSClubNewTopicListMain;
import com.lis99.mobile.club.newtopic.series.LSClubTopicActiveSeries;
import com.lis99.mobile.mine.LSLoginActivity;
import com.lis99.mobile.mine.LSUserHomeActivity;
import com.lis99.mobile.newhome.NewHomeActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Common {

    public static int WIDTH, HEIGHT;
    public static float scale;

    public static boolean mainIsStart;

    private static String TAG = "MYUTIL";

    public static void log(String str) {
        if ("ttest".equals(DeviceInfo.CHANNELVERSION))
        Log.w(TAG, str);
    }

    public static void log1(String str) {
        if (BuildConfig.LOG_DEBUG)
        Log.w(TAG + "1", str);
    }

    // 获取俱乐部最后一位ID，匹配本地图片
    public static int getClubId(String s) {
        if (TextUtils.isEmpty(s))
            return 0;
        int num = 0;
        String str = "";
        if (s.length() == 1) {
            str = s;
        } else {
            str = s.substring(s.length() - 1, s.length());
        }
        try {
            num = Integer.parseInt(str);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return num;
    }

    private static long firstClick, lastClick;
    private static int count;

    /**
     * 双击判断，
     *
     * @return true 双击， fasle 单击
     */
    public static boolean isDoubleClick() {
        // 如果第二次点击 距离第一次点击时间过长 那么将第二次点击看为第一次点击
        if (firstClick != 0 && System.currentTimeMillis() - firstClick > 300) {
            count = 0;
        }
        count++;
        if (count == 1) {
            firstClick = System.currentTimeMillis();
        } else if (count == 2) {
            lastClick = System.currentTimeMillis();
            // 两次点击小于300ms 也就是连续点击
            if (lastClick - firstClick < 300) {// 判断是否是执行了双击事件
                return true;
            }
        }
        return false;
    }

    /**
     * 双击退出应用
     *
     * @return true 双击， fasle 单击
     */
    public static boolean exitApp() {
        // 如果第二次点击 距离第一次点击时间过长 那么将第二次点击看为第一次点击
        if (firstClick != 0 && System.currentTimeMillis() - firstClick > 3000) {
            count = 0;
        }
        count++;
        if (count == 1) {
            firstClick = System.currentTimeMillis();
        } else if (count == 2) {
            lastClick = System.currentTimeMillis();
            // 两次点击小于300ms 也就是连续点击
            if (lastClick - firstClick < 3000) {// 判断是否是执行了双击事件
                return true;
            }
        }
        return false;
    }

    /**
     *      退出应用
     */
    public static void ExitLis ()
    {
        NewHomeActivity.CLOSEAPPLICATION = true;
        Intent i = new Intent(LSBaseActivity.activity, NewHomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        LSBaseActivity.activity.startActivity(i);
    }

    /**
     * 判断用户是否已经登陆
     *
     * @param a
     */
    public static boolean isLogin(Activity a) {
        String userID = DataManager.getInstance().getUser().getUser_id();
        if (TextUtils.isEmpty(userID)) {
            Intent intent = new Intent(a, LSLoginActivity.class);
            // intent.
            a.startActivity(intent);
            // a.overridePendingTransition(R.anim.login, 0);
            return false;
        }
        return true;
    }

    /**
     *      拨打电话
     * @param number
     */
    public static void telPhone ( String number )
    {
//        Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+number));
        Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+number));
        LSBaseActivity.activity.startActivity(intent);
    }


    public static int dip2px(float dipValue) {
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(float pxValue) {
        return (int) (pxValue / scale + 0.5f);
    }

    public static void toast(String s) {
        if (LSBaseActivity.activity != null) {
            Toast.makeText(LSBaseActivity.activity, s, Toast.LENGTH_SHORT)
                    .show();
        }
    }

    /**
     * 1创始人，2管理员，4成员,8网站编辑，-1无权限信息
     *
     * @param isJoin
     * @return
     */
    public static boolean clubDelete( String isJoin ) {
        if ( "1".equals(isJoin) )
        {
            return true;
        }
        return false;
    }
//  分享菜单弹出， 判断是否需要显示管理报名
    public static boolean visibleApplyManager ( ShareInterface s )
    {
        return ( s != null && ("1".equals(s.getCategory()) || "2".equals(s.getCategory()) || !TextUtils.isEmpty(s.getNewActive()) || "999".equals(s.getCategory()) ));
    }

    /**
     * 1创始人，2管理员，4成员,8网站编辑，-1无权限信息
     *
     * @param isJoin
     * @return
     */
    public static boolean replyDelete(String isJoin, String user_id) {
        String uid = DataManager.getInstance().getUser().getUser_id();
        if ( "1".equals(isJoin) || ("2".equals(isJoin) && !TextUtils.isEmpty(uid)) && uid.equals(user_id) )
        {
            return true;
        }
        return false;
    }

    public static Double string2Double(String str) {
        Double d = -1d;

        try {
            d = Double.valueOf(str);
        } catch (Exception e) {
            // TODO: handle exception
        }

        return d;

    }

    public static int string2int(String str) {
        int i = -1;
        try {
            i = Integer.parseInt(str);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return i;
    }

    /**
     * 是否是VIP
     */
    public static boolean isVip(String str) {
        if (TextUtils.isEmpty(str))
            return false;
        if ("1".equals(str)) {
            return true;
        }
        return false;
    }

    public static String getLikeNum(String num) {
        if (TextUtils.isEmpty(num))
            return "赞";
        if ("0".equals(num))
            return "赞";
        else
            return num;
    }

    /**
     * 判断手机是否有SD卡。
     *
     * @return 有SD卡返回true，没有返回false。
     */
    public static boolean hasSDCard() {
        return Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState());
    }

    /**
     * 距离转换
     *
     * @param f
     * @return
     */
    public static String float2km(float f) {
        String num = "";
        int i = (int) f;
        if (i < 1000) {
            num = i + "m";
        } else {
            num = (i / 1000 + "km");
        }
        return num;
    }

    //要替换的文字
    private static String colorString = "<font color='#49a34b'>lishi</font>";

    /**
     * 根据搜索的内容， 替换返回回来信息相同文字颜色
     *
     * @param stext 关键字
     * @param info  内容
     * @return
     */
    public static String getSearchText(String stext, String info) {
        if (TextUtils.isEmpty(stext)) {
//			stext = SearchActivity.searchText;
            return info;
        }
        String result = info;
        String tempStr = colorString.replace("lishi", stext);
        result = info.replace(stext, tempStr);
        return result;
    }

    /**
     * 隐藏键盘
     *
     * @param a
     */
    public static void hideSoftInput(Activity a ) {

        View view = a.getWindow().peekDecorView();
            if (view != null) {
                InputMethodManager inputmanger = (InputMethodManager) a.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

    }
//显示键盘
    public static void showSoftInput (Activity a, EditText view )
    {
        InputMethodManager inputmanger = (InputMethodManager) a.getSystemService(Context.INPUT_METHOD_SERVICE);
//                inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        inputmanger.showSoftInput(view,InputMethodManager.SHOW_FORCED);
    }


    public static String getSearchText (String str) {
        // 只允许字母和数字
        // String   regEx  =  "[^a-zA-Z0-9]";
        // 清除掉所有特殊字符
        String result = str;
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？\040]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        result = m.replaceAll("").trim();
        return result;
    }
/**获取用户Id*/
    public static String getUserId ()
    {
        String userId = DataManager.getInstance().getUser().getUser_id();
        return userId;
    }
/**关注按钮*/
    public static void setBtnAttention ( Button btn )
    {
        btn.setBackgroundResource(R.drawable.friends_attention);
    }
    /**未关注*/
    public static void setBtnNoAttention ( Button btn )
    {
        btn.setBackgroundResource(R.drawable.friends_no_attention);
    }
/**跳转到个人主页*/
    public static void goUserHomeActivit ( Activity c, String userId )
    {
        Intent intent2 = new Intent(c, LSUserHomeActivity.class);
        intent2.putExtra("userID", userId);
        c.startActivity(intent2);
    }

//    public static void getEditTextString ( EditText et, String s )
//    {
//
//    }

//    是否是百度更新， 是的话， 禁止用自己的更新
    public static boolean isBDUpdata ()
    {
        boolean b = false;

//        if ( "baidu".equals(DeviceInfo.CHANNELVERSION) )
//        {
//            b = true;
//            BDAutoUpdateSDK.uiUpdateAction(LSBaseActivity.activity, new UICheckUpdateCallback() {
//
//                @Override
//                public void onCheckComplete() {
//
//                }
//            });
//        }

        return b;
    }

/*
*   是否是版主
* */
    public static boolean isModerator (String s)
    {
        if ( "1".equals(s))
        {
            return true;
        }
        return false;
    }

    /**
     * 判断当前应用是否是debug状态
     */

    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }
//  是否显示撰稿人  35
    public static boolean isWriter ( int[] nums )
    {
        if ( nums == null || nums.length == 0 ) return false;
        for ( int num : nums )
        {
            if ( num == 35)
            {
                return true;
            }
        }
        return false;
    }
//  阅读量
    public static void visibleReader ( TextView tv, String num )
    {
        int n = string2int(num);
        if ( n < 1 )
        {
            tv.setVisibility(View.GONE);
        }
        else
        {
            tv.setVisibility(View.VISIBLE);
            tv.setText(""+n);
        }
    }


    public static String[] getTimeStamp ( String timeS )
    {
        String[] result = new String[]{"","",};
        if ( TextUtils.isEmpty(timeS) )
        {
            return result;
        }
        //时间戳转化为Sting或Date
        SimpleDateFormat format =  new SimpleDateFormat("MM,dd");
        Long time = new Long(timeS+"000");
        String d = format.format(time);

        result = d.split(",");

//        System.out.println("Format To String(Date):"+d);

        return result;
    }
/**  达人标签， 裁切字符串， 最多7个字 */
    public static String getTagString ( String s )
    {
        if ( s.length() <= 7 )
        {
            return s;
        }
        String result = s.substring(0, 7);
        return result;
    }


    /**
     *      帖子类型：0话题贴，1线路活动帖 ，2线上活动帖,3 新版话题帖, 4 新版活动帖子(无用了), 4.系列活动帖
     * @param c
     * @param catgory
     */
    public static void goTopic ( Context c, int catgory, int topicId )
    {
        if ( 0 == catgory || 1 == catgory )
        {
            Intent intent = new Intent(c, LSClubTopicActivity.class);
            intent.putExtra("topicID", topicId);
            c.startActivity(intent);
        }
        else if ( 2 == catgory )
        {
            Intent intent = new Intent(c, LSClubTopicNewActivity.class);
            intent.putExtra("topicID", topicId);
            c.startActivity(intent);
        }
        else if ( 3 == catgory )
        {
            Intent intent = new Intent(c, LSClubNewTopicListMain.class);
            intent.putExtra("TOPICID", "" + topicId);
            c.startActivity(intent);
        }
//        else if ( 4 == catgory )
//        {
//            Intent intent = new Intent(c, LSClubTopicActiveOffLine.class);
//            intent.putExtra("topicID", topicId);
//            c.startActivity(intent);
//        }
        else if ( 4 == catgory )
        {
            Intent intent = new Intent(c, LSClubTopicActiveSeries.class);
            intent.putExtra("topicID", topicId);
            c.startActivity(intent);
        }
        else if ( -1 == catgory )
        {
            Common.toast("帖子类型获取失败");
        }
        else if ( -1 == topicId )
        {
            Common.toast("帖子获取失败");
        }
    }


    /**
     *      Url 格式化， 如果没有添加http:// 在头上添加
     * @param url
     * @return
     */
    public static String httpUrlFomat ( String url )
    {
        if ( !url.startsWith("http://") )
        {
            url = "http://" + url;
        }
        return url;
    }


    // 安装APK
    public static final void installAPK(Activity activity, String fileURL) {
        try {
            File file = new File(fileURL);
            Common.log("apk path = " + file.getAbsolutePath());
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            String type = "application/vnd.android.package-archive";
            intent.setDataAndType(Uri.fromFile(file), type);
            activity.startActivity(intent);
        } catch (Exception e) {
            Common.log("installAPK Exception = " + e.toString());
        }
    }

    /**
     *  Unicode 转汉字
     * @param utfString
     * @return
     */
    public static String convert(String utfString){
        if ( !"ttest".equals(DeviceInfo.CHANNELVERSION) )
        {
            return utfString;
        }

        if ( TextUtils.isEmpty(utfString) )
        {
            return utfString;
        }

        StringBuilder sb = new StringBuilder();
        int i = -1;
        int pos = 0;

        while((i=utfString.indexOf("\\u", pos)) != -1){
            sb.append(utfString.substring(pos, i));
            if(i+5 < utfString.length()){
                pos = i+6;
                sb.append((char)Integer.parseInt(utfString.substring(i+2, i+6), 16));
            }
        }

        return sb.toString();
    }


    /**
     *      获取当前时间
     * @return
     */
    public static String getTime ()
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        Date now = new Date();
        return format.format(now);


    }

    public static boolean isTest ()
    {
        if ( "ttest".equals(DeviceInfo.CHANNELVERSION))
        {
            return true;
        }
        return false;
    }

}
