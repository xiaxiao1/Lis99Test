package com.lis99.mobile.newhome.activeline;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.choiceness.ActiveAllActivity;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.util.MyBaseAdapter;

import java.util.ArrayList;

/**
 * Created by yy on 16/1/15.
 */
public class LSActiveLineAdapter extends MyBaseAdapter {

    private final int normal = 0;

    private final int ad = 1;

    private int isLast = 2;

    private int num = 3;


    public LSActiveLineAdapter(Context c, ArrayList listItem) {
        super(c, listItem);
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return num;
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {

        int flag = getItemViewType(i);

        if ( flag == normal )
        {
            getNormal(i, view, viewGroup);
        }
        else if ( flag == ad )
        {
            getAd(i, view, viewGroup);
        }
        else if ( flag == isLast )
        {

        }

        return null;
    }


    private View getNormal (int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = View.inflate(mContext, R.layout.active_line_item, null);
            viewHolder = new ViewHolder();
            viewHolder.ivBg = (RoundedImageView) view.findViewById(R.id.iv_bg);
            viewHolder.ivLoad = (ImageView) view.findViewById(R.id.iv_load);
            viewHolder.layout = (RelativeLayout) view.findViewById(R.id.layout);
            viewHolder.tvDay = (TextView) view.findViewById(R.id.tv_day);
            viewHolder.tvMonth = (TextView) view.findViewById(R.id.tv_month);
            viewHolder.tvTag = (TextView) view.findViewById(R.id.tv_tag);
            viewHolder.tvTitle = (TextView) view.findViewById(R.id.tv_title);
            view.setTag(viewHolder);
        } else
        {
            viewHolder = (ViewHolder) view.getTag();
        }

        return view;
    }

    private View getAd (int i, View view, ViewGroup viewGroup)
    {
        ViewHolderAD viewholder = null;
        if ( view == null )
        {
            view = View.inflate(mContext, R.layout.active_line_ad, null);
            viewholder = new ViewHolderAD();
            viewholder.recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

            view.setTag(viewholder);
        }
        else
        {
            viewholder = (ViewHolderAD) view.getTag();
        }

        LSActiveLineADAdapter adapter = new LSActiveLineADAdapter(mContext, null);

        LinearLayoutManager linearLayoutM = new LinearLayoutManager(mContext);
        linearLayoutM.setOrientation(LinearLayoutManager.HORIZONTAL);

        viewholder.recyclerView.setLayoutManager(linearLayoutM);
        viewholder.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickLitener(new LSActiveLineADAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });


        return view;
    }

    private View getIsLast (int i, View view, ViewGroup viewGroup)
    {
        ViewHolderLast viewHolder = null;

        if ( view == null )
        {
            view = View.inflate(mContext, R.layout.active_line_all, null);
            viewHolder = new ViewHolderLast();

            viewHolder.btn = (Button) view.findViewById(R.id.btn_ok);

            view.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolderLast) view.getTag();
        }

        viewHolder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, ActiveAllActivity.class));
            }
        });


        return view;
    }


    protected class ViewHolder {
        private RoundedImageView ivBg;
        private ImageView ivLoad;
        private RelativeLayout layout;
        private TextView tvDay;
        private TextView tvMonth;
        private TextView tvTag;
        private TextView tvTitle;
    }

    protected class ViewHolderAD {
        private RecyclerView recyclerView;
    }

    protected class ViewHolderLast
    {
        private Button btn;
    }

}
