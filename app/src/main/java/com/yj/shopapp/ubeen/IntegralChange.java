package com.yj.shopapp.ubeen;

/**
 * From ShopApp
 * Created by wxq on 17/4/5.
 * <p>
 * Beautiful Life ～
 */

public class IntegralChange {

    /**
     * id : 6
     * accountnumber : 13662829560
     * changetype : 0
     * changetime : 1490431327
     * edittime : 0
     * rmb : 50
     * integral : 10000
     * status : 1
     * remark : null
     */

    private int id;
    private String accountnumber;
    private int changetype;
    private long changetime;
    private String edittime;
    private String rmb;
    private String integral;
    private int status;
    private String remark;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccountnumber() {
        return accountnumber;
    }

    public void setAccountnumber(String accountnumber) {
        this.accountnumber = accountnumber;
    }

    public int getChangetype() {
        return changetype;
    }

    public void setChangetype(int changetype) {
        this.changetype = changetype;
    }

    public long getChangetime() {
        return changetime;
    }

    public void setChangetime(long changetime) {
        this.changetime = changetime;
    }

    public String getEdittime() {
        return edittime;
    }

    public void setEdittime(String edittime) {
        this.edittime = edittime;
    }

    public String getRmb() {
        return rmb;
    }

    public void setRmb(String rmb) {
        this.rmb = rmb;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
