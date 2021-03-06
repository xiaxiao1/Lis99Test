package com.lis99.mobile.club.activityinfo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.os.Build;


/**
 * <b>Project:</b> SlideDetailsLayout<br>
 * <b>Create Date:</b> 16/1/25<br>
 * <b>Author:</b> Gordon<br>
 * <b>Description:</b> <br>
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class BaseFragment extends Fragment {

    private ISlideCallback mISlideCallback;

    public BaseFragment() {

    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onAttach1(Activity context) {
        super.onAttach(context);
        if (!(context instanceof ISlideCallback)) {
            throw new IllegalArgumentException("Your activity must be implements ISlideCallback");
        }
        mISlideCallback = (ISlideCallback) context;
    }

    protected void open(boolean smooth) {
        mISlideCallback.openDetails(smooth);
    }

    protected void close(boolean smooth) {
        mISlideCallback.closeDetails(smooth);
    }

}
