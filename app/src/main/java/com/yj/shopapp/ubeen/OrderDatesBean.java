package com.yj.shopapp.ubeen;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by LK on 2017/12/22.
 *
 * @author LK
 */

public class OrderDatesBean {

    /**
     * oid : 201712141251545055
     * status : 1
     * addtime : 1513226995
     * agentuser : 多个批发商
     * money : 50.00
     * coupon : 0
     * couponlist : []
     * data : [{"money":"50.00","itemnum":"1","name":"饮料","id":"1"}]
     * itemlist : [{"itemname":"300ml新鲜港湾鲜磨豆奶","imageUrl":"http://u.19diandian.com/Public/uploads/20171118/5a0fe72d51209.jpg","itemcount":"1","moneysum":"50.00"}]
     */

    private String oid;
    private String status;
    private String addtime;
    private String agentuser;
    private String money;
    private int allnum;
    private int coupon;
    private List<CouponBran> couponlist;
    private List<DataBean> data;
    private List<ItemlistBean> itemlist;

    public int getAllnum() {
        return allnum;
    }

    public void setAllnum(int allnum) {
        this.allnum = allnum;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getAgentuser() {
        return agentuser;
    }

    public void setAgentuser(String agentuser) {
        this.agentuser = agentuser;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public int getCoupon() {
        return coupon;
    }

    public void setCoupon(int coupon) {
        this.coupon = coupon;
    }

    public List<CouponBran> getCouponlist() {
        return couponlist;
    }

    public void setCouponlist(List<CouponBran> couponlist) {
        this.couponlist = couponlist;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public List<ItemlistBean> getItemlist() {
        return itemlist;
    }

    public void setItemlist(List<ItemlistBean> itemlist) {
        this.itemlist = itemlist;
    }

    public static class CouponBran {
        private String remark;
        private int money;

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }
    }

    public static class DataBean {
        /**
         * money : 50.00
         * itemnum : 1
         * name : 饮料
         * id : 1
         */

        private String money;
        private String itemnum;
        private String name;
        private String id;

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getItemnum() {
            return itemnum;
        }

        public void setItemnum(String itemnum) {
            this.itemnum = itemnum;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "money='" + money + '\'' +
                    ", itemnum='" + itemnum + '\'' +
                    ", name='" + name + '\'' +
                    ", id='" + id + '\'' +
                    '}';
        }
    }

    public static class ItemlistBean implements Parcelable {
        /**
         * itemname : 300ml新鲜港湾鲜磨豆奶
         * imageUrl : http://u.19diandian.com/Public/uploads/20171118/5a0fe72d51209.jpg
         * itemcount : 1
         * moneysum : 50.00
         */

        private String itemname;
        private String imageUrl;
        private String itemcount;
        private String moneysum;
        private String cid;
        private String unitprice;

        public String getUnitprice() {
            return unitprice;
        }

        public void setUnitprice(String unitprice) {
            this.unitprice = unitprice;
        }

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getItemname() {
            return itemname;
        }

        public void setItemname(String itemname) {
            this.itemname = itemname;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getItemcount() {
            return itemcount;
        }

        public void setItemcount(String itemcount) {
            this.itemcount = itemcount;
        }

        public String getMoneysum() {
            return moneysum;
        }

        public void setMoneysum(String moneysum) {
            this.moneysum = moneysum;
        }

        public ItemlistBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.itemname);
            dest.writeString(this.imageUrl);
            dest.writeString(this.itemcount);
            dest.writeString(this.moneysum);
            dest.writeString(this.cid);
        }

        protected ItemlistBean(Parcel in) {
            this.itemname = in.readString();
            this.imageUrl = in.readString();
            this.itemcount = in.readString();
            this.moneysum = in.readString();
            this.cid = in.readString();
        }

        public static final Creator<ItemlistBean> CREATOR = new Creator<ItemlistBean>() {
            @Override
            public ItemlistBean createFromParcel(Parcel source) {
                return new ItemlistBean(source);
            }

            @Override
            public ItemlistBean[] newArray(int size) {
                return new ItemlistBean[size];
            }
        };
    }
}
