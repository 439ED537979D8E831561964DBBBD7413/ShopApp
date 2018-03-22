package com.yj.shopapp.ubeen;

import java.util.List;

/**
 * Created by LK on 2017/12/22.
 *
 * @author LK
 */

public class NewOrder {

    /**
     * agentuser : 多个批发商
     * oid : 201801051698481001
     * status : 1
     * data : [{"money":"41.80","itemnum":"1","name":"厨具类","id":"17"}]
     * sumtype : 1
     * sumnum : 1
     * money : 41.8
     * addtime : 1515139563
     * coupon : 0
     * receipt : 41.8
     */

    private String agentuser;
    private String oid;
    private String status;
    private int sumtype;
    private int sumnum;
    private String money;
    private String addtime;
    private String coupon;
    private String receipt;
    private List<DataBean> data;

    public String getAgentuser() {
        return agentuser;
    }

    public void setAgentuser(String agentuser) {
        this.agentuser = agentuser;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getSumtype() {
        return sumtype;
    }

    public void setSumtype(int sumtype) {
        this.sumtype = sumtype;
    }

    public int getSumnum() {
        return sumnum;
    }

    public void setSumnum(int sumnum) {
        this.sumnum = sumnum;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * money : 41.80
         * itemnum : 1
         * name : 厨具类
         * id : 17
         */

        private String money;
        private String itemnum;
        private String name;
        private String id;

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getItemnum() {
            return itemnum;
        }

        public void setItemnum(String itemnum) {
            this.itemnum = itemnum;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
