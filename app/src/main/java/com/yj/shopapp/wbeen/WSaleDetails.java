package com.yj.shopapp.wbeen;

/**
 * Created by LK on 2018/5/28.
 *
 * @author LK
 */
public class WSaleDetails {

    /**
     * status : 1
     * data : {"id":"1119","time1":"1522339200","time2":"1527782399","remark":"东鹏特价"}
     */

    private int status;
    private DataBean data;
    private String info;

    public String getInfo() {
        return info == null ? "" : info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1119
         * time1 : 1522339200
         * time2 : 1527782399
         * remark : 东鹏特价
         */

        private String id;
        private String time1;
        private String time2;
        private String remark;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTime1() {
            return time1;
        }

        public void setTime1(String time1) {
            this.time1 = time1;
        }

        public String getTime2() {
            return time2;
        }

        public void setTime2(String time2) {
            this.time2 = time2;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }
}
