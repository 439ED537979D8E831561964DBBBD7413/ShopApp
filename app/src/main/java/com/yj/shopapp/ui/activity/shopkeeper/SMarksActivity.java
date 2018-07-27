package com.yj.shopapp.ui.activity.shopkeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.dialog.BugGoodsDialog;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.Goods;
import com.yj.shopapp.ui.activity.Interface.GoodsItemListenter;
import com.yj.shopapp.ui.activity.Interface.OnViewScrollListenter;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.SNewGoodsAdpter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.StatusBarUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jm on 2016/5/14.
 */
public class SMarksActivity extends BaseActivity implements GoodsItemListenter, OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    TextView id_right_btu;
    @BindView(R.id.cart_total_price_tv)
    TextView carttotalpriceTv;
    @BindView(R.id.swipe_refresh_layout)
    SmartRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.forewadImg)
    ImageView forewadImg;
    @BindView(R.id.checkBox)
    CheckBox choose;
    @BindView(R.id.allchoose)
    LinearLayout allchoose;
    @BindView(R.id.submit)
    LinearLayout submit;
    @BindView(R.id.title_view)
    RelativeLayout titleView;

    private boolean isRequesting = false;//标记，是否正在刷新
    private int mCurrentPage = 1;
    private List<Goods> goodsList = new ArrayList<>();
    String agentuid = ""; //临时批发商ID 做个判断
    boolean isAllChoose = false;
    private SNewGoodsAdpter GoodAdpter;

    @Override
    protected int getLayoutId() {
        return R.layout.sactivity_marks;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtils.from(this)
                .setActionbarView(titleView)
                .setTransparentStatusbar(true)
                .setLightStatusBar(false)
                .process();
    }

    @Override
    protected void initData() {
        title.setText("收藏商品");
        id_right_btu.setVisibility(View.GONE);
        GoodAdpter = new SNewGoodsAdpter(mContext, true);
        GoodAdpter.setListenter(this);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerView.addItemDecoration(new DDecoration(mContext));
            recyclerView.addOnScrollListener(new OnViewScrollListenter() {
                @Override
                public void onBottom() {
                    mCurrentPage++;
                    refreshRequest();
                }
            });
            recyclerView.setAdapter(GoodAdpter);
        }
        Refresh();
        if (isNetWork(mContext)) {
            refreshRequest();
        }
    }

    private void Refresh() {
        swipeRefreshLayout.setHeaderHeight(50);
        swipeRefreshLayout.setFooterHeight(50);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setOnLoadMoreListener(this);
        swipeRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        swipeRefreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
    }

    public void countMoney() {
        int count = 0;
        for (int i = 0; i < goodsList.size(); i++) {
            if (goodsList.get(i).isSelected()) {
                count++;
            }
        }
        carttotalpriceTv.setText(count + "种");
    }

    @OnClick({R.id.allchoose, R.id.submit, R.id.checkBox})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.allchoose:
                if (goodsList.size() > 0) {
                    isAllChoose = !isAllChoose;
                    choose.setChecked(isAllChoose);
                    for (Goods g : goodsList) {
                        g.setSelected(isAllChoose);
                    }
                    GoodAdpter.notifyDataSetChanged();
                    countMoney();
                } else {
                    showToastShort("请添加商品");
                }
                break;
            case R.id.checkBox:
                if (goodsList.size() > 0) {
                    isAllChoose = !isAllChoose;
                    choose.setChecked(isAllChoose);
                    for (Goods g : goodsList) {
                        g.setSelected(isAllChoose);
                    }
                    GoodAdpter.notifyDataSetChanged();
                    countMoney();
                } else {
                    showToastShort("请添加商品");
                }
                break;
            case R.id.submit:
                boolean Selected = false;
                for (Goods g : goodsList) {
                    if (g.isSelected()) {
                        Selected = true;
                    }
                }
                if (Selected) {
                    new MaterialDialog.Builder(mContext)
                            .content("是否删除选中的商品?")
                            .positiveText("是")
                            .negativeText("否")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                                    delCartReport();
                                }
                            })
                            .show();
                } else {
                    showToastShort("请选择商品");
                }
                break;
            default:
                break;
        }
    }

    private void delCartReport() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < goodsList.size(); i++) {
            if (goodsList.get(i).isSelected()) {
                builder.append(goodsList.get(i).getId());
                builder.append("|");
                //  del(i, true);
            }
        }
        if (builder.toString().length() > 0) {
            String idstr = builder.substring(0, builder.length() - 1);
            //执行删除
            del(idstr);
        } else {
            showToastShort("请选择商品");
        }
    }

    @Override
    public void onClick(View V, int position) {
        switch (V.getId()) {
            case R.id.itemview:
                Bundle bundle = new Bundle();
                bundle.putString("goodsId", goodsList.get(position).getId());
                bundle.putString("unit", goodsList.get(position).getUnit());
                bundle.putBoolean("Collect", true);
                CommonUtils.goActivityForResult(mContext, SGoodsDetailActivity.class, bundle, 19, false);
                break;
            case R.id.addcartTv:
                //showAlertDialog(position);
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
                    BugGoodsDialog.newInstance(goodsList.get(position)).show(getFragmentManager(), "123");
                }
                break;
            default:
                break;

        }
    }

    @Override
    public void onCheckBoxClick(int position, boolean isChecked) {
        goodsList.get(position).setSelected(isChecked);
        isAllSelected();
        countMoney();
    }

    public void isAllSelected() {
        for (Goods g : goodsList) {
            if (!g.isSelected()) {
                choose.setChecked(false);
                return;
            }
        }
        choose.setChecked(true);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == SChooseAgentActivity.CHOOSEAGENT_TYPE_WHAT) {
            Bundle bundle = new Bundle();
            agentuid = data.getExtras().getString("agentuid");
            if (isNetWork(mContext)) {
                swipeRefreshLayout.autoRefresh();
            }
        }
    }


    /***网络数据***********************************************************/

    public void refreshRequest() {
        if (isRequesting) return;
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", String.valueOf(mCurrentPage));
        params.put("agentuid", WId);
        ShowLog.e(mCurrentPage + "");
        HttpHelper.getInstance().post(mContext, Contants.PortU.BookMark, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.finishRefresh(true);
                    swipeRefreshLayout.finishLoadMore(true);
                }
                isRequesting = false;
            }

            @Override
            public void onBefore() {
                super.onBefore();
                isRequesting = true;
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JsonHelper<Goods> jsonHelper = new JsonHelper<Goods>(Goods.class);
                    goodsList.addAll(jsonHelper.getDatas(json));
                    GoodAdpter.setList(goodsList);
                } else if (JsonHelper.getRequstOK(json) == 6) {
                    mCurrentPage--;
                    if (swipeRefreshLayout != null) {
                        swipeRefreshLayout.finishRefresh(false);
                        swipeRefreshLayout.finishLoadMoreWithNoMoreData();
                    }
                } else {
                    if (swipeRefreshLayout != null) {
                        swipeRefreshLayout.finishRefresh(false);
                        swipeRefreshLayout.finishLoadMoreWithNoMoreData();
                    }
                    showToastShort(JsonHelper.errorMsg(json));
                    mCurrentPage--;
                }
                GoodAdpter.notifyDataSetChanged();

            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
                goodsList.clear();
                mCurrentPage--;
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.finishLoadMore(false);
                    swipeRefreshLayout.finishRefresh(false);
                }
                GoodAdpter.notifyDataSetChanged();
            }
        });

    }


    public void del(String idstr) {
        if (isRequesting)
            return;

        //显示ProgressDialog
        //final KProgressHUD progressDialog = growProgress(Contants.Progress.DELETE_ING);
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
//        params.put("id", goodsList.get(pos).getId());
        params.put("idstr", idstr);

        HttpHelper.getInstance().post(mContext, Contants.PortU.DelBookmark, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
                // progressDialog.dismiss();
            }

            @Override
            public void onBefore() {
                super.onBefore();
                //progressDialog.show();
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                carttotalpriceTv.setText("0");
                //choose.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_shopcart_unhook));
                if (JsonHelper.isRequstOK(json, mContext)) {
                    showToastShort("删除成功");
                    if (swipeRefreshLayout!=null){
                        swipeRefreshLayout.autoRefresh();
                    }
                } else {
                    showToastShort(JsonHelper.errorMsg(json));
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
            }
        });
    }


    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mCurrentPage++;
        refreshRequest();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        goodsList.clear();
        mCurrentPage = 1;
        refreshRequest();
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setNoMoreData(false);
        }
    }
}
