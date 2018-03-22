package com.yj.shopapp.ui.activity.wholesale;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
import com.yj.shopapp.presenter.BaseRecyclerView;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.WGoodsAdapter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.AnimationUtil;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.util.PreferenceUtils;
import com.yj.shopapp.util.StringHelper;
import com.yj.shopapp.view.MyDecoration;
import com.yj.shopapp.view.TagGroup;
import com.yj.shopapp.view.headfootrecycleview.OnRecyclerViewScrollListener;
import com.yj.shopapp.view.headfootrecycleview.RecyclerViewHeaderFooterAdapter;
import com.yj.shopapp.wbeen.Goods;
import com.yj.shopapp.wbeen.Itemtype;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
public class WGoodsActivity extends BaseActivity implements BaseRecyclerView {


    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.submitTv)
    TextView submitTv;
    @BindView(R.id.value_Et)
    EditText valueEt;
    @BindView(R.id.bgView)
    View bgView;
    @BindView(R.id.topsearchLy)
    LinearLayout topsearchLy;
    @BindView(R.id.id_drawer_layout)
    RelativeLayout idDrawerLayout;
    @BindView(R.id.tagGroup)
    TagGroup tagGroup;

    private ILoadView iLoadView = null;
    private View loadMoreView = null;
    private RecyclerViewHeaderFooterAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private boolean isRequesting = false;//标记，是否正在刷新
    private boolean isRequestingType = false;//标记，获取类型是否正在刷新
    private int mCurrentPage = 1;
    private List<Goods> goodsList = new ArrayList<>();
    private List<Itemtype> itemtypeList = new ArrayList<>();
    private List<String> tArray = new ArrayList<>();
    private String username = "";
    private String type = "";
    private String categoryId;//商品分类ID
    private String keyWord;//首页关键词搜索
    private String bigtypeid = "";
    private int CurrentPosition = -1;

    @Override
    protected int getLayoutId() {
        return R.layout.wtab_goods;
    }

    @Override
    protected void initData() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (getIntent().hasExtra("categoryId")) {
            categoryId = getIntent().getStringExtra("categoryId");
        }
        if (getIntent().hasExtra("bigtypeid")) {
            bigtypeid = getIntent().getStringExtra("bigtypeid");
        }

        if (getIntent().hasExtra("keyword")) {
            keyWord = getIntent().getStringExtra("keyword");
        }
        title.setText("我的商品");
        idRightBtu.setText("搜索");
        if (getIntent().hasExtra("bigtypeName")) {
            title.setText(getIntent().getStringExtra("bigtypeName"));
        }
        swipeRefreshLayout.setColorSchemeResources(Contants.Refresh.refreshColorScheme);
        swipeRefreshLayout.setOnRefreshListener(listener);
        WGoodsAdapter oAdapter = new WGoodsAdapter(mContext, goodsList, this);
//        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //设置adapter
        layoutManager = new LinearLayoutManager(mContext);

        adapter = new RecyclerViewHeaderFooterAdapter(layoutManager, oAdapter);

        iLoadView = new ILoadViewImpl(mContext, new mLoadMoreClickListener());

        loadMoreView = iLoadView.inflate();
        recyclerView.addItemDecoration(new MyDecoration(mContext, MyDecoration.VERTICAL_LIST));
        recyclerView.addOnScrollListener(new MyScrollListener());


        if (recyclerView != null) {
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }

        loadItemtype(false);

        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(Contants.Bro.CLEAR_ALL_FACTOR_GOODS_W);
        //注册广播
        mContext.registerReceiver(mBroadcastReceiver, myIntentFilter);

        tagGroup.setOnTagClickListener(new TagGroup.OnTagClickListener() {
            @Override
            public void onTagClick(String tag) {
                if (tag.equals("点击重置筛选条件")) {
                    username = "";
                    type = "";
                    mItemtype = null;
                    valueEt.setText("");
                    if (null != swipeRefreshLayout) {

                        swipeRefreshLayout.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                swipeRefreshLayout.setRefreshing(true);

                                refreshRequest();
                            }
                        }, 200);
                    }
                }

            }
        });
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
        setTagGroup();
        EventBus.getDefault().register(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Goods Goods) {
        Goods mGoods = goodsList.get(CurrentPosition);
        mGoods.setItemsum(Goods.getItemsum());
        mGoods.setName(Goods.getName());
        mGoods.setPrice(Goods.getPrice());
        if (!"".equals(Goods.getImgurl())) {
            mGoods.setImgurl(Goods.getImgurl());
        }
        adapter.notifyItemChanged(CurrentPosition);
    }

    public void refreshRequest() {
        setTagGroup();
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", String.valueOf(mCurrentPage));
        params.put("type", type == null ? "" : type);
        params.put("itemname", username);
        params.put("typeid", categoryId);//96
        params.put("industryid", bigtypeid);
        params.put("keyword", keyWord);

        adapter.removeFooter(loadMoreView);
        ShowLog.e(mCurrentPage + "");
        HttpHelper.getInstance().post(mContext, Contants.PortA.GOODSITEMLIST, params, new OkHttpResponseHandler<String>(mContext) {

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
                    goodsList.clear();
                    goodsList.addAll(jsonHelper.getDatas(json));
                    if (goodsList.size() >= 10) {
                        adapter.addFooter(loadMoreView);
                    } else {
                        adapter.removeFooter(loadMoreView);
                    }
                    if (goodsList.size() == 0) {
                        //showToastShort("别扯了，我是有底线的！");
                    }
                } else if (JsonHelper.getRequstOK(json) == 6) {
                    if (goodsList.size() > 0) {
                        showToastShort("别扯了，我是有底线的！！");
                    } else {
                        showToastShort("没有数据");
                    }

                    adapter.removeFooter(loadMoreView);
                } else {
                    showToastShort(Contants.NetStatus.NETLOADERROR);
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
        if (goodsList.size() < 10) {
            return;
        }
        mCurrentPage++;
        iLoadView.showLoadingView(loadMoreView);
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", String.valueOf(mCurrentPage));
        params.put("type", type == null ? "" : type);
        params.put("itemname", username);
        params.put("typeid", categoryId);//96
        params.put("industryid", bigtypeid);
        params.put("keyword", keyWord);
        HttpHelper.getInstance().post(mContext, Contants.PortA.GOODSITEMLIST, params, new OkHttpResponseHandler<String>(mContext) {

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
                    mCurrentPage--;
                    showToastShort(Contants.NetStatus.NETLOADERROR);
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

    SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mCurrentPage = 1;
            refreshRequest();
        }
    };

    @Override
    public void onItemClick(int position) {

        Bundle bundle = new Bundle();
        bundle.putString("itemnoid", goodsList.get(position).getNumberid());
        bundle.putString("id", goodsList.get(position).getId());
        CommonUtils.goActivity(mContext, WGoodsDetailActivity.class, bundle, false);
        CurrentPosition = position;
    }

    @Override
    public void onLongItemClick(final int position) {
    }

    public void setTagGroup() {
        tArray.clear();

        if (!StringHelper.isEmpty(type) && mItemtype != null) {
            tArray.add("类型:" + mItemtype.getName());
        }
        if (!StringHelper.isEmpty(keyWord)) {
            tArray.add("搜索:" + keyWord);
        }
        if (tArray.size() > 0) {
            tArray.add("点击重置筛选条件");
            tagGroup.setVisibility(View.VISIBLE);
        } else {
            tagGroup.setVisibility(View.GONE);
        }
        tagGroup.setTags(tArray);

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

    /**
     * 加载商品分类
     */
    public void loadItemtype(final boolean isShow) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        //显示ProgressDialog
        final KProgressHUD progressDialog = growProgress(Contants.Progress.LOAD_ING);

        HttpHelper.getInstance().post(mContext, Contants.PortA.ITEMTYPE, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
                isRequestingType = false;
                progressDialog.dismiss();
            }

            @Override
            public void onBefore() {
                super.onBefore();
                isRequestingType = true;
                if (isShow) {
                    progressDialog.show();
                }
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);

                System.out.println("response" + json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JsonHelper<Itemtype> jsonHelper = new JsonHelper<Itemtype>(Itemtype.class);
                    itemtypeList = jsonHelper.getDatas(json);
                    PreferenceUtils.setPrefString(mContext, Contants.Preference.ITEMTYPE, json);
                    if (isShow) {
                        mItemtype = null;
                        showChooseify("选择商品类型", "0");
                    }
                } else {
                    if (StringHelper.isEmpty(PreferenceUtils.getPrefString(mContext, Contants.Preference.ITEMTYPE, ""))) {

                    } else {
                        json = PreferenceUtils.getPrefString(mContext, Contants.Preference.ITEMTYPE, "");
                        JsonHelper<Itemtype> jsonHelper = new JsonHelper<Itemtype>(Itemtype.class);
                        itemtypeList = jsonHelper.getDatas(json);
                        if (isShow) {
                            mItemtype = null;
                            showChooseify("选择商品类型", "0");
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                if (StringHelper.isEmpty(PreferenceUtils.getPrefString(mContext, Contants.Preference.ITEMTYPE, ""))) {

                } else {
                    String sjson = PreferenceUtils.getPrefString(mContext, Contants.Preference.ITEMTYPE, "");
                    JsonHelper<Itemtype> jsonHelper = new JsonHelper<Itemtype>(Itemtype.class);
                    itemtypeList = jsonHelper.getDatas(sjson);
                    if (isShow) {
                        mItemtype = null;
                        showChooseify("选择商品类型", "0");
                    }
                }
            }
        });
    }

    /**************************
     * 弹出框
     *****************/

    Itemtype mItemtype;

    public void showChooseify(String title, String id) {
        final List<Itemtype> list = new ArrayList<>();
        Itemtype item = new Itemtype();
        for (Itemtype itemtype : itemtypeList) {
            if (itemtype.getPid().equals(id)) {
                list.add(itemtype);
            }
            if (itemtype.getId().equals(id)) {
                item = itemtype;
            }
        }
        if (list.size() == 0) {
            type = item.getId();
            if (null != swipeRefreshLayout) {
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(true);
                        refreshRequest();
                    }
                }, 50);
            }
            return;
        }
        final String[] array;
        if (mItemtype == null) {
            array = new String[list.size()];
            int i = 0;
            for (Itemtype itemtype : list) {
                array[i] = itemtype.getName();
                i++;
            }
        } else {
            array = new String[list.size() + 1];
            int i = 1;
            array[0] = mItemtype.getName();
            for (Itemtype itemtype : list) {
                array[i] = itemtype.getName();
                i++;
            }
        }

        new MaterialDialog.Builder(mContext)
                .title(title)
                .items(array)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if (mItemtype != null && mItemtype.getName().equals(array[which])) {
                            type = mItemtype.getId();
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
                            if (mItemtype == null) {
                                mItemtype = list.get(which);
                                showChooseify(text == null ? "" : text.toString(), list.get(which).getId());
                            } else {
                                mItemtype = list.get(which - 1);
                                showChooseify(text == null ? "" : text.toString(), list.get(which - 1).getId());
                            }

                        }
                    }
                })
                .positiveText("重置")

                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        type = "";
                        if (null != swipeRefreshLayout) {
                            swipeRefreshLayout.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    swipeRefreshLayout.setRefreshing(true);
                                    refreshRequest();
                                }
                            }, 50);
                        }
                    }
                })
                .show();
    }

    /************************/

    /**
     * 右侧事件操作
     **/

    @OnClick(R.id.submitTv)
    public void search() {
        bgView.setVisibility(View.GONE);
        topsearchLy.setVisibility(View.GONE);
        if (isRequesting)
            return;

        String str = valueEt.getText().toString().trim();

        keyWord = str;
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

    @OnClick(R.id.id_right_btu)
    public void openDrawer() {
        if (isRequesting)
            return;
        if (topsearchLy.getVisibility() == View.GONE) {
            bgView.setVisibility(View.VISIBLE);
            AnimationUtil.setTranslationY(new AnimationUtil.OnEndListener() {
                @Override
                public void onEnd() {
                    topsearchLy.setVisibility(View.VISIBLE);
                }
            }, topsearchLy, -CommonUtils.screenHeight(mContext) / 2, 0, 300);
            valueEt.setFocusableInTouchMode(true);
            valueEt.setFocusable(true);
            valueEt.requestFocus();
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        } else {
            unkeyboard();
            bgView.setVisibility(View.GONE);
            AnimationUtil.setTranslationY(new AnimationUtil.OnEndListener() {
                @Override
                public void onEnd() {
                    topsearchLy.setVisibility(View.GONE);
                }
            }, topsearchLy, 0, -CommonUtils.screenHeight(mContext) / 2, 300);
        }
    }

    @OnClick(R.id.bgView)
    public void closeBg() {
        bgView.setVisibility(View.GONE);
        AnimationUtil.setTranslationY(new AnimationUtil.OnEndListener() {
            @Override
            public void onEnd() {
                topsearchLy.setVisibility(View.GONE);
            }
        }, topsearchLy, 0, -CommonUtils.screenHeight(mContext) / 2, 300);
    }

//    @OnClick(R.id.alltype)
//    public void onclickType() {
//        bgView.setVisibility(View.GONE);
//        topsearchLy.setVisibility(View.GONE);
//
//        if (isRequesting)
//            return;
//        if (isRequestingType) {
//            showToastShort("正在努力加载...");
//            return;
//        }
//
//        if (itemtypeList.size() == 0) {
//            loadItemtype(true);
//        } else {
//            mItemtype = null;
//            showChooseify("选择商品类型", "0");
//        }
//    }

    /****************
     * 广播
     *************/
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Contants.Bro.CLEAR_ALL_FACTOR_GOODS_W)) {
                mCurrentPage = 0;
                type = "";
                username = "";
                mItemtype = null;
                valueEt.setText("");
                if (null != swipeRefreshLayout) {
                    swipeRefreshLayout.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(true);
                            refreshRequest();
                        }
                    }, 50);
                }
            }
        }
    };


    @Override
    public void onDestroy() {
        mContext.unregisterReceiver(mBroadcastReceiver);
        EventBus.getDefault().unregister(this);
        super.onDestroy();

    }
}
