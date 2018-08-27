package com.yj.shopapp.ubeen;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LK on 2018/8/18.
 *
 * @author LK
 */
public class BoughtGoods implements Parcelable {

    /**
     * id : 208014
     * name : 傻大哥200g小籽焦糖花生
     * itemnumber : 6949128601239
     * itemsum : 0
     * sale_status : 0
     * minnum : 3
     * maxnum : 0
     * specs : 1*200g
     * unit : 包
     * split :
     * specialnote :
     * imgurl : http://u.19diandian.com/Public/uploads/20180627/c785cd86b87c70e75ae2edf20eeeaa2b.jpg
     * itemid : 208014
     * num : 15
     * money : 57.00
     * areaid : 27829
     * dolistcart : 1
     */

    private String id;
    private String name;
    private String itemnumber;
    private String itemsum;
    private String sale_status;
    private String minnum;
    private String maxnum;
    private String specs;
    private String unit;
    private String split;
    private String specialnote;
    private String imgurl;
    private String itemid;
    private String num;
    private String money;
    private String areaid;
    private int dolistcart;
    private String price;

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

    public String getItemnumber() {
        return itemnumber == null ? "" : itemnumber;
    }

    public void setItemnumber(String itemnumber) {
        this.itemnumber = itemnumber;
    }

    public String getItemsum() {
        return itemsum == null ? "" : itemsum;
    }

    public void setItemsum(String itemsum) {
        this.itemsum = itemsum;
    }

    public String getSale_status() {
        return sale_status == null ? "" : sale_status;
    }

    public void setSale_status(String sale_status) {
        this.sale_status = sale_status;
    }

    public String getMinnum() {
        return minnum == null ? "" : minnum;
    }

    public void setMinnum(String minnum) {
        this.minnum = minnum;
    }

    public String getMaxnum() {
        return maxnum == null ? "" : maxnum;
    }

    public void setMaxnum(String maxnum) {
        this.maxnum = maxnum;
    }

    public String getSpecs() {
        return specs == null ? "" : specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    public String getUnit() {
        return unit == null ? "" : unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getSplit() {
        return split == null ? "" : split;
    }

    public void setSplit(String split) {
        this.split = split;
    }

    public String getSpecialnote() {
        return specialnote == null ? "" : specialnote;
    }

    public void setSpecialnote(String specialnote) {
        this.specialnote = specialnote;
    }

    public String getImgurl() {
        return imgurl == null ? "" : imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getItemid() {
        return itemid == null ? "" : itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getNum() {
        return num == null ? "" : num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getMoney() {
        return money == null ? "" : money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getAreaid() {
        return areaid == null ? "" : areaid;
    }

    public void setAreaid(String areaid) {
        this.areaid = areaid;
    }

    public int getDolistcart() {
        return dolistcart;
    }

    public void setDolistcart(int dolistcart) {
        this.dolistcart = dolistcart;
    }

    public String getPrice() {
        return price == null ? "" : price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public BoughtGoods() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.itemnumber);
        dest.writeString(this.itemsum);
        dest.writeString(this.sale_status);
        dest.writeString(this.minnum);
        dest.writeString(this.maxnum);
        dest.writeString(this.specs);
        dest.writeString(this.unit);
        dest.writeString(this.split);
        dest.writeString(this.specialnote);
        dest.writeString(this.imgurl);
        dest.writeString(this.itemid);
        dest.writeString(this.num);
        dest.writeString(this.money);
        dest.writeString(this.areaid);
        dest.writeInt(this.dolistcart);
        dest.writeString(this.price);
    }

    protected BoughtGoods(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.itemnumber = in.readString();
        this.itemsum = in.readString();
        this.sale_status = in.readString();
        this.minnum = in.readString();
        this.maxnum = in.readString();
        this.specs = in.readString();
        this.unit = in.readString();
        this.split = in.readString();
        this.specialnote = in.readString();
        this.imgurl = in.readString();
        this.itemid = in.readString();
        this.num = in.readString();
        this.money = in.readString();
        this.areaid = in.readString();
        this.dolistcart = in.readInt();
        this.price = in.readString();
    }

    public static final Creator<BoughtGoods> CREATOR = new Creator<BoughtGoods>() {
        @Override
        public BoughtGoods createFromParcel(Parcel source) {
            return new BoughtGoods(source);
        }

        @Override
        public BoughtGoods[] newArray(int size) {
            return new BoughtGoods[size];
        }
    };
}
