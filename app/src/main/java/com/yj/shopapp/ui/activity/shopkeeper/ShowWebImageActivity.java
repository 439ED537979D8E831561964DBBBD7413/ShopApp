package com.yj.shopapp.ui.activity.shopkeeper;

import com.bumptech.glide.Glide;
import com.yj.shopapp.R;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.view.ScaleImageView;

import butterknife.BindView;

/**
 * Created by LK on 2017/10/29.
 */

public class ShowWebImageActivity extends BaseActivity {
    @BindView(R.id.imgload)
    ScaleImageView imgload;



    @Override
    protected int getLayoutId() {
        return R.layout.activity_showimg;
    }

    @Override
    protected void initData() {
        if (getIntent().hasExtra("image")) {
            String url = getIntent().getStringExtra("image");
            Glide.with(mContext).load(url).into(imgload);
            imgload.setMaxZoom(4f);
        }
    }
}
