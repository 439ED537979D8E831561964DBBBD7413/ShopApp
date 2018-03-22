package com.yj.shopapp.ui.activity.shopkeeper;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.AdvMap;
import com.yj.shopapp.ubeen.Store;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.StoreAdpter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.view.EasyBanner.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class AdditionalservicesActivity extends BaseActivity implements OnBannerListener, AdapterView.OnItemClickListener, View.OnClickListener {

    @BindView(R.id.forewadImg)
    ImageView forewadImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.bannerView)
    Banner bannerView;
    @BindView(R.id.my_RecyclerView)
    RecyclerView myRecyclerView;
    @BindView(R.id.compare)
    TextView compares;
    @BindView(R.id.The_door)
    TextView TheDoor;
    private AdvMap advMap;
    private Store store;
    private List<String> imags = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private StoreAdpter adpter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_additionalservices;
    }

    @Override
    protected void initData() {
        title.setText("城市服务");
        bannerView.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        bannerView.setIndicatorGravity(BannerConfig.CENTER);
        bannerView.setImageLoader(new GlideImageLoader());
        bannerView.setOnBannerListener(this);
        bannerView.setDelayTime(5000);
        adpter = new StoreAdpter(mContext);
        myRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        myRecyclerView.setAdapter(adpter);
        adpter.setOnItemClickListener(this);
        compares.setOnClickListener(this);
        TheDoor.setOnClickListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (NetUtils.isNetworkConnected(mContext)) {
            getShopClass();
            getBannerImag();
        } else {
            showToastShort("无网络");
        }
        bannerView.startAutoPlay();
    }

    private void getBannerImag() {
        imags.clear();
        titles.clear();
        Map<String, String> params = new HashMap<>(16);
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(mContext, Contants.PortU.SHOP_ADVMAP, params, new OkHttpResponseHandler<String>(mContext) {
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
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JSONObject object = JSONObject.parseObject(json);
                    if (object.getInteger("status") == 1) {
                        advMap = object.toJavaObject(AdvMap.class);
                        for (AdvMap.DataBean bean : advMap.getData()) {
                            imags.add(bean.getImgurl());
                            titles.add(bean.getTitle());
                        }
                        bannerView.setBannerTitles(titles);
                        bannerView.setImages(imags);
                        bannerView.start();
                    } else {
                        showToastShort(object.getString("data"));
                    }
                }

            }
        });
    }

    private void getShopClass() {
        Map<String, String> params = new HashMap<>(16);
        params.put("uid", uid);
        params.put("token", token);
        HttpHelper.getInstance().post(mContext, Contants.PortU.SHOPLIST, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    JSONObject object = JSONObject.parseObject(json);
                    if (object.getInteger("status") == 1) {
                        store = object.toJavaObject(Store.class);
                        adpter.setList(store.getData());
                    } else {
                        showToastShort(object.getString("data"));
                    }
                }
            }

            @Override
            public void onAfter() {
                super.onAfter();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        bannerView.stopAutoPlay();
    }

    @Override
    public void OnBannerClick(int position) {
        int type = Integer.parseInt(advMap.getData().get(position).getType());
        switch (type) {
            case 1:
                //跳转至店铺
                Bundle bundle = new Bundle();
                bundle.putInt("type", Integer.parseInt(advMap.getData().get(position).getType()));
                bundle.putString("Store_id", advMap.getData().get(position).getShop_id());
                CommonUtils.goActivity(mContext, ClassifyListActivity.class, bundle);
                break;
            case 2:
                //跳转至商品
                Bundle bundle2 = new Bundle();
                bundle2.putString("shop_id", advMap.getData().get(position).getShop_id());
                CommonUtils.goActivity(mContext, CommodityDetails.class, bundle2);
                break;
            default:
                //跳转至案例
                Bundle bundle3 = new Bundle();
                bundle3.putInt("type", Integer.parseInt(advMap.getData().get(position).getType()));
                bundle3.putString("Store_id", advMap.getData().get(position).getShop_id());
                CommonUtils.goActivity(mContext, ClassifyListActivity.class, bundle3);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle bundle = new Bundle();
        bundle.putString("Store_id", store.getData().get(position).getId());
        bundle.putString("shop_name", store.getData().get(position).getShopname());
        bundle.putInt("type", store.getData().get(position).getType());
        CommonUtils.goActivity(mContext, ClassifyListActivity.class, bundle);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.The_door:
                CommonUtils.goActivity(mContext, ValueAddedService.class, null);
                break;
            case R.id.compare:
                break;
        }
    }


}
