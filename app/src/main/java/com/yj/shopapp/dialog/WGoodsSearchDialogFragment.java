package com.yj.shopapp.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
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

import com.alibaba.fastjson.JSONArray;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.ScreenLvAdpter;
import com.yj.shopapp.ui.activity.adapter.WGoodsAdapter;
import com.yj.shopapp.ui.activity.wholesale.WGoodsDetailActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.view.ClearEditText;
import com.yj.shopapp.wbeen.Goods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import ezy.ui.layout.LoadingLayout;

/**
 * Created by LK on 2018/5/23.
 *
 * @author LK
 */
public class WGoodsSearchDialogFragment extends BaseDialogFragment {

    @BindView(R.id.value_Et)
    ClearEditText valueEt;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.loading)
    LoadingLayout loading;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.title_view)
    LinearLayout titleView;
    @BindView(R.id.translucent_mask)
    View translucentMask;


    private int mCurrPage = 1;
    private List<Goods> goodsList = new ArrayList<>();
    private WGoodsAdapter newGoodsAdapter;
    private String bigtypeid = "";
    private String sale_status = "0";
    private PopupWindow pw;
    private View itemView;
    private ScreenLvAdpter screenLvAdpter;
    private String[] status = {"全部", "销售中", "停售中"};

    @Override
    protected int getLayoutId() {
        return R.layout.wsearchpwd_view;
    }

    public static WGoodsSearchDialogFragment newInstance(String typeid) {

        Bundle args = new Bundle();
        args.putString("typeid", typeid);
        WGoodsSearchDialogFragment fragment = new WGoodsSearchDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initData() {
        valueEt.setHint("请输入商品名或条码");
        if (loading != null) {
            loading.showContent();
        }
        bigtypeid = getArguments().getString("typeid");
        Refresh();
        newGoodsAdapter = new WGoodsAdapter(mActivity);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.addItemDecoration(new DDecoration(mActivity, getResources().getDrawable(R.drawable.recyviewdecoration1dp)));
        recyclerView.setAdapter(newGoodsAdapter);
        showKeyBoard(valueEt);
        newGoodsAdapter.setOnItemClickListener((parent, view, position, id) -> {
            Bundle bundle = new Bundle();
            bundle.putString("itemnoid", goodsList.get(position).getNumberid());
            bundle.putString("id", goodsList.get(position).getId());
            CommonUtils.goActivity(mActivity, WGoodsDetailActivity.class, bundle, false);
        });
        screenLvAdpter = new ScreenLvAdpter(mActivity, Arrays.asList(status));
        screenLvAdpter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sale_status = String.valueOf(position);
                screenLvAdpter.setDef(position);
            }
        });
        initpw();
        translucentMask.setVisibility(View.VISIBLE);
        new Handler().postDelayed(this::showPW, 200);
        valueEt.setOnClickListener(v -> {
            if (pw != null && pw.isShowing()) {

            } else {
                showPW();
                translucentMask.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initpw() {
        itemView = LayoutInflater.from(mActivity).inflate(R.layout.screening_view, null);
        RecyclerView recyclerView1 = itemView.findViewById(R.id.recycler_view);
        RecyclerView recyclerView2 = itemView.findViewById(R.id.recycler_view_2);
        recyclerView2.setVisibility(View.GONE);
        itemView.findViewById(R.id.tagTv).setVisibility(View.GONE);
        ((TextView) itemView.findViewById(R.id.classify_tv)).setText("状态");
        recyclerView1.setLayoutManager(new GridLayoutManager(mActivity, 3));
        recyclerView1.setAdapter(screenLvAdpter);
    }

    /**
     * 显示弹出窗
     */
    private void showPW() {
        pw = new PopupWindow(itemView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pw.setOutsideTouchable(false);
        pw.setFocusable(false);
        pw.setTouchable(true);
        pw.showAsDropDown(titleView);
        pw.setOnDismissListener(() -> translucentMask.setVisibility(View.GONE));
    }

    private void Refresh() {
        smartRefreshLayout.setHeaderHeight(50);
        smartRefreshLayout.setFooterHeight(50);
        smartRefreshLayout.setEnableRefresh(false);
        smartRefreshLayout.setOnLoadMoreListener((v) -> {
            mCurrPage++;
            refreshRequest(valueEt.getText().toString());
        });
        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        smartRefreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
    }

    private void refreshRequest(String keyWord) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", String.valueOf(mCurrPage));
        params.put("keyword", keyWord);
        params.put("industryid", bigtypeid);
        params.put("sale_status", sale_status);
        HttpHelper.getInstance().post(mActivity, Contants.PortA.GOODSITEMLIST, params, new OkHttpResponseHandler<String>(mActivity) {

            @Override
            public void onAfter() {
                super.onAfter();
                if (smartRefreshLayout != null) {
                    smartRefreshLayout.finishLoadMore();
                }
            }

            @Override
            public void onBefore() {
                super.onBefore();
                if (smartRefreshLayout != null) {
                    smartRefreshLayout.setEnableLoadMore(true);
                }
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (loading != null) {
                    loading.showContent();
                }
                if (JsonHelper.isRequstOK(json, mActivity)) {
                    goodsList.addAll(JSONArray.parseArray(json, Goods.class));
                    newGoodsAdapter.setList(goodsList);
                } else if (JsonHelper.getRequstOK(json) == 6) {
                    if (goodsList.size() == 0) {
                        if (loading != null) {
                            loading.showEmpty();
                        }
                    } else {
                        if (null != smartRefreshLayout) {
                            smartRefreshLayout.finishLoadMore();
                        }
                    }
                }

            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
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
                mCurrPage = 1;
                goodsList.clear();
                if (pw != null) {
                    pw.dismiss();
                }
                hideImm(valueEt);
                refreshRequest(valueEt.getText().toString());
                break;
        }
    }

}
