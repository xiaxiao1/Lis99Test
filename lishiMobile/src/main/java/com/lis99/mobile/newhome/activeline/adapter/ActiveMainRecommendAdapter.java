package com.lis99.mobile.newhome.activeline.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.model.ActiveMainHeadModel;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.MyBaseAdapter;

import java.util.List;

/**
 * Created by yy on 16/7/7.
 */
public class ActiveMainRecommendAdapter extends MyBaseAdapter {


    public ActiveMainRecommendAdapter(Context c, List listItem) {
        super(c, listItem);
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(mContext, R.layout.active_recommend_adapter, null);
            view.setTag(new ViewHolder(view));
        }
        initializeViews(getItem(i), (ViewHolder) view.getTag());
        return view;
    }

    private void initializeViews(Object object, ViewHolder holder) {
        //TODO implement

        final ActiveMainHeadModel.HotlistEntity item = (ActiveMainHeadModel.HotlistEntity) object;
        if ( item == null ) return;
        holder.title.setText(item.title);

        if ( item.actlist == null ) return;

        ActiveMainRecommendRecycler recycler = new ActiveMainRecommendRecycler(item.actlist, mContext);
        holder.recyclerView.setAdapter(recycler);
        recycler.setOnItemClickLitener(new MyBaseRecycler.OnItemClickLitener() {
            @Override
            public void onItemClick(int position) {
                int topicId = item.actlist.get(position).topicId;
                Common.goTopic(mContext, 4, topicId);

            }
        });

    }

    protected class ViewHolder {
        private TextView title;
        private RecyclerView recyclerView;

        public ViewHolder(View view) {
            title = (TextView) view.findViewById(R.id.title);
            recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(
                    mContext, LinearLayoutManager.HORIZONTAL, false));
        }
    }
}
