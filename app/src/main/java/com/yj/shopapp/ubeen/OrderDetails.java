package com.yj.shopapp.ubeen;

/**
 * Created by jm on 2016/5/23.
 */
public class OrderDetails {

    /**
     * itemname : TSL
     * itemcount : 12
     * moneysum : 880.00
     */

    private String itemname;
    private String itemcount;
    private String moneysum;
    private String imageUrl;


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageurl) {
        this.imageUrl = imageUrl;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getItemcount() {
        return itemcount;
    }

    public void setItemcount(String itemcount) {
        this.itemcount = itemcount;
    }

    public String getMoneysum() {
        return moneysum;
    }

    public void setMoneysum(String moneysum) {
        this.moneysum = moneysum;
    }
}
