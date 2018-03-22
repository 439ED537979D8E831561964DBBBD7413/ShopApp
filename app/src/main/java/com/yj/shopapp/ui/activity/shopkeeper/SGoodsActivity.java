package com.yj.shopapp.ui.activity.shopkeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.yj.shopapp.view.headfootrecycleview.RecycleViewEmpty;
import com.yj.shopapp.wbeen.Itemtype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jm on 2016/4/25.
 * <p/>
 * 我的商品
 */
public class SGoodsActivity extends BaseActivity implements GoodsItemListenter, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.recycler_view)
    RecycleViewEmpty recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
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
    @BindView(R.id.id_drawer_layout)
    RelativeLayout idDrawerLayout;
    @BindView(R.id.bgView)
    View bgView;
    //    @BindView(R.id.tagGroup)
//    TagGroup tagGroup;
    String categoryId;//商品分类ID
    String keyWord;//首页关键词搜索
    @BindView(R.id.popupwindow)
    RelativeLayout popupwindow;
    @BindView(R.id.empty_tv)
    TextView emptyTv;
    @BindView(R.id.Cempty_view)
    NestedScrollView CemptyView;


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
    private View FooterView;
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
        emptyTv.setText("此品牌没有商品");
        title.setText(titlename);
        if (getIntent().hasExtra("bigtypeName")) {
            title.setText(getIntent().getStringExtra("bigtypeName"));
        }

        swipeRefreshLayout.setColorSchemeResources(Contants.Refresh.refreshColorScheme);
        swipeRefreshLayout.setOnRefreshListener(this);

        GoodAdpter = new SNewGoodsAdpter(mContext);
        GoodAdpter.setListenter(this);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerView.addItemDecoration(new DDecoration(mContext));
            recyclerView.setAdapter(GoodAdpter);
            recyclerView.addOnScrollListener(new OnViewScrollListenter() {
                @Override
                public void onBottom() {
                    mCurrentPage++;
                    refreshRequest();
                }
            });
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

    @Override
    public void onRefresh() {
        goodsList.clear();
        mCurrentPage = 1;
        refreshRequest();
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

    /***
     * 网络数据
     ***********************************************************/

    public void refreshRequest() {
        if (isRequesting) return;
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", String.valueOf(mCurrentPage));
        params.put("brandid", brandid);
        if (isSet == 1) {
            params.put("cid", cid);
        }
        params.put("bigtypeid", typeid);
        params.put("itemname", username);
        params.put("keyword", keyWord);
        HttpHelper.getInstance().post(mContext, Contants.PortU.ITEMLIST, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                isRequesting = false;
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
                } else if (JsonHelper.getRequstOK(json) == 6) {
                    mCurrentPage--;
                    showToastShort(JsonHelper.errorMsg(json));
                    if (goodsList.size() == 0) {
                        recyclerView.setEmptyView(CemptyView);
                    } else {
                        if (goodsList.size() > 6) {
                            GoodAdpter.setmFooterView(FooterView);
                        }
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

                        swipeRefreshLayout.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                swipeRefreshLayout.setRefreshing(true);
                                refreshRequest();
                                //loadItemtype(false);
                            }
                        }, 200);
                    }
                } else {
                    showToastShort("网络不给力");
                }
                break;
            case Seek_brand.GOBACKONE:
                brandid = data.getExtras().getString("bid");
                keyWord = "";
                if (NetUtils.isNetworkConnected(mContext)) {
                    if (null != swipeRefreshLayout) {

                        swipeRefreshLayout.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isRequesting = false;
                                swipeRefreshLayout.setRefreshing(true);
                                onRefresh();
                            }
                        }, 200);
                    }
                } else {
                    showToastShort("网络不给力");
                }
                break;
            case Seek_brand.GOBACKTWO:
                keyWord = data.getExtras().getString("content");
                brandid = "";
                if (NetUtils.isNetworkConnected(mContext)) {
                    if (null != swipeRefreshLayout) {

                        swipeRefreshLayout.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isRequesting = false;
                                swipeRefreshLayout.setRefreshing(true);
                                onRefresh();
                            }
                        }, 200);
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
        switch (V.getId()) {
            case R.id.itemview:
                Bundle bundle = new Bundle();
                bundle.putString("goodsId", goodsList.get(position).getId());
                bundle.putString("unit", goodsList.get(position).getUnit());
                CommonUtils.goActivityForResult(mContext, SGoodsDetailActivity.class, bundle, 19, false);
                break;
            case R.id.addcartTv:
                BugGoodsDialog.newInstance(goodsList.get(position)).show(getFragmentManager(), "123");
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckBoxClick(int position, boolean isChecked) {

    }


}
