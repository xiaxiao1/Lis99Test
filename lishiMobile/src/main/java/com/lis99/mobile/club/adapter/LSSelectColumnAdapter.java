package com.lis99.mobile.club.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.model.LSClubSpecialList;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.MyBaseAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by zhangjie on 1/28/16.
 */
public class LSSelectColumnAdapter extends MyBaseAdapter {


    public interface  LSSelectColumnAdapterListener {
        void onSelectColumn(int columnID);
    }

    public LSSelectColumnAdapterListener listener;


    @Override
    public int getCount() {
        return  (listItem == null ) ? 0 : listItem.size()/2 + listItem.size()%2;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }


    public LSSelectColumnAdapter(Activity c, ArrayList listItem) {
        super(c, listItem);
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {
        Holder holder = null;
        if ( view == null )
        {
            view = View.inflate(mContext, R.layout.ls_select_column_item, null);
            holder = new Holder();
            holder.columnPanel1 = view.findViewById(R.id.columnPanel1);
            holder.columnPanel2 = view.findViewById(R.id.columnPanel2);
            holder.columnImage1 = (ImageView) view.findViewById(R.id.columnImage1);
            holder.columnImage2 = (ImageView) view.findViewById(R.id.columnImage2);
            holder.columnTitle1 = (TextView) view.findViewById(R.id.columnTitle1);
            holder.columnTitle2 = (TextView) view.findViewById(R.id.columnTitle2);
            holder.columnInfo1 = (TextView) view.findViewById(R.id.columnInfo1);
            holder.columnInfo2 = (TextView) view.findViewById(R.id.columnInfo2);

            view.setTag(holder);

        }
        else
        {
            holder = (Holder) view.getTag();
        }

        LSClubSpecialList.Taglist item = (LSClubSpecialList.Taglist)listItem.get(i*2);



        if ( item == null ) return view;

        final int item1ID = item.id;

        holder.columnTitle1.setText(item.name);
        String topicNumStr = item.topicTotal + "篇";
        SpannableString ss = new SpannableString(topicNumStr);
        ss.setSpan(new ForegroundColorSpan(Color.WHITE), topicNumStr.length() - 1, topicNumStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.columnInfo1.setText(ss);
        if (!TextUtils.isEmpty(item.images))
        {
            ImageLoader.getInstance().displayImage(item.images, holder.columnImage1, ImageUtil.getDefultImageOptions());
        }

        holder.columnPanel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listener != null) {
                    listener.onSelectColumn(item1ID);
                }
            }
        });


        if (listItem.size() > i * 2 + 1) {
            holder.columnPanel2.setVisibility(View.VISIBLE);
            item = (LSClubSpecialList.Taglist)listItem.get(i*2+1);

            if ( item == null ) return view;

            final int item2ID = item.id;

            holder.columnTitle2.setText(item.name);
            topicNumStr = item.topicTotal + "篇";
            ss = new SpannableString(topicNumStr);
            ss.setSpan(new ForegroundColorSpan(Color.WHITE), topicNumStr.length() - 1, topicNumStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.columnInfo2.setText(ss);
            if (!TextUtils.isEmpty(item.images))
            {
                ImageLoader.getInstance().displayImage(item.images, holder.columnImage2, ImageUtil.getDefultImageOptions());
            }

            holder.columnPanel2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (listener != null) {
                        listener.onSelectColumn(item2ID);
                    }
                }
            });

        } else {
            holder.columnPanel2.setVisibility(View.INVISIBLE);
        }

        return view;
    }


    class Holder
    {
        View columnPanel1, columnPanel2;
        ImageView columnImage1, columnImage2;
        TextView columnTitle1, columnTitle2, columnInfo1, columnInfo2;
    }

}