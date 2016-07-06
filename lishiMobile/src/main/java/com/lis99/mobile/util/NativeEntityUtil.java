package com.lis99.mobile.util;

import com.lis99.mobile.R;

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

    private ArrayList<HashMap<String, String>> activeBanner;

    public static NativeEntityUtil getInstance ()
    {
        if ( instance == null ) instance = new NativeEntityUtil();
        return instance;
    }


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
        communityStarTags.put("潜白色瘾君子", R.drawable.label_bg_baise);
        communityStarTags.put("山友", R.drawable.label_bg_shanyou);
        communityStarTags.put("老司机", R.drawable.label_bg_laosiji);

        return communityStarTags;
    }


    public ArrayList<HashMap<String, String>> getActiveBanner ()
    {
        if ( activeBanner != null ) return activeBanner;
        activeBanner = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();




        return null;
    }


}
