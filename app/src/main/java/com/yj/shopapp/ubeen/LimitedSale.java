package com.yj.shopapp.ubeen;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by LK on 2018/3/22.
 *
 * @author LK
 */

public class LimitedSale implements Parcelable {


    /**
     * id : 8
     * itemid : 76102
     * name : 250ML东鹏特饮
     * itemnumber : 6934502300709
     * price : 58.00
     * unitprice : 58.00
     * num : 100
     * itemcount : 5
     * salesnum : 5
     * status : 1
     * start : 1521425600
     * unit : 件
     * specs : 1*24
     * imgurl : http://u.19diandian.com/Public/uploads/20170720/979b1f9df6e31b41a0b60469c1074f11.jpg
     * is_sale : 0
     * list : ["测试 14:11:40 抢到了5件250ML东鹏特饮"]
     */

    private String id;
    private String itemid;
    private String name;
    private String itemnumber;
    private String price;
    private String unitprice;
    private String num;
    private String itemcount;
    private String salesnum;
    private String status;
    private String start;
    private String unit;
    private String specs;
    private String imgurl;
    private int is_sale;
    private List<String> list;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
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

    public String getPrice() {
        return price;
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

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getItemcount() {
        return itemcount;
    }

    public void setItemcount(String itemcount) {
        this.itemcount = itemcount;
    }

    public String getSalesnum() {
        return salesnum;
    }

    public void setSalesnum(String salesnum) {
        this.salesnum = salesnum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
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

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public int getIs_sale() {
        return is_sale;
    }

    public void setIs_sale(int is_sale) {
        this.is_sale = is_sale;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.itemid);
        dest.writeString(this.name);
        dest.writeString(this.itemnumber);
        dest.writeString(this.price);
        dest.writeString(this.unitprice);
        dest.writeString(this.num);
        dest.writeString(this.itemcount);
        dest.writeString(this.salesnum);
        dest.writeString(this.status);
        dest.writeString(this.start);
        dest.writeString(this.unit);
        dest.writeString(this.specs);
        dest.writeString(this.imgurl);
        dest.writeInt(this.is_sale);
        dest.writeStringList(this.list);
    }

    public LimitedSale() {
    }

    public LimitedSale(String id) {
        this.id = id;
    }

    protected LimitedSale(Parcel in) {
        this.id = in.readString();
        this.itemid = in.readString();
        this.name = in.readString();
        this.itemnumber = in.readString();
        this.price = in.readString();
        this.unitprice = in.readString();
        this.num = in.readString();
        this.itemcount = in.readString();
        this.salesnum = in.readString();
        this.status = in.readString();
        this.start = in.readString();
        this.unit = in.readString();
        this.specs = in.readString();
        this.imgurl = in.readString();
        this.is_sale = in.readInt();
        this.list = in.createStringArrayList();
    }

    public static final Parcelable.Creator<LimitedSale> CREATOR = new Parcelable.Creator<LimitedSale>() {
        @Override
        public LimitedSale createFromParcel(Parcel source) {
            return new LimitedSale(source);
        }

        @Override
        public LimitedSale[] newArray(int size) {
            return new LimitedSale[size];
        }
    };
}
