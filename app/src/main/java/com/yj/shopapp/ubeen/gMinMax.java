package com.yj.shopapp.ubeen;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LK on 2017/9/30.
 */

public class gMinMax implements Parcelable {
    /**
     * minnum : 3
     * maxnum : 9
     * itemsum : 6
     */

    private String minnum;
    private String maxnum;
    private String itemsum;

    public String getMinnum() {
        return minnum;
    }

    public void setMinnum(String minnum) {
        this.minnum = minnum;
    }

    public String getMaxnum() {
        return maxnum;
    }

    public void setMaxnum(String maxnum) {
        this.maxnum = maxnum;
    }

    public String getItemsum() {
        return itemsum;
    }

    public void setItemsum(String itemsum) {
        this.itemsum = itemsum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.minnum);
        dest.writeString(this.maxnum);
        dest.writeString(this.itemsum);
    }

    public gMinMax(String minnum, String maxnum, String itemsum) {
        this.minnum = minnum;
        this.maxnum = maxnum;
        this.itemsum = itemsum;
    }

    public gMinMax() {
    }

    protected gMinMax(Parcel in) {
        this.minnum = in.readString();
        this.maxnum = in.readString();
        this.itemsum = in.readString();
    }

    public static final Parcelable.Creator<gMinMax> CREATOR = new Parcelable.Creator<gMinMax>() {
        @Override
        public gMinMax createFromParcel(Parcel source) {
            return new gMinMax(source);
        }

        @Override
        public gMinMax[] newArray(int size) {
            return new gMinMax[size];
        }
    };
}
