package com.lis99.mobile.newhome.equip;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.model.EquipInfoModel;
import com.lis99.mobile.club.model.KeyValueModel;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.MyBaseAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by yy on 15/9/23.
 */
public class PropertyAdapter extends MyBaseAdapter{

    private final int IMG = 1;

    private final int property = 0;

    private final int count = 2;

    private int state = 0;

    public PropertyAdapter(Context c, ArrayList listItem) {
        super(c, listItem);
    }

    @Override
    public int getItemViewType(int position) {
        Object o = getItem(position);
        if ( o instanceof KeyValueModel )
        {
            return property;
        }
        else if ( o instanceof  EquipInfoModel.Zhuangbeiimg )
        {
            return IMG;
        }
        return property;

    }

    @Override
    public int getViewTypeCount() {
        return count;
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {

        state = getItemViewType(i);

        switch (state)
        {
            case property:
                return property(i, view);
            case IMG:
                return img(i, view);
        }
        return view;
    }

    private View property (int i, View view)
    {
        Holder holder = null;

        if ( view == null )
        {
            view = View.inflate(mContext, R.layout.ls_equip_property_item, null);
            holder = new Holder();

            holder.tv_key = (TextView) view.findViewById(R.id.tv_key);
            holder.tv_value = (TextView) view.findViewById(R.id.tv_value);
            holder.layout_value = (LinearLayout) view.findViewById(R.id.layout_value);
            holder.layout_name = (LinearLayout) view.findViewById(R.id.layout_name);

            view.setTag(holder);
        }
        else
        {
            holder = (Holder) view.getTag();
        }

        KeyValueModel item = (KeyValueModel) getItem(i);

        if ( item == null ) return view;

        if ( item.isLast )
        {
            holder.layout_name.setBackgroundResource(R.drawable.equip_property_bottom_left);
            holder.layout_value.setBackgroundResource(R.color.white);
        }
        else
        {
            holder.layout_name.setBackgroundResource(R.drawable.equip_property_top_left);
            holder.layout_value.setBackgroundResource(R.drawable.equip_property_top_right);
        }

        holder.tv_key.setText(item.key);
        holder.tv_value.setText(item.valule);



        return view;
    }

    private View img (int i, View view)
    {
        Holder holder = null;

        if ( view == null )
        {
            view = View.inflate(mContext, R.layout.ls_equip_property_item_img, null);
            holder = new Holder();

            holder.img = (ImageView) view.findViewById(R.id.img);
            holder.iv_load = (ImageView) view.findViewById(R.id.iv_load);
            view.setTag(holder);

        }
        else
        {
            holder = (Holder) view.getTag();
        }

        EquipInfoModel.Zhuangbeiimg item = (EquipInfoModel.Zhuangbeiimg) getItem(i);

        if ( item == null ) return view;

        ImageLoader.getInstance().displayImage(item.images, holder.img, ImageUtil.getclub_topic_imageOptions(), ImageUtil.getImageLoading(holder.iv_load, holder.img));

        return view;
    }

    class Holder
    {
        LinearLayout layout_value, layout_name;
        TextView tv_key, tv_value;
        ImageView img, iv_load;
    }

}
