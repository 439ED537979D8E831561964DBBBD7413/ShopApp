package com.yj.shopapp.ui.activity.shopkeeper;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.BrandGroup;
import com.yj.shopapp.ubeen.IndustryCatelist;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.SBrandAdapter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class SSecondActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.search_layout)
    LinearLayout searchLayout;
    @BindView(R.id.value_Et)
    EditText valueEt;
    private GridLayoutManager layoutManager;
    boolean isBrang;
    private IndustryCatelist industryCatelist;
    private List<BrandGroup> brandGroup = new ArrayList<>();
    private SBrandAdapter brandAdapter;
    private List<BrandGroup.ListBean> listBeans = new ArrayList<>();
    private List<IndustryCatelist.DataBean.TagGroup> groups = new ArrayList<>();
    private List<BrandGroup.ListBean> NewlistBeans = new ArrayList<>();
    private List<IndustryCatelist.DataBean.TagGroup> Newgroups = new ArrayList<>();
    private boolean isSereen;
    private boolean isRefresh;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_ssecond;
    }

    @Override
    protected void initData() {
        if (getIntent().hasExtra("Name")) {
            title.setText(getIntent().getStringExtra("Name"));
        }
        brandAdapter = new SBrandAdapter(mContext);
        idRightBtu.setText("品牌");
        swipeRefreshLayout.setColorSchemeResources(Contants.Refresh.refreshColorScheme);
        swipeRefreshLayout.setOnRefreshListener(listener);
        layoutManager = new GridLayoutManager(mContext, 4);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(brandAdapter);
        }
        brandAdapter.setOnItemClickListener(this);
        valueEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!"".equals(s.toString())) {
                    if (isBrang) {
                        for (BrandGroup.ListBean bean : listBeans) {
                            if (!bean.isSort()) {
                                if (bean.getName().contains(s.toString())) {
                                    NewlistBeans.add(bean);
                                }
                            } else {
                                NewlistBeans.add(bean);
                            }
                        }
                        List<BrandGroup.ListBean> list = new ArrayList<>();
                        for (int i = 0; i < NewlistBeans.size(); i++) {
                            if (i == NewlistBeans.size() - 1) {
                                if (NewlistBeans.get(i).isSort()) {
                                    continue;
                                } else {
                                    list.add(NewlistBeans.get(i));
                                    continue;
                                }
                            }
                            if (NewlistBeans.get(i).isSort() && !NewlistBeans.get(i + 1).isSort()
                                    || !NewlistBeans.get(i).isSort() && NewlistBeans.get(i + 1).isSort()
                                    || !NewlistBeans.get(i).isSort() && !NewlistBeans.get(i + 1).isSort()) {
                                list.add(NewlistBeans.get(i));
                            }
                        }
                        NewlistBeans = list;
                        brandAdapter.setList(NewlistBeans);
                    } else {
                        for (IndustryCatelist.DataBean.TagGroup group : groups) {
                            if (!group.isSort()) {
                                if (group.getName().contains(s.toString())) {
                                    Newgroups.add(group);
                                }
                            } else {
                                Newgroups.add(group);
                            }
                        }
                        List<IndustryCatelist.DataBean.TagGroup> list = new ArrayList<>();
                        for (int i = 0; i < Newgroups.size(); i++) {
                            if (i == Newgroups.size() - 1) {
                                if (Newgroups.get(i).isSort()) {
                                    continue;
                                } else {
                                    list.add(Newgroups.get(i));
                                    continue;
                                }
                            }
                            if (Newgroups.get(i).isSort() && !Newgroups.get(i + 1).isSort()
                                    || !Newgroups.get(i).isSort() && Newgroups.get(i + 1).isSort()
                                    || !Newgroups.get(i).isSort() && !Newgroups.get(i + 1).isSort()) {
                                list.add(Newgroups.get(i));
                            }
                        }
                        Newgroups = list;
                        brandAdapter.setList(Newgroups);
                    }
                    isSereen = true;
                } else {
                    isSereen = false;
                    if (isBrang) {
                        brandAdapter.setList(listBeans);
                    } else {
                        brandAdapter.setList(groups);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if (NetUtils.isNetworkConnected(mContext)) {
            if (null != swipeRefreshLayout) {

                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(true);
                        industry();
                        getBrand();
                    }
                }, 200);
            }
        } else {
            showToastShort("网络不给力");
        }
    }

    SwipeRefreshLayout.OnRefreshListener listener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            industry();
            getBrand();
        }
    };

    private void industry() {
        if (isRefresh)return;
        groups.clear();
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("cid", getIntent().getStringExtra("CId"));
        HttpHelper.getInstance().post(mContext, Contants.PortU.INDUSTRY_CATELIST, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onAfter() {
                super.onAfter();
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                Mosaic();
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JSONObject object = JSONObject.parseObject(json);
                    if (object.getInteger("status") == 1) {
                        industryCatelist = object.toJavaObject(IndustryCatelist.class);
                        for (int i = 0; i < industryCatelist.getData().size(); i++) {
                            IndustryCatelist.DataBean bean = industryCatelist.getData().get(i);
                            groups.add(new IndustryCatelist.DataBean.TagGroup(bean.getName(), true));
                            groups.addAll(industryCatelist.getData().get(i).getList());
                        }
                    } else {
                        showToastShort(object.getString("info"));
                    }

                }
            }
        });
    }

    @OnClick({R.id.forewadImg, R.id.id_right_btu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forewadImg:
                finish();
                break;
            case R.id.id_right_btu:
                isBrang = !isBrang;
                idRightBtu.setText(isBrang ? "分类" : "品牌");
                valueEt.setHint(isBrang ? "分类搜索" : "品牌搜索");
                if (isBrang) {
                    brandAdapter.setList(listBeans);
                    ShowLog.e(listBeans.size() + "");
                } else {
                    brandAdapter.setList(groups);
                    ShowLog.e(groups.size() + "");
                }
                break;
            default:
                break;
        }
    }

    public void getBrand() {
        if (isRefresh)return;
        listBeans.clear();
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("cid", getIntent().getStringExtra("CId"));
        HttpHelper.getInstance().post(mContext, Contants.PortU.BRANDGROUP, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onAfter() {
                super.onAfter();
                Mosaic();
            }

            @Override
            public void onBefore() {
                super.onBefore();
                isRefresh = true;
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    if (!"".equals(json)) {
                        brandGroup = JSONArray.parseArray(json, BrandGroup.class);
                        for (int i = 0; i < brandGroup.size(); i++) {
                            BrandGroup group = brandGroup.get(i);
                            listBeans.add(new BrandGroup.ListBean(group.getName(), true));
                            listBeans.addAll(group.getList());
                        }
                    }
                }
            }
        });
    }

    /**
     * 组装list
     */
    private void Mosaic() {
        if (isBrang) {
            brandAdapter.setList(listBeans);
        } else {
            brandAdapter.setList(groups);
        }
        isRefresh=false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (isBrang) {
            Bundle bundle = new Bundle();
            if (isSereen) {
                if (NewlistBeans.get(position).isSort()) {
                    return;
                }
                bundle.putString("bid", NewlistBeans.get(position).getId());
                bundle.putString("typeName", NewlistBeans.get(position).getName());
            } else {
                if (listBeans.get(position).isSort()) {
                    return;
                }
                bundle.putString("bid", listBeans.get(position).getId());
                bundle.putString("typeName", listBeans.get(position).getName());
            }
            CommonUtils.goActivity(mContext, SGoodsActivity.class, bundle);

        } else {

            Bundle bundle = new Bundle();
            if (isSereen) {
                if (Newgroups.get(position).isSort()) {
                    return;
                }
                bundle.putString("typeid", Newgroups.get(position).getId());
                bundle.putString("typeName", Newgroups.get(position).getName());
            } else {
                if (groups.get(position).isSort()) {
                    return;
                }
                bundle.putString("typeid", groups.get(position).getId());
                bundle.putString("typeName", groups.get(position).getName());
            }
            CommonUtils.goActivity(mContext, SGoodsActivity.class, bundle);
        }
    }


}
