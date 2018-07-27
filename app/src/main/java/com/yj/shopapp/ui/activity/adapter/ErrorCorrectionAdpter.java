package com.yj.shopapp.ui.activity.adapter;

import android.content.Context;

import com.yj.shopapp.R;
import com.yj.shopapp.ui.activity.ImgUtil.Common2Adapter;
import com.yj.shopapp.ui.activity.ImgUtil.ViewHolder;
import com.yj.shopapp.wbeen.WorderDetails;

/**
 * Created by LK on 2018/5/31.
 *
 * @author LK
 */
public class ErrorCorrectionAdpter extends Common2Adapter<WorderDetails> {
    public ErrorCorrectionAdpter(Context context) {
        super(context);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.errorcorrection_recy_item;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WorderDetails details = list.get(position);
        holder.getTextView(R.id.itemname).setText(details.getName());
        if (details.getStatus() == 0) {
            holder.getCheckBox(R.id.my_checkbox).setChecked(false);
        } else {
            holder.getCheckBox(R.id.my_checkbox).setChecked(true);
        }
    }
}
