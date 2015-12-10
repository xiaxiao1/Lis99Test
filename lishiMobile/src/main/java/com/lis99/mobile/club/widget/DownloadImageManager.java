package com.lis99.mobile.club.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;


public class DownloadImageManager {
    private Context context;

    private ImageLruCache cache;
    private static DownloadImageManager manager;

    private DownloadImageManager() {
        long maxSize = Runtime.getRuntime().maxMemory() / 8;
        cache = new ImageLruCache((int) maxSize);
    }

    public static DownloadImageManager getInstance(Context context) {
        if (manager == null) {
            return new DownloadImageManager();
        } else {
            return manager;
        }
    }

    public void putImage(String url, Bitmap bitmap) {
        cache.put(url, bitmap);
    }

    public Bitmap getImage(String url) {
        System.out.println("getImage__" + url);
        return cache.get(url);
    }

    public class ImageLruCache extends LruCache<String, Bitmap> {
        public ImageLruCache(int maxSize) {
            super(maxSize);
        }

        @Override
        protected int sizeOf(String key, Bitmap value) {
            if (value != null) {
                return value.getHeight() * value.getRowBytes();
            } else {
                return 0;
            }
        }

        @Override
        protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
            super.entryRemoved(evicted, key, oldValue, newValue);
        }
    }


}