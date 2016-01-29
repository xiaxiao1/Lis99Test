package com.lis99.mobile.newhome.activeline;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.choiceness.ActiveAllActivity;
import com.lis99.mobile.club.model.ActiveLineNewModel;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.MyBaseAdapter;
import com.lis99.mobile.webview.MyActivityWebView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by yy on 16/1/15.
 */
public class LSActiveLineAdapter extends MyBaseAdapter {

    private final int normal = 0;

    private final int ad = 1;

    private int isLast = 2;

    private int num = 3;


    public LSActiveLineAdapter(Context c, List listItem) {
        super(c, listItem);
    }


    @Override
    public int getItemViewType(int position) {

        Object o = getItem(position);

        if ( o instanceof ActiveLineNewModel.ActivitylistEntity )
        {
            return normal;
        }
        else if ( o instanceof List )
        {
            return ad;
        }
        else if ( o instanceof String )
        {
            return isLast;
        }

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
            return getNormal(i, view, viewGroup);
        }
        else if ( flag == ad )
        {
            return getAd(i, view, viewGroup);
        }
        else if ( flag == isLast )
        {
            return getIsLast(i, view, viewGroup);
        }

        return view;
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
            viewHolder.tv_style = (TextView) view.findViewById(R.id.tv_style);
            view.setTag(viewHolder);
        } else
        {
            viewHolder = (ViewHolder) view.getTag();
        }

        ActiveLineNewModel.ActivitylistEntity item = (ActiveLineNewModel.ActivitylistEntity) getItem(i);

        if ( !TextUtils.isEmpty(item.getImages()))
        {
            ImageLoader.getInstance().displayImage(item.getImages(), viewHolder.ivBg, ImageUtil.getclub_topic_imageOptions(), ImageUtil.getImageLoading(viewHolder.ivLoad, viewHolder.ivBg));
        }

        viewHolder.tvTag.setText(item.getHarddesc());

        viewHolder.tv_style.setText(item.getCatename());

        viewHolder.tvTitle.setText(item.getTitle());



        String[] date = Common.getTimeStamp(item.getStartdate());
        if ( date != null )
        {
            if (date.length > 0 )
            {
                viewHolder.tvMonth.setText(date[1]);
            }
            if ( date.length > 1 )
            {
                viewHolder.tvDay.setText(date[0]+"月");
            }
        }



        return view;
    }

    private View getAd (final int i, View view, ViewGroup viewGroup)
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

        final List<Object> list = (List<Object>) getItem(i);

        if ( list != null && list.size() > 0 )
        {

            LSActiveLineADAdapter adapter = new LSActiveLineADAdapter(mContext, list);

            LinearLayoutManager linearLayoutM = new LinearLayoutManager(mContext);
            linearLayoutM.setOrientation(LinearLayoutManager.HORIZONTAL);

            viewholder.recyclerView.setLayoutManager(linearLayoutM);
            viewholder.recyclerView.setAdapter(adapter);

            adapter.setOnItemClickLitener(new LSActiveLineADAdapter.OnItemClickLitener() {
                @Override
                public void onItemClick(View view, int position) {

                    ActiveLineNewModel.AreaweblistEntity item = (ActiveLineNewModel.AreaweblistEntity) list.get(position);
                    if ( item == null ) return;

                    Intent intent = new Intent(mContext, MyActivityWebView.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("URL", item.getTagid());
                    bundle.putString("TITLE", item.getTagname());
                    bundle.putString("IMAGE_URL", item.getImages());
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);

                }
            });

        }


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

                Intent i = new Intent(mContext, ActiveAllActivity.class);
                i.putExtra("CITYID", LSActiveLineFragment.cityId);
                mContext.startActivity(i);
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
        private TextView tvTag, tv_style;
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
