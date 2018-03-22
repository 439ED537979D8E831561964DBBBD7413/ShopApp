package com.yj.shopapp.ubeen;

/**
 * Created by LK on 2017/10/16.
 */

public class Reward {

    /**
     * id : 2
     * Reward : 100.00
     * type : 2
     * order_money : 1000
     * order_num : 10
     * addtime : 1596666666
     * remark : 推荐的用户下单累计满10次，且每次订单满1000元，可领取100元大红包（单次不满1000元不纳入统计）
     */

    private String id;
    private String reward;
    private String type;
    private String order_money;
    private String order_num;
    private String addtime;
    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getReward() {
        return Float.parseFloat(reward);
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrder_money() {
        return order_money;
    }

    public void setOrder_money(String order_money) {
        this.order_money = order_money;
    }

    public String getOrder_num() {
        return order_num;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
