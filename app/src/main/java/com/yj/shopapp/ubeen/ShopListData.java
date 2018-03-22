package com.yj.shopapp.ubeen;

import java.util.List;

/**
 * Created by LK on 2018/1/8.
 *
 * @author LK
 */

public class ShopListData {

    /**
     * status : 1
     * data : [{"id":"2","name":"小米mix","itemnumber":"123456","specs":"S,M,L","unit":"件","price":"30.00","sales_price":"28.00","message":"","imgurl":"http://u.19diandian.com/Public/uploads/shop/goods/5a4e10a94fd58.jpg","brand":"李宁","shop_id":"1","type":"1"},{"id":"3","name":"小米note","itemnumber":"690002","specs":"S,M,L","unit":"件","price":"30.00","sales_price":"28.00","message":"","imgurl":"http://u.19diandian.com/Public/uploads/shop/goods/5a4e10a94fd58.jpg","brand":"李宁","shop_id":"1","type":"1"},{"id":"4","name":"红米手机","itemnumber":"690003","specs":"玫瑰金","unit":"部","price":"699.00","sales_price":"599.00","message":"","imgurl":"http://u.19diandian.com/Public/uploads/shop/goods/5a4e10a94fd58.jpg","brand":"小米","shop_id":"1","type":"1"}]
     */

    private int status;
    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 2
         * name : 小米mix
         * itemnumber : 123456
         * specs : S,M,L
         * unit : 件
         * price : 30.00
         * sales_price : 28.00
         * message :
         * imgurl : http://u.19diandian.com/Public/uploads/shop/goods/5a4e10a94fd58.jpg
         * brand : 李宁
         * shop_id : 1
         * type : 1
         */

        private String id;
        private String name;
        private String itemnumber;
        private String specs;
        private String unit;
        private String price;
        private String sales_price;
        private String message;
        private String imgurl;
        private String brand;
        private String shop_id;
        private String type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getSales_price() {
            return sales_price;
        }

        public void setSales_price(String sales_price) {
            this.sales_price = sales_price;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getShop_id() {
            return shop_id;
        }

        public void setShop_id(String shop_id) {
            this.shop_id = shop_id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
