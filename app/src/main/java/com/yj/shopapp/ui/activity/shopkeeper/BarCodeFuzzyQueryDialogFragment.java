package com.yj.shopapp.ui.activity.shopkeeper;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.dialog.BaseV4DialogFragment;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.WGoodsAdapter;
import com.yj.shopapp.ui.activity.wholesale.WGoodsDetailActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.wbeen.Goods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LK on 2018/3/28.
 *
 * @author LK
 */

public class BarCodeFuzzyQueryDialogFragment extends BaseV4DialogFragment {

    @BindView(R.id.exit_tv)
    ImageView exitTv;
    @BindView(R.id.value_Et)
    EditText valueEt;
    @BindView(R.id.title_layout)
    LinearLayout titleLayout;
    @BindView(R.id.my_RecyclerView)
    RecyclerView myRecyclerView;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.submitTv)
    TextView submitTv;
    private List<Goods> goodsList = new ArrayList<>();
    private int mCurrentPage = 1;
    private WGoodsAdapter oAdapter;
    private String keyWord;

    public static BarCodeFuzzyQueryDialogFragment newInstance() {

        Bundle args = new Bundle();
        BarCodeFuzzyQueryDialogFragment fragment = new BarCodeFuzzyQueryDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.wactivity_likecheck;
    }

    @Override
    protected void initData() {
        oAdapter = new WGoodsAdapter(mActivity);
        submitTv.setVisibility(View.INVISIBLE);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        myRecyclerView.addItemDecoration(new DDecoration(mActivity, getResources().getDrawable(R.drawable.recyviewdecoration3)));
        myRecyclerView.setAdapter(oAdapter);
        oAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("itemnoid", goodsList.get(position).getNumberid());
                bundle.putString("id", goodsList.get(position).getId());
                CommonUtils.goActivity(mActivity, WGoodsDetailActivity.class, bundle);
            }
        });
        Refresh();
        valueEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {

                } else {
                    keyWord = valueEt.getText().toString();
                    goodsList.clear();
                    refreshRequest();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        showKeyBoard(valueEt);
    }

    private void Refresh() {
        smartRefreshLayout.setHeaderHeight(50);
        smartRefreshLayout.setFooterHeight(50);
        smartRefreshLayout.setEnableRefresh(false);
        smartRefreshLayout.setEnableLoadMore(false);
        //smartRefreshLayout.setOnRefreshListener(v -> refreshRequest());
        smartRefreshLayout.setOnLoadMoreListener(v -> {
            mCurrentPage++;
            refreshRequest();
        });
        smartRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        smartRefreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
    }

    @OnClick({R.id.exit_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.exit_tv:
                dismiss();
                break;
        }
    }

    public void refreshRequest() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", String.valueOf(mCurrentPage));
        params.put("type", "");
        params.put("itemname", "");
        params.put("typeid", "");//96
        params.put("industryid", "");
        params.put("keyword", keyWord);
        HttpHelper.getInstance().post(mActivity, Contants.PortA.GOODSITEMLIST, params, new OkHttpResponseHandler<String>(mActivity) {

            @Override
            public void onAfter() {
                super.onAfter();
                if (smartRefreshLayout != null) {
                    smartRefreshLayout.finishLoadMore();
                }
                if (smartRefreshLayout != null) {
                    smartRefreshLayout.setEnableLoadMore(true);
                }
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mActivity)) {
                    JsonHelper<Goods> jsonHelper = new JsonHelper<Goods>(Goods.class);
                    goodsList.addAll(jsonHelper.getDatas(json));
                    oAdapter.setList(goodsList);
                } else if (JsonHelper.getRequstOK(json) == 6) {
                    if (goodsList.size() > 0) {
                        if (smartRefreshLayout != null) {
                            smartRefreshLayout.finishLoadMoreWithNoMoreData();
                        }
                    }
                } else {
                    Toast.makeText(mActivity, Contants.NetStatus.NETLOADERROR, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                Toast.makeText(mActivity, Contants.NetStatus.NETDISABLEORNETWORKDISABLE, Toast.LENGTH_SHORT).show();
                goodsList.clear();
            }
        });

    }

}
