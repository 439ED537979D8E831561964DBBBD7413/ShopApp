package com.yj.shopapp.wbeen;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jm on 2016/5/4.
 */
public class SPlist implements Parcelable {


    /**
     * unitprice : 50
     * status : 2
     * time1 : 1522598400
     * time2 : 1527695999
     * id : 199345
     * name : 450ml 蓝色可乐这个商品的名称不是一般的长很长测试
     * itemnumber : 6971163470105
     * price : 55.00
     * specs : 1*15/件
     * imgid : 66511
     * sale_status : 1
     * imgurl : http://u.19diandian.com/Public/uploads/20180515/5afa7d42230f9.jpg
     * msg :
     */

    private String unitprice;
    private String status;
    private String time1;
    private String time2;
    private String id;
    private String name;
    private String itemnumber;
    private String price;
    private String specs;
    private String imgid;
    private String sale_status;
    private String imgurl;
    private String msg;
    private String saveid;
    private String addtime;

    public String getAddtime() {
        return addtime == null ? "" : addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getSaveid() {
        return saveid == null ? "" : saveid;
    }

    public void setSaveid(String saveid) {
        this.saveid = saveid;
    }

    public String getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(String unitprice) {
        this.unitprice = unitprice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    public String getImgid() {
        return imgid;
    }

    public void setImgid(String imgid) {
        this.imgid = imgid;
    }

    public String getSale_status() {
        return sale_status;
    }

    public void setSale_status(String sale_status) {
        this.sale_status = sale_status;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public SPlist() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.unitprice);
        dest.writeString(this.status);
        dest.writeString(this.time1);
        dest.writeString(this.time2);
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.itemnumber);
        dest.writeString(this.price);
        dest.writeString(this.specs);
        dest.writeString(this.imgid);
        dest.writeString(this.sale_status);
        dest.writeString(this.imgurl);
        dest.writeString(this.msg);
        dest.writeString(this.saveid);
        dest.writeString(this.addtime);
    }

    protected SPlist(Parcel in) {
        this.unitprice = in.readString();
        this.status = in.readString();
        this.time1 = in.readString();
        this.time2 = in.readString();
        this.id = in.readString();
        this.name = in.readString();
        this.itemnumber = in.readString();
        this.price = in.readString();
        this.specs = in.readString();
        this.imgid = in.readString();
        this.sale_status = in.readString();
        this.imgurl = in.readString();
        this.msg = in.readString();
        this.saveid = in.readString();
        this.addtime = in.readString();
    }

    public static final Creator<SPlist> CREATOR = new Creator<SPlist>() {
        @Override
        public SPlist createFromParcel(Parcel source) {
            return new SPlist(source);
        }

        @Override
        public SPlist[] newArray(int size) {
            return new SPlist[size];
        }
    };
}
