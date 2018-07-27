package com.yj.shopapp.ui.activity.shopkeeper;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.dialog.BugGoodsV4Dialog;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.Goods;
import com.yj.shopapp.ui.activity.Interface.GoodsItemListenter;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.SNewGoodsAdpter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.StatusBarUtil;
import com.yj.shopapp.view.ClearEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import ezy.ui.layout.LoadingLayout;

/**
 * Created by huanghao on 2016/11/11.
 */

public class SLikeCkeckActivity extends BaseActivity {

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

    private String keyword = "";
    private SNewGoodsAdpter GoodAdpter;
    private List<Goods> goodsList = new ArrayList<>();
    private int page = 1;
    @Override
    protected int getLayoutId() {
        return R.layout.wsearchpwd_view;
    }

    @Override
    protected void initData() {
        if (getIntent().hasExtra("itemNoid")) {
            keyword = getIntent().getStringExtra("itemNoid");
            valueEt.setText(keyword);
        }
        loading.showContent();
        valueEt.setHint("请输入商品名称或条码");
        GoodAdpter = new SNewGoodsAdpter(mContext);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new DDecoration(mContext, getResources().getDrawable(R.drawable.recyviewdecoration1dp)));
        recyclerView.setAdapter(GoodAdpter);
        Refresh();
        GoodAdpter.setListenter(new GoodsItemListenter() {
            @Override
            public void onClick(View V, int position) {
                switch (V.getId()) {
                    case R.id.itemview:
                        Bundle bundle = new Bundle();
                        bundle.putString("goodsId", goodsList.get(position).getId());
                        bundle.putString("unit", goodsList.get(position).getUnit());
                        CommonUtils.goActivityForResult(mContext, SGoodsDetailActivity.class, bundle, 19, false);
                        break;
                    case R.id.addcartTv:
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
                            BugGoodsV4Dialog.newInstance(goodsList.get(position)).show(getSupportFragmentManager(), "123");
                        }
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onCheckBoxClick(int position, boolean isChecked) {

            }
        });
        showKeyBoard(valueEt);
        if (!keyword.equals("")){
            requestData();
        }
    }

    private void Refresh() {
        smartRefreshLayout.setHeaderHeight(50);
        smartRefreshLayout.setFooterHeight(50);
        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.setEnableRefresh(false);
        smartRefreshLayout.setOnRefreshListener(refreshLayout -> {
            page = 1;
            goodsList.clear();
            requestData();
        });
        smartRefreshLayout.setOnLoadMoreListener(refreshLayout -> {
            page++;
            requestData();
        });
        smartRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        smartRefreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_4c4c4c), 30);
    }

    private void requestData() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("keyword", keyword);
        params.put("p", String.valueOf(page));
        HttpHelper.getInstance().post(mContext, Contants.PortU.ITEMLIST, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onResponse(Request request, String response) {
                super.onResponse(request, response);
                ShowLog.e(response);
                if (JsonHelper.isRequstOK(response, mContext)) {
                    goodsList.addAll(JSONArray.parseArray(response, Goods.class));
                    GoodAdpter.setList(goodsList);
                } else {
                    Toast.makeText(mContext, JSONObject.parseObject(response).getString("info"), Toast.LENGTH_SHORT).show();
                    page--;
                }
            }

            @Override
            public void onAfter() {
                super.onAfter();
                if (smartRefreshLayout != null) {
                    smartRefreshLayout.finishRefresh();
                    smartRefreshLayout.finishLoadMore();
                    smartRefreshLayout.setEnableLoadMore(true);
                }
            }
        });
    }

    @OnClick({R.id.finish_tv, R.id.search2Btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.finish_tv:
                finish();
                break;
            case R.id.search2Btn:
                if (!"".equals(valueEt.getText().toString())) {
                    hideImm(valueEt);
                    keyword = valueEt.getText().toString();
                    page = 1;
                    goodsList.clear();
                    requestData();
                } else {
                    valueEt.setError("输入不能为空");
                }
                break;
        }
    }
}
