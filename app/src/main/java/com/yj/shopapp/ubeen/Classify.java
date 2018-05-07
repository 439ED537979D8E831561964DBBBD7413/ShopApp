package com.yj.shopapp.ubeen;

/**
 * Created by LK on 2018/1/3.
 *
 * @author LK
 */

public class Classify {


    /**
     * id : 2
     * name : 副食区
     * money : 16.50
     */

    private String id;
    private String name;
    private String money;
    private int page;
    private boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMoney() {
        return money == null ? "" : money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public Classify() {
    }

    public Classify(String id, String name, String money) {
        this.id = id;
        this.name = name;
        this.money = money;
    }
}
