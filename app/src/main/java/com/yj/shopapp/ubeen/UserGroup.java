package com.yj.shopapp.ubeen;

import java.io.Serializable;

/**
 * Created by jm on 2016/5/17.
 */
public class UserGroup implements Serializable{

    /**
     * id : 1
     * uid : 4
     * name : 普通
     * dis : 100
     * status : 1
     */

    private String id;
    private String uid;
    private String name;
    private String dis;
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDis() {
        return dis;
    }

    public void setDis(String dis) {
        this.dis = dis;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
