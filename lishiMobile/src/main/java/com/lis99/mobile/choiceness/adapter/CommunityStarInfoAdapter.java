package com.lis99.mobile.choiceness.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.model.CommunityInfoModel;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.LSRequestManager;
import com.lis99.mobile.util.MyBaseAdapter;
import com.lis99.mobile.util.NativeEntityUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by yy on 16/6/28.
 */
public class CommunityStarInfoAdapter extends MyBaseAdapter {

    public CommunityStarInfoAdapter(Activity c, List listItem) {
        super(c, listItem);
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = View.inflate(mContext, R.layout.community_star_info_item, null);
            view.setTag(new ViewHolder(view));
        }
        initializeViews(getItem(i), (ViewHolder) view.getTag());
        return view;
    }

    private void initializeViews(Object object, final ViewHolder holder) {
        //TODO implement
        final CommunityInfoModel.ListsEntity item = (CommunityInfoModel.ListsEntity) object;

        if ( item == null ) return;

        holder.tvTag1.setVisibility(View.GONE);
        holder.tvTag2.setVisibility(View.GONE);

        List<CommunityInfoModel.ListsEntity.UsercatelistEntity> tagList = item.usercatelist;
        if ( tagList != null && tagList.size() != 0 )
        {
            if ( tagList.size() > 0 )
            {
                String name = tagList.get(0).title;
                holder.tvTag1.setVisibility(View.VISIBLE);
                holder.tvTag1.setText(name);
                if ( NativeEntityUtil.getInstance().getCommunityStarTags().containsKey(name) )
                {
                    int num = NativeEntityUtil.getInstance().getCommunityStarTags().get(name);
                    holder.tvTag1.setBackgroundResource(num);
                }
                else
                {
                    holder.tvTag1.setBackgroundResource(R.drawable.label_bg_default);
                }
            }
            if ( tagList.size() > 1 )
            {
                String name = tagList.get(1).title;
                holder.tvTag2.setVisibility(View.VISIBLE);
                holder.tvTag2.setText(name);
                if ( NativeEntityUtil.getInstance().getCommunityStarTags().containsKey(name) )
                {
                    int num = NativeEntityUtil.getInstance().getCommunityStarTags().get(name);
                    holder.tvTag2.setBackgroundResource(num);
                }
                else
                {
                    holder.tvTag2.setBackgroundResource(R.drawable.label_bg_default);
                }
            }
        }

            if ( !TextUtils.isEmpty(item.headicon))
            {
                ImageLoader.getInstance().displayImage(item.headicon, holder.roundedImageView1,
                        ImageUtil.getclub_topic_headImageOptions());
            }

            holder.tvNikeName.setText(item.nickname);
            holder.tvContent.setText(item.note);
            holder.tvLikeNum.setText("粉丝数 "+item.totfans);
            holder.tvReplyNum.setText("帖子数 "+item.tottopics);
            if ( item.is_follow == 0 )
            {
                holder.btnAttention.setVisibility(View.VISIBLE);
                holder.btnAttention.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!Common.isLogin((Activity)mContext))
                        {
                            return;
                        }

                        LSRequestManager.getInstance().getFriendsAddAttention(item.userId, new CallBack() {

                            @Override
                            public void handler(MyTask mTask) {
                                holder.btnAttention.setVisibility(View.INVISIBLE);
                                item.is_follow = 1;
                            }
                        });
                    }
                });
            }
            else
            {
                holder.btnAttention.setVisibility(View.INVISIBLE);
            }







    }



    protected class ViewHolder {
        private RoundedImageView roundedImageView1;
        private Button btnAttention;
        private TextView tvNikeName;
        private TextView tvTag1;
        private TextView tvTag2;
        private TextView tvContent;
        private TextView tvLikeNum;
        private TextView tvReplyNum;

        public ViewHolder(View view) {
            roundedImageView1 = (RoundedImageView) view.findViewById(R.id.roundedImageView1);
            btnAttention = (Button) view.findViewById(R.id.btn_attention);
            tvNikeName = (TextView) view.findViewById(R.id.tv_nikeName);
            tvTag1 = (TextView) view.findViewById(R.id.tv_tag1);
            tvTag2 = (TextView) view.findViewById(R.id.tv_tag2);
            tvContent = (TextView) view.findViewById(R.id.tv_content);
            tvLikeNum = (TextView) view.findViewById(R.id.tv_likeNum);
            tvReplyNum = (TextView) view.findViewById(R.id.tv_replyNum);
        }
    }

}
