package com.yj.shopapp.ubeen;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jm on 2016/5/13.
 */
public class LookItem implements Parcelable {


    /**
     * imgurl : http://u.19diandian.com/Public/uploads/20180325/5ab776494ae40.jpg
     * id : 199345
     * sprice : 55.00
     * stock : 380
     * industry_name : 0.5元区
     * class_name : 副食区
     * typestr : 0.5元区
     * name : 450ml 蓝色可乐
     * itemnoid : 6971163470105
     * cnumber : lskl
     * unit : 件
     * specs : 1*15
     * brochure : 美国蓝色可乐450ML 新品上市，火爆抢购中！
     * specialnote : 内赠东鹏特饮一瓶
     * sale_status : 1
     * bigtypeid : 267
     * bigtypename : 0.5元区
     * agentuser : 13999999999
     * agentname : 测试
     * agenttel :
     * is_promotion : 1
     * sales : 3
     * disstr : 50
     * gift : null
     */

    private String imgurl;
    private String id;
    private String sprice;
    private String stock;
    private String industry_name;
    private String class_name;
    private String typestr;
    private String name;
    private String itemnoid;
    private String cnumber;
    private String unit;
    private String specs;
    private String brochure;
    private String specialnote;
    private String sale_status;
    private String bigtypeid;
    private String bigtypename;
    private String agentuser;
    private String agentname;
    private String agenttel;
    private int is_promotion;
    private String sales;
    private String disstr;
    private String gift;
    private String msg;
    private String bookmark;

    public String getBookmark() {
        return bookmark == null ? "" : bookmark;
    }

    public void setBookmark(String bookmark) {
        this.bookmark = bookmark;
    }

    public String getMsg() {
        return msg == null ? "" : msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getImgurl() {
        return imgurl == null ? "" : imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSprice() {
        return sprice == null ? "" : sprice;
    }

    public void setSprice(String sprice) {
        this.sprice = sprice;
    }

    public String getStock() {
        return stock == null ? "" : stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getIndustry_name() {
        return industry_name == null ? "" : industry_name;
    }

    public void setIndustry_name(String industry_name) {
        this.industry_name = industry_name;
    }

    public String getClass_name() {
        return class_name == null ? "" : class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getTypestr() {
        return typestr == null ? "" : typestr;
    }

    public void setTypestr(String typestr) {
        this.typestr = typestr;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItemnoid() {
        return itemnoid == null ? "" : itemnoid;
    }

    public void setItemnoid(String itemnoid) {
        this.itemnoid = itemnoid;
    }

    public String getCnumber() {
        return cnumber == null ? "" : cnumber;
    }

    public void setCnumber(String cnumber) {
        this.cnumber = cnumber;
    }

    public String getUnit() {
        return unit == null ? "" : unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getSpecs() {
        return specs == null ? "" : specs;
    }

    public void setSpecs(String specs) {
        this.specs = specs;
    }

    public String getBrochure() {
        return brochure == null ? "" : brochure;
    }

    public void setBrochure(String brochure) {
        this.brochure = brochure;
    }

    public String getSpecialnote() {
        return specialnote == null ? "" : specialnote;
    }

    public void setSpecialnote(String specialnote) {
        this.specialnote = specialnote;
    }

    public String getSale_status() {
        return sale_status == null ? "" : sale_status;
    }

    public void setSale_status(String sale_status) {
        this.sale_status = sale_status;
    }

    public String getBigtypeid() {
        return bigtypeid == null ? "" : bigtypeid;
    }

    public void setBigtypeid(String bigtypeid) {
        this.bigtypeid = bigtypeid;
    }

    public String getBigtypename() {
        return bigtypename == null ? "" : bigtypename;
    }

    public void setBigtypename(String bigtypename) {
        this.bigtypename = bigtypename;
    }

    public String getAgentuser() {
        return agentuser == null ? "" : agentuser;
    }

    public void setAgentuser(String agentuser) {
        this.agentuser = agentuser;
    }

    public String getAgentname() {
        return agentname == null ? "" : agentname;
    }

    public void setAgentname(String agentname) {
        this.agentname = agentname;
    }

    public String getAgenttel() {
        return agenttel == null ? "" : agenttel;
    }

    public void setAgenttel(String agenttel) {
        this.agenttel = agenttel;
    }

    public int getIs_promotion() {
        return is_promotion;
    }

    public void setIs_promotion(int is_promotion) {
        this.is_promotion = is_promotion;
    }

    public String getSales() {
        return sales == null ? "" : sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public String getDisstr() {
        return disstr == null ? "" : disstr;
    }

    public void setDisstr(String disstr) {
        this.disstr = disstr;
    }

    public String getGift() {
        return gift == null ? "" : gift;
    }

    public void setGift(String gift) {
        this.gift = gift;
    }

    public LookItem() {
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
        dest.writeString(this.industry_name);
        dest.writeString(this.class_name);
        dest.writeString(this.typestr);
        dest.writeString(this.name);
        dest.writeString(this.itemnoid);
        dest.writeString(this.cnumber);
        dest.writeString(this.unit);
        dest.writeString(this.specs);
        dest.writeString(this.brochure);
        dest.writeString(this.specialnote);
        dest.writeString(this.sale_status);
        dest.writeString(this.bigtypeid);
        dest.writeString(this.bigtypename);
        dest.writeString(this.agentuser);
        dest.writeString(this.agentname);
        dest.writeString(this.agenttel);
        dest.writeInt(this.is_promotion);
        dest.writeString(this.sales);
        dest.writeString(this.disstr);
        dest.writeString(this.gift);
        dest.writeString(this.msg);
        dest.writeString(this.bookmark);
    }

    protected LookItem(Parcel in) {
        this.imgurl = in.readString();
        this.id = in.readString();
        this.sprice = in.readString();
        this.stock = in.readString();
        this.industry_name = in.readString();
        this.class_name = in.readString();
        this.typestr = in.readString();
        this.name = in.readString();
        this.itemnoid = in.readString();
        this.cnumber = in.readString();
        this.unit = in.readString();
        this.specs = in.readString();
        this.brochure = in.readString();
        this.specialnote = in.readString();
        this.sale_status = in.readString();
        this.bigtypeid = in.readString();
        this.bigtypename = in.readString();
        this.agentuser = in.readString();
        this.agentname = in.readString();
        this.agenttel = in.readString();
        this.is_promotion = in.readInt();
        this.sales = in.readString();
        this.disstr = in.readString();
        this.gift = in.readString();
        this.msg = in.readString();
        this.bookmark = in.readString();
    }

    public static final Creator<LookItem> CREATOR = new Creator<LookItem>() {
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
