package com.yj.shopapp.ui.activity.wholesale;

import android.os.Bundle;
import android.widget.TextView;

import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.DateUtils;
import com.yj.shopapp.util.PreferenceUtils;

import butterknife.BindView;

/**
 * Created by huang on 2016/9/12.
 */
public class WDuetimeActivity extends BaseActivity {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.time_tv)
    TextView time_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wduetime;
    }

    @Override
    protected void initData() {
        title.setText("软件到期时间");
        String time=PreferenceUtils.getPrefString(mContext, Contants.Preference.DUETIME, "");
        time_tv.setText("软件到期时间为："+DateUtils.getDateToString(PreferenceUtils.getPrefString(mContext, Contants.Preference.DUETIME, "") + "000"));
    }


}
