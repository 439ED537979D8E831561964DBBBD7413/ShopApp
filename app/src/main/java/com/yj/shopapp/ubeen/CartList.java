package com.yj.shopapp.ubeen;

/**
 * Created by jm on 2016/5/12.
 */
public class CartList {

    /**
     * id : 136
     * moneysum : 0.00
     * name : 龙井茶修改
     * itemnumber : 123456
     * customnumber : 334
     * price : 24.00
     * imgurl :
     * ashopname : 批发部4
     */

    private String id;
    private String moneysum;
    private String name;
    private String itemnumber;
    private String customnumber;
    private String price;
    private String imgurl;
    private String ashopname;
    private String itemcount;
    private String is_sales;
    private String sales;
    private String disstr;
    private String bigtypename;
    private String itemid;
    private boolean isChoosed;
    private String sale_status;

    public boolean isChoosed() {
        return isChoosed;
    }

    public void setChoosed(boolean choosed) {
        isChoosed = choosed;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getBigtypename() {
        return bigtypename;
    }

    public void setBigtypename(String bigtypename) {
        this.bigtypename = bigtypename;
    }

    public String getIs_sales() {
        return is_sales;
    }

    public void setIs_sales(String is_sales) {
        this.is_sales = is_sales;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public String getDisstr() {
        return disstr;
    }

    public void setDisstr(String disstr) {
        this.disstr = disstr;
    }

    public String getItemcount() {
        return itemcount;
    }

    public void setItemcount(String itemcount) {
        this.itemcount = itemcount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMoneysum() {
        return moneysum;
    }

    public void setMoneysum(String moneysum) {
        this.moneysum = moneysum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItemnumber() {
        return itemnumber;
    }

    public void setItemnumber(String itemnumber) {
        this.itemnumber = itemnumber;
    }

    public String getCustomnumber() {
        return customnumber;
    }

    public void setCustomnumber(String customnumber) {
        this.customnumber = customnumber;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getAshopname() {
        return ashopname;
    }

    public void setAshopname(String ashopname) {
        this.ashopname = ashopname;
    }

    public String getSale_status() {
        return sale_status;
    }

    public void setSale_status(String sale_status) {
        this.sale_status = sale_status;
    }
}
