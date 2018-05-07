package com.yj.shopapp.ui.activity.shopkeeper;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.loading.ILoadView;
import com.yj.shopapp.loading.ILoadViewImpl;
import com.yj.shopapp.loading.LoadMoreClickListener;
import com.yj.shopapp.presenter.BaseRecyclerView;
import com.yj.shopapp.ubeen.Address;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.AddressAdapter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.PreferenceUtils;
import com.yj.shopapp.view.headfootrecycleview.OnRecyclerViewScrollListener;
import com.yj.shopapp.view.headfootrecycleview.RecyclerViewHeaderFooterAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by jm on 2016/5/14.
 */
public class SAddressActivity extends BaseActivity implements BaseRecyclerView, AddressAdapter.Choose {

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.id_right_btu)
    TextView id_right_btu;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;


    private ILoadView iLoadView = null;
    private View loadMoreView = null;

    private RecyclerViewHeaderFooterAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<Address> notes = new ArrayList<Address>();

    boolean isEdit = true; //是否可编译

    @Override
    protected int getLayoutId() {
        return R.layout.sactivity_address;
    }

    @Override
    protected void initData() {
        title.setText("收货地址管理");
        //id_right_btu.setText("添加");

        if (getIntent().hasExtra("isEdit")) {
            isEdit = getIntent().getExtras().getBoolean("isEdit");
        }
        AddressAdapter nAdapter = new AddressAdapter(SAddressActivity.this, notes, this, this, isEdit);

        layoutManager = new LinearLayoutManager(mContext);

        adapter = new RecyclerViewHeaderFooterAdapter(layoutManager, nAdapter);

        iLoadView = new ILoadViewImpl(mContext, new mLoadMoreClickListener());

        loadMoreView = iLoadView.inflate();
        recyclerView.addOnScrollListener(new MyScrollListener());

        if (recyclerView != null) {
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isNetWork(mContext)) {
            refreshRequest();
        }

    }

    @Override
    protected void onRestart() {
        adapter.notifyDataSetChanged();
        super.onRestart();
    }

    public void refreshRequest() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);

        HttpHelper.getInstance().post(mContext, Contants.PortU.Uaddress, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                notes.clear();
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JsonHelper<Address> jsonHelper = new JsonHelper<Address>(Address.class);
                    notes.addAll(jsonHelper.getDatas(json));
                    PreferenceUtils.setPrefString(mContext, "addressId", notes.get(0).getId());
                } else {
                    showToastShort(JsonHelper.errorMsg(json));
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
//        showToastShort("changan"+position);
    }

    @Override
    public void choose(int pos) {

    }

    @Override
    public void edit(int pos) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("been", notes.get(pos));
        CommonUtils.goActivity(mContext, SAddressRefreshActivity.class, bundle);
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

    public void delectAddres(String id) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("id", id);
        HttpHelper.getInstance().post(mContext, Contants.PortU.Deladdress, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();

            }

            @Override
            public void onBefore() {
                super.onBefore();

            }

            @Override
            public void onResponse(Request request, String json) {

                super.onResponse(request, json);

                notes.clear();
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    refreshRequest();
                } else {
                    showToastShort(JsonHelper.errorMsg(json));
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

    public void showDelectClint(final int position) {
        new MaterialDialog.Builder(SAddressActivity.this)
                .content("是否删除地址：" + notes.get(position).getAddress() + "?")
                .positiveText("是")
                .negativeText("否")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        delectAddres(notes.get(position).getId());
                    }
                })
                .show();
    }

}
