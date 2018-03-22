package com.yj.shopapp.ubeen;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * From ShopApp
 * Created by wxq on 17/3/31.
 * <p>
 * Beautiful Life ～
 */

public class Notice implements Parcelable {

    private String id;
    private String type;
    private String title;
    private String content;
    private long addtime;
    private String classify;
    private String url;

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getAddtime() {
        return addtime;
    }

    public void setAddtime(long addtime) {
        this.addtime = addtime;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.type);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeLong(this.addtime);
        dest.writeString(this.classify);
        dest.writeString(this.url);
    }

    public Notice() {
    }

    protected Notice(Parcel in) {
        this.id = in.readString();
        this.type = in.readString();
        this.title = in.readString();
        this.content = in.readString();
        this.addtime = in.readLong();
        this.classify = in.readString();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<Notice> CREATOR = new Parcelable.Creator<Notice>() {
        @Override
        public Notice createFromParcel(Parcel source) {
            return new Notice(source);
        }

        @Override
        public Notice[] newArray(int size) {
            return new Notice[size];
        }
    };
}
