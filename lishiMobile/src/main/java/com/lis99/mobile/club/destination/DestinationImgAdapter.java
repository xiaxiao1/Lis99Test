package com.lis99.mobile.club.destination;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.destination.model.DestinationTwoModel;
import com.lis99.mobile.util.ActivityUtil;
import com.lis99.mobile.util.MyBaseAdapter;
import com.lis99.mobile.view.MyGridView;

import java.util.List;

/**
 * Created by yy on 16/7/11.
 */
public class DestinationImgAdapter extends MyBaseAdapter {



    public DestinationImgAdapter(Activity c, List listItem) {
        super(c, listItem);
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(mContext, R.layout.destination_info_img_item, null);
            view.setTag(new ViewHolder(view));
        }
        initializeViews(getItem(i), (ViewHolder) view.getTag());
        return view;
    }

    private void initializeViews(Object object, ViewHolder holder) {
        //TODO implement
        DestinationTwoModel.DestlistEntity item = (DestinationTwoModel.DestlistEntity) object;
        holder.title.setText(item.title);
        if ( item.destlists == null ) return;
        final DestinationImgGridAdapter adapter = new DestinationImgGridAdapter(mContext, item.destlists);
        holder.grid.setAdapter(adapter);

        holder.grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                DestinationTwoModel.DestlistEntity.DestlistsEntity item = (DestinationTwoModel
                        .DestlistEntity.DestlistsEntity) adapter.getItem(position);

                ActivityUtil.goDestinationInfo(item.tagId, item.destId);
            }
        });


    }

    protected class ViewHolder {
        private TextView title;
        private MyGridView grid;

        public ViewHolder(View view) {
            title = (TextView) view.findViewById(R.id.title);
            grid = (MyGridView) view.findViewById(R.id.grid);
        }
    }
}
