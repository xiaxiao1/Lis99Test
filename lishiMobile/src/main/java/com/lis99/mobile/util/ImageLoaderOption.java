package com.lis99.mobile.util;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;

/**
 * Created by yy on 15/7/7.
 *      设置ImageLoader 属性
 */
public class ImageLoaderOption {

    public static void init ( Context context )
    {
        ImageLoader imageLoader = ImageLoader.getInstance();

//        File cacheDir = StorageUtils.getOwnCacheDirectory(
//                context.getApplicationContext(), "lishi99");

        File cacheDir = new File(FileUtil.imgPath);

        DisplayImageOptions opt = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisc(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                .defaultDisplayImageOptions(opt)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .threadPoolSize(3).memoryCacheSize(getMemoryCacheSize(context))
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .discCacheFileCount(100) // 缓存的文件数量
                .discCache(new UnlimitedDiscCache(cacheDir))// 自定义缓存路径
                .tasksProcessingOrder(QueueProcessingType.FIFO).build();
        imageLoader.init(config);

    }


    private static int getMemoryCacheSize(Context context) {
        int memoryCacheSize;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            int memClass = ((ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE))
                    .getMemoryClass();
            memoryCacheSize = (memClass / 20) * 1024 * 1024; // 1/8 of app
            // memory
            // limit
        } else {
            memoryCacheSize = 2 * 1024 * 1024;
        }
        return memoryCacheSize;
    }


}
