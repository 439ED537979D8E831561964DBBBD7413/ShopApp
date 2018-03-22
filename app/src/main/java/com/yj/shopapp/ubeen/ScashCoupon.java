package com.yj.shopapp.ubeen;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huanghao on 2016/12/5.
 */

public class ScashCoupon implements Serializable {

    private List<CanuseBean> canuse;
    private List<CanuseBean> uncanuse;

    public List<CanuseBean> getCanuse() {
        return canuse;
    }

    public void setCanuse(List<CanuseBean> canuse) {
        this.canuse = canuse;
    }

    public List<CanuseBean> getUncanuse() {
        return uncanuse;
    }

    public void setUncanuse(List<CanuseBean> uncanuse) {
        this.uncanuse = uncanuse;
    }

    public static class CanuseBean implements Serializable {
        /**
         * id : 33
         * money : 10.00
         * starttime : 2016-11-29
         * endtime : 2016-12-03
         * bigtypeid : 18
         * available_money : 20.00
         * shopname : 点点批发部
         * bigtypename : 钓鱼用品
         */

        private String id;
        private String money;
        private String starttime;
        private String endtime;
        private String bigtypeid;
        private String available_money;
        private String shopname;
        private String bigtypename;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getStarttime() {
            return starttime;
        }

        public void setStarttime(String starttime) {
            this.starttime = starttime;
        }

        public String getEndtime() {
            return endtime;
        }

        public void setEndtime(String endtime) {
            this.endtime = endtime;
        }

        public String getBigtypeid() {
            return bigtypeid;
        }

        public void setBigtypeid(String bigtypeid) {
            this.bigtypeid = bigtypeid;
        }

        public String getAvailable_money() {
            return available_money;
        }

        public void setAvailable_money(String available_money) {
            this.available_money = available_money;
        }

        public String getShopname() {
            return shopname;
        }

        public void setShopname(String shopname) {
            this.shopname = shopname;
        }

        public String getBigtypename() {
            return bigtypename;
        }

        public void setBigtypename(String bigtypename) {
            this.bigtypename = bigtypename;
        }
    }


}
