package com.yj.shopapp.ubeen;

import java.util.List;

/**
 * Created by LK on 2018/1/8.
 *
 * @author LK
 */

public class Store {


    /**
     * status : 1
     * data : [{"id":"3","shopname":"服装店","contacts":"李先生","tel":"13666666666","address":"东莞市","imgurl":"http://u.19diandian.com/Public/uploads/shop/5a506b79bd98a.jpg"},{"id":"4","shopname":"儿童服装","contacts":"李先生","tel":"13666666666","address":"东莞市石排镇","imgurl":"http://u.19diandian.com/Public/uploads/shop/5a4da054743aa.jpg"},{"id":"5","shopname":"小米手机专卖店","contacts":"李先生","tel":"13666666666","address":"石排镇东莞市","imgurl":"http://u.19diandian.com/Public/uploads/shop/5a52d80cd11a8.jpg"}]
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
         * id : 3
         * shopname : 服装店
         * contacts : 李先生
         * tel : 13666666666
         * address : 东莞市
         * imgurl : http://u.19diandian.com/Public/uploads/shop/5a506b79bd98a.jpg
         */

        private String id;
        private String shopname;
        private String contacts;
        private String tel;
        private String address;
        private String imgurl;
        private int type;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

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

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }
    }
}
