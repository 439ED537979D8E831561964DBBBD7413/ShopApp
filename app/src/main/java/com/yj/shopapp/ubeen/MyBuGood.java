package com.yj.shopapp.ubeen;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LK on 2018/4/7.
 *
 * @author LK
 */
public class MyBuGood {


    /**
     * status : 1
     * lists : [{"id":"10125","sale_id":"7","addtime":"1523069392","name":"500ML纯净水（东鹏）","itemid":"76132","itemnumber":"6934502302024","specs":null,"itemcount":"5","unitprice":"11.00","moneysum":"55.00","imgurl":"http://u.19diandian.com/Public/uploads/20170527/xt_592993bd0e552.jpg","canceltime":1523072992},{"id":"10127","sale_id":"8","addtime":"1523073692","name":"250ML东鹏特饮","itemid":"76102","itemnumber":"6934502300709","specs":null,"itemcount":"5","unitprice":"58.00","moneysum":"290.00","imgurl":"http://u.19diandian.com/Public/uploads/20170720/979b1f9df6e31b41a0b60469c1074f11.jpg","canceltime":1523077292},{"id":"10132","sale_id":"9","addtime":"1523081083","name":"500ML纯净水（东鹏）","itemid":"76132","itemnumber":"6934502302024","specs":null,"itemcount":"5","unitprice":"11.00","moneysum":"55.00","imgurl":"http://u.19diandian.com/Public/uploads/20170527/xt_592993bd0e552.jpg","canceltime":1523084683}]
     * info : 查询成功!
     */

    private int status;
    private String info;
    private List<ListsBean> lists;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInfo() {
        return info == null ? "" : info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public List<ListsBean> getLists() {
        if (lists == null) {
            return new ArrayList<>();
        }
        return lists;
    }

    public void setLists(List<ListsBean> lists) {
        this.lists = lists;
    }

    public static class ListsBean implements Parcelable {
        /**
         * id : 10125
         * sale_id : 7
         * addtime : 1523069392
         * name : 500ML纯净水（东鹏）
         * itemid : 76132
         * itemnumber : 6934502302024
         * specs : null
         * itemcount : 5
         * unitprice : 11.00
         * moneysum : 55.00
         * imgurl : http://u.19diandian.com/Public/uploads/20170527/xt_592993bd0e552.jpg
         * canceltime : 1523072992
         */

        private String id;
        private String sale_id;
        private String addtime;
        private String name;
        private String itemid;
        private String itemnumber;
        private String specs;
        private String itemcount;
        private String unitprice;
        private String moneysum;
        private String imgurl;
        private int canceltime;
        private String unit;
        private int status;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public static Creator<ListsBean> getCREATOR() {
            return CREATOR;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSale_id() {
            return sale_id;
        }

        public void setSale_id(String sale_id) {
            this.sale_id = sale_id;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getItemid() {
            return itemid;
        }

        public void setItemid(String itemid) {
            this.itemid = itemid;
        }

        public String getItemnumber() {
            return itemnumber;
        }

        public void setItemnumber(String itemnumber) {
            this.itemnumber = itemnumber;
        }

        public String getSpecs() {
            return specs;
        }

        public void setSpecs(String specs) {
            this.specs = specs;
        }

        public String getItemcount() {
            return itemcount;
        }

        public void setItemcount(String itemcount) {
            this.itemcount = itemcount;
        }

        public String getUnitprice() {
            return unitprice;
        }

        public void setUnitprice(String unitprice) {
            this.unitprice = unitprice;
        }

        public String getMoneysum() {
            return moneysum;
        }

        public void setMoneysum(String moneysum) {
            this.moneysum = moneysum;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public int getCanceltime() {
            return canceltime;
        }

        public void setCanceltime(int canceltime) {
            this.canceltime = canceltime;
        }

        public ListsBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.sale_id);
            dest.writeString(this.addtime);
            dest.writeString(this.name);
            dest.writeString(this.itemid);
            dest.writeString(this.itemnumber);
            dest.writeString(this.specs);
            dest.writeString(this.itemcount);
            dest.writeString(this.unitprice);
            dest.writeString(this.moneysum);
            dest.writeString(this.imgurl);
            dest.writeInt(this.canceltime);
            dest.writeString(this.unit);
            dest.writeInt(this.status);
        }

        protected ListsBean(Parcel in) {
            this.id = in.readString();
            this.sale_id = in.readString();
            this.addtime = in.readString();
            this.name = in.readString();
            this.itemid = in.readString();
            this.itemnumber = in.readString();
            this.specs = in.readString();
            this.itemcount = in.readString();
            this.unitprice = in.readString();
            this.moneysum = in.readString();
            this.imgurl = in.readString();
            this.canceltime = in.readInt();
            this.unit = in.readString();
            this.status = in.readInt();
        }

        public static final Creator<ListsBean> CREATOR = new Creator<ListsBean>() {
            @Override
            public ListsBean createFromParcel(Parcel source) {
                return new ListsBean(source);
            }

            @Override
            public ListsBean[] newArray(int size) {
                return new ListsBean[size];
            }
        };
    }
}
