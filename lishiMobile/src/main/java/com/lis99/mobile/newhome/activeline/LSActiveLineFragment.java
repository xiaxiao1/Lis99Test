package com.lis99.mobile.newhome.activeline;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.widget.BannerView;
import com.lis99.mobile.club.widget.ImagePageAdapter;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.newhome.LSFragment;
import com.lis99.mobile.newhome.LSSelectAdapter;
import com.lis99.mobile.newhome.LSSelectContent;
import com.lis99.mobile.newhome.LSSelectItem;

/**
 * Created by yy on 16/1/14.
 */
public class LSActiveLineFragment extends LSFragment implements
        PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener, View.OnClickListener,
        LSSelectAdapter.OnSelectItemClickListener, ImagePageAdapter.ImagePageAdapterListener, ImagePageAdapter.ImagePageClickListener {

    private TextView tvMassage;
    private TextView tvLocation;
    private ListView list;
    private PullToRefreshView pull_refresh_view;

    private BannerView viewBanner;

    private View head;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initViews(ViewGroup container) {
        super.initViews(container);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        body = inflater.inflate(R.layout.active_line, container, false);

        tvMassage = (TextView)findViewById(R.id.tv_massage);
        tvLocation = (TextView)findViewById(R.id.tv_location);
        list = (ListView)findViewById(R.id.list);


        head = View.inflate(getActivity(), R.layout.active_line_head, null);

        viewBanner = (BannerView) head.findViewById(R.id.viewBanner);


    }
//
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

    }




    @Override
    public void onClick(View view) {

    }

    @Override
    public void dispalyImage(ImageView banner, ImageView iv_load, int position) {

    }

    @Override
    public void onClick(int index) {

    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {

    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {

    }

    @Override
    public void onSelectItemClick(LSSelectContent content, LSSelectItem item) {

    }
}
