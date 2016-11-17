package com.lis99.mobile.newhome.activeline.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lis99.mobile.R;
import com.lis99.mobile.club.model.ActiveLineADModel;
import com.lis99.mobile.club.model.ActiveMainHeadModel;
import com.lis99.mobile.club.widget.BannerView;
import com.lis99.mobile.club.widget.ImagePageAdapter;
import com.lis99.mobile.util.ActivityUtil;
import com.lis99.mobile.util.Common;
import com.lis99.mobile.util.ImageUtil;
import com.lis99.mobile.util.MyBaseAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * Created by yy on 16/7/7.
 */
public class ActiveMainRecommendAdapter extends MyBaseAdapter {

    private final int count = 2;

    private int info = 0;

    private  int ad = 1;


    public ActiveMainRecommendAdapter(Activity c, List listItem) {
        super(c, listItem);
    }
//  添加广告， 遍历列表插入
    public void setAd(ActiveLineADModel model )
    {
        int activeSize = listItem.size();
//        int adSize = model.adlist.size();
//        int size = Math.max(activeSize, adSize);
        List<Object> newList = new ArrayList<>();
        ActiveLineADModel.Adlist lastItem = null;

        for ( int i = 0; i < activeSize; i++ )
        {
//            添加推荐活动
            newList.add(listItem.get(i));
//          权重从1开始
            int weight = i + 1;
            if ( model.adlist.containsKey(""+weight))
            {
                ActiveLineADModel.Adlist item = new ActiveLineADModel.Adlist();
                item.lists = model.adlist.get(""+weight);
                newList.add(item);
                model.adlist.remove(""+weight);

//                最后一条
                if ( i == activeSize - 1 )
                {
                    lastItem = item;
                }
            }
        }
//        获取剩下的权重信息
        Set<String> keySet =  model.adlist.keySet();

        if ( keySet.isEmpty())
        {
            setList(newList);
            return;
        }

//        如果最后一条没有广告， 则手动添加一个
        if ( lastItem == null )
        {
            lastItem = new ActiveLineADModel.Adlist();
            lastItem.lists = new ArrayList<>();
            newList.add(lastItem);
        }

        Iterator<String> iterator = keySet.iterator();
        while (iterator.hasNext())
        {
            String key = iterator.next();
            lastItem.lists.addAll(model.adlist.get(key));
        }

        setList(newList);
    }

    @Override
    public int getViewTypeCount() {
        return count;
    }

    @Override
    public int getItemViewType(int position) {

        Object o = getItem(position);
        if ( o instanceof ActiveMainHeadModel.HotlistEntity )
        {
            return info;
        }
        else
        {
            return ad;
        }

    }

    @Override
    public View setView(int i, View view, ViewGroup viewGroup) {

        if ( ad == getItemViewType(i) )
        {
            return getAD(i, view, viewGroup);
        }

        if (view == null) {
            view = View.inflate(mContext, R.layout.active_recommend_adapter, null);
            view.setTag(new ViewHolder(view));
        }
        initializeViews(getItem(i), (ViewHolder) view.getTag());
        return view;
    }

    private View getAD ( int i, View view, ViewGroup viewGroup )
    {
        BannerHolder bannerHolder = null;
        if ( view == null )
        {
            view = View.inflate(mContext, R.layout.active_line_hot_ad, null);
            bannerHolder = new BannerHolder(view);
            view.setTag(bannerHolder);
        }
        else
        {
            bannerHolder = (BannerHolder) view.getTag();
        }

        final BannerHolder finalBannerHolder = bannerHolder;
        bannerHolder.bannerAD.mViewPager.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                v.getParent().requestDisallowInterceptTouchEvent(true);

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        finalBannerHolder.bannerAD.stopAutoScroll();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        finalBannerHolder.bannerAD.stopAutoScroll();
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        finalBannerHolder.bannerAD.startAutoScroll();
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });

        final ActiveLineADModel.Adlist item = (ActiveLineADModel.Adlist) getItem(i);

        ImagePageAdapter bannerAdapter=new ImagePageAdapter(mContext, item.lists.size());
        bannerHolder.bannerAD.setBannerAdapter(bannerAdapter);
        bannerHolder.bannerAD.startAutoScroll();

        bannerAdapter.addImagePageAdapterListener(new ImagePageAdapter.ImagePageAdapterListener() {
            @Override
            public void dispalyImage(ImageView banner, ImageView iv_load, int position) {
                if ( item == null || item.lists == null || item.lists.size() == 0 ) return;
                ActiveLineADModel.Adlist info = item.lists.get(position);
                ImageLoader.getInstance().displayImage(info.images, banner,
                        ImageUtil.getclub_topic_imageOptions(), ImageUtil.getImageLoading(iv_load, banner));

            }
        });
        bannerAdapter.setImagePageClickListener(new ImagePageAdapter.ImagePageClickListener() {
            @Override
            public void onClick(int index) {
                if ( item == null || item.lists == null || item.lists.size() == 0 ) return;

                Intent intent = null;

                ActiveLineADModel.Adlist info = item.lists.get(index);

                int id = Common.string2int(item.url);

                int type = Common.string2int(item.type);

                switch (type) {
//            话题
                    case 0:
                    case 1:
//                        intent = new Intent(mContext, LSClubTopicActivity.class);
//                        intent.putExtra("topicID", id);
//                        mContext.startActivity(intent);
                        Common.goTopic(mContext, 0, id);
                        break;
//            线下贴
                    case 5:
//                intent = new Intent(getActivity(), LSClubTopicActiveOffLine.class);
//                intent.putExtra("topicID", item.id);
//                startActivity(intent);
                        Common.goTopic(mContext, 4, id);

                        break;
//            新版话题帖
                    case 6:
//                        intent = new Intent(getActivity(), LSClubNewTopicListMain.class);
//                        intent.putExtra("TOPICID", ""+id);
//                        startActivity(intent);
                        Common.goTopic(mContext, 3, id);
                        break;
//            线上贴
                    case 2:
//                        intent = new Intent(getActivity(), LSClubTopicNewActivity.class);
//                        intent.putExtra("topicID", id);
//                        startActivity(intent);
                        Common.goTopic(mContext, 2, id);
                        break;
//            URL
                    case 3:
//                        intent = new Intent(getActivity(), MyActivityWebView.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putString("URL", item.link);
//                        bundle.putString("TITLE", "");
//                        bundle.putString("IMAGE_URL", item.image);
//                        bundle.putInt("TOPIC_ID", 0);
//                        intent.putExtras(bundle);
//                        startActivity(intent);
                        ActivityUtil.getURLActivity(mContext, item.url, "", item.images, id);
                        break;
//            俱乐部
                    case 4:
//                        intent = new Intent(getActivity(), LSClubDetailActivity.class);
//                        intent.putExtra("clubID", id);
//                        startActivity(intent);

                        ActivityUtil.goClubInfo(mContext, id);

                        break;
                }



            }
        });

        return view;
    }

    private void initializeViews(Object object, ViewHolder holder) {
        //TODO implement

        final ActiveMainHeadModel.HotlistEntity item = (ActiveMainHeadModel.HotlistEntity) object;
        if ( item == null ) return;
        holder.title.setText(item.title);

        if ( item.actlist == null ) return;

        ActiveMainRecommendRecycler recycler = new ActiveMainRecommendRecycler(item.actlist, mContext);
        holder.recyclerView.setAdapter(recycler);
        recycler.setOnItemClickLitener(new MyBaseRecycler.OnItemClickLitener() {
            @Override
            public void onItemClick(int position) {
                int topicId = item.actlist.get(position).topicId;
                Common.goTopic(mContext, 4, topicId);

            }
        });

    }

    protected static class BannerHolder
    {
        private BannerView bannerAD;

        public BannerHolder( View view ) {
            this.bannerAD = (BannerView) view.findViewById(R.id.bannerAD);
        }
    }

    protected class ViewHolder {
        private TextView title;
        private RecyclerView recyclerView;

        public ViewHolder(View view) {
            title = (TextView) view.findViewById(R.id.title);
            recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(
                    mContext, LinearLayoutManager.HORIZONTAL, false));
        }
    }
}
