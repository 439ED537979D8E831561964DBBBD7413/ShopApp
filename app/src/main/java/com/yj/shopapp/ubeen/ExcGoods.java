package com.yj.shopapp.ubeen;

import java.util.List;

/**
 * Created by LK on 2017/12/25.
 *
 * @author LK
 */

public class ExcGoods {

    /**
     * status : 1
     * data : [{"id":"2","name":"测试","integral":"500","num":"20","imgurl":"http://u.19diandian.com/Public/uploads/goods/5a38e07b67b61.jpg"},{"id":"1","name":"万年历","integral":"100","num":"0","imgurl":" http://u.19diandian.com/Public/uploads/goods/5a38e07b67b61.jpg "}]
     * info : 获取成功
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
         * id : 2
         * name : 测试
         * integral : 500
         * num : 20
         * imgurl : http://u.19diandian.com/Public/uploads/goods/5a38e07b67b61.jpg
         */

        private String id;
        private String name;
        private String integral;
        private String num;
        private String imgurl;
        private String details;

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

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

        public String getIntegral() {
            return integral;
        }

        public void setIntegral(String integral) {
            this.integral = integral;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }
    }
}
