package com.yj.shopapp.ubeen;

import java.util.List;

/**
 * Created by LK on 2018/6/4.
 *
 * @author LK
 */
public class ReturnGoodsBean {

    /**
     * info : {"num":1,"money":58,"stock_num":0,"stock_money":0}
     * list : [{"name":"*250ML东鹏特饮","itemnumber":"6934502300709","itemid":"76102","specs":"1*24","num":"1","money":"58.00","stock_num":0,"stock_money":0,"imgurl":"http://u.19diandian.com/Public/uploads/20180515/5afa7da66e148.jpg","unit":"件"}]
     */

    private InfoBean info;
    private List<ListBean> list;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class InfoBean {
        /**
         * num : 1
         * money : 58
         * stock_num : 0
         * stock_money : 0
         */

        private int num;
        private String money;
        private String stock_num;
        private String stock_money;

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getStock_num() {
            return stock_num;
        }

        public void setStock_num(String stock_num) {
            this.stock_num = stock_num;
        }

        public String getStock_money() {
            return stock_money;
        }

        public void setStock_money(String stock_money) {
            this.stock_money = stock_money;
        }
    }

    public static class ListBean {
        /**
         * name : *250ML东鹏特饮
         * itemnumber : 6934502300709
         * itemid : 76102
         * specs : 1*24
         * num : 1
         * money : 58.00
         * stock_num : 0
         * stock_money : 0
         * imgurl : http://u.19diandian.com/Public/uploads/20180515/5afa7da66e148.jpg
         * unit : 件
         */

        private String name;
        private String itemnumber;
        private String itemid;
        private String specs;
        private String num;
        private String money;
        private int stock_num;
        private String stock_money;
        private String imgurl;
        private String unit;

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

        public String getItemid() {
            return itemid;
        }

        public void setItemid(String itemid) {
            this.itemid = itemid;
        }

        public String getSpecs() {
            return specs;
        }

        public void setSpecs(String specs) {
            this.specs = specs;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public int getStock_num() {
            return stock_num;
        }

        public void setStock_num(int stock_num) {
            this.stock_num = stock_num;
        }

        public String getStock_money() {
            return stock_money;
        }

        public void setStock_money(String stock_money) {
            this.stock_money = stock_money;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }
    }
}
