package com.yj.shopapp.ui.activity.wholesale;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.WBaseSelectAdpter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.PreferenceUtils;
import com.yj.shopapp.util.StatusBarUtils;
import com.yj.shopapp.util.StringHelper;
import com.yj.shopapp.wbeen.Itemtype;
import com.yj.shopapp.wbeen.Itemunit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class WBaseSelectActivity extends BaseActivity {
    public static final int ITEMTYPE = 99;
    public static final int ITEMUNIT = 89;
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
    private WBaseSelectAdpter adpter;
    private int type = -1;
    private List<Itemtype> itemtypeList = new ArrayList<>(); //商品分类
    private List<Itemtype> newItemtypeList = new ArrayList<>(); //商品分类
    private List<Itemunit> itemunitList = new ArrayList<>();
    private List<Itemunit> newItemunitList = new ArrayList<>();
    private boolean isFilter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wbase_select;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtils.from(this)
                .setActionbarView(titleView)
                .setTransparentStatusbar(true)
                .setLightStatusBar(false)
                .process();
    }

    @Override
    protected void initData() {
        if (getIntent().hasExtra("title_name")) {
            title.setText(getIntent().getStringExtra("title_name"));
        }
        if (getIntent().hasExtra("type")) {
            type = getIntent().getIntExtra("type", 1);
        }
        valueEt.setHint(type == 0 ? "输入分类名称" : "输入单位名称");
        if (type == 0) {
            reportType();
        } else {
            reportUnits();
        }
        adpter = new WBaseSelectAdpter(mContext);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        myRecyclerView.addItemDecoration(new DDecoration(mContext, getResources().getDrawable(R.drawable.recyviewdecoration1dp)));
        myRecyclerView.setAdapter(adpter);
        adpter.setOnItemClickListener((parent, view, position, id) -> {
            Bundle bundle = new Bundle();
            if (type == 0) {
                if (!isFilter) {
                    bundle.putParcelable("item", itemtypeList.get(position));
                } else {
                    bundle.putParcelable("item", newItemtypeList.get(position));
                }
                CommonUtils.goResult(mContext, bundle, ITEMTYPE);
            } else {
                if (!isFilter) {
                    bundle.putParcelable("item", itemunitList.get(position));
                } else {
                    bundle.putParcelable("item", newItemunitList.get(position));
                }
                CommonUtils.goResult(mContext, bundle, ITEMUNIT);
            }
        });
        valueEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("")) {
                    isFilter = true;
                    if (type == 0) {
                        for (Itemtype i : itemtypeList) {
                            if (i.getName().contains(s.toString())) {
                                newItemtypeList.add(i);
                            }
                        }
                        adpter.setList(newItemtypeList);
                    } else {
                        for (Itemunit i : itemunitList) {
                            if (i.getName().contains(s.toString())) {
                                newItemunitList.add(i);
                            }
                        }
                        adpter.setList(newItemunitList);
                    }
                } else {
                    isFilter = false;
                    if (type == 0) {
                        newItemtypeList.clear();
                        adpter.setList(itemtypeList);
                    } else {
                        newItemunitList.clear();
                        adpter.setList(itemunitList);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void reportType() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);

        HttpHelper.getInstance().post(mContext, Contants.PortA.ITEMTYPE, params, new OkHttpResponseHandler<String>(mContext) {

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
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mContext)) {
//                    JsonHelper<Itemtype> jsonHelper = new JsonHelper<Itemtype>(Itemtype.class);
//                    itemtypeList = jsonHelper.getDatas(json);
                    itemtypeList=JSONArray.parseArray(json,Itemtype.class);
                    adpter.setList(itemtypeList);
                    PreferenceUtils.setPrefString(mContext, Contants.Preference.ITEMTYPE, json);
                } else {
                    if (!StringHelper.isEmpty(PreferenceUtils.getPrefString(mContext, Contants.Preference.ITEMTYPE, ""))) {
                        json = PreferenceUtils.getPrefString(mContext, Contants.Preference.ITEMTYPE, "");
//                        JsonHelper<Itemtype> jsonHelper = new JsonHelper<Itemtype>(Itemtype.class);
//                        itemtypeList = jsonHelper.getDatas(json);
                        itemtypeList=JSONArray.parseArray(json,Itemtype.class);
                        adpter.setList(itemtypeList);
                    }
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                if (!StringHelper.isEmpty(PreferenceUtils.getPrefString(mContext, Contants.Preference.ITEMTYPE, ""))) {
                    String sjson = PreferenceUtils.getPrefString(mContext, Contants.Preference.ITEMTYPE, "");
//                    JsonHelper<Itemtype> jsonHelper = new JsonHelper<Itemtype>(Itemtype.class);
//                    itemtypeList = jsonHelper.getDatas(sjson);
                    itemtypeList=JSONArray.parseArray(sjson,Itemtype.class);
                    adpter.setList(itemtypeList);
                }
            }
        });
    }

    public void reportUnits() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(mContext, Contants.PortA.ITEMUNIT, params, new OkHttpResponseHandler<String>(mContext) {

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
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JsonHelper<Itemunit> jsonHelper = new JsonHelper<Itemunit>(Itemunit.class);
                    itemunitList = jsonHelper.getDatas(json);
                    adpter.setList(itemunitList);
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
            }
        });
    }
}
