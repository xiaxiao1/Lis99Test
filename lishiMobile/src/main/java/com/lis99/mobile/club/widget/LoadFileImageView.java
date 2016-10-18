package com.lis99.mobile.club.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;


public class LoadFileImageView extends ImageView {
    private String path;
    private DownloadImageManager manager;
    private BitmapLoadTask bitmapLoadTask;
    private int viewWidth;
    private int viewHeight;

    public LoadFileImageView(Context context) {
        this(context, null);
    }

    public LoadFileImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadFileImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        manager = DownloadImageManager.getInstance(context);
    }


    public void loadUrl(String path, int width, int height) {
        setImageBitmap(null);
        this.path = path;
        if (manager.getImage(path) != null) {
            setImageBitmap(manager.getImage(path));
        } else {
            if (bitmapLoadTask != null) {
                bitmapLoadTask.cancel(true);
                bitmapLoadTask = null;
                bitmapLoadTask = new BitmapLoadTask();
                Item item = new Item();
                item.width = width;
                item.height = height;
                item.path = path;
                bitmapLoadTask.execute(item);
            } else {
                Item item = new Item();
                item.width = width;
                item.height = height;
                item.path = path;
                bitmapLoadTask = new BitmapLoadTask();
                bitmapLoadTask.execute(item);
            }
        }
    }

    private class BitmapLoadTask extends AsyncTask<Item, Integer, android.util.Pair<String, Bitmap>> {
        @Override
        protected android.util.Pair<String, Bitmap> doInBackground(Item... params) {
            if (params != null) {
                Item item = params[0];
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(path, options);
                int width = options.outWidth;
                int height = options.outHeight;
                double rationWidth = width / item.width;
                double rationHeight = height / item.height;
                double maxRation = Math.max(rationHeight, rationWidth);
                double ration = 1.0;
                while (ration * 2 < maxRation) {
                    ration = ration * 2;
                }
                options.inJustDecodeBounds = false;
                options.inPurgeable = true;
                options.inInputShareable = true;
                options.inSampleSize = (int) ration;
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                Bitmap bitmap = BitmapFactory.decodeFile(path, options);
                android.util.Pair<String, Bitmap> pair = new android.util.Pair<String, Bitmap>(path, bitmap);


                return pair;

            }
            return null;
        }

        @Override
        protected void onPostExecute(android.util.Pair<String, Bitmap> pair) {
            super.onPostExecute(pair);
            if (pair != null) {
                if (TextUtils.equals(pair.first, path) && pair.second != null) {
                    //   manager.putImage(path, pair.second);
                    setImageBitmap(pair.second);
                }
            }
        }
    }

    public class Item {
        int width;
        int height;
        String path;
    }
}
