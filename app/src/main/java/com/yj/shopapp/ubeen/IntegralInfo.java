package com.yj.shopapp.ubeen;

/**
 * From ShopApp
 * Created by wxq on 17/4/1.
 * <p>
 * Beautiful Life ～
 */

public class IntegralInfo {


    /**
     * id : 3
     * uid : 277
     * integral : 71540
     * ratio : 0.002
     * min_limit : 2000
     * remark : 1.积分获取 : 在平台上消费1元获得1积分，积分不上限；2. 积分兑现 :  1000积分=2元；3. 最低兑现额度: 2000积分。
     * rmb : 264
     * changeintegral : 61801
     */

    private int id;
    private String uid;
    private long integral;
    private String ratio;
    private long min_limit;
    private String remark;
    private long rmb;
    private long changeintegral;
    private String ranking;

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getIntegral() {
        return integral;
    }

    public void setIntegral(long integral) {
        this.integral = integral;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public long getMin_limit() {
        return min_limit;
    }

    public void setMin_limit(long min_limit) {
        this.min_limit = min_limit;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public long getRmb() {
        return rmb;
    }

    public void setRmb(long rmb) {
        this.rmb = rmb;
    }

    public long getChangeintegral() {
        return changeintegral;
    }

    public void setChangeintegral(long changeintegral) {
        this.changeintegral = changeintegral;
    }
}
