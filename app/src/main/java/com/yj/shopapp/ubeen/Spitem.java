package com.yj.shopapp.ubeen;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jm on 2016/6/21.
 */
public class Spitem implements Parcelable {

    /**
     * id : 8
     * uid : 4
     * itemid : 2
     * time1 : 1456934400
     * time2 : 1459353599
     * sales : 2
     * gift : null
     * disstr : 10
     * status : 3
     * imgurl : http://u.yee9.net/Public/uploads/20160407/570663a23f9df.jpg
     * unit : 箱
     * itemname : 龙井茶修改
     */

    private String id;
    private String uid;
    private String itemid;
    private String time1;
    private String time2;
    private String sales;
    private String gift;
    private String disstr;
    private String status;
    private String imgurl;
    private String unit;
    private String itemname;
    private String sale_status;
    private String price;
    private String itemsum;
    private String specs;
    private String msg;
    private String specialnote;

    public String getSpecialnote() {
        return specialnote == null ? "" : specialnote;
    }

    public void setSpecialnote(String specialnote) {
        this.specialnote = specialnote;
    }

    public String getMsg() {
        return msg == null ? "" : msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSpecs() {
        return specs == null ? "" : specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    public String getItemsum() {
        return itemsum == null ? "" : itemsum;
    }

    public void setItemsum(String itemsum) {
        this.itemsum = itemsum;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSale_status() {
        return sale_status;
    }

    public void setSale_status(String sale_status) {
        this.sale_status = sale_status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getTime1() {
        return time1;
    }

    public void setTime1(String time1) {
        this.time1 = time1;
    }

    public String getTime2() {
        return time2;
    }

    public void setTime2(String time2) {
        this.time2 = time2;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public String getGift() {
        return gift;
    }

    public void setGift(String gift) {
        this.gift = gift;
    }

    public String getDisstr() {
        return disstr;
    }

    public void setDisstr(String disstr) {
        this.disstr = disstr;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public Spitem() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.uid);
        dest.writeString(this.itemid);
        dest.writeString(this.time1);
        dest.writeString(this.time2);
        dest.writeString(this.sales);
        dest.writeString(this.gift);
        dest.writeString(this.disstr);
        dest.writeString(this.status);
        dest.writeString(this.imgurl);
        dest.writeString(this.unit);
        dest.writeString(this.itemname);
        dest.writeString(this.sale_status);
        dest.writeString(this.price);
        dest.writeString(this.itemsum);
        dest.writeString(this.specs);
        dest.writeString(this.msg);
        dest.writeString(this.specialnote);
    }

    protected Spitem(Parcel in) {
        this.id = in.readString();
        this.uid = in.readString();
        this.itemid = in.readString();
        this.time1 = in.readString();
        this.time2 = in.readString();
        this.sales = in.readString();
        this.gift = in.readString();
        this.disstr = in.readString();
        this.status = in.readString();
        this.imgurl = in.readString();
        this.unit = in.readString();
        this.itemname = in.readString();
        this.sale_status = in.readString();
        this.price = in.readString();
        this.itemsum = in.readString();
        this.specs = in.readString();
        this.msg = in.readString();
        this.specialnote = in.readString();
    }

    public static final Creator<Spitem> CREATOR = new Creator<Spitem>() {
        @Override
        public Spitem createFromParcel(Parcel source) {
            return new Spitem(source);
        }

        @Override
        public Spitem[] newArray(int size) {
            return new Spitem[size];
        }
    };
}
