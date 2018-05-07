package com.yj.shopapp.util;

import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * Created by LK on 2018/4/4.
 *
 * @author LK
 */

public class MyCountDownTimer extends CountDownTimer {
    /**
     * @param millisInFuture    The number of millis in the future from the call
     * to {@link #start()} until the countdown is done and {@link #onFinish()}
     * is called.
     * @param countDownInterval The interval along the way to receive
     * {@link #onTick(long)} callbacks.
     */
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
    }
}
