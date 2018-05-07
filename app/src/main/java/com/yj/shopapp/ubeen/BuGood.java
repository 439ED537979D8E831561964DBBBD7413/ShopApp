package com.yj.shopapp.ubeen;

/**
 * Created by LK on 2018/3/30.
 *
 * @author LK
 */

public class BuGood {

    /**
     * num : 1
     * sumintegral : 10000
     * name : 红牛
     * unit : 件
     * shopname : 仓库
     * date : 2018-02-10 15:09:40
     */

    private String num;
    private String sumintegral;
    private String name;
    private String unit;
    private String shopname;
    private String date;

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getSumintegral() {
        return sumintegral;
    }

    public void setSumintegral(String sumintegral) {
        this.sumintegral = sumintegral;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getDate() {
        return date == null ? "" : date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
