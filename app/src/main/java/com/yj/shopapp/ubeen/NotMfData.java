package com.yj.shopapp.ubeen;

/**
 * Created by lk on 2017/10/6.
 */

public class NotMfData {
    private String num;
    private String content;
    private String num_1;
    private String content_1;

    public NotMfData() {
    }

    public String getNum_1() {

        return num_1;
    }

    public void setNum_1(String num_1) {
        this.num_1 = num_1;
    }

    public String getContent_1() {
        return content_1;
    }

    public NotMfData(String num, String content, String num_1, String content_1) {
        this.num = num;
        this.content = content;
        this.num_1 = num_1;
        this.content_1 = content_1;
    }

    public void setContent_1(String content_1) {
        this.content_1 = content_1;
    }

    public String getNum() {

        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
