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
