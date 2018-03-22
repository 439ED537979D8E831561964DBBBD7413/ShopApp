package com.yj.shopapp.ubeen;

import java.io.Serializable;

/**
 * Created by jm on 2016/5/14.
 */
public class Address implements Serializable{

    /**
     * id : 3
     * shopname : 2号店
     * mobile : 186
     * contacts : 本人
     * tel : 0769
     * address : 广东省东莞市就是在那里
     * status : 1
     */

    private String id;
    private String shopname;
    private String mobile;
    private String contacts;
    private String tel;
    private String address;
    private String status;

    public Address() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
