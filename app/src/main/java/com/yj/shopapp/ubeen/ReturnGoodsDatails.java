package com.yj.shopapp.ubeen;

import java.util.List;

/**
 * Created by LK on 2018/6/4.
 *
 * @author LK
 */
public class ReturnGoodsDatails {

    /**
     * info : {"name":"*250ML东鹏特饮","itemnumber":"6934502300709","imgurl":"http://u.19diandian.com/Public/uploads/20180515/5afa7da66e148.jpg","unit":"件","specs":"1*24","num":"1","money":"58.00","stock_num":"5","sock_money":"290.00"}
     * list : [{"price":"58.00","num":"1","money":"58.00","order":"201806032048495554"}]
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
         * name : *250ML东鹏特饮
         * itemnumber : 6934502300709
         * imgurl : http://u.19diandian.com/Public/uploads/20180515/5afa7da66e148.jpg
         * unit : 件
         * specs : 1*24
         * num : 1
         * money : 58.00
         * stock_num : 5
         * sock_money : 290.00
         */

        private String name;
        private String itemnumber;
        private String imgurl;
        private String unit;
        private String specs;
        private String num;
        private String money;
        private String stock_num;
        private String sock_money;

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

        public String getStock_num() {
            return stock_num;
        }

        public void setStock_num(String stock_num) {
            this.stock_num = stock_num;
        }

        public String getSock_money() {
            return sock_money;
        }

        public void setSock_money(String sock_money) {
            this.sock_money = sock_money;
        }
    }

    public static class ListBean {
        /**
         * price : 58.00
         * num : 1
         * money : 58.00
         * order : 201806032048495554
         */

        private String price;
        private String num;
        private String money;
        private String order;
        private String addtime;

        public String getAddtime() {
            return addtime == null ? "" : addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
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

        public String getOrder() {
            return order;
        }

        public void setOrder(String order) {
            this.order = order;
        }
    }
}
