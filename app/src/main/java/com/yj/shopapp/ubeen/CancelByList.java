package com.yj.shopapp.ubeen;

import java.util.List;

/**
 * Created by LK on 2018/8/16.
 *
 * @author LK
 */
public class CancelByList {

    /**
     * status : 1
     * lists : [{"name":"450ml 蓝色可乐这个商品的名称不是一般的长很长测试","itemnumber":"6971163470105？","imgid":"66766","specs":"1*15","unit":"件","id":"4","addtime":"1534410167","itemcount":"5","unitprice":"50.00","money":"250.00","imgurl":"http://u.19diandian.com/Public/uploads/20180615/5b2339d0c9238.jpg"}]
     * info : 查询成功!
     */

    private int status;
    private String info;
    private List<ListsBean> lists;

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

    public List<ListsBean> getLists() {
        return lists;
    }

    public void setLists(List<ListsBean> lists) {
        this.lists = lists;
    }

    public static class ListsBean {
        /**
         * name : 450ml 蓝色可乐这个商品的名称不是一般的长很长测试
         * itemnumber : 6971163470105？
         * imgid : 66766
         * specs : 1*15
         * unit : 件
         * id : 4
         * addtime : 1534410167
         * itemcount : 5
         * unitprice : 50.00
         * money : 250.00
         * imgurl : http://u.19diandian.com/Public/uploads/20180615/5b2339d0c9238.jpg
         */

        private String name;
        private String itemnumber;
        private String imgid;
        private String specs;
        private String unit;
        private String id;
        private String addtime;
        private String itemcount;
        private String unitprice;
        private String money;
        private String imgurl;

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

        public String getImgid() {
            return imgid;
        }

        public void setImgid(String imgid) {
            this.imgid = imgid;
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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getItemcount() {
            return itemcount;
        }

        public void setItemcount(String itemcount) {
            this.itemcount = itemcount;
        }

        public String getUnitprice() {
            return unitprice;
        }

        public void setUnitprice(String unitprice) {
            this.unitprice = unitprice;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }
    }
}
