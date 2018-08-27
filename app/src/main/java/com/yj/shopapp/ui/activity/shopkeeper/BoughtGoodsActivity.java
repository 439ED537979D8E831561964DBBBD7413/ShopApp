package com.yj.shopapp.ui.activity.shopkeeper;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSONArray;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.dialog.BoughtGoodsDialog;
import com.yj.shopapp.dialog.BugGoodsV4Dialog;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.BoughtGoods;
import com.yj.shopapp.ubeen.Industry;
import com.yj.shopapp.ui.activity.Interface.OnItemChildViewOnClickListener;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.BoughtRvAdpter;
import com.yj.shopapp.ui.activity.adapter.ScreenLvAdpter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.StatusBarUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import ezy.ui.layout.LoadingLayout;

public class BoughtGoodsActivity extends BaseActivity implements OnItemChildViewOnClickListener {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.title_layout)
    ConstraintLayout titleLayout;
    @BindView(R.id.bought_rv)
    RecyclerView boughtRv;
    @BindView(R.id.loading)
    LoadingLayout loading;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.view_transparent)
    View viewTransparent;
    @BindView(R.id.flipping)
    ImageView flipping;
    @BindView(R.id.screenTv)
    RelativeLayout screenTv;
    private List<Industry> mdatas = new ArrayList<Industry>();
    private BoughtRvAdpter rvAdpter;
    private int currPage = 1;
    private List<BoughtGoods> boughtGoodsList = new ArrayList<>();
    private int orderNum;
    private String cid = "0";
    private int orderMoney;
    private PopupWindow pw;
    private ScreenLvAdpter screenLvAdpter, screenLvAdpter2, screenLvAdpter3;
    private View itemView;
    private int status = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bought_goods;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtils.from(BoughtGoodsActivity.this)
                .setActionbarView(titleLayout)
                .setTransparentStatusbar(true)
                .setLightStatusBar(false)
                .process();
    }

    @Override
    protected void initData() {
        title.setText("购买记录");
        idRightBtu.setText("搜索");
        rvAdpter = new BoughtRvAdpter(mContext, this);
        boughtRv.setLayoutManager(new LinearLayoutManager(mContext));
        boughtRv.addItemDecoration(new DDecoration(mContext, getResources().getDrawable(R.drawable.recyviewdecoration1dp)));
        boughtRv.setAdapter(rvAdpter);
        initpw();
        Refresh();
        if (isNetWork(mContext)) {
            requestIndustry();
            requestData();
        }
    }

    private void initpw() {
        screenLvAdpter = new ScreenLvAdpter(mContext);
        screenLvAdpter.setList(Arrays.asList("全部", "降序", "升序"));
        screenLvAdpter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boughtGoodsList.clear();
                screenLvAdpter.setDef(position);
                if (pw != null) {
                    pw.dismiss();
                }
                orderNum = position;
                currPage = 1;
                requestData();
            }
        });
        screenLvAdpter2 = new ScreenLvAdpter(mContext);
        screenLvAdpter2.setList(Arrays.asList("全部", "降序", "升序"));
        screenLvAdpter2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boughtGoodsList.clear();
                screenLvAdpter2.setDef(position);
                if (pw != null) {
                    pw.dismiss();
                }
                orderMoney = position;
                currPage = 1;
                requestData();
            }
        });
        screenLvAdpter3 = new ScreenLvAdpter(mContext);
        screenLvAdpter3.setList(Arrays.asList("销售中", "补货中", "已下架"));
        screenLvAdpter3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boughtGoodsList.clear();
                screenLvAdpter3.setDef(position);
                if (pw != null) {
                    pw.dismiss();
                }
                status = position + 1;
                currPage = 1;
                requestData();
            }
        });
        itemView = LayoutInflater.from(mContext).inflate(R.layout.screening_view2, null);
        ((TextView) itemView.findViewById(R.id.classify_tv2)).setText("数量排序");
        ((TextView) itemView.findViewById(R.id.tagTv2)).setText("金额排序");
        ((TextView) itemView.findViewById(R.id.tagTv3)).setText("状态");
        RecyclerView recyclerView1 = itemView.findViewById(R.id.classify_2_rv);
        RecyclerView recyclerView2 = itemView.findViewById(R.id.status_2_rv);
        RecyclerView recyclerView3 = itemView.findViewById(R.id.status_3_rv);
        recyclerView1.setLayoutManager(new GridLayoutManager(mContext, 3));
        recyclerView2.setLayoutManager(new GridLayoutManager(mContext, 3));
        recyclerView3.setLayoutManager(new GridLayoutManager(mContext, 3));
        recyclerView1.setAdapter(screenLvAdpter);
        recyclerView2.setAdapter(screenLvAdpter2);
        recyclerView3.setAdapter(screenLvAdpter3);
        screenLvAdpter3.setDef(-1);
    }

    /**
     * 显示弹出窗
     */
    private void showPW() {
        flipping.setRotation(180);
        pw = new PopupWindow(itemView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pw.setOutsideTouchable(true);
        pw.setTouchable(true);
        pw.showAsDropDown(screenTv);
        pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                viewTransparent.setVisibility(View.GONE);
                flipping.setRotation(0);
            }
        });
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

    private void requestIndustry() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(mContext, Contants.PortU.Classifylist, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onResponse(Request request, String response) {
                super.onResponse(request, response);
                ShowLog.e(response);
                if (JsonHelper.isRequstOK(response, mContext)) {
                    mdatas = JSONArray.parseArray(response, Industry.class);
                    setTabLayout();
                } else {
                    showToastShort(JsonHelper.errorMsg(response));
                }
            }
        });
    }

    private void setTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("全部"));
        for (Industry i : mdatas) {
            tabLayout.addTab(tabLayout.newTab().setText(i.getName()));
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                orderNum = 0;
                orderMoney = 0;
                if (position == 0) {
                    cid = "0";
                } else {
                    cid = mdatas.get(position - 1).getId();
                }
                currPage = 1;
                boughtGoodsList.clear();
                requestData();
                loading.showLoading();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void requestData() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("cid", cid);
        params.put("p", String.valueOf(currPage));
        params.put("order_num", String.valueOf(orderNum));
        params.put("order_money", String.valueOf(orderMoney));
        params.put("status", String.valueOf(status));
        // ShowLog.e("cid" + cid + "p" + currPage + "orderNum" + orderNum + "orderMoney" + orderMoney);
        HttpHelper.getInstance().post(mContext, Contants.PortU.HISTORY, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onResponse(Request request, String response) {
                super.onResponse(request, response);
                ShowLog.e(response);
                if (JsonHelper.isRequstOK(response, mContext)) {
                    boughtGoodsList.addAll(JSONArray.parseArray(response, BoughtGoods.class));
                    rvAdpter.setList(boughtGoodsList);
                    if (boughtGoodsList.size() > 0 && response.equals("[]")) {
                        //没有更多了
                        if (null != smartRefreshLayout) {
                            smartRefreshLayout.finishLoadMoreWithNoMoreData();
                        }
                        currPage--;
                    }
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

    @OnClick({R.id.finish_tv, R.id.id_right_btu, R.id.screenTv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.finish_tv:
                finish();
                break;
            case R.id.id_right_btu:
                //搜索
                new BoughtGoodsDialog().show(getSupportFragmentManager(), "bought");
                break;
            case R.id.screenTv:
                showPW();
                viewTransparent.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onChildViewClickListener(View view, int position) {
        switch (view.getId()) {
            case R.id.itemview:
                Bundle bundle = new Bundle();
                bundle.putString("goodsId", boughtGoodsList.get(position).getId());
                bundle.putString("unit", boughtGoodsList.get(position).getUnit());
                CommonUtils.goActivity(mContext, SGoodsDetailActivity.class, bundle);
                break;
            case R.id.add_card:
                if (boughtGoodsList.get(position).getDolistcart() == 0) return;
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
                    BugGoodsV4Dialog.newInstance(boughtGoodsList.get(position)).show(getSupportFragmentManager(), "123");
                }
                break;
        }
    }

}
