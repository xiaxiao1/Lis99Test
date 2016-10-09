package com.lis99.mobile.club.widget.ioscitychoose;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.model.ActiveBannerInfoModel;
import com.lis99.mobile.util.AnimationHelper;
import com.lis99.mobile.util.MyBaseAdapter;

import java.util.List;

/**
 * Created by yy on 16/7/5.
 */
public class GridActiveAdapter extends MyBaseAdapter {

    AnimationHelper animationHelper;
    public GridActiveAdapter(Activity c, List listItem) {
        super(c, listItem);
        animationHelper=new AnimationHelper(c);
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(mContext, R.layout.grid_in_active_banner_item, null);
            view.setTag(new ViewHolder(view));
        }
        initializeViews(getItem(i), (ViewHolder) view.getTag());
        return view;
    }

    private void initializeViews(Object object, ViewHolder holder) {
        //TODO implement
        ActiveBannerInfoModel item = (ActiveBannerInfoModel) object;

        holder.ivImg.setImageResource(item.resultId);
        holder.tvName.setText(item.name);
        animationHelper.showAnimation(holder.item,R.anim.add_home_banner);
        animationHelper.showAnimation(holder.tvName,R.anim.add_home_banner2);

    }

    protected class ViewHolder {
        private ImageView ivImg;
        private TextView tvName;
        private RelativeLayout item;

        public ViewHolder(View view) {
            ivImg = (ImageView) view.findViewById(R.id.iv_img);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            item = (RelativeLayout) view.findViewById(R.id.item_rl);
        }
    }
}
