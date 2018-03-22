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
    private boolean isSwitch;

    public boolean isSwitch() {
        return isSwitch;
    }

    public void setSwitch(boolean aSwitch) {
        isSwitch = aSwitch;
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

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public Classify() {
    }

    public Classify(String id, String name, String money, boolean isSwitch) {
        this.id = id;
        this.name = name;
        this.money = money;
        this.isSwitch = isSwitch;
    }
}
