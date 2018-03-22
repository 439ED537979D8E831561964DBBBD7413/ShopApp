package com.yj.shopapp.ui.activity.shopkeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.loading.ILoadView;
import com.yj.shopapp.loading.ILoadViewImpl;
import com.yj.shopapp.loading.LoadMoreClickListener;
import com.yj.shopapp.presenter.CardListRecyclerView;
import com.yj.shopapp.ubeen.ScashCoupon;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.util.PreferenceUtils;
import com.yj.shopapp.view.headfootrecycleview.OnRecyclerViewScrollListener;
import com.yj.shopapp.view.headfootrecycleview.RecyclerViewHeaderFooterAdapter;
import com.yj.shopapp.wbeen.CashCoupon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by huanghao on 2016/12/1.
 */

public class SCashCouponActivity extends BaseActivity implements CardListRecyclerView {


    @BindView(R.id.forewadImg)
    ImageView forewadImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.id_drawer_layout)
    RelativeLayout idDrawerLayout;
    @BindView(R.id.id_del_btu)
    TextView idDelBtu;

    private ILoadView iLoadView = null;
    private View loadMoreView = null;

    private int mCurrentPage = 0;

//    @OnClick(R.id.add_btu)
//    public void openRightDrawer() {
//
//
//        CommonUtils.goActivity(mContext,WAddCashCouponActivity.class,null);
//
//    }


    private RecyclerViewHeaderFooterAdapter adapter;

    private RecyclerView.LayoutManager layoutManager;

    private boolean isRequesting = false;//标记，是否正在刷新或者加载


    private List<ScashCoupon.CanuseBean> megsList = new ArrayList<>();
    private List<Integer> chooseArray = new ArrayList<>();  //0 不选择 1 选中


    String uid;
    String token;
    String agentuid = "";
    boolean isAllChoose = false;
    String idstr = "";








    @OnClick(R.id.id_del_btu)
    public void delCart() {

        StringBuffer stringBuffer = new StringBuffer();
        int i = 0;
        for (ScashCoupon.CanuseBean cartList : megsList) {
            if (chooseArray.get(i) == 1) {
                stringBuffer.append(cartList.getId() + "|");
            }
            i++;
        }

        if (stringBuffer.toString().length() > 0) {
            idstr = stringBuffer.substring(0, stringBuffer.length() - 1);
            Bundle bundle=new Bundle();
            bundle.putString("CashCoupon",idstr);
CommonUtils.goResult(mContext,bundle,999);


        } else {
            showToastShort("请选择商品");
        }


    }

    @Override
    protected int getLayoutId() {
        return R.layout.sactivity_cashcoupon;
    }

    @Override
    protected void initData() {
        title.setText("现金券列表");

        uid = PreferenceUtils.getPrefString(mContext, Contants.Preference.UID, "");
        token = PreferenceUtils.getPrefString(mContext, Contants.Preference.TOKEN, "");

        swipeRefreshLayout.setColorSchemeResources(Contants.Refresh.refreshColorScheme);
        swipeRefreshLayout.setOnRefreshListener(listener);

//        CashCouponListAdapter nAdapter = new CashCouponListAdapter(SCashCouponActivity.this, megsList, this, chooseArray);
//
//        layoutManager = new LinearLayoutManager(mContext);
//
//        adapter = new RecyclerViewHeaderFooterAdapter(layoutManager, nAdapter);

        iLoadView = new ILoadViewImpl(mContext, new SCashCouponActivity.mLoadMoreClickListener());

        loadMoreView = iLoadView.inflate();

        recyclerView.addOnScrollListener(new SCashCouponActivity.MyScrollListener());


        if (recyclerView != null) {
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
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
    }

    SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {

            refreshRequest();
        }
    };

    @Override
    public void onItemClick(int position) {
    }

    @Override
    public void onLongItemClick(int position) {
    }

    @Override
    public void chooseItem(int pos, int value) {

        chooseArray.set(pos, value);
        adapter.notifyDataSetChanged();

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

        }

        @Override
        public void onMoved(int distanceX, int distanceY) {

        }
    }

    /****************************
     * 弹出框
     **********/





    /**********************
     * 网络请求
     ******************/

    public void refreshRequest() {
        mCurrentPage=1;
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);

        params.put("agentuid", WId);
        adapter.removeFooter(loadMoreView);
        HttpHelper.getInstance().post(mContext, Contants.PortU.Canusecash, params, new OkHttpResponseHandler<String>(mContext) {

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

                megsList.clear();
                chooseArray.clear();
                System.out.println("response" + json);

                if (JsonHelper.isRequstOK(json, mContext)) {
                    JsonHelper<CashCoupon> jsonHelper = new JsonHelper<CashCoupon>(CashCoupon.class);

                   // megsList.addAll(jsonHelper.getDatas(json));
                    if(megsList.size()==0){

                    }
                    else if (megsList.size() >= 20) {
                        adapter.addFooter(loadMoreView);
                    }
//                    for (CashCoupon cashCoupon : megsList) {
//                        chooseArray.add(0);
//                    }

                } else if (JsonHelper.getRequstOK(json) == 6) {
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
                megsList.clear();
                adapter.notifyDataSetChanged();
            }
        });

    }







    public void delCartReport() {

        if (isRequesting)
            return;

        //显示ProgressDialog
        final KProgressHUD progressDialog = growProgress(Contants.Progress.DELETE_ING);
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("idstr", idstr);

        HttpHelper.getInstance().post(mContext, Contants.PortA.Delcash, params, new OkHttpResponseHandler<String>(mContext) {

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

                System.out.println("response===============" + json);

                if (JsonHelper.isRequstOK(json, mContext)) {
                    showToastShort("删除成功");

                    if (null != swipeRefreshLayout) { //删除成功重新刷新数据
                        swipeRefreshLayout.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                swipeRefreshLayout.setRefreshing(true);
                                refreshRequest();
                            }
                        }, 200);
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

    @Override
    protected void onResume() {
        super.onResume();
        refreshRequest();
    }
}


