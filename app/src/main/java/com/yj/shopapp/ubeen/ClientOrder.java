package com.yj.shopapp.ubeen;

import java.util.List;

/**
 * Created by LK on 2018/8/22.
 *
 * @author LK
 */
public class ClientOrder {

    /**
     * money : 0.12
     * lists : [{"id":"19274","mainorder":"201808240952569853","profit":"0.12","shopname":"测试1","time":"1535075846"}]
     */

    private String money;
    private List<ListsBean> lists;

    public String getMoney() {
        return money == null ? "" : money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public List<ListsBean> getLists() {
        return lists;
    }

    public void setLists(List<ListsBean> lists) {
        this.lists = lists;
    }

    public static class ListsBean {
        /**
         * id : 19274
         * mainorder : 201808240952569853
         * profit : 0.12
         * shopname : 测试1
         * time : 1535075846
         */

        private String id;
        private String mainorder;
        private String profit;
        private String shopname;
        private String time;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMainorder() {
            return mainorder;
        }

        public void setMainorder(String mainorder) {
            this.mainorder = mainorder;
        }

        public String getProfit() {
            return profit;
        }

        public void setProfit(String profit) {
            this.profit = profit;
        }

        public String getShopname() {
            return shopname;
        }

        public void setShopname(String shopname) {
            this.shopname = shopname;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
