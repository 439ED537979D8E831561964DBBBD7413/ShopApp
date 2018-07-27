package com.yj.shopapp.dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.ubeen.IntegralDetail;
import com.yj.shopapp.util.DateUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LK on 2018/6/12.
 *
 * @author LK
 */
public class IntegraDtlDialogFragment extends BaseDialogFragment {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.integral_status)
    TextView integralStatus;
    @BindView(R.id.amount_tv)
    TextView amountTv;
    @BindView(R.id.order_id)
    TextView orderId;
    @BindView(R.id.integral_tv)
    TextView integralTv;
    @BindView(R.id.create_time)
    TextView createTime;
    @BindView(R.id.integral_dtl_Tv)
    TextView integralDtlTv;
    @BindView(R.id.order_rl)
    RelativeLayout orderRl;
    private IntegralDetail detail;

    public static IntegraDtlDialogFragment newInstance(IntegralDetail detail) {

        Bundle args = new Bundle();
        args.putParcelable("IntegralDetail", detail);
        IntegraDtlDialogFragment fragment = new IntegraDtlDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.df_integra_dtl_fragment;
    }

    @Override
    protected void initData() {
        detail = getArguments().getParcelable("IntegralDetail");
        if (detail == null) return;
        if (detail.getOrderid().equals("")) {
            orderRl.setVisibility(View.GONE);
        }
        integralStatus.setText(String.format("【%s】%s", Contants.OrderState[Integer.parseInt(detail.getType())], detail.getType_name()));
        amountTv.setText(String.format("￥%s", detail.getMoney()));
        orderId.setText(detail.getOrderid());
        integralTv.setText(detail.getIntegral());
        createTime.setText(DateUtils.timet(detail.getTime(), "yyyy-M-dd  HH: mm"));
        integralDtlTv.setText(detail.getRemark());
    }

    @OnClick(R.id.forewadImg)
    public void onViewClicked() {
        dismiss();
    }

}
