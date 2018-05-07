package com.yj.shopapp.ubeen;

/**
 * Created by LK on 2018/4/13.
 *
 * @author LK
 */
public class CheckDatails {
    private String id;
    private Boolean isSelect;

    public String getId() {
        return id == null ? "" : id;
    }

    public Boolean getSelect() {
        return isSelect;
    }

    public CheckDatails(String id, Boolean isSelect) {
        this.id = id;
        this.isSelect = isSelect;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSelect(Boolean select) {
        isSelect = select;
    }
}
