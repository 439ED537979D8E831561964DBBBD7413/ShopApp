package com.yj.shopapp.ubeen;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jm on 2016/5/11.
 */
public class Goods implements Parcelable {


    /**
     * id : 1799
     * name : XX饼干
     * specs : 1*12
     * price : 20.00
     * unitid : 13
     * imgurl :
     * unit : 个
     */

    private String id;
    private String name;
    private String specs;
    private String price;
    private String unitid;
    private String imgurl;
    private String unit;
    private String is_show_price;
    private String is_show_stock;
    private String sale_status;
    private String costprice;
    private String itemnumber;
    private String bigtypeid;
    private String imageurl;
    private String sales_price;
    private boolean isSelected;
    private String date;
    private String itemsum;
    private String msg;
    private String specialnote;
    private String localhost;

    public String getLocalhost() {
        return localhost == null ? "" : localhost;
    }

    public void setLocalhost(String localhost) {
        this.localhost = localhost;
    }

    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecs() {
        return specs == null ? "" : specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    public String getPrice() {
        return price == null ? "" : price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUnitid() {
        return unitid == null ? "" : unitid;
    }

    public void setUnitid(String unitid) {
        this.unitid = unitid;
    }

    public String getImgurl() {
        return imgurl == null ? "" : imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getUnit() {
        return unit == null ? "" : unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getIs_show_price() {
        return is_show_price == null ? "" : is_show_price;
    }

    public void setIs_show_price(String is_show_price) {
        this.is_show_price = is_show_price;
    }

    public String getIs_show_stock() {
        return is_show_stock == null ? "" : is_show_stock;
    }

    public void setIs_show_stock(String is_show_stock) {
        this.is_show_stock = is_show_stock;
    }

    public String getSale_status() {
        return sale_status == null ? "" : sale_status;
    }

    public void setSale_status(String sale_status) {
        this.sale_status = sale_status;
    }

    public String getCostprice() {
        return costprice == null ? "" : costprice;
    }

    public void setCostprice(String costprice) {
        this.costprice = costprice;
    }

    public String getItemnumber() {
        return itemnumber == null ? "" : itemnumber;
    }

    public void setItemnumber(String itemnumber) {
        this.itemnumber = itemnumber;
    }

    public String getBigtypeid() {
        return bigtypeid == null ? "" : bigtypeid;
    }

    public void setBigtypeid(String bigtypeid) {
        this.bigtypeid = bigtypeid;
    }

    public String getImageurl() {
        return imageurl == null ? "" : imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getSales_price() {
        return sales_price == null ? "" : sales_price;
    }

    public void setSales_price(String sales_price) {
        this.sales_price = sales_price;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getDate() {
        return date == null ? "" : date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getItemsum() {
        return itemsum == null ? "" : itemsum;
    }

    public void setItemsum(String itemsum) {
        this.itemsum = itemsum;
    }

    public String getSpecialnote() {
        return specialnote == null ? "" : specialnote;
    }

    public void setSpecialnote(String specialnote) {
        this.specialnote = specialnote;
    }

    public String getMsg() {
        return msg == null ? "" : msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Goods() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.specs);
        dest.writeString(this.price);
        dest.writeString(this.unitid);
        dest.writeString(this.imgurl);
        dest.writeString(this.unit);
        dest.writeString(this.is_show_price);
        dest.writeString(this.is_show_stock);
        dest.writeString(this.sale_status);
        dest.writeString(this.costprice);
        dest.writeString(this.itemnumber);
        dest.writeString(this.bigtypeid);
        dest.writeString(this.imageurl);
        dest.writeString(this.sales_price);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
        dest.writeString(this.date);
        dest.writeString(this.itemsum);
        dest.writeString(this.msg);
        dest.writeString(this.specialnote);
        dest.writeString(this.localhost);
    }

    protected Goods(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.specs = in.readString();
        this.price = in.readString();
        this.unitid = in.readString();
        this.imgurl = in.readString();
        this.unit = in.readString();
        this.is_show_price = in.readString();
        this.is_show_stock = in.readString();
        this.sale_status = in.readString();
        this.costprice = in.readString();
        this.itemnumber = in.readString();
        this.bigtypeid = in.readString();
        this.imageurl = in.readString();
        this.sales_price = in.readString();
        this.isSelected = in.readByte() != 0;
        this.date = in.readString();
        this.itemsum = in.readString();
        this.msg = in.readString();
        this.specialnote = in.readString();
        this.localhost = in.readString();
    }

    public static final Creator<Goods> CREATOR = new Creator<Goods>() {
        @Override
        public Goods createFromParcel(Parcel source) {
            return new Goods(source);
        }

        @Override
        public Goods[] newArray(int size) {
            return new Goods[size];
        }
    };
}
