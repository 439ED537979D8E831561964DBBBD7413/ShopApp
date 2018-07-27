package com.yj.shopapp.ui.activity.shopkeeper;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.dialog.BaseV4DialogFragment;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.NewOrder;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.SNewOrderAdpter;
import com.yj.shopapp.ui.activity.wholesale.WGoodsActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.view.ClearEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import ezy.ui.layout.LoadingLayout;

/**
 * Created by LK on 2018/3/28.
 *
 * @author LK
 */

public class FragmentSearchBoxSelect extends BaseV4DialogFragment {
    @BindView(R.id.exit_tv)
    ImageView exitTv;
    @BindView(R.id.value_Et)
    ClearEditText valueEt;
    @BindView(R.id.submitTv)
    TextView submitTv;
    @BindView(R.id.my_RecyclerView)
    RecyclerView myRecyclerView;
    @BindView(R.id.title_layout)
    LinearLayout titleLayout;
    @BindView(R.id.loading)
    LoadingLayout loading;
    private int Type = 0;
    private SNewOrderAdpter adapter;
    private List<NewOrder> orderList = new ArrayList<>();

    public static FragmentSearchBoxSelect newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt("type", type);
        FragmentSearchBoxSelect fragment = new FragmentSearchBoxSelect();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void initData() {
        Type = getArguments().getInt("type");

        loading.showContent();
        myRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        myRecyclerView.addItemDecoration(new DDecoration(mActivity, getResources().getDrawable(R.drawable.recyviewdecoration3)));
        adapter = new SNewOrderAdpter(mActivity, orderList);
        myRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (orderList.size() > 0) {
                    Bundle bundle = new Bundle();
                    bundle.putString("oid", orderList.get(position).getOid());
                    CommonUtils.goActivity(mActivity, SOrderDatesActivity.class, bundle);
                   // dismiss();
                }
            }
        });
        showKeyBoard(valueEt);
    }

    @OnClick({R.id.exit_tv, R.id.submitTv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.exit_tv:
                dismiss();
                break;
            case R.id.submitTv:
                switch (Type) {
                    case 0:
                        String input = valueEt.getText().toString();
                        if (!input.equals("")) {
                            Bundle bundle = new Bundle();
                            bundle.putString("name", "商品详情");
                            bundle.putString("keyword", input);
                            CommonUtils.goActivity(mActivity, SGoodsActivity.class, bundle);
                            dismiss();
                        }
                        break;
                    case 1:
                        String input1 = valueEt.getText().toString();
                        if (!input1.equals("")) {
                            hideImm(valueEt);
                            refreshRequest(input1);
                            loading.showLoading();
                        }
                        break;
                    case 3:
                        String input2 = valueEt.getText().toString();
                        if (!input2.equals("")) {
                            hideImm(valueEt);
                            new Handler().postDelayed(() -> {
                                Bundle bundle = new Bundle();
                                bundle.putString("name", "商品详情");
                                bundle.putString("keyword", input2);
                                CommonUtils.goActivity(mActivity, WGoodsActivity.class, bundle);
                                //dismiss();
                            }, 100);

                        }
                        break;
                }
                break;
        }
    }

    public void refreshRequest(String keyword) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", "1");
        params.put("keyword", keyword);
        params.put("ostatus", "0");
        HttpHelper.getInstance().post(mActivity, Contants.PortU.MYORDER, params, new OkHttpResponseHandler<String>(mActivity) {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (loading != null) {
                    loading.showContent();
                }
                if (JsonHelper.isRequstOK(json, mActivity)) {
                    orderList.addAll(JSONArray.parseArray(json, NewOrder.class));
                    adapter.setList(orderList);
                } else {
                    loading.showEmpty();
                }

            }

            @Override
            public void onAfter() {
                super.onAfter();
            }

            @Override
            public void onBefore() {
                super.onBefore();
                orderList.clear();
            }
        });
    }
}
