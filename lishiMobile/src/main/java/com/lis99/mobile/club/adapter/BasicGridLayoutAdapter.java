package com.lis99.mobile.club.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class BasicGridLayoutAdapter<T> extends BaseAdapter {
    protected List<T> resource;
    protected Context context;
    protected LayoutInflater layoutInflater;

    public BasicGridLayoutAdapter(Context context) {
        this(context, null);
    }

    public BasicGridLayoutAdapter(Context context,List<T> resource) {
        this.context = context;
        this.resource = resource;
        layoutInflater = LayoutInflater.from(context);
    }

    public abstract View getView(int position);

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public T getItem(int position) {
        if (position < getCount()) {
            return resource.get(position);
        }
        return null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getView(position);
    }

    protected void setData(List<T> data) {
        this.resource = data;
    }

    public int getCount() {
        if (resource == null) {
        	return 0;
        }
        return resource.size();
    }
}
