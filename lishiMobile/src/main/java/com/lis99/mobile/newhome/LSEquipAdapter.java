package com.lis99.mobile.newhome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.ClubSpecialListActivity;
import com.lis99.mobile.club.LSClubTopicActivity;
import com.lis99.mobile.club.LSClubTopicNewActivity;
import com.lis99.mobile.webview.MyActivityWebView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by zhangjie on 9/19/15.
 */
public class LSEquipAdapter extends BaseAdapter {

    public List<LSEquipContent> getContents() {
        return contents;
    }

    public void setContents(List<LSEquipContent> contents) {
        this.contents = contents;
    }

    List<LSEquipContent> contents;
    private Context c;
    DisplayImageOptions options;
    LayoutInflater inflater;

    ImageLoader imageLoader = ImageLoader.getInstance();



    private void buildOptions() {
        options = new DisplayImageOptions.Builder()
//				.showImageOnLoading(R.drawable.select_item_default)
//				.showImageForEmptyUri(R.drawable.select_item_default)
//				.showImageOnFail(R.drawable.select_item_default)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
    }


    public LSEquipAdapter(Context context, List<LSEquipContent> contents){
        this.contents = contents;
        inflater = LayoutInflater.from(context);
        c = context;
        buildOptions();
    }


    private final static int ITEM_TYPE = 0;
    private final static int HEADER_TYPE = 1;
    private final static int FOOTER_TYPE = 2;


    @Override
    public int getViewTypeCount() {
        return 3;
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


        LSEquipContent content = (LSEquipContent) getItem(position);

        if (content.getType() == LSEquipContent.FREE_TYPE || content.getType() == LSEquipContent.CHANGE_TYPE) {
            return ITEM_TYPE;
        }

        if (content.getType() == LSEquipContent.FREE_HEADER || content.getType() == LSEquipContent.CHANGE_HEADER) {
            return HEADER_TYPE;
        }

        if (content.getType() == LSEquipContent.FREE_FOOTER || content.getType() == LSEquipContent.CHANGE_FOOTER) {
            return FOOTER_TYPE;
        }




        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LSEquipContent content = (LSEquipContent) getItem(position);

        int type = getItemViewType(position);

        switch (type) {
            case ITEM_TYPE:
                return configureItemView(content, convertView, parent);
            case HEADER_TYPE:
                return configureHeaderView(content, convertView, parent);
            case FOOTER_TYPE:
                return configureFooterView(content, convertView, parent);
            default:
                return configureItemView(content, convertView, parent);
        }
    }

    private View configureItemView(final LSEquipContent content, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.ls_equip_content_item, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            holder.titleView = (TextView) convertView.findViewById(R.id.titleView);

            holder.descView = (TextView) convertView.findViewById(R.id.descView);
            holder.priceView = (TextView) convertView.findViewById(R.id.priceView);
            holder.tagview = (ImageView) convertView.findViewById(R.id.actionButton);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        imageLoader.displayImage(content.getImages(), holder.imageView, options);
        holder.titleView.setText(content.getTitle());
        holder.descView.setText(content.getContent());
        if (content.getType() == LSEquipContent.FREE_TYPE) {
            holder.priceView.setText("市场价：" + content.getMarket_price() + "元");
            holder.tagview.setBackgroundResource(R.drawable.ls_equip_free_btn);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ( "2".equals(content.getTopic_type()) )
                    {
                        Intent intent = new Intent(c, LSClubTopicNewActivity.class);
                        intent.putExtra("topicID", content.getTopicid());
                        c.startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(c, LSClubTopicActivity.class);
                        intent.putExtra("topicID", content.getTopicid());
                        c.startActivity(intent);
                    }
                }
            });

        } else {
            holder.priceView.setText(content.getIntegral() + " 积分");
            holder.tagview.setBackgroundResource(R.drawable.ls_equip_change_btn);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int id = 0;
                    String Userid = DataManager.getInstance().getUser().getUser_id();
                    if (!TextUtils.isEmpty(Userid))
                    {
                        id = Integer.parseInt(Userid);
                    }

                    Intent intent = new Intent(c, MyActivityWebView.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("URL", "http://m.lis99.com/club/integralshop/goodDetail/"+content.getId()+"/"+id);
                    bundle.putString("TITLE", "积分商城");
                    intent.putExtras(bundle);
                    c.startActivity(intent);
                }
            });

        }

        return convertView;
    }


    private View configureHeaderView(LSEquipContent content, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.ls_equip_free_header, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (content.getType() == LSEquipContent.FREE_HEADER) {
            holder.imageView.setImageResource(R.drawable.ls_equip_free_header);
        } else {
            holder.imageView.setImageResource(R.drawable.ls_equip_change_header);
        }

        return convertView;
    }

    private View configureFooterView(LSEquipContent content, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.ls_equip_free_footer, null);
            holder = new ViewHolder();
            holder.button = (Button) convertView.findViewById(R.id.actionButton);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (content.getType() == LSEquipContent.FREE_FOOTER) {
            holder.button.setText("更多免费装备");

            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(c, ClubSpecialListActivity.class);
                    intent.putExtra("tagid", 6);
                    c.startActivity(intent);
                }
            });

        } else {
            holder.button.setText("更多换购装备");

            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    商城
                    int id = 0;
                    String Userid = DataManager.getInstance().getUser().getUser_id();
                    if (!TextUtils.isEmpty(Userid))
                    {
                        id = Integer.parseInt(Userid);
                    }
//				商城
                    Intent intent = new Intent(c, MyActivityWebView.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("URL", "http://m.lis99.com/club/integralshop/goodList/"+id);
                    bundle.putString("TITLE", "积分商城");
                    intent.putExtras(bundle);
                    c.startActivity(intent);
                }
            });

        }

        return convertView;
    }


    static class ViewHolder{
        ImageView imageView, tagview;
        TextView titleView;
        TextView descView;
        TextView priceView;
        Button button;
    }
}
