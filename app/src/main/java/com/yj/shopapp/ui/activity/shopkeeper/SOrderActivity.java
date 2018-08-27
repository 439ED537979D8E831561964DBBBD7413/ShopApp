package com.yj.shopapp.ui.activity.shopkeeper;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.ui.activity.ImgUtil.NewBaseFragment;
import com.yj.shopapp.ui.activity.adapter.OrderViewPageAdpter;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.StatusBarUtils;
import com.yj.shopapp.view.CustomViewPager;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jm on 2016/4/25.
 */
public class SOrderActivity extends NewBaseFragment {
    @BindView(R.id.tabs_tl)
    TabLayout tabsTl;
    @BindView(R.id.viewpager)
    CustomViewPager viewpager;
    @BindView(R.id.title_view)
    LinearLayout titleView;
    @BindView(R.id.search_tv)
    TextView searchTv;
    @BindView(R.id.orderRecord)
    ImageView orderRecord;

    private OrderViewPageAdpter adpter;
    private int[] status = {0, 1, 4, 3};

    @Override
    protected int getLayoutId() {
        return R.layout.stab_order;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        if (Contants.isNotch) {
            StatusBarUtils.from(getActivity())
                    .setActionbarView(titleView)
                    .setTransparentStatusbar(true)
                    .setLightStatusBar(false)
                    .process();
        }
        adpter = new OrderViewPageAdpter(getFragmentManager(), status);
        viewpager.setOpenAnimation(false);
        viewpager.setScanScroll(false);

    }

    @Override
    protected void initData() {
        viewpager.setAdapter(adpter);
        tabsTl.setupWithViewPager(viewpager);
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
                FragmentSearchBoxSelect.newInstance(1).show(getFragmentManager(), "searchBox");
                break;
            case R.id.orderRecord:
                showPopwpwindows();
                break;
        }
    }

    private void showPopwpwindows() {
        View rootView = LayoutInflater.from(mActivity).inflate(R.layout.pwd_orderhistory_select, null);
        PopupWindow pw = new PopupWindow(rootView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        TextView goGoodsHistory = rootView.findViewById(R.id.go_goodshistory);
        TextView goReturnGoods = rootView.findViewById(R.id.go_returngoods);
        goGoodsHistory.setOnClickListener(v -> {
            CommonUtils.goActivity(mActivity, GoodsRecord.class, null);
            pw.dismiss();
        });
        goReturnGoods.setOnClickListener(v -> {
            CommonUtils.goActivity(mActivity, ReturnGoodsHistory.class, null);
            pw.dismiss();
        });

        pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pw.setOutsideTouchable(true);
        pw.setTouchable(true);
        backgroundAlpha(0.8f);
        pw.showAsDropDown(orderRecord, 0, 20);
        pw.setOnDismissListener(() -> backgroundAlpha(1f));
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        mActivity.getWindow().setAttributes(lp);
    }

}