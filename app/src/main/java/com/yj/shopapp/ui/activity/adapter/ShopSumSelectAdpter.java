package com.yj.shopapp.ui.activity.adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import com.yj.shopapp.R;
import com.yj.shopapp.ui.activity.ImgUtil.Common2Adapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;

import java.util.List;

/**
 * Created by LK on 2018/3/26.
 *
 * @author LK
 */

public class ShopSumSelectAdpter extends Common2Adapter<Integer> {
    private String mUnit;

    public ShopSumSelectAdpter(Context context, List list, String unit) {
        super(context, list);
        mUnit = unit;
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.view_shopsumitem;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Integer Number = list.get(position);
        holder.getTextView(R.id.itemsum).setText(String.format("+%1$d%2$s", Number, mUnit));
    }
}
