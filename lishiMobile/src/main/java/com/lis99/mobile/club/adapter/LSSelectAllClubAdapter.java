package com.lis99.mobile.club.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSClubDetailActivity;
import com.lis99.mobile.club.model.LSSelectClub;
import com.lis99.mobile.util.ImageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by zhangjie on 1/28/16.
 */
public class LSSelectAllClubAdapter extends BaseExpandableListAdapter {

    public LSSelectAllClubAdapter(List<LSSelectClub> djClubs, List<LSSelectClub> tsClubs, List<LSSelectClub> ppClubs, Activity context) {
        this.djClubs = djClubs;
        this.tsClubs = tsClubs;
        this.ppClubs = ppClubs;
        this.context = context;
    }

    List<LSSelectClub> djClubs;
    List<LSSelectClub> tsClubs;
    List<LSSelectClub> ppClubs;

    private Activity context;



    @Override
    public int getGroupCount() {
        return 3;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (groupPosition == 0) {
            if (djClubs == null) {
                return 0;
            }
            return djClubs.size() / 2 + djClubs.size() % 2;
        }
        else if (groupPosition == 1) {
            if (tsClubs == null) {
                return 0;
            }
            return tsClubs.size() / 2 + tsClubs.size() % 2;
        }
        else {
            if (ppClubs == null) {
                return 0;
            }
            return ppClubs.size() / 2 + ppClubs.size() % 2;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.ls_all_club_group_item, null);
            holder = new GroupHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            holder.titleView = (TextView) convertView.findViewById(R.id.titleView);
            holder.arrowView = (ImageView) convertView.findViewById(R.id.arrowView);
            holder.sepLine = convertView.findViewById(R.id.sepLine);
            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();
        }

        switch (groupPosition) {
            case 0:
                holder.imageView.setImageResource(R.drawable.icon_club_group_season);
                holder.titleView.setText("当季推荐");
                break;
            case 1:
                holder.imageView.setImageResource(R.drawable.icon_club_group_recommend);
                holder.titleView.setText("特色项目");
                break;
            case 2:
                holder.imageView.setImageResource(R.drawable.icon_club_group_brand);
                holder.titleView.setText("品牌专区");
                break;
        }

        if (isExpanded) {
            holder.arrowView.setImageResource(R.drawable.club_group_arrow_up);
            holder.sepLine.setVisibility(View.GONE);
        } else {
            holder.arrowView.setImageResource(R.drawable.club_group_arrow_down);
            holder.sepLine.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder holder = null;
        if ( convertView == null )
        {
            convertView = View.inflate(context, R.layout.ls_select_all_club_item, null);
            holder = new ChildHolder();
            holder.clubPanel1 = convertView.findViewById(R.id.clubCard);
            holder.clubPanel2 = convertView.findViewById(R.id.clubCard2);

            holder.clubImage1 = (ImageView) convertView.findViewById(R.id.roundedImageView1);
            holder.clubImage2 = (ImageView) convertView.findViewById(R.id.roundedImageView2);

            holder.clubTitle1 = (TextView) convertView.findViewById(R.id.titleView);
            holder.clubTitle2 = (TextView) convertView.findViewById(R.id.titleView2);

            holder.clubMember1 = (TextView) convertView.findViewById(R.id.memberView);
            holder.clubMember2 = (TextView) convertView.findViewById(R.id.memberView2);

            holder.clubTopic1 = (TextView) convertView.findViewById(R.id.topicNumView);
            holder.clubTopic2 = (TextView) convertView.findViewById(R.id.topicNumView2);

            holder.bottomSep = convertView.findViewById(R.id.bottomSep);
            holder.sepLine = convertView.findViewById(R.id.sepLine);

            convertView.setTag(holder);

        }
        else
        {
            holder = (ChildHolder) convertView.getTag();
        }


        List<LSSelectClub> clubs = djClubs;
        if (groupPosition == 1) {
            clubs = tsClubs;
        } else if (groupPosition == 2) {
            clubs = ppClubs;
        }

        LSSelectClub item = clubs.get(childPosition*2);
        final int item1ID = item.getClub_id();

        holder.clubTitle1.setText(item.getClub_title());
//        holder.clubMember1.setText(item.getMembers() + "人参与");
        holder.clubTopic1.setText(item.getTotal()+"个话题");
        if (!TextUtils.isEmpty(item.getImage()))
        {
            ImageLoader.getInstance().displayImage(item.getImage(), holder.clubImage1, ImageUtil.getDefultImageOptions());
        }

        holder.clubPanel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LSClubDetailActivity.class);
                intent.putExtra("clubID", item1ID);
                context.startActivity(intent);
            }
        });


        if (clubs.size() > childPosition * 2 + 1) {
            holder.clubPanel2.setVisibility(View.VISIBLE);
            item = clubs.get(childPosition*2+1);
            final int item2ID = item.getClub_id();

            holder.clubTitle2.setText(item.getClub_title());
//            holder.clubMember2.setText(item.getMembers() + "人参与");
            holder.clubTopic2.setText(item.getTotal()+"个话题");
            if (!TextUtils.isEmpty(item.getImage()))
            {
                ImageLoader.getInstance().displayImage(item.getImage(), holder.clubImage2, ImageUtil.getDefultImageOptions());
            }

            holder.clubPanel2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, LSClubDetailActivity.class);
                    intent.putExtra("clubID", item2ID);
                    context.startActivity(intent);
                }
            });

        } else {
            holder.clubPanel2.setVisibility(View.INVISIBLE);
        }

        if (isLastChild) {
            holder.bottomSep.setVisibility(View.VISIBLE);
            holder.sepLine.setVisibility(View.VISIBLE);
        } else {
            holder.bottomSep.setVisibility(View.GONE);
            holder.sepLine.setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


    static class GroupHolder {
        ImageView imageView, arrowView;
        TextView titleView;
        View sepLine;
    }

    static class ChildHolder {
        View clubPanel1, clubPanel2;
        ImageView clubImage1, clubImage2;
        TextView clubTitle1, clubTitle2;
        TextView clubMember1, clubMember2;
        TextView clubTopic1, clubTopic2;
        View bottomSep, sepLine;
    }


}
