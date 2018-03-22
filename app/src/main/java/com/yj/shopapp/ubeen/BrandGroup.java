package com.yj.shopapp.ubeen;

import java.util.List;

/**
 * Created by LK on 2018/2/3.
 *
 * @author LK
 */

public class BrandGroup {

    /**
     * name : 一线品牌
     * list : [{"id":"1","name":"康师傅","imgurl":"/Public/uploads/brand/5a61a7aadf857.png","sort":"1"},{"id":"2","name":"东鹏","imgurl":"/Public/uploads/brand/brand.png","sort":"2"},{"id":"3","name":"洁柔","imgurl":"/Public/uploads/brand/brand.png","sort":"3"},{"id":"4","name":"清扬","imgurl":"/Public/uploads/brand/brand.png","sort":"4"}]
     */

    private String name;
    private List<ListBean> list;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * id : 1
         * name : 康师傅
         * imgurl : /Public/uploads/brand/5a61a7aadf857.png
         * sort : 1
         */

        private String id;
        private String name;
        private String imgurl;
        private String sort;
        private boolean isSort;
        private boolean isFoot;
        private String gid;
        private int position;

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public String getGid() {
            return gid;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }

        public boolean isFoot() {
            return isFoot;
        }

        public void setFoot(boolean foot) {
            isFoot = foot;
        }

        public boolean isSort() {
            return isSort;
        }

        public void setSort(boolean sort) {
            isSort = sort;
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

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public ListBean() {
        }

        public ListBean(String name, boolean isSort) {
            this.name = name;
            this.isSort = isSort;
        }

        public ListBean(String name, String gid, boolean isFoot,int position) {
            this.name = name;
            this.gid = gid;
            this.isFoot = isFoot;
            this.position=position;
        }

    }

}
