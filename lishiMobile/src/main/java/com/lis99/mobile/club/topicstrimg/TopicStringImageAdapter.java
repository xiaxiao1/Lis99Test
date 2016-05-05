package com.lis99.mobile.club.topicstrimg;

import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.util.DialogManager;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.MyBaseAdapter;
import com.lis99.mobile.util.dbhelp.DataHelp;
import com.lis99.mobile.util.dbhelp.StringImageChildModel;
import com.lis99.mobile.util.emotion.MyEmotionsUtil;
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

    private final int EDIT_INFO = 3;

    private final int NULL_VIEW = 4;

    private final int COUNT = 5;

    private LayoutInflater layoutInflater;

    private LSTopicStringImageActivity main;

    private int position = -1;

//    private EditText currentEdit;


    public TopicStringImageAdapter(Context c, List listItem) {
        super(c, listItem);

        this.layoutInflater = LayoutInflater.from(c);
    }

    public void setMain ( LSTopicStringImageActivity _main )
    {
        main = _main;
    }


    @Override
    public int getCount() {
        return  (listItem == null ) ? 0 : listItem.size() * 2 - 1;
    }

    @Override
    public Object getItem(int i) {
        if ( i == 0 )
        {
            return (listItem == null || listItem.size() == 0 ) ? null : listItem.get(i);
        }
        else
        {
            return (listItem == null || listItem.size() == 0 ) ? null : listItem.get(getItemIndexWithPosition(i));
        }
    }

//    根据当前Position View 获取实体内容对应位置
    private int getItemIndexWithPosition ( int position )
    {
        int num = (position + 1) / 2;
        return num;
    }

    @Override
    public int getItemViewType(int position) {

        Object o = getItem(position);

        StringImageChildModel item = (StringImageChildModel) o;

        if ( position == 0 )
        {
            return TITLE;
        }
//        else if ( o instanceof String )
//        {
//            return EDIT_INFO;
//        }
        else if ( position == 1 )
        {
            return EDIT_INFO;
        }

        else if ( position % 2 == 1 )
        {
            if ( TextUtils.isEmpty(item.img) )
            {
                return NULL_VIEW;
            }
            return EDIT_INFO;
        }

        else if ( item != null )
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
            case EDIT_INFO:
                return getEditInfo(i, view);
            case NULL_VIEW:
                return getNullView(view);
            default:
                return getTitle(i, view);
        }

    }
//空View
    private View getNullView (View view)
    {
        if ( view == null )
        {
            LinearLayout l = new LinearLayout(mContext);
            l.setBackgroundResource(R.color.white);//mContext.getResources().getColor(R.color.white));
            return l;
        }
        return view;
    }

    //  输入内容
    private View getEditInfo ( int i, View view )
    {
        ViewEditHolder holder = null;
        if ( view == null )
        {
            view = View.inflate(mContext, R.layout.topic_edit_info_adapter, null );
            holder = new ViewEditHolder(view);
            holder.editInfo.setTag(i);
            new EditListener(holder.editInfo, holder.ivPen);
            view.setTag(holder);

        }
        else
        {
            holder = (ViewEditHolder) view.getTag();
            holder.editInfo.setTag(i);
        }

        final StringImageChildModel item = (StringImageChildModel) getItem(i);

        if ( item == null ) return view;

        holder.editInfo.setText(MyEmotionsUtil.getInstance().getTextWithEmotion(main, item.content));

        holder.editInfo.clearFocus();

        if ( position == i )
        {
            holder.editInfo.requestFocus();


            if (!TextUtils.isEmpty(holder.editInfo.getText().toString()))
            {
                holder.editInfo.setSelection(holder.editInfo.getText().toString().length());
            }

        }


        return view;
    }

    //  标题
    private View getTitle ( int i, View view )
    {
        ViewHolderTitle holder = null;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.topic_img_string_title_adapter, null);
            holder = new ViewHolderTitle(view);
            view.setTag(holder);
            holder.editInfo.setTag(i);
            new EditListener(holder.editInfo, null);

        }
        else
        {
            holder = (ViewHolderTitle) view.getTag();
            holder.editInfo.setTag(i);
        }

        StringImageChildModel item = (StringImageChildModel) getItem(i);

        holder.editInfo.setText(item.content);

        if ( main.getIsAdd() )
        {
            holder.editInfo.clearFocus();
            holder.editInfo.setClickable(false);
            holder.editInfo.setFocusable(false);
            holder.editInfo.setFocusableInTouchMode(false);
        }
        else
        {
            holder.editInfo.clearFocus();

            if ( position == i )
            {
                holder.editInfo.requestFocus();

                if (!TextUtils.isEmpty(holder.editInfo.getText().toString()))
                {
                    holder.editInfo.setSelection(holder.editInfo.getText().toString().length());
                }

            }
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
                    main.addImage( getItemIndexWithPosition(i) );
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

                    DialogManager.getInstance().startAlert(main, "删除", "将要删除这张图片及配文，确定吗?", true, "删除", new DialogInterface.OnClickListener() {


                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finalHolder.layoutAdded.setVisibility(View.GONE);
                            finalHolder.layoutAdd.setVisibility(View.VISIBLE);
                            finalHolder.ivImage.setImageBitmap(null);
//                    if ( ImageUtil.deleteNativeImg(item.img) )
//                    {
//                        Common.toast("OK");
//                    }
//                    else
//                    {
//                        Common.toast("ERROR");
//                    }

                            DataHelp.getInstance().removeItem(item);
                            item.content = "";
                            item.img = null;
                            removeAt(getItemIndexWithPosition(i));
                        }
                    }, true, "保留", null);


                }
            });

            if ( item.img.startsWith("/"))
            {
                item.img = "file://" + item.img;
            }
            ImageLoader.getInstance().displayImage(item.img, holder.ivImage, ImageUtil
                    .getDefultImageOptions());
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


        StringImageChildModel item = (StringImageChildModel) getItem(i);

        holder.tvInfo.setText(MyEmotionsUtil.getInstance().getTextWithEmotion(main, item.content));

        ImageLoader.getInstance().displayImage(item.img, holder.ivImage, ImageUtil
                .getDefultImageOptions());


        return view;
    }

    protected class ViewEditHolder
    {
        private EditText editInfo;
        private ImageView ivPen;

        public ViewEditHolder(View view) {
            editInfo = (EditText) view.findViewById(R.id.edit_info);
            ivPen = (ImageView) view.findViewById(R.id.iv_pen);
        }
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
        private RelativeLayout layoutAdd;
        private ImageView ivAdd;
        private RelativeLayout layoutAdded;
        private ImageView ivImage;
        private ImageView ivRemove;

        public ViewHolderNomal(View view) {

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


    class EditListener
    {
        private StringImageChildModel childModel;
        private EditText et;
        private TextWatcher tw;
        private View viewPen;

        public EditListener( EditText et, View view ) {
            this.et = et;
            this.viewPen = view;

            listener();
        }

        private void listener ()
        {

            MyEmotionsUtil.getInstance().setVisibleEmotion(callBack);
            MyEmotionsUtil.getInstance().initView(main, et, main.bottomBar_emotion, main.emoticonsCover, main.parentLayout);

            tw = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                    Common.log("before content == "+s.toString());
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    childModel.content = s.toString();
//                    Common.log("changed content == "+s.toString());

                    int position = (int) et.getTag();
                    childModel = (StringImageChildModel) getItem(position);
                    childModel.content = s.toString();
                    if ( viewPen == null ) return;
                    if ( !TextUtils.isEmpty(childModel.content))
                    {
                        viewPen.setVisibility(View.GONE);
                    }
                    else
                    {
                        viewPen.setVisibility(View.VISIBLE);
                    }
//                    Common.log("onTextChanged content == "+position+childModel.content);

                }

                @Override
                public void afterTextChanged(Editable s) {


                }
            };

            et.addTextChangedListener(tw);

            et.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if ( event.getAction() == MotionEvent.ACTION_UP )
                    {
                        position = (int) et.getTag();

//                        currentEdit = et;

                        if ( position == 0 )
                        {
                            MyEmotionsUtil.getInstance().dismissPopupWindow();
                            main.visibleEmotionBar(false);
                        }
                        else
                        {
                            MyEmotionsUtil.getInstance().initView(et);
                            main.visibleEmotionBar(true);
                        }

                        MyEmotionsUtil.getInstance().onKeyDown(0, null);

                    }
                    return false;
                }
            });

//            et.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//
//                }
//            });


//            et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View view, boolean b) {
//                    if (b) {
//                        int position = (int) et.getTag();
////                        main.list.setSelection(position);
//
//                        Common.log("onFocusChange true"+position);
//                        if ( position == 0 )
//                        {
//                            MyEmotionsUtil.getInstance().dismissPopupWindow();
//                            main.visibleEmotionBar(false);
////                            view.requestFocus();
//                        }
//                        else
//                        {
//                            main.visibleEmotionBar(true);
////                            view.requestFocus();
//                        }
//                    }
//                    else
//                    {
//                        int position = (int) et.getTag();
//                        Common.log("onFocusChange false"+position);
//                    }
//                }
//            });

//            et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View v, boolean hasFocus) {
//                    if ( hasFocus )
//                    {
//                        Common.log("content has foucs = "+childModel.content);
//                        et.addTextChangedListener(tw);
//                    }
//                    else
//                    {
//                        Common.log("content no foucs = "+childModel.content);
//                        et.removeTextChangedListener(tw);
//
//                    }
//                }
//            });

        }


    }

    private CallBack callBack = new CallBack() {
        @Override
        public void handler(MyTask mTask) {
            if ("GONE".equals(mTask.getresult())) {
                main.addEmotion.setImageResource(R.drawable.emotion_face);
            } else {
                main.addEmotion.setImageResource(R.drawable.emotion_keybody);
            }
        }
    };




}
