package com.yj.shopapp.ubeen;

import android.os.Parcel;
import android.os.Parcelable;

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
     * couponlist : ["1.副食区、调味区下单满100返15"]
     * address : {"id":"1187","uid":"495","shopname":"测试","contacts":"测试安卓","tel":null,"mobile":"13688889999","address":"北京市东城区东华门街道祺胜酒店","status":"1"}
     * allcount : 1
     * allmoeny : 3.2
     * cashback : 0
     * class : [{"money":"3.20","num":"1","cid":"2","class":"副食区","classname":"副食区","imgurl":"http://u.19diandian.com/Public/uploads/classify/5ac8802bbb090.png"}]
     * classlist : [{"money":"3.20","num":"1","cid":"2","class":"副食区","classname":"副食区","imgurl":"http://u.19diandian.com/Public/uploads/classify/5ac8802bbb090.png"}]
     * cancel : [{"name":"168g天天旺香橙派","num":5,"unit":"包","itemid":"77523","sortid":"178905"}]
     */

    private AddressBean address;
    private int allcount;
    private String allmoeny;
    private String cashback;
    private List<String> couponlist;
    @SerializedName("class")
    @JSONField(name = "class")
    private List<ClassBean> classX;
    private List<ClasslistBean> classlist;
    private List<CancelBean> cancel;

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

    public String getCashback() {
        return cashback;
    }

    public void setCashback(String cashback) {
        this.cashback = cashback;
    }

    public List<String> getCouponlist() {
        return couponlist;
    }

    public void setCouponlist(List<String> couponlist) {
        this.couponlist = couponlist;
    }

    public List<ClassBean> getClassX() {
        return classX;
    }

    public void setClassX(List<ClassBean> classX) {
        this.classX = classX;
    }

    public List<ClasslistBean> getClasslist() {
        return classlist;
    }

    public void setClasslist(List<ClasslistBean> classlist) {
        this.classlist = classlist;
    }

    public List<CancelBean> getCancel() {
        return cancel;
    }

    public void setCancel(List<CancelBean> cancel) {
        this.cancel = cancel;
    }

    public static class AddressBean {
        /**
         * id : 1187
         * uid : 495
         * shopname : 测试
         * contacts : 测试安卓
         * tel : null
         * mobile : 13688889999
         * address : 北京市东城区东华门街道祺胜酒店
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
         * money : 3.20
         * num : 1
         * cid : 2
         * class : 副食区
         * classname : 副食区
         * imgurl : http://u.19diandian.com/Public/uploads/classify/5ac8802bbb090.png
         */

        private String money;
        private String num;
        private String cid;
        @SerializedName("class")
        @JSONField(name = "class")
        private String classX;
        private String classname;
        private String imgurl;

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

        public String getClassname() {
            return classname;
        }

        public void setClassname(String classname) {
            this.classname = classname;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }
    }

    public static class ClasslistBean {
        /**
         * money : 3.20
         * num : 1
         * cid : 2
         * class : 副食区
         * classname : 副食区
         * imgurl : http://u.19diandian.com/Public/uploads/classify/5ac8802bbb090.png
         */

        private String money;
        private String num;
        private String cid;
        @SerializedName("class")
        @JSONField(name = "class")
        private String classX;
        private String classname;
        private String imgurl;

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

        public String getClassname() {
            return classname;
        }

        public void setClassname(String classname) {
            this.classname = classname;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }
    }

    public static class CancelBean implements Parcelable {
        /**
         * name : 168g天天旺香橙派
         * num : 5
         * unit : 包
         * itemid : 77523
         * sortid : 178905
         */

        private String name;
        private int num;
        private String unit;
        private String itemid;
        private String sortid;
        private String ordernum;

        public String getOrdernum() {
            return ordernum == null ? "" : ordernum;
        }

        public void setOrdernum(String ordernum) {
            this.ordernum = ordernum;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getItemid() {
            return itemid;
        }

        public void setItemid(String itemid) {
            this.itemid = itemid;
        }

        public String getSortid() {
            return sortid;
        }

        public void setSortid(String sortid) {
            this.sortid = sortid;
        }

        public CancelBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.name);
            dest.writeInt(this.num);
            dest.writeString(this.unit);
            dest.writeString(this.itemid);
            dest.writeString(this.sortid);
            dest.writeString(this.ordernum);
        }

        protected CancelBean(Parcel in) {
            this.name = in.readString();
            this.num = in.readInt();
            this.unit = in.readString();
            this.itemid = in.readString();
            this.sortid = in.readString();
            this.ordernum = in.readString();
        }

        public static final Creator<CancelBean> CREATOR = new Creator<CancelBean>() {
            @Override
            public CancelBean createFromParcel(Parcel source) {
                return new CancelBean(source);
            }

            @Override
            public CancelBean[] newArray(int size) {
                return new CancelBean[size];
            }
        };
    }
}
