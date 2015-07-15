package com.lis99.mobile.application.data;

import android.os.Parcel;
import android.os.Parcelable;

public class ShaiTuBean  implements Parcelable{

	private String id;
	private String image_url;
	private String nickname;
	private String headicon;
	private String user_id;
	private String is_vip;
	private String title;
	private String desc;
	
	
	public ShaiTuBean() {
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getImage_url() {
		return image_url;
	}
	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getHeadicon() {
		return headicon;
	}
	public void setHeadicon(String headicon) {
		this.headicon = headicon;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getIs_vip() {
		return is_vip;
	}
	public void setIs_vip(String is_vip) {
		this.is_vip = is_vip;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	private ShaiTuBean(Parcel source) {
		id=source.readString();
		image_url=source.readString();
		nickname=source.readString();
		headicon=source.readString();
		user_id=source.readString();
		is_vip=source.readString();
		title=source.readString();
		desc=source.readString();
    }
    
    public static final Parcelable.Creator<ShaiTuBean> CREATOR = new Creator<ShaiTuBean>() {

        @Override
        public ShaiTuBean createFromParcel(Parcel source) {
            return new ShaiTuBean(source);
        }

        @Override
        public ShaiTuBean[] newArray(int size) {
            return new ShaiTuBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(image_url);
        dest.writeString(nickname);
        dest.writeString(headicon);
        dest.writeString(user_id);
        dest.writeString(is_vip);
        dest.writeString(title);
        dest.writeString(desc);
    }
	
}
