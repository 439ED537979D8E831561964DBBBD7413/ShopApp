package com.yj.shopapp.ubeen;

import java.util.List;

/**
 * Created by LK on 2018/1/8.
 *
 * @author LK
 */

public class AdvMap {


    /**
     * status : 1
     * data : [{"id":"2","imgurl":"http://u.19diandian.com/Public/uploads/shop/advmap/5a506bd8c4f8a.jpg","title":"石排服装店","type":"1","shop_id":"5","shop_info":{"id":"5","shopname":"小米手机专卖店","contacts":"李先生","tel":"13666666666","address":"石排镇东莞市","shopdetails":"<p><img src=\"http://img.19diandian.com/Public/conimg/20180108/15153786909792.jpg\" _src=\"http://img.19diandian.com/Public/conimg/20180108/15153786909792.jpg\" style=\"\"/><\/p><p><img src=\"http://img.19diandian.com/Public/conimg/20180108/15153786964079.jpg\" _src=\"http://img.19diandian.com/Public/conimg/20180108/15153786964079.jpg\" style=\"\"/><\/p><p><br/><\/p>"}},{"id":"4","imgurl":"http://u.19diandian.com/Public/uploads/shop/advmap/5a506d4b58ad8.jpg","title":"服装","type":"2","shop_id":"7","shop_info":{"id":"5","shopname":"小米手机专卖店","contacts":"李先生","tel":"13666666666","address":"石排镇东莞市","shopdetails":"<p><img src=\"http://img.19diandian.com/Public/conimg/20180108/15153786909792.jpg\" _src=\"http://img.19diandian.com/Public/conimg/20180108/15153786909792.jpg\" style=\"\"/><\/p><p><img src=\"http://img.19diandian.com/Public/conimg/20180108/15153786964079.jpg\" _src=\"http://img.19diandian.com/Public/conimg/20180108/15153786964079.jpg\" style=\"\"/><\/p><p><br/><\/p>"},"goods_info":{"id":"7","name":"小米noto","itemnumber":"690012","specs":"4GB+64GB,6GB+64GB,6GB+256GB","unit":"部","price":"1999.00","sales_price":"1899.00","message":"下单立减100元","imgurl":"http://u.19diandian.com/Public/uploads/shop/goods/5a52dd7cb7801.jpg","brand":"小米","shop_id":"5"}},{"id":"5","imgurl":"http://u.19diandian.com/Public/uploads/shop/advmap/5a66b1df2ce93.jpg","title":"服装案例测试","type":"3","shop_id":"3","shop_info":{"id":"4","shopname":"儿童服装","contacts":"李先生","tel":"13666666666","address":"东莞市石排镇","shopdetails":"<p><img src=\"http://img.19diandian.com/Public/conimg/20180104/1515036754412.gif\" _src=\"http://img.19diandian.com/Public/conimg/20180104/1515036754412.gif\"/><\/p>"},"case_info":{"id":"3","details":"<p><img src=\"http://img.19diandian.com/Public/conimg/20180123/15166783752535.jpg\" _src=\"http://img.19diandian.com/Public/conimg/20180123/15166783752535.jpg\"/><\/p>","shop_id":"4","imgurl":"http://u.19diandian.com/Public/uploads/shop/case/5a66ace97e2d4.jpg"}}]
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
         * imgurl : http://u.19diandian.com/Public/uploads/shop/advmap/5a506bd8c4f8a.jpg
         * title : 石排服装店
         * type : 1
         * shop_id : 5
         * shop_info : {"id":"5","shopname":"小米手机专卖店","contacts":"李先生","tel":"13666666666","address":"石排镇东莞市","shopdetails":"<p><img src=\"http://img.19diandian.com/Public/conimg/20180108/15153786909792.jpg\" _src=\"http://img.19diandian.com/Public/conimg/20180108/15153786909792.jpg\" style=\"\"/><\/p><p><img src=\"http://img.19diandian.com/Public/conimg/20180108/15153786964079.jpg\" _src=\"http://img.19diandian.com/Public/conimg/20180108/15153786964079.jpg\" style=\"\"/><\/p><p><br/><\/p>"}
         * goods_info : {"id":"7","name":"小米noto","itemnumber":"690012","specs":"4GB+64GB,6GB+64GB,6GB+256GB","unit":"部","price":"1999.00","sales_price":"1899.00","message":"下单立减100元","imgurl":"http://u.19diandian.com/Public/uploads/shop/goods/5a52dd7cb7801.jpg","brand":"小米","shop_id":"5"}
         * case_info : {"id":"3","details":"<p><img src=\"http://img.19diandian.com/Public/conimg/20180123/15166783752535.jpg\" _src=\"http://img.19diandian.com/Public/conimg/20180123/15166783752535.jpg\"/><\/p>","shop_id":"4","imgurl":"http://u.19diandian.com/Public/uploads/shop/case/5a66ace97e2d4.jpg"}
         */

        private String id;
        private String imgurl;
        private String title;
        private String type;
        private String shop_id;
        private ShopInfoBean shop_info;
        private GoodsInfoBean goods_info;
        private CaseInfoBean case_info;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getShop_id() {
            return shop_id;
        }

        public void setShop_id(String shop_id) {
            this.shop_id = shop_id;
        }

        public ShopInfoBean getShop_info() {
            return shop_info;
        }

        public void setShop_info(ShopInfoBean shop_info) {
            this.shop_info = shop_info;
        }

        public GoodsInfoBean getGoods_info() {
            return goods_info;
        }

        public void setGoods_info(GoodsInfoBean goods_info) {
            this.goods_info = goods_info;
        }

        public CaseInfoBean getCase_info() {
            return case_info;
        }

        public void setCase_info(CaseInfoBean case_info) {
            this.case_info = case_info;
        }

        public static class ShopInfoBean {
            /**
             * id : 5
             * shopname : 小米手机专卖店
             * contacts : 李先生
             * tel : 13666666666
             * address : 石排镇东莞市
             * shopdetails : <p><img src="http://img.19diandian.com/Public/conimg/20180108/15153786909792.jpg" _src="http://img.19diandian.com/Public/conimg/20180108/15153786909792.jpg" style=""/></p><p><img src="http://img.19diandian.com/Public/conimg/20180108/15153786964079.jpg" _src="http://img.19diandian.com/Public/conimg/20180108/15153786964079.jpg" style=""/></p><p><br/></p>
             */

            private String id;
            private String shopname;
            private String contacts;
            private String tel;
            private String address;
            private String shopdetails;

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

            public String getShopdetails() {
                return shopdetails;
            }

            public void setShopdetails(String shopdetails) {
                this.shopdetails = shopdetails;
            }
        }

        public static class GoodsInfoBean {
            /**
             * id : 7
             * name : 小米noto
             * itemnumber : 690012
             * specs : 4GB+64GB,6GB+64GB,6GB+256GB
             * unit : 部
             * price : 1999.00
             * sales_price : 1899.00
             * message : 下单立减100元
             * imgurl : http://u.19diandian.com/Public/uploads/shop/goods/5a52dd7cb7801.jpg
             * brand : 小米
             * shop_id : 5
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
        }

        public static class CaseInfoBean {
            /**
             * id : 3
             * details : <p><img src="http://img.19diandian.com/Public/conimg/20180123/15166783752535.jpg" _src="http://img.19diandian.com/Public/conimg/20180123/15166783752535.jpg"/></p>
             * shop_id : 4
             * imgurl : http://u.19diandian.com/Public/uploads/shop/case/5a66ace97e2d4.jpg
             */

            private String id;
            private String details;
            private String shop_id;
            private String imgurl;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getDetails() {
                return details;
            }

            public void setDetails(String details) {
                this.details = details;
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
}
