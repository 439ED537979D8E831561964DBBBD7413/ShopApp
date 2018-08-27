package com.yj.shopapp.ubeen;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LK on 2018/3/30.
 *
 * @author LK
 */

public class HotIndex implements Parcelable {


    /**
     * id : 75821
     * name : 250ML东鹏特饮
     * specs : 1*24/件
     * unitid : 9602
     * imgid : 37419
     * uid : 251
     * sale_status : 1
     * imgurl : http://u.19diandian.com/Public/uploads/20170720/59701b12f2e4c.jpg
     * unit : 件
     * price : 60.00
     * unitprice : 0
     */

    private String id;
    private String name;
    private String specs;
    private String unitid;
    private String imgid;
    private String uid;
    private String sale_status;
    private String imgurl;
    private String unit;
    private String price;
    private String unitprice;
    private String itemsum;
    private String msg;
    private String specialnote;
    private String localhost;
    private String split;

    public String getSplit() {
        return split == null ? "" : split;
    }

    public void setSplit(String split) {
        this.split = split;
    }

    public String getLocalhost() {
        return localhost == null ? "" : localhost;
    }

    public void setLocalhost(String localhost) {
        this.localhost = localhost;
    }

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

    public String getItemsum() {
        return itemsum == null ? "" : itemsum;
    }

    public void setItemsum(String itemsum) {
        this.itemsum = itemsum;
    }

    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecs() {
        return specs == null ? "" : specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    public String getUnitid() {
        return unitid == null ? "" : unitid;
    }

    public void setUnitid(String unitid) {
        this.unitid = unitid;
    }

    public String getImgid() {
        return imgid == null ? "" : imgid;
    }

    public void setImgid(String imgid) {
        this.imgid = imgid;
    }

    public String getUid() {
        return uid == null ? "" : uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSale_status() {
        return sale_status == null ? "" : sale_status;
    }

    public void setSale_status(String sale_status) {
        this.sale_status = sale_status;
    }

    public String getImgurl() {
        return imgurl == null ? "" : imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getUnit() {
        return unit == null ? "" : unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPrice() {
        return price == null ? "" : price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(String unitprice) {
        this.unitprice = unitprice;
    }

    public HotIndex() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.specs);
        dest.writeString(this.unitid);
        dest.writeString(this.imgid);
        dest.writeString(this.uid);
        dest.writeString(this.sale_status);
        dest.writeString(this.imgurl);
        dest.writeString(this.unit);
        dest.writeString(this.price);
        dest.writeString(this.unitprice);
        dest.writeString(this.itemsum);
        dest.writeString(this.msg);
        dest.writeString(this.specialnote);
        dest.writeString(this.localhost);
        dest.writeString(this.split);
    }

    protected HotIndex(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.specs = in.readString();
        this.unitid = in.readString();
        this.imgid = in.readString();
        this.uid = in.readString();
        this.sale_status = in.readString();
        this.imgurl = in.readString();
        this.unit = in.readString();
        this.price = in.readString();
        this.unitprice = in.readString();
        this.itemsum = in.readString();
        this.msg = in.readString();
        this.specialnote = in.readString();
        this.localhost = in.readString();
        this.split = in.readString();
    }

    public static final Creator<HotIndex> CREATOR = new Creator<HotIndex>() {
        @Override
        public HotIndex createFromParcel(Parcel source) {
            return new HotIndex(source);
        }

        @Override
        public HotIndex[] newArray(int size) {
            return new HotIndex[size];
        }
    };
}
