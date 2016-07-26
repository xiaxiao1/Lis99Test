package com.lis99.mobile.choiceness.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSClubDetailActivity;
import com.lis99.mobile.club.model.ChoicenessBannerModel;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.MyBaseAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yy on 16/7/5.
 */
public class ChoicenessNewAdapter extends MyBaseAdapter {



    public ChoicenessNewAdapter(Activity c, List listItem) {
        super(c, listItem);
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(mContext, R.layout.choiceness_hot_talk_images, null);
            view.setTag(new ViewHolder(view));
        }
        initializeViews(getItem(i), (ViewHolder) view.getTag(), i);
        return view;
    }

    private void initializeViews(Object object, ViewHolder holder, int i) {
        //TODO implement

        holder.layoutImg0.setVisibility(View.GONE);
        holder.layoutImg.setVisibility(View.GONE);
        holder.layout_all.setVisibility(View.GONE);

        holder.ivBg.setVisibility(View.INVISIBLE);
        holder.ivBg1.setVisibility(View.INVISIBLE);
        holder.ivBg2.setVisibility(View.INVISIBLE);


        ChoicenessBannerModel.OmListEntiry item = (ChoicenessBannerModel.OmListEntiry) object;
        if ( item == null ) return;


        if ( i == getCount() - 1 )
        {
            holder.layout_all.setVisibility(View.VISIBLE);
            holder.layout_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    户外范 俱乐部
                    Intent intent = new Intent(mContext, LSClubDetailActivity.class);
                    intent.putExtra("clubID", 48);
                    mContext.startActivity(intent);
                }
            });
        }

        holder.tvTitle.setText(item.title);
        holder.tvContent.setText(item.subhead);
        holder.tvCreate.setText(item.nickname + " 发布于 " + item.club_title);
        holder.tvReply.setText(""+item.reply_num);



        ArrayList<ChoicenessBannerModel.ImageListEntity> imgl = item.imagelist;

        if ( imgl != null && imgl.size() != 0 )
        {
            int num = imgl.size();
            if ( num == 1 )
            {
                holder.layoutImg0.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(imgl.get(0).images, holder.ivBg0, ImageUtil.getDefultImageOptions());
            }
            else
            {
                if ( num > 0 )
                {
                    holder.layoutImg.setVisibility(View.VISIBLE);
                    holder.ivBg.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(imgl.get(0).images, holder.ivBg,
                            ImageUtil.getDefultImageOptions());
                }
                if ( num > 1 )
                {
                    holder.ivBg1.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(imgl.get(1).images, holder.ivBg1,
                            ImageUtil.getDefultImageOptions());
                }
                if ( num > 2 )
                {
                    holder.ivBg2.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(imgl.get(2).images, holder.ivBg2,
                            ImageUtil.getDefultImageOptions());
                }
            }

        }



    }

    protected class ViewHolder {
        private TextView tvTitle;
        private TextView tvContent;
        private LinearLayout layoutImg0;
        private RoundedImageView ivBg0;
        private LinearLayout layoutImg;
        private RoundedImageView ivBg;
        private RoundedImageView ivBg1;
        private RoundedImageView ivBg2;
        private TextView tvCreate;
        private TextView tvReply;
        private View layout_all;

        public ViewHolder(View view) {
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            tvContent = (TextView) view.findViewById(R.id.tv_content);
            layoutImg0 = (LinearLayout) view.findViewById(R.id.layout_img0);
            ivBg0 = (RoundedImageView) view.findViewById(R.id.iv_bg0);
            layoutImg = (LinearLayout) view.findViewById(R.id.layout_img);
            ivBg = (RoundedImageView) view.findViewById(R.id.iv_bg);
            ivBg1 = (RoundedImageView) view.findViewById(R.id.iv_bg1);
            ivBg2 = (RoundedImageView) view.findViewById(R.id.iv_bg2);
            tvCreate = (TextView) view.findViewById(R.id.tv_create);
            tvReply = (TextView) view.findViewById(R.id.tv_reply);
            layout_all = view.findViewById(R.id.layout_all);
        }
    }
}
