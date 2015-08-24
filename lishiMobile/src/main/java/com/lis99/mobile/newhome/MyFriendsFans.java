package com.lis99.mobile.newhome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.model.MyFriendsRecommendModel;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.Page;

import java.util.HashMap;

/**
 * Created by yy on 15/8/13.
 */
public class MyFriendsFans extends MyFragmentBase {


    private FansAdapter adapter;

    private MyFriendsRecommendModel model;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public boolean getInitState() {
        return initState;
    }

    @Override
    public void cleanList() {
        page = new Page();
        list.setAdapter(null);
        adapter = null;
    }

    @Override
    public void getList() {
        if ( page.isLastPage() )
        {
            return;
        }

        String userID = DataManager.getInstance().getUser().getUser_id();

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("uid", userID);

        MyFriendsRecommendModel model = new MyFriendsRecommendModel();

//        String url = C.MYFRIENDS_RECOMMED + page.pageNo;
        String url = C.MYFRIENDS_FANS + page.pageNo;

        MyRequestManager.getInstance().requestPost(url, map, model, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                MyFriendsRecommendModel model = (MyFriendsRecommendModel) mTask.getResultModel();
                page.nextPage();
                initState = true;
                if (adapter == null) {
                    page.setPageSize(model.totPage);
                    adapter = new FansAdapter(getActivity(), model.lists);
                    list.setAdapter(adapter);

                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            if ( adapter == null )
                                 return;
                            MyFriendsRecommendModel.Lists item = (MyFriendsRecommendModel.Lists) adapter.getItem(i);
                            if ( item == null ) return;
                            Common.goUserHomeActivit(getActivity(), ""+item.user_id);
                        }
                    });

                } else {
                    adapter.addList(model.lists);
                }
            }
        });

    }
}
