package com.lis99.mobile.newhome.click;

import android.view.View;
import android.widget.Button;

import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.LSRequestManager;

/**
 * Created by yy on 15/8/18.
 */
public class FriendsItemOnClick implements View.OnClickListener {

    public Button btn;

    public FriendsInterface interFace;

    public FriendsItemOnClick ( Button btn, FriendsInterface interFace )
    {
        this.btn = btn;
        this.interFace = interFace;
    }

    @Override
    public void onClick(View view) {
        if ( interFace.getIsAttention() == 0 )
        {
            LSRequestManager.getInstance().getFriendsAddAttention(interFace.getUserId(), new CallBack() {
                @Override
                public void handler(MyTask mTask) {
                    interFace.setAttention(1);
                    Common.setBtnAttention(btn);
                }
            });
        }
        else if ( interFace.getIsAttention() == 1 )
        {
            LSRequestManager.getInstance().getFriendsCancelAttention(interFace.getUserId(), new CallBack() {
                @Override
                public void handler(MyTask mTask) {
                    interFace.setAttention(0);
                    Common.setBtnNoAttention(btn);
                }
            });
        }
    }
}
