package com.yj.shopapp.ubeen;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jm on 2016/5/13.
 */
public class LookItem implements Parcelable {

    /**
     * imgurl : http://u.yee9.net/Public/uploads/20160315/xt_56e7d71e93362.jpg
     * id : 23
     * sprice : 12.00
     * stock : 111
     * typestr : 通用分类
     * name : 另外一个
     * itemnoid : 123456789
     * cnumber : dfadfa
     * unit : null
     * specs : a
     * brochure : 堶
     * agentuser : 186
     * agentname : XX批发部
     * agenttel :
     */

    private String imgurl;
    private String id;
    private String sprice;
    private String stock;
    private String typestr;
    private String name;
    private String itemnoid;
    private String cnumber;
    private String unit;
    private String specs;
    private String brochure;
    private String agentuser;
    private String agentname;
    private String agenttel;
    private String is_show_price;
    private String is_show_stock;
    private String is_promotion;
    private String sales;
    private String sale_status;
    private String specialnote;
    private String bigtypeid;

    public String getBigtypeid() {
        return bigtypeid;
    }

    public void setBigtypeid(String bigtypeid) {
        this.bigtypeid = bigtypeid;
    }

    private String disstr;
    private String gift;

    public String getSpecialnote() {
        return specialnote;
    }

    public void setSpecialnote(String specialnote) {
        this.specialnote = specialnote;
    }

    public String getSale_status() {
        return sale_status;
    }

    public void setSale_status(String sale_status) {
        this.sale_status = sale_status;
    }

    public String getIs_promotion() {
        return is_promotion;
    }

    public String getSales() {
        return sales;
    }

    public String getDisstr() {
        return disstr;
    }

    public String getGift() {
        return gift;
    }

    public void setIs_promotion(String is_promotion) {
        this.is_promotion = is_promotion;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public void setDisstr(String disstr) {
        this.disstr = disstr;
    }

    public void setGift(String gift) {
        this.gift = gift;
    }

    public String getIs_show_price() {
        return is_show_price;
    }

    public void setIs_show_price(String is_show_price) {
        this.is_show_price = is_show_price;
    }

    public String getIs_show_stock() {
        return is_show_stock;
    }

    public void setIs_show_stock(String is_show_stock) {
        this.is_show_stock = is_show_stock;
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

    public String getTypestr() {
        return typestr;
    }

    public void setTypestr(String typestr) {
        this.typestr = typestr;
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

    public String getCnumber() {
        return cnumber;
    }

    public void setCnumber(String cnumber) {
        this.cnumber = cnumber;
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

    public String getAgentuser() {
        return agentuser;
    }

    public void setAgentuser(String agentuser) {
        this.agentuser = agentuser;
    }

    public String getAgentname() {
        return agentname;
    }

    public void setAgentname(String agentname) {
        this.agentname = agentname;
    }

    public String getAgenttel() {
        return agenttel;
    }

    public void setAgenttel(String agenttel) {
        this.agenttel = agenttel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imgurl);
        dest.writeString(this.id);
        dest.writeString(this.sprice);
        dest.writeString(this.stock);
        dest.writeString(this.typestr);
        dest.writeString(this.name);
        dest.writeString(this.itemnoid);
        dest.writeString(this.cnumber);
        dest.writeString(this.unit);
        dest.writeString(this.specs);
        dest.writeString(this.brochure);
        dest.writeString(this.agentuser);
        dest.writeString(this.agentname);
        dest.writeString(this.agenttel);
        dest.writeString(this.is_show_price);
        dest.writeString(this.is_show_stock);
        dest.writeString(this.is_promotion);
        dest.writeString(this.sales);
        dest.writeString(this.sale_status);
        dest.writeString(this.specialnote);
        dest.writeString(this.bigtypeid);
        dest.writeString(this.disstr);
        dest.writeString(this.gift);
    }

    public LookItem() {
    }

    protected LookItem(Parcel in) {
        this.imgurl = in.readString();
        this.id = in.readString();
        this.sprice = in.readString();
        this.stock = in.readString();
        this.typestr = in.readString();
        this.name = in.readString();
        this.itemnoid = in.readString();
        this.cnumber = in.readString();
        this.unit = in.readString();
        this.specs = in.readString();
        this.brochure = in.readString();
        this.agentuser = in.readString();
        this.agentname = in.readString();
        this.agenttel = in.readString();
        this.is_show_price = in.readString();
        this.is_show_stock = in.readString();
        this.is_promotion = in.readString();
        this.sales = in.readString();
        this.sale_status = in.readString();
        this.specialnote = in.readString();
        this.bigtypeid = in.readString();
        this.disstr = in.readString();
        this.gift = in.readString();
    }

    public static final Parcelable.Creator<LookItem> CREATOR = new Parcelable.Creator<LookItem>() {
        @Override
        public LookItem createFromParcel(Parcel source) {
            return new LookItem(source);
        }

        @Override
        public LookItem[] newArray(int size) {
            return new LookItem[size];
        }
    };
}
