package com.yj.shopapp.wbeen;

import java.io.Serializable;

/**
 * Created by jm on 2016/4/28.
 */
public class Client implements Serializable{

    /**
     * useruid : 6
     * status : 1
     * shopname : 我是零售
     * username : 18622228888
     * mobile : 18622228888
     * contacts : 你大爷
     */

    private String useruid="";
    private String status="";
    private String shopname="";
    private String username="";
    private String mobile="";
    private String contacts="";
    private  String gname="";
    private String location="";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public String getUseruid() {
        return useruid;
    }

    public void setUseruid(String useruid) {
        this.useruid = useruid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }
}
