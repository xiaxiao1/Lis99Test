package com.lis99.mobile.newhome;

import com.lis99.mobile.util.Page;

/**
 * Created by yy on 15/8/13.
 */
public class MyFriendsFans extends MyFragmentBase {


    private FansAdapter adapter;

    private Page page;


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
