package com.lis99.mobile.newhome.equip;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.LSBaseActivity;
import com.lis99.mobile.club.model.EquipInfoModel;
import com.lis99.mobile.club.model.KeyValueModel;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.equip.LSEquipCommentActivity;
import com.lis99.mobile.equip.LSRelatedShopActivity;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.LSRequestManager;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.ShareManager;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yy on 15/9/23.
 *
 *  id int   装备id
 *
 */
public class LSEquipInfoActivity extends LSBaseActivity implements
        PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener {

    private PullToRefreshView pull_refresh_view;

    private ListView list_info, list_reply;

    private ImageView iv_title, iv_load;

    private TextView tv_title, tv_price, tv_info, tv_shop, tv_reply, tv_like;

    private RatingBar ratingBar;

    private ReplayAdapter rAdapter;

    private PropertyAdapter pAdapter;
//装备id
    private int id;

    private EquipInfoModel model;

    private ImageView moredot;

    private View head;

    private LinearLayout layout_shop, layout_reply, layout_like;

    private ImageView iv_like, iv_shop;

    private View layoutMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ls_equip_info);

        id = Common.string2int(getIntent().getStringExtra("id"));

        if ( id == -1 )
        {
            id = getIntent().getIntExtra("id", 0);
        }

//        id = 8407;

        initViews();

        setTitle("装备详情");

        getlist();

    }

    @Override
    protected void initViews() {
        super.initViews();

        pull_refresh_view = (PullToRefreshView) findViewById(R.id.pull_refresh_view);

        pull_refresh_view.setOnHeaderRefreshListener(this);
        pull_refresh_view.setOnFooterRefreshListener(this);

        head = View.inflate(this, R.layout.ls_equip_info_head, null);

        list_info = (ListView) findViewById(R.id.list_info);

        list_info.addHeaderView(head);

        list_reply = (ListView) head.findViewById(R.id.list_reply);

        iv_title = (ImageView) head.findViewById(R.id.iv_title);

        iv_load = (ImageView) head.findViewById(R.id.iv_load);

        ratingBar = (RatingBar) head.findViewById(R.id.ratingBar);

        tv_title = (TextView) head.findViewById(R.id.tv_title);

        tv_price = (TextView) head.findViewById(R.id.tv_price);

        tv_info = (TextView) head.findViewById(R.id.tv_info);

        tv_shop = (TextView) findViewById(R.id.tv_shop);

        tv_reply = (TextView) findViewById(R.id.tv_reply);

        tv_like = (TextView) findViewById(R.id.tv_like);

        moredot = (ImageView) head.findViewById(R.id.moredot);

        layout_shop = (LinearLayout) findViewById(R.id.layout_shop);

        layout_reply = (LinearLayout) findViewById(R.id.layout_reply);

        layout_like = (LinearLayout) findViewById(R.id.layout_like);

        layoutMain = findViewById(R.id.layoutMain);

        iv_shop = (ImageView) findViewById(R.id.iv_shop);

        iv_like = (ImageView) findViewById(R.id.iv_like);


        tv_info.setOnClickListener( this );

        layout_shop.setOnClickListener(this);
        layout_like.setOnClickListener(this);
        layout_reply.setOnClickListener(this);

    }


    @Override
    protected void rightAction() {
        super.rightAction();

        if ( model == null || model.info == null ) return;

        ShareManager.getInstance().showPopWindowInShare(null, null, model.info.thumb, model.info.title, "", "", false, layoutMain, null, model.info.share_url);

    }

    @Override
    public void onClick(View arg0) {
        Intent i = null;
        switch ( arg0.getId())
        {
            case R.id.tv_info:
                moredot.setVisibility(View.GONE);
                tv_info.setMaxLines(Integer.MAX_VALUE);
                break;
            case R.id.layout_reply:
                i = new Intent(this, LSEquipCommentActivity.class);
                i.putExtra("equipID", id);
                startActivity(i);
                break;
            case R.id.layout_shop:
//                跳转店铺
                if ( model == null || model.info == null || model.info.is_buy == 0 ) return;
                i = new Intent(this, LSRelatedShopActivity.class);
                i.putExtra("brandID", Common.string2int(model.info.brand_id));
                startActivity(i);

                break;
            case R.id.layout_like:
                if ( model == null || model.info == null || model.info.likestatus == 1 ) return;
                if ( !Common.isLogin(activity) )
                {
                    return;
                }
                LSRequestManager.getInstance().equipLike(id, new CallBack() {
                    @Override
                    public void handler(MyTask mTask) {
                        iv_like.setImageResource(R.drawable.ls_equip_likeed);
                        if (model != null && model.info != null) {
                            model.info.likestatus = 1;
                        }
                    }
                });
                break;
        }

        super.onClick(arg0);
    }

    private void getlist ()
    {
//        if ( page.isLastPage() ) return;
        String url = C.EQUIP_INFO_LIST + id;

        HashMap<String, Object> map = new HashMap<String, Object>();

        String userId = DataManager.getInstance().getUser().getUser_id();

        if ( TextUtils.isEmpty(userId))
        {
            userId = "0";
        }

        map.put("user_id", userId);

        model = new EquipInfoModel();

        MyRequestManager.getInstance().requestPost(url, map, model, callBack);
//        MyRequestManager.getInstance().requestGet(url, model, callBack);


    }

    private void cleanlist ()
    {
//        page = new Page();
        list_reply.setAdapter(null);
        list_info.setAdapter(null);
        rAdapter = null;
        pAdapter = null;
    }


    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        pull_refresh_view.onFooterRefreshComplete();
//        getlist();
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        pull_refresh_view.onHeaderRefreshComplete();
        cleanlist();
        getlist();
    }





    // 设置title右边按钮
    private void setTitleRight(boolean isBg)
    {
        if (isBg)
        {
            setRightView(R.drawable.club_share);
        } else
        {
            setRightView(R.drawable.club_share_nul);
        }
    }

    // 设置返回按钮
    private void setBack(boolean isBg)
    {
        if (isBg)
        {
            setLeftView(R.drawable.ls_club_back_icon_bg);
        } else
        {
            setLeftView(R.drawable.ls_page_back_icon);
        }
    }

    CallBack callBack = new CallBack() {
        @Override
        public void handler(MyTask mTask) {
            model = (EquipInfoModel) mTask.getResultModel();

            if ( model == null ) return;
            if ( model.info == null )
            {
                return;
            }
            //透明
            setTitleBarAlpha(0);
            setTitleRight(true);
            setBack(true);

            ImageLoader.getInstance().displayImage(model.info.thumb, iv_title, ImageUtil.getDefultImageOptions(), ImageUtil.getImageLoading(iv_load, iv_title));

            tv_title.setText(model.info.title);

            if (TextUtils.isEmpty(model.info.market_price))
            {
                tv_price.setText("暂无");
            }
            else {
                tv_price.setText(model.info.market_price);
            }

            tv_info.setText(model.info.description);

            if ( model.totstart < 0 )
            {
                model.totstart = 0;
            }
            else if ( model.totstart > 5 )
            {
                model.totstart = 5;
            }

            ratingBar.setRating(model.totstart);
//是否有购买
            if ( model.info.is_buy == 0 )
            {
                iv_shop.setImageResource(R.drawable.ls_equip_shop_none);
                tv_shop.setText("暂无");
            }
            else {
                iv_shop.setImageResource(R.drawable.ls_equip_shop);
                tv_shop.setText("在哪买");
            }
//            喜欢
            if ( model.info.likestatus == 0 )
            {
                iv_like.setImageResource(R.drawable.ls_equip_like);
            }
            else {
                iv_like.setImageResource(R.drawable.ls_equip_likeed);
            }


            if (rAdapter == null )
            {

                rAdapter = new ReplayAdapter(activity, model.info.commenlist);
                rAdapter.setId(""+id);
                rAdapter.setHaveMore(model.info.totcomment > 3);

                list_reply.setAdapter(rAdapter);
            }
            if ( pAdapter == null )
            {
                ArrayList<Object> info = new ArrayList<Object>();
                KeyValueModel kv = null;
                if ( model.info.texture != null )
                {
                    if ( !TextUtils.isEmpty(model.info.texture.model) )
                    {
                        kv = new KeyValueModel();
                        kv.key = "型号";
                        kv.valule = model.info.texture.model;
                        info.add(kv);
                    }
                    if ( !TextUtils.isEmpty(model.info.texture.country_title) )
                    {
                        kv = new KeyValueModel();
                        kv.key = "品牌国家";
                        kv.valule = model.info.texture.country_title;
                        info.add(kv);
                    }
                    if ( !TextUtils.isEmpty(model.info.texture.textturename) )
                    {
                        kv = new KeyValueModel();
                        kv.key = "面料";
                        kv.valule = model.info.texture.textturename;
                        info.add(kv);
                    }
//                    if ( !TextUtils.isEmpty(model.info.texture.textturedescribe) )
//                    {
//                        kv = new KeyValueModel();
//                        kv.key = "品牌描述";
//                        kv.valule = model.info.texture.textturedescribe;
//                        info.add(kv);
//                    }
                    if ( !TextUtils.isEmpty(model.info.texture.weight) && Common.string2int(model.info.texture.weight) > 0 )
                    {
                        kv = new KeyValueModel();
                        kv.key = "重量";
                        kv.valule = model.info.texture.weight + "克";
                        info.add(kv);
                    }

                    int num = info.size();
                    if ( num > 0 )
                    {
                        kv = (KeyValueModel) info.get(num - 1);
                        kv.isLast = true;
                    }

                }



                if ( model.info.zhuangbeiimg != null )
                {
                    info.addAll(model.info.zhuangbeiimg);
                }
                pAdapter = new PropertyAdapter(activity, info);
                list_info.setAdapter(pAdapter);
            }

            list_info.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView absListView, int i) {

                }

                @Override
                public void onScroll(AbsListView absListView, int i, int i1, int i2) {

                    if ( i > 0 )
                    {
                        setTitleAlpha(headHeight);
                        return;
                    }

                    View v = list_info.getChildAt(0);
                    if (v == null)
                        return;
                    float alpha = v.getTop();

                    setTitleAlpha(-alpha);
                }
            });

        }
    };

    private float headHeight = Common.dip2px(200);

    /**
     * 设置标题栏透明度
     *
     * @param num
     */
    private void setTitleAlpha(float num)
    {
        if (num > headHeight)
        {
            num = headHeight;
        } else if (num < 0)
        {
            num = 0;
        }
        if (num < headHeight)
        {

            setTitleRight(true);
            setBack(true);
        } else
        {
            setTitleRight(false);
            setBack(false);
        }
        float alpha = num / headHeight;
        setTitleBarAlpha(alpha);
    }

}
