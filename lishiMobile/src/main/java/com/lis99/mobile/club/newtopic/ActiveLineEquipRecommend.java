package com.lis99.mobile.club.newtopic;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.model.EquipRecommendInterFace;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.newhome.equip.LSEquipInfoActivity;
import com.lis99.mobile.util.ImageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by yy on 16/3/21.
 */
public class ActiveLineEquipRecommend implements View.OnClickListener{

    private RelativeLayout layoutEquip;
    private RoundedImageView equiImageView;
    private TextView equiNameView;
    private RatingBar equiRatingBar;
    private RelativeLayout layoutEquip1;
    private RoundedImageView equiImageView1;
    private TextView equiNameView1;
    private RatingBar equiRatingBar1;
    private RelativeLayout layoutEquip2;
    private RoundedImageView equiImageView2;
    private TextView equiNameView2;
    private RatingBar equiRatingBar2;
    private Context mContext;

    private ArrayList<EquipRecommendInterFace> model;

    public ActiveLineEquipRecommend(Context context) {
        this.mContext = context;
    }

    public void init( View view )
    {
        layoutEquip = (RelativeLayout) view.findViewById(R.id.layout_equip);
        equiImageView = (RoundedImageView) view.findViewById(R.id.equiImageView);
        equiNameView = (TextView) view.findViewById(R.id.equiNameView);
        equiRatingBar = (RatingBar) view.findViewById(R.id.equiRatingBar);
        layoutEquip1 = (RelativeLayout) view.findViewById(R.id.layout_equip1);
        equiImageView1 = (RoundedImageView) view.findViewById(R.id.equiImageView1);
        equiNameView1 = (TextView) view.findViewById(R.id.equiNameView1);
        equiRatingBar1 = (RatingBar) view.findViewById(R.id.equiRatingBar1);
        layoutEquip2 = (RelativeLayout) view.findViewById(R.id.layout_equip2);
        equiImageView2 = (RoundedImageView) view.findViewById(R.id.equiImageView2);
        equiNameView2 = (TextView) view.findViewById(R.id.equiNameView2);
        equiRatingBar2 = (RatingBar) view.findViewById(R.id.equiRatingBar2);


        layoutEquip.setVisibility(View.GONE);
        layoutEquip1.setVisibility(View.GONE);
        layoutEquip2.setVisibility(View.GONE);

    }

    public void setModel ( ArrayList<EquipRecommendInterFace> model ) {

        if (model == null && model.size() == 0) {
            return;
        }

        this.model = model;

        int num = model.size();

        if (num > 0)
        {
            EquipRecommendInterFace in = model.get(0);
            layoutEquip.setVisibility(View.VISIBLE);
            if ( !TextUtils.isEmpty(in.getImgUrl()))
            {
                ImageLoader.getInstance().displayImage(in.getImgUrl(), equiImageView, ImageUtil
                        .getDefultImageOptions());
            }
            equiNameView.setText(in.getTitle());
            equiRatingBar.setRating(in.getStar());
        }
        if ( num > 1 )
        {
            EquipRecommendInterFace in = model.get(1);
            layoutEquip1.setVisibility(View.VISIBLE);
            if ( !TextUtils.isEmpty(in.getImgUrl()))
            {
                ImageLoader.getInstance().displayImage(in.getImgUrl(), equiImageView1, ImageUtil
                        .getDefultImageOptions());
            }
            equiNameView1.setText(in.getTitle());
            equiRatingBar1.setRating(in.getStar());
        }
        if ( num > 2 )
        {
            EquipRecommendInterFace in = model.get(2);
            layoutEquip2.setVisibility(View.VISIBLE);
            if ( !TextUtils.isEmpty(in.getImgUrl()))
            {
                ImageLoader.getInstance().displayImage(in.getImgUrl(), equiImageView2, ImageUtil
                        .getDefultImageOptions());
            }
            equiNameView2.setText(in.getTitle());
            equiRatingBar2.setRating(in.getStar());
        }



        layoutEquip.setOnClickListener(this);
        layoutEquip1.setOnClickListener(this);
        layoutEquip2.setOnClickListener(this);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if ( model == null ) return;
        String id = null;
        switch (v.getId())
        {
            case R.id.layout_equip:
                id = model.get(0).getId();
                break;
            case R.id.layout_equip1:
                id = model.get(1).getId();
                break;
            case R.id.layout_equip2:
                id = model.get(2).getId();
                break;
        }

        if ( TextUtils.isEmpty(id) )
        {
            return;
        }

        Intent intent = new Intent(mContext,LSEquipInfoActivity.class);
//        String id
        intent.putExtra("id", id);
        mContext.startActivity(intent);

    }
}
