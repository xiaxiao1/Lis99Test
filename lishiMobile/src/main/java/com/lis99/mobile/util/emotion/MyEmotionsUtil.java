package com.lis99.mobile.util.emotion;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
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

    private final String[] NAME = {"[[大笑]]", "[[得意]]", "[[点赞]]", "[[感动]]", "[[好色]]",
                                    "[[流泪]]", "[[难过]]", "[[生气]]", "[[失落]]", "[[微笑]]",
                                    "[[享受]]", "[[愉快]]","[[调皮]]", "[[捂嘴笑]]", "[[嘻嘻]]",
                                    "[[笑哭]]", "[[羞涩]]",//"[[调皮]]", "[[捂嘴笑]]", "[[嘻嘻]]",
//            "[[金盾宇]]",
    };

    private final int COUNT = NAME.length;

    private EditText edit;

    private PopupWindow popupWindow;

    private int keyboardHeight;

    private boolean isKeyBoardVisible;

    private View popUpView;

    private View emoticonsButton;
// 输入框表情
    private LinearLayout emoticonsCover;
//父view
    private View parentLayout;

    private Activity c;


    private int emotionBound = Common.dip2px(24);

    private CallBack callBack;

    public void setVisibleEmotion (CallBack callBack )
    {
        this.callBack = callBack;
    }



    //初始化表情
    public void initBitmap ()
    {
        if ( emotionList != null && emotionList.size() != 0 ) return;
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

        emotionMap.put("[[金盾宇]]", getImage("jindunyu.png"));

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
    public void initView ( final Activity c, final EditText edit, View emoticonsButton, LinearLayout emoticonsCovers, View parentLayoutp )
    {
        initBitmap();

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

//                    if (isKeyBoardVisible) {
//                        emoticonsCover.setVisibility(LinearLayout.GONE);
//                    } else {
                    emoticonsCover.setVisibility(LinearLayout.VISIBLE);
//                    }
                    Common.hideSoftInput(c);
                    if (callBack != null) {
                        MyTask m = new MyTask();
                        m.setresult("VISIBLE");
                        callBack.handler(m);
                    }
                    popupWindow.showAtLocation(parentLayout, Gravity.BOTTOM, 0, 0);

                } else {
                    popupWindow.dismiss();
                }

            }
        });

        enablePopUpView();
//        checkKeyboardHeight(parentLayout);
        enableFooterView();

    }

    public void dismissPopupWindow ()
    {
        if ( popupWindow != null && popupWindow.isShowing())
        {
            popupWindow.dismiss();
        }
    }

    /**
     *      表情解析
     * @param cs
     * @param mContext
     * @return
     */
    public String convertToMsg(CharSequence cs, Context mContext) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(cs);
        ImageSpan[] spans = ssb.getSpans(0, cs.length(), ImageSpan.class);
        for (int i = 0; i < spans.length; i++) {
            ImageSpan span = spans[i];
            String c = span.getSource();
            int a = ssb.getSpanStart(span);
            int b = ssb.getSpanEnd(span);
        }
        ssb.clearSpans();
        return ssb.toString();
    }


    /**
     * Enabling all content in footer i.e. post window
     */
    private void enableFooterView() {

        edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (popupWindow.isShowing()) {

                    popupWindow.dismiss();

                }

            }
        });

        edit.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (popupWindow.isShowing()) {

                    popupWindow.dismiss();

                }
                return false;
            }
        });

//        final Button postButton = (Button) findViewById(R.id.post_button);
//
//        postButton.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                if (content.getText().toString().length() > 0) {
//
//                    Spanned sp = content.getText();
//                    chats.add(sp);
//                    Log.i("keyBoard", sp.toString());
//                    content.setText("");
//                    mAdapter.notifyDataSetChanged();
//
//                }
//
//            }
//        });
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
                edit.setFocusable(true);
                edit.setFocusableInTouchMode(true);
                Common.showSoftInput(c, edit);
                if (callBack != null) {
                    MyTask m = new MyTask();
                    m.setresult("GONE");
                    callBack.handler(m);
                }
            }
        });
    }



    /**
     * Checking keyboard height and keyboard visibility
     */
    int previousHeightDiffrence = 0;
    private void checkKeyboardHeight(final View parentLayout) {

        parentLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {

                        Rect r = new Rect();
                        parentLayout.getWindowVisibleDisplayFrame(r);

                        int screenHeight = parentLayout.getRootView()
                                .getHeight();
                        int heightDifference = screenHeight - (r.bottom);

                        if (previousHeightDiffrence - heightDifference > 50) {
                            popupWindow.dismiss();
                        }

                        previousHeightDiffrence = heightDifference;
                        if (heightDifference > 100) {

                            isKeyBoardVisible = true;
                            changeKeyboardHeight(heightDifference);

                        } else {

                            isKeyBoardVisible = false;

                        }

                    }
                });

    }

    /**
     * change height of emoticons keyboard according to height of actual
     * keyboard
     *
     * @param height
     *            minimum height by which we can make sure actual keyboard is
     *            open or not
     */
    private void changeKeyboardHeight(int height) {

        if (height > 100) {
            keyboardHeight = height;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, keyboardHeight);
            emoticonsCover.setLayoutParams(params);
        }

    }







    public void keyClickedIndex(final String index) {

//        Html.ImageGetter imageGetter = new Html.ImageGetter() {
//            public Drawable getDrawable(String source) {
//                Drawable d = new BitmapDrawable(LSBaseActivity.activity.getResources(),emotionMap.get(index));
//                d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
//                return d;
//            }
//        };
//
//        Spanned cs = Html.fromHtml("<img src ='" + index + "'/>", imageGetter, null);

//删除
        if ( index.equals("back"))
        {
            KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
            edit.dispatchKeyEvent(event);
            return;
        }

        Drawable d = new BitmapDrawable(emotionMap.get(index));
        d.setBounds(0,0, emotionBound, emotionBound);
        ImageSpan imageSpan = new ImageSpan(d);
        SpannableString spannable = new SpannableString(index);
        spannable.setSpan(imageSpan, 0, index.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        int cursorPosition = edit.getSelectionStart();
        edit.getText().insert(cursorPosition, spannable);

    }

    private String zhengze = "\\[\\[[^\\]]+\\]\\]";

    /**
     *          TextView 转换表情
     * @param context
     * @param str       内容
     * @return
     */
    public SpannableString getTextWithEmotion ( Activity context, String str )
    {
        if (TextUtils.isEmpty(str) )
        {
            return new SpannableString("");
        }

        initBitmap();

        this.c = context;

        Pattern pattern = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE);

        SpannableString spannableString = new SpannableString(str);
        try {
            dealExpression(context, spannableString, pattern, 0);
        } catch (Exception e) {
            Common.log("Emotion dealExpression====" + e.getMessage());
        }
        return spannableString;


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
            Bitmap value = emotionMap.get(key);
            if ( value == null ) {
                continue;
            }
//            int resId = context.getResources().getIdentifier(value, "drawable",
//                    context.getPackageName());
            // 通过上面匹配得到的字符串来生成图片资源id，下边的方法可用，但是你工程混淆的时候就有事了，你懂的。不是我介绍的重点
            // Field field=R.drawable.class.getDeclaredField(value);
            // int resId=Integer.parseInt(field.get(null).toString());
//            if (resId != 0) {
//                Bitmap bitmap = BitmapFactory.decodeResource(SPAN_INCLUSIVE_EXCLUSIVE
//                        context.getResources(), resId);SPAN_EXCLUSIVE_EXCLUSIVE
//            Bitmap bitmap = Bitmap.createScaledBitmap(value, value.getWidth(), value.getHeight(), true);


            Drawable d = new BitmapDrawable(LSBaseActivity.activity.getResources(),value);
            d.setBounds(0,0, emotionBound, emotionBound);
                // 通过图片资源id来得到bitmap，用一个ImageSpan来包装
                ImageSpan imageSpan = new ImageSpan(d, key, ImageSpan.ALIGN_BASELINE);
                // 计算该图片名字的长度，也就是要替换的字符串的长度
                int end = matcher.start() + key.length();
                // 将该图片替换字符串中规定的位置中
                spannableString.setSpan(imageSpan, matcher.start(), end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                if (end < spannableString.length()) {
                    // 如果整个字符串还未验证完，则继续。。
                    dealExpression(context, spannableString, patten, end);
                }
                break;
//            }
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

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
            return true;
        } else {
            return false;
        }
    }
















}
