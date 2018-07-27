package com.yj.shopapp.dialog;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
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
import com.yj.shopapp.ui.activity.adapter.WNewGoodsAdpate;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.DateUtils;
import com.yj.shopapp.view.ClearEditText;
import com.yj.shopapp.wbeen.Classify;
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
 * Created by LK on 2018/6/10.
 *
 * @author LK
 */
public class WNewGoodsSelectDialogFragment extends BaseDialogFragment {
    @BindView(R.id.goods_recy)
    RecyclerView goodsRecy;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.title_view)
    LinearLayout titleView;
    @BindView(R.id.loading)
    LoadingLayout loading;
    @BindView(R.id.translucent_mask)
    View translucentMask;
    @BindView(R.id.value_Et)
    ClearEditText valueEt;


    private List<Classify> classLists = new ArrayList<>();
    private ScreenLvAdpter screenLvAdpter;
    private ScreenLvAdpter screenLvAdpter2;
    private ScreenLvAdpter screenLvAdpter3;
    private ScreenLvAdpter screenLvAdpter4;
    private WNewGoodsAdpate goodsAdapter;
    private List<Goods> goodsList = new ArrayList<>();
    private int mCurrentPage = 1;
    private String keyword = "";
    private PopupWindow pw;
    private View itemView;
    private String price;
    private String date;
    private List<String> times = new ArrayList<>();
    private List<String> times2 = new ArrayList<>();
    private String cid;
    private String[] name = {"默认", "升序", "降序"};
    private String saleStatus;

    public static WNewGoodsSelectDialogFragment newInstance(List<Classify> classLists) {

        Bundle args = new Bundle();
        args.putParcelableArrayList("classify", (ArrayList<? extends Parcelable>) classLists);
        WNewGoodsSelectDialogFragment fragment = new WNewGoodsSelectDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.df_base_select_itemview;
    }

    @Override
    protected void initData() {
        valueEt.setHint("请输入商品名或条码");
        classLists = getArguments().getParcelableArrayList("classify");
        times = DateUtils.test(10, "MM-dd");
        times2 = DateUtils.test(10, "yyyy-MM-dd");
        screenLvAdpter = new ScreenLvAdpter(mActivity, classLists);
        screenLvAdpter2 = new ScreenLvAdpter(mActivity, times);
        screenLvAdpter3 = new ScreenLvAdpter(mActivity, Arrays.asList(name));
        screenLvAdpter4 = new ScreenLvAdpter(mActivity, Arrays.asList(new String[]{"全部", "销售中", "停售中"}));
        screenLvAdpter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cid = classLists.get(position).getCid();
                screenLvAdpter.setDef(position);
            }
        });
        screenLvAdpter2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                date = times2.get(position);
                screenLvAdpter2.setDef(position);
            }
        });
        screenLvAdpter3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                price = name[position];
                screenLvAdpter3.setDef(position);
            }
        });
        screenLvAdpter4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                screenLvAdpter4.setDef(position);
                saleStatus = String.valueOf(position);
            }
        });
        goodsAdapter = new WNewGoodsAdpate(mActivity);
        goodsRecy.setLayoutManager(new LinearLayoutManager(mActivity));
        goodsRecy.addItemDecoration(new DDecoration(mActivity, getResources().getDrawable(R.drawable.recyviewdecoration1dp)));
        goodsRecy.setAdapter(goodsAdapter);
        Refresh();
        initpw();
        if (loading != null) {
            loading.showContent();
        }
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
        itemView = LayoutInflater.from(mActivity).inflate(R.layout.screening_view3, null);
        RecyclerView recyclerView1 = itemView.findViewById(R.id.recycler_view);
        RecyclerView recyclerView2 = itemView.findViewById(R.id.recycler_view_2);
        RecyclerView recyclerView3 = itemView.findViewById(R.id.recycler_view_3);
        RecyclerView recyclerView4 = itemView.findViewById(R.id.recycler_view_4);
        ((TextView) itemView.findViewById(R.id.tagTv)).setText("时间");
        recyclerView1.setLayoutManager(new GridLayoutManager(mActivity, 4));
        recyclerView1.setAdapter(screenLvAdpter);
        recyclerView2.setLayoutManager(new GridLayoutManager(mActivity, 4));
        recyclerView2.setAdapter(screenLvAdpter2);
        recyclerView3.setLayoutManager(new GridLayoutManager(mActivity, 3));
        recyclerView3.setAdapter(screenLvAdpter3);
        recyclerView4.setLayoutManager(new GridLayoutManager(mActivity, 3));
        recyclerView4.setAdapter(screenLvAdpter4);
    }

    /**
     * 显示弹出窗
     */
    private void showPW() {
        pw = new PopupWindow(itemView, ViewGroup.LayoutParams.MATCH_PARENT, (int) (CommonUtils.screenHeight(mActivity)*0.4), true);
        pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pw.setOutsideTouchable(false);
        pw.setFocusable(false);
        pw.setTouchable(true);
        pw.showAsDropDown(titleView);
        pw.setOnDismissListener(() -> translucentMask.setVisibility(View.GONE));
    }

    public void refreshRequest() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("price", price);
        params.put("cid", cid);
        params.put("keyword", keyword);
        params.put("date", date);
        params.put("p", String.valueOf(mCurrentPage));
        params.put("sale_status", saleStatus);
        HttpHelper.getInstance().post(mActivity, Contants.PortA.TODAYNEWITEMS, params, new OkHttpResponseHandler<String>(mActivity) {

            @Override
            public void onAfter() {
                super.onAfter();
                if (smartRefreshLayout != null) {
                    smartRefreshLayout.finishLoadMore();
                    smartRefreshLayout.finishRefresh();
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
                if (!json.equals("[]")) {
                    goodsList.addAll(JSONArray.parseArray(json, Goods.class));
                    goodsAdapter.setList(goodsList);
                } else {
                    if (goodsList.size() == 0) {
                        if (smartRefreshLayout != null) {
                            smartRefreshLayout.finishRefresh();
                        }
                        if (loading != null) {
                            loading.showEmpty();
                        }
                    } else {
                        if (smartRefreshLayout != null) {
                            smartRefreshLayout.finishLoadMore();
                        }
                    }
                    mCurrentPage--;
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                if (smartRefreshLayout != null) {
                    smartRefreshLayout.finishLoadMore();
                    smartRefreshLayout.finishRefresh();
                }
                mCurrentPage--;
            }
        });
    }

    private void Refresh() {
        smartRefreshLayout.setHeaderHeight(50);
        smartRefreshLayout.setFooterHeight(50);
        smartRefreshLayout.setEnableRefresh(false);
        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.setOnLoadMoreListener((v) -> {
            mCurrentPage++;
            refreshRequest();
        });
        smartRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        smartRefreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
    }


    @OnClick({R.id.finish_tv, R.id.select_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.finish_tv:
                if (pw != null) {
                    pw.dismiss();
                }
                dismiss();
                break;
            case R.id.select_tv:
                keyword = valueEt.getText().toString();
                mCurrentPage = 1;
                goodsList.clear();
                if (pw != null) {
                    pw.dismiss();
                }
                hideImm(valueEt);
                refreshRequest();
                break;
        }
    }

    public void hideImm(EditText valuet) {
        InputMethodManager inputMethodManager = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(valuet.getWindowToken(), 0);
    }
}
