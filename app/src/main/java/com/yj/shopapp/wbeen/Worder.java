package com.yj.shopapp.wbeen;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huanghao on 2016/11/30.
 */

public class Worder implements Serializable{
    /**
     * username : 测试商店名称
     * oid : 201611301449534910
     * status : 1
     * preferentialprice : 0.00
     * money : 10.50
     * addtime : 1480486129
     * childorder : [{"oid":"20161130144953491012","status":"1","preferentialprice":"0.00","money":"10.50","addtime":"1480486140","bigtypename":null}]
     */

    private String username;
    private String oid;
    private String status;
    private String preferentialprice;
    private String money;
    private String addtime;
    private String sumnum ;
    private String sumtype ;
    private List<Worder.ChildorderBean> childorder;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getSumnum() {
        return sumnum;
    }

    public void setSumnum(String sumnum) {
        this.sumnum = sumnum;
    }

    public String getSumtype() {
        return sumtype;
    }

    public void setSumtype(String sumtype) {
        this.sumtype = sumtype;
    }

    public String getPreferentialprice() {
        return preferentialprice;
    }

    public void setPreferentialprice(String preferentialprice) {
        this.preferentialprice = preferentialprice;
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

    public List<Worder.ChildorderBean> getChildorder() {
        return childorder;
    }

    public void setChildorder(List<Worder.ChildorderBean> childorder) {
        this.childorder = childorder;
    }

    public static class ChildorderBean implements Serializable{
        /**
         * oid : 20161130144953491012
         * status : 1
         * preferentialprice : 0.00
         * money : 10.50
         * addtime : 1480486140
         * bigtypename : null
         */

        private String oid;
        private String status;
        private String preferentialprice;
        private String money;
        private String addtime;
        private Object bigtypename;

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

        public String getPreferentialprice() {
            return preferentialprice;
        }

        public void setPreferentialprice(String preferentialprice) {
            this.preferentialprice = preferentialprice;
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

        public Object getBigtypename() {
            return bigtypename;
        }

        public void setBigtypename(Object bigtypename) {
            this.bigtypename = bigtypename;
        }
    }
}
