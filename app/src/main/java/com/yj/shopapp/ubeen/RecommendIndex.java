package com.yj.shopapp.ubeen;

import java.util.List;

/**
 * Created by LK on 2018/8/9.
 *
 * @author LK
 */
public class RecommendIndex {

    /**
     * is_open : 1
     * info : 推荐功能已关闭，请等待开放！
     * rules : 1.你所推荐的客户每下一笔订单，你均会获得订单中所属行业（副食、调味、白酒）商品金额的0.1%奖励，上不封顶。
     2.推荐奖励以月结方式发放，每月3日奖励将会以红包的形式发送至您的微信或支付包账号上。
     * num : 1
     * allprofit : 0.00
     * monthprofit : 0.00
     * list : [{"id":1,"shopname":"店名1","username":"13662829001","mprofit":12,"time":1533817033,"contents":"副食:10元"},{"id":2,"shopname":"店名2","username":"13662829002","mprofit":12,"time":1533817033,"contents":"副食:10元"},{"id":3,"shopname":"店名3","username":"13662829003","mprofit":12,"time":1533817033,"contents":"副食:10元"},{"id":4,"shopname":"店名4","username":"13662829004","mprofit":12,"time":1533817033,"contents":"副食:10元"},{"id":5,"shopname":"店名5","username":"13662829005","mprofit":12,"time":1533817033,"contents":"副食:10元"},{"id":6,"shopname":"店名6","username":"13662829006","mprofit":12,"time":1533817033,"contents":"副食:10元"},{"id":7,"shopname":"店名7","username":"13662829007","mprofit":12,"time":1533817033,"contents":"副食:10元"},{"id":8,"shopname":"店名8","username":"13662829008","mprofit":12,"time":1533817033,"contents":"副食:10元"},{"id":9,"shopname":"店名9","username":"13662829009","mprofit":12,"time":1533817033,"contents":"副食:10元"},{"id":10,"shopname":"店名10","username":"136628290010","mprofit":12,"time":1533817033,"contents":"副食:10元"}]
     */

    private int is_open;
    private String info;
    private String rules;
    private int num;
    private String allprofit;
    private String monthprofit;
    private List<ListBean> list;

    public int getIs_open() {
        return is_open;
    }

    public void setIs_open(int is_open) {
        this.is_open = is_open;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getAllprofit() {
        return allprofit;
    }

    public void setAllprofit(String allprofit) {
        this.allprofit = allprofit;
    }

    public String getMonthprofit() {
        return monthprofit;
    }

    public void setMonthprofit(String monthprofit) {
        this.monthprofit = monthprofit;
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
         * shopname : 店名1
         * username : 13662829001
         * mprofit : 12
         * time : 1533817033
         * contents : 副食:10元
         */

        private int id;
        private String shopname;
        private String username;
        private String mprofit;
        private int time;
        private String contents;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public String getMprofit() {
            return mprofit;
        }

        public void setMprofit(String mprofit) {
            this.mprofit = mprofit;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public String getContents() {
            return contents;
        }

        public void setContents(String contents) {
            this.contents = contents;
        }
    }
}
