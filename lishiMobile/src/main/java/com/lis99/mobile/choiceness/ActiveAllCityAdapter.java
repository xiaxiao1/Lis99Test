package com.lis99.mobile.choiceness;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.util.MyBaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yy on 15/7/28.
 */
public class ActiveAllCityAdapter extends MyBaseAdapter {

    private int count = 2;

    private final int TITLE = 0;

    private final int INFO = 1;

    public ActiveAllCityAdapter(Context c, ArrayList listItem) {
        super(c, listItem);
    }

    @Override
    public int getItemViewType(int position) {
        HashMap<String, String> map = (HashMap<String, String>) getItem(position);
        if ( map.containsKey("type"))
        {
            return TITLE;
        }
        return INFO;
    }

    @Override
    public int getViewTypeCount() {
        return count;
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {
        int type = getItemViewType(i);
        if ( type == TITLE )
        {
            return titleView(i, view);
        }
        else if ( type == INFO )
        {
            return infoView(i, view);
        }
        return view;
    }

    private View titleView ( int i, View v )
    {
        TextView text = null;
        if ( v == null )
        {
            v = View.inflate(mContext, R.layout.active_all_citylist_title_item, null);
            text = (TextView) v.findViewById(R.id.titleView);
            v.setTag(text);
        }
        else
        {
            text = (TextView) v.getTag();
        }

        HashMap<String, String> map = (HashMap<String, String>) getItem(i);
        text.setText(map.get("name"));

        return v;
    }

    private View infoView ( int i, View v )
    {
        Holder holder = null;
        if ( v == null )
        {
            v = View.inflate(mContext, R.layout.active_all_citylist_item, null);
            holder = new Holder();
            holder.text = (TextView) v.findViewById(R.id.nameView);
            holder.tv_select = (ImageView) v.findViewById(R.id.tv_select);
            v.setTag(holder);
        }
        else
        {
            holder = (Holder) v.getTag();
        }

        HashMap<String, String> map = (HashMap<String, String>) getItem(i);

        if ( "1".equals(map.get("select")))
        {
            holder.tv_select.setVisibility(View.VISIBLE);
            holder.text.setTextColor(mContext.getResources().getColor(R.color.text_color_blue));
        }
        else
        {
            holder.tv_select.setVisibility(View.GONE);
            holder.text.setTextColor(mContext.getResources().getColor(R.color.color_six));
        }

        holder.text.setText(map.get("name"));

        return v;
    }


    class Holder
    {
        ImageView tv_select;
        TextView text;
    }

}
