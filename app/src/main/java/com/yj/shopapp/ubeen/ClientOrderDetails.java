package com.yj.shopapp.ubeen;

import java.util.List;

/**
 * Created by LK on 2018/8/22.
 *
 * @author LK
 */
public class ClientOrderDetails {

    /**
     * id : 15951
     * mainorder : 201807280950535010
     * profit : 0.00
     * time : 1533524106
     * contents : [{"name":"副食区","money":"8.00","profit":"0"}]
     */

    private String id;
    private String mainorder;
    private String profit;
    private String time;
    private List<ContentsBean> contents;

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<ContentsBean> getContents() {
        return contents;
    }

    public void setContents(List<ContentsBean> contents) {
        this.contents = contents;
    }

    public static class ContentsBean {
        /**
         * name : 副食区
         * money : 8.00
         * profit : 0
         */

        private String name;
        private String money;
        private String profit;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getProfit() {
            return profit;
        }

        public void setProfit(String profit) {
            this.profit = profit;
        }
    }
}
