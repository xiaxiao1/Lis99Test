package com.lis99.mobile.newhome.activeline.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.model.ActiveMainHeadModel;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.util.ImageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by yy on 16/7/8.
 */
public class SupperLaderRecycler extends MyBaseRecycler<SupperLaderRecycler.MyHolder> {


    public SupperLaderRecycler(List<?> list, Context mContext) {
        super(list, mContext);
    }

    @Override
    public boolean getInfo(MyHolder myHolder, int i) {

        ActiveMainHeadModel.LeaderlistEntity item = (ActiveMainHeadModel.LeaderlistEntity) getItem(i);
        if ( item == null ) return false;

        myHolder.title.setText(item.nickname);
        myHolder.content.setText(item.description);
        myHolder.tag.setText(item.tag);
        if ( !TextUtils.isEmpty(item.headicon))
        {
            ImageLoader.getInstance().displayImage(item.headicon, myHolder.roundedImageView,
                    ImageUtil.getDefultImageOptions());
        }
        return false;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = View.inflate(mContext, R.layout.active_supper_lader_recycler, null);

        MyHolder holder = new MyHolder(view);

        return holder;
    }

    public static class MyHolder extends RecyclerView.ViewHolder
    {


        private RoundedImageView roundedImageView;
        private TextView title;
        private TextView tag;
        private TextView content;

        public MyHolder(View itemView) {
            super(itemView);
            roundedImageView = (RoundedImageView) itemView.findViewById(R.id.roundedImageView);
            title = (TextView) itemView.findViewById(R.id.title);
            tag = (TextView) itemView.findViewById(R.id.tag);
            content = (TextView) itemView.findViewById(R.id.content);
        }
    }
}
