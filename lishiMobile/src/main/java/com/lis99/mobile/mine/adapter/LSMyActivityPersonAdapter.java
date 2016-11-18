package com.lis99.mobile.mine.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.mine.model.LSMyActivity;
import com.lis99.mobile.util.MyBaseAdapter;

import java.util.ArrayList;

/**
 * Created by zhangjie on 5/22/16.
 */

public class LSMyActivityPersonAdapter extends MyBaseAdapter {

    public LSMyActivityPersonAdapter(Activity c, ArrayList listItem) {
        super(c, listItem);
    }




    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {
        Holder holder = null;
        if ( view == null )
        {
            view = View.inflate(mContext, R.layout.mine_activity_person_item, null );

            holder = new Holder();

            holder.nameVieww = (TextView) view.findViewById(R.id.nameView);
            holder.sepHalf = view.findViewById(R.id.sepHalf);

            view.setTag(holder);
        }
        else
        {
            holder = (Holder) view.getTag();
        }

        if ( getCount() == (i + 1) )
        {
            holder.sepHalf.setVisibility(View.GONE);
        }
        else
        {
            holder.sepHalf.setVisibility(View.VISIBLE);
        }

        LSMyActivity.Applicant item = (LSMyActivity.Applicant) getItem(i);
        holder.nameVieww.setText(formatInfos(new String[]{item.name,item.sex,item.mobile,item.credentials}));
        return view;
    }

    public class Holder
    {
        public TextView nameVieww;
        public View  sepHalf;
    }

    public String formatInfos(String[] infos) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i=0;i<infos.length;i++) {
            if (infos[i]!=null) {
                stringBuffer.append(infos[i]);
                stringBuffer.append(", ");
            }
        }
        stringBuffer=stringBuffer.deleteCharAt(stringBuffer.length() - 2);
        return stringBuffer.toString();

    }
}