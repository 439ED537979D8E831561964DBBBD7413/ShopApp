package com.yj.shopapp.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import com.gongwen.marqueen.MarqueeFactory;

/**
 * Created by LK on 2018/2/2.
 *
 * @author LK
 */

public class NoticeMF2 extends MarqueeFactory<TextView, String> {
    public NoticeMF2(Context mContext) {
        super(mContext);

    }

    @Override
    public TextView generateMarqueeItemView(String data) {
        TextView tv = new TextView(mContext);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        tv.setText(data);
        return tv;
    }
}
