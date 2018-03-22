package com.yj.shopapp.ubeen;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/8/7 0007.
 */

public class Industry implements Parcelable {

    /**
     * id : 1
     * name : 饮料
     * url : http://u.19diandian.com/Public/uploads/classify/5986ed46426c5.png
     * addtime : 1502222222
     * uid : 1
     * sort : 1
     * <p>
     * remark : 饮料
     */

    private String id;
    private String name;
    private String url;
    private String addtime;
    private String uid;
    private String sort;
    private String remark;
    private String result;

    public int getResult() {
        return Integer.parseInt(result);
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.url);
        dest.writeString(this.addtime);
        dest.writeString(this.uid);
        dest.writeString(this.sort);
        dest.writeString(this.remark);
        dest.writeString(this.result);
    }

    public Industry() {
    }

    protected Industry(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.url = in.readString();
        this.addtime = in.readString();
        this.uid = in.readString();
        this.sort = in.readString();
        this.remark = in.readString();
        this.result = in.readString();
    }

    public static final Parcelable.Creator<Industry> CREATOR = new Parcelable.Creator<Industry>() {
        @Override
        public Industry createFromParcel(Parcel source) {
            return new Industry(source);
        }

        @Override
        public Industry[] newArray(int size) {
            return new Industry[size];
        }
    };
}
