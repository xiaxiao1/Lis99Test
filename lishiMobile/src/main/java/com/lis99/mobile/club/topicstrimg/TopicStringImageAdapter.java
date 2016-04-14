package com.lis99.mobile.club.topicstrimg;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.MyBaseAdapter;
import com.lis99.mobile.util.dbhelp.StringImageChildModel;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by yy on 16/3/30.
 */
public class TopicStringImageAdapter extends MyBaseAdapter {


    private final int TITLE = 0;
//    可以删除的
    private final int IMG_STRING_NOMAL = 1;
//    不可以删除的
    private final int IMG_STRING_NO_REMOVE = 2;

    private final int COUNT = 3;

    private LayoutInflater layoutInflater;

    private LSTopicStringImageActivity main;

    public TopicStringImageAdapter(Context c, List listItem) {
        super(c, listItem);

        this.layoutInflater = LayoutInflater.from(c);
    }

    public void setMain ( LSTopicStringImageActivity _main )
    {
        main = _main;
    }


    @Override
    public int getItemViewType(int position) {

        if ( position == 0 )
        {
            return TITLE;
        }

        StringImageChildModel item = (StringImageChildModel) getItem(position);

        if ( item != null )
        {
            if ( item.isEditing == 0 )
            {
                return IMG_STRING_NOMAL;
            }
            else
            {
                return IMG_STRING_NO_REMOVE;
            }
        }


        return TITLE;
    }

    @Override
    public int getViewTypeCount() {
        return COUNT;
    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {

        int num = getItemViewType(i);

        switch ( num )
        {
            case TITLE:
                return getTitle(i, view);
            case IMG_STRING_NOMAL:
                return getAddImageStringNomal(i, view);
            case IMG_STRING_NO_REMOVE:
                return getAddImageStringNoRemove(i, view);
            default:
                return getTitle(i, view);
        }

    }



//  标题
    private View getTitle ( int i, View view )
    {
        ViewHolderTitle holder = null;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.topic_img_string_title_adapter, null);
            holder = new ViewHolderTitle(view);
            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolderTitle) view.getTag();
        }





        return view;
    }
    //可删除的图文混排
    private View getAddImageStringNomal (final int i, View view )
    {
        ViewHolderNomal holder = null;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.topic_img_string_adapter, null);
            holder = new ViewHolderNomal(view);
            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolderNomal) view.getTag();
        }

        holder.layoutAdded.setVisibility(View.GONE);

        final StringImageChildModel item = (StringImageChildModel) getItem(i);

        if ( item == null ) return view;

        final ViewHolderNomal finalHolder = holder;

        if (TextUtils.isEmpty(item.img))
        {
            holder.layoutAdded.setVisibility(View.GONE);
            holder.layoutAdd.setVisibility(View.VISIBLE);
            holder.layoutAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    main.addImage(i);
                }
            });
        }
        else
        {
            holder.layoutAdd.setVisibility(View.GONE);
            holder.layoutAdded.setVisibility(View.VISIBLE);
//            删除按钮
            holder.ivRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finalHolder.layoutAdded.setVisibility(View.GONE);
                    finalHolder.layoutAdd.setVisibility(View.VISIBLE);
                    finalHolder.ivImage.setImageBitmap(null);
                    if ( ImageUtil.deleteNativeImg(item.img) )
                    {
                        Common.toast("OK");
                    }
                    else
                    {
                        Common.toast("ERROR");
                    }
                    item.img = null;
                    removeAt(i);

                }
            });

            if ( item.img.startsWith("/"))
            {
                item.img = "file://" + item.img;
            }
            ImageLoader.getInstance().displayImage(item.img, holder.ivImage, ImageUtil
                    .getDefultTravelImageOptions());

//            if ( holder.ivImage.getTag(1) != item.id )
//            {
//                Bitmap b = ImageUtil.Bytes2Bimap(item.imgInfo);
//                holder.ivImage.setImageBitmap(b);
//                holder.ivImage.setTag(1, item.id);
//                holder.ivImage.setTag(2, b);

//            }
//            else
//            {
//                holder.ivImage.setImageBitmap((Bitmap) holder.ivImage.getTag(2));
//            }

        }


        return view;
    }

    //不可删除的图文混排
    private View getAddImageStringNoRemove ( int i, View view )
    {
        ViewHolderNoRemove holder = null;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.topic_img_string_no_remove_adapter, null);
            holder = new ViewHolderNoRemove(view);
            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolderNoRemove) view.getTag();
        }




        return view;
    }



//  标题
    protected class ViewHolderTitle {
        private EditText editInfo;

        public ViewHolderTitle(View view) {
            editInfo = (EditText) view.findViewById(R.id.edit_info);
        }
    }

//  可修改内容的
    protected class ViewHolderNomal {
        private EditText editInfo;
        private ImageView ivPen;
        private RelativeLayout layoutAdd;
        private ImageView ivAdd;
        private RelativeLayout layoutAdded;
        private ImageView ivImage;
        private ImageView ivRemove;

        public ViewHolderNomal(View view) {
            editInfo = (EditText) view.findViewById(R.id.edit_info);
            ivPen = (ImageView) view.findViewById(R.id.iv_pen);
            layoutAdd = (RelativeLayout) view.findViewById(R.id.layout_add);
            ivAdd = (ImageView) view.findViewById(R.id.iv_add);
            layoutAdded = (RelativeLayout) view.findViewById(R.id.layout_added);
            ivImage = (ImageView) view.findViewById(R.id.iv_image);
            ivRemove = (ImageView) view.findViewById(R.id.iv_remove);
        }
    }
//  不可修改内容的
    protected class ViewHolderNoRemove {
        private TextView tvInfo;
        private ImageView ivImage;

        public ViewHolderNoRemove(View view) {
            tvInfo = (TextView) view.findViewById(R.id.tv_info);
            ivImage = (ImageView) view.findViewById(R.id.iv_image);
        }
    }


}
