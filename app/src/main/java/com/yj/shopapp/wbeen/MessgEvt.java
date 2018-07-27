package com.yj.shopapp.wbeen;

/**
 * Created by LK on 2018/5/25.
 *
 * @author LK
 */
public class MessgEvt {
    private int status;
    private String value;
    private String cid;

    public String getCid() {
        return cid == null ? "" : cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public MessgEvt(int status, String value) {
        this.status = status;
        this.value = value;
    }

    public MessgEvt(int status, String value, String cid) {
        this.status = status;
        this.value = value;
        this.cid = cid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getValue() {
        return value == null ? "" : value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
