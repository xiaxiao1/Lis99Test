package com.lis99.mobile.club.activityinfo;

import android.annotation.TargetApi;
import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.lis99.mobile.R;
import com.lis99.mobile.club.LSClubTopicInfoLocation;
import com.lis99.mobile.club.widget.BannerView;
import com.lis99.mobile.club.widget.ImagePageAdapter;
import com.lis99.mobile.club.widget.RoundedImageView;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.ImageUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;


public class ListFragment extends BaseFragment implements ImagePageAdapter.ImagePageAdapterListener, ImagePageAdapter.ImagePageClickListener{

    //下拉刷新
    RefreshListview listView;
    //listview头部
    View header;
    //顶部轮播
    BannerView bannerView;
    //顶部标签串
    RecyclerView mRecyclerView;
    //百度地图
    MapView mMapView = null;
    //跳转到全屏地图
    ImageView toBigMap_img;
    View map_view;
    //活动标题
    TextView activeTitle_tv;
    //活动说明
    TextView activeNote_tv;
    //活动价格
    TextView activePrice_tv;
    //领队头像
    RoundedImageView leaderHead_img;
    //领队名字
    TextView leaderName_tv;
    //领队标签1
    TextView leaderlabel1_tv;
    //两队标签2
    TextView leaderlabel2_tv;
    //领队标签3
    TextView leaderlabel3_tv;
    //两队简介
    TextView leaderIntroduce_tv;
    //领队来自的俱乐部
    TextView leaderFrom_tv;
    //玩家评论view 动态是否显示
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
    RatingBar equipLevel;




    ImagePageAdapter bannerAdapter;
    FullInfoAdapter fullInfoAdapter;
    MyRecyclerAdapter recyclerviewAdapter;


    BaiduMap mBaiduMap = null;
    //活动详情图文数据
    List<FullInfo> activeInfos;
    //轮播图片urls
    List<String> urls_banner;
    //标签串数据
    List<String> recycler_datas;
    //地图定位的坐标
    private float latx = 30.963175f;
    private float laty = 120.400244f;
    //活动详情中当前要显示的item数目
    int currentSize;
    //活动详情一共多少item数目
    int fullSize;


    //回调赋值给Activity
    FullInfoInterface fullInfoInterface;

    //回调给activity执行title透明度操作
    AlphaInterface alphaInterface;

    public ListFragment() {

    }
    public void setAlphaInterface(AlphaInterface alphaInterface) {
        this.alphaInterface = alphaInterface;
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
        initDatas();
        //以下具体是要放在网络请求的回调中处理的。
        listView = (RefreshListview) view.findViewById(R.id.list);
        header=getActivity().getLayoutInflater().inflate(R.layout.activityinfo_header,null);
        listView.addHeaderView(header);
        View footer_ownerinfo=getActivity().getLayoutInflater().inflate(R.layout.activityinfo_footer_4ownerinfo,null);
        View footer_playerEvaluation=getActivity().getLayoutInflater().inflate(R.layout.activityinfo_footer_4player_evaluation,null);
        View footer_zhuangbei=getActivity().getLayoutInflater().inflate(R.layout.activityinfo_footer_4zhuangbei,null);
        listView.addFooterView(footer_ownerinfo);
        listView.addFooterView(footer_playerEvaluation);
        listView.addFooterView(footer_zhuangbei);
        final View footView = getActivity().getLayoutInflater()
                .inflate(R.layout.activityinfo_slidedetails_marker_default_layout, null);
        footView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open(true);
            }
        });
        listView.addFooterView(footView);
        fullInfoAdapter=new FullInfoAdapter(activeInfos);
        //下拉刷新
        listView.setonRefreshListener(new RefreshListview.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                Toast.makeText(ListFragment.this.getActivity(), "hh", Toast.LENGTH_SHORT).show();
                activeInfos.add(0,new FullInfo("我是新来的","http://pic10.nipic.com/20101019/3050636_171041025000_2.jpg"));
                fullInfoAdapter.notifyDataSetChanged();
                fullSize++;
                listView.onRefreshComplete();
            }
        });
        listView.setAlphaInterface(alphaInterface);
        listView.setAdapter(fullInfoAdapter);

        initBaiduMap();

        //顶部轮播图片
        bannerView=(BannerView)header.findViewById(R.id.afullinfo_lv_header_banner_banner);
        bannerAdapter=new ImagePageAdapter(getActivity(), urls_banner.size());
        bannerAdapter.addImagePageAdapterListener(this);
        bannerAdapter.setImagePageClickListener(this);
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
        bannerView.setBannerAdapter(bannerAdapter);
        bannerView.startAutoScroll();

        //横向可滑动标签串
        mRecyclerView=(RecyclerView)header.findViewById(R.id.afullinfo_recyclerview);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        recyclerviewAdapter=new MyRecyclerAdapter();
        mRecyclerView.setAdapter(recyclerviewAdapter);




    }

    @Override
    public void dispalyImage(ImageView banner, ImageView iv_load, int position) {
        if (urls_banner.size() == 0)
            return;

        ImageLoader.getInstance().displayImage(urls_banner.get(position), banner,
                ImageUtil.getclub_topic_imageOptions(), ImageUtil.getImageLoading(iv_load, banner));
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

    public void initDatas(){

        //初始化recyclerview的数据
        recycler_datas = new ArrayList<String>();
        for (int i = 'A'; i < 'F'; i++)
        {
            recycler_datas.add("你好啊世界" + (char) i);
        }
        //初始化活动详情的图文信息
        activeInfos= new ArrayList<FullInfo>();
        activeInfos.add(new FullInfo("1在那遥远的地方，有一群姑娘，哇哈哈哈藕丝作线难胜针，蕊粉染黄那得深。玉白兰芳不相顾， \n" +
                "青楼一笑轻千金。莫言自古皆如此，健剑刜钟铅绕指。 \n" +
                "三秋庭绿尽迎霜，惟有荷花守红死。庐江小吏朱斑轮， \n" +
                "柳缕吐芽香玉春。两股金钗已相许，不令独作空成尘。 \n" +
                "悠悠楚水流如马，恨紫愁红满平野。野土千年怨不平，","http://img.taopic.com/uploads/allimg/140814/240410-140Q40A60222.jpg"));
        activeInfos.add(new FullInfo("","http://pic45.nipic.com/20140804/2531170_201333732000_2.jpg"));
        activeInfos.add(new FullInfo("","http://img.taopic.com/uploads/allimg/140814/240410-140Q40F92258.jpg"));
        activeInfos.add(new FullInfo("4在那遥远的地方，有一群姑娘，哇哈哈哈故经之以五事，校之以计而索其情：一曰道，二曰天，三曰地，四曰将，" +
                "五曰法。道者，令民与上同意也，故可以与之死，可以与之生，而不畏危。天者，阴阳、寒暑、时制也。地者，远近、险易、广狭、死生也。" +
                "将者，智、信、仁、勇、严也。法者，曲制、官道、主用也。凡此五者，将莫不闻，知之者胜，不知者不胜。故校之以计而索其情，曰：" +
                "主孰有道？将孰有能？天地孰得？法令孰行？兵众孰强？士卒孰练？赏罚孰明？" +
                "吾以此知胜负矣。","http://img05.tooopen.com/images/20140805/sy_68279879485.jpg"));
        activeInfos.add(new FullInfo("5在那遥远的地方，有一群姑娘，哇哈哈哈",""));
        activeInfos.add(new FullInfo("6在那遥远的地方，有一群姑娘，哇哈哈哈计利以听，乃为之势，以佐其外。势者，因利而制权也。兵者，" +
                "诡道也。故能而示之不能，用而示之不用，近而示之远，远而示之近；利而诱之，乱而取之，实而备之，强而避之，怒而挠之，卑而骄之，" +
                "佚而劳之，亲而离之。攻其无备，出其不意。此兵家之胜，不可先传也。\n" +
                "\n" +
                "　　夫未战而庙算胜者，得算多也；未战而庙算不胜者，得算少也。多算胜，少算不胜，而况于无算乎！吾以此观之，胜负见矣。",""));
        activeInfos.add(new FullInfo("","http://img05.tooopen.com/images/20140805/sy_68268574237.jpg"));
        activeInfos.add(new FullInfo("8在那遥远的地方，有一群姑娘，哇哈哈哈",""));
        activeInfos.add(new FullInfo("","http://img05.tooopen.com/images/20140621/sy_63746973469.jpg"));
        activeInfos.add(new FullInfo("10在那遥远的地方，有一群姑娘，哇哈哈哈",""));
        activeInfos.add(new FullInfo("11在那遥远的地方，有一群姑娘，哇哈哈哈孙子曰：凡用兵之法，驰车千驷，革车千乘，带甲十万，千里馈粮。则内外之费，" +
                "宾客之用，胶漆之材，车甲之奉，日费千金，然后十万之师举矣。\n" +
                "\n" +
                "　　其用战也胜，久则钝兵挫锐，攻城则力屈，久暴师则国用不足。夫钝兵挫锐，屈力殚货，则诸侯乘其弊而起，虽有智者不能善其后矣。" +
                "故兵闻拙速，未睹巧之久也。夫兵久而国利者，" +
                "未之有也。故不尽知用兵之害者，则不能尽知用兵之利也。","http://pic10.nipic.com/20101019/3050636_171041025000_2.jpg"));
        fullSize=activeInfos.size();
        currentSize=fullSize>3?3:fullSize;
//        currentSize=fullSize;
        updateActivity("fragment");

       //初始化banner
        urls_banner=new ArrayList<String>();
        urls_banner.add("http://scimg.jb51.net/allimg/160618/77-16061Q44U6444.jpg");
        urls_banner.add("http://www.pptbz.com/pptpic/UploadFiles_6909/201204/2012041411433867.jpg");
        urls_banner.add("http://pic31.nipic.com/20130725/1729271_112810285306_2.jpg");
        urls_banner.add("http://pic32.nipic.com/20130813/9422601_092721678000_2.jpg");



    }

    @Override
    public void onDestroy() {
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
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

        mMapView = (MapView) header.findViewById(R.id.afullinfo_header_bmapView);
        mMapView.showZoomControls(false);
        mMapView.showScaleControl(false);
        map_view = (View) header.findViewById(R.id.afullinfo_header_mapclick);
        map_view.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onClick(View v) {
                Toast.makeText(ListFragment.this.getActivity(),"map",Toast.LENGTH_SHORT).show();
            }
        });

        mBaiduMap = mMapView.getMap();
        mBaiduMap.getUiSettings().setRotateGesturesEnabled(false);
        mBaiduMap.getUiSettings().setAllGesturesEnabled(false);
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        LatLng point = new LatLng(latx, laty);

        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_gcoding);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);

        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(point)
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
                String latx2 = "30.963175f";
                String laty2 = "120.400244f";
                Double latitude = Common.string2Double(latx2);
                Double longtitude = Common.string2Double(laty2);
                if ( latitude == -1 || longtitude == -1 )
                {
                    Common.toast("暂时没集合地图位置");
                    return;
                }
                intent.putExtra("latitude", latitude);
                intent.putExtra("longtitude", longtitude);
                startActivity(intent);
            }
        });

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

        private List<FullInfo> datas;
        Holder viewHolder = null;

        FullInfoAdapter(List<FullInfo> datas) {
            this.datas = datas;
        }

        @Override
        public int getCount() {
            return null == datas ? 0 : currentSize;
        }

        @Override
        public FullInfo getItem(int position) {
            return null == datas ? null : datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            FullInfo fi=datas.get(position);
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
            String c=fi.getContent();
            String img=fi.getImgUrl();
            Log.i("xx","c:"+c+"\n img:"+img+" currentsize:"+currentSize+"  fullsize:"+fullSize);
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
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        holder.tv.setText(recycler_datas.get(position));
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
}