package com.yj.shopapp.wbeen;

import java.io.Serializable;

/**
 * Created by jm on 2016/5/2.
 */
public class Lookitem implements Serializable {

    /**
     * imgurl :
     * imgid :
     * id : 1862
     * sprice : 20.00
     * stock : 999
     * typeid : 22
     * typename : 通用分类
     * name : 白沙二代黑
     * itemnoid : 6901028191098
     * unitid :
     * unit :
     * specs :
     * brochure :
     */

    private String imgurl;
    private String imgid;
    private String id;
    private String sprice;
    private String stock;
    private String typeid;
    private String typename;
    private String name;
    private String brandname;
    private String itemnoid;
    private String unitid;
    private String unit;
    private String specs;
    private String brochure;
    private String saveid;
    private String costprice;
    private String is_new;
    private String minitemsum;
    private String maxitemsum;
    private String suppilername;
    private String supplierid;
    private String specialnote;
    private String customnumber;
    private String sale_status;
    private String minmum;
    private String maxmum;
    private String stopitemsum;
    private String is_hot;
    private String is_sales;
    private String vipprice;

    public String getVipprice() {
        return vipprice == null ? "" : vipprice;
    }

    public void setVipprice(String vipprice) {
        this.vipprice = vipprice;
    }

    public String getIs_sales() {
        return is_sales == null ? "" : is_sales;
    }

    public void setIs_sales(String is_sales) {
        this.is_sales = is_sales;
    }

    public String getMinmum() {
        return minmum == null ? "" : minmum;
    }

    public void setMinmum(String minmum) {
        this.minmum = minmum;
    }

    public String getMaxmum() {
        return maxmum == null ? "" : maxmum;
    }

    public void setMaxmum(String maxmum) {
        this.maxmum = maxmum;
    }

    public String getIs_hot() {
        return is_hot == null ? "" : is_hot;
    }

    public void setIs_hot(String is_hot) {
        this.is_hot = is_hot;
    }

    public String getStopitemsum() {
        return stopitemsum;
    }

    public void setStopitemsum(String stopitemsum) {
        this.stopitemsum = stopitemsum;
    }

    public String getMinnum() {
        return minmum;
    }

    public void setMinnum(String minnum) {
        this.minmum = minnum;
    }

    public String getMaxnum() {
        return maxmum;
    }

    public void setMaxnum(String maxnum) {
        this.maxmum = maxnum;
    }

    public String getBrandname() {
        return brandname;
    }

    public void setBrandname(String brand) {
        this.brandname = brand;
    }

    public String getLocalhostname() {
        return localhostname;
    }

    public void setLocalhostname(String localhostname) {
        this.localhostname = localhostname;
    }

    private String localhostname;

    public String getLocalhost() {
        return localhost;
    }

    public void setLocalhost(String localhost) {
        this.localhost = localhost;
    }

    private String localhost;

    public String getSuppilername() {
        return suppilername;
    }

    public void setSuppilername(String suppilername) {
        this.suppilername = suppilername;
    }

    public String getSupplierid() {
        return supplierid;
    }

    public void setSupplierid(String supplierid) {
        this.supplierid = supplierid;
    }

    public String getSpecialnote() {
        return specialnote;
    }

    public void setSpecialnote(String specialnote) {
        this.specialnote = specialnote;
    }

    public String getCustomnumber() {
        return customnumber;
    }

    public void setCustomnumber(String customnumber) {
        this.customnumber = customnumber;
    }

    public String getSale_status() {
        return sale_status;
    }

    public void setSale_status(String sale_status) {
        this.sale_status = sale_status;
    }

    public String getMinitemsum() {
        return minitemsum;
    }

    public void setMinitemsum(String minitemsum) {
        this.minitemsum = minitemsum;
    }

    public String getMaxitemsum() {
        return maxitemsum;
    }

    public void setMaxitemsum(String maxitemsum) {
        this.maxitemsum = maxitemsum;
    }

    public String getIs_new() {
        return is_new;
    }

    public void setIs_new(String is_new) {
        this.is_new = is_new;
    }

    public String getCostprice() {
        return costprice;
    }

    public void setCostprice(String costprice) {
        this.costprice = costprice;
    }

    public void setSaveid(String saveid) {
        this.saveid = saveid;
    }

    public String getSaveid() {
        return saveid;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getImgid() {
        return imgid;
    }

    public void setImgid(String imgid) {
        this.imgid = imgid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSprice() {
        return sprice;
    }

    public void setSprice(String sprice) {
        this.sprice = sprice;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItemnoid() {
        return itemnoid;
    }

    public void setItemnoid(String itemnoid) {
        this.itemnoid = itemnoid;
    }

    public String getUnitid() {
        return unitid;
    }

    public void setUnitid(String unitid) {
        this.unitid = unitid;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getSpecs() {
        return specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    public String getBrochure() {
        return brochure;
    }

    public void setBrochure(String brochure) {
        this.brochure = brochure;
    }
}
