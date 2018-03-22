package com.yj.shopapp.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gongwen.marqueen.MarqueeFactory;
import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.NotMfData;

/**
 * From ShopApp
 * Created by wxq on 17/3/27.
 * <p>
 * Beautiful Life ～
 */

public class NoticeMF extends MarqueeFactory<RelativeLayout, NotMfData> {
    private LayoutInflater inflater;

    public NoticeMF(Context mContext) {
        super(mContext);
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public RelativeLayout generateMarqueeItemView(NotMfData data) {
        RelativeLayout ll = (RelativeLayout) inflater.inflate(R.layout.noticemfxml, null);
        ((TextView) ll.findViewById(R.id.mf_content)).setText(data.getContent());
        ((TextView) ll.findViewById(R.id.mf_content_1)).setText(data.getContent_1());
        if (data.getNum_1().equals("-1")) {
            ll.findViewById(R.id.mf_content_1).setVisibility(View.INVISIBLE);
        }
        return ll;
    }

}
