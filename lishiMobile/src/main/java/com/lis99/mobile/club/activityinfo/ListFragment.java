package com.lis99.mobile.club.activityinfo;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.LSClubDetailActivity;
import com.lis99.mobile.club.LSClubTopicInfoLocation;
import com.lis99.mobile.club.destination.DestinationActivity;
import com.lis99.mobile.club.model.ClubTopicActiveSeriesLineMainModel;
import com.lis99.mobile.club.newtopic.LSClubTopicInfoAdapter;
import com.lis99.mobile.club.widget.BannerView;
import com.lis99.mobile.club.widget.ImagePageAdapter;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.newhome.equip.LSEquipInfoActivity;
import com.lis99.mobile.util.ActivityUtil;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.LocationUtil;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.NativeEntityUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ListFragment extends BaseFragment implements ImagePageAdapter.ImagePageAdapterListener, ImagePageAdapter.ImagePageClickListener{


    //领队信息view
    View footer_ownerinfo;
    //玩家评论view
    View footer_playerEvaluation;
    //推荐装备View
    View footer_zhuangbei;

    //下拉刷新
    RefreshListview listView;
    //listview头部
    View header;
    //顶部轮播
    BannerView bannerView;
    //顶部标签串
    RecyclerView mRecyclerView;
    //百度地图是否显示区域
    RelativeLayout baiduMap_rl;
    //百度地图
    MapView mMapView = null;
    //跳转到全屏地图
    ImageView toBigMap_img;
    //目的地名称
    TextView destinationName_tv;
    View map_view;
    //没有地图时
    ImageView noMap_img;
    //活动标题区域
    LinearLayout title_area_ll;
    //活动标题
    TextView activeTitle_tv;
    //活动说明
    TextView activeNote_tv;
    //活动价格
    TextView activePrice_tv;
    TextView Qi_tv;
    //活动亮点区域
    LinearLayout lightspots_ll;

    //领队区域
    RelativeLayout leaderArea_rl;
    //领队view分割线
//    LinearLayout leader_fengexian_ll;
    //领队头像
    RoundedImageView leaderHead_img;
    //领队名字
    TextView leaderName_tv;
    //领队标签区域
    LinearLayout leaderlabels_ll;
    //领队标签1
    TextView leaderlabel1_tv;
    //两队标签2
    TextView leaderlabel2_tv;
    //领队标签3
    TextView leaderlabel3_tv;
    TextView labels[]=new TextView[3];
    //两队简介
    TextView leaderIntroduce_tv;
    //领队来自的俱乐部
    TextView leaderFrom_tv;

    //玩家评论区域
    LinearLayout player_pinglun1_ll;
    LinearLayout player_pinglun2_ll;
    LinearLayout player_pinglun3_ll;
    //玩家评论view分割线
    //  LinearLayout player_fengexian_ll;

    /*//玩家评论view 动态是否显示
    View  playerEvaluations_view;
    //玩家头像
    RoundedImageView playerHeader_img;
    //玩家姓名
    TextView playerName_tv;
    //玩家标签1
    TextView playerLabel1_tv;
    //玩家标签2
    TextView palyerLabel2_tv;
    //玩家标签3
    TextView playerLabel3_tv;
    //玩家玩过的时间
    TextView playedTime_tv;
    //玩家的评论
    TextView playerEvaluation_tv;
    //玩家的等级
    RatingBar playerLevel;
    //推荐装备view  动态是否显示
    View equip_view;
    //装备图片
    ImageView  equipPic_img;
    //装备介绍
    TextView equipIntroduce_tv;
    //装备等级
    RatingBar equipLevel;*/

    //装备区域
    LinearLayout zhuangbei1_ll;
    LinearLayout zhuangbei2_ll;
    LinearLayout zhuangbei3_ll;
    //底部上拉详情条
    View footView;





    ImagePageAdapter bannerAdapter;
    FullInfoAdapter fullInfoAdapter;
    MyRecyclerAdapter recyclerviewAdapter;
    LSClubTopicInfoAdapter highlightsAdapter;



    private SlideDetailsLayout mSlideDetailsLayout;

    int activity_id;
    int clubID;
    int leaderId;
    ClubTopicActiveSeriesLineMainModel model;
    BaiduMap mBaiduMap = null;
    //活动详情图文数据
    List<FullInfo> activeInfos;

    //标签串数据
    List<ClubTopicActiveSeriesLineMainModel.TagEntity> recycler_datas;
    //地图定位的坐标
    private double longitude = 0.0000000;
    private double latitude = 0.0000000;
    //活动详情中当前要显示的item数目
    int currentSize;
    //活动详情一共多少item数目
    int fullSize;
    //存放动态加载的亮点views
    List<View> lightSpots_list=new ArrayList<View>();


    //回调赋值给Activity
    FullInfoInterface fullInfoInterface;

    //回调给activity执行title透明度操作
    AlphaInterface alphaInterface;

    public ListFragment() {

    }
    public void setAlphaInterface(AlphaInterface alphaInterface) {
        this.alphaInterface = alphaInterface;
    }
    public void setmSlideDetailsLayout(SlideDetailsLayout mSlideDetailsLayout) {
        this.mSlideDetailsLayout = mSlideDetailsLayout;
    }

    public FullInfoInterface getFullInfoInterface() {
        return fullInfoInterface;
    }

    public void setFullInfoInterface(FullInfoInterface fullInfoInterface) {
        this.fullInfoInterface = fullInfoInterface;
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //初始化百度地图
        SDKInitializer.initialize(ListFragment.this.getActivity().getApplicationContext());
        return inflater.inflate(R.layout.activityinfo_layout_listview, null);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        activity_id=this.getActivity().getIntent().getIntExtra("topicID",0);
        getInfo(activity_id);


        //下拉刷新
        listView.setonRefreshListener(new RefreshListview.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                cleanInfo();
                getInfo(activity_id);
                listView.onRefreshComplete();

            }
        });
        listView.setAlphaInterface(alphaInterface);
        listView.setAdapter(fullInfoAdapter);

        //   initBaiduMap();








    }

    @Override
    public void dispalyImage(ImageView banner, ImageView iv_load, int position) {
        if (model!=null) {

            if (model.activityimgs==null||model.activityimgs.size() == 0)
                return;

            ImageLoader.getInstance().displayImage(model.activityimgs.get(position).images, banner,
                    ImageUtil.getclub_topic_imageOptions(), ImageUtil.getImageLoading(iv_load, banner));
        }
    }

    //轮播图片的点击事件，这里不用
    @Override
    public void onClick(int index) {

    }

    public void openInfo(boolean smooth){
        open(smooth);
    }

    public void closeInfo(boolean smooth) {
        close(smooth);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        try
        {
            mBaiduMap.setMyLocationEnabled(false);
            mMapView.onDestroy();
            mMapView = null;
        }
        catch (Exception e)
        {}
    }
    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    public void initBaiduMap(){



        LatLng latlng = new LatLng(latitude, longitude);
        latlng = LocationUtil.getinstance().gaode2baidu(latlng);
        mBaiduMap.clear();
        mBaiduMap.addOverlay(new MarkerOptions().position(latlng)
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.icon_gcoding)));

        MapStatus mMapStatus = new MapStatus.Builder()
                .target(latlng)
                .zoom(11)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
        toBigMap_img=(ImageView)header.findViewById(R.id.afullinfo_header_biggermap_img);
        toBigMap_img.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View v) {
                //跳转地图
                Intent intent = new Intent(ListFragment.this.getActivity(), LSClubTopicInfoLocation.class);

               /* Double latitude2 =latitude;
                Double longtitude2 = Common.string2Double(laty2);*/
                if ( latitude == -1 || longitude == -1 )
                {
                    Common.toast("暂时没集合地图位置");
                    return;
                }
                intent.putExtra("latitude", latitude);
                intent.putExtra("longtitude", longitude);
                startActivity(intent);
            }
        });
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mMapView.onResume();


    }

    /**
     * 通过回调向Activity传值，更新数据
     * @param datas 传递给Activity的数据
     */
    public void updateActivity(Object datas){
        if (fullInfoInterface!=null) {
            fullInfoInterface.initFullInfo(datas);
        }
    }

    /**
     * 图文详情实体类 测试用
     */
    class FullInfo{
        String content;
        String imgUrl;

        public FullInfo(String content, String imgUrl) {
            this.content=content;
            this.imgUrl=imgUrl;
        }
        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    /**
     * 活动详情图文adapter
     */
    private class FullInfoAdapter extends BaseAdapter implements View.OnClickListener{

        private List<ClubTopicActiveSeriesLineMainModel.ActivitydetailEntity> datas;
        Holder viewHolder = null;

        FullInfoAdapter(List<ClubTopicActiveSeriesLineMainModel.ActivitydetailEntity> datas) {
            this.datas = datas;
        }

        @Override
        public int getCount() {
            return null == datas ? 0 : currentSize;
        }

        @Override
        public ClubTopicActiveSeriesLineMainModel.ActivitydetailEntity getItem(int position) {
            return null == datas ? null : datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ClubTopicActiveSeriesLineMainModel.ActivitydetailEntity entity=datas.get(position);
            if (null == convertView) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.activityinfo_layout_list_item, null);
                viewHolder=new Holder();
                viewHolder.content = (TextView) convertView.findViewById(R.id.fullinfo_item_tv);
                viewHolder.img = (ImageView) convertView.findViewById(R.id.fullinfo_item_img);
                viewHolder.load_img = (ImageView) convertView.findViewById(R.id.fullinfo_item_load_img);
                viewHolder.showMore_rl = (RelativeLayout) convertView.findViewById(R.id
                        .fullinfo_item_show_more_rl);
                viewHolder.showMore_tv = (TextView) convertView.findViewById(R.id
                        .fullinfo_item_showmore_tv);
                convertView.setTag(viewHolder);
            }
            else
            {
                viewHolder = (Holder) convertView.getTag();
            }
            String c=entity.content;
            String img=entity.images;
            viewHolder.img.setVisibility(View.GONE);
            viewHolder.load_img.setVisibility(View.GONE);
            viewHolder.content.setVisibility(View.GONE);
            viewHolder.showMore_rl.setVisibility(View.GONE);
            if (c!=null&&!c.equals("")) {

                viewHolder.content.setVisibility(View.VISIBLE);
                viewHolder.content.setText(c);

            }
            if (img!=null&!img.equals("")) {

                viewHolder.img.setVisibility(View.VISIBLE);
                viewHolder.load_img.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(img, viewHolder.img,
                        ImageUtil.getclub_topic_imageOptions(), ImageUtil.getImageLoading(viewHolder.load_img, viewHolder
                                .img ));
            }

            if(currentSize!=fullSize&&position+1==currentSize){
                viewHolder.showMore_rl.setVisibility(View.VISIBLE);
                viewHolder.showMore_tv.setOnClickListener(this);
            }

            return convertView;
        }

        @Override
        public void onClick(View v) {
//            currentSize=currentSize+3<fullSize?currentSize+3:fullSize;
            currentSize=fullSize;
            viewHolder.showMore_rl.setVisibility(View.GONE);
            FullInfoAdapter.this.notifyDataSetChanged();
        }

        class Holder{
            TextView content;
            ImageView img;
            ImageView load_img;
            RelativeLayout showMore_rl;
            TextView showMore_tv;
        }
    }


    /**
     * recyclerView的adapter
     */
    class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder>
    {

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {

            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    ListFragment.this.getActivity()).inflate(R.layout.activityinfo_recyclerview_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position)
        {
            String tagName=recycler_datas.get(position).name;
            if (tagName.length() > 7) {
                holder.tv.setText("    "+tagName.substring(0,7) + "..." + "    ");
            } else {
                holder.tv.setText("    "+tagName+"    ");
            }
            holder.tv.setOnClickListener(new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                @Override
                public void onClick(View v) {
                    ActivityUtil.goSpecialInfoActivity(ListFragment.this.getActivity(), recycler_datas.get(position).id);

                }
            });
        }

        @Override
        public int getItemCount()
        {
            return recycler_datas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder
        {

            TextView tv;

            public MyViewHolder(View view)
            {
                super(view);
                tv = (TextView) view.findViewById(R.id.afullinfo_recycler_item_tv);
            }
        }
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void initViews(View view){
        listView = (RefreshListview) view.findViewById(R.id.list);
        header=getActivity().getLayoutInflater().inflate(R.layout.activityinfo_header,null);
        listView.addHeaderView(header);
        header.setVisibility(View.INVISIBLE);
        title_area_ll=(LinearLayout)header.findViewById(R.id.ownerinfo_huodong_title_ll);
        title_area_ll.setVisibility(View.INVISIBLE);
        footer_ownerinfo=getActivity().getLayoutInflater().inflate(R.layout.activityinfo_footer_4ownerinfo,null);
        footer_playerEvaluation=getActivity().getLayoutInflater().inflate(R.layout.activityinfo_footer_4player_evaluation,null);
        footer_zhuangbei=getActivity().getLayoutInflater().inflate(R.layout.activityinfo_footer_4zhuangbei,null);
        listView.addFooterView(footer_ownerinfo);
        footer_ownerinfo.setVisibility(View.INVISIBLE);
//        listView.addFooterView(footer_playerEvaluation);
//        listView.addFooterView(footer_zhuangbei);
        footView = getActivity().getLayoutInflater()
                .inflate(R.layout.activityinfo_slidedetails_marker_default_layout, null);
        footView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlideDetailsLayout.smoothOpen(true);
                //    mSlideDetailsLayout.close=false;
            }
        });
        //   listView.addFooterView(footView);


        //正式开始
        activeTitle_tv = (TextView) header.findViewById(R.id.afullinfo_active_title_tv);
        activeNote_tv = (TextView) header.findViewById(R.id.afullinfo_active_note_tv);
        activePrice_tv = (TextView) header.findViewById(R.id.afullinfo_active_nowprice_tv);

        //顶部轮播图片
        bannerView=(BannerView)header.findViewById(R.id.afullinfo_lv_header_banner_banner);
        bannerView.mViewPager.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                v.getParent().requestDisallowInterceptTouchEvent(true);

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        bannerView.stopAutoScroll();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        bannerView.stopAutoScroll();
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        bannerView.startAutoScroll();
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });

        //横向可滑动标签串
        mRecyclerView=(RecyclerView)header.findViewById(R.id.afullinfo_recyclerview);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));

        //活动亮点区域
        lightspots_ll = (LinearLayout) header.findViewById(R.id.afullinfo_header_add_liangdian_ll);

        //百度地图区域
        mMapView = (MapView) header.findViewById(R.id.afullinfo_header_bmapView);
        map_view = (View) header.findViewById(R.id.afullinfo_header_mapclick);
        mMapView.showZoomControls(false);
        mMapView.showScaleControl(false);

        mBaiduMap = mMapView.getMap();
        mBaiduMap.getUiSettings().setRotateGesturesEnabled(false);
        mBaiduMap.getUiSettings().setAllGesturesEnabled(false);
        baiduMap_rl=(RelativeLayout)header.findViewById(R.id.afullinfo_header_baidumap_rl);
        noMap_img=(ImageView)header.findViewById(R.id.afullinfo_header_nomap_img);
        destinationName_tv=(TextView)header.findViewById(R.id.afullinfo_header_destination_name_tv);
        //领队区域
        leaderHead_img=(RoundedImageView)footer_ownerinfo.findViewById(R.id.footer4openmore_ownerhead_img);
        leaderName_tv = (TextView) footer_ownerinfo.findViewById(R.id.footer4openmore_ownername_tv);
        leaderIntroduce_tv=(TextView)footer_ownerinfo.findViewById(R.id.footer4openmore_ownerintroduce_tv);
        leaderFrom_tv=(TextView)footer_ownerinfo.findViewById(R.id.footer4openmore_from_club_tv);
        leaderFrom_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ListFragment.this.getActivity(), LSClubDetailActivity.class);
                i.putExtra("clubID", clubID);
                startActivity(i);

            }
        });
        leaderlabel1_tv=(TextView)footer_ownerinfo.findViewById(R.id.footer4openmore_ownerlabel1_tv);
        leaderlabel2_tv=(TextView)footer_ownerinfo.findViewById(R.id.footer4openmore_ownerlabel2_tv);
        leaderlabel3_tv=(TextView)footer_ownerinfo.findViewById(R.id.footer4openmore_ownerlabel3_tv);
        labels[0]=leaderlabel1_tv;
        labels[1]=leaderlabel2_tv;
        labels[2]=leaderlabel3_tv;
//        leader_fengexian_ll=(LinearLayout)footer_ownerinfo.findViewById(R.id.afullinfo_leader_fengexian_ll);
        leaderArea_rl=(RelativeLayout)footer_ownerinfo.findViewById(R.id.footer4openmore_ownerarea_rl);
        //点击领队区域跳转
        leaderArea_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (leaderId>0) {
                    //如果领队id不存在，直接去他所在的俱乐部
                    if ( TextUtils.isEmpty(""+model.leaderUserid) || "0".equals(model.leaderUserid) )
                    {
                        Intent i = new Intent(ListFragment.this.getActivity(), LSClubDetailActivity.class);
                        i.putExtra("clubID", clubID);
                        startActivity(i);
                    }
                    else
                    {
                        //去领队个人主页
                        Common.goUserHomeActivit(ListFragment.this.getActivity(), ""+model.leaderUserid);
                    }
                }
            }
        });
        leaderlabels_ll=(LinearLayout)footer_ownerinfo.findViewById(R.id.footer4openmore_labels_ll);

        //玩家分割线
//        player_fengexian_ll=(LinearLayout)footer_playerEvaluation.findViewById(R.id.afullinfo_player_fengexian_ll);

        player_pinglun1_ll=(LinearLayout)footer_playerEvaluation.findViewById(R.id.afullinfo_players_pinglun1_ll);
        player_pinglun2_ll=(LinearLayout)footer_playerEvaluation.findViewById(R.id.afullinfo_players_pinglun2_ll);
        player_pinglun3_ll=(LinearLayout)footer_playerEvaluation.findViewById(R.id.afullinfo_players_pinglun3_ll);
        //装备
        zhuangbei1_ll=(LinearLayout)footer_zhuangbei.findViewById(R.id.footer4zhuangbei_ll_1);
        zhuangbei2_ll=(LinearLayout)footer_zhuangbei.findViewById(R.id.footer4zhuangbei_ll_2);
        zhuangbei3_ll=(LinearLayout)footer_zhuangbei.findViewById(R.id.footer4zhuangbei_ll_3);


    }

    /**
     * 根据传入的id 重新获取活动详情数据
     * @param activityId 活动id
     */
    public void refreshDatas(int activityId) {
        if (activityId!=this.activity_id) {
            this.activity_id=activityId;
            cleanInfo();
            getInfo(activityId);
        }
    }
    private void getInfo(int activityId) {
        String url = C.CLUB_TOPIC_ACTIVE_SERIES_LINE_MIAN;

        String userId = DataManager.getInstance().getUser().getUser_id();

        HashMap<String, Object> map = new HashMap<String, Object>();

        map.put("user_id", userId);
        map.put("activity_id", activityId);
        /*map.put("user_id", 456957);
        map.put("activity_id", 4829);*/

        model = new ClubTopicActiveSeriesLineMainModel();

        MyRequestManager.getInstance().requestPost(url, map, model, new CallBack() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void handler(MyTask mTask) {
                model = (ClubTopicActiveSeriesLineMainModel) mTask.getResultModel();

                if (model == null) {
                    listView.addFooterView(footView);
                    return;
                }

                clubID = model.clubId;
                header.setVisibility(View.VISIBLE);
                //设置活动标题
                activeTitle_tv.setText(model.getTitle());
                //设置活动时间区间，批次数量
                if (!model.starttimeIntval.equals("")) {

                    activeNote_tv.setText(model.starttimeIntval+"  共"+model.batchCount+"期");
                    //设置价格区间
                    activePrice_tv.setText(model.priceIntval);
                }
                //设置顶部轮播图
                if (model.activityimgs != null && model.activityimgs.size() != 0 ) {
                    bannerView.setVisibility(View.VISIBLE);

                    bannerAdapter=new ImagePageAdapter(getActivity(), model.activityimgs.size());
                    bannerAdapter.addImagePageAdapterListener(ListFragment.this);
                    bannerAdapter.setImagePageClickListener(ListFragment.this);
                    bannerView.setBannerAdapter(bannerAdapter);
                    bannerView.startAutoScroll();
                }
                else {
                    bannerView.setVisibility(View.GONE);
                }

                //设置标签串
                if (model.taglist!=null&&model.taglist.size()!=0) {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    recycler_datas=model.taglist;
                    recyclerviewAdapter = new MyRecyclerAdapter();
                    mRecyclerView.setAdapter(recyclerviewAdapter);
                }else{
                    mRecyclerView.setVisibility(View.GONE);
                }

                //设置活动亮点
                if (model.activelightspot != null && model.activelightspot.size() != 0) {
                    lightspots_ll.setVisibility(View.VISIBLE);
                    addLightSpots(model.activelightspot);
                } else {
                    lightspots_ll.setVisibility(View.GONE);
                }

                //设置百度地图
                if (model.desti_id != null && !model.desti_id.equals("0")) {
                    //如果关联了目的地并且有坐标
                    if (!model.aimlongitude.equals("0") && !model.aimlatitude.equals("0")) {
                        longitude = Double.parseDouble(model.aimlongitude);
                        latitude = Double.parseDouble(model.aimlatitude);
                        if (longitude != -1 && latitude != -1) {
                            //跳转到目的地页
                            map_view.setOnClickListener(new View.OnClickListener() {
                                @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                                @Override
                                public void onClick(View v) {
                                    //去往目的地详情页
                                    goDestinationInfo(Integer.parseInt(model.aimid),Integer.parseInt(model.desti_id));
                                }
                            });
                            baiduMap_rl.setVisibility(View.VISIBLE);
                            noMap_img.setVisibility(View.GONE);
                            initBaiduMap();
                            destinationName_tv.setText(model.tagname);
                        }

                    }
                    //关联了目的地 但是没有坐标
                    else {
                        baiduMap_rl.setVisibility(View.GONE);
                        noMap_img.setVisibility(View.VISIBLE);
                        //显示默认图时，也跳转到目的地页
                        noMap_img.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //去往目的地详情页
                                goDestinationInfo(Integer.parseInt(model.desti_id),Integer.parseInt(model.aimid));
                            }
                        });
                    }

                }
                //没有关联目的地
                else {
                    mMapView.setVisibility(View.GONE);
                    map_view.setVisibility(View.GONE);
                    noMap_img.setVisibility(View.GONE);
                }

                //活动图文
                if (model.activitydetail!=null&&model.activitydetail.size()>0) {
                    //初始化活动详情的图文信息
                    title_area_ll.setVisibility(View.VISIBLE);
                    if (fullInfoAdapter==null) {
                        fullInfoAdapter=new FullInfoAdapter(model.activitydetail);
                    }
                    fullSize=model.activitydetail.size();
                    currentSize=fullSize>3?3:fullSize;
                    listView.setAdapter(fullInfoAdapter);
                }

                //设置leader头像leaderHead_img,ImageUtil.getclub_topic_headImageOptions()
                leaderId=model.leaderUserid;
                footer_ownerinfo.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(model.leaderheadicon)) {
                    ImageLoader.getInstance().displayImage(model.leaderheadicon,leaderHead_img , ImageUtil.getclub_topic_headImageOptions());
                }
                //设置leader名字
                if (!TextUtils.isEmpty(model.leadernickname)) {
                    leaderName_tv.setText(model.leadernickname);
                }
                //设置leader简介
                if (!TextUtils.isEmpty(model.leadernote)) {
                    leaderIntroduce_tv.setText(model.leadernote);
                }
                //设置leader俱乐部
                if (!TextUtils.isEmpty(model.clubTitle)) {
                    leaderFrom_tv.setText(model.clubTitle);
                }
                //设置leader标签
                if (model.leaderdesc!=null&&model.leaderdesc.size()!=0) {
                    leaderlabels_ll.setVisibility(View.VISIBLE);
                    int labelSize=model.leaderdesc.size();
                    if (labelSize>3) {
                        labelSize=3;
                    }
                    for(int i=0;i<labelSize;i++){
                        String s_label=model.leaderdesc.get(i);
                        labels[i].setVisibility(View.VISIBLE);
                        labels[i].setText(s_label);
                        if (NativeEntityUtil.getInstance().getCommunityStarTags().get(s_label) != null) {
                            labels[i].setBackgroundResource(NativeEntityUtil.getInstance()
                                    .getCommunityStarTags().get(s_label));
                        }
                    }
                }else{
                    leaderlabels_ll.setVisibility(View.GONE);
                }

                //设置玩家评论


                /*打开推荐玩家评价，2016.9.13*/
                /*if(model.commentlist!=null){

                    model.commentlist=null;
                }*/
                if (model.commentlist != null && model.commentlist.size() != 0) {
//                    leader_fengexian_ll.setVisibility(View.VISIBLE);
                    footer_playerEvaluation.setVisibility(View.VISIBLE);
                    listView.addFooterView(footer_playerEvaluation);
                    int size = model.commentlist.size();
                    //第一条评论
                    if (size > 0) {
                        player_pinglun1_ll.setVisibility(View.VISIBLE);
                        RoundedImageView playerHead1=(RoundedImageView)footer_playerEvaluation.findViewById(R.id.footer4playerevaluation_player_head1_img);
                        ImageLoader.getInstance().displayImage(model.commentlist.get(0).userhead, playerHead1, ImageUtil.getclub_topic_headImageOptions());
                        TextView playerName1=(TextView)footer_playerEvaluation.findViewById(R.id.footer4playerevaluation_player_name1_tv);
                        playerName1.setText(model.commentlist.get(0).nickname);
                        RatingBar ratBar1=(RatingBar)footer_playerEvaluation.findViewById(R.id.footer4playerevaluation_player_level1_img);
                        ratBar1.setRating(Float.parseFloat(model.commentlist.get(0).star));
                        TextView playTime1=(TextView)footer_playerEvaluation.findViewById(R.id.footer4playerevaluation_play_time1_tv);
                        playTime1.setText(model.commentlist.get(0).createtime+"玩过");
                        TextView playComment1=(TextView)footer_playerEvaluation.findViewById(R.id.footer4playerevaluation_player_evaluation1_tv);
                        playComment1.setText(model.commentlist.get(0).custom);
                        TextView playLabels1[]={(TextView)footer_playerEvaluation.findViewById(R.id.footer4playerevaluation_player_label_11_tv),(TextView)footer_playerEvaluation.findViewById(R.id.footer4playerevaluation_player_label_12_tv),(TextView)footer_playerEvaluation.findViewById(R.id.footer4playerevaluation_player_label_13_tv)};
                        if (model.commentlist.get(0).usercatelist!=null&&model.commentlist.get(0).usercatelist.size()!=0) {

                            for(int i=0;i<model.commentlist.get(0).usercatelist.size();i++){
                                String s_label=model.commentlist.get(0).usercatelist.get(i).title;
                                playLabels1[i].setVisibility(View.VISIBLE);
                                playLabels1[i].setText(s_label);
                                if (NativeEntityUtil.getInstance().getCommunityStarTags().get(s_label) != null) {
                                    playLabels1[i].setBackgroundResource(NativeEntityUtil.getInstance()
                                            .getCommunityStarTags().get(s_label));
                                }
                            }
                        }else{
                            //    leaderlabels_ll.setVisibility(View.GONE);
                        }
                    }else{
                        player_pinglun1_ll.setVisibility(View.GONE);
                    }
                    //第二条评论
                    if (size > 1) {
                        player_pinglun2_ll.setVisibility(View.VISIBLE);
                        RoundedImageView playerHead2=(RoundedImageView)footer_playerEvaluation.findViewById(R.id.footer4playerevaluation_player_head2_img);
                        ImageLoader.getInstance().displayImage(model.commentlist.get(1).userhead, playerHead2, ImageUtil.getclub_topic_headImageOptions());
                        TextView playerName2=(TextView)footer_playerEvaluation.findViewById(R.id.footer4playerevaluation_player_name2_tv);
                        playerName2.setText(model.commentlist.get(1).nickname);
                        RatingBar ratBar2=(RatingBar)footer_playerEvaluation.findViewById(R.id.footer4playerevaluation_player_level2_img);
                        ratBar2.setRating(Float.parseFloat(model.commentlist.get(1).star));
                        TextView playTime2=(TextView)footer_playerEvaluation.findViewById(R.id.footer4playerevaluation_play_time2_tv);
                        playTime2.setText(model.commentlist.get(1).createtime+"玩过");
                        TextView playComment2=(TextView)footer_playerEvaluation.findViewById(R.id.footer4playerevaluation_player_evaluation2_tv);
                        playComment2.setText(model.commentlist.get(1).custom);
                        TextView playLabels2[]={(TextView)footer_playerEvaluation.findViewById(R.id.footer4playerevaluation_player_label_21_tv),(TextView)footer_playerEvaluation.findViewById(R.id.footer4playerevaluation_player_label_22_tv),(TextView)footer_playerEvaluation.findViewById(R.id.footer4playerevaluation_player_label_23_tv)};
                        if (model.commentlist.get(1).usercatelist!=null&&model.commentlist.get(1).usercatelist.size()!=0) {

                            for(int i=0;i<model.commentlist.get(1).usercatelist.size();i++){
                                String s_label=model.commentlist.get(1).usercatelist.get(i).title;
                                playLabels2[i].setVisibility(View.VISIBLE);
                                playLabels2[i].setText(s_label);
                                if (NativeEntityUtil.getInstance().getCommunityStarTags().get(s_label) != null) {
                                    playLabels2[i].setBackgroundResource(NativeEntityUtil.getInstance()
                                            .getCommunityStarTags().get(s_label));
                                }
                            }
                        }else{
                            //    leaderlabels_ll.setVisibility(View.GONE);
                        }
                    }else{
                        player_pinglun2_ll.setVisibility(View.GONE);
                    }
                    //第三条评论
                    if (size > 2) {
                        player_pinglun3_ll.setVisibility(View.VISIBLE);
                        RoundedImageView playerHead3=(RoundedImageView)footer_playerEvaluation.findViewById(R.id.footer4playerevaluation_player_head3_img);
                        ImageLoader.getInstance().displayImage(model.commentlist.get(2).userhead, playerHead3, ImageUtil.getclub_topic_headImageOptions());
                        TextView playerName3=(TextView)footer_playerEvaluation.findViewById(R.id.footer4playerevaluation_player_name3_tv);
                        playerName3.setText(model.commentlist.get(2).nickname);
                        RatingBar ratBar3=(RatingBar)footer_playerEvaluation.findViewById(R.id.footer4playerevaluation_player_level3_img);
                        ratBar3.setRating(Float.parseFloat(model.commentlist.get(2).star));
                        TextView playTime3=(TextView)footer_playerEvaluation.findViewById(R.id.footer4playerevaluation_play_time3_tv);
                        playTime3.setText(model.commentlist.get(2).createtime+"玩过");
                        TextView playComment3=(TextView)footer_playerEvaluation.findViewById(R.id.footer4playerevaluation_player_evaluation3_tv);
                        playComment3.setText(model.commentlist.get(2).custom);
                        TextView playLabels3[]={(TextView)footer_playerEvaluation.findViewById(R.id.footer4playerevaluation_player_label_31_tv),(TextView)footer_playerEvaluation.findViewById(R.id.footer4playerevaluation_player_label_32_tv),(TextView)footer_playerEvaluation.findViewById(R.id.footer4playerevaluation_player_label_33_tv)};
                        if (model.commentlist.get(1).usercatelist!=null&&model.commentlist.get(1).usercatelist.size()!=0) {

                            for(int i=0;i<model.commentlist.get(2).usercatelist.size();i++){
                                String s_label=model.commentlist.get(2).usercatelist.get(i).title;
                                playLabels3[i].setVisibility(View.VISIBLE);
                                playLabels3[i].setText(s_label);
                                if (NativeEntityUtil.getInstance().getCommunityStarTags().get(s_label) != null) {
                                    playLabels3[i].setBackgroundResource(NativeEntityUtil.getInstance()
                                            .getCommunityStarTags().get(s_label));
                                }
                            }
                        }else{
                            listView.removeFooterView(footer_playerEvaluation);
                        }
                    }else{
                        player_pinglun3_ll.setVisibility(View.GONE);
                    }

                } else {
                    footer_playerEvaluation.setVisibility(View.GONE);
//                    leader_fengexian_ll.setVisibility(View.GONE);
                }

                //设置推荐装备
                if (model.zhuangbeilist != null && model.zhuangbeilist.size() > 0) {
                    listView.addFooterView(footer_zhuangbei);
//                    player_fengexian_ll.setVisibility(View.VISIBLE);
                    int size = model.zhuangbeilist.size();
                    //第一个装备
                    if (size > 0) {
                        ImageView zhuangbei_img1 = (ImageView) footer_zhuangbei.findViewById(R.id
                                .footer4zhuangbei_zhuangbei1_img);
                        ImageLoader.getInstance().displayImage(model.zhuangbeilist.get(0)
                                .zhuangbei_image, zhuangbei_img1, ImageUtil
                                .getclub_topic_headImageOptions());
                        TextView zhuangbei_miaoshu1 = (TextView) footer_zhuangbei.findViewById(R
                                .id.footer4zhuangbei_introduce1_tv);
                        zhuangbei_miaoshu1.setText(model.zhuangbeilist.get(0).zhuangbei_title);
                        RatingBar zhaungbei_level1 = (RatingBar) footer_zhuangbei.findViewById(R
                                .id.footer4zhuangbei_level1_ratingbar);
                        zhaungbei_level1.setRating(model.zhuangbeilist.get(0).zhuangbei_star);
                        zhuangbei1_ll.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //跳转到装备页
                                toEquipInfo(model.zhuangbeilist.get(0).getId());
                            }
                        });
                        zhuangbei1_ll.setVisibility(View.VISIBLE);
                    }
                    //第二个装备
                    if (size > 1) {
                        ImageView zhuangbei_img2 = (ImageView) footer_zhuangbei.findViewById(R.id
                                .footer4zhuangbei_zhuangbei2_img);
                        ImageLoader.getInstance().displayImage(model.zhuangbeilist.get(1)
                                .zhuangbei_image, zhuangbei_img2, ImageUtil
                                .getclub_topic_headImageOptions());
                        TextView zhuangbei_miaoshu2 = (TextView) footer_zhuangbei.findViewById(R
                                .id.footer4zhuangbei_introduce2_tv);
                        zhuangbei_miaoshu2.setText(model.zhuangbeilist.get(1).zhuangbei_title);
                        RatingBar zhaungbei_level2 = (RatingBar) footer_zhuangbei.findViewById(R
                                .id.footer4zhuangbei_level2_ratingbar);
                        zhaungbei_level2.setRating(model.zhuangbeilist.get(1).zhuangbei_star);
                        zhuangbei2_ll.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //跳转到装备页
                                toEquipInfo(model.zhuangbeilist.get(1).getId());
                            }
                        });
                        zhuangbei2_ll.setVisibility(View.VISIBLE);
                    }
                    //第3个装备
                    if (size > 2) {
                        ImageView zhuangbei_img3 = (ImageView) footer_zhuangbei.findViewById(R.id
                                .footer4zhuangbei_zhuangbei3_img);
                        ImageLoader.getInstance().displayImage(model.zhuangbeilist.get(2)
                                .zhuangbei_image, zhuangbei_img3, ImageUtil
                                .getclub_topic_headImageOptions());
                        TextView zhuangbei_miaoshu3 = (TextView) footer_zhuangbei.findViewById(R
                                .id.footer4zhuangbei_introduce3_tv);
                        zhuangbei_miaoshu3.setText(model.zhuangbeilist.get(2).zhuangbei_title);
                        RatingBar zhaungbei_level3 = (RatingBar) footer_zhuangbei.findViewById(R
                                .id.footer4zhuangbei_level3_ratingbar);
                        zhaungbei_level3.setRating(model.zhuangbeilist.get(2).zhuangbei_star);
                        zhuangbei3_ll.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //跳转到装备页

                                toEquipInfo(model.zhuangbeilist.get(2).getId());
                            }
                        });
                        zhuangbei3_ll.setVisibility(View.VISIBLE);
                    }
                } else {
//                    player_fengexian_ll.setVisibility(View.GONE);
                }

                listView.addFooterView(footView);
                //设置底部详情信息
                updateActivity(model);

            }
        });

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void addLightSpots(List<String> lightspots) {
        for (int i=0;i<lightspots.size();i++) {
            View spot = getActivity().getLayoutInflater().inflate(R.layout
                    .ls_club_topic_list_adapter, null);
            TextView txt = (TextView) spot.findViewById(R.id.tv);
            txt.setText(lightspots.get(i));
            txt.setTextColor(Color.parseColor("#999999"));
            txt.setTextSize(14);
            lightspots_ll.addView(spot);
            lightSpots_list.add(spot);
        }
    }
    public void cleanLightSpots(){
        for (int i=0;i<lightSpots_list.size();i++) {
            lightspots_ll.removeView(lightSpots_list.get(i));
        }
        lightSpots_list.clear();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void toEquipInfo(String id){
        Intent intent = new Intent(ListFragment.this.getActivity(),LSEquipInfoActivity.class);
//        String id
        intent.putExtra("id", id);
        startActivity(intent);
    }

    public void cleanInfo(){
        listView.removeFooterView(footer_zhuangbei);
        listView.removeFooterView(footer_playerEvaluation);
        listView.removeFooterView(footView);
        header.setVisibility(View.INVISIBLE);
        title_area_ll.setVisibility(View.INVISIBLE);
        footer_ownerinfo.setVisibility(View.INVISIBLE);
        fullInfoAdapter=null;
        listView.setAdapter(null);
        model=null;
        cleanLightSpots();
        // fullInfoAdapter=null;
    }

    //  跳转目的地详情
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public  void goDestinationInfo (int tagId, int desId )
    {
        Intent intent = new Intent(ListFragment.this.getActivity(), DestinationActivity.class);
        intent.putExtra("destID", desId);
        intent.putExtra("tagID", tagId);
        startActivity(intent);

    }
}