package com.lis99.mobile.util;

import com.lis99.mobile.R;
import com.lis99.mobile.club.model.ActiveBannerInfoModel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yy on 16/6/29.
 *
 *      本地存储数据
 *
 */
public class NativeEntityUtil {

    private static NativeEntityUtil instance;
//  达人标签
    private HashMap<String, Integer> communityStarTags;

    private ArrayList<ActiveBannerInfoModel> activeBanner;

    public static NativeEntityUtil getInstance ()
    {
        if ( instance == null ) instance = new NativeEntityUtil();
        return instance;
    }

//      默认 label_bg_default
    public HashMap<String, Integer> getCommunityStarTags ()
    {
        if ( communityStarTags != null ) return communityStarTags;

        communityStarTags = new HashMap<>();

        communityStarTags.put("潜水员", R.drawable.label_bg_qianshui);
        communityStarTags.put("攀冰狂人", R.drawable.label_bg_panbing);
        communityStarTags.put("岩壁舞者", R.drawable.label_bg_yanbi);
        communityStarTags.put("装备玩家", R.drawable.label_bg_zhuangbei);
        communityStarTags.put("光影大师", R.drawable.label_bg_guangying);
        communityStarTags.put("徒步行者", R.drawable.label_bg_tubu);
        communityStarTags.put("企业官方帐号", R.drawable.label_bg_qiye);
        communityStarTags.put("白色瘾君子", R.drawable.label_bg_baise);
        communityStarTags.put("山友", R.drawable.label_bg_shanyou);
        communityStarTags.put("老司机", R.drawable.label_bg_laosiji);

        return communityStarTags;
    }


    public ArrayList<ActiveBannerInfoModel> getActiveBanner ()
    {
        if ( activeBanner != null ) return activeBanner;
        activeBanner = new ArrayList<>();
        ActiveBannerInfoModel item = new ActiveBannerInfoModel();

        item.id = -1;
        item.name = "目的地";
        item.resultId = R.drawable.active_new_main_head_supper_icon;
        activeBanner.add(item);


        item = new ActiveBannerInfoModel();
        item.id = -2;
        item.name = "附近的活动";
        item.resultId = R.drawable.active_new_main_native_icon;
        activeBanner.add(item);

        item = new ActiveBannerInfoModel();
        item.id = 202;
        item.name = "徒步";
        item.resultId = R.drawable.active_new_banner_onfoot;
        activeBanner.add(item);

        item = new ActiveBannerInfoModel();
        item.id = 223;
        item.name = "登山";
        item.resultId = R.drawable.active_new_banner_hill;
        activeBanner.add(item);

        item = new ActiveBannerInfoModel();
        item.id = 224;
        item.name = "行摄";
        item.resultId = R.drawable.active_new_banner_photo;
        activeBanner.add(item);

        item = new ActiveBannerInfoModel();
        item.id = 204;
        item.name = "攀岩";
        item.resultId = R.drawable.active_new_banner_rock;
        activeBanner.add(item);

//        item = new ActiveBannerInfoModel();
//        item.id = 654;
//        item.name = "水上";
//        item.resultId = R.drawable.active_new_banner_water;
//        activeBanner.add(item);

        item = new ActiveBannerInfoModel();
        item.id = 211;
        item.name = "滑雪";
        item.resultId = R.drawable.active_new_banner_snow;
        activeBanner.add(item);

        item = new ActiveBannerInfoModel();
        item.id = 719;
        item.name = "特色";
        item.resultId = R.drawable.active_special_banner_icon;
        activeBanner.add(item);

        item = new ActiveBannerInfoModel();
        item.id = 225;
        item.name = "长线";
        item.resultId = R.drawable.active_new_banner_deep;
        activeBanner.add(item);



//        item = new ActiveBannerInfoModel();
//        item.id = 210;
//        item.name = "自驾";
//        item.resultId = R.drawable.active_new_banner_car;
//        activeBanner.add(item);



        item = new ActiveBannerInfoModel();
        item.id = 655;
        item.name = "其他";
        item.resultId = R.drawable.active_new_banner_other;
        activeBanner.add(item);




        return activeBanner;
    }

    public ArrayList<HashMap<String, String>> getNearbyActiveTime ()
    {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("name", "由近及远");
        map.put("id", "1");
        list.add(map);

        map = new HashMap<>();
        map.put("name", "由远及近");
        map.put("id", "2");
        list.add(map);

        return list;
    }

    public ArrayList<HashMap<String, String>> getNearbyActivePrice ()
    {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("name", "由低到高");
        map.put("id", "1");
        list.add(map);

        map = new HashMap<>();
        map.put("name", "由高到低");
        map.put("id", "2");
        list.add(map);

        return list;
    }


}
