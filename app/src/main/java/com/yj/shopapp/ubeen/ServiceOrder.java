package com.yj.shopapp.ubeen;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;

/**
 * Created by LK on 2018/1/16.
 *
 * @author LK
 */

public class ServiceOrder implements Parcelable {

    /**
     * id : 19
     * order : 20180116145202
     * imgurl : http://u.19diandian.com/Public/uploads/shop/goods/5a4e10a94fd58.jpg
     * name : 小米mix
     * specs : S
     * unit : 件
     * num : 1
     * unitprice : 28
     * money : 28
     * shopname : 易久网络
     * contacts : 易久网络
     * mobile : 13688889999
     * address : 中国广东省东莞市东莞市市辖区里仁路
     * addtime : 1516085522
     * class_id : 2
     * shop_id : 1
     * status : 1
     * class : 下装
     * shop : 服装店
     */

    private String id;
    private String order;
    private String imgurl;
    private String name;
    private String specs;
    private String unit;
    private String num;
    private String unitprice;
    private String money;
    private String shopname;
    private String contacts;
    private String mobile;
    private String address;
    private String addtime;
    private String class_id;
    private String shop_id;
    private String status;
    @SerializedName("class")
    @JSONField(name = "class")
    private String classX;
    private String shop;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(String unitprice) {
        this.unitprice = unitprice;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClassX() {
        return classX;
    }

    public void setClassX(String classX) {
        this.classX = classX;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.order);
        dest.writeString(this.imgurl);
        dest.writeString(this.name);
        dest.writeString(this.specs);
        dest.writeString(this.unit);
        dest.writeString(this.num);
        dest.writeString(this.unitprice);
        dest.writeString(this.money);
        dest.writeString(this.shopname);
        dest.writeString(this.contacts);
        dest.writeString(this.mobile);
        dest.writeString(this.address);
        dest.writeString(this.addtime);
        dest.writeString(this.class_id);
        dest.writeString(this.shop_id);
        dest.writeString(this.status);
        dest.writeString(this.classX);
        dest.writeString(this.shop);
    }

    public ServiceOrder() {
    }

    protected ServiceOrder(Parcel in) {
        this.id = in.readString();
        this.order = in.readString();
        this.imgurl = in.readString();
        this.name = in.readString();
        this.specs = in.readString();
        this.unit = in.readString();
        this.num = in.readString();
        this.unitprice = in.readString();
        this.money = in.readString();
        this.shopname = in.readString();
        this.contacts = in.readString();
        this.mobile = in.readString();
        this.address = in.readString();
        this.addtime = in.readString();
        this.class_id = in.readString();
        this.shop_id = in.readString();
        this.status = in.readString();
        this.classX = in.readString();
        this.shop = in.readString();
    }

    public static final Parcelable.Creator<ServiceOrder> CREATOR = new Parcelable.Creator<ServiceOrder>() {
        @Override
        public ServiceOrder createFromParcel(Parcel source) {
            return new ServiceOrder(source);
        }

        @Override
        public ServiceOrder[] newArray(int size) {
            return new ServiceOrder[size];
        }
    };
}
