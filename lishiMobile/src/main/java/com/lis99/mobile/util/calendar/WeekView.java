package com.lis99.mobile.util.calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Created by Administrator on 2016/7/31.
 */
public class WeekView extends View {
    private IWeekTheme weekTheme;
    private Paint paint;
    private DisplayMetrics mDisplayMetrics;
    private String[] weekString = new String[]{"日","一","二","三","四","五","六"};
    public WeekView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDisplayMetrics = getResources().getDisplayMetrics();
        paint = new Paint();
        weekTheme = new DefaultWeekTheme();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        本身warp && parent 明确尺寸
        if(heightMode == MeasureSpec.AT_MOST){
            heightSize = (int) (mDisplayMetrics.density * 25);
        }
        //        本身warp&&parent也是warp
        else if(heightMode == MeasureSpec.UNSPECIFIED){
            heightSize = (int) (mDisplayMetrics.density * 25);
        }
//        本身明确尺寸
        else if(heightMode == MeasureSpec.EXACTLY){
        }
        else
        {
            heightSize = (int) (mDisplayMetrics.density * 225);
        }

        if(widthMode == MeasureSpec.AT_MOST){
            widthSize = (int) (mDisplayMetrics.density * 300);
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        canvas.drawColor(weekTheme.colorWeekView());
        //进行画上下线
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(weekTheme.colorTopLinen());
        paint.setStrokeWidth(weekTheme.sizeLine());
        canvas.drawLine(0, 0, width, 0, paint);

        //画下横线
        paint.setColor(weekTheme.colorBottomLine());
        canvas.drawLine(0, height, width, height, paint);
        //填充
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(weekTheme.sizeText() * mDisplayMetrics.scaledDensity);
        int columnWidth = width / 7;
        //总高度的中间 - 文字中间距离的左上点Y, Y 点在bottom位置
        int startY = (int) (height/2 - (paint.ascent() + paint.descent())/2);
//        canvas.drawLine(0, startY, width, startY, paint);
        for(int i=0;i < weekString.length;i++){
            String text = weekString[i];
            int fontWidth = (int) paint.measureText(text);
            int startX = columnWidth * i + (columnWidth - fontWidth)/2;

            if(text.indexOf("日") > -1|| text.indexOf("六") > -1){
                paint.setColor(weekTheme.colorWeekend());
            }else{
                paint.setColor(weekTheme.colorWeekday());
            }
            canvas.drawText(text, startX, startY, paint);
        }
    }

    public void setWeekTheme(IWeekTheme weekTheme) {
        this.weekTheme = weekTheme;
    }

    /**
     * 设置星期的形式
     * @param weekString
     * 默认值	"日","一","二","三","四","五","六"
     */
    public void setWeekString(String[] weekString) {
        this.weekString = weekString;
    }
}
