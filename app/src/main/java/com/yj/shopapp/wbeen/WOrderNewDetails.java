package com.yj.shopapp.wbeen;

import java.util.List;

/**
 * Created by LK on 2018/5/21.
 *
 * @author LK
 */
public class WOrderNewDetails {
    /**
     * oid : 201804251910053524
     * sale_id : 0
     * status : 1
     * addtime : 1524656765
     * money : 671.10
     * coupon : 15
     * couponlist : [{"money":"15","remark":"副食区、调味区下单满100返15"}]
     * data : [{"money":"606.60","itemnum":"46","name":"副食区","id":"2"},{"money":"3.20","itemnum":"1","name":"日化","id":"3"},{"money":"16.50","itemnum":"1","name":"纸巾","id":"4"},{"money":"4.80","itemnum":"1","name":"调味区","id":"36"},{"money":"40.00","itemnum":"1","name":"槟榔","id":"37"}]
     * driver_info :
     * allnum : 50
     * oosmoney : 886
     * oosnum : 41
     * itemlist : [{"itemname":"16g椒骄麻辣鱼仔","imageUrl":"http://u.19diandian.com/Public/uploads/20170527/xt_17130929230519ac47b2c9a8105b6729.jpg","unitprice":"21.00","itemcount":"20","moneysum":"420.00","oos":"19","oosmoney":380,"cid":"2"},{"itemname":"23g凯兴麻辣风暴蓝色地带1*","imageUrl":"http://u.19diandian.com/Public/uploads/20170526/xt_a6acb2484e7f84e918124874a0f29a9e.jpg","unitprice":"9.90","itemcount":"1","moneysum":"9.90","oos":"0","oosmoney":0,"cid":"2"},{"itemname":"280g味芭蕾香辣脆脆鱼(新)","imageUrl":"http://u.19diandian.com/Public/uploads/1/15a2b1753bb421506bcff73763344fec.jpg","unitprice":"7.00","itemcount":"23","moneysum":"161.00","oos":"22","oosmoney":506,"cid":"2"},{"itemname":"23g凯兴麻辣风暴柠檬片1*1","imageUrl":"http://u.19diandian.com/Public/uploads/20170527/xt_59293fd617bd9.jpg","unitprice":"9.90","itemcount":"1","moneysum":"9.90","oos":"0","oosmoney":0,"cid":"2"},{"itemname":"60g金里桥香脆开心果","imageUrl":"http://u.19diandian.com/Public/uploads/20170526/xt_7065155cac42af076ad1725a02bedcab.jpg","unitprice":"5.80","itemcount":"1","moneysum":"5.80","oos":"0","oosmoney":0,"cid":"2"},{"itemname":"125ml特泉老牌十全酒","imageUrl":"http://u.19diandian.com/Public/uploads/20170527/xt_69860df3172c3bc53e688ec39eabb95d.jpg","unitprice":"3.20","itemcount":"1","moneysum":"3.20","oos":"0","oosmoney":0,"cid":"3"},{"itemname":"维达8包装倍韧s装抽纸","imageUrl":"http://u.19diandian.com/Public/uploads/20170604/xt_5933aae88af72.png","unitprice":"16.50","itemcount":"1","moneysum":"16.50","oos":"0","oosmoney":0,"cid":"4"},{"itemname":"318g福香纯食用调和油","imageUrl":"http://u.19diandian.com/Public/uploads/20170527/xt_79ed378d09df439f79490ef1f7b8e946.jpg","unitprice":"4.80","itemcount":"1","moneysum":"4.80","oos":"0","oosmoney":0,"cid":"36"},{"itemname":"20g雄究究槟榔","imageUrl":"http://u.19diandian.com/Public/uploads/1/c6b968eb49cf01832161bc667933c49d.jpg","unitprice":"40.00","itemcount":"1","moneysum":"40.00","oos":"0","oosmoney":0,"cid":"37"}]
     * oosdata : [{"name":"","itemnumber":"","unit":"","unitprice":"","num":"","money":""}]
     */

    private String oid;
    private String sale_id;
    private String status;
    private String addtime;
    private String money;
    private String coupon;
    private driverInfo driver_info;
    private int allnum;
    private String oosmoney;
    private int oosnum;
    private List<CouponlistBean> couponlist;
    private List<DataBean> data;
    private List<ItemlistBean> itemlist;
    private List<OosdataBean> oosdata;
    private Address address;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

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

    public driverInfo getDriver_info() {
        return driver_info;
    }

    public void setDriver_info(driverInfo driver_info) {
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

    public List<OosdataBean> getOosdata() {
        return oosdata;
    }

    public void setOosdata(List<OosdataBean> oosdata) {
        this.oosdata = oosdata;
    }

    public static class Address {
        private String shopname;
        private String contacts;
        private String mobile;
        private String address;

        public String getShopname() {
            return shopname == null ? "" : shopname;
        }

        public void setShopname(String shopname) {
            this.shopname = shopname;
        }

        public String getContacts() {
            return contacts == null ? "" : contacts;
        }

        public void setContacts(String contacts) {
            this.contacts = contacts;
        }

        public String getMobile() {
            return mobile == null ? "" : mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getAddress() {
            return address == null ? "" : address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }

    public static class driverInfo {
        private String name;
        private String tel;

        public String getName() {
            return name == null ? "" : name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTel() {
            return tel == null ? "" : tel;
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
         * money : 606.60
         * itemnum : 46
         * name : 副食区
         * id : 2
         */

        private String money;
        private String itemnum;
        private String name;
        private String id;
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
    }

    public static class ItemlistBean {
        /**
         * itemname : 16g椒骄麻辣鱼仔
         * imageUrl : http://u.19diandian.com/Public/uploads/20170527/xt_17130929230519ac47b2c9a8105b6729.jpg
         * unitprice : 21.00
         * itemcount : 20
         * moneysum : 420.00
         * oos : 19
         * oosmoney : 380
         * cid : 2
         */

        private String itemname;
        private String imageUrl;
        private String unitprice;
        private String itemcount;
        private String moneysum;
        private String oos;
        private int oosmoney;
        private String cid;
        private String name;
        private int position;
        private String unit;

        public String getUnit() {
            return unit == null ? "" : unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public String getName() {
            return name == null ? "" : name;
        }

        public void setName(String name) {
            this.name = name;
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
    }

    public static class OosdataBean {
        /**
         * name :
         * itemnumber :
         * unit :
         * unitprice :
         * num :
         * money :
         */

        private String name;
        private String itemnumber;
        private String unit;
        private String unitprice;
        private String num;
        private int money;

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

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
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

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }
    }
}
