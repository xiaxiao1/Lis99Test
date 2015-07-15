package com.lis99.mobile.club;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


/**
 * Created with IntelliJ IDEA.
 * User: liuqi
 * Date: 13-6-13
 * Time: 上午10:33
 * To change this template use File | Settings | File Templates.
 */
public class BaseConfig {
    // 三星市场渠道的渠道名称
    public static final int MODE_NO_NEED_VERIFY = 2;
    public static String versionName;
    public static int versionCode;
    public static String channel;
    public static String subChannel;
    public static int width;
    public static int height;
    public static float density;
    public static int densityDpi;
    public static String uuid = "";
    public static String pushToken;
    public static String ste = "";
    // DEVICE_ID 默认为15个零，每台机器都不一样。
    public static String iccid;
    public static String imsi;
    public static String mac;
    public static String launch = "group";
    public static String pushId = "";
    public static String networkType;
    public static String networkSubtype;
    public static String networkOperator;
    public static boolean useFlurry = true;
    public static String buildTime = "";
    public static long ONE_DAY = 24 * 60 * 60 * 1000L;
    public static int PAGE_LIMIT = 20;
    private static boolean displayInited;

    public static boolean isMapValid = true;

    public static void init(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            versionName = pi.versionName;
            versionCode = pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
        }

        initDisplay(context);
        initNetwork(context);
        initBuildTime(context);
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        iccid = tm.getSimSerialNumber();
        imsi = tm.getSubscriberId();
        mac = getMac(context);

    }


    public static void initDisplay(Context context, boolean refresh) {
        if (refresh) {
            displayInited = false;
        }

        initDisplay(context);
    }

    public static void initDisplay(Context context) {
        if ((!displayInited) && (context.getResources() != null)) {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            width = metrics.widthPixels;
            height = metrics.heightPixels;
            density = metrics.density;
            densityDpi = metrics.densityDpi;
            displayInited = true;
        }
    }

    public static void initNetwork(Context context) {
        NetworkInfo ni = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        networkType = ni == null ? "" : ni.getTypeName();
        networkSubtype = ni == null ? "" : ni.getSubtypeName();
        networkOperator = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperatorName();
    }



    public static String getDisplayVersionName() {
        return versionName + "-b" + versionCode;
    }

    public static String getBuildTime() {
        return buildTime;
    }

    public static int dp2px(int dp) {
        return (int) (dp * density);
    }

    public static String getNetwork() {
        String net;
        if ("MOBILE".equals(networkType)) {
            if (TextUtils.isEmpty(networkSubtype)) {
                net = networkType;
            } else {
                net = networkSubtype;
            }
        } else {
            net = networkType;
        }
        return net;
    }


    /**
     * 获取网卡mac地址,过滤掉":"
     *
     * @return
     */
    private static String getMac(Context context) {
        try {


            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();

            String mac = info.getMacAddress();
            if (mac != null) {
                mac = mac.replaceAll(":", "").toUpperCase();
            }
            return mac;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static void initBuildTime(Context context) {
        ApplicationInfo appinfo = context.getApplicationInfo();
        String sourceDir = appinfo.sourceDir;
        //以zip文件打开
        Enumeration<?> entries;
        ZipFile zipfile = null;
        try {
            zipfile = new ZipFile(sourceDir);
            entries = zipfile.entries();
            while (entries.hasMoreElements()) {
                final ZipEntry entry = ((ZipEntry) entries.nextElement());
                final String entryName = entry.getName();
                if (entryName.startsWith("META-INF/mtbuildtime")) {
                    String[] parts = entryName.split("_");
                    if (parts.length > 1) {
                        buildTime = entryName.substring(parts[0].length() + 1);
                    }
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zipfile != null) {
                try {
                    zipfile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
