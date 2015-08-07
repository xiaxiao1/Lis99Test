package com.lis99.mobile.club.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.LSClubDetailActivity;
import com.lis99.mobile.club.LSClubListActivity;
import com.lis99.mobile.club.model.ClubMainListModel;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.LSRequestManager;
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
        if ( recommend != null )
        {
            this.clubs.addAll(recommend);
        }

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

    @Override
    public int getCount() {
        int rnum = (recommend == null ) ? 0 : recommend.size();
        int i = rnum > 5 ? 5 : rnum;
        int num = mine.size() + i;
        return num;
    }

    @Override
    public Object getItem(int position) {

        return clubs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        ClubMainListModel item = (ClubMainListModel) getItem(position);
        int state = item.type;
        return state;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        switch (type)
        {
            case NEEDLOGIN:
                return getneedLogin(position, convertView);
            case NOJOINCLUB:
                return getneedAdd(position, convertView);
            case JOINEDCLUB:
                return getMineClub(position, convertView);
            case RECOMMENDCLUB:
                return getRecommendClub(position, convertView);

        }

        return convertView;
    }

    private View getMineClub(int position, View convertView ) {
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

            viewHolder.layout_all = (RelativeLayout) convertView.findViewById(R.id.layout_all);

            viewHolder.layout_club = (RelativeLayout) convertView.findViewById(R.id.layout_club);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        if ( getCount() == mine.size() - 1 ) {
//            viewHolder.sepAll.setVisibility(View.VISIBLE);
//            viewHolder.layout_all.setVisibility(View.VISIBLE);
//        } else {
            viewHolder.sepAll.setVisibility(View.GONE);
            viewHolder.layout_all.setVisibility(View.GONE);
//        }

        final ClubMainListModel item = (ClubMainListModel) getItem(position);

        if ( item == null ) return convertView;

        viewHolder.layout_club.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LSClubDetailActivity.class);
                intent.putExtra("clubID", item.id);
                context.startActivity(intent);
            }
        });

        //线的显示
        imageLoader.displayImage(item.image,
                viewHolder.imageView, options);
        viewHolder.nameView.setText(item.title);
        //描述
        viewHolder.recentView.setText(item.topic_title);

        return convertView;
    }

    private View getRecommendClub(final int position, View convertView ) {
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

            holder.btn_join = (Button) convertView.findViewById(R.id.btn_join);

            convertView.setTag(holder);

            convertView.setLayoutParams(new ListView.LayoutParams(
                    ListView.LayoutParams.MATCH_PARENT,
                    ListView.LayoutParams.WRAP_CONTENT));

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ClubMainListModel item = (ClubMainListModel) getItem(position);

        if ( item == null ) return convertView;

        holder.btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Common.isLogin((Activity) context)) {
                    addClub(item);
                }
            }
        });

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
        else
        {
            holder.sepAll.setVisibility(View.INVISIBLE);
            holder.layout_all.setVisibility(View.GONE);
        }

        holder.layout_club.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
					Intent intent = new Intent(context, LSClubDetailActivity.class);
					intent.putExtra("clubID", item.id);
                context.startActivity(intent);
            }
        });


        holder.layout_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LSClubListActivity.class);
                context.startActivity(intent);
            }
        });


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
//需要登陆
    private View getneedLogin ( int position, View convertView  )
    {
        if ( convertView == null )
        {
            convertView = View.inflate(context, R.layout.club_main_need_login, null);
        }

        convertView.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.isLogin((Activity) context);
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.isLogin((Activity)context);
            }
        });

        return convertView;
    }
//没有加入俱乐部
    private View getneedAdd ( int position, View convertView  )
    {
        if ( convertView == null )
        {
            convertView = View.inflate(context, R.layout.club_main_need_add, null);
        }

        convertView.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LSClubListActivity.class);
                context.startActivity(intent);
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LSClubListActivity.class);
					context.startActivity(intent);
            }
        });

        return  convertView;
    }

    private void addClub ( final ClubMainListModel item )
    {
        LSRequestManager.getInstance().addClub("" + item.id, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                setRecommendclub2Mine(item);
            }
        });
    }

    private void setRecommendclub2Mine ( ClubMainListModel item )
    {
        //去掉未添加俱乐部显示
        if ( mine.size() == 1 )
        {
            ClubMainListModel info = mine.get(0);
            if ( info.type == NOJOINCLUB )
            {
                mine.remove(0);
            }
        }

        if ( recommend.remove(item) )
        {
            item.type = JOINEDCLUB;
            mine.add(item);
            clubs.clear();
            clubs.addAll(mine);
            clubs.addAll(recommend);
        }
        notifyDataSetChanged();
    }

    public class ViewHolder {
        RelativeLayout layout_all, layout_club, layout_title;
        public ImageView imageView;
        public TextView nameView, recentView, tv_all;
        public View sepAll, sepHalf;
        public View addButton;
        Button btn_join;

    }
}