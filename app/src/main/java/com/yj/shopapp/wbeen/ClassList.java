package com.yj.shopapp.wbeen;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by LK on 2018/5/14.
 *
 * @author LK
 */
public class ClassList {
    /**

     * cid : 2
     * name : 副食区
     * imgurl : http://u.19diandian.com/Public/uploads/classify/5ac8802bbb090.png
     * list : [{"id":"4","cid":"2","name":"饼干面包","imgurl":"http://u.19diandian.com/Public/uploads/industrys/5916d7d19067e.jpg"}]
     */

    private String cid;
    private String name;
    private String imgurl;
    private List<ListBean> list;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Parcelable {
        /**
         * id : 4
         * cid : 2
         * name : 饼干面包
         * imgurl : http://u.19diandian.com/Public/uploads/industrys/5916d7d19067e.jpg
         */

        private String id;
        private String cid;
        private String name;
        private String imgurl;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.cid);
            dest.writeString(this.name);
            dest.writeString(this.imgurl);
        }

        public ListBean() {
        }

        protected ListBean(Parcel in) {
            this.id = in.readString();
            this.cid = in.readString();
            this.name = in.readString();
            this.imgurl = in.readString();
        }

        public static final Parcelable.Creator<ListBean> CREATOR = new Parcelable.Creator<ListBean>() {
            @Override
            public ListBean createFromParcel(Parcel source) {
                return new ListBean(source);
            }

            @Override
            public ListBean[] newArray(int size) {
                return new ListBean[size];
            }
        };
    }
}
