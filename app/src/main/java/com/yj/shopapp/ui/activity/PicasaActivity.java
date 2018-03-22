package com.yj.shopapp.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.View;
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
import com.yj.shopapp.ui.activity.adapter.PicasaAdapter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.util.PreferenceUtils;
import com.yj.shopapp.view.headfootrecycleview.OnRecyclerViewScrollListener;
import com.yj.shopapp.view.headfootrecycleview.RecyclerViewHeaderFooterAdapter;
import com.yj.shopapp.wbeen.Imglist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jm on 2016/4/27.
 * <p/>
 * 图片管理页面
 */
public class PicasaActivity extends BaseActivity implements BaseRecyclerView {


    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    GridLayoutManager staggeredGridLayoutManager;
    List<Imglist> imglists = new ArrayList<>();

    private RecyclerViewHeaderFooterAdapter adapter;

    private RecyclerView.LayoutManager layoutManager;

    private boolean isRequesting = false;//标记，是否正在刷新或者加载

    private int mCurrentPage = 0;

    private ILoadView iLoadView = null;
    private View loadMoreView = null;

    String uid;
    String token;

    boolean isAlter = false;
    String bundle;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_picasa;
    }

    @Override
    protected void initData() {
        idRightBtu.setText("添加");
        title.setText("我的图片");

        uid = PreferenceUtils.getPrefString(mContext,Contants.Preference.UID,"");
        token = PreferenceUtils.getPrefString(mContext,Contants.Preference.TOKEN,"");

        init();
    }

    private void init() {
        if (getIntent().hasExtra("chooseURL")) {
            bundle = getIntent().getStringExtra("chooseURL");
        }
        swipeRefreshLayout.setColorSchemeResources(Contants.Refresh.refreshColorScheme);
        swipeRefreshLayout.setOnRefreshListener(listener);

        layoutManager =  new StaggeredGridLayoutManager(Contants.Column.THREE, StaggeredGridLayoutManager.VERTICAL);

        PicasaAdapter picasaAdapter = new PicasaAdapter(mContext,imglists,this);
        adapter = new RecyclerViewHeaderFooterAdapter(layoutManager,picasaAdapter);

        iLoadView = new ILoadViewImpl(mContext, new mLoadMoreClickListener());

        loadMoreView = iLoadView.inflate();

        recyclerView.addOnScrollListener(new MyScrollListener());

        if(recyclerView != null) {
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
        }else{
            showToastShort("网络不给力");
        }

    }

    SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {

            refreshRequest();
        }
    };
@OnClick(R.id.id_right_btu)
public void addonclick()
{

}



    public void refreshRequest() {

        mCurrentPage = 1;

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", String.valueOf(mCurrentPage));
        params.put("number", "");

        HttpHelper.getInstance().post(mContext, Contants.PortA.IMGLIST, params, new OkHttpResponseHandler<String>(mContext) {

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

                imglists.clear();
                System.out.println("response"+json);
                if(JsonHelper.isRequstOK(json,mContext)){
                    JsonHelper<Imglist> jsonHelper = new JsonHelper<Imglist>(Imglist.class);

                    imglists.addAll(jsonHelper.getDatas(json));

                    if (imglists.size() >= 20) {
                        adapter.addFooter(loadMoreView);
                    }
                }else{
//                    showToastShort(JsonHelper.errorMsg(json));
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
                imglists.clear();
                adapter.notifyDataSetChanged();
            }
        });

    }

    public void loadMoreRequest(){
        if(isRequesting)
            return;
        if (imglists.size() < 20) {
            return;
        }

        mCurrentPage++;

        iLoadView.showLoadingView(loadMoreView);

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", String.valueOf(mCurrentPage));
        params.put("number", "");

        HttpHelper.getInstance().post(mContext, Contants.PortA.IMGLIST, params, new OkHttpResponseHandler<String>(mContext) {

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
                if(JsonHelper.isRequstOK(json,mContext)){
                    JsonHelper<Imglist> jsonHelper = new JsonHelper<Imglist>(Imglist.class);

                    if(jsonHelper.getDatas(json).size() == 0){
                        iLoadView.showFinishView(loadMoreView);
                    }else {
                        imglists.addAll(jsonHelper.getDatas(json));
                    }
                }else{
//                    showToastShort(JsonHelper.errorMsg(json));
                    mCurrentPage--;
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

    public void delImg(final int postion){
        if(isRequesting)
            return;

        //显示ProgressDialog
        final KProgressHUD progressDialog = growProgress(Contants.Progress.DELETE_ING);

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("imgid", imglists.get(postion).getId());

        HttpHelper.getInstance().post(mContext, Contants.PortA.DELIMG, params, new OkHttpResponseHandler<String>(mContext) {


            @Override
            public void onAfter() {
                super.onAfter();
                progressDialog.dismiss();
                isRequesting = false;
            }

            @Override
            public void onBefore() {
                super.onBefore();
                progressDialog.show();
                isRequesting = true;
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);

                System.out.println("response"+json);
                showToastShort(JsonHelper.errorMsg(json));
                if(JsonHelper.isRequstOK(json,mContext)){
                    imglists.remove(postion);
                    isAlter = true;
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


    @Override
    public void onItemClick(int position) {
        if (bundle.equals("getURL"))
        {
            Bundle bundle = new Bundle();

            bundle.putString("IMAGEURLS", imglists.get(position).getImgurl());



            CommonUtils.goResult(mContext, bundle, 001);
        }
        else {
            Bundle bundle = new Bundle();
            String[] mageurls = new String[1];
            mageurls[0] = imglists.get(position).getImgurl();
            bundle.putStringArray("IMAGEURLS", mageurls);
            CommonUtils.goActivity(mContext, PreviewActivity.class, bundle, false);
        }

    }

    @Override
    public void onLongItemClick(final int position) {
        new MaterialDialog.Builder(this)
                .content("确定删除该图片？")
                .positiveText("确定")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        delImg(position);
                    }
                })
                .show();

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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(isRequesting)
                return true;
            onFinish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onFinish(){
        if(isAlter){
            setResult(10);
            finish();
        }else{
            finish();
        }
    }
}
