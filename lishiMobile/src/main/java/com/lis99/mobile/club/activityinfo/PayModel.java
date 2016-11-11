package com.lis99.mobile.club.activityinfo;

import com.lis99.mobile.club.model.NewApplyUpData;
import com.lis99.mobile.club.model.SpecInfoListModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by yy on 2016/11/10.
 *      用来传递报名信息的类
 */

public class PayModel implements Serializable {

    public int clubId;
    public int batchId;
    public int topicId;
    public int selectNum;
    public double price;

    //    选择的规格信息
    public ArrayList<SpecInfoListModel.GuigelistEntity> joinList;
    //    选择的规格信息
    public HashMap<Integer, Integer> batchs;

    // 填写报名信息上传列表
    public ArrayList<NewApplyUpData> updata;
    //  出行日期
    public String startTime;

}
