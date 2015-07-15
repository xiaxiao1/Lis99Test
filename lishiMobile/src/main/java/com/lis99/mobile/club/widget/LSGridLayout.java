package com.lis99.mobile.club.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.lis99.mobile.club.BaseConfig;
import com.lis99.mobile.club.adapter.BasicGridLayoutAdapter;

/**
 * 自定义的GridLayout
 * -----------------------------
 * |   t1  |   t2    |   t3    |
 * |----------------------------
 * |   t4  |   t5    |   t6    |
 * -----------------------------
 */
public class LSGridLayout extends LinearLayout {
    private int columnCount = 1;
    private int rowCount = 1;
    private int rowSpace = 1;
    private int leftMargin = 1;
    private int rightMargin = 1;
    private OnClickListener onItemClickListener;
    private BaseAdapter adapter;

    public LSGridLayout(Context context) {
        super(context);
    }

    public LSGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void setOnItemClickListener(OnClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private View.OnLongClickListener onItemLongClickListener;

    public void setOnLongClickListener(View.OnLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    /**
     * 设置行与行之间的间隙
     *
     * @param space
     */
    public void setRowSpace(int space) {
        this.rowSpace = space;
    }

    /**
     * 设置列与列之间的间隙
     */
    public void setColumnSpace(int space) {
        this.leftMargin = space;
        this.rightMargin = space;
    }

    public void setLeftMargin(int leftMargin) {
        this.leftMargin = leftMargin;
    }

    public void setRightMargin(int rightMargin) {
        this.rightMargin = rightMargin;
    }

    /**
     * 每行显示的列数
     *
     * @param count
     */
    public void setColumnCount(int count) {
        this.columnCount = count;
    }

    /**
     * 每列显示的行数
     *
     * @param count
     */
    public void setRowCount(int count) {
        this.rowCount = count;
    }


    //得到指定位置的itemView
    public View getItemView(int position) {
        int row = position / columnCount;
        int column = position % columnCount;
        return ((ViewGroup) getChildAt(row)).getChildAt(column);
    }


    public BaseAdapter getAdapter() {
        return this.adapter;
    }

    private LinearLayout.LayoutParams layoutParams;

    public void setCellLayoutParams(LinearLayout.LayoutParams layoutParams) {
        this.layoutParams = layoutParams;
    }


    private LayoutParams rowLayoutParams;

    public void setRowLayoutParams(LayoutParams layoutParams) {
        rowLayoutParams = layoutParams;
    }

    public void setAdapterWithMargin(BasicGridLayoutAdapter adapter, int rightMargin, int imageSize) {
        this.adapter = adapter;
        int total = adapter.getCount();
        rowCount = total % columnCount == 0 ? total / columnCount : total / columnCount + 1;
        LayoutParams rowLayoutParams = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int rowMargin = (int) (rowSpace * BaseConfig.density);
        rowLayoutParams.setMargins(0, rowMargin, 0, rowMargin);

        if (total > 0) {
            for (int i = 0; i < rowCount; i++) {
                LinearLayout child = new LinearLayout(getContext());
                child.setLayoutParams(rowLayoutParams);

                for (int j = 0; j < columnCount; j++) {
                    int position = i * columnCount + j;
                    View cellView;
                    if (position >= total) {
                        cellView = new View(getContext());//return space view
                    } else {
                        cellView = adapter.getView(position);
                        cellView.setTag(position);
                        // TODO need refactor
                        if (onItemClickListener != null) {
                            cellView.setOnClickListener(onItemClickListener);
                        }
                        if (onItemLongClickListener != null) {
                            cellView.setOnLongClickListener(onItemLongClickListener);
                        }
                    }
                    LayoutParams layoutParams = new LayoutParams(imageSize, imageSize);
                    if (j < columnCount - 1) {
                        layoutParams.setMargins(0, 0, rightMargin, 0);
                    } else {
                        layoutParams.setMargins(0, 0, 0, 0);
                    }
                    child.addView(cellView, layoutParams);
                }
                addView(child);
            }
        }
    }


    public void setAdapter(BaseAdapter adapter) {
        this.adapter = adapter;
        int total = adapter.getCount();
        rowCount = total % columnCount == 0 ? total / columnCount : total / columnCount + 1;
        if (rowLayoutParams == null) {
            rowLayoutParams = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int rowMargin = (int) (rowSpace * BaseConfig.density);
        rowLayoutParams.setMargins(0, rowMargin, 0, rowMargin);

        if (layoutParams == null) {
            layoutParams = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        }

        layoutParams.setMargins(BaseConfig.dp2px(leftMargin), 0, BaseConfig.dp2px(rightMargin), 0);

        if (total > 0) {
            for (int i = 0; i < rowCount; i++) {
                LinearLayout child = new LinearLayout(getContext());
                child.setLayoutParams(rowLayoutParams);

                for (int j = 0; j < columnCount; j++) {
                    int position = i * columnCount + j;
                    View cellView;
                    if (position >= total) {
                        cellView = new View(getContext());//return space view
                    } else {
                        cellView = adapter.getView(position,null,null);
                        cellView.setTag(position);
                        // TODO need refactor
                        if (onItemClickListener != null) {
                            cellView.setOnClickListener(onItemClickListener);
                        }
                        if (onItemLongClickListener != null) {
                            cellView.setOnLongClickListener(onItemLongClickListener);
                        }
                    }
                    child.addView(cellView, layoutParams);
                }
                addView(child, rowLayoutParams);
            }
        }
    }
}
