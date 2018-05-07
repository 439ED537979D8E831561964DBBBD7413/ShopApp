package com.yj.shopapp.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by LK on 2018/4/4.
 *
 * @author LK
 */

@SuppressLint("AppCompatCustomView")
public class mTextView extends TextView {
    public mTextView(Context context) {
        super(context);
    }

    public mTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public mTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //设置单行
        setSingleLine();
        // 设置Ellipsize
        setEllipsize(TextUtils.TruncateAt.MARQUEE);
        // 获取焦点
        setFocusable(true);
        // 走马灯的重复次数，-1代表无限重复
        setMarqueeRepeatLimit(-1);
        // 强制获得焦点
        setFocusableInTouchMode(true);

    }

    @Override
    public boolean isFocused() {
        return true;
    }

}
