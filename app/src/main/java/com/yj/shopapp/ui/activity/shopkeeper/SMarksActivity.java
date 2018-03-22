package com.yj.shopapp.ui.activity.shopkeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.Goods;
import com.yj.shopapp.ui.activity.Interface.GoodsItemListenter;
import com.yj.shopapp.ui.activity.Interface.OnViewScrollListenter;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.SNewGoodsAdpter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.BugGoodsDialog;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jm on 2016/5/14.
 */
public class SMarksActivity extends BaseActivity implements GoodsItemListenter, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    TextView id_right_btu;
    @BindView(R.id.cart_total_price_tv)
    TextView carttotalpriceTv;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
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

    private boolean isRequesting = false;//标记，是否正在刷新
    private int mCurrentPage = 1;
    private List<Goods> goodsList = new ArrayList<>();
    String agentuid = ""; //临时批发商ID 做个判断
    boolean isAllChoose = false;
    private SNewGoodsAdpter GoodAdpter;
    private View FooterView;
    @Override
    protected int getLayoutId() {
        return R.layout.sactivity_marks;
    }

    @Override
    protected void initData() {
        title.setText("收藏商品");
        id_right_btu.setVisibility(View.GONE);
        swipeRefreshLayout.setColorSchemeResources(Contants.Refresh.refreshColorScheme);
        swipeRefreshLayout.setOnRefreshListener(this);

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
        if (NetUtils.isNetworkConnected(mContext)) {
            if (null != swipeRefreshLayout) {
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(true);
                        refreshRequest();
                    }
                }, 200);
            }
        } else {
            showToastShort("网络不给力");
        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        FooterView = LayoutInflater.from(mContext).inflate(R.layout.footerview, null);
        FooterView.setLayoutParams(lp);
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

        for (int i = 0; i < goodsList.size(); i++) {
            if (goodsList.get(i).isSelected()) {
                del(i, true);
            }
        }
        if (null != swipeRefreshLayout) { //删除成功重新刷新数据
            swipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                    onRefresh();
                }
            }, 200);
        }
    }

    @Override
    public void onRefresh() {
        goodsList.clear();
        mCurrentPage = 1;
        refreshRequest();
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
                BugGoodsDialog.newInstance(goodsList.get(position)).show(getFragmentManager(),"123");
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
            if (NetUtils.isNetworkConnected(mContext)) {
                if (null != swipeRefreshLayout) {

                    swipeRefreshLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            swipeRefreshLayout.setRefreshing(true);
                            refreshRequest();
                        }
                    }, 50);
                }
            } else {
                showToastShort("网络不给力");
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
                swipeRefreshLayout.setRefreshing(false);
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
                    if (goodsList.size() > 6) {
                        GoodAdpter.setmFooterView(FooterView);
                    }
                } else {
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
                GoodAdpter.notifyDataSetChanged();
            }
        });

    }


    public void del(int pos, final boolean toast) {
        if (isRequesting)
            return;

        //显示ProgressDialog
        //final KProgressHUD progressDialog = growProgress(Contants.Progress.DELETE_ING);
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("id", goodsList.get(pos).getId());

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
                System.out.println("response===============" + json);
                carttotalpriceTv.setText("0");
                //choose.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_shopcart_unhook));
                if (JsonHelper.isRequstOK(json, mContext)) {
                    if (toast) {
                        showToastShort("删除成功");
                    } else {
                        showToastShort("添加购物车成功！");
                    }

                } else {
                    if (toast) {
                        showToastShort(JsonHelper.errorMsg(json));
                    }
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
            }
        });
    }



}
