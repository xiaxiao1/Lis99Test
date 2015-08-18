package com.lis99.mobile.club.model;

import com.lis99.mobile.newhome.click.FriendsInterface;

import java.util.ArrayList;

/**
 * Created by yy on 15/8/18.
 */
public class MyFriendsAttentionModel extends BaseModel {


    public int totPage;

    public ArrayList<Lists> lists;

    public class Lists implements FriendsInterface
    {
        public String headicon;
        public int user_id;
        public String nickname;
        public int is_vip;
        public String topic_title;

        @Override
        public int getUserId() {
            return this.user_id;
        }

        @Override
        public int getIsAttention() {
            return 0;
        }

        @Override
        public void setAttention(int attention) {
        }
    }

}
