package com.lis99.mobile.util;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.model.RedDotModel;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;

import java.util.ArrayList;

/**
 * Created by yy on 16/1/25.
 *      调用setRedText放入要设置的TextView， 调用setRedSend设置回调， 调用getRedDot获取红点
 */
public class RedDotUtil {

    static private RedDotUtil instance;

    private OnRedSend redSend;

    private ArrayList<View> tvList = new ArrayList<View>();

    private boolean isConnect = false;

    public RedDotUtil() {
        if ( tvList == null ) tvList = new ArrayList<View>();
    }

    static public RedDotUtil getInstance ()
    {
        if ( instance == null ) instance = new RedDotUtil();
        return instance;
    }
/**       设置 红点数量（显示）
 * */
    public void setRedText ( View textView )
    {
        synchronized (tvList)
        {
            if ( tvList == null ) tvList = new ArrayList<View>();
            if ( tvList.contains(textView) ) return;
            //默认不显示
            textView.setVisibility(View.GONE);
            if ( textView instanceof TextView )
            {
                ((TextView)textView).setText("");
            }
            tvList.add(textView);
        }
    }

    public OnRedSend getRedSend() {
        return redSend;
    }

    public void setRedSend(OnRedSend redSend) {
        this.redSend = redSend;
    }

    public void getRedDot ()
    {
        String userId = DataManager.getInstance().getUser().getUser_id();
        if (TextUtils.isEmpty(userId)) return;

        if ( isConnect ) return;
        isConnect = true;

        String url = C.USER_NOTICE_TIPS_URL
                + userId;

        RedDotModel model = new RedDotModel();

        MyRequestManager.getInstance().requestGetNoDialog(url, model, new CallBack() {

            @Override
            public void handler(MyTask mTask) {
                // TODO Auto-generated method stub
                isConnect = false;
                RedDotModel model = (RedDotModel) mTask.getResultModel();
//				相加为0没有通知
                int num = model.is_baoming + model.is_reply + model.manage_baoming + model.is_follow + model.notice + model.likeStatus;
//                显示红点
                if ( tvList != null && model.notice != 0 )
                {
                    synchronized (tvList)
                    {
                        for ( View tv:tvList)
                        {
                            tv.setVisibility(View.VISIBLE);
                        }
                    }
                }

                if ( redSend == null ) return;
                if ( model.notice != 0 )
                {
                    redSend.SenderSystem(1);
                }


            }
        });

    }

    public void VisibleDot ()
    {
        //                显示红点
        if ( tvList != null )
        {
            synchronized (tvList)
            {
                for ( View tv:tvList)
                {
                    tv.setVisibility(View.VISIBLE);
                }
            }
        }
    }

//  取消红点显示
    public void InVisibleDot ()
    {
        //                隐藏红点
        if ( tvList != null )
        {
            synchronized (tvList)
            {
                for ( View tv:tvList)
                {
                    tv.setVisibility(View.GONE);
                }
            }
        }
    }


    public interface OnRedSend
    {
//        系统消息
        void SenderSystem( int num );
//         回复
//        void SenderReply ( int num );
    }

}


