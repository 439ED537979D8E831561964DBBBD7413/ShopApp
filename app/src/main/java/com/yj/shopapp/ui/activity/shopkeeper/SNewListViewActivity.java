package com.yj.shopapp.ui.activity.shopkeeper;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.loading.ILoadView;
import com.yj.shopapp.loading.ILoadViewImpl;
import com.yj.shopapp.loading.LoadMoreClickListener;
import com.yj.shopapp.presenter.BaseRecyclerView;
import com.yj.shopapp.ui.activity.adapter.NewsAdapter;
import com.yj.shopapp.ui.activity.base.BaseFragment;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.util.PreferenceUtils;
import com.yj.shopapp.view.headfootrecycleview.OnRecyclerViewScrollListener;
import com.yj.shopapp.view.headfootrecycleview.RecyclerViewHeaderFooterAdapter;
import com.yj.shopapp.wbeen.Msgs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by jm on 2016/5/16.
 */
public class SNewListViewActivity extends BaseFragment implements BaseRecyclerView {


    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private RecyclerViewHeaderFooterAdapter adapter;

    private RecyclerView.LayoutManager layoutManager;

    private boolean isRequesting = false;//标记，是否正在刷新或者加载

    private int mCurrentPage = 0;

    private List<Msgs> megsList = new ArrayList<>();

    String uid;
    String token;
    String mtype = "0";

    private ILoadView iLoadView = null;
    private View loadMoreView = null;



    public static SNewListViewActivity newInstance(String content) {
        SNewListViewActivity fragment = new SNewListViewActivity();
        fragment.mtype = content;
        return fragment;
    }

    @Override
    public void init(Bundle savedInstanceState) {


        uid = PreferenceUtils.getPrefString(mActivity, Contants.Preference.UID,"");
        token = PreferenceUtils.getPrefString(mActivity,Contants.Preference.TOKEN,"");

        swipeRefreshLayout.setColorSchemeResources(Contants.Refresh.refreshColorScheme);
        swipeRefreshLayout.setOnRefreshListener(listener);

        NewsAdapter nAdapter = new NewsAdapter(mActivity,megsList,this);

        layoutManager = new LinearLayoutManager(mActivity);

        adapter = new RecyclerViewHeaderFooterAdapter(layoutManager,nAdapter);

        iLoadView = new ILoadViewImpl(mActivity, new mLoadMoreClickListener());

        loadMoreView = iLoadView.inflate();

        recyclerView.addOnScrollListener(new MyScrollListener());


        if(recyclerView != null) {
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }

        if (NetUtils.isNetworkConnected(mActivity)) {
            if (null != swipeRefreshLayout) {

                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        swipeRefreshLayout.setRefreshing(true);

                        refreshRequest();
                    }
                }, 200);
            }
        }else{
            showToastShort("网络不给力");
        }

    }

    public void refreshRequest() {

        mCurrentPage = 1;

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", String.valueOf(mCurrentPage));
        params.put("mtype", mtype);
        adapter.removeFooter(loadMoreView);
        HttpHelper.getInstance().post(mActivity, Contants.PortU.Msgs, params, new OkHttpResponseHandler<String>(mActivity) {

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
                System.out.println("response"+json);
                if(JsonHelper.isRequstOK(json,mActivity)){
                    JsonHelper<Msgs> jsonHelper = new JsonHelper<Msgs>(Msgs.class);

                    megsList.addAll(jsonHelper.getDatas(json));

                    if (megsList.size() >= 20) {
                        adapter.addFooter(loadMoreView);
                    }
                }else if(JsonHelper.getRequstOK(json)==6){
                    adapter.removeFooter(loadMoreView);
                }else{
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

    public void loadMoreRequest(){
        if(isRequesting)
            return;
        if (megsList.size() < 20) {
            return;
        }
        mCurrentPage++;

        iLoadView.showLoadingView(loadMoreView);

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", String.valueOf(mCurrentPage));
        params.put("mtype", mtype);

        HttpHelper.getInstance().post(mActivity, Contants.PortU.Msgs, params, new OkHttpResponseHandler<String>(mActivity) {

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

                System.out.println("response"+json);
                if(JsonHelper.isRequstOK(json,mActivity)){
                    JsonHelper<Msgs> jsonHelper = new JsonHelper<Msgs>(Msgs.class);

                    if(jsonHelper.getDatas(json).size() == 0){
                        iLoadView.showFinishView(loadMoreView);
                    }else {
                        megsList.addAll(jsonHelper.getDatas(json));
                    }
                }else{
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

    SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {

            refreshRequest();
        }
    };

    @Override
    public void onItemClick(int position) {
        megsList.get(position).setStatus("0");
        Bundle bundle = new Bundle();
        bundle.putString("id",megsList.get(position).getId());
        CommonUtils.goActivity(mActivity,SMyNewsDetailActivity.class,bundle,false);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onLongItemClick(int position) {
//        showToastShort("changan"+position);
    }


    public class mLoadMoreClickListener implements LoadMoreClickListener {

        @Override
        public void clickLoadMoreData() {
            loadMoreRequest();
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
    public int getLayoutID() {
        return R.layout.activity_newslist;
    }
}
