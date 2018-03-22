package com.yj.shopapp.wbeen;

import java.io.Serializable;

/**
 * Created by jm on 2016/5/4.
 */
public class SPlist implements Serializable{

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
    private String itemname;
    private String sale_status;
    private String imgurl;
    private String price;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
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

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }
}
