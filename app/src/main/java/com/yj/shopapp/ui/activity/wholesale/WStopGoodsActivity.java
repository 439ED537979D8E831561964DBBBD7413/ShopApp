package com.yj.shopapp.ui.activity.wholesale;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.yj.shopapp.ui.activity.adapter.WStopGoodsAdapter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.util.PreferenceUtils;
import com.yj.shopapp.view.headfootrecycleview.OnRecyclerViewScrollListener;
import com.yj.shopapp.view.headfootrecycleview.RecyclerViewHeaderFooterAdapter;
import com.yj.shopapp.wbeen.Goods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by jm on 2016/5/14.
 */
public class WStopGoodsActivity extends BaseActivity implements GoodsRecyclerView {

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.id_right_btu)
    TextView id_right_btu;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.bottom_layout)
    LinearLayout bottomLayout;


    private ILoadView iLoadView = null;
    private View loadMoreView = null;

    private RecyclerViewHeaderFooterAdapter adapter;

    private RecyclerView.LayoutManager layoutManager;

    private boolean isRequesting = false;//标记，是否正在刷新

    private int mCurrentPage = 0;

    private List<Goods> goodsList = new ArrayList<>();

    private ArrayAdapter Dardapter;

    String uid;
    String token;
    String agentuid = ""; //临时批发商ID 做个判断

//    @OnClick(R.id.id_right_btu)
//    public void Open(){
//        Bundle bundle = new Bundle();
//        bundle.putString("choosetype","0");
//        CommonUtils.goActivityForResult(mContext, SChooseAgentActivity.class, bundle,0, false);
//    }

    @Override
    protected int getLayoutId() {
        return R.layout.sactivity_marks;
    }

    @Override
    protected void initData() {
        id_right_btu.setVisibility(View.GONE);
//        agentuid = myApplication.getAgentuid();
        bottomLayout.setVisibility(View.GONE);
        title.setText("暂停销售产品");
        id_right_btu.setText("筛选");

        uid = PreferenceUtils.getPrefString(mContext, Contants.Preference.UID, "");
        token = PreferenceUtils.getPrefString(mContext, Contants.Preference.TOKEN, "");

        swipeRefreshLayout.setColorSchemeResources(Contants.Refresh.refreshColorScheme);
        swipeRefreshLayout.setOnRefreshListener(listener);

        WStopGoodsAdapter oAdapter = new WStopGoodsAdapter(mContext, goodsList, this);

        layoutManager = new LinearLayoutManager(mContext);

        adapter = new RecyclerViewHeaderFooterAdapter(layoutManager, oAdapter);

        iLoadView = new ILoadViewImpl(mContext, new mLoadMoreClickListener());

        loadMoreView = iLoadView.inflate();

        recyclerView.addOnScrollListener(new MyScrollListener());


        if (recyclerView != null) {
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }

    }



    SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {

            refreshRequest();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
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

    @Override
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("itemnoid", goodsList.get(position).getNumberid());
        bundle.putString("id", goodsList.get(position).getId());
        CommonUtils.goActivity(mContext, WGoodsDetailActivity.class, bundle, false);
    }

    @Override
    public void onLongItemClick(final int position) {
//        showToastShort("changan"+position);

        new MaterialDialog.Builder(mContext)
                .content("是否删除" + goodsList.get(position).getName() + "?")
                .positiveText("是")
                .negativeText("否")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        showDelectClint(position);
                    }
                })
                .show();

    }

    @Override
    public void CardClick(int postion) {
        showDelectClint(postion);
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

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode == SChooseAgentActivity.CHOOSEAGENT_TYPE_WHAT){
//            Bundle bundle = new Bundle();
//            agentuid = data.getExtras().getString("agentuid");
//            if (NetUtils.isNetworkConnected(mContext)) {
//                if (null != swipeRefreshLayout) {
//
//                    swipeRefreshLayout.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            swipeRefreshLayout.setRefreshing(true);
//                            refreshRequest();
//                        }
//                    }, 50);
//                }
//            } else {
//                showToastShort("网络不给力");
//            }
//        }
//    }

    /*********弹出框***********/

    TextView unitTv;
    EditText inputEt;

    public void showDelectClint(final int position) {
        new MaterialDialog.Builder(mContext)
                .content("是否删除" + goodsList.get(position).getName() + "?")
                .positiveText("是")
                .negativeText("否")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        delClient(position);
                    }
                })
                .show();
    }

    public void delClient(int pos) {
        if (isRequesting)
            return;

        //显示ProgressDialog
        final KProgressHUD progressDialog = growProgress(Contants.Progress.DELETE_ING);
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("idstr", goodsList.get(pos).getId());

        HttpHelper.getInstance().post(mContext, Contants.PortA.delstopitem, params, new OkHttpResponseHandler<String>(mContext) {

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

                System.out.println("response=" + json);

                if (JsonHelper.errorNo(json).equals("0")) {

                    if (null != swipeRefreshLayout) { //删除成功重新刷新数据
                        swipeRefreshLayout.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                swipeRefreshLayout.setRefreshing(true);
                                refreshRequest();
                            }
                        }, 200);
                    }
                } else if (JsonHelper.getRequstOK(json) == 6) {

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

    /***网络数据***********************************************************/

    public void refreshRequest() {
        mCurrentPage = 1;

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", String.valueOf(mCurrentPage));

        adapter.removeFooter(loadMoreView);
        HttpHelper.getInstance().post(mContext, Contants.PortA.Stopsaleitemlist, params, new OkHttpResponseHandler<String>(mContext) {

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

                goodsList.clear();
                System.out.println("response" + json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JsonHelper<Goods> jsonHelper = new JsonHelper<Goods>(Goods.class);

                    goodsList.addAll(jsonHelper.getDatas(json));

                    if (goodsList.size() >= 20) {
                        adapter.addFooter(loadMoreView);
                    }
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

        mCurrentPage++;

        iLoadView.showLoadingView(loadMoreView);

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", String.valueOf(mCurrentPage));


        HttpHelper.getInstance().post(mContext, Contants.PortA.Stopsaleitemlist, params, new OkHttpResponseHandler<String>(mContext) {

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


}
