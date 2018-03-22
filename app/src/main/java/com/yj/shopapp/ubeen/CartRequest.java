package com.yj.shopapp.ubeen;

/**
 * Created by Administrator on 2016/8/29.
 */
public class CartRequest {
    /**
     * moneysum : 3.00
     * itemname : 链子银耳羹
     * totalmoney : 1772.35
     */

    private String moneysum;
    private String itemname;
    private String totalmoney;

    public String getMoneysum() {
        return moneysum;
    }

    public void setMoneysum(String moneysum) {
        this.moneysum = moneysum;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getTotalmoney() {
        return totalmoney;
    }

    public void setTotalmoney(String totalmoney) {
        this.totalmoney = totalmoney;
    }
}
