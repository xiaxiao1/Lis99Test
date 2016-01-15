package com.lis99.mobile.newhome.activeline;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.newhome.LSFragment;

/**
 * Created by yy on 16/1/14.
 */
public class LSActiveLineFragment extends LSFragment implements View.OnClickListener {

    private TextView tvMassage;
    private TextView tvLocation;
    private ListView list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViews(ViewGroup container) {
        super.initViews(container);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        body = inflater.inflate(R.layout.fragment_mine, container, false);

        tvMassage = (TextView)findViewById(R.id.tv_massage);
        tvLocation = (TextView)findViewById(R.id.tv_location);
        list = (ListView)findViewById(R.id.list);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

    }



    @Override
    public void onClick(View view) {

    }
}
