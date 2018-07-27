package com.yj.shopapp.wbeen;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LK on 2018/5/23.
 *
 * @author LK
 */
public class Classify implements Parcelable {
    private String cid;
    private String name;

    public Classify() {
    }

    public Classify(String cid, String name) {
        this.cid = cid;
        this.name = name;
    }

    public String getCid() {
        return cid == null ? "" : cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cid);
        dest.writeString(this.name);
    }

    protected Classify(Parcel in) {
        this.cid = in.readString();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<Classify> CREATOR = new Parcelable.Creator<Classify>() {
        @Override
        public Classify createFromParcel(Parcel source) {
            return new Classify(source);
        }

        @Override
        public Classify[] newArray(int size) {
            return new Classify[size];
        }
    };
}
