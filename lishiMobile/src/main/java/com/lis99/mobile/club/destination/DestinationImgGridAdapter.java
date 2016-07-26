package com.lis99.mobile.club.destination;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.destination.model.DestinationTwoModel;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.MyBaseAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by yy on 16/7/11.
 */
public class DestinationImgGridAdapter extends MyBaseAdapter {
    public DestinationImgGridAdapter(Activity c, List listItem) {
        super(c, listItem);
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(mContext, R.layout.destination_info_img_grid, null);
            view.setTag(new ViewHolder(view));
        }
        initializeViews(getItem(i), (ViewHolder) view.getTag());
        return view;
    }

    private void initializeViews(Object object, ViewHolder holder) {
        //TODO implement
        DestinationTwoModel.DestlistEntity.DestlistsEntity item = (DestinationTwoModel
                .DestlistEntity.DestlistsEntity) object;
        if ( item == null ) return;

        holder.name.setText(item.name);
        if ( !TextUtils.isEmpty(item.images))
        {
            ImageLoader.getInstance().displayImage(item.images, holder.roundedImageView, ImageUtil.getDefultImageOptions());
        }

    }

    protected class ViewHolder {
        private RoundedImageView roundedImageView;
        private TextView name;

        public ViewHolder(View view) {
            roundedImageView = (RoundedImageView) view.findViewById(R.id.roundedImageView);
            name = (TextView) view.findViewById(R.id.name);
        }
    }
}
