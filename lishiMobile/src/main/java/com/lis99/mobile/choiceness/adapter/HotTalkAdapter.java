package com.lis99.mobile.choiceness.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.model.ChoicenessBannerModel;
import com.lis99.mobile.util.Common;
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

        final ChoicenessBannerModel.HotlistEntity item = (ChoicenessBannerModel.HotlistEntity) object;

        holder.title.setText(item.title);

        if (item.hlist == null || item.hlist.size() == 0)
        {
            holder.recyclerView.setVisibility(View.GONE);
            return;
        }

        HotTalkRecycler adapter = new HotTalkRecycler(item.hlist, mContext);

        LinearLayoutManager linearLayoutM = new LinearLayoutManager(mContext);
        linearLayoutM.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder.recyclerView.setLayoutManager(linearLayoutM);
        holder.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickLitener(new HotTalkRecycler.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                ChoicenessBannerModel.HotlistEntity.HlistEntity info = item.hlist.get(position);
                if ( info == null ) return;
                int category = info.category;
                int topicId = info.id;
                Common.goTopic(mContext, category, topicId);
            }
        });


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
