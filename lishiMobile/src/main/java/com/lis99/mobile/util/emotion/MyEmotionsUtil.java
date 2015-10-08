package com.lis99.mobile.util.emotion;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.util.Common;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yy on 15/9/28.
 *
 *      自定义表情类
 */
public class MyEmotionsUtil implements EmoticonsGridAdapter.KeyClickListener {

    private static MyEmotionsUtil instance;

    public static MyEmotionsUtil getInstance ()
    {
        if ( instance == null ) instance = new MyEmotionsUtil();
        return instance;
    }
//表情数组
//    private Bitmap[] emoticons;

    private HashMap<String, Bitmap> emotionMap;

    private ArrayList<ArrayList<Object>> emotionList;

    private final int COUNT = 5;

    private final String[] NAME = {"[[大笑]]", "[[大哭]]", "[[呵呵]]", "[[好的]]", "[[开心]]", };

    private EditText edit;

    private TextView text;

    private PopupWindow popupWindow;

    private int keyboardHeight;

    private boolean isKeyBoardVisible;

    private View popUpView;

    private ImageView emoticonsButton;
//父view, 输入框表情
    private LinearLayout parentLayout, emoticonsCover;

    private Context c;

    //初始化表情
    public void initBitmap ()
    {
        emotionMap = new HashMap<String, Bitmap>();
        emotionList = new ArrayList<ArrayList<Object>>();

        for ( int i = 0; i < COUNT; i++ )
        {
            Bitmap b =getImage((i+1) + ".png");
            emotionMap.put(NAME[i], b);
            ArrayList<Object> item = new ArrayList<Object>();
            item.add(NAME[i]);
            item.add(b);
            emotionList.add(item);
        }

        keyboardHeight = Common.dip2px(230);

    }

    /**
     *          初始化输入法
     * @param c
     * @param edit
     * @param emoticonsButton 表情按钮
     * @param emoticonsCovers   表情选择9宫格
     * @param parentLayoutp     父View
     */
    public void initView ( Context c, EditText edit, ImageView emoticonsButton, LinearLayout emoticonsCovers, LinearLayout parentLayoutp )
    {
        popUpView = View.inflate(c, R.layout.emoticons_popup, null);

        this.c = c;

        this.edit = edit;

        this.emoticonsButton = emoticonsButton;

        this.parentLayout = parentLayoutp;

        this.emoticonsCover = emoticonsCovers;

        emoticonsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!popupWindow.isShowing()) {

                    popupWindow.setHeight((int) (keyboardHeight));

                    if (isKeyBoardVisible) {
                        emoticonsCover.setVisibility(LinearLayout.GONE);
                    } else {
                        emoticonsCover.setVisibility(LinearLayout.VISIBLE);
                    }
                    popupWindow.showAtLocation(parentLayout, Gravity.BOTTOM, 0, 0);

                } else {
                    popupWindow.dismiss();
                }

            }
        });

        enablePopUpView();

    }


    /**
     * Defining all components of emoticons keyboard
     */
    private void enablePopUpView() {

        ViewPager pager = (ViewPager) popUpView.findViewById(R.id.emoticons_pager);
        pager.setOffscreenPageLimit(3);

        ArrayList<String> paths = new ArrayList<String>();

        EmoticonsPagerAdapter adapter = new EmoticonsPagerAdapter(c , emotionList, this);
        pager.setAdapter(adapter);

        // Creating a pop window for emoticons keyboard
        popupWindow = new PopupWindow(popUpView, LinearLayout.LayoutParams.MATCH_PARENT,
                (int) keyboardHeight, false);

        TextView backSpace = (TextView) popUpView.findViewById(R.id.back);
        backSpace.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
                edit.dispatchKeyEvent(event);
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                emoticonsCover.setVisibility(LinearLayout.GONE);
            }
        });
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

    private String zhengze = "\\[\\[[^\\]]+\\]\\]";

    public Spanned getTextWithEmotion ( String str )
    {
        Pattern pattern = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);


    }

    /**
     * 对spanableString进行正则判断，如果符合要求，则以表情图片代替
     *
     * @param context
     * @param spannableString
     * @param patten
     * @param start
     * @throws Exception
     */
    private void dealExpression(Context context,
                                SpannableString spannableString, Pattern patten, int start)
            throws Exception {
        Matcher matcher = patten.matcher(spannableString);
        while (matcher.find()) {
            String key = matcher.group();
            // 返回第一个字符的索引的文本匹配整个正则表达式,ture 则继续递归
            if (matcher.start() < start) {
                continue;
            }
            String value = emojiMap.get(key);
            if (TextUtils.isEmpty(value)) {
                continue;
            }
            int resId = context.getResources().getIdentifier(value, "drawable",
                    context.getPackageName());
            // 通过上面匹配得到的字符串来生成图片资源id，下边的方法可用，但是你工程混淆的时候就有事了，你懂的。不是我介绍的重点
            // Field field=R.drawable.class.getDeclaredField(value);
            // int resId=Integer.parseInt(field.get(null).toString());
            if (resId != 0) {
                Bitmap bitmap = BitmapFactory.decodeResource(
                        context.getResources(), resId);
                bitmap = Bitmap.createScaledBitmap(bitmap, 50, 50, true);
                // 通过图片资源id来得到bitmap，用一个ImageSpan来包装
                ImageSpan imageSpan = new ImageSpan(bitmap);
                // 计算该图片名字的长度，也就是要替换的字符串的长度
                int end = matcher.start() + key.length();
                // 将该图片替换字符串中规定的位置中
                spannableString.setSpan(imageSpan, matcher.start(), end,
                        Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                if (end < spannableString.length()) {
                    // 如果整个字符串还未验证完，则继续。。
                    dealExpression(context, spannableString, patten, end);
                }
                break;
            }
        }
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
