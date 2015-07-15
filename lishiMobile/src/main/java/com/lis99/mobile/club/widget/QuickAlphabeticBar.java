package com.lis99.mobile.club.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class QuickAlphabeticBar extends View {

    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;

    private String[] mAlphas;
    private boolean mShowBg = false;

    private boolean mIsTouchable = true;

    private float mTextSize = 0f;
    private float mTextHeight = 0f;
    private static final int CHARACTER_COUNT = 26;

    private Paint mPaint;
    private Paint mPathPaint = null;
    private Path mPath = null;
    private PathEffect mPathEffect = null;

    public QuickAlphabeticBar(Context context) {
        super(context);
        init();
    }

    public QuickAlphabeticBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QuickAlphabeticBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.rgb(119, 119, 119));
        mPaint.setAntiAlias(true);

        mPathPaint = new Paint();
        mPathPaint.setAntiAlias(true);
        mPathPaint.setColor(Color.parseColor("#40000000"));
        mPathEffect = new CornerPathEffect(10);
    }

    private Path makePath() {
        int w = getWidth();
        int h = getHeight();
        Path p = new Path();
        p.moveTo(0, 0);
        p.lineTo(w, 0);
        p.lineTo(w, h);
        p.lineTo(0, h);
        p.lineTo(0, 0);
        p.lineTo(w, 0);
        return p;
    }

    private int height ;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mAlphas == null) {
            return;
        }

        int width = getWidth();

        if (mShowBg) {
            if (mPath == null) {
                mPath = makePath();
            }
            mPathPaint.setPathEffect(mPathEffect);
            canvas.drawPath(mPath, mPathPaint);
        }

        float textDistance = 0;
        if(mAlphas.length > 0) {
            textDistance = (height - mTextHeight * mAlphas.length) / (mAlphas.length );
        }

        for (int i = 0; i < mAlphas.length; i++) {
            float x = width / 2 - mPaint.measureText(mAlphas[i]) / 2;
            float y = mTextHeight * (i+1) + textDistance * i;
            // 绘制文本,左边为文字的左下角,为了好看,y方向居中(文字是0.8倍,上下留出0.1倍),右、下为正，所以是-0.1倍
            canvas.drawText(mAlphas[i], x, y - 0.1f * mTextHeight, mPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mAlphas == null) {
            return;
        }

        int width = getMeasuredWidth();
        height = getMeasuredHeight();
        if (mTextHeight <= 0) {
            mTextHeight = (float) height / CHARACTER_COUNT;
        }
        if (mTextSize <= 0) {
            mTextSize = mTextHeight * 0.6f;
        }
        mPaint.setTextSize(mTextSize);
        for (int i = 0; i < mAlphas.length; i++) {
            if (width < mPaint.measureText(mAlphas[i])) {
                this.setMeasuredDimension(MeasureSpec.makeMeasureSpec((int) mPaint.measureText(mAlphas[i]),
                        MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
                return;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mAlphas == null) {
            return true;
        }
        int action = event.getAction();
        final float y = event.getY();
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        final int c = (int) (y / getHeight() * mAlphas.length);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mShowBg = true;
                if (listener != null && mIsTouchable) {
                    if (c >= 0 && c < mAlphas.length) {
                        listener.onTouchingLetterChanged(c);
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (listener != null && mIsTouchable) {
                    if (c >= 0 && c < mAlphas.length) {
                        listener.onTouchingLetterChanged(c);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                mShowBg = false;
                if (listener != null && mIsTouchable) {
                    listener.onActionUp();
                }
                break;
        }
        invalidate();
        return true;
    }

    public void setAlphas(String[] alphas) {
        this.mAlphas = alphas;
        invalidate();
        requestLayout();
    }

    public void setTouchable(boolean flag) {
        mIsTouchable = flag;
    }

    public void setOnTouchingLetterChangedListener(
            OnTouchingLetterChangedListener l) {
        this.onTouchingLetterChangedListener = l;
    }

    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(int section);

        void onActionUp();
    }

}