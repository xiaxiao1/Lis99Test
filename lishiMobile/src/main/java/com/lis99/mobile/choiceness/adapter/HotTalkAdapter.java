package com.lis99.mobile.choiceness.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.util.MyBaseAdapter;

import java.util.List;

/**
 * Created by yy on 16/6/23.
 */
public class HotTalkAdapter extends MyBaseAdapter {
    public HotTalkAdapter(Context c, List listItem) {
        super(c, listItem);
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(mContext, R.layout.hot_talk_adatper, null);
            view.setTag(new ViewHolder(view));
        }
        initializeViews(getItem(i), (ViewHolder) view.getTag());
        return view;
    }

    private void initializeViews(Object object, ViewHolder holder) {
        //TODO implement

        HotTalkRecycler adapter = new HotTalkRecycler(null, mContext);

    }

    protected class ViewHolder {
        private TextView title;
        private RecyclerView recyclerView;

        public ViewHolder(View view) {
            title = (TextView) view.findViewById(R.id.title);
            recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        }
    }
}
