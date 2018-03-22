package com.yj.shopapp.ui.activity.shopkeeper;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.yj.shopapp.R;
import com.yj.shopapp.ubeen.EventMassg;
import com.yj.shopapp.ui.activity.adapter.OrderViewPageAdpter;
import com.yj.shopapp.ui.activity.base.BaseFragment;
import com.yj.shopapp.util.CommonUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jm on 2016/4/25.
 */
public class SOrderActivity extends BaseFragment {
    @BindView(R.id.tabs_tl)
    TabLayout tabsTl;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    private OrderViewPageAdpter adpter;
    private int[] status = {0, 1, 4, 3};

    @Override
    public void init(Bundle savedInstanceState) {
        adpter = new OrderViewPageAdpter(getFragmentManager(), status);
        viewpager.setAdapter(adpter);
        tabsTl.setupWithViewPager(viewpager);
    }

    @Override
    public int getLayoutID() {
        return R.layout.stab_order;
    }

    /**
     * 1.订单统计
     * 2.搜索
     * 3.订单记录
     *
     * @param view
     */
    @OnClick({R.id.order_count, R.id.search_rl, R.id.orderRecord})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.order_count:
                CommonUtils.goActivity(mActivity, PieChartActivity.class, null);
                break;
            case R.id.search_rl:
                new MaterialDialog.Builder(mActivity).title("请输入要查询的关键字")
                        .inputType(InputType.TYPE_CLASS_TEXT).input("请输入关键字 订单号", "", false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {

                    }
                }).negativeText("取消").positiveText("确定").onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String msg = dialog.getInputEditText().getText().toString();
                        if (!"".equals(msg)) {
                            EventBus.getDefault().post(new EventMassg(msg));
                        }
                    }
                }).show();
                break;
            case R.id.orderRecord:
                CommonUtils.goActivity(mActivity, GoodsRecord.class, null);
                break;
        }
    }
}