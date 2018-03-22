package com.yj.shopapp.ubeen;

import java.util.List;

/**
 * From ShopApp
 * Created by wxq on 17/4/5.
 * <p>
 * Beautiful Life ～
 */

public class IntegralDetail {


    /**
     * status : 1
     * data : [{"id":"5","num":"5","integral":"100","sumintegral":"500","addtime":"1513826410","status":"1","imgurl":"http://u.19diandian.com/Public/uploads/goods/5a4b7d101c99b.jpg","name":"万年历"},{"id":"1","num":"2","integral":"2000","sumintegral":"4000","addtime":"1511111111","status":"3","imgurl":"http://u.19diandian.com/Public/uploads/goods/5a4b7d101c99b.jpg","name":"万年历"},{"id":"3","num":"2","integral":"2000","sumintegral":"4000","addtime":"1511111111","status":"2","imgurl":"http://u.19diandian.com/Public/uploads/goods/5a4b7d101c99b.jpg","name":"万年历"},{"id":"2","num":"3","integral":"1000","sumintegral":"3000","addtime":"1511111111","status":"3","imgurl":"http://u.19diandian.com/Public/uploads/goods/5a3b32f2ebd7f.png","name":"防水手表"},{"id":"4","num":"3","integral":"1000","sumintegral":"3000","addtime":"1511111111","status":"1","imgurl":"http://u.19diandian.com/Public/uploads/goods/5a3b32f2ebd7f.png","name":"防水手表"}]
     */

    private int status;
    private List<DataBean> data;
    private String info;

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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public static class DataBean {
        /**
         * id : 5
         * num : 5
         * integral : 100
         * sumintegral : 500
         * addtime : 1513826410
         * status : 1
         * imgurl : http://u.19diandian.com/Public/uploads/goods/5a4b7d101c99b.jpg
         * name : 万年历
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
