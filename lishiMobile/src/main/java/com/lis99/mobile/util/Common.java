package com.lis99.mobile.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.mine.LSLoginActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Common {

    public static int WIDTH, HEIGHT;
    public static float scale;

    public static boolean mainIsStart;

    private static String TAG = "MYUTIL";

    public static void log(String str) {
        Log.w(TAG, str);
    }

    public static void log1(String str) {
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
    public static boolean clubDelete(String isJoin) {
        if ( "1".equals(isJoin) || "8".equals(isJoin))
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
            return "";
        if ("0".equals(num))
            return "";
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
    public static void hideSoftInput(Activity a) {
        View view = a.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) a.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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

}
