package com.yj.shopapp.ui.activity.shopkeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
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
import com.yj.shopapp.ui.activity.adapter.SGoodsAdapter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.util.PreferenceUtils;
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
 * Created by Administrator on 2016/8/5.
 */
public class SSalesPromotionActivity extends BaseActivity implements GoodsRecyclerView {

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.id_right_btu)
    TextView id_right_btu;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;


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

    @OnClick(R.id.id_right_btu)
    public void Open(){
        Bundle bundle = new Bundle();
        bundle.putString("choosetype","0");
        CommonUtils.goActivityForResult(mContext, SChooseAgentActivity.class, bundle,0, false);
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_salespromotion;
    }

    @Override
    protected void initData() {
        title.setText("促销商品");
        id_right_btu.setText("筛选");

        uid = PreferenceUtils.getPrefString(mContext, Contants.Preference.UID, "");
        token = PreferenceUtils.getPrefString(mContext, Contants.Preference.TOKEN, "");

        swipeRefreshLayout.setColorSchemeResources(Contants.Refresh.refreshColorScheme);
        swipeRefreshLayout.setOnRefreshListener(listener);

        SGoodsAdapter oAdapter = new SGoodsAdapter(mContext, goodsList, this);

        layoutManager = new LinearLayoutManager(mContext);

        adapter = new RecyclerViewHeaderFooterAdapter(layoutManager, oAdapter);

        iLoadView = new ILoadViewImpl(mContext, new mLoadMoreClickListener());

        loadMoreView = iLoadView.inflate();

        recyclerView.addOnScrollListener(new MyScrollListener());


        if (recyclerView != null) {
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }

        if(StringHelper.isEmpty(agentuid)){
            Bundle bundle = new Bundle();
            bundle.putString("choosetype","0");
            CommonUtils.goActivityForResult(mContext, SChooseAgentActivity.class, bundle,0, false);
        }else {
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
    }



    SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {

            refreshRequest();
        }
    };

    @Override
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("goodsId",goodsList.get(position).getId());
        bundle.putString("unit",goodsList.get(position).getUnit());
        CommonUtils.goActivityForResult(mContext,SGoodsDetailActivity.class,bundle,19,false);
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
                        del(position);
                    }
                })
                .show();

    }

    @Override
    public void CardClick(int postion) {
        setDialogInput(postion);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == SChooseAgentActivity.CHOOSEAGENT_TYPE_WHAT){
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

    /*********弹出框***********/

    TextView unitTv;
    EditText inputEt;
    public void setDialogInput(final int pos){
        MaterialDialog dialog = new MaterialDialog.Builder(mContext)
                .customView(R.layout.dialog_add, true)
                .positiveText(R.string.right)
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String str = inputEt.getText().toString();
                        if (StringHelper.isEmpty(str)){
                            showToastShort("请填写购买数量");
                        }else {
                            long num = Long.parseLong(str);
                            if (num>0){
                                saveDolistcart(goodsList.get(pos).getId(),str);
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


    /***网络数据***********************************************************/

    public void refreshRequest() {
        mCurrentPage = 1;

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", String.valueOf(mCurrentPage));
        params.put("agentuid", agentuid);
        adapter.removeFooter(loadMoreView);
        String url=Contants.PortU.SalesPromotion;
        //http://u.19diandian.com/index.php/Appu/sqitem
        HttpHelper.getInstance().post(mContext,  Contants.PortU.SPitem, params, new OkHttpResponseHandler<String>(mContext) {

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
                if (JsonHelper.isRequstOK(json,mContext)) {
                    JsonHelper<Goods> jsonHelper = new JsonHelper<Goods>(Goods.class);

                    goodsList.addAll(jsonHelper.getDatas(json));
//                    for (Goods goods: goodsList)
//                    {
//                        if (goods.get)
//                    }

                    if (goodsList.size() >= 20) {
                        adapter.addFooter(loadMoreView);
                    }
                } else if(JsonHelper.getRequstOK(json)==6){
                    adapter.removeFooter(loadMoreView);
                }else {
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
        params.put("agentuid",agentuid);

        HttpHelper.getInstance().post(mContext,  Contants.PortU.BookMark, params, new OkHttpResponseHandler<String>(mContext) {

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
                if (JsonHelper.isRequstOK(json,mContext)) {
                    JsonHelper<Goods> jsonHelper = new JsonHelper<Goods>(Goods.class);

                    if (jsonHelper.getDatas(json).size() == 0) {
                        iLoadView.showFinishView(loadMoreView);
                    } else {
                        goodsList.addAll(jsonHelper.getDatas(json));
                    }
                }  else if(JsonHelper.getRequstOK(json)==6){
                    iLoadView.showFinishView(loadMoreView);
                }else {
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
     * */
    public void saveDolistcart(String itemid,String itemsum) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("itemid", itemid);
        params.put("itemsum", itemsum);

        final KProgressHUD kProgressHUD = growProgress(Contants.Progress.SUMBIT_ING);

        HttpHelper.getInstance().post(mContext, Contants.PortU.DOLISTCART, params, new OkHttpResponseHandler<String>(mContext) {

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
                showToastShort(JsonHelper.errorMsg(json));
                if (JsonHelper.isRequstOK(json,mContext)) {
                   // CommonUtils.setBroadCast(mContext,Contants.Bro.REFRESH_CARTLIST);
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

    public void del(int pos) {
        if (isRequesting)
            return;

        //显示ProgressDialog
        final KProgressHUD progressDialog = growProgress(Contants.Progress.DELETE_ING);
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("id", goodsList.get(pos).getId());

        HttpHelper.getInstance().post(mContext, Contants.PortU.DelBookmark, params, new OkHttpResponseHandler<String>(mContext) {

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

                if (JsonHelper.isRequstOK(json,mContext)) {
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

}
