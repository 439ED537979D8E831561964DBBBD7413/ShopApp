package com.yj.shopapp.wbeen;

/**
 * Created by jm on 2016/4/29.
 */
public class Goods {


    /**
     * imgurl :
     * id : 1947
     * name : 软黄山
     * numberid : 6901028225649
     * itemsum : 999
     * price : 20.00
     */

    private String imgurl;
    private String id;
    private String name;
    private String numberid;
    private String itemsum;
    private String price;
    private String sale_status;

    public String getSale_status() {
        return sale_status;
    }

    public void setSale_status(String sale_status) {
        this.sale_status = sale_status;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
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

    public String getNumberid() {
        return numberid;
    }

    public void setNumberid(String numberid) {
        this.numberid = numberid;
    }

    public String getItemsum() {
        return itemsum;
    }

    public void setItemsum(String itemsum) {
        this.itemsum = itemsum;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Goods() {
    }

    public Goods(String imgurl, String name, String itemsum, String price) {
        this.imgurl = imgurl;
        this.name = name;
        this.itemsum = itemsum;
        this.price = price;
    }
}
