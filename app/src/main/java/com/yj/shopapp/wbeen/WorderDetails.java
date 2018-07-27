package com.yj.shopapp.wbeen;

/**
 * Created by LK on 2018/5/31.
 *
 * @author LK
 */
public class WorderDetails {
    private String name;
    private int status;

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public WorderDetails() {
    }

    public WorderDetails(String name, int status) {
        this.name = name;
        this.status = status;
    }
}
