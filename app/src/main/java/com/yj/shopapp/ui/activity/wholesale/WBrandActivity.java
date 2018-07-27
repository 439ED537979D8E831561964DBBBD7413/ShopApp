package com.yj.shopapp.ui.activity.wholesale;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.TagGroup;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.BrandAdapter;
import com.yj.shopapp.ui.activity.adapter.WBaseSelectAdpter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.util.StatusBarUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by huang on 2016/9/11.
 */
public class WBrandActivity extends BaseActivity {
    public static final int goback = 2;
    @BindView(R.id.forewadImg)
    ImageView forewadImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;
    @BindView(R.id.title_view)
    RelativeLayout titleView;
    @BindView(R.id.value_Et)
    EditText valueEt;
    @BindView(R.id.my_RecyclerView)
    RecyclerView myRecyclerView;
    private List<TagGroup> datas = new ArrayList<>();
    private List<TagGroup> newDatas = new ArrayList<>();
    String name = "";
    private BrandAdapter adapter;
    private WBaseSelectAdpter baseSelectAdpter;
    private boolean isFilter;

    @Override
    protected void setStatusBar() {
        StatusBarUtils.from(this)
                .setActionbarView(titleView)
                .setTransparentStatusbar(true)
                .setLightStatusBar(false)
                .process();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.goodsbrand;
    }

    @Override
    protected void initData() {
        title.setText("选择品牌");
        if (NetUtils.isNetworkConnected(mContext)) {
            getBrandList();
        } else {
            showToastShort("无网络");
        }
        baseSelectAdpter = new WBaseSelectAdpter(mContext);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        myRecyclerView.addItemDecoration(new DDecoration(mContext, getResources().getDrawable(R.drawable.recyviewdecoration1dp)
        ));
        myRecyclerView.setAdapter(baseSelectAdpter);
        baseSelectAdpter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle b = new Bundle();
                String tname;
                String bid;
                if (isFilter) {
                    tname = newDatas.get(position).getName();
                    bid = newDatas.get(position).getId();
                } else {
                    tname = datas.get(position).getName();
                    bid = datas.get(position).getId();
                }
                b.putString("name", tname);
                b.putString("id", bid);
                CommonUtils.goResult(mContext, b, WBrandActivity.goback);
            }
        });
        valueEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    isFilter = false;
                    newDatas.clear();
                    baseSelectAdpter.setList(datas);
                } else {
                    isFilter = true;
                    for (TagGroup t : datas) {
                        if (t.getName().contains(s.toString())) {
                            newDatas.add(t);
                        }
                    }
                    baseSelectAdpter.setList(newDatas);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    /**
     * 获取品牌列表
     */
    private void getBrandList() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("name", name);
        HttpHelper.getInstance().post(mContext, Contants.PortA.GoodsBrand, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onAfter() {
                super.onAfter();
            }

            @Override
            public void onBefore() {
                super.onBefore();
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JsonHelper<TagGroup> jsonHelper = new JsonHelper<TagGroup>(TagGroup.class);
                    datas.addAll(jsonHelper.getDatas(json));
                    baseSelectAdpter.setList(datas);
                } else {
                    showToastShort(JsonHelper.errorMsg(json));
                }
            }
        });
    }


    @OnClick({R.id.forewadImg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forewadImg:
                finish();
                break;
//            case R.id.id_right_btu:
//                break;
//            case R.id.submitTv:
//                name = valueEt.getText().toString().trim();
//                getBrandList();
//                break;
            default:
                break;
        }
    }
}