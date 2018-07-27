package com.yj.shopapp.ubeen;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by LK on 2018/2/3.
 *
 * @author LK
 */

public class BrandGroup implements Parcelable {

    /**
     * name : 一线品牌
     * list : [{"id":"1","name":"康师傅","imgurl":"/Public/uploads/brand/5a61a7aadf857.png","sort":"1"},{"id":"2","name":"东鹏","imgurl":"/Public/uploads/brand/brand.png","sort":"2"},{"id":"3","name":"洁柔","imgurl":"/Public/uploads/brand/brand.png","sort":"3"},{"id":"4","name":"清扬","imgurl":"/Public/uploads/brand/brand.png","sort":"4"}]
     */

    private String name;
    private List<ListBean> list;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Parcelable {
        /**
         * id : 1
         * name : 康师傅
         * imgurl : /Public/uploads/brand/5a61a7aadf857.png
         * sort : 1
         */

        private String id;
        private String name;
        private String imgurl;
        private String sort;
        private boolean isSort;
        private boolean isFoot;
        private String gid;
        private int position;//记录标题的位置
        private int index;
        private int is_open;
        private String info;

        public int getIs_open() {
            return is_open;
        }

        public void setIs_open(int is_open) {
            this.is_open = is_open;
        }

        public String getInfo() {
            return info == null ? "" : info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public String getGid() {
            return gid;
        }

        public void setGid(String gid) {
            this.gid = gid;
        }

        public boolean isFoot() {
            return isFoot;
        }

        public void setFoot(boolean foot) {
            isFoot = foot;
        }

        public boolean isSort() {
            return isSort;
        }

        public void setSort(boolean sort) {
            isSort = sort;
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

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public ListBean() {
        }

        public ListBean(String name, boolean isSort) {
            this.name = name;
            this.isSort = isSort;
        }

        public ListBean(String name, int position) {
            this.name = name;
            this.position = position;
        }

        public ListBean(String name, String gid, boolean isFoot, int position) {
            this.name = name;
            this.gid = gid;
            this.isFoot = isFoot;
            this.position = position;
        }

        public ListBean(String name, int index, boolean isSort) {
            this.name = name;
            this.index = index;
            this.isSort = isSort;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.name);
            dest.writeString(this.imgurl);
            dest.writeString(this.sort);
            dest.writeByte(this.isSort ? (byte) 1 : (byte) 0);
            dest.writeByte(this.isFoot ? (byte) 1 : (byte) 0);
            dest.writeString(this.gid);
            dest.writeInt(this.position);
            dest.writeInt(this.index);
            dest.writeInt(this.is_open);
            dest.writeString(this.info);
        }

        protected ListBean(Parcel in) {
            this.id = in.readString();
            this.name = in.readString();
            this.imgurl = in.readString();
            this.sort = in.readString();
            this.isSort = in.readByte() != 0;
            this.isFoot = in.readByte() != 0;
            this.gid = in.readString();
            this.position = in.readInt();
            this.index = in.readInt();
            this.is_open = in.readInt();
            this.info = in.readString();
        }

        public static final Creator<ListBean> CREATOR = new Creator<ListBean>() {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeTypedList(this.list);
    }

    public BrandGroup() {
    }

    protected BrandGroup(Parcel in) {
        this.name = in.readString();
        this.list = in.createTypedArrayList(ListBean.CREATOR);
    }

    public static final Parcelable.Creator<BrandGroup> CREATOR = new Parcelable.Creator<BrandGroup>() {
        @Override
        public BrandGroup createFromParcel(Parcel source) {
            return new BrandGroup(source);
        }

        @Override
        public BrandGroup[] newArray(int size) {
            return new BrandGroup[size];
        }
    };
}
