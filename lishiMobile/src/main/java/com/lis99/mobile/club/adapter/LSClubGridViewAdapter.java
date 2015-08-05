package com.lis99.mobile.club.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.model.ClubMainListModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

public class LSClubGridViewAdapter extends BaseAdapter {

    List<ClubMainListModel> clubs = new ArrayList<ClubMainListModel>();
    LayoutInflater inflater;
    Context context;


    DisplayImageOptions options;
    protected ImageLoader imageLoader = ImageLoader.getInstance();

    boolean editable = false;

    //=======3.4=======
    //我加入的
    List<ClubMainListModel> mine;
    //	推荐的
    List<ClubMainListModel> recommend;

    public static final int NEEDLOGIN = 0;

    public static final int NOJOINCLUB = 1;

    public static final int JOINEDCLUB = 2;

    public static final int RECOMMENDCLUB = 3;

    //样式数量
    private int count = 4;


    public LSClubGridViewAdapter(List<ClubMainListModel> mine, List<ClubMainListModel> recommend, Context context) {
        this.clubs = clubs;
        this.mine = mine;
        this.recommend = recommend;

        this.clubs.addAll(mine);
        this.clubs.addAll(recommend);

        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.club_icon_header_default)
                .showImageForEmptyUri(R.drawable.club_icon_header_default)
                .showImageOnFail(R.drawable.club_icon_header_default)
                .cacheInMemory(false).cacheOnDisk(true)
                .displayer(new SimpleBitmapDisplayer()).build();
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        if (this.editable ^ editable) {
            this.editable = editable;
            this.notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return clubs == null ? 1 : clubs.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        if (outRange(position))
            return null;
        return clubs.get(position);
    }

    private boolean outRange(int position) {
        if (clubs == null)
            return true;
        return !(position >= 0 && position < clubs.size());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        return convertView;
    }

    private View getMineClub(int position, View convertView) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.club_grid_item, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView
                    .findViewById(R.id.roundedImageView1);
            viewHolder.nameView = (TextView) convertView
                    .findViewById(R.id.nameView);
            viewHolder.recentView = (TextView) convertView.findViewById(R.id.recentView);
            viewHolder.tv_all = (TextView) convertView.findViewById(R.id.tv_all);
            viewHolder.sepAll = convertView.findViewById(R.id.sepAll);
            viewHolder.sepHalf = convertView.findViewById(R.id.sepHalf);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (outRange(position)) {
//			viewHolder.imageView.setImageBitmap(null);
//			viewHolder.imageView.setImageResource(R.drawable.club_icon_grid_add);
//			imageLoader.displayImage("drawable://" + R.drawable.club_icon_grid_add,
//					viewHolder.imageView, options);
//			viewHolder.nameView.setText("添加俱乐部");
            viewHolder.imageView.setVisibility(View.INVISIBLE);
            viewHolder.nameView.setVisibility(View.INVISIBLE);
            viewHolder.recentView.setVisibility(View.INVISIBLE);
            viewHolder.tv_all.setVisibility(View.VISIBLE);
            viewHolder.sepHalf.setVisibility(View.GONE);
            viewHolder.sepAll.setVisibility(View.VISIBLE);
        } else {
            //线的显示
            if (position == clubs.size() - 1) {
                viewHolder.sepHalf.setVisibility(View.GONE);
                viewHolder.sepAll.setVisibility(View.VISIBLE);
            } else {
                viewHolder.sepHalf.setVisibility(View.VISIBLE);
                viewHolder.sepAll.setVisibility(View.GONE);
            }
            viewHolder.imageView.setVisibility(View.VISIBLE);
            viewHolder.nameView.setVisibility(View.VISIBLE);
            viewHolder.recentView.setVisibility(View.VISIBLE);
            viewHolder.tv_all.setVisibility(View.GONE);
            imageLoader.displayImage(clubs.get(position).image,
                    viewHolder.imageView, options);
            viewHolder.nameView.setText(clubs.get(position).title);
            //描述
            viewHolder.recentView.setText(clubs.get(position).topic_title);
        }
        return convertView;
    }

    private View needLogin(int position, View convertView) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.club_list_title_view, null);
        }
        return convertView;
    }

    private View getRecommendClub(int position, View convertView) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.club_list_item_recommend, null);
            holder = new ViewHolder();
            holder.addButton = convertView
                    .findViewById(R.id.addButton);
            holder.nameView = (TextView) convertView
                    .findViewById(R.id.nameView);
            holder.imageView = (ImageView) convertView
                    .findViewById(R.id.roundedImageView1);
            holder.recentView = (TextView) convertView
                    .findViewById(R.id.recentView);
            holder.sepAll = convertView.findViewById(R.id.sepAll);
            holder.sepHalf = convertView.findViewById(R.id.sepHalf);
            holder.tv_all = (TextView) convertView.findViewById(R.id.tv_all);

            holder.layout_all = (RelativeLayout) convertView.findViewById(R.id.layout_all);

            holder.layout_club = (RelativeLayout) convertView.findViewById(R.id.layout_club);

            holder.layout_title = (RelativeLayout) convertView.findViewById(R.id.layout_title);

            convertView.setTag(holder);

            convertView.setLayoutParams(new ListView.LayoutParams(
                    ListView.LayoutParams.MATCH_PARENT,
                    ListView.LayoutParams.WRAP_CONTENT));

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //推荐第一个, 标题
        if (position == mine.size()) {
            holder.layout_title.setVisibility(View.VISIBLE);
        } else {
            holder.layout_title.setVisibility(View.GONE);
        }

        //全部俱乐部
        if (position == getCount() - 1) {
            holder.sepAll.setVisibility(View.VISIBLE);
            holder.layout_all.setVisibility(View.VISIBLE);
        }

        holder.sepAll.setVisibility(View.INVISIBLE);
        holder.layout_all.setVisibility(View.GONE);

        ClubMainListModel item = (ClubMainListModel) getItem(position);

        if (item == null) return convertView;

        //是否显示同城标签
        if (item.is_samecity == 1) {
            holder.addButton.setVisibility(View.VISIBLE);
        } else {
            holder.addButton.setVisibility(View.GONE);
        }
        imageLoader.displayImage(item.image, holder.imageView, options);
        holder.nameView.setText(item.title);
        if (item.topic_title != null) {
            holder.recentView.setText(item.topic_title);
        } else {
            holder.recentView.setText("");
        }
        return convertView;

    }

    public class ViewHolder {
        RelativeLayout layout_all, layout_club, layout_title;
        public ImageView imageView;
        public TextView nameView, recentView, tv_all;
        public View sepAll, sepHalf;
        public View addButton;

    }
}