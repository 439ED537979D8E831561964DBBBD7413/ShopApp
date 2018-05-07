package com.yj.shopapp.ubeen;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by LK on 2017/12/25.
 *
 * @author LK
 */

public class ExcGoods implements Parcelable {


    /**
     * status : 1
     * data : [{"id":"5","name":"红牛","integral":"10000","num":"48","imgurl":"http://u.19diandian.com/Public/uploads/goods/5a630ae94118c.jpg","details":"红牛"},{"id":"6","name":"蓝色可乐","integral":"10000","num":"50","imgurl":"http://u.19diandian.com/Public/uploads/goods/5a630b0b5af22.jpg","details":"蓝色可乐"},{"id":"4","name":"罐装东鹏","integral":"6000","num":"50","imgurl":"http://u.19diandian.com/Public/uploads/goods/5a630aac66b9a.jpg","details":"罐装东鹏"},{"id":"3","name":"纸盒东鹏","integral":"5000","num":"50","imgurl":"http://u.19diandian.com/Public/uploads/goods/5a630a7b5837c.jpg","details":"纸盒东鹏"},{"id":"2","name":"小米手机","integral":"500","num":"16","imgurl":"http://u.19diandian.com/Public/uploads/goods/5a63076b7524d.jpg","details":"防水手表"},{"id":"1","name":"华为手机","integral":"100","num":"90","imgurl":"http://u.19diandian.com/Public/uploads/goods/5a630755ade0f.jpg","details":"<p><\/p><p><img src=\"http://img.19diandian.com/Public/conimg/20180204/15177378733134.jpg\" _src=\"http://img.19diandian.com/Public/conimg/20180204/15177378733134.jpg\" style=\"\"/><\/p><p><img src=\"http://img.19diandian.com/Public/conimg/20180204/15177378767279.gif\" _src=\"http://img.19diandian.com/Public/conimg/20180204/15177378767279.gif\" style=\"\"/><\/p><p><br/><\/p>"}]
     * info : 获取成功
     */

    private int status;
    private String info;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Parcelable {
        /**
         * id : 5
         * name : 红牛
         * integral : 10000
         * num : 48
         * imgurl : http://u.19diandian.com/Public/uploads/goods/5a630ae94118c.jpg
         * details : 红牛
         */

        private String id;
        private String name;
        private String integral;
        private String num;
        private String imgurl;
        private String details;
        private String specs;
        private String unit;

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

        public String getIntegral() {
            return integral;
        }

        public void setIntegral(String integral) {
            this.integral = integral;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.name);
            dest.writeString(this.integral);
            dest.writeString(this.num);
            dest.writeString(this.imgurl);
            dest.writeString(this.details);
            dest.writeString(this.specs);
            dest.writeString(this.unit);
        }

        public DataBean() {
        }

        protected DataBean(Parcel in) {
            this.id = in.readString();
            this.name = in.readString();
            this.integral = in.readString();
            this.num = in.readString();
            this.imgurl = in.readString();
            this.details = in.readString();
            this.specs = in.readString();
            this.unit = in.readString();
        }

        public static final Parcelable.Creator<DataBean> CREATOR = new Parcelable.Creator<DataBean>() {
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
        dest.writeTypedList(this.data);
    }

    public ExcGoods() {
    }

    protected ExcGoods(Parcel in) {
        this.status = in.readInt();
        this.info = in.readString();
        this.data = in.createTypedArrayList(DataBean.CREATOR);
    }

    public static final Parcelable.Creator<ExcGoods> CREATOR = new Parcelable.Creator<ExcGoods>() {
        @Override
        public ExcGoods createFromParcel(Parcel source) {
            return new ExcGoods(source);
        }

        @Override
        public ExcGoods[] newArray(int size) {
            return new ExcGoods[size];
        }
    };
}
