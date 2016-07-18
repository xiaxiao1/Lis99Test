package com.lis99.mobile.util;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.filter.model.NearbyFilterModel;
import com.lis99.mobile.club.model.TopicSeriesBatchsListModel;
import com.lis99.mobile.club.newtopic.series.model.ManagerSeriesLineListModel;
import com.lis99.mobile.view.MyGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yy on 16/5/30.
 * <p/>
 * PopUpWindow 列表样式
 */
public class PopListAdapter {

//    static private PopListAdapter instance;
//
//    public static PopListAdapter getInstance ()
//    {
//        if ( instance == null ) instance = new PopListAdapter();
//        return instance;
//    }


    public static class TopicActiveSeriesLineAdapter extends MyBaseAdapter {


        private int position = -1;

        public TopicActiveSeriesLineAdapter(Context c, List listItem) {
            super(c, listItem);
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
            this.notifyDataSetChanged();
        }


        @Override
        public View setView(int i, View view, ViewGroup viewGroup) {

            ViewHolder holder = null;

            if (view == null) {
                view = View.inflate(mContext, R.layout.topic_series_line_adapter, null);
                holder = new ViewHolder(view);
                view.setTag(holder);
            }
            else
            {
                holder = (ViewHolder) view.getTag();
            }

            TopicSeriesBatchsListModel.BatchListEntity item = (TopicSeriesBatchsListModel
                    .BatchListEntity) getItem(i);


            String str1 = "第 "+(i + 1)+" 批　　"+item.starttime + "~" + item.endtime;
            String str2 = "集合日期　" + item.settime;
            String str3 = "活动价格　"+item.price + "/"+item.describe;

            holder.tv_1.setText(str1);
            holder.tv_2.setText(str2);
            holder.tv_3.setText(str3);

            int state = 1;

            if ( item.isEnd == 1 )
            {
                state = 3;
            }
            else if ( item.isBaoming == 1 )
            {
                state = 2;
            }
            else if ( position == i )
            {
                state = 4;
            }



            switch ( state )
            {
//                默认
                case 1:
                    holder.iv_icon.setImageBitmap(null);
                    holder.tv_1.setTextColor(mContext.getResources().getColor(R.color.text_color_black) );
                    holder.tv_2.setTextColor(mContext.getResources().getColor(R.color.text_color_black) );
                    String str = "<font color=#525252>活动价格　</font><font color=#ff7800>" + item.price + "/"+item.describe + "</font>";
                    holder.tv_3.setText(Html.fromHtml(str));
                    break;
//                    已报名
                case 2:
                    holder.iv_icon.setImageResource(R.drawable.series_joined);
                    holder.tv_1.setTextColor(mContext.getResources().getColor(R.color.color_nine) );
                    holder.tv_2.setTextColor(mContext.getResources().getColor(R.color.color_nine) );
                    holder.tv_3.setTextColor(mContext.getResources().getColor(R.color.color_nine) );
                    break;
//                报名截止
                case 3:
                    holder.iv_icon.setImageResource(R.drawable.series_join_pass);
                    holder.tv_1.setTextColor(mContext.getResources().getColor(R.color.color_nine) );
                    holder.tv_2.setTextColor(mContext.getResources().getColor(R.color.color_nine) );
                    holder.tv_3.setTextColor(mContext.getResources().getColor(R.color.color_nine) );
                    break;
//                选中
                case 4:
                    holder.iv_icon.setImageResource(R.drawable.active_all_is_chosed);
                    holder.tv_1.setTextColor(mContext.getResources().getColor(R.color.text_color_green) );
                    holder.tv_2.setTextColor(mContext.getResources().getColor(R.color.text_color_green) );
                    holder.tv_3.setTextColor(mContext.getResources().getColor(R.color.text_color_green) );
                    break;
            }



            return view;
        }

        public static class ViewHolder {
            public View rootView;
            public ImageView iv_icon;
            public TextView tv_1;
            public TextView tv_2;
            public TextView tv_3;
            public RelativeLayout layoutMain;

            public ViewHolder(View rootView) {
                this.rootView = rootView;
                this.iv_icon = (ImageView) rootView.findViewById(R.id.iv_icon);
                this.tv_1 = (TextView) rootView.findViewById(R.id.tv_1);
                this.tv_2 = (TextView) rootView.findViewById(R.id.tv_2);
                this.tv_3 = (TextView) rootView.findViewById(R.id.tv_3);
                this.layoutMain = (RelativeLayout) rootView.findViewById(R.id.layoutMain);
            }

        }
    }

    public static class TopicActiveSeriesLineManagerAdapter extends MyBaseAdapter {

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
            this.notifyDataSetChanged();
        }

        private int position = -1;

        public TopicActiveSeriesLineManagerAdapter(Context c, List listItem) {
            super(c, listItem);
        }

        @Override
        public View setView(int i, View view, ViewGroup viewGroup)
        {
            Holder holder = null;
            if ( view == null )
            {
                view = View.inflate(mContext, R.layout.topic_active_series_manager, null);
                holder = new Holder();
                holder.tv_all = (TextView) view.findViewById(R.id.tv_all);
                holder.tv_select = (ImageView) view.findViewById(R.id.tv_select);

                view.setTag(holder);
            }
            else {
                holder = (Holder) view.getTag();
            }

//            HashMap<String, String> map = (HashMap<String, String>) getItem(i);

            ManagerSeriesLineListModel.BatchListEntity item = (ManagerSeriesLineListModel
                    .BatchListEntity) getItem(i);

            if ( item == null ) return view;

            if ( i == position )
            {
                holder.tv_select.setVisibility(View.VISIBLE);
                holder.tv_all.setTextColor(mContext.getResources().getColor(R.color.text_color_green));
            }
            else
            {
                holder.tv_select.setVisibility(View.INVISIBLE);
                holder.tv_all.setTextColor(mContext.getResources().getColor(R.color.text_color_black));
            }

            if ( i == 0 )
            {
                holder.tv_all.setText("全部批次");
                return view;
            }


            holder.tv_all.setText("第"+(i) + "批  "+item.starttime+"~"+item.endtime);

            return view;
        }

        class Holder
        {
            TextView tv_all;
            ImageView tv_select;
        }



    }

//  附近活动排序
    public static class NearByActiveTime extends MyBaseAdapter {

        private int postion;

        public NearByActiveTime(Context c, List listItem) {
            super(c, listItem);
        }

        public void setposition(int postion)
        {
            this.postion = postion;
        }

        public int getPostion()
        {
            return postion;
        }

        @Override
        public View setView(int i, View view, ViewGroup viewGroup) {

            if (view == null) {
                view = View.inflate(mContext, R.layout.pop_window_nearby_time_item, null);
                view.setTag(new ViewHolder(view));
            }
            initializeViews(getItem(i), (ViewHolder) view.getTag(),i);
            return view;
        }

        private void initializeViews(Object object, ViewHolder holder, int i) {
            //TODO implement
            HashMap<String, String> map = (HashMap<String, String>) object;
            holder.tvAll.setText(map.get("name"));
            if ( postion == i )
            {
                holder.tvAll.setTextColor(mContext.getResources().getColor(R.color.text_color_green));
            }
            else
            {
                holder.tvAll.setTextColor(mContext.getResources().getColor(R.color.text_color_black));
            }


        }

        protected class ViewHolder {
            private TextView tvAll;

            public ViewHolder(View view) {
                tvAll = (TextView) view.findViewById(R.id.tv_all);
            }
        }
    }
//  附近活动筛选
    public static class NearbyFilter extends MyBaseAdapter
    {

        private List<NearbyFilterGrid> adapters;

        public NearbyFilter(Context c, List listItem) {
            super(c, listItem);
            adapters = new ArrayList<NearbyFilterGrid>();
        }

        public HashMap<String, String> getSelect ()
        {
            HashMap<String, String> map = new HashMap<>();
            for ( int i = 0; i < listItem.size(); i++ )
            {
                NearbyFilterModel.SievenlistEntity item = (NearbyFilterModel.SievenlistEntity) getItem(i);
                if ( adapters.size() <= i )
                {
                    map.put(""+item.id, "");
                }
                else
                {
                    map.put(""+item.id, adapters.get(i).getSelectId());
                }
            }
            return map;
        }

        public void reSetSelect ()
        {
            for ( NearbyFilterGrid grid : adapters )
            {
                grid.resetSelect();
            }
        }

        @Override
        public View setView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = View.inflate(mContext,R.layout.nearby_filter_list_item, null);
                view.setTag(new ViewHolder(view));
            }
            initializeViews(getItem(i), (ViewHolder) view.getTag());
            return view;
        }

        private void initializeViews(Object object, ViewHolder holder) {
            //TODO implement
            NearbyFilterModel.SievenlistEntity item = (NearbyFilterModel.SievenlistEntity) object;
            if ( item == null ) return;
            holder.title.setText(item.name);
            if ( item.lists != null )
            {
                final NearbyFilterGrid adapter = new NearbyFilterGrid(mContext, item.lists);
                holder.grid.setAdapter(adapter);
                adapters.add(adapter);

                holder.grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        NearbyFilterModel.SievenlistEntity.ListsEntity item = (NearbyFilterModel
                                .SievenlistEntity.ListsEntity) adapter.getItem(position);
                        int select = adapter.getSelect();
                        if ( item == null || select == position ) return;

                        item.isSelect = 1;

                        if ( select != -1 )
                        {
                            item = (NearbyFilterModel
                                    .SievenlistEntity.ListsEntity) adapter.getItem(select);
                            item.isSelect = 0;
                        }
                        adapter.notifyDataSetChanged();


                    }
                });

            }

        }

        protected class ViewHolder {
            private TextView title;
            private MyGridView grid;

            public ViewHolder(View view) {
                title = (TextView) view.findViewById(R.id.title);
                grid = (MyGridView) view.findViewById(R.id.grid);
            }
        }
    }
    //  附近活动筛选
    public static class NearbyFilterGrid extends MyBaseAdapter
    {

        public NearbyFilterGrid(Context c, List listItem) {
            super(c, listItem);
        }

        private int select = -1;

        public int getSelect() {
            return select;
        }

        public void setSelect(int select) {
            this.select = select;
        }
//      重置选择
        public void resetSelect ()
        {

            if ( select != -1 )
            {
                NearbyFilterModel.SievenlistEntity.ListsEntity item = (NearbyFilterModel
                        .SievenlistEntity.ListsEntity) getItem(select);
                item.isSelect = 0;
                notifyDataSetChanged();
            }
        }
//      获取选择的Id
        public String getSelectId ()
        {
            if ( select != -1 )
            {
                NearbyFilterModel.SievenlistEntity.ListsEntity item = (NearbyFilterModel
                        .SievenlistEntity.ListsEntity) getItem(select);
                return ""+item.id;
            }
            return "";
        }

        @Override
        public View setView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = View.inflate(mContext, R.layout.nearby_filter_grid_item, null);
                view.setTag(new ViewHolder(view));
            }
            initializeViews(getItem(i), (ViewHolder) view.getTag(), i);
            return view;
        }

        private void initializeViews(Object object, ViewHolder holder, int i) {
            //TODO implement
            NearbyFilterModel.SievenlistEntity.ListsEntity item = (NearbyFilterModel
                    .SievenlistEntity.ListsEntity) object;

            if ( item == null ) return;
            holder.name.setText(item.name);

            if ( item.isSelect == 1 )
            {
                setSelect(i);
                holder.name.setBackgroundResource(R.drawable.destination_select);
            }
            else
            {
                holder.name.setBackgroundResource(R.drawable.destination_nomal);
            }

        }

        protected class ViewHolder {
            private TextView name;

            public ViewHolder(View view) {
                name = (TextView) view.findViewById(R.id.name);
            }
        }
    }



}
