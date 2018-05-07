package com.yj.shopapp.ui.activity.shopkeeper;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.yj.shopapp.ubeen.OrderDetails;
import com.yj.shopapp.ui.activity.adapter.SOrderDetailAdapter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.DateUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.view.headfootrecycleview.OnRecyclerViewScrollListener;
import com.yj.shopapp.view.headfootrecycleview.RecyclerViewHeaderFooterAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by jm on 2016/5/23.
 */
public class SOrderDetailActivity extends BaseActivity implements BaseRecyclerView {
    @BindView(R.id.usernameTv)
    TextView usernameTv;
    @BindView(R.id.text)
    TextView text;
    @BindView(R.id.oidTv)
    TextView oidTv;
    @BindView(R.id.moneyTv)
    TextView moneyTv;
    @BindView(R.id.oedertimeTv)
    TextView oedertimeTv;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.forewadImg)
    ImageView forewadImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;


    private ILoadView iLoadView = null;
    private View loadMoreView = null;

    private RecyclerViewHeaderFooterAdapter adapter;

    private LinearLayoutManager layoutManager;

    private boolean isRequesting = false;//标记，是否正在刷新

    private int mCurrentPage = 0;
    String oid;
    int isType = 0;  // 0 批发商 1 零售商
    String url;

    List<OrderDetails> notes = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.sactivity_orderdetail;
    }

    @Override
    protected void initData() {
        title.setText("订单详情");
        oid = getIntent().getExtras().getString("oid");
        isType = getIntent().getExtras().getInt("isType");
        if (isType == 0) {
            url = Contants.PortA.OrderDetails;
            usernameTv.setVisibility(View.VISIBLE);
            text.setVisibility(View.VISIBLE);
        } else {
            url = Contants.PortU.OrderDetails;
            usernameTv.setVisibility(View.GONE);
            text.setVisibility(View.GONE);
        }

        swipeRefreshLayout.setColorSchemeResources(Contants.Refresh.refreshColorScheme);
        swipeRefreshLayout.setOnRefreshListener(listener);

        SOrderDetailAdapter oAdapter = new SOrderDetailAdapter(mContext, notes, isType, this);


        layoutManager = new LinearLayoutManager(mContext);
        //设置adapter

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
    }


    public void refreshRequest() {
        mCurrentPage = 1;

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("oid", oid);
        params.put("p", String.valueOf(mCurrentPage));

        adapter.removeFooter(loadMoreView);
        HttpHelper.getInstance().post(mContext, url, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                    isRequesting = false;
                }
            }

            @Override
            public void onBefore() {
                super.onBefore();
                isRequesting = true;
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);

                notes.clear();
                System.out.println("response" + json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JsonHelper<OrderDetails> jsonHelper = new JsonHelper<OrderDetails>(OrderDetails.class);

                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        if (isType == 0) {
                            usernameTv.setText(jsonObject.getString("username"));
                        } else {
                            usernameTv.setText(jsonObject.getString("agentuser"));
                        }
                        oidTv.setText(jsonObject.getString("oid"));
                        moneyTv.setText(jsonObject.getString("money"));
                        oedertimeTv.setText(DateUtils.timet(jsonObject.getString("addtime")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    notes.addAll(jsonHelper.getDatas(json, "itemlist"));

                    if (notes.size() >= 20) {
                        adapter.addFooter(loadMoreView);
                    } else {
                        adapter.removeFooter(loadMoreView);
                    }
                } else if (JsonHelper.getRequstOK(json) == 6) {

                } else {
                    showToastShort(Contants.NetStatus.NETLOADERROR);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
                notes.clear();
                adapter.notifyDataSetChanged();
            }
        });

    }

    public void loadMoreRequest() {
        if (isRequesting)
            return;
        if (notes.size() < 20) {
            return;
        }

        mCurrentPage++;

        iLoadView.showLoadingView(loadMoreView);

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", String.valueOf(mCurrentPage));
        params.put("oid", oid);

        HttpHelper.getInstance().post(mContext, url, params, new OkHttpResponseHandler<String>(mContext) {

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
                    JsonHelper<OrderDetails> jsonHelper = new JsonHelper<OrderDetails>(OrderDetails.class);

                    if (jsonHelper.getDatas(json, "itemlist").size() == 0) {
                        iLoadView.showFinishView(loadMoreView);
                    } else {
                        notes.addAll(jsonHelper.getDatas(json, "itemlist"));
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
    public void onItemClick(int position) {

    }

    @Override
    public void onLongItemClick(int position) {

    }
}
