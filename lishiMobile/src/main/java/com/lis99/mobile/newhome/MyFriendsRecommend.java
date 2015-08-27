package com.lis99.mobile.newhome;

import android.view.View;
import android.widget.AdapterView;

import com.lis99.mobile.club.model.MyFriendsRecommendModel;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.LSRequestManager;
import com.lis99.mobile.util.Page;

/**
 * Created by yy on 15/8/13.
 */
public class MyFriendsRecommend extends MyFragmentBase {


    private RecommendAdapter adapter;

    private View view;

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
        LSRequestManager.getInstance().getFriendsAttentionRecommend(page.getPageNo(), new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                MyFriendsRecommendModel model = (MyFriendsRecommendModel) mTask.getResultModel();
                page.nextPage();
                initState = true;
                if (adapter == null)
                {
                    page.setPageSize(model.totPage);
                    adapter = new RecommendAdapter(getActivity(), model.lists);
                    list.setAdapter(adapter);

                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            if (adapter == null)
                                return;
                            MyFriendsRecommendModel.Lists item = (MyFriendsRecommendModel.Lists) adapter.getItem(i);
                            if (item == null) return;
                            Common.goUserHomeActivit(getActivity(), "" + item.user_id);
                        }
                    });

                }
                else
                {
                    adapter.addList(model.lists);
                }
            }
        });
    }

}
