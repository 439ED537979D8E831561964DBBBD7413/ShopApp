package com.yj.shopapp.ubeen;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LK on 2018/1/30.
 *
 * @author LK
 */

public class IndustryCatelist implements Parcelable {

    /**
     * status : 1
     * info : 获取数据成功
     * data : [{"name":"一线品牌","list":[{"id":"7","name":"方便面","imgurl":"/Public/uploads/industrys/5916cb7d93acd.jpg","cid":"2"},{"id":"55","name":"口香/润喉糖","imgurl":"/Public/uploads/industrys/5916d13432f7e.jpg","cid":"2"},{"id":"58","name":"鱼仔/脆骨","imgurl":"/Public/uploads/industrys/5916ce148aa55.jpg","cid":"2"}]},{"name":"二线品牌1","list":[{"id":"65","name":"儿童食品区","imgurl":"/Public/uploads/industrys/5926540219654.jpg","cid":"2"},{"id":"270","name":"饼干","imgurl":"/Public/uploads/industrys/59af6410e376c.jpg","cid":"2"}]},{"name":"其他","list":[{"id":"275","name":"瓜子","imgurl":"/Public/uploads/industrys/59af649026879.jpg","cid":"2"},{"id":"283","name":"火腿香肠","imgurl":"/Public/uploads/industrys/59af6531f0461.jpg","cid":"2"},{"id":"287","name":"品牌糖果","imgurl":"/Public/uploads/industrys/59af65a3227d1.jpg","cid":"2"},{"id":"294","name":"油炸食品","imgurl":"/Public/uploads/industrys/59af666bdd6cd.jpg","cid":"2"},{"id":"271","name":"冲调饮品","imgurl":"/Public/uploads/industrys/59af642650d1d.jpg","cid":"2"},{"id":"276","name":"麻辣食品","imgurl":"/Public/uploads/industrys/59af64a60d687.jpg","cid":"2"},{"id":"284","name":"凉果·坚果","imgurl":"/Public/uploads/industrys/59af6544cc206.jpg","cid":"2"},{"id":"289","name":"瓶装凉果","imgurl":"/Public/uploads/industrys/59af65cac0566.jpg","cid":"2"},{"id":"446","name":"紫菜.挂面","imgurl":"/Public/uploads/industrys/59f5bc7819af1.jpg","cid":"2"},{"id":"272","name":"豆类食品","imgurl":"/Public/uploads/industrys/59af64389afa1.jpg","cid":"2"},{"id":"277","name":"翅·爪·脖·腿·蹄","imgurl":"/Public/uploads/industrys/59af64b82007c.jpg","cid":"2"},{"id":"285","name":"面包","imgurl":"/Public/uploads/industrys/59af6556a9e75.jpg","cid":"2"},{"id":"290","name":"薯片·薯条","imgurl":"/Public/uploads/industrys/59af65de6619b.jpg","cid":"2"},{"id":"447","name":"豆干卤蛋类","imgurl":"/Public/uploads/industrys/5a1281b81e269.jpg","cid":"2"},{"id":"274","name":"糕点","imgurl":"/Public/uploads/industrys/59af647c73076.jpg","cid":"2"},{"id":"282","name":"花生","imgurl":"/Public/uploads/industrys/59af65214967d.jpg","cid":"2"},{"id":"286","name":"膨化食品","imgurl":"/Public/uploads/industrys/59af656b44938.jpg","cid":"2"},{"id":"292","name":"小包麻辣","imgurl":"/Public/uploads/industrys/59af6642b1e02.jpg","cid":"2"},{"id":"448","name":"鱿鱼类·牛肉类","imgurl":"/Public/uploads/industrys/5a129519c3930.jpg","cid":"2"}]}]
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

    public static class DataBean {
        /**
         * name : 一线品牌
         * list : [{"id":"7","name":"方便面","imgurl":"/Public/uploads/industrys/5916cb7d93acd.jpg","cid":"2"},{"id":"55","name":"口香/润喉糖","imgurl":"/Public/uploads/industrys/5916d13432f7e.jpg","cid":"2"},{"id":"58","name":"鱼仔/脆骨","imgurl":"/Public/uploads/industrys/5916ce148aa55.jpg","cid":"2"}]
         */

        private String name;
        private String id;
        private List<TagGroup> list;

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

        public List<TagGroup> getList() {
            return list;
        }

        public void setList(List<TagGroup> list) {
            this.list = list;
        }

        public static class TagGroup implements Parcelable {
            /**
             * id : 7
             * name : 方便面
             * imgurl : /Public/uploads/industrys/5916cb7d93acd.jpg
             * cid : 2
             */

            private String id;
            private String name;
            private String imgurl;
            private String cid;
            private boolean isSort;
            private int position;
            private int index;

            public int getIndex() {
                return index;
            }

            public void setIndex(int index) {
                this.index = index;
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

            public String getCid() {
                return cid;
            }

            public void setCid(String cid) {
                this.cid = cid;
            }

            public int getPosition() {
                return position;
            }

            public void setPosition(int position) {
                this.position = position;
            }

            public TagGroup() {
            }

            public TagGroup(String name, boolean isSort) {
                this.name = name;
                this.isSort = isSort;
            }

            public TagGroup(String name, int index, boolean isSort) {
                this.name = name;
                this.index = index;
                this.isSort = isSort;
            }

            public TagGroup(String name, int position) {
                this.name = name;
                this.position = position;
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
                dest.writeString(this.cid);
                dest.writeByte(this.isSort ? (byte) 1 : (byte) 0);
                dest.writeInt(this.position);
                dest.writeInt(this.index);
            }

            protected TagGroup(Parcel in) {
                this.id = in.readString();
                this.name = in.readString();
                this.imgurl = in.readString();
                this.cid = in.readString();
                this.isSort = in.readByte() != 0;
                this.position = in.readInt();
                this.index = in.readInt();
            }

            public static final Parcelable.Creator<TagGroup> CREATOR = new Parcelable.Creator<TagGroup>() {
                @Override
                public TagGroup createFromParcel(Parcel source) {
                    return new TagGroup(source);
                }

                @Override
                public TagGroup[] newArray(int size) {
                    return new TagGroup[size];
                }
            };
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.status);
        dest.writeString(this.info);
        dest.writeList(this.data);
    }

    public IndustryCatelist() {
    }

    protected IndustryCatelist(Parcel in) {
        this.status = in.readInt();
        this.info = in.readString();
        this.data = new ArrayList<DataBean>();
        in.readList(this.data, DataBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<IndustryCatelist> CREATOR = new Parcelable.Creator<IndustryCatelist>() {
        @Override
        public IndustryCatelist createFromParcel(Parcel source) {
            return new IndustryCatelist(source);
        }

        @Override
        public IndustryCatelist[] newArray(int size) {
            return new IndustryCatelist[size];
        }
    };
}
