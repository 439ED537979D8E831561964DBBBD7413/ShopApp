package com.yj.shopapp.ui.activity.Interface;

import android.view.View;

/**
 * Created by LK on 2018/2/27.
 *
 * @author LK
 */

public interface GoodsItemListenter {
    void onClick(View V, int position);

    void onCheckBoxClick(int position, boolean isChecked);
}
