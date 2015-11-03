package com.lis99.mobile.choiceness;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.lis99.mobile.R;
import com.lis99.mobile.application.data.DataManager;
import com.lis99.mobile.club.ClubSpecialListActivity;
import com.lis99.mobile.club.LSCLubSpecialMain;
import com.lis99.mobile.club.LSClubDetailActivity;
import com.lis99.mobile.club.LSClubTopicActivity;
import com.lis99.mobile.club.LSClubTopicNewActivity;
import com.lis99.mobile.club.model.ChoicenessBannerModel;
import com.lis99.mobile.club.model.ChoicenessModel;
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
import com.lis99.mobile.util.CardsAnimationAdapter;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.DeviceInfo;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.MyRequestManager;
import com.lis99.mobile.util.Page;
import com.lis99.mobile.webview.MyActivityWebView;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;

/**
 * Created by yy on 15/10/16.
 */
public class FragmentChoicenessList extends Fragment  implements
        com.lis99.mobile.entry.view.PullToRefreshView.OnHeaderRefreshListener, com.lis99.mobile.entry.view.PullToRefreshView.OnFooterRefreshListener, View.OnClickListener,
        LSSelectAdapter.OnSelectItemClickListener, ImagePageAdapter.ImagePageAdapterListener, ImagePageAdapter.ImagePageClickListener
{

    private View v, head;
    private ListView list;

    private PullToRefreshView pull_refresh_view;

    private ChoicenessAdapter adapter;

    private Page page;

    private BannerView bannerView;
    private LinearLayout layout_club_level, layout_leader_level, layout_hot_topic, layout_lis_special;

    private ImagePageAdapter bannerAdapter;

    private ChoicenessBannerModel bannerModel;

    private ChoicenessModel listModel;

    private View include_search;

    public FragmentChoicenessList ()
    {
        page = new Page();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        v = View.inflate(getActivity(), R.layout.club_level_list, null );

        pull_refresh_view = (PullToRefreshView) v.findViewById(R.id.pull_refresh_view);
        pull_refresh_view.setOnFooterRefreshListener(this);
        pull_refresh_view.setOnHeaderRefreshListener(this);

        list = (ListView) v.findViewById(R.id.list);


        head = View.inflate(getActivity(), R.layout.choiceness_new_head, null);


        bannerView = (BannerView) head.findViewById(R.id.viewBanner);

        layout_club_level = (LinearLayout) head.findViewById(R.id.layout_club_level);
        layout_leader_level = (LinearLayout) head.findViewById(R.id.layout_leader_level);
        layout_hot_topic = (LinearLayout) head.findViewById(R.id.layout_hot_topic);
        layout_lis_special = (LinearLayout) head.findViewById(R.id.layout_lis_special);
        include_search = head.findViewById(R.id.include_search);

        include_search.setOnClickListener(this);

        layout_lis_special.setOnClickListener(this);
        layout_hot_topic.setOnClickListener(this);
        layout_leader_level.setOnClickListener(this);
        layout_club_level.setOnClickListener(this);

//        page = new Page();

        list.addHeaderView(head);

        return v;
    }

    public void init ()
    {
        if ( adapter != null ) return;
        getList();

    }

    private void getList ()
    {
        if ( bannerAdapter == null )
        {
            String url = C.CHOICENESS_AD_BANNER;

            HashMap<String, Object> map = new HashMap<String, Object>();

            map.put("channel", DeviceInfo.CHANNELVERSION);
            map.put("version", DeviceInfo.CLIENTVERSIONCODE);
            map.put("platform", "Android");

            bannerModel = new ChoicenessBannerModel();

            MyRequestManager.getInstance().requestPost(url, map, bannerModel, new CallBack() {
                @Override
                public void handler(MyTask mTask) {
                    bannerModel = (ChoicenessBannerModel) mTask.getResultModel();

                    bannerAdapter = new ImagePageAdapter(getActivity(), bannerModel.lists.size());
                    bannerAdapter.addImagePageAdapterListener(FragmentChoicenessList.this);
                    bannerAdapter.setImagePageClickListener(FragmentChoicenessList.this);
                    bannerView.setBannerAdapter(bannerAdapter);
                }
            });
        }

        getInfo();

//        ImagePageAdapter adapter = new ImagePageAdapter(getActivity(), model.banners.size());
//        adapter.addImagePageAdapterListener(LSClubFragment.this);
//        adapter.setImagePageClickListener(LSClubFragment.this);
//        bannerView.setBannerAdapter(adapter);
    }

    private void getInfo ()
    {
        if ( page.isLastPage() )
        {
            return;
        }

        String userId = DataManager.getInstance().getUser().getUser_id();
        if (TextUtils.isEmpty(userId))
        {
            userId = "0";
        }
        String url = C.CHOICENESS_NEW_LIST + page.getPageNo() + "/" + userId;
        listModel = new ChoicenessModel();

        MyRequestManager.getInstance().requestGet(url, listModel, new CallBack() {
            @Override
            public void handler(MyTask mTask) {
                listModel = (ChoicenessModel) mTask.getResultModel();

                page.pageNo += 1;
                if ( adapter == null )
                {
                    page.setPageSize(listModel.totalpage);

                    if ("ttest".equals(DeviceInfo.CHANNELVERSION))
                    {
                        ChoicenessModel.Omnibuslist f = new ChoicenessModel().new Omnibuslist();
                        f.type = 4;
                        f.id = "846";
                        f.image = "http://i3.lis99.com/upload/club/6/7/4/677730e984cc53b440dbda459f09f564.jpg";
                        f.title = "本周天津户外精选";
                        f.subhead = "砾石在全国--天津站";

                        listModel.omnibuslist.add(0, f);
                    }

                    adapter = new ChoicenessAdapter(getActivity(), listModel.omnibuslist);

                    AnimationAdapter animationAdapter = new CardsAnimationAdapter(adapter);
                    animationAdapter.setAbsListView(list);
                    list.setAdapter(animationAdapter);


                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                                long arg3) {

                            if (adapter == null) return;
                            ChoicenessModel.Omnibuslist item = (ChoicenessModel.Omnibuslist) adapter.getItem(arg2 - 1);
                            if (item == null) return;
                            Intent intent = null;
                            if (item.type == 1 || item.type == 2 || item.type == 5 || item.type == 6) {
                                intent = new Intent(getActivity(), LSClubTopicActivity.class);
                                intent.putExtra("topicID", item.topic_id);
                                startActivity(intent);

                            } else if (item.type == 3) {
                                intent = new Intent(getActivity(), MyActivityWebView.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("URL", item.url);
                                bundle.putString("TITLE", item.title);
                                bundle.putString("IMAGE_URL", item.image);
                                bundle.putInt("TOPIC_ID", item.topic_id);
                                intent.putExtras(bundle);
                                startActivity(intent);

                            } else if (item.type == 4) {
                                intent = new Intent(getActivity(), SubjectActivity.class);
                                intent.putExtra("TITLE", item.title);
                                intent.putExtra("ID", item.id);
                                Common.log("item.id=" + item.id);
                                startActivity(intent);
                            } else if (item.type == 7) {
                                intent = new Intent(getActivity(), ClubSpecialListActivity.class);
                                intent.putExtra("tagid", item.tag_id);
                                startActivity(intent);
                            } else if (item.type == 8 || item.type == 9 ) {
                                intent = new Intent(getActivity(), LSClubTopicNewActivity.class);
                                intent.putExtra("topicID", item.topic_id);
                                startActivity(intent);
                            }

                        }
                    });

                }
                else
                {
                    adapter.setList(listModel.omnibuslist);
                }

            }
        });

    }

    private void cleanList ()
    {
        page = new Page();
        adapter = null;
        list.setAdapter(null);

    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId())
        {
            case R.id.include_search:
                startActivity(new Intent(getActivity(), SearchActivity.class));
                break;
            case R.id.layout_club_level:
                intent = new Intent(getActivity(), ClubSpecialListActivity.class);
                intent.putExtra("tagid", 12);
                startActivity( intent );
                break;
            case R.id.layout_leader_level:
                intent = new Intent(getActivity(), ClubSpecialListActivity.class);
                intent.putExtra("tagid", 3);
                startActivity( intent );
                break;
            case R.id.layout_hot_topic:
                intent = new Intent(getActivity(), ClubSpecialListActivity.class);
                intent.putExtra("tagid", 13);
                startActivity( intent );
                break;
            case R.id.layout_lis_special:
                startActivity( new Intent(getActivity(), LSCLubSpecialMain.class));

//                intent = new Intent(getActivity(), MyTestActivityWebView.class);
//                startActivity(intent);

                break;
        }
    }

    @Override
    public void onFooterRefresh(com.lis99.mobile.entry.view.PullToRefreshView view) {
        view.onFooterRefreshComplete();
        getList();

    }

    @Override
    public void onHeaderRefresh(com.lis99.mobile.entry.view.PullToRefreshView view) {
        view.onHeaderRefreshComplete();
        cleanList();
        getList();
    }

    @Override
    public void dispalyImage(ImageView banner, ImageView iv_load, int position) {

        if ( bannerModel == null || bannerModel.lists == null || bannerModel.lists.size() == 0 ) return;

        ImageLoader.getInstance().displayImage(bannerModel.lists.get(position).image, banner, ImageUtil.getclub_topic_imageOptions(), ImageUtil.getImageLoading(iv_load, banner));

    }

    @Override
    public void onClick(int index) {
        if ( bannerModel == null || bannerModel.lists == null || bannerModel.lists.size() <= index ) return;

        Intent intent = null;

        ChoicenessBannerModel.Lists item = bannerModel.lists.get(index);

        switch ( item.type )
        {
//            话题
            case 0:
//            线下贴
            case 1:
                intent = new Intent(getActivity(), LSClubTopicActivity.class);
                intent.putExtra("topicID", item.id);
                startActivity(intent);
                break;
//            线上贴
            case 2:
                intent = new Intent(getActivity(), LSClubTopicNewActivity.class);
                intent.putExtra("topicID", item.id);
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
                intent.putExtra("clubID", item.id);
                startActivity(intent);

                break;
        }

    }

    @Override
    public void onSelectItemClick(LSSelectContent content, LSSelectItem item) {

    }
}
