package com.yj.shopapp.ubeen;

/**
 * Created by LK on 2017/12/28.
 *
 * @author LK
 */

public class EventMassg {
    private int status;
    private boolean ischeck;
    private String siteid;
    private int cid;
    private String message;
    public String getSiteid() {
        return siteid;
    }

    public EventMassg(String message) {
        this.message = message;
    }

    public EventMassg(int status, int cid) {
        this.status = status;
        this.cid = cid;
    }

    public EventMassg(int status, String siteid, int cid) {
        this.status = status;
        this.siteid = siteid;
        this.cid = cid;
    }

    public EventMassg(int status, boolean ischeck, int cid) {
        this.status = status;
        this.ischeck = ischeck;
        this.cid = cid;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public boolean isIscheck() {
        return ischeck;
    }

    public int getCid() {
        return cid;
    }
}
