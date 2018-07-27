package com.yj.shopapp.wbeen;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jm on 2016/5/2.
 */
public class Itemtype implements Parcelable {

    /**
     * id : 22
     * industryid : 0
     * pid : 0
     * name : 通用分类
     * sort : 99
     * uid : 0
     */

    private String id;
    private String industryid;
    private String pid;
    private String name;
    private String sort;
    private String uid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIndustryid() {
        return industryid;
    }

    public void setIndustryid(String industryid) {
        this.industryid = industryid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.industryid);
        dest.writeString(this.pid);
        dest.writeString(this.name);
        dest.writeString(this.sort);
        dest.writeString(this.uid);
    }

    public Itemtype() {
    }

    protected Itemtype(Parcel in) {
        this.id = in.readString();
        this.industryid = in.readString();
        this.pid = in.readString();
        this.name = in.readString();
        this.sort = in.readString();
        this.uid = in.readString();
    }

    public static final Parcelable.Creator<Itemtype> CREATOR = new Parcelable.Creator<Itemtype>() {
        @Override
        public Itemtype createFromParcel(Parcel source) {
            return new Itemtype(source);
        }

        @Override
        public Itemtype[] newArray(int size) {
            return new Itemtype[size];
        }
    };
}
