package com.yj.shopapp.wbeen;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by LK on 2018/5/21.
 *
 * @author LK
 */
public class WNewOrder {

    /**
     * id : 201805121610297544
     * money : 1.80
     * order : 201805121610297524
     * addtime : 1526114479
     * address : {"shopname":"土豆","contacts":"土豆","mobile":"18888888888","address":"土豆"}
     * classlist : [{"money":"1.80","name":"副食区","imgurl":"http://u.19diandian.com/Public/uploads/classify/5ac8802bbb090.png","num":"1"}]
     */

    private String id;
    private String money;
    private String order;
    private String addtime;
    private String status;
    private AddressBean address;
    private List<ClasslistBean> classlist;
    private String num;
    private String sale_id;
    /**
     * num : 5
     * iteminfo : {"name":"250ML东鹏特饮","itemnumber":"6934502300709","specs":"1*24","unit":"件","unitprice":"58.00","itemcount":"5","moneysum":"290.00","imgid":"37418","imgurl":"http://u.19diandian.com/Public/img/dpshow.gif"}
     */

    @SerializedName("num")
    private int numX;
    private IteminfoBean iteminfo;

    public String getSale_id() {
        return sale_id == null ? "" : sale_id;
    }

    public void setSale_id(String sale_id) {
        this.sale_id = sale_id;
    }

    public String getNum() {
        return num == null ? "" : num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getStatus() {
        return status == null ? "" : status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public AddressBean getAddress() {
        return address;
    }

    public void setAddress(AddressBean address) {
        this.address = address;
    }

    public List<ClasslistBean> getClasslist() {
        return classlist;
    }

    public void setClasslist(List<ClasslistBean> classlist) {
        this.classlist = classlist;
    }

    public int getNumX() {
        return numX;
    }

    public void setNumX(int numX) {
        this.numX = numX;
    }

    public IteminfoBean getIteminfo() {
        return iteminfo;
    }

    public void setIteminfo(IteminfoBean iteminfo) {
        this.iteminfo = iteminfo;
    }

    public static class AddressBean {
        /**
         * shopname : 土豆
         * contacts : 土豆
         * mobile : 18888888888
         * address : 土豆
         */

        private String shopname;
        private String contacts;
        private String mobile;
        private String address;

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
    }

    public static class ClasslistBean {
        /**
         * money : 1.80
         * name : 副食区
         * imgurl : http://u.19diandian.com/Public/uploads/classify/5ac8802bbb090.png
         * num : 1
         */

        private String money;
        private String name;
        private String imgurl;
        private String num;

        public String getMoney() {
            return money == null ? "" : money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getName() {
            return name == null ? "" : name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImgurl() {
            return imgurl == null ? "" : imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public String getNum() {
            return num == null ? "" : num;
        }

        public void setNum(String num) {
            this.num = num;
        }
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
         * imgurl : http://u.19diandian.com/Public/img/dpshow.gif
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
}
