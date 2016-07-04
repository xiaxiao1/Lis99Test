package com.lis99.mobile.util;

import com.lis99.mobile.R;

import java.util.HashMap;

/**
 * Created by yy on 16/6/29.
 *
 *      本地存储数据
 *
 */
public class NativeEntityUtil {

    private static NativeEntityUtil instance;

    private HashMap<String, Integer> communityStarTags;

    public static NativeEntityUtil getInstance ()
    {
        if ( instance == null ) instance = new NativeEntityUtil();
        return instance;
    }


    public HashMap<String, Integer> getCommunityStarTags ()
    {
        if ( communityStarTags != null ) return communityStarTags;

        communityStarTags = new HashMap<>();

        communityStarTags.put("白色瘾君子", R.drawable.community_star_0);
        communityStarTags.put("光影大师", R.drawable.community_star_1);
        communityStarTags.put("老司机", R.drawable.community_star_2);
        communityStarTags.put("攀冰狂人", R.drawable.community_star_3);
        communityStarTags.put("企业账号", R.drawable.community_star_4);
        communityStarTags.put("潜水员", R.drawable.community_star_5);
        communityStarTags.put("山友", R.drawable.community_star_6);
        communityStarTags.put("徒步行者", R.drawable.community_star_7);
        communityStarTags.put("岩壁舞者", R.drawable.community_star_8);
        communityStarTags.put("装备玩家", R.drawable.community_star_9);
        communityStarTags.put("默认色值", R.drawable.community_star_10);

        return communityStarTags;
    }



}
