package com.yj.shopapp.ubeen;

/**
 * Created by LK on 2018/3/3.
 *
 * @author LK
 */

public class RefreshListCar {
    private String kw;
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getKw() {
        return kw == null ? "" : kw;
    }

    public void setKw(String kw) {
        this.kw = kw;
    }

    public RefreshListCar() {
    }

    public RefreshListCar(String kw) {
        this.kw = kw;
    }

    public RefreshListCar(String kw, int status) {
        this.kw = kw;
        this.status = status;
    }
}
