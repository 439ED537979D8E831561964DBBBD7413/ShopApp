package com.yj.shopapp.ubeen;

/**
 * Created by Administrator on 2017/8/13 0013.
 */

public class NoticeHint {

    /**
     * id : 12
     * title : 每天中午13:00后下的订单第二天配送
     * addtime : 1496390999
     * type : 系统公告
     */

    private String id;
    private String title;
    private String addtime;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private String type;

    public NoticeHint(String id, String title, String addtime, String type, String content) {
        this.id = id;
        this.title = title;
        this.addtime = addtime;
        this.type = type;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
