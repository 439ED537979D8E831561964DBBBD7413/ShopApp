package com.yj.shopapp.ubeen;

import java.util.List;

/**
 * Created by LK on 2018/1/24.
 *
 * @author LK
 */

public class ShopCase {

    /**
     * status : 1
     * data : [{"id":"3","title":"服装店展示","shop_id":"4","imgurl":"http://u.19diandian.com/Public/uploads/shop/case/5a66ace97e2d4.jpg","details":"<p><img src=\"http://img.19diandian.com/Public/conimg/20180123/15166783752535.jpg\" _src=\"http://img.19diandian.com/Public/conimg/20180123/15166783752535.jpg\"/><\/p>"}]
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
         * title : 服装店展示
         * shop_id : 4
         * imgurl : http://u.19diandian.com/Public/uploads/shop/case/5a66ace97e2d4.jpg
         * details : <p><img src="http://img.19diandian.com/Public/conimg/20180123/15166783752535.jpg" _src="http://img.19diandian.com/Public/conimg/20180123/15166783752535.jpg"/></p>
         */

        private String id;
        private String title;
        private String shop_id;
        private String imgurl;
        private String details;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
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

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }
    }
}
