package com.lis99.mobile.newhome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.Page;

/**
 * Created by yy on 15/8/13.
 */
public class MyFriendsFans extends MyFragmentBase {


    private FansAdapter adapter;

    private Page page;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Common.log("MyFriendsFans onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Common.log("MyFriendsFans onActivityCreated");
        super.onActivityCreated(savedInstanceState);
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

    }
}
