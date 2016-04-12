package com.lis99.mobile.club.topicstrimg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.util.MyBaseAdapter;
import com.lis99.mobile.util.dbhelp.StringImageChildModel;

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

    public TopicStringImageAdapter(Context c, List listItem) {
        super(c, listItem);

        this.layoutInflater = LayoutInflater.from(c);
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
    private View getAddImageStringNomal ( int i, View view )
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

//        holder.layoutAdded.setVisibility(View.GONE);



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
