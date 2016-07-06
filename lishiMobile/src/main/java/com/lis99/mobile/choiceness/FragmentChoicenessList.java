package com.lis99.mobile.choiceness;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.choiceness.adapter.ChoicenessNewAdapter;
import com.lis99.mobile.choiceness.adapter.CommunityStarAdapter;
import com.lis99.mobile.choiceness.adapter.HotTalkAdapter;
import com.lis99.mobile.choiceness.adapter.SpecialAdapter;
import com.lis99.mobile.club.LSClubDetailActivity;
import com.lis99.mobile.club.LSClubTopicActivity;
import com.lis99.mobile.club.LSClubTopicNewActivity;
import com.lis99.mobile.club.model.ChoicenessBannerModel;
import com.lis99.mobile.club.model.ChoicenessModel;
import com.lis99.mobile.club.newtopic.LSClubNewTopicListMain;
import com.lis99.mobile.club.widget.BannerView;
import com.lis99.mobile.club.widget.ImagePageAdapter;
import com.lis99.mobile.engine.base.CallBack;
import com.lis99.mobile.engine.base.MyTask;
import com.lis99.mobile.entry.view.PullToRefreshView;
import com.lis99.mobile.newhome.LSSelectAdapter;
import com.lis99.mobile.newhome.LSSelectContent;
import com.lis99.mobile.newhome.LSSelectItem;
import com.lis99.mobile.search.SearchActivity;
import com.lis99.mobile.util.C;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.DeviceInfo;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.Page;
import com.lis99.mobile.view.MyListView;
import com.lis99.mobile.webview.MyActivityWebView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;

/**
 * Created by yy on 15/10/16.
 */
public class FragmentChoicenessList extends Fragment implements
        com.lis99.mobile.entry.view.PullToRefreshView.OnHeaderRefreshListener, com.lis99.mobile.entry.view.PullToRefreshView.OnFooterRefreshListener, View.OnClickListener,
        LSSelectAdapter.OnSelectItemClickListener, ImagePageAdapter.ImagePageAdapterListener, ImagePageAdapter.ImagePageClickListener {

    private View v, head;
    private ListView list;

    private PullToRefreshView pull_refresh_view;

//    private ChoicenessAdapter adapter;

    private ChoicenessNewAdapter adapter;

    private Page page;

    private BannerView bannerView;

    private ImagePageAdapter bannerAdapter;

    private ChoicenessBannerModel bannerModel;

    private ChoicenessModel listModel;

    private View include_search;

//    下面是改版新加的
    private MyListView sList;
    private TextView tvAllSpecial;
    private RecyclerView recyclerView;
    private MyListView hotList;
//  专栏
    private SpecialAdapter specialAdapter;
//  社区明星
    private CommunityStarAdapter communityStarAdapter;
//  热门讨论区
    private HotTalkAdapter hotTalkAdapter;






    public FragmentChoicenessList() {
        page = new Page();
    }

           @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);

            v = View.inflate(getActivity(), R.layout.club_level_list, null);

        pull_refresh_view = (PullToRefreshView) v.findViewById(R.id.pull_refresh_view);
        pull_refresh_view.setOnFooterRefreshListener(this);
        pull_refresh_view.setOnHeaderRefreshListener(this);

        list = (ListView) v.findViewById(R.id.list);


        head = View.inflate(getActivity(), R.layout.choiceness_new_new_head, null);
               head.setVisibility(View.GONE);

               initHead(head);

        bannerView = (BannerView) head.findViewById(R.id.viewBanner);

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

        include_search = head.findViewById(R.id.include_search);

        include_search.setOnClickListener(this);


//        page = new Page();

        list.addHeaderView(head);

        list.setAdapter(null);

        return v;
    }

//    新加入的
    private void initHead (View head)
    {
        sList = (MyListView) head.findViewById(R.id.s_list);
        tvAllSpecial = (TextView) head.findViewById(R.id.tv_all_special);
        tvAllSpecial.setOnClickListener(this);

        recyclerView = (RecyclerView) head.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutM = new LinearLayoutManager(getActivity());
        linearLayoutM.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutM);

        hotList = (MyListView) head.findViewById(R.id.hot_list);
    }

    public void init() {
        if (adapter != null) return;
        getList();

    }

    private void getList() {

            String url = C.COMMUNITY_AD_MAIN;

            HashMap<String, Object> map = new HashMap<String, Object>();

            map.put("channel", DeviceInfo.CHANNELVERSION);
            map.put("version", DeviceInfo.CLIENTVERSIONCODE);
            map.put("platform", "Android");

            bannerModel = new ChoicenessBannerModel();

            MyRequestManager.getInstance().requestPost(url, map, bannerModel, new CallBack() {
                @Override
                public void handler(MyTask mTask) {
                    bannerModel = (ChoicenessBannerModel) mTask.getResultModel();

                    if ( bannerModel == null ) return;
                    head.setVisibility(View.VISIBLE);

                    bannerAdapter = new ImagePageAdapter(getActivity(), bannerModel.adlist.size());
                    bannerAdapter.addImagePageAdapterListener(FragmentChoicenessList.this);
                    bannerAdapter.setImagePageClickListener(FragmentChoicenessList.this);
                    bannerView.setBannerAdapter(bannerAdapter);
                    bannerView.startAutoScroll();

//                    专栏
                    if ( bannerModel.daylist != null && bannerModel.daylist.size() != 0 )
                    {
                        specialAdapter = new SpecialAdapter(getActivity(), bannerModel.daylist);
                        sList.setAdapter(specialAdapter);
                        sList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                ChoicenessBannerModel.DaylistEntity item = (ChoicenessBannerModel
                                        .DaylistEntity) parent.getAdapter().getItem(position);
                                if ( item == null ) return;

                                int category = item.category;
                                int topicId = item.topicId;

                                Common.goTopic(getActivity(), category, topicId);
                            }
                        });
                    }


                    //                    社区明星
                    if ( bannerModel.startlist != null && bannerModel.startlist.size() != 0 )
                    {
                        ChoicenessBannerModel.StartlistEntity star = new ChoicenessBannerModel
                                .StartlistEntity();
                        star.nickname = "查看全部";
                        bannerModel.startlist.add(star);



                        communityStarAdapter = new CommunityStarAdapter(bannerModel.startlist, getActivity());
                        recyclerView.setAdapter(communityStarAdapter);
                        communityStarAdapter.setmOnItemClickLitener(new CommunityStarAdapter.OnItemClickLitener() {


                            @Override
                            public void onItemClick(View view, int position) {

                                if ( position == bannerModel.startlist.size() - 1 )
                                {
                                    startActivity(new Intent(getActivity(), CommunityStarActivity.class));
                                    return;
                                }

                                ChoicenessBannerModel.StartlistEntity item = bannerModel.startlist.get(position);
                                if ( item == null ) return;
                                Common.goUserHomeActivit(getActivity(), ""+item.userId);
                            }
                        });

                    }



//                    热门专题
                    if ( bannerModel.hotlist != null && bannerModel.hotlist.size() != 0 )
                    {
                        hotTalkAdapter = new HotTalkAdapter(getActivity(), bannerModel.hotlist);
                        hotList.setAdapter(hotTalkAdapter);
                    }
//                    热门讨论区
                    if ( bannerModel.omlist != null && bannerModel.omlist.size() != 0 )
                    {
                        adapter = new ChoicenessNewAdapter(getActivity(), bannerModel.omlist);
                        list.setAdapter(adapter);

                        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                if ( adapter == null ) return;

                                ChoicenessBannerModel.OmListEntiry item = (ChoicenessBannerModel
                                        .OmListEntiry) adapter.getItem(position - 1);
                                if ( item == null ) return;

                                Intent intent = new Intent(getActivity(), LSClubNewTopicListMain.class);
                                intent.putExtra("TOPICID", ""+item.topic_id);
                                startActivity(intent);

                            }
                        });

                    }



                }
            });

//        getInfo();

    }

//    private void getInfo() {
//        if (page.isLastPage()) {
//            return;
//        }
//
//        String userId = DataManager.getInstance().getUser().getUser_id();
//        if (TextUtils.isEmpty(userId)) {
//            userId = "0";
//        }
//        String url = C.COMMUNITY_LIST_MAIN + page.getPageNo() + "/" + userId;
//        listModel = new ChoicenessModel();
//
//        MyRequestManager.getInstance().requestGet(url, listModel, new CallBack() {
//            @Override
//            public void handler(MyTask mTask) {
//                listModel = (ChoicenessModel) mTask.getResultModel();
//
//                page.pageNo += 1;
//                if (adapter == null) {
//                    page.setPageSize(listModel.totalpage);
//
//                    if ("ttest".equals(DeviceInfo.CHANNELVERSION)) {
//                        ChoicenessModel.Omnibuslist f = new ChoicenessModel().new Omnibuslist();
//                        f.type = 4;
//                        f.id = "846";
//                        f.image = "http://i3.lis99.com/upload/club/6/7/4/677730e984cc53b440dbda459f09f564.jpg";
//                        f.title = "本周天津户外精选";
//                        f.subhead = "砾石在全国--天津站";
//
//                        listModel.omnibuslist.add(0, f);
//                    }
//
//                    adapter = new ChoicenessAdapter(getActivity(), listModel.omnibuslist);
//
//                    list.setAdapter(adapter);
//
////                    AnimationAdapter animationAdapter = new CardsAnimationAdapter(adapter);
////                    animationAdapter.setAbsListView(list);
////                    list.setAdapter(animationAdapter);
//
//
//                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//                        @Override
//                        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//                                                long arg3) {
//
//                            /**
//                             * 精选类型(1: 线下活动帖有文字，2：话题帖有文字 3：URL 4：专题 5: 线下活动帖无文字  6：话题帖无文字  7：标签 8：线上活动 9:线上活动帖有文字 10:新版话题无文字 11：新版话题有文字 12：专题V2)
//                             */
//                            if (adapter == null) return;
//                            ChoicenessModel.Omnibuslist item = (ChoicenessModel.Omnibuslist) adapter.getItem(arg2 - 1);
//                            if (item == null) return;
//                            Intent intent = null;
//                            if (item.type == 1 || item.type == 2 || item.type == 5 || item.type == 6) {
//                                intent = new Intent(getActivity(), LSClubTopicActivity.class);
//                                intent.putExtra("topicID", item.topic_id);
//                                startActivity(intent);
//
//                            } else if (item.type == 3) {
//                                intent = new Intent(getActivity(), MyActivityWebView.class);
//                                Bundle bundle = new Bundle();
//                                bundle.putString("URL", item.url);
//                                bundle.putString("TITLE", item.title);
//                                bundle.putString("IMAGE_URL", item.image);
//                                bundle.putInt("TOPIC_ID", item.topic_id);
//                                intent.putExtras(bundle);
//                                startActivity(intent);
//
//                            } else if (item.type == 4) {
//                                intent = new Intent(getActivity(), SubjectActivity.class);
//                                intent.putExtra("TITLE", item.title);
//                                intent.putExtra("ID", item.id);
//                                Common.log("item.id=" + item.id);
//                                startActivity(intent);
//                            } else if (item.type == 7) {
//                                intent = new Intent(getActivity(), ClubSpecialListActivity.class);
//                                intent.putExtra("tagid", item.tag_id);
//                                startActivity(intent);
//                            } else if (item.type == 8 || item.type == 9) {
//                                intent = new Intent(getActivity(), LSClubTopicNewActivity.class);
//                                intent.putExtra("topicID", item.topic_id);
//                                startActivity(intent);
//                            }
//                            else if ( item.type == 10 || item.type == 11 )
//                            {
//                                intent = new Intent(getActivity(), LSClubNewTopicListMain.class);
//                                intent.putExtra("TOPICID", ""+item.topic_id);
//                                startActivity(intent);
//                            }
//                            else if ( item.type == 12 )
//                            {
//                                intent = new Intent(getActivity(), SubjectActivity.class);
//                                intent.putExtra("TITLE", item.title);
//                                intent.putExtra("ID", item.id);
//                                intent.putExtra("TYPE", 12);
//                                Common.log("item.id=" + item.id);
//
//                                startActivity(intent);
//                            }
//
//                        }
//                    });
//
//                } else {
//                    adapter.setList(listModel.omnibuslist);
//                }
//            }
//        });
//
//    }

    private void cleanList() {
//        page = new Page();
//        adapter = null;
//        list.setAdapter(null);
//        bannerView.setBannerAdapter(null);


//        list.setAdapter(null);
//        hotList.setAdapter(null);
//        sList.setAdapter(null);
//        bannerAdapter = null;
//        communityStarAdapter = null;
//        hotTalkAdapter = null;
//        specialAdapter = null;


    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.include_search:
                startActivity(new Intent(getActivity(), SearchActivity.class));
//                startActivity(new Intent(getActivity(), MyTest.class));
                break;
            case R.id.tv_all_special:
                startActivity(new Intent(getActivity(), SpecialActivity.class));
                break;
        }

    }

    @Override
    public void onFooterRefresh(com.lis99.mobile.entry.view.PullToRefreshView view) {
        pull_refresh_view.onFooterRefreshComplete();
//        getList();

    }

    @Override
    public void onHeaderRefresh(com.lis99.mobile.entry.view.PullToRefreshView view) {
        pull_refresh_view.onHeaderRefreshComplete();
        cleanList();
        getList();
    }

    @Override
    public void dispalyImage(ImageView banner, ImageView iv_load, int position) {

        if (bannerModel == null || bannerModel.adlist == null || bannerModel.adlist.size() == 0)
            return;

        ImageLoader.getInstance().displayImage(bannerModel.adlist.get(position).image, banner,
                ImageUtil.getclub_topic_imageOptions(), ImageUtil.getImageLoading(iv_load, banner));

    }

    @Override
    public void onClick(int index) {
        if (bannerModel == null || bannerModel.adlist == null || bannerModel.adlist.size() <= index)
            return;

        Intent intent = null;

        ChoicenessBannerModel.AdlistEntity item = bannerModel.adlist.get(index);

        int id = Common.string2int(item.id);

        switch (item.type) {
//            话题
            case 0:
            case 1:
                intent = new Intent(getActivity(), LSClubTopicActivity.class);
                intent.putExtra("topicID", id);
                startActivity(intent);
                break;
//            线下贴
            case 5:
//                intent = new Intent(getActivity(), LSClubTopicActiveOffLine.class);
//                intent.putExtra("topicID", item.id);
//                startActivity(intent);
                Common.goTopic(getActivity(), 4, id);

                break;
//            新版话题帖
            case 6:
                intent = new Intent(getActivity(), LSClubNewTopicListMain.class);
                intent.putExtra("TOPICID", ""+id);
                startActivity(intent);
                break;
//            线上贴
            case 2:
                intent = new Intent(getActivity(), LSClubTopicNewActivity.class);
                intent.putExtra("topicID", id);
                startActivity(intent);
                break;
//            URL
            case 3:
                intent = new Intent(getActivity(), MyActivityWebView.class);
                Bundle bundle = new Bundle();
                bundle.putString("URL", item.link);
                bundle.putString("TITLE", "");
                bundle.putString("IMAGE_URL", item.image);
                bundle.putInt("TOPIC_ID", 0);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
//            俱乐部
            case 4:
                intent = new Intent(getActivity(), LSClubDetailActivity.class);
                intent.putExtra("clubID", id);
                startActivity(intent);

                break;
        }

    }

    @Override
    public void onSelectItemClick(LSSelectContent content, LSSelectItem item) {

    }

    public void scrollToTop() {
        if (list.getAdapter() != null) {
            list.setSelection(0);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
