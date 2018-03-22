package com.yj.shopapp.ubeen;

import java.io.Serializable;

/**
 * Created by huanghao on 2016/11/25.
 */

public class Province implements Serializable{
    /**
     * id : 26227
     * area_name : 广东省
     * is_reg : 1
     * is_open : 1
     */

    private String id;
    private String area_name;
    private String is_reg;
    private String is_open;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    public String getIs_reg() {
        return is_reg;
    }

    public void setIs_reg(String is_reg) {
        this.is_reg = is_reg;
    }

    public String getIs_open() {
        return is_open;
    }

    public void setIs_open(String is_open) {
        this.is_open = is_open;
    }
}
