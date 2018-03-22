package com.yj.shopapp.ubeen;

/**
 * Created by Administrator on 2017/8/8 0008.
 */

public class TagGroup {

    /**
     * id : 1
     * name : 康师傅
     */

    private String id;
    private String name;
    private String imgurl;
    private String cid;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
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
