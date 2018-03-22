package com.yj.shopapp.ubeen;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by LK on 2018/1/9.
 *
 * @author LK
 */

public class GoodData {

    /**
     * status : 1
     * data : {"id":"2","name":"小米mix","itemnumber":"123456","specs":["S","M","L"],"itemsum":"100","unit":"件","price":"30.00","sales_price":"28.00","message":"","imgurl":["http://u.19diandian.com/Public/uploads/shop/goods/5a4e10a94fd58.jpg","http://u.19diandian.com/Public/uploads/shop/goods/5a4e10a9510e0.jpg","http://u.19diandian.com/Public/uploads/shop/goods/5a4e10a9518b0.jpg"],"brand":"李宁","details":"<p><img src=\"http://img.19diandian.com/Public/conimg/20180105/15151211992236.jpg\" _src=\"http://img.19diandian.com/Public/conimg/20180105/15151211992236.jpg\" style=\"\"/><\/p><p><img src=\"http://img.19diandian.com/Public/conimg/20180105/1515121204153.jpg\" _src=\"http://img.19diandian.com/Public/conimg/20180105/1515121204153.jpg\" style=\"\"/><\/p><p><img src=\"http://img.19diandian.com/Public/conimg/20180105/15151212117233.jpg\" _src=\"http://img.19diandian.com/Public/conimg/20180105/15151212117233.jpg\" style=\"\"/><\/p><p><br/><\/p>","class_id":"2","shop_id":"1","type":"1"}
     */

    private int status;
    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Parcelable {
        /**
         * id : 2
         * name : 小米mix
         * itemnumber : 123456
         * specs : ["S","M","L"]
         * itemsum : 100
         * unit : 件
         * price : 30.00
         * sales_price : 28.00
         * message :
         * imgurl : ["http://u.19diandian.com/Public/uploads/shop/goods/5a4e10a94fd58.jpg","http://u.19diandian.com/Public/uploads/shop/goods/5a4e10a9510e0.jpg","http://u.19diandian.com/Public/uploads/shop/goods/5a4e10a9518b0.jpg"]
         * brand : 李宁
         * details : <p><img src="http://img.19diandian.com/Public/conimg/20180105/15151211992236.jpg" _src="http://img.19diandian.com/Public/conimg/20180105/15151211992236.jpg" style=""/></p><p><img src="http://img.19diandian.com/Public/conimg/20180105/1515121204153.jpg" _src="http://img.19diandian.com/Public/conimg/20180105/1515121204153.jpg" style=""/></p><p><img src="http://img.19diandian.com/Public/conimg/20180105/15151212117233.jpg" _src="http://img.19diandian.com/Public/conimg/20180105/15151212117233.jpg" style=""/></p><p><br/></p>
         * class_id : 2
         * shop_id : 1
         * type : 1
         */

        private String id;
        private String name;
        private String itemnumber;
        private String itemsum;
        private String unit;
        private String price;
        private String sales_price;
        private String message;
        private String brand;
        private String details;
        private String class_id;
        private String shop_id;
        private String type;
        private List<String> specs;
        private List<String> imgurl;

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

        public String getItemsum() {
            return itemsum;
        }

        public void setItemsum(String itemsum) {
            this.itemsum = itemsum;
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

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        public String getClass_id() {
            return class_id;
        }

        public void setClass_id(String class_id) {
            this.class_id = class_id;
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

        public List<String> getSpecs() {
            return specs;
        }

        public void setSpecs(List<String> specs) {
            this.specs = specs;
        }

        public List<String> getImgurl() {
            return imgurl;
        }

        public void setImgurl(List<String> imgurl) {
            this.imgurl = imgurl;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.name);
            dest.writeString(this.itemnumber);
            dest.writeString(this.itemsum);
            dest.writeString(this.unit);
            dest.writeString(this.price);
            dest.writeString(this.sales_price);
            dest.writeString(this.message);
            dest.writeString(this.brand);
            dest.writeString(this.details);
            dest.writeString(this.class_id);
            dest.writeString(this.shop_id);
            dest.writeString(this.type);
            dest.writeStringList(this.specs);
            dest.writeStringList(this.imgurl);
        }

        public DataBean() {
        }

        protected DataBean(Parcel in) {
            this.id = in.readString();
            this.name = in.readString();
            this.itemnumber = in.readString();
            this.itemsum = in.readString();
            this.unit = in.readString();
            this.price = in.readString();
            this.sales_price = in.readString();
            this.message = in.readString();
            this.brand = in.readString();
            this.details = in.readString();
            this.class_id = in.readString();
            this.shop_id = in.readString();
            this.type = in.readString();
            this.specs = in.createStringArrayList();
            this.imgurl = in.createStringArrayList();
        }

        public static final Parcelable.Creator<DataBean> CREATOR = new Parcelable.Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel source) {
                return new DataBean(source);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };
    }
}
