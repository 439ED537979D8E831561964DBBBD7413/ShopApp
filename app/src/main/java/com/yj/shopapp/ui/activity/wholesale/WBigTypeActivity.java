package com.yj.shopapp.ui.activity.wholesale;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.loading.ILoadView;
import com.yj.shopapp.loading.ILoadViewImpl;
import com.yj.shopapp.loading.LoadMoreClickListener;
import com.yj.shopapp.presenter.GoodsRecyclerView;
import com.yj.shopapp.ubeen.Classise;
import com.yj.shopapp.ui.activity.adapter.WBigTypeAdapter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.view.headfootrecycleview.OnRecyclerViewScrollListener;
import com.yj.shopapp.view.headfootrecycleview.RecyclerViewHeaderFooterAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by huanghao on 2016/11/26.
 */

public class WBigTypeActivity extends BaseActivity implements GoodsRecyclerView {
    @BindView(R.id.id_right_btu)
    TextView id_right_btu;

    @BindView(R.id.edit_ttx)
    TextView edit_ttx;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    private ILoadView iLoadView = null;
    private View loadMoreView = null;
    List<Integer> isedit=new ArrayList<Integer>();

    private RecyclerViewHeaderFooterAdapter adapter;

    private RecyclerView.LayoutManager layoutManager;


    private int mCurrentPage = 0;
    private String agentuid = ""; //临时批发商ID 做个判断
    private boolean isRequesting = false;//标记，是否正在刷新
    private boolean isRequestingType = false;//标记，获取类型是否正在刷新
    private List<Classise>classises=new ArrayList<>();
    final static int RESULTCODE=1111111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       ;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.sfragment_classification;
    }

    @Override
    protected void initData() {
        if (getIntent().hasExtra("action"))
        {
            if (getIntent().getStringExtra("action").equals("choose"))
            {
                edit_ttx.setVisibility(View.GONE);
                id_right_btu.setVisibility(View.GONE);
            }
        }
        else
        {
            edit_ttx.setVisibility(View.VISIBLE);
            id_right_btu.setVisibility(View.VISIBLE);
        }
        isedit.add(0);

        title.setText("分类");
        id_right_btu.setText("添加分类");


        swipeRefreshLayout.setColorSchemeResources(Contants.Refresh.refreshColorScheme);
//        swipeRefreshLayout.setOnRefrshListener(listener);
        WBigTypeAdapter sadapter=new WBigTypeAdapter(WBigTypeActivity.this,classises,this,isedit);
        layoutManager= new GridLayoutManager(mContext,5);
        adapter = new RecyclerViewHeaderFooterAdapter(layoutManager, sadapter);
        iLoadView = new ILoadViewImpl(mContext, new WBigTypeActivity.mLoadMoreClickListener());

        loadMoreView = iLoadView.inflate();

        recyclerView.addOnScrollListener(new WBigTypeActivity.MyScrollListener());


        if (recyclerView != null) {
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }
        refreshRequest();
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
            //loadMoreRequest();
        }

        @Override
        public void onMoved(int distanceX, int distanceY) {

        }
    }

    @OnClick(R.id.edit_ttx)
    public void onClick()
    {
        if (isedit.get(0)==1)
        {
            edit_ttx.setText("编辑");
            isedit.set(0,0);

        }
        else {
            isedit.set(0,1);
            edit_ttx.setText("取消");
        }
        adapter.notifyDataSetChanged();
    }
    @OnClick(R.id.id_right_btu)
    public void onClickAdd()
    {
        CommonUtils.goActivity(mContext,WaddCategoryActivity.class,null);

    }



    public void refreshRequest()
    {
        mCurrentPage=1;
        Map<String,String> params=new HashMap<String,String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", String.valueOf(mCurrentPage));


        HttpHelper.getInstance().post(mContext, Contants.PortA.Bigtypelist, params, new OkHttpResponseHandler<String>(mContext) {

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



                classises.clear();
                System.out.println("response" + json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JsonHelper<Classise> jsonHelper = new JsonHelper<Classise>(Classise.class);

                    classises.addAll(jsonHelper.getDatas(json));

                    if (classises.size() >= 20) {
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
//            showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
//            goodsList.clear();
//            adapter.notifyDataSetChanged();
            }
        });

    }

    /**********************
     * 删除分类
     ******************/

    public void delectRequest(String id) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("idstr", id);

        HttpHelper.getInstance().post(mContext, Contants.PortA.Delbigtype, params, new OkHttpResponseHandler<String>(mContext) {

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


                if (JsonHelper.isRequstOK(json, mContext)) {
                    edit_ttx.setText("编辑");
                    showToastShort(Contants.NetStatus.NETSUCCESS);
                    isedit.set(0,0);
                    adapter.notifyDataSetChanged();
                    refreshRequest();

                }else if(JsonHelper.getRequstOK(json)==6){

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




    public void loadMoreRequest() {
        if (isRequesting)
            return;
        if (classises.size() < 20) {
            return;
        }

        mCurrentPage++;

        iLoadView.showLoadingView(loadMoreView);

        Map<String,String> params=new HashMap<String,String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", String.valueOf(mCurrentPage));
//        params.put("agentuid", WId);

        HttpHelper.getInstance().post(mContext, Contants.PortU.ITEMLIST, params, new OkHttpResponseHandler<String>(mContext) {

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
                    JsonHelper<Classise> jsonHelper = new JsonHelper<Classise>(Classise.class);

                    if (jsonHelper.getDatas(json).size() == 0) {
                        iLoadView.showFinishView(loadMoreView);
                    } else {
                        classises.addAll(jsonHelper.getDatas(json));
                        classises.add(new Classise());
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




    @Override
    public void CardClick(int postion) {

    }

    @Override
    public void onItemClick(int position) {
        Bundle bundle=new Bundle();
        bundle.putString("typename",classises.get(position).getName());
        bundle.putString("typeId",classises.get(position).getId());
      CommonUtils.goResult(mContext,bundle,RESULTCODE);

    }

    @Override
    public void onLongItemClick(int position) {

    }
//    public void loadMoreRequest() {
//        if (isRequesting)
//            return;
//        if (goodsList.size() < 20) {
//            return;
//        }
//
//        mCurrentPage++;
//
//        iLoadView.showLoadingView(loadMoreView);
//
//        Map<string, string> params = new HashMap<string, string>();
//        params.put("uid", uid);
//        params.put("token", token);
//        params.put("p", string.valueOf(mCurrentPage));
//        params.put("agentuid", agentuid);
//        params.put("typeid", typeid);
//        params.put("itemname", username);
//
//        HttpHelper.getInstance().post(mActivity, Contants.PortU.ITEMLIST, params, new OkHttpResponseHandler<string>(mActivity) {
//
//            @Override
//            public void onAfter() {
//                super.onAfter();
//                isRequesting = false;
//            }
//
//            @Override
//            public void onBefore() {
//                super.onBefore();
//                isRequesting = true;
//            }
//
//            @Override
//            public void onResponse(Request request, string json) {
//                super.onResponse(request, json);
//
//                System.out.println("response" + json);
//                if (JsonHelper.isRequstOK(json, mActivity)) {
//                    JsonHelper<Goods> jsonHelper = new JsonHelper<Goods>(Goods.class);
//
//                    if (jsonHelper.getDatas(json).size() == 0) {
//                        iLoadView.showFinishView(loadMoreView);
//                    } else {
//                        goodsList.addAll(jsonHelper.getDatas(json));
//                    }
//                } else if (JsonHelper.getRequstOK(json) == 6) {
//                    iLoadView.showFinishView(loadMoreView);
//                } else {
//                    showToastShort(JsonHelper.errorMsg(json));
//                }
//
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onError(Request request, Exception e) {
//                super.onError(request, e);
//                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
//                mCurrentPage--;
//                iLoadView.showErrorView(loadMoreView);
//            }
//        });
//    }
//        SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//
//                refreshRequest();
//            }
//        };
}
