package com.yj.shopapp.ui.activity.shopkeeper;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
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
import com.yj.shopapp.ui.activity.adapter.SClassificationAdapter;
import com.yj.shopapp.ui.activity.base.BaseFragment;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DialogUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.PreferenceUtils;
import com.yj.shopapp.view.headfootrecycleview.OnRecyclerViewScrollListener;
import com.yj.shopapp.view.headfootrecycleview.RecyclerViewHeaderFooterAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by huanghao on 2016/10/18.
 */

public class SclassificationFragment extends BaseFragment implements GoodsRecyclerView{

@BindView(R.id.edit_ttx)
TextView edit_ttx;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.edit_li)
    LinearLayout edit_li ;
    private ILoadView iLoadView = null;
    private View loadMoreView = null;

    private RecyclerViewHeaderFooterAdapter adapter;

    private RecyclerView.LayoutManager layoutManager;
    boolean isedit=false;
    String uid;
    String token;
    private int mCurrentPage = 0;
    private String agentuid = ""; //临时批发商ID 做个判断
    private boolean isRequesting = false;//标记，是否正在刷新
    private boolean isRequestingType = false;//标记，获取类型是否正在刷新
    private List<Classise>classises=new ArrayList<>();

    @OnClick(R.id.search_ttx)
    public void searchOnclick()
    {
        DialogUtils dialogUtils=new DialogUtils();
        dialogUtils.getInputMaterialDialog(mActivity, "输入商品名称","输入商品名称", new MaterialDialog.InputCallback() {
            @Override
            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                Bundle bundle=new Bundle();
                bundle.putString("keyword",input.toString());
                CommonUtils.goActivity(mActivity,SGoodsActivity.class,bundle);
            }
        },null,null);
        dialogUtils.show();
    }
    @Override
    public void init(Bundle savedInstanceState) {
        edit_ttx.setVisibility(View.GONE);
        title.setText("分类");
        edit_li.setVisibility(View.GONE);
        uid = PreferenceUtils.getPrefString(mActivity, Contants.Preference.UID, "");
        token = PreferenceUtils.getPrefString(mActivity, Contants.Preference.TOKEN, "");
        swipeRefreshLayout.setColorSchemeResources(Contants.Refresh.refreshColorScheme);
//        swipeRefreshLayout.setOnRefreshListener(listener);
        SClassificationAdapter sadapter=new SClassificationAdapter(mActivity,classises,this);
        layoutManager= new GridLayoutManager(mActivity,5);
        adapter = new RecyclerViewHeaderFooterAdapter(layoutManager, sadapter);
        iLoadView = new ILoadViewImpl(mActivity, new mLoadMoreClickListener());

        loadMoreView = iLoadView.inflate();

        recyclerView.addOnScrollListener(new MyScrollListener());


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
            loadMoreRequest();
        }

        @Override
        public void onMoved(int distanceX, int distanceY) {

        }
    }
    @Override
    public int getLayoutID() {
        return R.layout.sfragment_classification;
    }


    /**
     * 分类接口
     */
    public void refreshRequest()
    {
        Map<String,String> params=new HashMap<String,String>();
        params.put("uid", uid);
        params.put("token", token);
//        params.put("p", string.valueOf(mCurrentPage));
//        params.put("agentuid", WId);

    HttpHelper.getInstance().post(mActivity, Contants.PortU.Classification, params, new OkHttpResponseHandler<String>(mActivity) {

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
            if (JsonHelper.isRequstOK(json, mActivity)) {
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

        HttpHelper.getInstance().post(mActivity, Contants.PortU.ITEMLIST, params, new OkHttpResponseHandler<String>(mActivity) {

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
                if (JsonHelper.isRequstOK(json, mActivity)) {
                    JsonHelper<Classise> jsonHelper = new JsonHelper<Classise>(Classise.class);

                    if (jsonHelper.getDatas(json).size() == 0) {
                        iLoadView.showFinishView(loadMoreView);
                    } else {
                        classises.addAll(jsonHelper.getDatas(json));
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void CardClick(int postion) {

    }

    @Override
    public void onItemClick(int position) {
        Bundle bundle=new Bundle();
        bundle.putString("bigtypeid",classises.get(position).getId());
        bundle.putString("bigtypeName",classises.get(position).getName());
        CommonUtils.goActivity(mActivity,SGoodsActivity.class,bundle);
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
