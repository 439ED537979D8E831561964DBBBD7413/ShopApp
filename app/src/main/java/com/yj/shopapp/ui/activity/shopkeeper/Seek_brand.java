package com.yj.shopapp.ui.activity.shopkeeper;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.TagGroup;
import com.yj.shopapp.ui.activity.adapter.SBrand2Adapter;
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

/**
 * Created by Administrator on 2017/8/12 0012.
 */

public class Seek_brand extends BaseActivity {
    public static final int GOBACKONE = 3;
    public static final int GOBACKTWO = 4;
    @BindView(R.id.value_Et)
    EditText valueEt;
    @BindView(R.id.submitTv)
    TextView submitTv;
    @BindView(R.id.brand_recy)
    RecyclerView brandRecy;
    private List<TagGroup> groups = new ArrayList<TagGroup>();
    private String typeid = "";
    private String cid = "";
    private SBrand2Adapter adpter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_seek_b;
    }

    @Override
    protected void initData() {
        if (getIntent().hasExtra("typeid")) {
            typeid = getIntent().getStringExtra("typeid");
        }
        if (getIntent().hasExtra("cid")) {
            cid = getIntent().getStringExtra("cid");
        }
        adpter = new SBrand2Adapter(mContext);
        if (brandRecy != null) {
            brandRecy.setLayoutManager(new GridLayoutManager(mContext, 4));
            brandRecy.setAdapter(adpter);
        }
        adpter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String bId = groups.get(position).getId();
                Bundle b = new Bundle();
                b.putString("bid", bId);
                CommonUtils.goResult(mContext, b, GOBACKONE);
            }
        });
        if (NetUtils.isNetworkConnected(mContext)) {
            getScreen();
        } else {
            showToastShort("无网络");
        }
    }


    /**
     * 获取group 名字
     */
    private void getScreen() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("industryid", typeid);
        params.put("cid", cid);
        HttpHelper.getInstance().post(mContext, Contants.PortU.BrandList, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onBefore() {
                super.onBefore();
            }

            @Override
            public void onAfter() {
                super.onAfter();
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                Log.e("request", json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JsonHelper<TagGroup> jsonHelper = new JsonHelper<TagGroup>(TagGroup.class);
                    groups.addAll(jsonHelper.getDatas(json));
                    adpter.setList(groups);
                } else {
                    showToastShort(JsonHelper.errorMsg(json));
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);

            }
        });
    }

    @OnClick(R.id.submitTv)
    public void onClick() {
        String content = valueEt.getText().toString().trim();
        Bundle b = new Bundle();
        b.putString("content", content);
        CommonUtils.goResult(mContext, b, GOBACKTWO);
    }

    @OnClick(R.id.exit_tv)
    public void onViewClicked() {
        finish();
    }


}
