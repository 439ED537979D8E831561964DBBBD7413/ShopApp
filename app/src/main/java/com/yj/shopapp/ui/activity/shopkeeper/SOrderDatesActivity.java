package com.yj.shopapp.ui.activity.shopkeeper;

import android.annotation.SuppressLint;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.OrderDatesBean;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.OrderDatasAdapte;
import com.yj.shopapp.ui.activity.adapter.OrderDatasAdapte1;
import com.yj.shopapp.ui.activity.adapter.ViewPageAdpter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.DateUtils;
import com.yj.shopapp.util.JsonHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by LK on 2017/12/20.
 *
 * @author LK
 */

public class SOrderDatesActivity extends BaseActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.appbarlayout)
    AppBarLayout appbarlayout;
    @BindView(R.id.order_viewpager)
    ViewPager orderViewpager;
    @BindView(R.id.recycler_view_2)
    RecyclerView recyclerView2;
    @BindView(R.id.orderid)
    TextView orderid;
    @BindView(R.id.driver)
    TextView driver;
    @BindView(R.id.order_time)
    TextView orderTime;
    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.total_package)
    TextView totalPackage;
    @BindView(R.id.discount_money)
    TextView discountMoney;
    @BindView(R.id.meet_money)
    TextView meetMoney;
    @BindView(R.id.paid_money)
    TextView paidMoney;
    @BindView(R.id.title)
    TextView title;
    private OrderDatasAdapte datasAdapte;
    private ViewPageAdpter pageAdpter;
    private OrderDatasAdapte1 adapte1;
    private List<String> names = new ArrayList<>();
    private List<String> types = new ArrayList<>();
    private String oid;
    private String page = "0";
    private OrderDatesBean mData;
    @Override
    protected int getLayoutId() {
        return R.layout.orderdateslayout;
    }

    @Override
    protected void initData() {
        title.setText("订单详情");
        if (getIntent().hasExtra("oid")) {
            oid = getIntent().getStringExtra("oid");
            orderid.setText("订单号：" + oid);
        }
        datasAdapte = new OrderDatasAdapte(mContext);
        adapte1 = new OrderDatasAdapte1(mContext);
        pageAdpter = new ViewPageAdpter(getSupportFragmentManager());
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        recyclerView.setAdapter(datasAdapte);
        orderViewpager.setAdapter(pageAdpter);
        tabLayout.setupWithViewPager(orderViewpager);
        refreshRequest();
    }

    private void refreshRequest() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("p", page);
        params.put("oid", oid);
        HttpHelper.getInstance().post(mContext, Contants.PortU.ORDERDETAILS, params, new OkHttpResponseHandler<String>(mContext) {
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
                    mData = JSONObject.parseObject(json, OrderDatesBean.class);
                    setData();
                } else {
                    showToastShort(JsonHelper.errorMsg(json));
                }
            }
        });
    }

    @SuppressLint("DefaultLocale")
    private void setData() {
        //totalPackage.setText(mData.getCoupon());
        status.setText(Contants.OrderStadus[Integer.parseInt(mData.getStatus())]);
        orderTime.setText(DateUtils.timet(mData.getAddtime()));
        meetMoney.setText(String.format("应付：￥%s", mData.getMoney()));
        DecimalFormat df = new DecimalFormat("#.00");
        paidMoney.setText(String.format("实付：￥%s", df.format(Double.parseDouble(mData.getMoney()) - (double) mData.getCoupon())));
        if (mData.getCoupon() != 0) {
            discountMoney.setText(String.format("优惠：￥-%d", mData.getCoupon()));
        } else {
            discountMoney.setText("优惠金额:0");
        }
        totalPackage.setText("数量：" + mData.getAllnum());
        datasAdapte.setList(mData.getData());
        adapte1.setList(mData.getCouponlist());
        pageAdpter.setItemlist(mData.getItemlist());
        initdata();
    }

    public void initdata() {
        for (OrderDatesBean.DataBean bean : mData.getData()) {
            names.add(bean.getName());
            types.add(bean.getId());
        }
        pageAdpter.setNames(names,types);
    }


}
