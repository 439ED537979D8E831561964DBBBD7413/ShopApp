package com.yj.shopapp.wbeen;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LK on 2018/5/28.
 *
 * @author LK
 */
public class Power implements Parcelable {

    /**
     * name : 0
     * itemnumber : 0
     * customnumber : 0
     * specs : 1
     * unitid : 1
     * supplierid : 1
     * sale_status : 1
     * minitemsum : 1
     * maxitemsum : 1
     * itemsum : 1
     * minnum : 1
     * maxnum : 1
     * stopitemsum : 1
     * costprice : 1
     * price : 1
     * imgid : 1
     * brochure : 1
     * specialnote : 1
     * is_hot : 1
     * is_new : 1
     * industryid : 0
     * localhost : 1
     * brand : 1
     * sold_out : 1
     */

    private int name;
    private int itemnumber;
    private int customnumber;
    private int specs;
    private int unitid;
    private int supplierid;
    private int sale_status;
    private int minitemsum;
    private int maxitemsum;
    private int itemsum;
    private int minnum;
    private int maxnum;
    private int stopitemsum;
    private int costprice;
    private int price;
    private int imgid;
    private int brochure;
    private int specialnote;
    private int is_hot;
    private int is_new;
    private int industryid;
    private int localhost;
    private int brand;
    private int sold_out;
    private int is_sales;
    private int vipprice;

    public int getVipprice() {
        return vipprice;
    }

    public void setVipprice(int vipprice) {
        this.vipprice = vipprice;
    }

    public int getIs_sales() {
        return is_sales;
    }

    public void setIs_sales(int is_sales) {
        this.is_sales = is_sales;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public int getItemnumber() {
        return itemnumber;
    }

    public void setItemnumber(int itemnumber) {
        this.itemnumber = itemnumber;
    }

    public int getCustomnumber() {
        return customnumber;
    }

    public void setCustomnumber(int customnumber) {
        this.customnumber = customnumber;
    }

    public int getSpecs() {
        return specs;
    }

    public void setSpecs(int specs) {
        this.specs = specs;
    }

    public int getUnitid() {
        return unitid;
    }

    public void setUnitid(int unitid) {
        this.unitid = unitid;
    }

    public int getSupplierid() {
        return supplierid;
    }

    public void setSupplierid(int supplierid) {
        this.supplierid = supplierid;
    }

    public int getSale_status() {
        return sale_status;
    }

    public void setSale_status(int sale_status) {
        this.sale_status = sale_status;
    }

    public int getMinitemsum() {
        return minitemsum;
    }

    public void setMinitemsum(int minitemsum) {
        this.minitemsum = minitemsum;
    }

    public int getMaxitemsum() {
        return maxitemsum;
    }

    public void setMaxitemsum(int maxitemsum) {
        this.maxitemsum = maxitemsum;
    }

    public int getItemsum() {
        return itemsum;
    }

    public void setItemsum(int itemsum) {
        this.itemsum = itemsum;
    }

    public int getMinnum() {
        return minnum;
    }

    public void setMinnum(int minnum) {
        this.minnum = minnum;
    }

    public int getMaxnum() {
        return maxnum;
    }

    public void setMaxnum(int maxnum) {
        this.maxnum = maxnum;
    }

    public int getStopitemsum() {
        return stopitemsum;
    }

    public void setStopitemsum(int stopitemsum) {
        this.stopitemsum = stopitemsum;
    }

    public int getCostprice() {
        return costprice;
    }

    public void setCostprice(int costprice) {
        this.costprice = costprice;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getImgid() {
        return imgid;
    }

    public void setImgid(int imgid) {
        this.imgid = imgid;
    }

    public int getBrochure() {
        return brochure;
    }

    public void setBrochure(int brochure) {
        this.brochure = brochure;
    }

    public int getSpecialnote() {
        return specialnote;
    }

    public void setSpecialnote(int specialnote) {
        this.specialnote = specialnote;
    }

    public int getIs_hot() {
        return is_hot;
    }

    public void setIs_hot(int is_hot) {
        this.is_hot = is_hot;
    }

    public int getIs_new() {
        return is_new;
    }

    public void setIs_new(int is_new) {
        this.is_new = is_new;
    }

    public int getIndustryid() {
        return industryid;
    }

    public void setIndustryid(int industryid) {
        this.industryid = industryid;
    }

    public int getLocalhost() {
        return localhost;
    }

    public void setLocalhost(int localhost) {
        this.localhost = localhost;
    }

    public int getBrand() {
        return brand;
    }

    public void setBrand(int brand) {
        this.brand = brand;
    }

    public int getSold_out() {
        return sold_out;
    }

    public void setSold_out(int sold_out) {
        this.sold_out = sold_out;
    }

    public Power() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.name);
        dest.writeInt(this.itemnumber);
        dest.writeInt(this.customnumber);
        dest.writeInt(this.specs);
        dest.writeInt(this.unitid);
        dest.writeInt(this.supplierid);
        dest.writeInt(this.sale_status);
        dest.writeInt(this.minitemsum);
        dest.writeInt(this.maxitemsum);
        dest.writeInt(this.itemsum);
        dest.writeInt(this.minnum);
        dest.writeInt(this.maxnum);
        dest.writeInt(this.stopitemsum);
        dest.writeInt(this.costprice);
        dest.writeInt(this.price);
        dest.writeInt(this.imgid);
        dest.writeInt(this.brochure);
        dest.writeInt(this.specialnote);
        dest.writeInt(this.is_hot);
        dest.writeInt(this.is_new);
        dest.writeInt(this.industryid);
        dest.writeInt(this.localhost);
        dest.writeInt(this.brand);
        dest.writeInt(this.sold_out);
        dest.writeInt(this.is_sales);
        dest.writeInt(this.vipprice);
    }

    protected Power(Parcel in) {
        this.name = in.readInt();
        this.itemnumber = in.readInt();
        this.customnumber = in.readInt();
        this.specs = in.readInt();
        this.unitid = in.readInt();
        this.supplierid = in.readInt();
        this.sale_status = in.readInt();
        this.minitemsum = in.readInt();
        this.maxitemsum = in.readInt();
        this.itemsum = in.readInt();
        this.minnum = in.readInt();
        this.maxnum = in.readInt();
        this.stopitemsum = in.readInt();
        this.costprice = in.readInt();
        this.price = in.readInt();
        this.imgid = in.readInt();
        this.brochure = in.readInt();
        this.specialnote = in.readInt();
        this.is_hot = in.readInt();
        this.is_new = in.readInt();
        this.industryid = in.readInt();
        this.localhost = in.readInt();
        this.brand = in.readInt();
        this.sold_out = in.readInt();
        this.is_sales = in.readInt();
        this.vipprice = in.readInt();
    }

    public static final Creator<Power> CREATOR = new Creator<Power>() {
        @Override
        public Power createFromParcel(Parcel source) {
            return new Power(source);
        }

        @Override
        public Power[] newArray(int size) {
            return new Power[size];
        }
    };
}
