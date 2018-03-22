package com.yj.shopapp.ubeen;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by LK on 2017/12/26.
 *
 * @author LK
 */

public class OrderPreview {


    /**
     * couponlist : ["副食区、调味区下单满100返15"]
     * address : {"id":"747","uid":"495","shopname":"易久网络","contacts":"易久网络","tel":null,"mobile":"13688889999","address":"中国广东省东莞市东莞市市辖区里仁路","status":"1"}
     * youhui : ["副食区、调味区下单满100返15"]
     * allcount : 34
     * allmoeny : 1472
     * cashback : 20
     * class : [{"money":"1472.00","num":"34","cid":"2","class":"副食区"}]
     */

    private AddressBean address;
    private int allcount;
    private String allmoeny;
    private int cashback;
    private List<String> couponlist;
    private List<String> youhui;
    @SerializedName("class")
    @JSONField(name = "class")
    private List<ClassBean> classX;

    public AddressBean getAddress() {
        return address;
    }

    public void setAddress(AddressBean address) {
        this.address = address;
    }

    public int getAllcount() {
        return allcount;
    }

    public void setAllcount(int allcount) {
        this.allcount = allcount;
    }

    public String getAllmoeny() {
        return allmoeny;
    }

    public void setAllmoeny(String allmoeny) {
        this.allmoeny = allmoeny;
    }

    public int getCashback() {
        return cashback;
    }

    public void setCashback(int cashback) {
        this.cashback = cashback;
    }

    public List<String> getCouponlist() {
        return couponlist;
    }

    public void setCouponlist(List<String> couponlist) {
        this.couponlist = couponlist;
    }

    public List<String> getYouhui() {
        return youhui;
    }

    public void setYouhui(List<String> youhui) {
        this.youhui = youhui;
    }

    public List<ClassBean> getClassX() {
        return classX;
    }

    public void setClassX(List<ClassBean> classX) {
        this.classX = classX;
    }

    public static class AddressBean {
        /**
         * id : 747
         * uid : 495
         * shopname : 易久网络
         * contacts : 易久网络
         * tel : null
         * mobile : 13688889999
         * address : 中国广东省东莞市东莞市市辖区里仁路
         * status : 1
         */

        private String id;
        private String uid;
        private String shopname;
        private String contacts;
        private Object tel;
        private String mobile;
        private String address;
        private String status;

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

        public Object getTel() {
            return tel;
        }

        public void setTel(Object tel) {
            this.tel = tel;
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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public static class ClassBean {
        /**
         * money : 1472.00
         * num : 34
         * cid : 2
         * class : 副食区
         */

        private String money;
        private String num;
        private String cid;
        @SerializedName("class")
        @JSONField(name = "class")
        private String classX;

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getClassX() {
            return classX;
        }

        public void setClassX(String classX) {
            this.classX = classX;
        }
    }
}
