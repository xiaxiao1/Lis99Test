package com.lis99.mobile.club.widget;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;

/**
 * Created by yy on 15/9/15.
 */
public class LSClubTopicHeadLike implements View.OnClickListener{

    private Context c;
//19人赞过
    private TextView tv_like;

    private RoundedImageView iv_like_0, iv_like_1, iv_like_2, iv_like_3, iv_like_4, iv_like_5, iv_like_6, iv_like_7, iv_like_8, iv_like_9;

    private ImageView vipStar_1, vipStar_2, vipStar_3, vipStar_4, vipStar_5, vipStar_6, vipStar_7, vipStar_8, iv_like;

    public LSClubTopicHeadLike(Context context)
    {
        // TODO Auto-generated constructor stub
        c = context;
    }

    public void InitView ( View v )
    {
        tv_like = (TextView) v.findViewById(R.id.tv_like);

        iv_like_0 = (RoundedImageView) v.findViewById(R.id.iv_like_0);
        iv_like_1 = (RoundedImageView) v.findViewById(R.id.iv_like_1);
        iv_like_2 = (RoundedImageView) v.findViewById(R.id.iv_like_2);
        iv_like_3 = (RoundedImageView) v.findViewById(R.id.iv_like_3);
        iv_like_4 = (RoundedImageView) v.findViewById(R.id.iv_like_4);
        iv_like_5 = (RoundedImageView) v.findViewById(R.id.iv_like_5);
        iv_like_6 = (RoundedImageView) v.findViewById(R.id.iv_like_6);
        iv_like_7 = (RoundedImageView) v.findViewById(R.id.iv_like_7);
        iv_like_8 = (RoundedImageView) v.findViewById(R.id.iv_like_8);
        iv_like_9 = (RoundedImageView) v.findViewById(R.id.iv_like_9);

        vipStar_1 = (ImageView) v.findViewById(R.id.vipStar_1);
        vipStar_2 = (ImageView) v.findViewById(R.id.vipStar_2);
        vipStar_3 = (ImageView) v.findViewById(R.id.vipStar_3);
        vipStar_4 = (ImageView) v.findViewById(R.id.vipStar_4);
        vipStar_5 = (ImageView) v.findViewById(R.id.vipStar_5);
        vipStar_6 = (ImageView) v.findViewById(R.id.vipStar_6);
        vipStar_7 = (ImageView) v.findViewById(R.id.vipStar_7);
        vipStar_8 = (ImageView) v.findViewById(R.id.vipStar_8);

        iv_like = (ImageView) v.findViewById(R.id.iv_like);


        iv_like_1.setVisibility(View.INVISIBLE);
        iv_like_2.setVisibility(View.INVISIBLE);
        iv_like_3.setVisibility(View.INVISIBLE);
        iv_like_4.setVisibility(View.INVISIBLE);
        iv_like_5.setVisibility(View.INVISIBLE);
        iv_like_6.setVisibility(View.INVISIBLE);
        iv_like_7.setVisibility(View.INVISIBLE);
        iv_like_8.setVisibility(View.INVISIBLE);
        iv_like_9.setVisibility(View.INVISIBLE);

        vipStar_1.setVisibility(View.GONE);
        vipStar_2.setVisibility(View.GONE);
        vipStar_3.setVisibility(View.GONE);
        vipStar_4.setVisibility(View.GONE);
        vipStar_5.setVisibility(View.GONE);
        vipStar_6.setVisibility(View.GONE);
        vipStar_7.setVisibility(View.GONE);
        vipStar_8.setVisibility(View.GONE);



    }

    public void setModel ()
    {

    }


    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.iv_like_0:

                break;
            case R.id.iv_like_1:
                break;
            case R.id.iv_like_2:
                break;
            case R.id.iv_like_3:
                break;
            case R.id.iv_like_4:
                break;
            case R.id.iv_like_5:
                break;
            case R.id.iv_like_6:
                break;
            case R.id.iv_like_7:
                break;
            case R.id.iv_like_8:
                break;
            case R.id.iv_like_9:
                break;
        }

    }

    private CallBack  callBack = new CallBack() {
        @Override
        public void handler(MyTask mTask) {

        }
    };

}
