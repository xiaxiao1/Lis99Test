package com.lis99.mobile.util;

import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.model.RedDotModel;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;

/**
 * Created by yy on 16/1/25.
 */
public class RedDotUtil {

    static private RedDotUtil instance;

    private OnRedSend redSend;

    public RedDotUtil() {
    }

    static public RedDotUtil getInstance ()
    {
        if ( instance == null ) instance = new RedDotUtil();
        return instance;
    }

    public OnRedSend getRedSend() {
        return redSend;
    }

    public void setRedSend(OnRedSend redSend) {
        this.redSend = redSend;
    }

    public void getRedDot ()
    {
        String url = C.USER_NOTICE_TIPS_URL
                + DataManager.getInstance().getUser().getUser_id();

        RedDotModel model = new RedDotModel();

        MyRequestManager.getInstance().requestGetNoDialog(url, model, new CallBack() {

            @Override
            public void handler(MyTask mTask) {
                // TODO Auto-generated method stub
                RedDotModel model = (RedDotModel) mTask.getResultModel();
//				相加为0没有通知
                int num = model.is_baoming + model.is_reply + model.manage_baoming + model.is_follow + model.notice + model.likeStatus;

                if ( redSend == null ) return;
                if ( model.notice != 0 )
                {
                    redSend.SenderSystem(1);
                }

            }
        });

    }




    public interface OnRedSend
    {
//        系统消息
        void SenderSystem( int num );
//         回复
//        void SenderReply ( int num );
    }

}


