package com.yj.shopapp.ubeen;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LK on 2018/1/23.
 *
 * @author LK
 */

public class ShopDetails {

    /**
     * status : 1
     * data : {"id":"3","shopname":"服装店","shopdetails":"<p><img src=\"http://127.0.0.1/fuhengwww/img.19diandian.com/Public/conimg/20180104/15150358466715.jpg\" _src=\"http://127.0.0.1/fuhengwww/img.19diandian.com/Public/conimg/20180104/15150358466715.jpg\"/><\/p>","imgurl":"http://u.19diandian.com/Public/uploads/shop/5a506b79bd98a.jpg","tel":"13666666666","contacts":"李先生","address":"东莞市"}
     */

    private int status;
    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Parcelable {
        /**
         * id : 3
         * shopname : 服装店
         * shopdetails : <p><img src="http://127.0.0.1/fuhengwww/img.19diandian.com/Public/conimg/20180104/15150358466715.jpg" _src="http://127.0.0.1/fuhengwww/img.19diandian.com/Public/conimg/20180104/15150358466715.jpg"/></p>
         * imgurl : http://u.19diandian.com/Public/uploads/shop/5a506b79bd98a.jpg
         * tel : 13666666666
         * contacts : 李先生
         * address : 东莞市
         */

        private String id;
        private String shopname;
        private String shopdetails;
        private String imgurl;
        private String tel;
        private String contacts;
        private String address;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getShopname() {
            return shopname;
        }

        public void setShopname(String shopname) {
            this.shopname = shopname;
        }

        public String getShopdetails() {
            return shopdetails;
        }

        public void setShopdetails(String shopdetails) {
            this.shopdetails = shopdetails;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getContacts() {
            return contacts;
        }

        public void setContacts(String contacts) {
            this.contacts = contacts;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.shopname);
            dest.writeString(this.shopdetails);
            dest.writeString(this.imgurl);
            dest.writeString(this.tel);
            dest.writeString(this.contacts);
            dest.writeString(this.address);
        }

        public DataBean() {
        }

        protected DataBean(Parcel in) {
            this.id = in.readString();
            this.shopname = in.readString();
            this.shopdetails = in.readString();
            this.imgurl = in.readString();
            this.tel = in.readString();
            this.contacts = in.readString();
            this.address = in.readString();
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
}
