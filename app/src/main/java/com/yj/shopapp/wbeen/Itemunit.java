package com.yj.shopapp.wbeen;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jm on 2016/5/2.
 */
public class Itemunit implements Parcelable {

    /**
     * id : 14
     * name : 件
     * uid : 4
     */

    private String id;
    private String name;
    private String uid;

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
        dest.writeString(this.name);
        dest.writeString(this.uid);
    }

    public Itemunit() {
    }

    protected Itemunit(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.uid = in.readString();
    }

    public static final Parcelable.Creator<Itemunit> CREATOR = new Parcelable.Creator<Itemunit>() {
        @Override
        public Itemunit createFromParcel(Parcel source) {
            return new Itemunit(source);
        }

        @Override
        public Itemunit[] newArray(int size) {
            return new Itemunit[size];
        }
    };
}
