package com.yj.shopapp.ui.activity.shopkeeper;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.ClassifyList;
import com.yj.shopapp.ubeen.ShopCase;
import com.yj.shopapp.ui.activity.ImgUtil.NewBaseFragment;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.ClassifyListAdpter;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassifyDetaFragment extends NewBaseFragment implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.my_RecyclerView)
    RecyclerView myRecyclerView;
    @BindView(R.id.pullToRefresh)
    SwipeRefreshLayout pullToRefresh;
    private ClassifyListAdpter adpter;
    private ClassifyList classifyList;
    private ShopCase shopCase;
    private String StordId = "";
    private int Type;
    private AppBarLayout appBarLayout;
    private String titleName;

    public static ClassifyDetaFragment newInstance(String stordId, int type, String titlename) {
        Bundle args = new Bundle();
        ClassifyDetaFragment fragment = new ClassifyDetaFragment();
        args.putString("shop_id", stordId);
        args.putInt("type", type);
        args.putString("name", titlename);
        fragment.setArguments(args);
        return fragment;
    }

    private String getStordId() {
        return getArguments().getString("shop_id");
    }

    private int getTyep() {
        return getArguments().getInt("type");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_classify_deta;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        titleName = getArguments().getString("name");
        adpter = new ClassifyListAdpter(mActivity);
        myRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, 2));
        myRecyclerView.setAdapter(adpter);
        adpter.setOnItemClickListener(this);
        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.mAppBar);
        pullToRefresh.setOnRefreshListener(this);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                pullToRefresh.setEnabled(verticalOffset >= 0 ? true : false);
            }
        });

    }

    @Override
    protected void initData() {
        StordId = getStordId();
        Type = getTyep();
        if (NetUtils.isNetworkConnected(mActivity)) {
            pullToRefresh.setRefreshing(true);
            onRefresh();
        } else {
            showToast("无网络");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Type == 1) {
            if (classifyList != null) {
                adpter.setList(classifyList.getData());
            } else {
                if (NetUtils.isNetworkConnected(mActivity)) {
                    pullToRefresh.setRefreshing(true);
                    onRefresh();
                } else {
                    showToast("无网络");
                }
            }
        } else {
            if (shopCase != null) {
                adpter.setList(shopCase.getData());
            } else {
                if (NetUtils.isNetworkConnected(mActivity)) {
                    pullToRefresh.setRefreshing(true);
                    onRefresh();
                } else {
                    showToast("无网络");
                }
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (Type == 1) {
            Bundle bundle = new Bundle();
            bundle.putString("shop_id", classifyList.getData().get(position).getId());
            bundle.putString("Store_id", StordId);
            bundle.putString("titlename", classifyList.getData().get(position).getName());
            bundle.putString("shopname", titleName);
            CommonUtils.goActivity(mActivity, ShopListActivity.class, bundle);
        } else {
            Bundle bundle1 = new Bundle();
            bundle1.putString("shopname", titleName);
            bundle1.putString("caseId", shopCase.getData().get(position).getId());
            bundle1.putString("titlename", shopCase.getData().get(position).getTitle());
            CommonUtils.goActivity(mActivity, Shop_Case_detailsActivity.class, bundle1);
        }
    }

    private void getClassifyList() {
        Map<String, String> params = new HashMap<>(16);
        params.put("uid", uid);
        params.put("token", token);
        params.put("shop_id", StordId);

        HttpHelper.getInstance().post(mActivity, Contants.PortU.SHOP_CLASS, params, new OkHttpResponseHandler<String>(mActivity) {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                if (pullToRefresh != null) {
                    pullToRefresh.setRefreshing(false);
                }
            }

            @Override
            public void onAfter() {
                super.onAfter();
                if (pullToRefresh != null) {
                    pullToRefresh.setRefreshing(false);
                }
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mActivity)) {
                    classifyList = JSONObject.parseObject(json, ClassifyList.class);
                    if (classifyList.getStatus() == 1) {
                        adpter.setList(classifyList.getData());
                    } else {
                        showToast("无数据");
                    }
                }
            }
        });
    }

    private void shopCase() {
        Map<String, String> params = new HashMap<>(16);
        params.put("uid", uid);
        params.put("token", token);
        params.put("shop_id", StordId);
        //ShowLog.e("uid" + uid + "token" + token + "shop_id" + StordId);
        HttpHelper.getInstance().post(mActivity, Contants.PortU.SHOP_CASE, params, new OkHttpResponseHandler<String>(mActivity) {
            @Override
            public void onAfter() {
                super.onAfter();
                if (pullToRefresh != null) {
                    pullToRefresh.setRefreshing(false);
                }
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mActivity)) {
                    JSONObject object = JSONObject.parseObject(json);
                    if (object.getInteger("status") == 1) {
                        shopCase = object.toJavaObject(ShopCase.class);
                        adpter.setList(shopCase.getData());
                    } else {
                        showToast("无数据");
                    }
                }

            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                if (pullToRefresh != null) {
                    pullToRefresh.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        if (Type == 1) {
            getClassifyList();
        } else {
            shopCase();
        }
    }
}
