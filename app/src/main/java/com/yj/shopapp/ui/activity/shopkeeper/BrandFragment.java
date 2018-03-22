package com.yj.shopapp.ui.activity.shopkeeper;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.BrandGroup;
import com.yj.shopapp.ubeen.Industry;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.Lift3Adpter;
import com.yj.shopapp.ui.activity.adapter.SBrandAdapter;
import com.yj.shopapp.ui.activity.base.BaseFragment;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.view.headfootrecycleview.RecycleViewEmpty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class BrandFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.right_recy)
    RecycleViewEmpty rightRecy;
    @BindView(R.id.lift_list)
    ListView liftList;
    @BindView(R.id.value_Et)
    EditText valueEt;
    @BindView(R.id.empty_tv)
    TextView emptyTv;
    @BindView(R.id.Cempty_view)
    NestedScrollView CemptyView;
    private List<Industry> industryList = new ArrayList<>();
    private String Cid = "";
    private Lift3Adpter lift3Adpter;
    private List<BrandGroup> brandGroup = new ArrayList<>();
    private SBrandAdapter brandAdapter;
    private List<BrandGroup.ListBean> newBrandGroup = new ArrayList<>();
    private List<BrandGroup.ListBean> listBeans = new ArrayList<>();
    private boolean isSereen;
    private String mGid = "-1";

    @Override
    public void init(Bundle savedInstanceState) {
        title.setText("品牌");
        emptyTv.setText("此行业暂无品牌");
        brandAdapter = new SBrandAdapter(mActivity);
        rightRecy.setLayoutManager(new GridLayoutManager(mActivity, 3));
        rightRecy.setAdapter(brandAdapter);
        lift3Adpter = new Lift3Adpter(mActivity);
        liftList.setAdapter(lift3Adpter);
        lift3Adpter.setDefSelect(0);
        brandAdapter.setOnItemClickListener(this);
        liftList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cid = industryList.get(position).getId();
                getBrand();
                lift3Adpter.setDefSelect(position);
            }
        });
        valueEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newBrandGroup.clear();
                if (!"".equals(s.toString())) {
                    for (BrandGroup.ListBean bean : listBeans) {
                        if (!bean.isSort()) {
                            if (bean.getName().contains(s.toString())) {
                                newBrandGroup.add(bean);
                            }
                        } else {
                            newBrandGroup.add(bean);
                        }
                    }
                    isSereen = true;
                    List<BrandGroup.ListBean> list = new ArrayList<>();
                    for (int i = 0; i < newBrandGroup.size(); i++) {
                        if (i == newBrandGroup.size() - 1) {
                            if (newBrandGroup.get(i).isSort()) {
                                continue;
                            } else {
                                list.add(newBrandGroup.get(i));
                                continue;
                            }
                        }
                        if (newBrandGroup.get(i).isSort() && !newBrandGroup.get(i + 1).isSort()
                                || !newBrandGroup.get(i).isSort() && newBrandGroup.get(i + 1).isSort()
                                || !newBrandGroup.get(i).isSort() && !newBrandGroup.get(i + 1).isSort()) {
                            list.add(newBrandGroup.get(i));
                        }
                    }
                    newBrandGroup = list;
                    brandAdapter.setList(newBrandGroup);
                } else {
                    isSereen = false;
                    brandAdapter.setList(listBeans);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if (NetUtils.isNetworkConnected(mActivity)) {
            rightRecy.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getindustry();
                }
            }, 200);

        } else {
            showToastShort("无网络");
        }
    }


    @Override
    public int getLayoutID() {
        return R.layout.fragment_brand;
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
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
            }

            @Override
            public void onResponse(Request request, String response) {
                ShowLog.e(response);
                if (JsonHelper.isRequstOK(response, mActivity)) {
                    if (!"[]".equals(response)) {
                        industryList = JSONArray.parseArray(response, Industry.class);
                        Cid = industryList.get(0).getId();
                        lift3Adpter.setList(industryList);
                        //getClasses();
                        getBrand();
                    } else {
                        showToastShort("无数据");
                    }
                }
            }

        });
    }


    public void getBrand() {
        brandGroup.clear();
        listBeans.clear();
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("cid", Cid);
        HttpHelper.getInstance().post(mActivity, Contants.PortU.BRANDGROUP, params, new OkHttpResponseHandler<String>(mActivity) {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onAfter() {
                super.onAfter();
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
                            listBeans.add(new BrandGroup.ListBean(group.getName(), true));
                            if (group.getList().size() > 6) {
                                listBeans.addAll(group.getList().subList(0, 6));
                                listBeans.add(new BrandGroup.ListBean("点击展开", group.getList().get(0).getGid(), true, listBeans.size() - 7));

                            } else {
                                listBeans.addAll(group.getList());
                            }
                        }
                        brandAdapter.setList(listBeans);
                        rightRecy.scrollToPosition(0);
                    }
                } else {
                    rightRecy.setEmptyView(CemptyView);
                    brandAdapter.notifyDataSetChanged();
                }
            }
        });
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
            if (listBeans.get(position).isFoot()) {
                rightRecy.scrollToPosition(listBeans.get(position).getPosition());
                if (mGid.equals(listBeans.get(position).getGid())) {
                    showAll("-1");
                    mGid = "-1";
                } else {
                    showAll(listBeans.get(position).getGid());
                    mGid = listBeans.get(position).getGid();
                }

                return;
            }
            if (!listBeans.get(position).isSort()) {
                Bundle bundle = new Bundle();
                bundle.putString("cid", Cid);
                bundle.putString("name", listBeans.get(position).getName());
                bundle.putString("bid", listBeans.get(position).getId());
                CommonUtils.goActivity(mActivity, SGoodsActivity.class, bundle);
            }
        }

    }

    private void showAll(String gid) {
        listBeans.clear();
        brandAdapter.setImagRotate(Integer.parseInt(gid));
        for (int i = 0; i < brandGroup.size(); i++) {
            BrandGroup group = brandGroup.get(i);
            listBeans.add(new BrandGroup.ListBean(group.getName(), true));
            if (group.getList().get(0).getGid().equals(gid)) {
                listBeans.addAll(group.getList());
                listBeans.add(new BrandGroup.ListBean("点击收起", group.getList().get(0).getGid(), true, listBeans.size() - (group.getList().size() + 1)));
            } else {
                if (group.getList().size() > 6) {
                    listBeans.addAll(group.getList().subList(0, 6));
                    listBeans.add(new BrandGroup.ListBean("点击展开", group.getList().get(0).getGid(), true, listBeans.size() - 7));
                } else {
                    listBeans.addAll(group.getList());
                }
            }
        }
        brandAdapter.setList(listBeans);
    }

}
