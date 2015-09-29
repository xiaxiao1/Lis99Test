package com.lis99.mobile.util;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Spanned;
import android.widget.EditText;
import android.widget.TextView;

import com.lis99.mobile.club.LSBaseActivity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yy on 15/9/28.
 *
 *      自定义表情类
 */
public class MyEmotionsUtil {

    private static MyEmotionsUtil instance;

    public static MyEmotionsUtil getInstance ()
    {
        if ( instance == null ) instance = new MyEmotionsUtil();
        return instance;
    }
//表情数组
//    private Bitmap[] emoticons;

    private HashMap<String, Bitmap> emotionMap;

    private ArrayList<HashMap<String, Bitmap>> emotionList;

    private final int COUNT = 5;

    private final String[] NAME = {"[[大笑]]", "[[大哭]]", "[[呵呵]]", "[[好的]]", "[[开心]]", };

    private EditText edit;

    private TextView text;

    //初始化表情
    public void initBitmap ()
    {
        emotionMap = new HashMap<String, Bitmap>();
        emotionList = new ArrayList<HashMap<String, Bitmap>>();

        for ( int i = 0; i < COUNT; i++ )
        {
            Bitmap b =getImage((i+1) + ".png");
            emotionMap.put(NAME[i], b);
            HashMap<String, Bitmap> map = new HashMap<String, Bitmap>();
            map.put(NAME[i], b);
            emotionList.add(map);
        }
    }




    public void keyClickedIndex(final String index) {

        Html.ImageGetter imageGetter = new Html.ImageGetter() {
            public Drawable getDrawable(String source) {
                Drawable d = new BitmapDrawable(LSBaseActivity.activity.getResources(),emotionMap.get(index));
                d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
                return d;
            }
        };

        Spanned cs = Html.fromHtml("<img src ='" + index + "'/>", imageGetter, null);

        int cursorPosition = edit.getSelectionStart();
        edit.getText().insert(cursorPosition, cs);
    }


    /**
     * For loading smileys from assets
     */
    private Bitmap getImage(String path) {
        AssetManager mngr = LSBaseActivity.activity.getAssets();
        InputStream in = null;
        try {
            in = mngr.open("emoticons/" + path);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bitmap temp = BitmapFactory.decodeStream(in, null, null);
        return temp;
    }


















}
