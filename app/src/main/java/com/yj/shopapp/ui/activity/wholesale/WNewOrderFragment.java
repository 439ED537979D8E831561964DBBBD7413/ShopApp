package com.yj.shopapp.ui.activity.wholesale;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ui.activity.ImgUtil.NewBaseFragment;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.WNewOrderAdpter;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.wbeen.Worder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class WNewOrderFragment extends NewBaseFragment{



    private WNewOrderAdpter adapter;
    private int mCurrentPage;
    private List<Worder> orderList = new ArrayList<>();

    public static WNewOrderFragment newInstance(int type) {
        Bundle args = new Bundle();
        WNewOrderFragment fragment = new WNewOrderFragment();
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    private int getType() {
        return getArguments().getInt("type");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_snew_order;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        adapter = new WNewOrderAdpter(mActivity, orderList);

    }

    @Override
    protected void initData() {
        if (NetUtils.isNetworkConnected(mActivity)) {
            refreshRequest();
        } else {
            showToast("无网络");
        }
    }

    public void refreshRequest() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", String.valueOf(mCurrentPage));
        params.put("ostatus", getType() + "");
        HttpHelper.getInstance().post(mActivity, Contants.PortA.ORDERS, params, new OkHttpResponseHandler<String>(mActivity) {

            @Override
            public void onAfter() {
                super.onAfter();

            }

            @Override
            public void onBefore() {
                super.onBefore();
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToast(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);

            }
        });

    }

}
