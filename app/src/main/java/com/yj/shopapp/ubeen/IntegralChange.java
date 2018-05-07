package com.yj.shopapp.ubeen;

import java.util.List;

/**
 * From ShopApp
 * Created by wxq on 17/4/5.
 * <p>
 * Beautiful Life ～
 */

public class IntegralChange {


    /**
     * status : 1
     * info : 有换购记录!
     * data : [{"id":"15","num":"2","integral":"100","sumintegral":"200","addtime":"1522295774","status":"1","imgurl":"http://u.19diandian.com/Public/uploads/goods/5a63076b7524d.jpg","name":"小米手机"},{"id":"14","num":"2","integral":"100","sumintegral":"200","addtime":"1522295771","status":"1","imgurl":"http://u.19diandian.com/Public/uploads/goods/5a630755ade0f.jpg","name":"华为手机"}]
     */

    private int status;
    private String info;
    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 15
         * num : 2
         * integral : 100
         * sumintegral : 200
         * addtime : 1522295774
         * status : 1
         * imgurl : http://u.19diandian.com/Public/uploads/goods/5a63076b7524d.jpg
         * name : 小米手机
         */

        private String id;
        private String num;
        private String integral;
        private String sumintegral;
        private String addtime;
        private String status;
        private String imgurl;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getIntegral() {
            return integral;
        }

        public void setIntegral(String integral) {
            this.integral = integral;
        }

        public String getSumintegral() {
            return sumintegral;
        }

        public void setSumintegral(String sumintegral) {
            this.sumintegral = sumintegral;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
