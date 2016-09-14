package com.lis99.mobile.newhome;

import android.content.Context;

import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaxiao on 2016.09.14.
 * 新版福利社adapter
 */
public class LSWelfareAdapter extends BaseAdapter {



    List<Object> contents;
    private Context c;
    List<String> goods = new ArrayList<String>();

    LayoutInflater inflater;

    private static final int ITEM_TYPE_WELFARE=0;
    private static final int ITEM_TYPE_HUANGOU=1;

    public LSWelfareAdapter(Context context, List<Object> contents){
        this.contents = contents;
        inflater = LayoutInflater.from(context);
        c = context;
        for (int i=0;i<10;i++) {
            goods.add("cdcdc " + i);
        }
    }
    public List<Object> getContents() {
        return contents;
    }

    public void setContents(List<Object> contents) {
        this.contents = contents;
    }





    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return contents == null ? 0 : contents.size();
    }

    @Override
    public Object getItem(int position) {
        return contents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (position==1) {
            return ITEM_TYPE_HUANGOU;
        }
        return ITEM_TYPE_WELFARE;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int type = getItemViewType(position);
        Object obj = contents.get(position);
        switch (type) {
            case ITEM_TYPE_WELFARE:
                return getWelfareView(obj, convertView, parent);
            case ITEM_TYPE_HUANGOU:
                return getHuanGouView(obj, convertView, parent);
            default:
                return null;
        }
    }

    private View getWelfareView(final Object obj, View convertView, ViewGroup parent) {
        WelfareViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_listview_of_new_edition_equip, null);
            holder = new WelfareViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (WelfareViewHolder) convertView.getTag();
        }
        holder.itemLogoDescTv.setText((String)obj);

        return convertView;
    }


    private View getHuanGouView(final Object obj, View convertView, ViewGroup parent) {
        HuanGouViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.jifenhuangou_of_new_equip, null);
            holder = new HuanGouViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (HuanGouViewHolder) convertView.getTag();
        }

        JiFenhuangouAdapter jAdapter = new JiFenhuangouAdapter(c,goods );
        holder.jifenhuangouRc.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        holder.jifenhuangouRc.setAdapter(jAdapter);

        return convertView;
    }



    protected class WelfareViewHolder {
        private View viewBar;
        private ImageView itemBgImg;
        private ImageView itemTypeImg;
        private TextView itemLogoDescTv;
        private TextView itemJoinTv;

        public WelfareViewHolder(View view) {
            viewBar = (View) view.findViewById(R.id.view_bar);
            itemBgImg = (ImageView) view.findViewById(R.id.item_bg_img);
            itemTypeImg = (ImageView) view.findViewById(R.id.item_type_img);
            itemLogoDescTv = (TextView) view.findViewById(R.id.item_logo_desc_tv);
            itemJoinTv = (TextView) view.findViewById(R.id.item_join_tv);
        }
    }

    protected class HuanGouViewHolder {

        private RecyclerView jifenhuangouRc;

        public HuanGouViewHolder(View view) {

            jifenhuangouRc = (RecyclerView) view.findViewById(R.id.jifenhuangou_rc);
        }
    }


}
