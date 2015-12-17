package com.lis99.mobile.club.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.lis99.mobile.R;
import com.lis99.mobile.club.widget.LoadFileImageView;

import java.util.ArrayList;

public class ListAdapter extends android.widget.BaseAdapter {
    private ArrayList<ListItem> data = new ArrayList<ListItem>();
    private LayoutInflater layoutInflater;
    int width;
    public int selectID ;

    public ListAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        width = context.getResources().getDimensionPixelOffset(R.dimen.width);

    }

    public void setData(ArrayList<ListItem> items) {
        data.clear();
        if (items != null) {
            data.addAll(items);
        }
        notifyDataSetChanged();

    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_select_friend_picture, null);
        }
        ListItem item = data.get(position);
        LoadFileImageView imageView = (LoadFileImageView) convertView.findViewById(R.id.image);

        imageView.loadUrl(item.pictureUrl, width, width);
        TextView textView = (TextView) convertView.findViewById(R.id.name);
        textView.setText(item.name);

        View selectedView = convertView.findViewById(R.id.selectedView);
        if (item.id == selectID) {
            selectedView.setVisibility(View.VISIBLE);
        } else {
            selectedView.setVisibility(View.GONE);
        }

        return convertView;
    }


    public static class ListItem {
        public String name;
        public String pictureUrl;
        public int id;
    }
}