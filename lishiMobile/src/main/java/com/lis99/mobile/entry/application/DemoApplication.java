package com.lis99.mobile.entry.application;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.multidex.MultiDex;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.easemob.chat.EMChat;
import com.lecloud.config.LeCloudPlayerConfig;
import com.letv.proxy.LeCloudProxy;
import com.lis99.mobile.BuildConfig;
import com.lis99.mobile.club.BaseConfig;
import com.lis99.mobile.kf.easemob.KFCommon;
import com.lis99.mobile.kf.easemob.helpdesk.DemoHelper;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.FileUtil;
import com.lis99.mobile.util.ImageLoaderOption;
import com.lis99.mobile.util.MyOnTrimMemory;
import com.lis99.mobile.util.PushManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.umeng.analytics.MobclickAgent;

import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;




//import com.lecloud.config.LeCloudPlayerConfig;
//import com.letv.proxy.LeCloudProxy;


public class DemoApplication extends Application
{

    private static DemoApplication mInstance = null;
    public boolean m_bKeyRight = true;
    private BDLocation location;
    public int versionCode;
    public String versionName;
//  设备信息列表id
    public static int LOGIN_ID;

    public static WeakReference<Bitmap> publishBitmap;


    public BDLocation getLocation()
    {
        return location;
    }

    public void setLocation(BDLocation location)
    {
        this.location = location;
    }


    /**
     * 陈俊key: FKuW26myex26ePwHMe3kb1Vt V9sroLzTO6btKdauS9emWGZz 崔晓华key:
     * WF5wVTiwIG8p0m8IcAqwg9Si
     */
    @Override
    public void onCreate()
    {
        super.onCreate();
        mInstance = this;

//        客服
        kfInit();

        try
        {
//			地图
            SDKInitializer.initialize(this);
        } catch (Exception e)
        {

        }
//		统计
        MobclickAgent.openActivityDurationTrack(true);
        getPackageIngo();
        BaseConfig.init(this);

//		DisplayImageOptions opt = new DisplayImageOptions.Builder()
//		.cacheInMemory(true).cacheOnDisc(true).considerExifParams(true)
//		.bitmapConfig(Bitmap.Config.RGB_565).build();

//		DemoApplication.initImageLoader(this, opt);
//		initImageLoader(this, opt);

        //        设置文件保存路径
        FileUtil.setFilePath(this);

        //	设置ImageLoader 属性
        ImageLoaderOption.init(this);

        if (PushManager.isPush)
        {
            //		推送
            PushManager.getInstance().RegisterPush(this);
        }
        //崩溃记录本地
        CrashHandler1.getInstance().init(getApplicationContext());
//		LETV
        initLeTv();

//      xUtils
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);



//      注册内存太低释放接口
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            this.registerComponentCallbacks( new MyOnTrimMemory());
        }


    }

    private void kfInit ()
    {

        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
// 如果APP启用了远程的service，此application:onCreate会被调用2次
// 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
// 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null ||!processAppName.equalsIgnoreCase(getPackageName())) {
            Common.log("enter the service process!");

            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }

        EMChat.getInstance().setAppkey(KFCommon.APPKEY);
        // init demo helper
        DemoHelper.getInstance().init(mInstance);
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    private void getPackageIngo()
    {
        PackageManager packageManager = getPackageManager();
        String packageName = getPackageName();
        try
        {
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    packageName, 0);
            versionCode = packageInfo.versionCode;
            versionName = packageInfo.versionName;
        } catch (NameNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public static String getProcessName(Context cxt, int pid)
    {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps != null)
        {
            for (ActivityManager.RunningAppProcessInfo procInfo : runningApps)
            {
                if (procInfo.pid == pid)
                {
                    return procInfo.processName;
                }
            }
        }
        return null;
    }

    private void initLeTv()
    {
        String processName = getProcessName(this, android.os.Process.myPid());
        if (getApplicationInfo().packageName.equals(processName))
        {

            LeCloudPlayerConfig.getInstance().setDeveloperMode(true).setIsApp().setUseLiveToVod
                    (true);
            LeCloudProxy.init(getApplicationContext());

        }
    }

    public Bitmap bitmap;

    public Bitmap getBitmap()
    {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap)
    {
        this.bitmap = bitmap;
    }

    public static DemoApplication getInstance()
    {
        return mInstance;
    }

    public static void initImageLoader(Context context, DisplayImageOptions opt)
    {
//		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
//				context).threadPriority(Thread.NORM_PRIORITY - 4)
//				.denyCacheImageMultipleSizesInMemory()
//				.defaultDisplayImageOptions(opt).threadPoolSize(3)
//				.memoryCacheExtraOptions(480, 800)
//				.discCacheFileNameGenerator(new HashCodeFileNameGenerator())
//				.memoryCacheSize(2 * 1024 * 1024)
//				.discCacheSize(10 * 1024 * 1024)
//				.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
//				.tasksProcessingOrder(QueueProcessingType.LIFO) // Not
//				.build();
//		ImageLoader.getInstance().init(config);
    }

//  DEX太大问题
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}