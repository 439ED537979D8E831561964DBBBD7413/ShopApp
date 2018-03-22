package com.yj.shopapp.ubeen;

/**
 * Created by LK on 2017/10/16.
 */

public class Extend {

    /**
     * id : 1
     * uid : 4
     * status : 0
     * rid : 2
     * reward : 100.00
     * num : 10
     * finish_num : 0
     * finish : 0%
     * username : 13611111111
     * shopname : 测试
     */
    private String id;
    private String uid;
    private String status;
    private String rid;
    private String reward;
    private String num;
    private String finish_num;
    private String finish;
    private String username;
    private String shopname;

    public int getId() {
        return Integer.parseInt(id);
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUid() {
        return Integer.parseInt(uid);
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getStatus() {
        return Integer.parseInt(status);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getRid() {
        return Integer.parseInt(rid);
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public int getNum() {
        return Integer.parseInt(num);
    }

    public void setNum(String num) {
        this.num = num;
    }

    public int getFinish_num() {
        return Integer.parseInt(finish_num);
    }

    public void setFinish_num(String finish_num) {
        this.finish_num = finish_num;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

}
