package com.yj.shopapp.ui.activity.shopkeeper;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSONArray;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.dialog.BugGoodsV4Dialog;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.BoughtGoods;
import com.yj.shopapp.ui.activity.ImgUtil.NewBaseFragment;
import com.yj.shopapp.ui.activity.Interface.OnItemChildViewOnClickListener;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.BoughtRvAdpter;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.JsonHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import ezy.ui.layout.LoadingLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class BoughtGoodsFragment extends NewBaseFragment implements OnItemChildViewOnClickListener {
    @BindView(R.id.bought_rv)
    RecyclerView boughtRv;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.loading)
    LoadingLayout loading;

    private String cid;
    private List<BoughtGoods> boughtGoodsList = new ArrayList<>();
    private BoughtRvAdpter rvAdpter;
    private String orderNum;
    private String orderMoney;
    private int currPage = 1;

    public static BoughtGoodsFragment newInstance(String cid) {

        Bundle args = new Bundle();
        args.putString("cid", cid);
        BoughtGoodsFragment fragment = new BoughtGoodsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bought_goods;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        cid = Objects.requireNonNull(getArguments()).getString("cid");
        rvAdpter = new BoughtRvAdpter(mActivity, this);
        boughtRv.setLayoutManager(new LinearLayoutManager(mActivity));
        boughtRv.addItemDecoration(new DDecoration(mActivity, getResources().getDrawable(R.drawable.recyviewdecoration1dp)));
        boughtRv.setAdapter(rvAdpter);
        Refresh();
    }

    @Override
    protected void initData() {
        if (isNetWork(mActivity)) {
            boughtGoodsList.clear();
            requestData();
        }
    }

    private void Refresh() {
        smartRefreshLayout.setHeaderHeight(50);
        smartRefreshLayout.setFooterHeight(50);
        smartRefreshLayout.setOnRefreshListener(v -> {
            currPage = 1;
            boughtGoodsList.clear();
            smartRefreshLayout.setNoMoreData(false);
            requestData();
        });
        smartRefreshLayout.setOnLoadMoreListener(v -> {
            currPage++;
            requestData();
        });
        smartRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        smartRefreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
    }

    private void requestData() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("cid", cid);
        params.put("p", String.valueOf(currPage));
        params.put("order_num", orderNum);
        params.put("order_money", orderMoney);
        HttpHelper.getInstance().post(mActivity, Contants.PortU.HISTORY, params, new OkHttpResponseHandler<String>(mActivity) {
            @Override
            public void onResponse(Request request, String response) {
                super.onResponse(request, response);
                ShowLog.e(response);
                if (JsonHelper.isRequstOK(response, mActivity)) {
                    boughtGoodsList.addAll(JSONArray.parseArray(response, BoughtGoods.class));
                    rvAdpter.setList(boughtGoodsList);
                } else {
                    if (boughtGoodsList.size() > 0) {
                        //没有更多了
                        currPage--;
                        if (null != smartRefreshLayout) {
                            smartRefreshLayout.finishLoadMoreWithNoMoreData();
                        }
                    } else {
                        //为空
                        currPage--;
                    }
                }
            }

            @Override
            public void onAfter() {
                super.onAfter();
                if (smartRefreshLayout != null) {
                    smartRefreshLayout.finishLoadMore();
                    smartRefreshLayout.finishRefresh();
                }
                if (boughtGoodsList.size() > 0) {
                    if (null != loading) {
                        loading.showContent();
                    }
                } else {
                    if (null != loading) {
                        loading.showEmpty();
                    }
                }
            }
        });
    }

    @Override
    public void onChildViewClickListener(View view, int position) {
        switch (view.getId()) {
            case R.id.itemview:
                Bundle bundle = new Bundle();
                bundle.putString("goodsId", boughtGoodsList.get(position).getId());
                bundle.putString("unit", boughtGoodsList.get(position).getUnit());
                CommonUtils.goActivity(mActivity, SGoodsDetailActivity.class, bundle);
                break;
            case R.id.add_card:
                if (getAddressId().equals("")) {
                    new MaterialDialog.Builder(mActivity).title("温馨提示!")
                            .content("您暂未添加收货地址!")
                            .positiveText("去完善信息")
                            .negativeText("下次吧")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    CommonUtils.goActivity(mActivity, SAddressRefreshActivity.class, null);
                                    dialog.dismiss();
                                }
                            }).show();
                } else {
                    BugGoodsV4Dialog.newInstance(boughtGoodsList.get(position)).show(getFragmentManager(), "123") ;
                }
                break;
        }

    }

}
