package com.yj.shopapp.ubeen;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LK on 2018/3/19.
 *
 * @author LK
 */

public class OrderRecord implements Parcelable {


    /**
     * num : 10
     * money : 580.00
     * itemid : 76102
     * unit : 件
     * specs : 1*24
     * name : 250ML东鹏特饮
     * itemnumber : 6934502300709
     * imgurl : http://u.19diandian.com/Public/uploads/20170720/979b1f9df6e31b41a0b60469c1074f11.jpg
     */

    private String num;
    private String money;
    private String itemid;
    private String unit;
    private String specs;
    private String name;
    private String itemnumber;
    private String imgurl;

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItemnumber() {
        return itemnumber;
    }

    public void setItemnumber(String itemnumber) {
        this.itemnumber = itemnumber;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.num);
        dest.writeString(this.money);
        dest.writeString(this.itemid);
        dest.writeString(this.unit);
        dest.writeString(this.specs);
        dest.writeString(this.name);
        dest.writeString(this.itemnumber);
        dest.writeString(this.imgurl);
    }

    public OrderRecord() {
    }

    protected OrderRecord(Parcel in) {
        this.num = in.readString();
        this.money = in.readString();
        this.itemid = in.readString();
        this.unit = in.readString();
        this.specs = in.readString();
        this.name = in.readString();
        this.itemnumber = in.readString();
        this.imgurl = in.readString();
    }

    public static final Parcelable.Creator<OrderRecord> CREATOR = new Parcelable.Creator<OrderRecord>() {
        @Override
        public OrderRecord createFromParcel(Parcel source) {
            return new OrderRecord(source);
        }

        @Override
        public OrderRecord[] newArray(int size) {
            return new OrderRecord[size];
        }
    };
}
