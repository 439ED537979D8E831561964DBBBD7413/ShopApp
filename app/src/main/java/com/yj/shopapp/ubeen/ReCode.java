package com.yj.shopapp.ubeen;

/**
 * Created by LK on 2018/3/17.
 *
 * @author LK
 */

public class ReCode {
    private int status;
    private String code;

    public ReCode(String code) {
        this.code = code;
    }

    public ReCode(int status, String code) {
        this.status = status;
        this.code = code;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
