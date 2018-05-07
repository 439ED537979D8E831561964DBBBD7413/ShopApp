package com.yj.shopapp.ubeen;

import java.util.List;

/**
 * Created by LK on 2017/12/22.
 *
 * @author LK
 */

public class NewOrder {


    /**
     * oid : 201804161197995053
     * sale_id : 2
     * status : 1
     * iteminfo : {"name":"250ML东鹏特饮","itemnumber":"6934502300709","specs":"1*24","unit":"件","unitprice":"58.00","itemcount":"5","moneysum":"290.00","imgid":"37418","imgurl":"http://u.19diandian.com/Public/uploads/20170720/979b1f9df6e31b41a0b60469c1074f11.jpg"}
     * data : [{"money":"290.00","itemnum":"5","name":"饮料","id":"1","url":"/Public/uploads/classify/5ac88033d4ccf.png","imgurl":"http://u.19diandian.com/Public/uploads/classify/5ac88033d4ccf.png"}]
     * sumtype : 1
     * sumnum : 5
     * money : 290
     * addtime : 1523850314
     * coupon : 0
     * receipt : 290
     */

    private String oid;
    private String sale_id;
    private String status;
    private IteminfoBean iteminfo;
    private int sumtype;
    private int sumnum;
    private String money;
    private String addtime;
    private int coupon;
    private int receipt;
    private List<DataBean> data;

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

    public IteminfoBean getIteminfo() {
        return iteminfo;
    }

    public void setIteminfo(IteminfoBean iteminfo) {
        this.iteminfo = iteminfo;
    }

    public int getSumtype() {
        return sumtype;
    }

    public void setSumtype(int sumtype) {
        this.sumtype = sumtype;
    }

    public int getSumnum() {
        return sumnum;
    }

    public void setSumnum(int sumnum) {
        this.sumnum = sumnum;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public int getCoupon() {
        return coupon;
    }

    public void setCoupon(int coupon) {
        this.coupon = coupon;
    }

    public int getReceipt() {
        return receipt;
    }

    public void setReceipt(int receipt) {
        this.receipt = receipt;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class IteminfoBean {
        /**
         * name : 250ML东鹏特饮
         * itemnumber : 6934502300709
         * specs : 1*24
         * unit : 件
         * unitprice : 58.00
         * itemcount : 5
         * moneysum : 290.00
         * imgid : 37418
         * imgurl : http://u.19diandian.com/Public/uploads/20170720/979b1f9df6e31b41a0b60469c1074f11.jpg
         */

        private String name;
        private String itemnumber;
        private String specs;
        private String unit;
        private String unitprice;
        private String itemcount;
        private String moneysum;
        private String imgid;
        private String imgurl;

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

        public String getImgid() {
            return imgid;
        }

        public void setImgid(String imgid) {
            this.imgid = imgid;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }
    }

    public static class DataBean {
        /**
         * money : 290.00
         * itemnum : 5
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
    }
}
