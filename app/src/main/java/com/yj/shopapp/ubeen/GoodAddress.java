package com.yj.shopapp.ubeen;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * From ShopApp
 * Created by wxq on 17/7/26.
 * <p>
 * Beautiful Life ～
 */

public class GoodAddress {

    /**
     * children : [{"id":"29","name":"货架A-1"},{"id":"30","name":"货架A-2"},{"id":"31","name":"货架A-3"},{"id":"104","name":"1-1"}]
     * name : A区
     */

    private String name;
    private List<ChildrenBean> children;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ChildrenBean> getChildren() {
        return children;
    }

    public void setChildren(List<ChildrenBean> children) {
        this.children = children;
    }

    public static class ChildrenBean implements Parcelable {
        /**
         * id : 29
         * name : 货架A-1
         */

        private String id;
        private String name;

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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.id);
            dest.writeString(this.name);
        }

        public ChildrenBean() {
        }

        protected ChildrenBean(Parcel in) {
            this.id = in.readString();
            this.name = in.readString();
        }

        public static final Parcelable.Creator<ChildrenBean> CREATOR = new Parcelable.Creator<ChildrenBean>() {
            @Override
            public ChildrenBean createFromParcel(Parcel source) {
                return new ChildrenBean(source);
            }

            @Override
            public ChildrenBean[] newArray(int size) {
                return new ChildrenBean[size];
            }
        };
    }
}
