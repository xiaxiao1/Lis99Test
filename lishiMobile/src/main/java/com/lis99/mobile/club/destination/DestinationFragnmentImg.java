package com.lis99.mobile.club.destination;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.destination.model.DestinationTwoModel;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.newhome.LSFragment;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.LSRequestManager;

/**
 * Created by yy on 16/7/11.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class DestinationFragnmentImg extends LSFragment {

    private View view;
    private ListView exList;
    private DestinationImgAdapter adapter;
    private String id;
    private DestinationTwoModel model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = View.inflate(getActivity(), R.layout.destination_info_img, null);

        exList = (ListView) view.findViewById(R.id.exList);


        return view;
    }

    public void setId (String id )
    {
        this.id = id;
        cleanList();
        getList();
    }

    private void getList ()
    {
        int d = Common.string2int(id);
        if ( d == -1 )
        {
            Common.log("id获取失败");
            return;
        }
        LSRequestManager.getInstance().getDestinationTwoList(d, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                model = (DestinationTwoModel) mTask.getResultModel();
                if ( model == null || model.destlist == null ) return;
                adapter = new DestinationImgAdapter(getActivity(), model.destlist);
                exList.setAdapter(adapter);
            }
        });
    }

    private void cleanList ()
    {
        if ( exList != null ) exList.setAdapter(null);
        if ( adapter != null ) adapter = null;
    }




}
