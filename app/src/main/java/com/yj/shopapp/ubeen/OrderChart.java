package com.yj.shopapp.ubeen;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by LK on 2018/3/16.
 *
 * @author LK
 */

public class OrderChart {

    /**
     * order_num : 1
     * allcount : 3
     * allmoney : 91.5
     * list : [{"class":"副食区","count":3,"money":"91.5","cid":"2"}]
     */

    private int order_num;
    private int allcount;
    private String allmoney;
    private List<ListBean> list;

    public int getOrder_num() {
        return order_num;
    }

    public void setOrder_num(int order_num) {
        this.order_num = order_num;
    }

    public int getAllcount() {
        return allcount;
    }

    public void setAllcount(int allcount) {
        this.allcount = allcount;
    }

    public String getAllmoney() {
        return allmoney;
    }

    public void setAllmoney(String allmoney) {
        this.allmoney = allmoney;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * class : 副食区
         * count : 3
         * money : 91.5
         * cid : 2
         */

        @SerializedName("class")
        @JSONField(name = "class")
        private String classX;
        private int count;
        private String money;
        private String cid;
        private String Percentage;

        public String getPercentage() {
            return Percentage;
        }

        public void setPercentage(String percentage) {
            Percentage = percentage;
        }

        public String getClassX() {
            return classX;
        }

        public void setClassX(String classX) {
            this.classX = classX;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }
    }
}
