package com.yj.shopapp.ui.activity.wholesale;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ui.activity.ImgUtil.NewBaseFragment;
import com.yj.shopapp.ui.activity.Interface.OnItemChildViewOnClickListener;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.WStopGoodsAdapter;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.wbeen.Goods;

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
public class WStopGoodsFragment extends NewBaseFragment implements OnItemChildViewOnClickListener {


    @BindView(R.id.loading)
    LoadingLayout loading;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.stop_goods_recycler)
    RecyclerView stopGoodsRecycler;

    private int mCurrentPage = 1;
    private List<Goods> goodsList = new ArrayList<>();
    private WStopGoodsAdapter stopGoodsAdapter;
    private String cid = "0";
    private String keyword = "";
    private int stop_status;

    public static WStopGoodsFragment newInstance(String cid, int s) {

        Bundle args = new Bundle();
        args.putString("cid", cid);
        args.putInt("stop_status", s);
        WStopGoodsFragment fragment = new WStopGoodsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_wstop_goods;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        cid = Objects.requireNonNull(getArguments()).getString("cid");
        stop_status = getArguments().getInt("stop_status");
        Refresh();
//        StickyDecoration decoration = StickyDecoration.Builder
//                .init(position -> {
//                    //组名回调
//                    //获取组名，用于判断是否是同一组
//                    if (goodsList.size() > 0) {
//                        return DateUtils.timet(goodsList.get(position).getAddtime());
//                    }
//                    return "";
//                })
//                .setGroupBackground(Color.parseColor("#f4f5f9"))
//                .setGroupHeight(CommonUtils.dip2px(mActivity, 34))
//                .setDivideColor(Color.parseColor("#E3E3E3"))
//                .setGroupTextColor(Color.parseColor("#000000"))
//                .setDivideHeight(1)
//                .setGroupTextSize(DisplayUtil.sp2px(mActivity, 14))
//                .setTextSideMargin(CommonUtils.dip2px(mActivity, 14))
//                .build();
        stopGoodsAdapter = new WStopGoodsAdapter(mActivity, this, stop_status);
        stopGoodsRecycler.setLayoutManager(new LinearLayoutManager(mActivity));
        stopGoodsRecycler.addItemDecoration(new DDecoration(mActivity, getResources().getDrawable(R.drawable.recyviewdecoration1dp)));
        //stopGoodsRecycler.addItemDecoration(decoration);
        stopGoodsRecycler.setAdapter(stopGoodsAdapter);

    }

    @Override
    protected void initData() {
        if (isNetWork(mActivity)) {
            refreshRequest();
        }
    }

    private void Refresh() {
        smartRefreshLayout.setHeaderHeight(50);
        smartRefreshLayout.setFooterHeight(50);
        smartRefreshLayout.setOnRefreshListener((v) -> {
            mCurrentPage = 1;
            goodsList.clear();
            refreshRequest();
            if (null != smartRefreshLayout) {
                smartRefreshLayout.setNoMoreData(false);
            }
        });
        smartRefreshLayout.setOnLoadMoreListener((v) -> {
            mCurrentPage++;
            refreshRequest();
        });
        smartRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        smartRefreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
    }

    private void showDelectClint(final int position) {
        new MaterialDialog.Builder(mActivity)
                .content("是否将 【" + goodsList.get(position).getName() + "】 上架销售")
                .positiveText("是")
                .negativeText("否")
                .onPositive((materialDialog, dialogAction) -> delClient(position))
                .show();
    }


    private void refreshRequest() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", String.valueOf(mCurrentPage));
        params.put("cid", cid);
        params.put("keyword", keyword);
        HttpHelper.getInstance().post(mActivity, Contants.PortA.STOPLIST, params, new OkHttpResponseHandler<String>(mActivity) {

            @Override
            public void onAfter() {
                super.onAfter();
                if (smartRefreshLayout != null) {
                    smartRefreshLayout.finishLoadMore();
                    smartRefreshLayout.finishRefresh();
                }
            }


            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                if (loading != null) {
                    loading.showContent();
                }
                ShowLog.e(json);
                if (!json.equals("[]")) {
                    if (json.startsWith("[")) {
                        goodsList.addAll(JSONArray.parseArray(json, Goods.class));
                    }
                    stopGoodsAdapter.setList(goodsList);
                } else {
                    if (goodsList.size() == 0) {
                        if (smartRefreshLayout != null) {
                            smartRefreshLayout.finishRefresh();
                        }
                        if (null != loading) {
                            loading.showEmpty();
                        }
                    } else {
                        if (smartRefreshLayout != null) {
                            smartRefreshLayout.finishLoadMoreWithNoMoreData();
                        }
                    }
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                if (smartRefreshLayout != null) {
                    smartRefreshLayout.finishLoadMore();
                    smartRefreshLayout.finishRefresh();
                }
                if (null != loading) {
                    loading.showError();
                }
            }
        });

    }

    private void delClient(int pos) {
        //显示ProgressDialog
        final KProgressHUD progressDialog = growProgress(Contants.Progress.DELETE_ING);
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("idstr", goodsList.get(pos).getId());

        HttpHelper.getInstance().post(mActivity, Contants.PortA.delstopitem, params, new OkHttpResponseHandler<String>(mActivity) {

            @Override
            public void onAfter() {
                super.onAfter();
                progressDialog.dismiss();
            }

            @Override
            public void onBefore() {
                super.onBefore();
                progressDialog.show();
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.errorNo(json).equals("0")) {
                    goodsList.clear();
                    mCurrentPage = 1;
                    refreshRequest();
                } else {
                    JSONObject object = JSONObject.parseObject(json);
                    if (object.getInteger("status") == 1) {

                    } else {
                        showToast(object.getString("info"));
                    }
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToast(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
            }
        });
    }


    @Override
    public void onChildViewClickListener(View view, int position) {
        switch (view.getId()) {
            case R.id.delect_tv:
                if (stop_status == 1) {
                    showDelectClint(position);
                }
                break;
            case R.id.stopgoods_recycler_item:
                Bundle bundle = new Bundle();
                bundle.putString("itemnoid", goodsList.get(position).getNumberid());
                bundle.putString("id", goodsList.get(position).getId());
                CommonUtils.goActivity(mActivity, WGoodsDetailActivity.class, bundle);
                break;
        }
    }
}
