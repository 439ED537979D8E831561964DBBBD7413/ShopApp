package com.yj.shopapp.ui.activity.shopkeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.EditText;
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
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.Goods;
import com.yj.shopapp.ui.activity.Interface.GoodsItemListenter;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.SNewGoodsAdpter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.dialog.BugGoodsDialog;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.util.StatusBarUtils;
import com.yj.shopapp.view.headfootrecycleview.RecycleViewEmpty;
import com.yj.shopapp.wbeen.Itemtype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import ezy.ui.layout.LoadingLayout;

/**
 * Created by jm on 2016/4/25.
 * <p/>
 * 我的商品
 */
public class SGoodsActivity extends BaseActivity implements GoodsItemListenter, OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.recycler_view)
    RecycleViewEmpty recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SmartRefreshLayout swipeRefreshLayout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    ImageView idRightBtu;

    @BindView(R.id.submitTv)
    TextView submitTv;
    @BindView(R.id.value_Et)
    EditText valueEt;
    @BindView(R.id.topsearchLy)
    LinearLayout topsearchLy;
    @BindView(R.id.bgView)
    View bgView;
    String categoryId;//商品分类ID
    String keyWord;//首页关键词搜索
    @BindView(R.id.popupwindow)
    RelativeLayout popupwindow;
    @BindView(R.id.loading)
    LoadingLayout loading;
    @BindView(R.id.title_layout)
    RelativeLayout titleLayout;

    private boolean isRequesting = false;//标记，是否正在刷新
    private int mCurrentPage = 1;
    private List<Goods> goodsList = new ArrayList<>();
    private List<Itemtype> itemtypeList = new ArrayList<>();
    private String username = "";
    private String titlename = "";
    private String agentuName = "";
    private String typeid = "";
    private String brandid = "";
    private String cid = "";
    private int isSet = 0;
    private SNewGoodsAdpter GoodAdpter;

    @Override
    protected int getLayoutId() {
        return R.layout.stab_goods;
    }

    @Override
    protected void initData() {
        if (getIntent().hasExtra("categoryId")) {
            categoryId = getIntent().getStringExtra("categoryId");
        }
        if (getIntent().hasExtra("typeid")) {
            typeid = getIntent().getStringExtra("typeid");
        }
        if (getIntent().hasExtra("keyword")) {
            keyWord = getIntent().getStringExtra("keyword");
            idRightBtu.setVisibility(View.GONE);
        }
        if (getIntent().hasExtra("cid")) {
            cid = getIntent().getStringExtra("cid");
        }
        if (getIntent().hasExtra("typeName")) {
            titlename = getIntent().getStringExtra("typeName");
        }
        if (getIntent().hasExtra("isshow")) {
            isSet = getIntent().getIntExtra("isshow", 1);
        }
        if (getIntent().hasExtra("bid")) {
            brandid = getIntent().getStringExtra("bid");
        }
        if (getIntent().hasExtra("name")) {
            titlename = getIntent().getStringExtra("name");
        }
        agentuid = myApplication.getAgentuid();
        agentuName = myApplication.getAgentuname();
        title.setText(titlename);
        if (getIntent().hasExtra("bigtypeName")) {
            title.setText(getIntent().getStringExtra("bigtypeName"));
        }

        Refresh();

        GoodAdpter = new SNewGoodsAdpter(mContext);
        GoodAdpter.setListenter(this);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerView.addItemDecoration(new DDecoration(mContext));
            recyclerView.setAdapter(GoodAdpter);
        }
        if (isNetWork(mContext)) {
            mCurrentPage = 1;
            goodsList.clear();
            refreshRequest();
        }
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtils.from(SGoodsActivity.this)
                .setActionbarView(titleLayout)
                .setTransparentStatusbar(true)
                .setLightStatusBar(false)
                .process();
    }


    private void Refresh() {
        swipeRefreshLayout.setHeaderHeight(50);
        swipeRefreshLayout.setFooterHeight(50);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setOnLoadMoreListener(this);
        swipeRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        swipeRefreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
    }

    @OnClick(R.id.id_right_btu)
    public void openRightDrawer() {
        Bundle b = new Bundle();
        b.putString("typeid", typeid);
        b.putString("cid", cid);
        CommonUtils.goActivityForResult(mContext, Seek_brand.class, b, 10086, false);
    }

    @OnClick(R.id.forewadImg)
    public void onClick() {
        if (topsearchLy.getVisibility() == View.VISIBLE) {
            bgView.setVisibility(View.GONE);
            topsearchLy.setVisibility(View.GONE);
        } else {
            finish();
        }


    }

    /**
     * 右侧事件操作
     **/

    @OnClick(R.id.submitTv)
    public void search() {
        bgView.setVisibility(View.GONE);
        topsearchLy.setVisibility(View.GONE);
        if (isRequesting)
            return;
        keyWord = valueEt.getText().toString().trim();

        if (NetUtils.isNetworkConnected(mContext)) {
            if (null != swipeRefreshLayout) {
                swipeRefreshLayout.autoRefresh(300, 300, 1.5f);
            }
        } else {
            showToastShort("网络不给力");
        }
    }

    /***
     * 网络数据
     ***********************************************************/

    public void refreshRequest() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", String.valueOf(mCurrentPage));
        params.put("brandid", brandid);
        //if (isSet == 1) {
        params.put("cid", cid);
        // }
        params.put("bigtypeid", typeid);
        params.put("itemname", username);
        params.put("keyword", keyWord);
        HttpHelper.getInstance().post(mContext, Contants.PortU.ITEMLIST, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.finishRefresh();
                    swipeRefreshLayout.finishLoadMore();
                }
            }

            @Override
            public void onBefore() {
                super.onBefore();
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JsonHelper<Goods> jsonHelper = new JsonHelper<Goods>(Goods.class);
                    goodsList.addAll(jsonHelper.getDatas(json));
                    GoodAdpter.setList(goodsList);
                    if (loading != null) {
                        loading.showContent();
                    }
                } else if (JsonHelper.getRequstOK(json) == 6) {
                    mCurrentPage--;
                    showToastShort(JsonHelper.errorMsg(json));
                    if (goodsList.size() == 0) {
                        if (loading != null) {
                            loading.showEmpty();
                        }
                    } else {
                        if (swipeRefreshLayout != null) {
                            swipeRefreshLayout.finishLoadMoreWithNoMoreData();
                        }
                    }
                } else {
                    showToastShort(JsonHelper.errorMsg(json));
                    mCurrentPage--;
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.finishRefresh(false);
                    swipeRefreshLayout.finishLoadMore(false);
                }
                mCurrentPage--;
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
                goodsList.clear();
                GoodAdpter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case SChooseAgentActivity.CHOOSEAGENT_TYPE_WHAT:
                agentuid = data.getExtras().getString("agentuid");
                agentuName = data.getExtras().getString("agentuName");
                if (NetUtils.isNetworkConnected(mContext)) {
                    if (null != swipeRefreshLayout) {

                        swipeRefreshLayout.autoRefresh(300, 300, 1.5f);
                    }
                } else {
                    showToastShort("网络不给力");
                }
                break;
            case Seek_brand.GOBACKONE:
                brandid = data.getExtras().getString("bid");
                title.setText(data.getExtras().getString("gname"));
                keyWord = "";
                if (NetUtils.isNetworkConnected(mContext)) {
                    if (null != swipeRefreshLayout) {
                        swipeRefreshLayout.autoRefresh(300, 300, 1.5f);
                    }
                } else {
                    showToastShort("网络不给力");
                }
                break;
            case Seek_brand.GOBACKTWO:
                keyWord = data.getExtras().getString("content");
                if (NetUtils.isNetworkConnected(mContext)) {
                    if (null != swipeRefreshLayout) {
                        swipeRefreshLayout.autoRefresh();
                    }
                } else {
                    showToastShort("网络不给力");
                }
                break;
            default:
                break;
        }

    }


    @Override
    public void onClick(View V, int position) {
        if (isRequesting) return;
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
                    BugGoodsDialog.newInstance(goodsList.get(position)).show(getFragmentManager(), "123");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckBoxClick(int position, boolean isChecked) {

    }


    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mCurrentPage++;
        refreshRequest();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        if (isNetWork(mContext)) {
            goodsList.clear();
            mCurrentPage = 1;
            refreshRequest();
            swipeRefreshLayout.setNoMoreData(false);
        }
    }

}
