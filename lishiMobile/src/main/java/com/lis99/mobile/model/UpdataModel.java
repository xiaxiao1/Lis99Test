package com.lis99.mobile.model;

import com.google.gson.annotations.SerializedName;
import com.lis99.mobile.club.model.BaseModel;

/**
 * Created by yy on 16/4/5.
 *      版本更新类
 */
public class UpdataModel extends BaseModel{

    public String filePath;
    public String appName = "/lis99.apk";
    @SerializedName("url")
    public String url;
    @SerializedName("changelog")
    public String changelog;
    @SerializedName("version")
    public String version;

    /**
     *      0， 没有更新， 1 有更新， 2强制更新
     */
    @SerializedName("type")
    public int type;


}
