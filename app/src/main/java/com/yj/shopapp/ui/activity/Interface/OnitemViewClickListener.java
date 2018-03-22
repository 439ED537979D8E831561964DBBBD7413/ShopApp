package com.yj.shopapp.ui.activity.Interface;

import java.util.List;

/**
 * Created by LK on 2018/2/4.
 *
 * @author LK
 */

public interface OnitemViewClickListener<T> {
    void onViewClick(int position, List<T> mdeta);
}
