package com.letv.skin.popupwindow;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.lecloud.leutils.ReUtils;

@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class BackLivePopWindow extends BasePopupWindow {
	private View mPopView;
    private PopupWindow mPopupWindow;

    private int mPopWidth;
    private int mPopHeight;

    protected LayoutInflater mLayoutInflater;
    
    private static BackLivePopWindow mPopWindow;
    private OnBackToLiveListener mOnBackToLiveListener;
    
    private BackLivePopWindow(Context context) {
		super(context);
	}

    public static BackLivePopWindow getInstance(Context context) {
        if (mPopWindow == null) {
        	mPopWindow = new BackLivePopWindow(context);
        }
        return mPopWindow;
    }

    @Override
    protected void initView(Context context) {
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mPopView = mLayoutInflater.inflate(ReUtils.getLayoutId(context, "letv_skin_v4_back_to_live"), null);
        this.mPopView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mOnBackToLiveListener != null) {
					mOnBackToLiveListener.onBackToLive();
				}
			}
		});
        initData(context);
    }

    protected void initData(final Context context) {
        this.mPopWidth = (int) this.context.getResources().getDimension(ReUtils.getDimen(context, "letv_skin_v4_volume_seekbar_width"));
        this.mPopHeight = (int) this.context.getResources().getDimension(ReUtils.getDimen(context, "letv_skin_v4_volume_seekbar_height"));

    }

    public PopupWindow getVolumePopWin() {
        if (this.mPopupWindow == null) {
            return null;
        }
        return this.mPopupWindow;
    }

    public void showPop(View view) {
    	if (!view.isShown()) {
			return;
		}
//        	93  165 177
    	int width = ((View)view.getParent().getParent()).getHeight()*165/93;
//    	this.mPopupWindow = new PopupWindow(this.mPopView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        this.mPopupWindow = new PopupWindow(this.mPopView, width, width * 177 /165);

        ColorDrawable mColorDrawable = new ColorDrawable(Color.TRANSPARENT);
        this.mPopupWindow.setBackgroundDrawable(mColorDrawable);
        this.mPopupWindow.setOutsideTouchable(false);

        if (this.mPopupWindow != null) {
//            int xOffSet = (this.mPopWidth - view.getWidth()) / 2;
//            this.mPopupWindow.showAsDropDown(view, view.getWidth()-mPopupWindow.getWidth()/2, -mPopupWindow.getHeight());
//        	93  165 177
//        	int xOffSet = (((View)view.getParent()).getRight() -width)/2 - PxUtils.dip2px(context, 10)-view.getPaddingRight()+2;
        	if (mPopView.getParent() != null) {
				return;
			}
            int xOffSet = view.getRight() - (((View)view.getParent().getParent()).getRight()+view.getPaddingRight() - width)/2 ;
            if (!view.isShown()) {
    			return;
    		}
            this.mPopupWindow.showAtLocation(view, Gravity.BOTTOM, xOffSet, -width * 10 /165);
        }
    }

    public void dismiss() {
        if (this.mPopupWindow == null) {
            return;
        }

        if (this.mPopupWindow.isShowing()) {
        	this.mPopupWindow.dismiss();
		}
    }

    public boolean isShowing() {
        if (this.mPopupWindow == null) {
            return false;
        }
        return this.mPopupWindow.isShowing();
    }

    @Override
    protected int getPopHeight(View anchor) {
        return 0;
    }

    @Override
    protected int getPopWidth(View anchor) {
        return 0;
    }

    @Override
    protected View getPopContentView() {
        return null;
    }

    @Override
    protected void initPlayer() {

    }
    
    
    
    public interface OnBackToLiveListener {
        public void onBackToLive();
    }
    
    public void setOnBackToLiveListener(OnBackToLiveListener onBackToLiveListener) {
    	mOnBackToLiveListener = onBackToLiveListener;
    }
    
    

}
