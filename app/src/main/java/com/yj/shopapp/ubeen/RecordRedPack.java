package com.yj.shopapp.ubeen;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LK on 2017/10/23.
 * 红包记录
 * @author LK
 */

public class RecordRedPack implements Parcelable {

    /**
     * id : 34
     * reward_type : 2
     * uid : 201
     * type : 1
     * accountnumber : 15573462029
     * changetime : 1508735592
     * edittime : 0
     * reward : 100
     * remark :
     * shopname : 广裕百货
     */

    private String id;
    private String reward_type;
    private String uid;
    private String type;
    private String accountnumber;
    private String changetime;
    private String edittime;
    private String reward;
    private String remark;
    private String shopname;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReward_type() {
        return reward_type;
    }

    public void setReward_type(String reward_type) {
        this.reward_type = reward_type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAccountnumber() {
        return accountnumber;
    }

    public void setAccountnumber(String accountnumber) {
        this.accountnumber = accountnumber;
    }

    public String getChangetime() {
        return changetime;
    }

    public void setChangetime(String changetime) {
        this.changetime = changetime;
    }

    public String getEdittime() {
        return edittime;
    }

    public void setEdittime(String edittime) {
        this.edittime = edittime;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    @Override
    public String toString() {
        return "RecordRedPack{" +
                "id='" + id + '\'' +
                ", reward_type='" + reward_type + '\'' +
                ", uid='" + uid + '\'' +
                ", type='" + type + '\'' +
                ", accountnumber='" + accountnumber + '\'' +
                ", changetime='" + changetime + '\'' +
                ", edittime='" + edittime + '\'' +
                ", reward='" + reward + '\'' +
                ", remark='" + remark + '\'' +
                ", shopname='" + shopname + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.reward_type);
        dest.writeString(this.uid);
        dest.writeString(this.type);
        dest.writeString(this.accountnumber);
        dest.writeString(this.changetime);
        dest.writeString(this.edittime);
        dest.writeString(this.reward);
        dest.writeString(this.remark);
        dest.writeString(this.shopname);
        dest.writeString(this.status);
    }

    public RecordRedPack() {
    }

    protected RecordRedPack(Parcel in) {
        this.id = in.readString();
        this.reward_type = in.readString();
        this.uid = in.readString();
        this.type = in.readString();
        this.accountnumber = in.readString();
        this.changetime = in.readString();
        this.edittime = in.readString();
        this.reward = in.readString();
        this.remark = in.readString();
        this.shopname = in.readString();
        this.status = in.readString();
    }

    public static final Parcelable.Creator<RecordRedPack> CREATOR = new Parcelable.Creator<RecordRedPack>() {
        @Override
        public RecordRedPack createFromParcel(Parcel source) {
            return new RecordRedPack(source);
        }

        @Override
        public RecordRedPack[] newArray(int size) {
            return new RecordRedPack[size];
        }
    };
}
