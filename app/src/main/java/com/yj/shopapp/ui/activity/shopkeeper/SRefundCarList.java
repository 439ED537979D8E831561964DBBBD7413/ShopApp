package com.yj.shopapp.ui.activity.shopkeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.yj.shopapp.presenter.CardListRecyclerView;
import com.yj.shopapp.ubeen.Address;
import com.yj.shopapp.ubeen.CartList;
import com.yj.shopapp.ui.activity.adapter.SRefunCarAdapter;
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
 * Created by huanghao on 2016/11/21.
 */

public class SRefundCarList extends BaseActivity implements CardListRecyclerView {


    @BindView(R.id.forewadImg)
    ImageView forewadImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;

    @BindView(R.id.choose)
    ImageView choose;
    @BindView(R.id.cart_total_price_tv)
    public TextView carttotalpriceTv;
    @BindView(R.id.bottom_layout)
    LinearLayout bottomLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.id_drawer_layout)
    RelativeLayout idDrawerLayout;
    @BindView(R.id.id_del_btu)
    TextView idDelBtu;
    @BindView(R.id.submit)
    LinearLayout submit;
    private ILoadView iLoadView = null;
    private View loadMoreView = null;
    private String remarks="";

    @OnClick(R.id.id_right_btu)
    public void openRightDrawer() {
        if (!isRequesting) {
            Bundle bundle = new Bundle();
            bundle.putString("choosetype", "0");
            CommonUtils.goActivityForResult(mContext, SChooseAgentActivity.class, bundle, 0, false);
        }
    }


    private RecyclerViewHeaderFooterAdapter adapter;

    private RecyclerView.LayoutManager layoutManager;

    private boolean isRequesting = false;//标记，是否正在刷新或者加载


    private List<CartList> megsList = new ArrayList<>();
    private List<Integer> chooseArray = new ArrayList<>();  //0 不选择 1 选中
    private List<Address> notes = new ArrayList<Address>();

    String uid;
    String token;
    String agentuid = "";
    boolean isAllChoose = false;
    String idstr = "";
    String addressid = "";


    @OnClick(R.id.allchoose)
    public void AllChoose() {
        isAllChoose = !isAllChoose;
        if (isAllChoose) {
            choose.setImageDrawable(mContext.getResources().getDrawable(R.drawable.check_true));
        } else {
            choose.setImageDrawable(mContext.getResources().getDrawable(R.drawable.check_false));
        }
        for (int i = 0; i < chooseArray.size(); i++) {
            chooseArray.set(i, isAllChoose ? 1 : 0);
        }
        adapter.notifyDataSetChanged();
        countMoney();
    }

    @OnClick(R.id.submit)
    public void saveOrder() {
        StringBuffer stringBuffer = new StringBuffer();
        int i = 0;
        for (CartList cartList : megsList) {
            if (chooseArray.get(i) == 1) {
                stringBuffer.append(cartList.getId() + "|");
            }
            i++;
        }

        if (stringBuffer.toString().length() > 0) {
            getAddress();
            idstr = stringBuffer.substring(0, stringBuffer.length() - 1);
        } else {
            showToastShort("请选择商品");
        }
    }

    @OnClick(R.id.id_del_btu)
    public void delCart(){

        StringBuffer stringBuffer = new StringBuffer();
        int i = 0;
        for (CartList cartList : megsList) {
            if (chooseArray.get(i) == 1) {
                stringBuffer.append(cartList.getId() + "|");
            }
            i++;
        }

        if (stringBuffer.toString().length() > 0) {
            idstr = stringBuffer.substring(0, stringBuffer.length() - 1);

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


    }
    @Override
    protected int getLayoutId() {
        return R.layout.sactivity_refundcarlist;
    }

    @Override
    protected void initData() {
        title.setText("退货车");
        idRightBtu.setVisibility(View.GONE);
        uid = PreferenceUtils.getPrefString(mContext, Contants.Preference.UID, "");
        token = PreferenceUtils.getPrefString(mContext, Contants.Preference.TOKEN, "");

        swipeRefreshLayout.setColorSchemeResources(Contants.Refresh.refreshColorScheme);
        swipeRefreshLayout.setOnRefreshListener(listener);

        SRefunCarAdapter nAdapter = new SRefunCarAdapter(SRefundCarList.this, megsList, this, chooseArray);

        layoutManager = new LinearLayoutManager(mContext);

        adapter = new RecyclerViewHeaderFooterAdapter(layoutManager, nAdapter);

        iLoadView = new ILoadViewImpl(mContext, new SRefundCarList.mLoadMoreClickListener());

        loadMoreView = iLoadView.inflate();

        recyclerView.addOnScrollListener(new SRefundCarList.MyScrollListener());


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


    public void loadMoreRequest() {

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
        if (isAllChoose) {
            choose.setImageDrawable(mContext.getResources().getDrawable(R.drawable.check_false));
            isAllChoose = false;
        }
        chooseArray.set(pos, value);
        adapter.notifyDataSetChanged();
        countMoney();
    }

    public void countMoney() {

        float count = 0;
        for (int i = 0; i < megsList.size(); i++) {
            if (chooseArray.get(i) == 1) {
                count = count + Float.valueOf(StringHelper.isEmpty(megsList.get(i).getMoneysum()) ? "0" : megsList.get(i).getMoneysum());
            }
        }
        carttotalpriceTv.setText(String.valueOf(count));
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
    public void showChooseify() {

        String[] array = new String[notes.size()];
        int i = 0;
        for (Address itemtype : notes) {
            array[i] = itemtype.getAddress();
            i++;
        }

        MaterialDialog .Builder materialDialog=  new MaterialDialog.Builder(this);
        materialDialog .title("选择退货地址");
        materialDialog.items(array);
        materialDialog .itemsCallback(new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                addressid = notes.get(which).getId();
                SaveDoorder();
                remarks="";
            }
        });
        if (!remarks.equals(""))
        {
            materialDialog.neutralText("备注:"+remarks);
        }
        else {
            materialDialog.neutralText("添加备注");
            materialDialog .onNeutral(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    showAddMark();

                }
            });
        }


        materialDialog .positiveText(android.R.string.cancel);
        materialDialog.show();
    }
    public void showAddMark() {



        new MaterialDialog.Builder(this)
                .title("添加备注")

                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        addressid = notes.get(which).getId();
                        SaveDoorder();
                    }
                })

                .positiveText("添加备注")
                .input("输入备注", "",true, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        remarks=input.toString();
                        showChooseify();
                    }
                })

                .neutralText(android.R.string.cancel)
                .show();
    }



    /**********************
     * 网络请求
     ******************/

    public void refreshRequest() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("agentuid", agentuid);
        adapter.removeFooter(loadMoreView);
        HttpHelper.getInstance().post(mContext, Contants.PortU.Refundslistcart, params, new OkHttpResponseHandler<String>(mContext) {

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
                if (isAllChoose) {
                    choose.setImageDrawable(mContext.getResources().getDrawable(R.drawable.check_false));
                    isAllChoose = false;
                }
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JsonHelper<CartList> jsonHelper = new JsonHelper<CartList>(CartList.class);

                    megsList.addAll(jsonHelper.getDatas(json));
                    for (CartList cartList : megsList) {
                        chooseArray.add(0);
                    }

                }else if(JsonHelper.getRequstOK(json)==6){
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


    public void SaveDoorder() {

        //显示ProgressDialog
        final KProgressHUD progressDialog = growProgress(Contants.Progress.SUMBIT_ING);

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("idstr", idstr);
        params.put("addressid", addressid);
        params.put("tradedate", "");
        params.put("remarks",remarks);

        HttpHelper.getInstance().post(mContext, Contants.PortU.Dorefundsorder, params, new OkHttpResponseHandler<String>(mContext) {

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

                System.out.println("response" + json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    carttotalpriceTv.setText("0");
                    if (null != swipeRefreshLayout) {

                        swipeRefreshLayout.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                swipeRefreshLayout.setRefreshing(true);

                                refreshRequest();
                            }
                        }, 200);
                    }
                    //CommonUtils.setBroadCast(mContext, Contants.Bro.REFRESH_ORDERLIST);
                    showToastShort(Contants.NetStatus.NETSUCCESS);
                } else {
                    showToastShort(Contants.NetStatus.NETERROR);
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
            }
        });

    }

    public void getAddress() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);

        //显示ProgressDialog
        final KProgressHUD progressDialog = growProgress(Contants.Progress.LOAD_ING);

        HttpHelper.getInstance().post(mContext, Contants.PortU.Uaddress, params, new OkHttpResponseHandler<String>(mContext) {

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

                System.out.println("response" + json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    notes.clear();
                    JsonHelper<Address> jsonHelper = new JsonHelper<Address>(Address.class);

                    notes.addAll(jsonHelper.getDatas(json));
                    showChooseify();
                } else {
                    showToastShort("获取地址失败");
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
                notes.clear();
                SaveDoorder();
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

        HttpHelper.getInstance().post(mContext, Contants.PortU.Delrefundslistcart, params, new OkHttpResponseHandler<String>(mContext) {

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
                    carttotalpriceTv.setText("0");
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
    public void changeNumber(String itemid,String itemsum,OkHttpResponseHandler okHttpResponseHandler )
    {
        final KProgressHUD progressDialog = growProgress(Contants.Progress.SUMBIT_ING);
        Map<String,String>params=new HashMap<String,String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("id",itemid);
        params.put("itemcount",itemsum);
        HttpHelper.getInstance().post(mContext,Contants.PortU.SaveListCart,params,okHttpResponseHandler);

    }


}

