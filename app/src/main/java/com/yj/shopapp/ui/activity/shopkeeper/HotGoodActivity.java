package com.yj.shopapp.ui.activity.shopkeeper;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSONArray;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.HotIndex;
import com.yj.shopapp.ui.activity.Interface.OnItemChildViewOnClickListener;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.HotIndexAdpter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.BugGoodsDialog;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.StatusBarUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import ezy.ui.layout.LoadingLayout;

public class HotGoodActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener, OnItemChildViewOnClickListener {


    @BindView(R.id.my_RecyclerView)
    RecyclerView myRecyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SmartRefreshLayout swipeRefreshLayout;
    @BindView(R.id.loading)
    LoadingLayout loading;
    @BindView(R.id.forewadImg)
    ImageView forewadImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    ImageView idRightBtu;
    @BindView(R.id.title_layout)
    RelativeLayout titleLayout;
    private List<HotIndex> hotIndexList = new ArrayList<>();
    private HotIndexAdpter adpter;
    private int p = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_hot_good;
    }

    @Override
    protected void initData() {
        idRightBtu.setVisibility(View.GONE);
        title.setText("热门商品");
        Refresh();
        adpter = new HotIndexAdpter(mContext, this);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        myRecyclerView.setAdapter(adpter);
        myRecyclerView.addItemDecoration(new DDecoration(mContext, getResources().getDrawable(R.drawable.recyviewdecoration1dp)));
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtils.from(HotGoodActivity.this)
                .setActionbarView(titleLayout)
                .setTransparentStatusbar(true)
                .setLightStatusBar(false)
                .process();
    }

    private void Refresh() {
        swipeRefreshLayout.setHeaderHeight(50);
        swipeRefreshLayout.setFooterHeight(50);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setOnLoadMoreListener(this);
        swipeRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        swipeRefreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isNetWork(mContext)) {
            hotIndexList.clear();
            RequestData();
        }
    }

    private void RequestData() {
        Map<String, String> params = new HashMap<>(16);
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", String.valueOf(p));
        HttpHelper.getInstance().post(mContext, Contants.PortS.HOT_INDEX, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                p--;

                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.finishRefresh(false);
                }
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.finishLoadMore(false);
                }
            }

            @Override
            public void onAfter() {
                super.onAfter();
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.finishRefresh(true);
                }
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.finishLoadMore(true);
                }
            }

            @Override
            public void onResponse(Request request, String response) {
                super.onResponse(request, response);
                ShowLog.e(response);
                if (JsonHelper.isRequstOK(response, mContext)) {
                    hotIndexList.addAll(JSONArray.parseArray(response, HotIndex.class));
                    adpter.setList(hotIndexList);
                    if (loading != null) {
                        loading.showContent();
                    }
                } else if (JsonHelper.getRequstOK(response) == 6) {
                    p--;
                    if (hotIndexList.size() > 0) {
                        if (swipeRefreshLayout != null) {
                            swipeRefreshLayout.finishLoadMoreWithNoMoreData();
                        }
                    } else {
                        if (loading != null) {
                            loading.showEmpty();
                        }
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
                bundle.putString("goodsId", hotIndexList.get(position).getId());
                CommonUtils.goActivity(mContext, SGoodsDetailActivity.class, bundle);
                break;
            case R.id.goCarlist:
                if (hotIndexList.get(position).getSale_status().equals("0")) {

                } else {
                    if (getAddressId().equals("")) {
                        new MaterialDialog.Builder(mContext).title("温馨提示!")
                                .content("您暂未添加收货地址!")
                                .positiveText("去完善信息")
                                .negativeText("下次吧")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        CommonUtils.goActivity(mContext, SAddressRefreshActivity.class, null);
                                        dialog.dismiss();
                                    }
                                }).show();
                    } else {
                        BugGoodsDialog.newInstance(hotIndexList.get(position)).show(getFragmentManager(), "123");
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        p++;
        RequestData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        p = 1;
        hotIndexList.clear();
        RequestData();
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setNoMoreData(false);
        }
    }

}
