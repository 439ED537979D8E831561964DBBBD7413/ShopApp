package com.yj.shopapp.ubeen;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * From ShopApp
 * Created by wxq on 17/4/5.
 * <p>
 * Beautiful Life ～
 */

public class IntegralDetail implements Parcelable {


    /**
     * id : 6715
     * type : 2
     * money : 0
     * orderid :
     * integral : -200
     * remark : 使用积分兑换商品
     * time : 1522295774
     */

    private String id;
    private String type;
    private String money;
    private String orderid;
    private String integral;
    private String remark;
    private String time;
    /**
     * type_name : APP下单
     */

    private String type_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.type);
        dest.writeString(this.money);
        dest.writeString(this.orderid);
        dest.writeString(this.integral);
        dest.writeString(this.remark);
        dest.writeString(this.time);
        dest.writeString(this.type_name);
    }

    public IntegralDetail() {
    }

    protected IntegralDetail(Parcel in) {
        this.id = in.readString();
        this.type = in.readString();
        this.money = in.readString();
        this.orderid = in.readString();
        this.integral = in.readString();
        this.remark = in.readString();
        this.time = in.readString();
        this.type_name = in.readString();
    }

    public static final Parcelable.Creator<IntegralDetail> CREATOR = new Parcelable.Creator<IntegralDetail>() {
        @Override
        public IntegralDetail createFromParcel(Parcel source) {
            return new IntegralDetail(source);
        }

        @Override
        public IntegralDetail[] newArray(int size) {
            return new IntegralDetail[size];
        }
    };
}
