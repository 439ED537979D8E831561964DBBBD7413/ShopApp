package com.yj.shopapp.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSONArray;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.BoughtGoods;
import com.yj.shopapp.ui.activity.Interface.OnItemChildViewOnClickListener;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.BoughtRvAdpter;
import com.yj.shopapp.ui.activity.adapter.ScreenLvAdpter;
import com.yj.shopapp.ui.activity.shopkeeper.SAddressRefreshActivity;
import com.yj.shopapp.ui.activity.shopkeeper.SGoodsDetailActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.PreferenceUtils;
import com.yj.shopapp.view.ClearEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import ezy.ui.layout.LoadingLayout;

/**
 * Created by LK on 2018/8/24.
 *
 * @author LK
 */
public class BoughtGoodsDialog extends BaseV4DialogFragment implements OnItemChildViewOnClickListener {
    @BindView(R.id.value_Et)
    ClearEditText valueEt;
    @BindView(R.id.title_view)
    LinearLayout titleView;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.loading)
    LoadingLayout loading;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.translucent_mask)
    View translucentMask;
    private int currPage = 1;
    private List<BoughtGoods> boughtGoodsList = new ArrayList<>();
    private int orderNum = 0;
    private int orderMoney = 0;
    private int status = 0;
    private BoughtRvAdpter rvAdpter;
    private String shopName;
    private PopupWindow pw;
    private ScreenLvAdpter screenLvAdpter, screenLvAdpter2, screenLvAdpter3;
    private View itemView;

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_boughtgoods;
    }

    @Override
    protected void initData() {
        rvAdpter = new BoughtRvAdpter(mActivity, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.addItemDecoration(new DDecoration(mActivity, getResources().getDrawable(R.drawable.recyviewdecoration1dp)));
        recyclerView.setAdapter(rvAdpter);
        initpw();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showKeyBoard(valueEt);
                showPW();
                translucentMask.setVisibility(View.VISIBLE);
            }
        }, 200);
        Refresh();
        if (null != loading) {
            loading.showContent();
        }
        valueEt.setOnClickListener(v -> {
            if (pw != null && pw.isShowing()) {

            } else {
                showPW();
                translucentMask.setVisibility(View.VISIBLE);
            }
        });

    }

    private void initpw() {
        screenLvAdpter = new ScreenLvAdpter(mActivity);
        screenLvAdpter.setList(Arrays.asList("全部", "降序", "升序"));
        screenLvAdpter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                orderNum = position;
                screenLvAdpter.setDef(position);
            }
        });
        screenLvAdpter2 = new ScreenLvAdpter(mActivity);
        screenLvAdpter2.setList(Arrays.asList("全部", "降序", "升序"));
        screenLvAdpter2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                orderMoney = position;
                screenLvAdpter2.setDef(position);
            }
        });
        screenLvAdpter3 = new ScreenLvAdpter(mActivity);
        screenLvAdpter3.setList(Arrays.asList("销售中", "补货中", "已下架"));
        screenLvAdpter3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                status = position + 1;
                screenLvAdpter3.setDef(position);
            }
        });
        itemView = LayoutInflater.from(mActivity).inflate(R.layout.screening_view2, null);
        ((TextView) itemView.findViewById(R.id.classify_tv2)).setText("数量排序");
        ((TextView) itemView.findViewById(R.id.tagTv2)).setText("金额排序");
        ((TextView) itemView.findViewById(R.id.tagTv3)).setText("状态");
        RecyclerView recyclerView1 = itemView.findViewById(R.id.classify_2_rv);
        RecyclerView recyclerView2 = itemView.findViewById(R.id.status_2_rv);
        RecyclerView recyclerView3 = itemView.findViewById(R.id.status_3_rv);
        recyclerView1.setLayoutManager(new GridLayoutManager(mActivity, 3));
        recyclerView2.setLayoutManager(new GridLayoutManager(mActivity, 3));
        recyclerView3.setLayoutManager(new GridLayoutManager(mActivity, 3));
        recyclerView1.setAdapter(screenLvAdpter);
        recyclerView2.setAdapter(screenLvAdpter2);
        recyclerView3.setAdapter(screenLvAdpter3);
        screenLvAdpter3.setDef(-1);
    }

    /**
     * 显示弹出窗
     */
    private void showPW() {
        pw = new PopupWindow(itemView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // pw.setOutsideTouchable(true);
        pw.setTouchable(true);
        pw.setOutsideTouchable(false);
        pw.setFocusable(false);
        pw.showAsDropDown(titleView);
        pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (translucentMask != null) {
                    translucentMask.setVisibility(View.GONE);
                }
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
        smartRefreshLayout.setEnableRefresh(false);
        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        smartRefreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
    }

    private void requestData() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", String.valueOf(currPage));
        params.put("order_num", String.valueOf(orderNum));
        params.put("order_money", String.valueOf(orderMoney));
        params.put("status", String.valueOf(status));
        params.put("name", shopName);
        // ShowLog.e("cid" + cid + "p" + currPage + "orderNum" + orderNum + "orderMoney" + orderMoney);
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

            @Override
            public void onBefore() {
                super.onBefore();
                if (smartRefreshLayout != null) {
                    smartRefreshLayout.setEnableLoadMore(true);
                }
            }
        });
    }

    @OnClick({R.id.finish_tv, R.id.search2Btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.finish_tv:
                if (pw != null) {
                    pw.dismiss();
                }
                dismiss();
                break;
            case R.id.search2Btn:
                if (pw != null) {
                    pw.dismiss();
                }
                hideImm(valueEt);
                boughtGoodsList.clear();
                currPage = 1;
                shopName = valueEt.getText().toString();
                requestData();
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
                CommonUtils.goActivity(mActivity, SGoodsDetailActivity.class, bundle);
                break;
            case R.id.add_card:
                if (boughtGoodsList.get(position).getDolistcart() == 0) return;
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
                    BugGoodsV4Dialog.newInstance(boughtGoodsList.get(position)).show(getChildFragmentManager(), "123");
                }
                break;
        }
    }

    protected String getAddressId() {
        return PreferenceUtils.getPrefString(mActivity, "addressId", "");
    }

}
