package com.yj.shopapp.ui.activity.shopkeeper;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.TagGroup;
import com.yj.shopapp.ui.activity.adapter.BrandAdapter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class Sales_brand extends BaseActivity {
    public static final int BACK_BRAND = 5;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.sales_recy)
    RecyclerView salesRecy;
    List<TagGroup> data = new ArrayList<>();
    BrandAdapter adapter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_sales_brand;
    }

    @Override
    protected void initData() {
        title.setText("促销品牌");

        adapter = new BrandAdapter(mContext);
        if (salesRecy != null) {
            salesRecy.setLayoutManager(new GridLayoutManager(mContext, 4));
            salesRecy.setAdapter(adapter);
        }
        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String bid = data.get(position).getId();
                Bundle b = new Bundle();
                b.putString("id", bid);
                CommonUtils.goResult(mContext, b, BACK_BRAND);
            }
        });
        if (NetUtils.isNetworkConnected(mContext)){
            getdata();
        }else {
            showToastShort("无网络");
        }
    }


    private void getdata() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(mContext, Contants.PortU.SalesBrandList, params, new OkHttpResponseHandler<String>(mContext) {
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
                Log.d("m_tag", json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JsonHelper<TagGroup> jsonHelper = new JsonHelper<TagGroup>(TagGroup.class);
                    data.addAll(jsonHelper.getDatas(json));
                    adapter.setList(data);
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }
        });
    }


}
