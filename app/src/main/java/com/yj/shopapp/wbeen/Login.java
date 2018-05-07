package com.yj.shopapp.wbeen;

/**
 * Created by jm on 2016/4/28.
 */
public class Login {

    private String uid;
    private String utype;
    private String token;
    private String islogin;
    private String agentuid;
    private String areaid;
    private int is_vip;
    private String customer_service_phone;
    private String address;

    public String getAddress() {
        return address == null ? "" : address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCustomer_service_phone() {
        return customer_service_phone == null ? "" : customer_service_phone;
    }

    public void setCustomer_service_phone(String customer_service_phone) {
        this.customer_service_phone = customer_service_phone;
    }

    public int getIs_vip() {
        return is_vip;
    }

    public void setIs_vip(int is_vip) {
        this.is_vip = is_vip;
    }

    public String getIslogin() {
        return islogin;
    }

    public void setIslogin(String islogin) {
        this.islogin = islogin;
    }

    public String getAgentuid() {
        return agentuid;
    }

    public void setAgentuid(String agentuid) {
        this.agentuid = agentuid;
    }

    public String getAreaid() {
        return areaid;
    }

    public void setAreaid(String areaid) {
        this.areaid = areaid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUtype() {
        return utype;
    }

    public void setUtype(String utype) {
        this.utype = utype;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
