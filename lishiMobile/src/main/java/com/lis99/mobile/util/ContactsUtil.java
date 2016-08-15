package com.lis99.mobile.util;

import android.text.TextUtils;

import com.lis99.mobile.club.model.ApplyContactsListModel;
import com.lis99.mobile.club.model.BaseModel;
import com.lis99.mobile.club.model.IdModel;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;

import java.util.HashMap;

/**
 * Created by yy on 16/8/9.
 */
public class ContactsUtil {


    private static ContactsUtil instance;

    private boolean isChenged;

    private ApplyContactsListModel model;


    public static ContactsUtil getInstance ()
    {
        if ( instance == null ) instance = new ContactsUtil();
        return instance;
    }


    public void addContacts (HashMap<String, Object> map, final CallBack callBack)
    {
        String url = C.APPLY_CONTACTS_ADD_NEW;

        if ( map == null )
        {
            return;
        }

        IdModel model = new IdModel();

        MyRequestManager.getInstance().requestPost(url, map, model, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                isChenged = true;
                if ( callBack != null ) callBack.handler(mTask);
            }
        });
    }

    public void updataContacts (HashMap<String, Object> map, final CallBack callBack)
    {
        String url = C.APPLY_CONTACTS_UPDATA;

        if ( map == null )
        {
            return;
        }

        IdModel model = new IdModel();

        MyRequestManager.getInstance().requestPost(url, map, model, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                isChenged = true;
                if ( callBack != null ) callBack.handler(null);
            }
        });
    }

    public void removeContactsInfo (String id, final CallBack callBack )
    {
        String url = C.APPLY_CONTACTS_REMOVE;

        String userid = Common.getUserId();

        HashMap<String, Object> map = new HashMap<>();

        map.put("id", id);
        map.put("user_id", userid);

        BaseModel bModel = new BaseModel();
        MyRequestManager.getInstance().requestPost(url, map, bModel, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                isChenged = true;
                if ( callBack != null ) callBack.handler(null);
            }
        });

    }

    /**
     * 		获取报名人员列表
     */
    public void getContactsList (final CallBack callBack)
    {
        if ( !isChenged && model != null && model.user_list != null && model.user_list.size() != 0 && callBack != null )
        {
            MyTask myTask = new MyTask();
            myTask.setResultModel(model);
            callBack.handler(myTask);
            return;
        }


        String userId = Common.getUserId();
        if (TextUtils.isEmpty(userId))
        {
            Common.log("no client UserId");
            return;
        }

        String url = C.GET_APPLY_CONTACTS_LIST;

        HashMap<String, Object> map = new HashMap<>();

        map.put("user_id", userId);

        model = new ApplyContactsListModel();

        MyRequestManager.getInstance().requestPostNoModel(url, map, model, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                if ( callBack != null ) callBack.handler(mTask);
                isChenged = false;
            }

            @Override
            public void handlerError(MyTask mTask) {
                if ( callBack != null ) callBack.handlerError(mTask);
            }
        });
    }
}
