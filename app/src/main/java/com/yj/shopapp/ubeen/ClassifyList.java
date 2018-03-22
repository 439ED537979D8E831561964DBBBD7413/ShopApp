package com.yj.shopapp.ubeen;

import java.util.List;

/**
 * Created by LK on 2018/1/8.
 *
 * @author LK
 */

public class ClassifyList {

    /**
     * status : 1
     * data : [{"id":"1","name":"上衣","shop_id":"1","imgurl":"http://u.19diandian.com/Public/uploads/shop/class/5a4dd10fa45e6.png"},{"id":"2","name":"下装","shop_id":"1","imgurl":"http://u.19diandian.com/Public/uploads/shop/class/5a4dd1181043f.png"}]
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
         * id : 1
         * name : 上衣
         * shop_id : 1
         * imgurl : http://u.19diandian.com/Public/uploads/shop/class/5a4dd10fa45e6.png
         */

        private String id;
        private String name;
        private String shop_id;
        private String imgurl;

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

        public String getShop_id() {
            return shop_id;
        }

        public void setShop_id(String shop_id) {
            this.shop_id = shop_id;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }
    }
}
