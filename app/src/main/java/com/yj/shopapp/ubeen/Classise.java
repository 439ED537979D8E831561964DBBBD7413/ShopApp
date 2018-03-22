package com.yj.shopapp.ubeen;

import java.io.Serializable;

/**
 * Created by huanghao on 2016/10/31.
 */

public class Classise implements Serializable {
    /**
     * id : 12
     * name : 纸巾类
     * imgurl : http://u.19diandian.com/Public/uploads/20161016/5803998159031.jpg
     */

    private String id;
    private String name;
    private String imgurl;
    private String ishot;
    private String supplierid;


    public Classise() {
    }

    public Classise(String id, String name, String imgurl) {
        this.id = id;
        this.name = name;
        this.imgurl = imgurl;
    }

    public String getIshot() {
        return ishot;
    }

    public void setIshot(String ishot) {
        this.ishot = ishot;
    }

    public String getSupplierid() {
        return supplierid;
    }

    public void setSupplierid(String supplierid) {
        this.supplierid = supplierid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
}
