package com.yj.shopapp.ui.activity.wholesale;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.yj.shopapp.presenter.BaseRecyclerView;
import com.yj.shopapp.ubeen.Province;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.AreaAdapter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.view.headfootrecycleview.OnRecyclerViewScrollListener;
import com.yj.shopapp.view.headfootrecycleview.RecyclerViewHeaderFooterAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by huanghao on 2016/12/8.
 */

public class AreaActivity extends BaseActivity implements BaseRecyclerView {
    @BindView(R.id.recycler_province)
    RecyclerView recyclerProvince;
    @BindView(R.id.recycler_area)
    RecyclerView recyclerArea;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;
    private RecyclerViewHeaderFooterAdapter prinAdapter;
    private RecyclerViewHeaderFooterAdapter areaAdapter;
    String role = "1";
    private ILoadView iLoadView = null;
    private View loadMoreView = null;
    private RecyclerView.LayoutManager areaLayoutManager;
    private RecyclerView.LayoutManager prinLayoutManager;
    List<Province> areaList1 = new ArrayList<>();
    List<Province> provinceList = new ArrayList<>();
    List<Integer> isPSelect = new ArrayList<>();
    int oldPosition;
    String name = "";
    String areaId;
    int mPosition;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_area;
    }

    @Override
    protected void initData() {
        title.setText("选择所在区域");

        role = getIntent().getStringExtra("role");
        Log.e("m_tag", role);
        AreaAdapter nAdapter = new AreaAdapter(mContext, provinceList, this, isPSelect);
        AreaAdapter nareaAdapter = new AreaAdapter(mContext, areaList1, baseRecyclerView, null);

        prinLayoutManager = new LinearLayoutManager(mContext);
        areaLayoutManager = new LinearLayoutManager(mContext);
        iLoadView = new ILoadViewImpl(mContext, new AreaActivity.mLoadMoreClickListener());

        loadMoreView = iLoadView.inflate();
        prinAdapter = new RecyclerViewHeaderFooterAdapter(prinLayoutManager, nAdapter);
        areaAdapter = new RecyclerViewHeaderFooterAdapter(areaLayoutManager, nareaAdapter);

        if (recyclerProvince != null) {
            recyclerProvince.setLayoutManager(prinLayoutManager);
            recyclerProvince.setAdapter(prinAdapter);
        }
        if (recyclerArea != null) {
            recyclerArea.setLayoutManager(areaLayoutManager);
            recyclerArea.setAdapter(areaAdapter);
        }


        getProvince();
    }


    SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {

        }
    };

    @Override
    public void onItemClick(int position) {
        isPSelect.set(oldPosition, 0);
        isPSelect.set(position, 1);
        areaId = provinceList.get(position).getId();
        name = provinceList.get(position).getArea_name();
        prinAdapter.notifyDataSetChanged();
        oldPosition = position;
        getArea();
    }

    @Override
    public void onLongItemClick(int position) {

    }

    public void getArea() {
        Map<String, String> params = new HashMap<>();
        params.put("putype", role);
        params.put("parent_id", areaId);
        HttpHelper.getInstance().post(mContext, Contants.PortA.Getchildarea, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onBefore() {
                super.onBefore();
            }

            @Override
            public void onAfter() {
                super.onAfter();

            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JsonHelper<Province> jsonHelper = new JsonHelper<Province>(Province.class);
                    if (jsonHelper.getDatas(json).size() != 0) {
                        areaList1.clear();
                        areaList1.addAll(jsonHelper.getDatas(json));
                        areaAdapter.notifyDataSetChanged();
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString("name", name);
                        bundle.putSerializable("area", (Serializable) areaList1.get(mPosition));
                        CommonUtils.goResult(mContext, bundle, 999);
                    }

                    // areaSp1.showContextMenu();
                }
            }
        });
    }

    /**
     * 获取省份
     */
    private void getProvince() {
        provinceList.clear();
        Map<String, String> params = new HashMap<>();
        params.put("putype", role + "");
        HttpHelper.getInstance().post(mContext, Contants.PortA.Getprovince, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onBefore() {
                super.onBefore();
            }

            @Override
            public void onAfter() {
                super.onAfter();

            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JsonHelper<Province> jsonHelper = new JsonHelper<Province>(Province.class);
                    provinceList.addAll(jsonHelper.getDatas(json));
                    for (Province p : provinceList) {
                        isPSelect.add(0);
                    }
                    prinAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    BaseRecyclerView baseRecyclerView = new BaseRecyclerView() {
        @Override
        public void onItemClick(int position) {
            mPosition = position;
            name = name + areaList1.get(position).getArea_name();
            areaId = areaList1.get(position).getId();
            getArea();
        }

        @Override
        public void onLongItemClick(int position) {

        }
    };



    public class MyScrollListener extends OnRecyclerViewScrollListener {

        @Override
        public void onScrollUp() {

        }

        @Override
        public void onScrollDown() {
        }

        @Override
        public void onBottom() {
            getProvince();
        }

        @Override
        public void onMoved(int distanceX, int distanceY) {

        }
    }

    public class mLoadMoreClickListener implements LoadMoreClickListener {

        @Override
        public void clickLoadMoreData() {

        }
    }
}
