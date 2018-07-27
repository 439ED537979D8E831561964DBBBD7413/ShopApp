package com.yj.shopapp.ui.activity.wholesale;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.gavin.com.library.StickyDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ui.activity.ImgUtil.NewBaseFragment;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.WNewGoodsAdpate;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DateUtils;
import com.yj.shopapp.util.DisplayUtil;
import com.yj.shopapp.wbeen.Goods;
import com.yj.shopapp.wbeen.MessgEvt;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
public class WNewGoodsFragment extends NewBaseFragment {

    @BindView(R.id.newgoods_recycler)
    RecyclerView newgoodsRecycler;
    @BindView(R.id.loading)
    LoadingLayout loading;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    private int currpage = 1;
    private String cid;
    private WNewGoodsAdpate goodsAdapter;
    private List<Goods> goodsList = new ArrayList<>();
    private String price = "";
    private String date = "";
    private String keyword = "";
    private String saleStatus;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_wnew_goods;
    }

    public static WNewGoodsFragment newInstance(String cid) {

        Bundle args = new Bundle();
        args.putString("cid", cid);
        WNewGoodsFragment fragment = new WNewGoodsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        cid = Objects.requireNonNull(getArguments()).getString("cid", cid);
        Refresh();
        StickyDecoration decoration = StickyDecoration.Builder
                .init(position -> {
                    //组名回调
                    //获取组名，用于判断是否是同一组
                    if (goodsList.size() > 0) {
                        return DateUtils.timet(goodsList.get(position).getAddtime(),"yyyy-MM-dd");
                    }
                    return "";
                })
                .setGroupBackground(Color.parseColor("#f4f5f9"))
                .setGroupHeight(CommonUtils.dip2px(mActivity, 34))
                .setDivideColor(Color.parseColor("#E3E3E3"))
                .setGroupTextColor(Color.parseColor("#000000"))
                .setDivideHeight(1)
                .setGroupTextSize(DisplayUtil.sp2px(mActivity, 14))
                .setTextSideMargin(CommonUtils.dip2px(mActivity, 14))
                .build();
        goodsAdapter = new WNewGoodsAdpate(mActivity);
        newgoodsRecycler.setLayoutManager(new LinearLayoutManager(mActivity));
        //newgoodsRecycler.addItemDecoration(new DDecoration(mActivity, getResources().getDrawable(R.drawable.recyviewdecoration1dp)));
        newgoodsRecycler.addItemDecoration(decoration);
        newgoodsRecycler.setAdapter(goodsAdapter);
        goodsAdapter.setOnItemClickListener((parent, view1, position, id) -> {
            Bundle bundle = new Bundle();
            bundle.putString("itemnoid", goodsList.get(position).getNumberid());
            bundle.putString("id", goodsList.get(position).getId());
            CommonUtils.goActivity(mActivity, WGoodsDetailActivity.class, bundle);
        });
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initData() {
        if (isNetWork(mActivity)) {
            refreshRequest();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessgEvt messgEvt) {
        switch (messgEvt.getStatus()) {
            case 1:
                price = messgEvt.getValue();
                currpage = 1;
                goodsList.clear();
                refreshRequest();
                break;
            case 2:
                date = messgEvt.getValue();
                currpage = 1;
                goodsList.clear();
                refreshRequest();
                break;
            case 3:
                saleStatus=messgEvt.getValue();
                currpage = 1;
                goodsList.clear();
                refreshRequest();
                break;
        }
    }

    private void Refresh() {
        smartRefreshLayout.setHeaderHeight(50);
        smartRefreshLayout.setFooterHeight(50);
        smartRefreshLayout.setOnRefreshListener((v) -> {
            currpage = 1;
            goodsList.clear();
            refreshRequest();
            if (null != smartRefreshLayout) {
                smartRefreshLayout.setNoMoreData(false);
            }
        });
        smartRefreshLayout.setOnLoadMoreListener((v) -> {
            currpage++;
            refreshRequest();
        });
        smartRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        smartRefreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
    }


    public void refreshRequest() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("price", price);
        params.put("cid", cid);
        params.put("keyword", keyword);
        params.put("date", date);
        params.put("p", String.valueOf(currpage));
        params.put("sale_status",saleStatus);
        HttpHelper.getInstance().post(mActivity, Contants.PortA.TODAYNEWITEMS, params, new OkHttpResponseHandler<String>(mActivity) {

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
                ShowLog.e(json);
                if (null != loading) {
                    loading.showContent();
                }
                if (!json.equals("[]")) {
                    goodsList.addAll(JSONArray.parseArray(json, Goods.class));
                    goodsAdapter.setList(goodsList);
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

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
