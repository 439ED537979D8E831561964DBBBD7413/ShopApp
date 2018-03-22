package com.yj.shopapp.util;

/**
 * Created by LK on 2017/12/28.
 *
 * @author LK
 */

public class MessageEvent {
    private int status;
    private boolean isCheck;
    private String message;

    public MessageEvent(int status, boolean isCheck) {
        this.status = status;
        this.isCheck = isCheck;
    }

    public MessageEvent(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public MessageEvent(int status, boolean isCheck, String message) {
        this.status = status;
        this.isCheck = isCheck;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public String getMessage() {
        return message;
    }
}
