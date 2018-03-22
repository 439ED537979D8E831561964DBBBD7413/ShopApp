package com.yj.shopapp.wbeen;

/**
 * Created by jm on 2016/5/3.
 */
public class Imglist {

    /**
     * id : 18
     * imgurl :
     * numberid : 123456
     * str : sss
     */

    private String id;
    private String imgurl;
    private String numberid;
    private String str;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getNumberid() {
        return numberid;
    }

    public void setNumberid(String numberid) {
        this.numberid = numberid;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public Imglist(){

    }

    public Imglist(String id, String imgurl,String numberid, String str){
        this.id = id;
        this.imgurl = imgurl;
        this.numberid = numberid;
        this.str = str;
    }
}
