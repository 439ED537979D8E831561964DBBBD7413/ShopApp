package com.yj.shopapp.ui.activity.wholesale;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.yj.shopapp.ui.activity.adapter.WNewGoodsAdapter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.util.PreferenceUtils;
import com.yj.shopapp.view.headfootrecycleview.OnRecyclerViewScrollListener;
import com.yj.shopapp.view.headfootrecycleview.RecyclerViewHeaderFooterAdapter;
import com.yj.shopapp.wbeen.Goods;
import com.yj.shopapp.wbeen.Itemtype;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/8/30.
 */
public class WNewGoodAcitvity extends BaseActivity implements BaseRecyclerView {


    //    @BindView(R.id.first_low)
//    TextView firstLow;
//    @BindView(R.id.first_high)
//    TextView firstHigh;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    //    @BindView(R.id.sort_price)
//    TextView sortPrice;
//    @BindView(R.id.IndustryList)
//    RecyclerView IndustryList;
//    @BindView(R.id.hide_view)
    // LinearLayout hideView;
    @BindView(R.id.view_transparent)
    View viewTransparent;
    @BindView(R.id.content_tv)
    TextView contentTv;
    @BindView(R.id.right_tv)
    ImageView rightTv;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private ILoadView iLoadView = null;
    private View loadMoreView = null;


    private RecyclerViewHeaderFooterAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private boolean isRequesting = false;//标记，是否正在刷新
    private boolean isRequestingType = false;//标记，获取类型是否正在刷新

    private int mCurrentPage = 0;

    private List<Goods> goodsList = new ArrayList<>();
    private List<Itemtype> itemtypeList = new ArrayList<>();
    List<String> tArray = new ArrayList<>();

    String uid;
    String token;
    String username = "";
    String type = "";

    @Override
    protected int getLayoutId() {
        return R.layout.wactivity_wnewgood;
    }

    @Override
    protected void initData() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        contentTv.setVisibility(View.GONE);
        uid = PreferenceUtils.getPrefString(mContext, Contants.Preference.UID, "");
        token = PreferenceUtils.getPrefString(mContext, Contants.Preference.TOKEN, "");

        swipeRefreshLayout.setColorSchemeResources(Contants.Refresh.refreshColorScheme);
        swipeRefreshLayout.setOnRefreshListener(listener);

        WNewGoodsAdapter oAdapter = new WNewGoodsAdapter(mContext, goodsList, this);


//        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //设置adapter
        layoutManager = new LinearLayoutManager(mContext);

        adapter = new RecyclerViewHeaderFooterAdapter(layoutManager, oAdapter);

        iLoadView = new ILoadViewImpl(mContext, new mLoadMoreClickListener());

        loadMoreView = iLoadView.inflate();

        recyclerView.addOnScrollListener(new MyScrollListener());


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
        //setTabLayout();
    }

    public void refreshRequest() {
        mCurrentPage = 1;
        //setTagGroup();
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        adapter.removeFooter(loadMoreView);
        HttpHelper.getInstance().post(mContext, Contants.PortA.Newitemlist, params, new OkHttpResponseHandler<String>(mContext) {

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
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JsonHelper<Goods> jsonHelper = new JsonHelper<Goods>(Goods.class);

                    goodsList.addAll(jsonHelper.getDatas(json));

                    if (goodsList.size() >= 10) {
                        adapter.addFooter(loadMoreView);
                    } else {
                        adapter.removeFooter(loadMoreView);
                    }
                } else if (JsonHelper.getRequstOK(json) == 6) {
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
//        params.put("type", type == null ? "" : type);
//        params.put("itemname", username);

        HttpHelper.getInstance().post(mContext, Contants.PortA.Newitemlist, params, new OkHttpResponseHandler<String>(mContext) {

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

            refreshRequest();
        }
    };

    @Override
    public void onItemClick(int position) {

        Bundle bundle = new Bundle();
        bundle.putString("itemnoid", goodsList.get(position).getNumberid());
        bundle.putString("id", goodsList.get(position).getId());
        CommonUtils.goActivity(mContext, WGoodsDetailActivity.class, bundle, false);
    }

    @Override
    public void onLongItemClick(final int position) {
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
    public void showToastShort(String msg) {
        super.showToastShort(msg);
    }


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }
}
