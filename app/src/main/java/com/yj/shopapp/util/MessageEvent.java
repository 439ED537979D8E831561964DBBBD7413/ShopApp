package com.yj.shopapp.util;

import com.yj.shopapp.ubeen.CartList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LK on 2017/12/28.
 *
 * @author LK
 */

public class MessageEvent {
    private int status;
    private boolean isCheck;
    private String message;
    private int fid;
    private List<CartList> cartLists;

    public MessageEvent() {
    }

    public MessageEvent(int status, int fid , boolean isCheck) {
        this.status = status;
        this.isCheck = isCheck;
        this.fid = fid;
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

    public MessageEvent(int status, int fid, List<CartList> cartLists) {
        this.status = status;
        this.fid = fid;
        this.cartLists = cartLists;
    }

    public MessageEvent(int status, List<CartList> cartLists) {
        this.status = status;
        this.cartLists = cartLists;
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

    public int getFid() {
        return fid;
    }

    public List<CartList> getCartLists() {
        if (cartLists == null) {
            return new ArrayList<>();
        }
        return cartLists;
    }
}
