package com.lis99.mobile.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by yy on 15/7/2.
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {

    public Context mContext;

    protected List<T> listItem;

    public MyBaseAdapter ( Context c, List<T> listItem )
    {
        this.mContext = c;
        this.listItem = listItem;
    }

    public void setList (List<T> listItem)
    {
        if ( listItem == null ) return;
        this.listItem = listItem;
        notifyDataSetChanged();
    }

    public void addList ( List<T> listItem )
    {
        if ( listItem == null ) return;
        this.listItem.addAll(listItem);
        notifyDataSetChanged();
    }

    public void clean ()
    {
        if ( listItem == null )
        return;
        listItem.clear();
        listItem = null;
    }

    @Override
    public int getCount() {
        return  (listItem == null ) ? 0 : listItem.size();
    }

    @Override
    public Object getItem(int i) {
        return (listItem == null || listItem.size() == 0 ) ? null : listItem.get(i);
    }

    public Object getList ()
    {
        return listItem;
    }

    public void removeAt ( int position )
    {
        listItem.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return setView(i, view, viewGroup);
    }

    public abstract View setView ( int i, View view, ViewGroup viewGroup );

}
