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
     * oid : 201804161851519957
     * sale_id : 0
     * status : 2
     * addtime : 1523875827
     * agentuser : 多个批发商
     * money : 525.50
     * coupon : 15
     * couponlist : [{"money":"15","remark":"副食区、调味区下单满100返15"}]
     * data : [{"money":"38.00","itemnum":"1","name":"饮料","id":"1","url":"/Public/uploads/classify/5ac88033d4ccf.png","imgurl":"http://u.19diandian.com/Public/uploads/classify/5ac88033d4ccf.png"},{"money":"350.00","itemnum":"7","name":"副食区","id":"2","url":"/Public/uploads/classify/5ac8802bbb090.png","imgurl":"http://u.19diandian.com/Public/uploads/classify/5ac8802bbb090.png"},{"money":"1.00","itemnum":"1","name":"日化","id":"3","url":"/Public/uploads/classify/5ac8803f4a26f.png","imgurl":"http://u.19diandian.com/Public/uploads/classify/5ac8803f4a26f.png"},{"money":"16.50","itemnum":"1","name":"纸巾","id":"4","url":"/Public/uploads/classify/5ac8804cbf07f.png","imgurl":"http://u.19diandian.com/Public/uploads/classify/5ac8804cbf07f.png"},{"money":"120.00","itemnum":"10","name":"调味区","id":"36","url":"/Public/uploads/classify/5a09726762bf7.jpg","imgurl":"http://u.19diandian.com/Public/uploads/classify/5a09726762bf7.jpg"}]
     * driver_info : {"name":"龙小志","tel":"13922920963"}
     * allnum : 20
     * oosmoney : 90
     * oosnum : 9
     * itemlist : [{"itemname":"新鲜港湾豆奶330ml","imageUrl":"http://u.19diandian.com/Public/uploads/20171104/59fd8f44e7a13.jpg","unitprice":"38.00","itemcount":"1","moneysum":"38.00","oos":"0","oosmoney":0,"cid":"1"},{"itemname":"111111","imageUrl":"http://u.19diandian.com/Public/uploads/20170912/b5b2fe7eb934e1e89fd7ac11b7ac3f98.jpg","unitprice":"1.00","itemcount":"1","moneysum":"1.00","oos":"0","oosmoney":0,"cid":"3"},{"itemname":"880g福香纯食用调和油","imageUrl":"http://u.19diandian.com/Public/uploads/20170527/xt_b914d351793f212b4046e20f599bf16f.jpg","unitprice":"12.00","itemcount":"10","moneysum":"120.00","oos":"9","oosmoney":90,"cid":"36"},{"itemname":"维达8包装倍韧s装抽纸","imageUrl":"http://u.19diandian.com/Public/uploads/20170604/xt_5933aae88af72.png","unitprice":"16.50","itemcount":"1","moneysum":"16.50","oos":"0","oosmoney":0,"cid":"4"},{"itemname":"450ml 蓝色可乐","imageUrl":"http://u.19diandian.com/Public/uploads/20180325/5ab776494ae40.jpg","unitprice":"50.00","itemcount":"7","moneysum":"350.00","oos":"0","oosmoney":0,"cid":"2"}]
     */

    private String oid;
    private String sale_id;
    private String status;
    private String addtime;
    private String agentuser;
    private String money;
    private String coupon;
    private DriverInfoBean driver_info;
    private int allnum;
    private String oosmoney;
    private int oosnum;
    private List<CouponlistBean> couponlist;
    private List<DataBean> data;
    private List<ItemlistBean> itemlist;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getSale_id() {
        return sale_id;
    }

    public void setSale_id(String sale_id) {
        this.sale_id = sale_id;
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

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public DriverInfoBean getDriver_info() {
        return driver_info;
    }

    public void setDriver_info(DriverInfoBean driver_info) {
        this.driver_info = driver_info;
    }

    public int getAllnum() {
        return allnum;
    }

    public void setAllnum(int allnum) {
        this.allnum = allnum;
    }

    public String getOosmoney() {
        return oosmoney;
    }

    public void setOosmoney(String oosmoney) {
        this.oosmoney = oosmoney;
    }

    public int getOosnum() {
        return oosnum;
    }

    public void setOosnum(int oosnum) {
        this.oosnum = oosnum;
    }

    public List<CouponlistBean> getCouponlist() {
        return couponlist;
    }

    public void setCouponlist(List<CouponlistBean> couponlist) {
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

    public static class DriverInfoBean {
        /**
         * name : 龙小志
         * tel : 13922920963
         */

        private String name;
        private String tel;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }
    }

    public static class CouponlistBean {
        /**
         * money : 15
         * remark : 副食区、调味区下单满100返15
         */

        private String money;
        private String remark;

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }

    public static class DataBean {
        /**
         * money : 38.00
         * itemnum : 1
         * name : 饮料
         * id : 1
         * url : /Public/uploads/classify/5ac88033d4ccf.png
         * imgurl : http://u.19diandian.com/Public/uploads/classify/5ac88033d4ccf.png
         */

        private String money;
        private String itemnum;
        private String name;
        private String id;
        private String url;
        private String imgurl;
        private int position;

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

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

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }


        public DataBean() {
        }

        public DataBean(String id) {
            this.id = id;
        }
    }

    public static class ItemlistBean implements Parcelable {
        /**
         * itemname : 新鲜港湾豆奶330ml
         * imageUrl : http://u.19diandian.com/Public/uploads/20171104/59fd8f44e7a13.jpg
         * unitprice : 38.00
         * itemcount : 1
         * moneysum : 38.00
         * oos : 0
         * oosmoney : 0
         * cid : 1
         */

        private String itemname;
        private String imageUrl;
        private String unitprice;
        private String itemcount;
        private String moneysum;
        private String oos;
        private int oosmoney;
        private String cid;
        private int index;
        private String classname;

        public String getClassname() {
            return classname == null ? "" : classname;
        }

        public void setClassname(String classname) {
            this.classname = classname;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
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

        public String getUnitprice() {
            return unitprice;
        }

        public void setUnitprice(String unitprice) {
            this.unitprice = unitprice;
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

        public String getOos() {
            return oos;
        }

        public void setOos(String oos) {
            this.oos = oos;
        }

        public int getOosmoney() {
            return oosmoney;
        }

        public void setOosmoney(int oosmoney) {
            this.oosmoney = oosmoney;
        }

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.itemname);
            dest.writeString(this.imageUrl);
            dest.writeString(this.unitprice);
            dest.writeString(this.itemcount);
            dest.writeString(this.moneysum);
            dest.writeString(this.oos);
            dest.writeInt(this.oosmoney);
            dest.writeString(this.cid);
        }

        public ItemlistBean() {
        }

        protected ItemlistBean(Parcel in) {
            this.itemname = in.readString();
            this.imageUrl = in.readString();
            this.unitprice = in.readString();
            this.itemcount = in.readString();
            this.moneysum = in.readString();
            this.oos = in.readString();
            this.oosmoney = in.readInt();
            this.cid = in.readString();
        }

        public static final Parcelable.Creator<ItemlistBean> CREATOR = new Parcelable.Creator<ItemlistBean>() {
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
