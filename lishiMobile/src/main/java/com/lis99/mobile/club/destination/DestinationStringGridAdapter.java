package com.lis99.mobile.club.destination;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.destination.model.DestinationTwoModel;
import com.lis99.mobile.util.MyBaseAdapter;

import java.util.List;

/**
 * Created by yy on 16/7/11.
 */
public class DestinationStringGridAdapter extends MyBaseAdapter {
    public DestinationStringGridAdapter(Activity c, List listItem) {
        super(c, listItem);
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(mContext, R.layout.destination_info_string_grid, null);
            view.setTag(new ViewHolder(view));
        }
        initializeViews(getItem(i), (ViewHolder) view.getTag());
        return view;
    }

    private void initializeViews(Object object, ViewHolder holder) {
        //TODO implement
        DestinationTwoModel.DestlistEntity.DestlistsEntity item = (DestinationTwoModel
                .DestlistEntity.DestlistsEntity) object;
        holder.name.setText(item.name);
    }

    protected class ViewHolder {
        private TextView name;

        public ViewHolder(View view) {
            name = (TextView) view.findViewById(R.id.name);
        }
    }
}
