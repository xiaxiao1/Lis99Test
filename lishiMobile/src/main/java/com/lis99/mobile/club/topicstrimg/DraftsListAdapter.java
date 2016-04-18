package com.lis99.mobile.club.topicstrimg;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.util.MyBaseAdapter;
import com.lis99.mobile.util.dbhelp.StringImageModel;

import java.util.List;

/**
 * Created by yy on 16/4/18.
 */
public class DraftsListAdapter extends MyBaseAdapter {

    public DraftsListAdapter(Context c, List listItem) {
        super(c, listItem);
    }


    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder = null;

        if ( view == null )
        {
            view = View.inflate(mContext, R.layout.my_drafts_list_adapter, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) view.getTag();
        }

        StringImageModel item = (StringImageModel) getItem(i);

        if ( item == null ) return view;

        holder.tv_club_name.setText(item.clubName);

        holder.tv_content.setText(item.content);

        holder.tv_create.setText(item.editTime);

        if (TextUtils.isEmpty(item.title))
        {
            holder.tv_title.setText("无标题");
        }
        else
        {
            holder.tv_title.setText(item.title);
        }


        return view;
    }

    public static class ViewHolder {
        public View rootView;
        public TextView tv_title;
        public TextView tv_content;
        public TextView tv_create;
        public TextView tv_club_name;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.tv_title = (TextView) rootView.findViewById(R.id.tv_title);
            this.tv_content = (TextView) rootView.findViewById(R.id.tv_content);
            this.tv_create = (TextView) rootView.findViewById(R.id.tv_create);
            this.tv_club_name = (TextView) rootView.findViewById(R.id.tv_club_name);
        }

    }
}
