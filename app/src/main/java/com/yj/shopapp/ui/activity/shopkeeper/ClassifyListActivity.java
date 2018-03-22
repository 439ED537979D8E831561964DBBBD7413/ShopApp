package com.yj.shopapp.ui.activity.shopkeeper;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.ShopDetails;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.ClassifyPagerAdpter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.NetUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class ClassifyListActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.m_tablayotu)
    TabLayout mTablayotu;
    @BindView(R.id.my_viewpager)
    ViewPager myViewpager;
    @BindView(R.id.header_imag)
    ImageView headerImag;
    private String StordId;
    private int type;
    private ClassifyPagerAdpter pagerAdpter;
    private ShopDetails shopDetails;
    private String titlename;
    private boolean isCan;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_classify_list;
    }

    @Override
    protected void initData() {
        if (getIntent().hasExtra("Store_id")) {
            StordId = getIntent().getStringExtra("Store_id");
        }
        if (getIntent().hasExtra("shop_name")) {
            titlename = getIntent().getStringExtra("shop_name");
            title.setText(titlename);
        }
        if (getIntent().hasExtra("type")) {
            type = getIntent().getIntExtra("type", -1);
        }
        if (getIntent().hasExtra("iscan")) {
            isCan = getIntent().getBooleanExtra("iscan", false);
        }
        myViewpager.setCurrentItem(isCan ? 1 : 0);
        pagerAdpter = new ClassifyPagerAdpter(getSupportFragmentManager());
        myViewpager.setAdapter(pagerAdpter);
        mTablayotu.setupWithViewPager(myViewpager);
        if (NetUtils.isNetworkConnected(mContext)) {
            getShopDetails();
        } else {
            showToastShort("无网络");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (shopDetails != null) {
            setDeta();
        } else {
            if (NetUtils.isNetworkConnected(mContext)) {
                getShopDetails();
            } else {
                showToastShort("无网络");
            }
        }
    }

    private void getShopDetails() {
        Map<String, String> params = new HashMap<>(16);
        params.put("uid", uid);
        params.put("token", token);
        params.put("shop_id", StordId);
        HttpHelper.getInstance().post(mContext, Contants.PortU.SHOPDETAILS, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onAfter() {
                super.onAfter();
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e("type" + type + json);
                JSONObject object = JSONObject.parseObject(json);
                if (object.getInteger("status") == 1) {
                    shopDetails = object.toJavaObject(ShopDetails.class);
                    setDeta();
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }
        });
    }

    private void setDeta() {
        Glide.with(mContext).load(shopDetails.getData().getImgurl()).into(headerImag);
        if (type == 1) {
            pagerAdpter.setDeta(StordId, type, shopDetails.getData().getShopdetails(), new String[]{"分类列表", "店铺详情"}, titlename);
        } else {
            pagerAdpter.setBean(shopDetails.getData());
            pagerAdpter.setDeta(StordId, type, shopDetails.getData().getShopdetails(), new String[]{"案例列表", "店铺详情", "联系我们"}, titlename);
        }
    }


}
