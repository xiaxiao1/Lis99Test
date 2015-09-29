package com.lis99.mobile.entry.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.adapter.BaseListAdapter;
import com.lis99.mobile.entity.bean.LSCate;

import java.util.List;

/**
 * Created by zhangjie on 9/28/15.
 */
public class LSEquipFilterAdapter extends BaseListAdapter<LSCate> {

    public LSEquipFilterAdapter(Context context, List<LSCate> data) {
        super(context, data);
    }

    @Override
    public int getItemViewType(int position) {
        LSCate cate = getItem(position);
        if (cate.getType() == LSCate.LSCATE_VIRTUAL) {
            return 1;
        }
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);

        if (type == 0) {
            if(convertView == null){
                convertView = mInflater.inflate(R.layout.cate_item, null);
            }
            LSCate cate = getItem(position);
            TextView nameView = (TextView) convertView.findViewById(R.id.nameView);
            nameView.setText(cate.getName());
            return convertView;
        }

        if(convertView == null){
            convertView = mInflater.inflate(R.layout.ls_cate_title_item, null);
        }
        LSCate cate = getItem(position);
        TextView nameView = (TextView) convertView.findViewById(R.id.nameView);
        nameView.setText(cate.getName());
        return convertView;
    }
}
