package com.lis99.mobile.club.widget.applywidget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.util.MyBaseAdapter;

import java.util.ArrayList;

/**
 * Created by yy on 15/11/19.
 */
public class ApplyManagerItem extends MyBaseAdapter {



    public ApplyManagerItem(Context c, ArrayList listItem) {
        super(c, listItem);
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {
        Holder holder = null;
        if ( view == null )
        {
            view = View.inflate(mContext, R.layout.apply_manager_list_item, null );
            holder = new Holder();

            holder.roundedImageView1 = (RoundedImageView) view.findViewById(R.id.roundedImageView1);
            holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            holder.tv_data = (TextView) view.findViewById(R.id.tv_data);
            holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
            holder.vipStar = (ImageView) view.findViewById(R.id.vipStar);
            holder.list = (ListView) view.findViewById(R.id.list);
            holder.bottombar = view.findViewById(R.id.bottombar);
            holder.btn_ok = (Button) view.findViewById(R.id.btn_ok);
            holder.btn_out = (Button) view.findViewById(R.id.btn_out);

            view.setTag(holder);
        }
        else
        {
            holder = (Holder) view.getTag();
        }

        ApplyManagerItem1 adapter = null;



        return view;
    }


    class Holder
    {
        RoundedImageView roundedImageView1;
        TextView tv_name, tv_data;
        ImageView vipStar;
        TextView tv_title;
        ListView list;
        View bottombar;
        Button btn_out, btn_ok;
    }

}
