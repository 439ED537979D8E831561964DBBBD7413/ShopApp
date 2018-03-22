package com.yj.shopapp.wbeen;

/**
 * Created by Administrator on 2017/8/10 0010.
 */

public class BrandList {

    /**
     * id : 1
     * name : 康师傅
     */

    private String id;
    private String name;

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

    public BrandList(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public BrandList() {
    }
}
