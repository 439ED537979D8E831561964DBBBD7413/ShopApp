package com.yj.shopapp.ubeen;

import java.util.List;

/**
 * Created by LK on 2018/8/10.
 *
 * @author LK
 */
public class ShopList {


    /**
     * money : 0.12
     * lists : [{"uid":"2539","profit":"0.00","shopname":"测试6","username":"13800000006","addtime":"1534906779","num":"0"},{"uid":"2538","profit":"0.00","shopname":"测试5","username":"13800000005","addtime":"1534906769","num":"0"},{"uid":"2537","profit":"0.00","shopname":"测试4","username":"13800000004","addtime":"1534906671","num":"0"},{"uid":"2536","profit":"0.00","shopname":"测试3","username":"13800000003","addtime":"1534906661","num":"0"},{"uid":"2535","profit":"0.00","shopname":"测试2","username":"13800000002","addtime":"1534906611","num":"0"},{"uid":"2534","profit":"0.12","shopname":"测试1","username":"13800000001","addtime":"1534906566","num":"1"},{"uid":"1339","profit":"0.00","shopname":"测试","username":"15573462029","addtime":"1524472790","num":"0"}]
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
         * uid : 2539
         * profit : 0.00
         * shopname : 测试6
         * username : 13800000006
         * addtime : 1534906779
         * num : 0
         */

        private String uid;
        private String profit;
        private String shopname;
        private String username;
        private String addtime;
        private String num;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
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

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }
    }
}
