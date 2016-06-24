package com.lis99.mobile.choiceness.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.util.MyBaseAdapter;

import java.util.List;

/**
 * Created by yy on 16/6/22.
 */
public class SpecialAdapter extends MyBaseAdapter {

    public SpecialAdapter(Context c, List listItem) {
        super(c, listItem);
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {


        if (view == null) {
            view = View.inflate(mContext, R.layout.special_adapter, null);
            view.setTag(new ViewHolder(view));
        }
        initializeViews(getItem(i), (ViewHolder) view.getTag());
        return view;
    }

    private void initializeViews(Object object, ViewHolder holder) {
        //TODO implement




    }





    protected class ViewHolder {
        private RoundedImageView roundedImageView;
        private TextView tvTitle;
        private TextView tvContent;
        private RoundedImageView ivHead;
        private TextView tvNikeName;

        public ViewHolder(View view) {
            roundedImageView = (RoundedImageView) view.findViewById(R.id.roundedImageView);
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            tvContent = (TextView) view.findViewById(R.id.tv_content);
            ivHead = (RoundedImageView) view.findViewById(R.id.iv_head);
            tvNikeName = (TextView) view.findViewById(R.id.tv_nikeName);
        }
    }


}
