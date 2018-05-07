package com.yj.shopapp.ui.activity.shopkeeper;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.GoodData;
import com.yj.shopapp.ubeen.ServiceOrder;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.GoodsDetailSpgerAdpter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.CommonUtils;
import com.yj.shopapp.util.FloatingLayout;
import com.yj.shopapp.util.NetUtils;
import com.yj.shopapp.util.PreferenceUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.bgabanner.BGABanner;

public class CommodityDetails extends BaseActivity {
    @BindView(R.id.my_banner)
    BGABanner myBanner;
    @BindView(R.id.shopname)
    TextView shopname;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.myviewpager)
    ViewPager myviewpager;
    @BindView(R.id.shophintmessage)
    TextView shophintmessage;
    private String[] titlename = {"商品", "详情"};
    private String shopId;
    private GoodData data;
    private GoodsDetailSpgerAdpter pageradpter;
    private final int REQUEST_CODE = 0x1001;
    private String addresId = "";
    private String StoreId = "";
    private String num;
    private int specs = -1;
    private String remark = "";
    private View RootView;
    private MaterialDialog dialog;
    private String shopName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_commodity_details;
    }

    @Override
    protected void initData() {
        if (getIntent().hasExtra("shop_id")) {
            shopId = getIntent().getStringExtra("shop_id");
        }
        if (getIntent().hasExtra("Store_id")) {
            StoreId = getIntent().getStringExtra("Store_id");
        }
        if (getIntent().hasExtra("shopname")) {
            shopName = getIntent().getStringExtra("shopname");
        }
        addresId = PreferenceUtils.getPrefString(mContext, "addressId", "");
        pageradpter = new GoodsDetailSpgerAdpter(getSupportFragmentManager(), titlename);
        tabLayout.setupWithViewPager(myviewpager);
        ininBanner();
    }

    private void ininBanner() {
        myBanner.setAdapter(new BGABanner.Adapter<ImageView, String>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, @Nullable String model, int position) {
                Glide.with(mContext).load(model).into(itemView);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        myBanner.startAutoPlay();
        if (NetUtils.isNetworkConnected(mContext)) {
            getRequestdata();
        } else {
            showToastShort("无网络");
        }
    }

    @OnClick({R.id.call_phone, R.id.my_store, R.id.buy_shop, R.id.exit_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.call_phone:
                if (Build.VERSION.SDK_INT >= 23) {
                    //判断有没有拨打电话权限
                    if (PermissionChecker.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        //请求拨打电话权限
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE);

                    } else {
                        callPhone();
                    }

                } else {
                    callPhone();
                }
                break;
            case R.id.my_store:
                Bundle bundle = new Bundle();
                bundle.putInt("type", 1);
                bundle.putString("Store_id", data.getData().getShop_id());
                bundle.putString("shop_name", shopName);
                CommonUtils.goActivity(mContext, ClassifyListActivity.class, bundle);
                break;
            case R.id.buy_shop:
                if (!"".equals(addresId)) {
                    dialog = new MaterialDialog.Builder(mContext)
                            .customView(R.layout.dialog_contextview, false)
                            .canceledOnTouchOutside(false)
                            .show();
                    setContentView();
                } else {
                    new MaterialDialog.Builder(mContext)
                            .title("提示")
                            .content("暂无收货地址")
                            .positiveText("去添加")
                            .negativeText("取消")
                            .canceledOnTouchOutside(false)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
                break;
            case R.id.exit_tv:
                finish();
                break;

            default:
                break;
        }
    }

    private void setContentView() {
        RootView = dialog.getCustomView();
        List<String> specslist = data.getData().getSpecs();
        TextView title = (TextView) RootView.findViewById(R.id.title_tv);
        title.setText(data.getData().getName());
        FloatingLayout floatingLayout = (FloatingLayout) RootView.findViewById(R.id.floatinglayout);
        final TextView price_tv = (TextView) RootView.findViewById(R.id.itemprice);
        price_tv.setText(data.getData().getSales_price());
        final TextView itemnumber = (TextView) RootView.findViewById(R.id.itemnumber);
        itemnumber.setText("1");
        final TextView plus = (TextView) RootView.findViewById(R.id.plus);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = Integer.parseInt(itemnumber.getText().toString());
                number++;
                itemnumber.setText(number + "");
                price_tv.setText((Double.parseDouble(data.getData().getSales_price()) * number) + "");
            }
        });
        TextView reduce = (TextView) RootView.findViewById(R.id.reduce);
        reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = Integer.parseInt(itemnumber.getText().toString());
                if (number == 1) {
                    showToastShort("最少购买一件");
                } else {
                    number--;
                    price_tv.setText((Double.parseDouble(data.getData().getSales_price()) * number) + "");
                }
                itemnumber.setText(number + "");
            }
        });
        final MaterialEditText leave = (MaterialEditText) RootView.findViewById(R.id.leave);
        TextView submit = (TextView) RootView.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = itemnumber.getText().toString().trim();
                remark = leave.getText().toString().trim();
                doShopOrder();
                dialog.dismiss();
            }
        });
        RootView.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        floatingLayout.setTags(specslist);
        floatingLayout.setOnItemCheckedChangeListener(new FloatingLayout.OnItemCheckedChangeListener() {
            @Override
            public void onItemCheckedChange(int position) {
                specs = position;
            }

        });
        floatingLayout.setDefaultView();
    }

    private void getRequestdata() {
        Map<String, String> params = new HashMap<>(16);
        params.put("uid", uid);
        params.put("token", token);
        params.put("goods_id", shopId);
        HttpHelper.getInstance().post(mContext, Contants.PortU.SHOP_GOODSDATAILS, params, new OkHttpResponseHandler<String>(mContext) {
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
                JSONObject object = JSONObject.parseObject(json);
                if (object.getInteger("status") == 1) {
                    data = object.toJavaObject(GoodData.class);
                    pageradpter.setBean(data.getData());
                    myviewpager.setAdapter(pageradpter);
                    shophintmessage.setText(data.getData().getMessage());
                    if ("".equals(data.getData().getSales_price())) {
                        shopname.setText(Html.fromHtml("<font size=16>" + data.getData().getName() + "</font>" + "&emsp" + "&emsp" + "<s>" + data.getData().getPrice() + "&emsp" + "&emsp" + "</s>" + "￥" + "<font color=red >" + "<b>" + data.getData().getSales_price() + "</b>" + "</font>"));
                    } else {
                        shopname.setText(Html.fromHtml("<font size=16>" + data.getData().getName() + "</font>" + "&emsp" + "<font color=red>" + "<b>" + "￥" + data.getData().getPrice() + "</b>" + "</font>"));
                    }
                    if (myBanner!=null){
                        myBanner.setData(data.getData().getImgurl(),new ArrayList<String>());
                    }
                } else {
                    showToastShort(object.getString("info"));
                }

            }
        });
    }

    /**
     * 购买商品
     */
    private void doShopOrder() {
        Map<String, String> params = new HashMap<>(16);
        params.put("uid", uid);
        params.put("token", token);
        params.put("addressid", addresId);
        params.put("shop_id", StoreId);
        params.put("class_id", data.getData().getClass_id());
        params.put("goods_id", shopId);
        params.put("num", num + "");
        params.put("specs", data.getData().getSpecs().get(specs));
        params.put("unit", data.getData().getUnit());
        params.put("remark", remark);
        HttpHelper.getInstance().post(mContext, Contants.PortU.DO_SHOP_ORDER, params, new OkHttpResponseHandler<String>(mContext) {
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
                JSONObject object = JSONObject.parseObject(json);
                if (object.getInteger("status") == 1) {
                    String data = object.getString("data");
                    ServiceOrder order = JSONObject.parseObject(data, ServiceOrder.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("data", order);
                    CommonUtils.goActivity(mContext, AddedServiceData.class, bundle);
                } else {
                    showToastShort("提交失败");
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE && PermissionChecker.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            showToastShort("授权成功");
            callPhone();
        } else {
            showToastShort("授权失败");
        }
    }

    private void callPhone() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "10010"));
            startActivity(intent);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
                //已经禁止提示了
                showToastShort("您已禁止该权限，需要重新开启");
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE);

            }

        }

    }


}
