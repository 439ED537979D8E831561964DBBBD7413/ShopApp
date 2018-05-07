package com.yj.shopapp.ubeen;

/**
 * Created by LK on 2018/4/1.
 *
 * @author LK
 */

public class UserAccount {

    /**
     * status : 1
     * info : 有提现资料
     * data : {"uid":"495","type":"2","wx":"123456789","wx_name":"","zfb":"13688889999","zfb_name":"luokun"}
     */

    private int status;
    private String info;
    private DataBean data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * uid : 495
         * type : 2
         * wx : 123456789
         * wx_name :
         * zfb : 13688889999
         * zfb_name : luokun
         */

        private String uid;
        private String type;
        private String wx;
        private String wx_name;
        private String zfb;
        private String zfb_name;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getWx() {
            return wx;
        }

        public void setWx(String wx) {
            this.wx = wx;
        }

        public String getWx_name() {
            return wx_name;
        }

        public void setWx_name(String wx_name) {
            this.wx_name = wx_name;
        }

        public String getZfb() {
            return zfb;
        }

        public void setZfb(String zfb) {
            this.zfb = zfb;
        }

        public String getZfb_name() {
            return zfb_name;
        }

        public void setZfb_name(String zfb_name) {
            this.zfb_name = zfb_name;
        }
    }
}
