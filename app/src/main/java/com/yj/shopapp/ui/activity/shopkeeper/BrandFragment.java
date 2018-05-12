package com.yj.shopapp.ui.activity.shopkeeper;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.BrandGroup;
import com.yj.shopapp.ubeen.Industry;
import com.yj.shopapp.ui.activity.ImgUtil.NewBaseFragment;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.Lift3Adpter;
import com.yj.shopapp.ui.activity.adapter.SBrandAdapter;
import com.yj.shopapp.ui.activity.adapter.TopRecyAdpter;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.StatusBarUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class BrandFragment extends NewBaseFragment implements AdapterView.OnItemClickListener {

    @BindView(R.id.right_recy)
    RecyclerView rightRecy;
    @BindView(R.id.lift_list)
    ListView liftList;
    @BindView(R.id.value_Et)
    TextView valueEt;
    @BindView(R.id.topRecyclerview)
    RecyclerView topRecyclerview;
    @BindView(R.id.title_layout)
    RelativeLayout titleLayout;
    Unbinder unbinder;

    private List<Industry> industryList = new ArrayList<>();
    private String Cid = "";
    private Lift3Adpter lift3Adpter;
    private List<BrandGroup> brandGroup = new ArrayList<>();
    private SBrandAdapter brandAdapter;
    private List<BrandGroup.ListBean> newBrandGroup = new ArrayList<>();
    private List<BrandGroup.ListBean> listBeans = new ArrayList<>();
    private List<BrandGroup.ListBean> tabname_brand = new ArrayList<>();
    private boolean isSereen;
    private int brandindex = 0;
    private TopRecyAdpter topRecyAdpter;
    private boolean mIsRefreshing;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_brand;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        StatusBarUtils.from(getActivity())
                .setActionbarView(titleLayout)
                .setTransparentStatusbar(true)
                .setLightStatusBar(false)
                .process();
        brandAdapter = new SBrandAdapter(mActivity, listBeans);
        rightRecy.setLayoutManager(new GridLayoutManager(mActivity, 4));
        rightRecy.setAdapter(brandAdapter);
        lift3Adpter = new Lift3Adpter(mActivity, tabname_brand);
        liftList.setAdapter(lift3Adpter);
        lift3Adpter.setDefSelect(0);
        brandAdapter.setOnItemClickListener(this);
        liftList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lift3Adpter.setDefSelect(position);
                ((LinearLayoutManager) rightRecy.getLayoutManager()).scrollToPositionWithOffset(tabname_brand.get(position).getPosition(), 0);
            }
        });
//        valueEt.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                newBrandGroup.clear();
//                if (!"".equals(s.toString())) {
//                    for (BrandGroup.ListBean bean : listBeans) {
//                        if (!bean.isSort()) {
//                            if (bean.getName().contains(s.toString())) {
//                                newBrandGroup.add(bean);
//                            }
//                        } else {
//                            newBrandGroup.add(bean);
//                        }
//                    }
//                    isSereen = true;
//                    List<BrandGroup.ListBean> list = new ArrayList<>();
//                    for (int i = 0; i < newBrandGroup.size(); i++) {
//                        if (i == newBrandGroup.size() - 1) {
//                            if (newBrandGroup.get(i).isSort()) {
//                                continue;
//                            } else {
//                                list.add(newBrandGroup.get(i));
//                                continue;
//                            }
//                        }
//                        if (newBrandGroup.get(i).isSort() && !newBrandGroup.get(i + 1).isSort()
//                                || !newBrandGroup.get(i).isSort() && newBrandGroup.get(i + 1).isSort()
//                                || !newBrandGroup.get(i).isSort() && !newBrandGroup.get(i + 1).isSort()) {
//                            list.add(newBrandGroup.get(i));
//                        }
//                    }
//                    newBrandGroup = list;
//                    brandAdapter.setList(newBrandGroup);
//                } else {
//                    isSereen = false;
//                    brandAdapter.setList(listBeans);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
        rightRecy.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                //判断是当前layoutManager是否为LinearLayoutManager
                // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    //获取第一个可见view的位置
                    int firstItemPosition = linearManager.findFirstVisibleItemPosition();
                    if (firstItemPosition == -1) return;
                    if (listBeans.size() > 0) {
                        if (firstItemPosition < listBeans.size()) {
                            if (lift3Adpter.getDefItem() != listBeans.get(firstItemPosition).getIndex()) {
                                lift3Adpter.setDefSelect(listBeans.get(firstItemPosition).getIndex());
                                liftList.setSelection(listBeans.get(firstItemPosition).getIndex());
                            }
                        }
                    }
                }
            }
        });
        topRecyAdpter = new TopRecyAdpter(mActivity);
        topRecyclerview.setLayoutManager(new GridLayoutManager(mActivity, 4));
        topRecyclerview.addItemDecoration(new DDecoration(mActivity, getResources().getDrawable(R.drawable.recyviewdecoration4)));
        topRecyclerview.setAdapter(topRecyAdpter);
        topRecyAdpter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (industryList.get(position).getId() != null) {
                    Cid = industryList.get(position).getId();
                    getBrand();
                    lift3Adpter.setDefSelect(0);
                    topRecyAdpter.setDefSelect(position);
                }
            }
        });
        rightRecy.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mIsRefreshing) {
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    @Override
    protected void initData() {
        if (isNetWork(mActivity)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getindustry();
                }
            }, 200);

        }
    }

    // 获取行业
    private void getindustry() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(mActivity, Contants.PortU.Classifylist, params, new OkHttpResponseHandler<String>(mActivity) {
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
                showToast(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
            }

            @Override
            public void onResponse(Request request, String response) {
                ShowLog.e(response);
                if (JsonHelper.isRequstOK(response, mActivity)) {
                    if (!"[]".equals(response)) {
                        industryList = JSONArray.parseArray(response, Industry.class);
                        Cid = industryList.get(0).getId();
                        while (industryList.size() < 8) {
                            industryList.add(new Industry());
                        }
                        topRecyAdpter.setList(industryList);
                        //getClasses();
                        getBrand();
                    } else {
                        showToast("无数据");
                    }
                }
            }

        });
    }

    public void getBrand() {
        brandGroup.clear();
        listBeans.clear();
        brandindex = 0;
        tabname_brand.clear();
        brandAdapter.notifyDataSetChanged();
        mIsRefreshing = true;
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("cid", Cid);
        HttpHelper.getInstance().post(mActivity, Contants.PortU.BRANDGROUP, params, new OkHttpResponseHandler<String>(mActivity) {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                mIsRefreshing = false;
            }

            @Override
            public void onAfter() {
                super.onAfter();
                mIsRefreshing = false;
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mActivity)) {
                    if (!"".equals(json)) {
                        brandGroup = JSONArray.parseArray(json, BrandGroup.class);
                        for (int i = 0; i < brandGroup.size(); i++) {
                            BrandGroup group = brandGroup.get(i);
                            tabname_brand.add(new BrandGroup.ListBean(group.getName(), brandindex));
                            listBeans.add(new BrandGroup.ListBean(group.getName(), i, true));
                            for (BrandGroup.ListBean b : group.getList()) {
                                b.setIndex(i);
                                listBeans.add(b);
                            }
                            brandindex += group.getList().size() + 1;
                        }
                        brandAdapter.notifyDataSetChanged();
                        lift3Adpter.notifyDataSetChanged();
                        addEmptyView();
                    }
                } else {
                    brandAdapter.notifyDataSetChanged();
                    lift3Adpter.notifyDataSetChanged();
                }
            }
        });
    }

    private void addEmptyView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int itemHight = rightRecy.getLayoutManager().getChildAt(1).getHeight();
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                        , rightRecy.getHeight() - itemHight);
                View view = new View(mActivity);
                view.setLayoutParams(layoutParams);
                brandAdapter.setFoootView(view);
            }
        }, 300);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (isSereen) {
            if (newBrandGroup.size() == 0) return;
            if (!newBrandGroup.get(position).isSort()) {
                Bundle bundle = new Bundle();
                bundle.putString("cid", Cid);
                bundle.putString("name", newBrandGroup.get(position).getName());
                bundle.putString("bid", newBrandGroup.get(position).getId());
                CommonUtils.goActivity(mActivity, SGoodsActivity.class, bundle);
            }
        } else {
            if (listBeans.size() == 0) return;
            if (!listBeans.get(position).isSort()) {
                Bundle bundle = new Bundle();
                bundle.putString("cid", Cid);
                bundle.putString("name", listBeans.get(position).getName());
                bundle.putString("bid", listBeans.get(position).getId());
                CommonUtils.goActivity(mActivity, SGoodsActivity.class, bundle);
            }
        }

    }

    @OnClick(R.id.value_Et)
    public void onViewClicked() {
        FragmentSearchBoxSelect.newInstance(0).show(mActivity.getFragmentManager(), "selectBox");
    }
}
