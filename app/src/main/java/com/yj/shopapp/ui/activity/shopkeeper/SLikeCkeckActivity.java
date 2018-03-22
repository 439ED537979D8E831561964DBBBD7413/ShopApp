package com.yj.shopapp.ui.activity.shopkeeper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.loading.ILoadView;
import com.yj.shopapp.loading.ILoadViewImpl;
import com.yj.shopapp.loading.LoadMoreClickListener;
import com.yj.shopapp.presenter.GoodsRecyclerView;
import com.yj.shopapp.ubeen.Goods;
import com.yj.shopapp.ui.activity.adapter.SLikeCheckAdapter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.StringHelper;
import com.yj.shopapp.view.headfootrecycleview.OnRecyclerViewScrollListener;
import com.yj.shopapp.view.headfootrecycleview.RecyclerViewHeaderFooterAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by huanghao on 2016/11/11.
 */

public class SLikeCkeckActivity extends BaseActivity implements GoodsRecyclerView {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    //    @BindView(R.id.swipe_refresh_layout)
//    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;
    @BindView(R.id.value_Et)
    EditText value_Et;
    private RecyclerViewHeaderFooterAdapter adapter;

    private RecyclerView.LayoutManager layoutManager;

    private boolean isRequesting = false;//标记，是否正在刷新
    private boolean isRequestingType = false;//标记，获取类型是否正在刷新

    private int mCurrentPage = 0;

    private List<Goods> goodsList = new ArrayList<>();
    private ILoadView iLoadView = null;
    private View loadMoreView = null;
     String checkGoods = "";
    @Override
    protected int getLayoutId() {
        return R.layout.wactivity_likecheck;
    }

    @Override
    protected void initData() {
        title.setText("条码模糊查询");
//        swipeRefreshLayout.setColorSchemeResources(Contants.Refresh.refreshColorScheme);
//        swipeRefreshLayout.setOnRefreshListener(listener);

        SLikeCheckAdapter oAdapter = new SLikeCheckAdapter(mContext, goodsList, this);

        layoutManager = new LinearLayoutManager(mContext);

        adapter = new RecyclerViewHeaderFooterAdapter(layoutManager, oAdapter);

        iLoadView = new ILoadViewImpl(mContext, new SLikeCkeckActivity.mLoadMoreClickListener());

        loadMoreView = iLoadView.inflate();

        recyclerView.addOnScrollListener(new SLikeCkeckActivity.MyScrollListener());


        if (recyclerView != null) {
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }
    }


    @OnClick(R.id.submitTv)
    public void sumitTvOnclick() {
        refreshRequest();
    }

    /***
     * 网络数据
     ***********************************************************/

    public void refreshRequest() {
        mCurrentPage = 1;

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("keyword", value_Et.getText().toString().trim().replace(" ", ""));
//        params.put("agentuid", WId);


        adapter.removeFooter(loadMoreView);
        HttpHelper.getInstance().post(mContext, Contants.PortU.Getitemsbynumber, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
                // swipeRefreshLayout.setRefreshing(false);
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

                goodsList.clear();
                System.out.println("response" + json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JsonHelper<Goods> jsonHelper = new JsonHelper<Goods>(Goods.class);

                    goodsList.addAll(jsonHelper.getDatas(json));

                    if (goodsList.size() >= 20) {
                        adapter.addFooter(loadMoreView);
                    }
                    if (goodsList.size()==0)
                    {
                        showToastShort("没有任何记录！");
                    }
                } else if (JsonHelper.getRequstOK(json) == 6) {
                    showToastShort("没有任何记录！");
                    adapter.removeFooter(loadMoreView);
                } else {
                    showToastShort(JsonHelper.errorMsg(json));
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
                goodsList.clear();
                adapter.notifyDataSetChanged();
            }
        });

    }

    public void loadMoreRequest() {
        if (isRequesting)
            return;
        if (goodsList.size() < 20) {
            return;
        }


        iLoadView.showLoadingView(loadMoreView);

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("keyword", value_Et.getText().toString().trim().replace(" ", ""));

//        params.put("agentuid", WId);


        HttpHelper.getInstance().post(mContext, Contants.PortU.ITEMLIST, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
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

                System.out.println("response" + json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JsonHelper<Goods> jsonHelper = new JsonHelper<Goods>(Goods.class);

                    if (jsonHelper.getDatas(json).size() == 0) {
                        iLoadView.showFinishView(loadMoreView);
                    } else {
                        goodsList.addAll(jsonHelper.getDatas(json));
                    }
                } else if (JsonHelper.getRequstOK(json) == 6) {
                    iLoadView.showFinishView(loadMoreView);
                } else {
                    showToastShort(JsonHelper.errorMsg(json));
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
                mCurrentPage--;
                iLoadView.showErrorView(loadMoreView);
            }
        });
    }

    /**
     * 保存购物车
     */
    public void saveDolistcart(String itemid, String itemsum) {
        String url = "";
        if (checkGoods.equals("refund")) {
            url = Contants.PortU.DorefundsListcart;
        } else {
            url = Contants.PortU.DOLISTCART;
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("itemid", itemid);
        params.put("itemsum", itemsum);

        final KProgressHUD kProgressHUD = growProgress(Contants.Progress.SUMBIT_ING);

        HttpHelper.getInstance().post(mContext, url, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
                kProgressHUD.dismiss();
            }

            @Override
            public void onBefore() {
                super.onBefore();
                kProgressHUD.show();
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);

                System.out.println("response" + json);

                if (JsonHelper.isRequstOK(json, mContext)) {
                    //CommonUtils.setBroadCast(mContext, Contants.Bro.REFRESH_CARTLIST);
                    showToastShort("加入购物车成功");
                } else {
                    showToastShort(JsonHelper.errorMsg(json));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
            }
        });
    }



    public class mLoadMoreClickListener implements LoadMoreClickListener {

        @Override
        public void clickLoadMoreData() {

        }
    }


    public class MyScrollListener extends OnRecyclerViewScrollListener {

        @Override
        public void onScrollUp() {

        }

        @Override
        public void onScrollDown() {

        }

        @Override
        public void onBottom() {
            loadMoreRequest();
        }

        @Override
        public void onMoved(int distanceX, int distanceY) {

        }
    }

    /*********
     * 弹出框
     ***********/

    TextView unitTv;
    EditText inputEt;

    public void setDialogInput(final int pos) {
        MaterialDialog dialog = new MaterialDialog.Builder(mContext)
                .customView(R.layout.dialog_add, true)
                .positiveText(R.string.right)
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        String str = inputEt.getText().toString();
                        if (StringHelper.isEmpty(str)) {
                            showToastShort("请填写购买数量");
                        } else {
                            long num = Long.parseLong(str);
                            if (num>0){
                                saveDolistcart(goodsList.get(pos).getId(), str);
                                dialog.dismiss();
                            }else {
                                showToastShort("购买数量必须大于0");
                            }
                        }

                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {

                        materialDialog.dismiss();
                    }
                })
                .autoDismiss(false)
                .build();


        unitTv = (TextView) dialog.getCustomView().findViewById(R.id.unitTv);
        inputEt = (EditText) dialog.getCustomView().findViewById(R.id.inputEt);
        unitTv.setText(goodsList.get(pos).getUnit());
        dialog.show();
    }


    @Override
    public void CardClick(int postion) {

//        setDialogInput(postion);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==resultCode){
            value_Et.setFocusable(true);
            value_Et.requestFocus();
            InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
//        bundle.putString("checkGoods", checkGoods);
        bundle.putString("goodsId", goodsList.get(position).getId());
        bundle.putString("unit", goodsList.get(position).getUnit());
        CommonUtils.goActivityForResult(mContext, SGoodsDetailActivity.class, bundle, 19, false);
        value_Et.setText("");
    }

    @Override
    public void onLongItemClick(int position) {

    }
}
