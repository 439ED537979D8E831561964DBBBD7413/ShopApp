package com.yj.shopapp.ui.activity.shopkeeper;

import android.os.Bundle;
import android.widget.TextView;

import com.yj.shopapp.R;
import com.yj.shopapp.ui.activity.base.BaseFragment;

import butterknife.BindView;

/**
 * Created by jm on 2016/4/25.
 * <p/>
 * 我的购物车
 */
public class SCartActivity extends BaseFragment{

    @BindView(R.id.title)
    TextView title;

    @Override
    public void init(Bundle savedInstanceState) {

        title.setText("我的购物车");

    }

    @Override
    public int getLayoutID() {
        return R.layout.wtab_client;
    }


}
