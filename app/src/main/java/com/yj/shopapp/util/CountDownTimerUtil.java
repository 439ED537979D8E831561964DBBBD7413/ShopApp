package com.yj.shopapp.util;

import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/5/24.
 */

public class CountDownTimerUtil {
    private static MyCountDownTimer myCountDownTimer;
    private static OnTickerListener listener;

    public static void startCountDown(TextView textView, long millisInFuture) {
        myCountDownTimer = new MyCountDownTimer(millisInFuture, textView);
        myCountDownTimer.start();
    }

    public static void cancelCountDown() {
        if (myCountDownTimer != null) {
            myCountDownTimer.cancel();
        }
    }

//    public static void startCountDown(long millure, OnTickerListener listener) {
//        myCountDownTimer = new MyCountDownTimer(millure, listener);
//        myCountDownTimer.start();
//    }

    public interface OnTickerListener {
        void onTicket(long millure);
    }

    private static class MyCountDownTimer extends CountDownTimer {

        private TextView mTextview;

        public MyCountDownTimer(long millisInFuture, TextView mTextview) {
            super(millisInFuture, 1000);
            this.mTextview = mTextview;
        }

        @Override
        public void onTick(long l) {
            mTextview.setText(DateUtils.timed(l));
        }

        @Override
        public void onFinish() {
            if (mTextview != null) {
                mTextview.setText("倒计时完成");
            }
            myCountDownTimer = null;
        }
    }
}