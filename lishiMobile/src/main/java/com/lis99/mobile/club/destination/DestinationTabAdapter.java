package com.lis99.mobile.club.destination;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.destination.model.DestinationOneModel;
import com.lis99.mobile.util.MyBaseAdapter;

import java.util.List;

/**
 * Created by yy on 16/7/11.
 */
public class DestinationTabAdapter extends MyBaseAdapter {
    public DestinationTabAdapter(Activity c, List listItem) {
        super(c, listItem);
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(mContext, R.layout.destination_tab_adater, null);
            view.setTag(new ViewHolder(view));
        }
        initializeViews(getItem(i), (ViewHolder) view.getTag());
        return view;
    }

    private void initializeViews(Object object, ViewHolder holder) {
        //TODO implement
        DestinationOneModel.FirstdestEntity item = (DestinationOneModel.FirstdestEntity) object;

        if ( item == null ) return;

        holder.name.setText(item.title);

        if ( item.isSelect == 1 )
        {
            holder.ivSelecter.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.ivSelecter.setVisibility(View.INVISIBLE);
        }



    }

    protected class ViewHolder {
        private TextView name;
        private ImageView ivSelecter;

        public ViewHolder(View view) {
            name = (TextView) view.findViewById(R.id.name);
            ivSelecter = (ImageView) view.findViewById(R.id.iv_selecter);
        }
    }
        
        
        
        
        
}
