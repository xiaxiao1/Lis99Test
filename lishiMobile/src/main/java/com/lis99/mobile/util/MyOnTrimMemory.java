package com.lis99.mobile.util;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.ComponentCallbacks2;
import android.content.res.Configuration;
import android.os.Build;

import com.bumptech.glide.Glide;
import com.lis99.mobile.entry.application.DemoApplication;

/**
 * Created by yy on 16/8/4.
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class MyOnTrimMemory implements ComponentCallbacks2 {
    /**
     * Called when the operating system has determined that it is a good
     * time for a process to trim unneeded memory from its process.  This will
     * happen for example when it goes in the background and there is not enough
     * memory to keep as many background processes running as desired.  You
     * should never compare to exact values of the level, since new intermediate
     * values may be added -- you will typically want to compare if the value
     * is greater or equal to a level you are interested in.
     * <p/>
     * <p>To retrieve the processes current trim level at any point, you can
     * use {@link ActivityManager#getMyMemoryState
     * ActivityManager.getMyMemoryState(RunningAppProcessInfo)}.
     *
     * @param level The context of the trim, giving a hint of the amount of
     *              trimming the application may like to perform.  May be
     *              {@link #TRIM_MEMORY_COMPLETE}, {@link #TRIM_MEMORY_MODERATE},
     *              {@link #TRIM_MEMORY_BACKGROUND}, {@link #TRIM_MEMORY_UI_HIDDEN},
     *              {@link #TRIM_MEMORY_RUNNING_CRITICAL}, {@link #TRIM_MEMORY_RUNNING_LOW},
     *              or {@link #TRIM_MEMORY_RUNNING_MODERATE}.
     */
    @Override
    public void onTrimMemory(int level) {
        /**
         *  TRIM_MEMORY_COMPLETE：内存不足，并且该进程在后台进程列表最后一个，马上就要被清理
         TRIM_MEMORY_MODERATE：内存不足，并且该进程在后台进程列表的中部。
         TRIM_MEMORY_BACKGROUND：内存不足，并且该进程是后台进程。
         TRIM_MEMORY_UI_HIDDEN：内存不足，并且该进程的UI已经不可见了。
         以上4个是4.0增加
         TRIM_MEMORY_RUNNING_CRITICAL：内存不足(后台进程不足3个)，并且该进程优先级比较高，需要清理内存
         TRIM_MEMORY_RUNNING_LOW：内存不足(后台进程不足5个)，并且该进程优先级比较高，需要清理内存
         TRIM_MEMORY_RUNNING_MODERATE：内存不足(后台进程超过5个)，并且该进程优先级比较高，需要清理内存
         */
        switch (level)
        {
            case TRIM_MEMORY_BACKGROUND:
                ImageLoaderOption.cleanMemory();
                Glide.get(DemoApplication.getInstance().getApplicationContext()).clearMemory();
                break;
        }
    }

    /**
     * Called by the system when the device configuration changes while your
     * component is running.  Note that, unlike activities, other components
     * are never restarted when a configuration changes: they must always deal
     * with the results of the change, such as by re-retrieving resources.
     * <p/>
     * <p>At the time that this function has been called, your Resources
     * object will have been updated to return resource values matching the
     * new configuration.
     * <p/>
     * <p>For more information, read <a href="{@docRoot}guide/topics/resources/runtime-changes.html"
     * >Handling Runtime Changes</a>.
     *
     * @param newConfig The new device configuration.
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {

    }

    /**
     * This is called when the overall system is running low on memory, and
     * actively running processes should trim their memory usage.  While
     * the exact point at which this will be called is not defined, generally
     * it will happen when all background process have been killed.
     * That is, before reaching the point of killing processes hosting
     * service and foreground UI that we would like to avoid killing.
     * <p/>
     * <p>You should implement this method to release
     * any caches or other unnecessary resources you may be holding on to.
     * The system will perform a garbage collection for you after returning from this method.
     * <p>Preferably, you should implement {@link ComponentCallbacks2#onTrimMemory} from
     * {@link ComponentCallbacks2} to incrementally unload your resources based on various
     * levels of memory demands.  That API is available for API level 14 and higher, so you should
     * only use this {@link #onLowMemory} method as a fallback for older versions, which can be
     * treated the same as {@link ComponentCallbacks2#onTrimMemory} with the {@link
     * ComponentCallbacks2#TRIM_MEMORY_COMPLETE} level.</p>
     */
    @Override
    public void onLowMemory() {
        ImageLoaderOption.cleanMemory();
        Glide.get(DemoApplication.getInstance().getApplicationContext()).clearMemory();
    }
}
