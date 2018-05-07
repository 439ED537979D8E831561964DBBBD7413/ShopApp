package com.yj.shopapp.ubeen;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LK on 2018/4/6.
 *
 * @author LK
 */
public class BuGoodShopDatail implements Parcelable {

    /**
     * status : 1
     * info : 获取成功
     * data : {"id":"4","itemid":"199345","name":"450ml 蓝色可乐","itemnumber":"6971163470105","price":"55.00","unitprice":"50.00","num":"100","itemcount":"5","salesnum":"50","status":"1","start":"1521425600","unit":"件","specs":"1*15","brochure":"","imgurl":"http://u.19diandian.com/Public/uploads/20180325/5ab776494ae40.jpg","is_sale":0}
     */

    private int status;
    private String info;
    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Parcelable {
        /**
         * id : 4
         * itemid : 199345
         * name : 450ml 蓝色可乐
         * itemnumber : 6971163470105
         * price : 55.00
         * unitprice : 50.00
         * num : 100
         * itemcount : 5
         * salesnum : 50
         * status : 1
         * start : 1521425600
         * unit : 件
         * specs : 1*15
         * brochure :
         * imgurl : http://u.19diandian.com/Public/uploads/20180325/5ab776494ae40.jpg
         * is_sale : 0
         */

        private String id;
        private String itemid;
        private String name;
        private String itemnumber;
        private String price;
        private String unitprice;
        private String num;
        private String itemcount;
        private String salesnum;
        private String status;
        private String start;
        private String unit;
        private String specs;
        private String brochure;
        private String imgurl;
        private int is_sale;
        private String msg;

        public String getMsg() {
            return msg == null ? "" : msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getItemid() {
            return itemid;
        }

        public void setItemid(String itemid) {
            this.itemid = itemid;
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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getUnitprice() {
            return unitprice;
        }

        public void setUnitprice(String unitprice) {
            this.unitprice = unitprice;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getItemcount() {
            return itemcount;
        }

        public void setItemcount(String itemcount) {
            this.itemcount = itemcount;
        }

        public String getSalesnum() {
            return salesnum;
        }

        public void setSalesnum(String salesnum) {
            this.salesnum = salesnum;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStart() {
            return start;
        }

        public void setStart(String start) {
            this.start = start;
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

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public int getIs_sale() {
            return is_sale;
        }

        public void setIs_sale(int is_sale) {
            this.is_sale = is_sale;
        }

        public DataBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.itemid);
            dest.writeString(this.name);
            dest.writeString(this.itemnumber);
            dest.writeString(this.price);
            dest.writeString(this.unitprice);
            dest.writeString(this.num);
            dest.writeString(this.itemcount);
            dest.writeString(this.salesnum);
            dest.writeString(this.status);
            dest.writeString(this.start);
            dest.writeString(this.unit);
            dest.writeString(this.specs);
            dest.writeString(this.brochure);
            dest.writeString(this.imgurl);
            dest.writeInt(this.is_sale);
            dest.writeString(this.msg);
        }

        protected DataBean(Parcel in) {
            this.id = in.readString();
            this.itemid = in.readString();
            this.name = in.readString();
            this.itemnumber = in.readString();
            this.price = in.readString();
            this.unitprice = in.readString();
            this.num = in.readString();
            this.itemcount = in.readString();
            this.salesnum = in.readString();
            this.status = in.readString();
            this.start = in.readString();
            this.unit = in.readString();
            this.specs = in.readString();
            this.brochure = in.readString();
            this.imgurl = in.readString();
            this.is_sale = in.readInt();
            this.msg = in.readString();
        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel source) {
                return new DataBean(source);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.status);
        dest.writeString(this.info);
        dest.writeParcelable(this.data, flags);
    }

    public BuGoodShopDatail() {
    }

    protected BuGoodShopDatail(Parcel in) {
        this.status = in.readInt();
        this.info = in.readString();
        this.data = in.readParcelable(DataBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<BuGoodShopDatail> CREATOR = new Parcelable.Creator<BuGoodShopDatail>() {
        @Override
        public BuGoodShopDatail createFromParcel(Parcel source) {
            return new BuGoodShopDatail(source);
        }

        @Override
        public BuGoodShopDatail[] newArray(int size) {
            return new BuGoodShopDatail[size];
        }
    };
}
