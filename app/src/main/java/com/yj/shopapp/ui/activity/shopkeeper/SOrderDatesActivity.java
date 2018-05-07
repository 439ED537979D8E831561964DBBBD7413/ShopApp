package com.yj.shopapp.ui.activity.shopkeeper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.OrderDatesBean;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.OrderDatasAdapte;
import com.yj.shopapp.ui.activity.adapter.OrderDatasAdapte1;
import com.yj.shopapp.ui.activity.adapter.OrderFragmentAdapte;
import com.yj.shopapp.ui.activity.adapter.ViewPageAdpter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.DateUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.StatusBarManager;

import java.lang.reflect.Field;
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


    @BindView(R.id.content_tv)
    TextView contentTv;
    @BindView(R.id.right_tv)
    TextView rightTv;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.orderid)
    TextView orderid;
    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.driver)
    TextView driver;
    @BindView(R.id.order_time)
    TextView orderTime;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.total_package)
    TextView totalPackage;
    @BindView(R.id.discount_money)
    TextView discountMoney;
    @BindView(R.id.paid_money)
    TextView paidMoney;
    @BindView(R.id.Contact_driver)
    TextView ContactDriver;
    @BindView(R.id.driverdatails)
    RelativeLayout driverdatails;
    @BindView(R.id.shopimag)
    ImageView shopimag;
    @BindView(R.id.bgoodsname)
    TextView bgoodsname;
    @BindView(R.id.bgoodsspesc)
    TextView bgoodsspesc;
    @BindView(R.id.bgoodsprice)
    TextView bgoodsprice;
    @BindView(R.id.buGoods)
    RelativeLayout buGoods;
    @BindView(R.id.tab_viewpager)
    LinearLayout tabViewpager;
    @BindView(R.id.my_RecyclerView)
    RecyclerView myRecyclerView;
    private OrderDatasAdapte datasAdapte;
    private ViewPageAdpter pageAdpter;
    private OrderDatasAdapte1 adapte1;
    private List<String> names = new ArrayList<>();
    private List<String> types = new ArrayList<>();
    private String oid;
    private String page = "0";
    private OrderDatesBean mData;
    private final int REQUEST_CODEC = 0x1001;
    private static final int REQUEST_CODE = 1;
    private int currItem = 0;
    private OrderFragmentAdapte adapte;

    @Override
    protected int getLayoutId() {
        return R.layout.orderdateslayout;
    }

    @Override
    protected void initData() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        contentTv.setText("订单详情");
        if (getIntent().hasExtra("oid")) {
            oid = getIntent().getStringExtra("oid");
            orderid.setText("订单号：" + oid);
        }
        //datasAdapte = new OrderDatasAdapte(mContext);
        //adapte1 = new OrderDatasAdapte1(mContext);
        //pageAdpter = new ViewPageAdpter(getSupportFragmentManager());
        adapte = new OrderFragmentAdapte(mContext);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        myRecyclerView.addItemDecoration(new DDecoration(mContext, getResources().getDrawable(R.drawable.recyviewdecoration3)));
        myRecyclerView.setAdapter(adapte);
        if (isNetWork(mContext)) {
            refreshRequest();
        }
        myRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                //判断是当前layoutManager是否为LinearLayoutManager
                // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    //获取第一个可见view的位置
                    int firstItemPosition = linearManager.findFirstVisibleItemPosition();
                    if (firstItemPosition == -1) return;
                    if (tabLayout == null) return;
                    if (firstItemPosition < mData.getItemlist().size()) {

                        tabLayout.setFocusable(true);
                        tabLayout.setFocusableInTouchMode(true);
                        if (!tabLayout.getTabAt(mData.getItemlist().get(firstItemPosition).getIndex()).isSelected()) {
                            tabLayout.getTabAt(mData.getItemlist().get(firstItemPosition).getIndex()).select();
                        }

                    }

                }
            }
        });

    }

    @Override
    protected void setStatusBar() {
       // StatusBarUtil.setColor(this, getResources().getColor(R.color.white), 0);
        StatusBarManager.getInstance().setStatusBar(getWindow(), getResources().getColor(R.color.white));
        StatusBarManager.getInstance().setStatusBarTextColor(getWindow(), true);
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

        try {
            status.setText(Contants.OrderStadus[Integer.parseInt(mData.getStatus())]);
            orderTime.setText(DateUtils.timet(mData.getAddtime()));
            DecimalFormat df = new DecimalFormat("#.00");
            paidMoney.setText(String.format("应付金额：￥%s", df.format(Double.parseDouble(mData.getMoney()) - (double) Integer.parseInt(mData.getCoupon()))));
            if (mData.getCoupon().equals("0")) {
                discountMoney.setText(String.format("优惠：￥%s", mData.getCoupon()));
            } else {
                discountMoney.setText("优惠:0");
            }

            switch (mData.getStatus()) {
                case "1":
                    driverdatails.setVisibility(View.GONE);
                    break;
                case "4":
                    if (mData.getDriver_info() == null) {
                        driverdatails.setVisibility(View.GONE);
                    } else {
                        driver.setText(String.format("司机：%s", mData.getDriver_info().getName()));
                        driverdatails.setVisibility(View.VISIBLE);
                    }
                    ContactDriver.setVisibility(View.VISIBLE);
                    break;
                case "3":
                    if (mData.getDriver_info() == null) {
                        driverdatails.setVisibility(View.GONE);
                    } else {
                        driver.setText(String.format("司机：%s", mData.getDriver_info().getName()));
                        driverdatails.setVisibility(View.VISIBLE);
                    }
                    ContactDriver.setVisibility(View.GONE);
                    break;
            }
            totalPackage.setText("数量：" + mData.getAllnum());
            //datasAdapte.setList(mData.getData());
            //adapte1.setList(mData.getCouponlist());
            adapte.setList(mData.getItemlist());
            //pageAdpter.setItemlist(mData.getItemlist());
            if (Integer.parseInt(mData.getSale_id()) > 0) {
                tabViewpager.setVisibility(View.GONE);
                buGoods.setVisibility(View.VISIBLE);
                bgoodsname.setText(mData.getItemlist().get(0).getItemname());
                bgoodsspesc.setText(String.format("数量:%s", mData.getItemlist().get(0).getItemcount()));
                bgoodsprice.setText(String.format("￥%s", mData.getItemlist().get(0).getUnitprice()));
                Glide.with(mContext).load(mData.getItemlist().get(0).getImageUrl()).into(shopimag);
                discountMoney.setVisibility(View.GONE);
                totalPackage.setText("优惠：限量抢购");
            } else {
                tabViewpager.setVisibility(View.VISIBLE);
                initdata();
            }
            ContactDriver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new MaterialDialog.Builder(mContext).title("提示").positiveText("拨打").negativeText("取消")
                            .content(String.format("是否要拨打司机%s电话?", mData.getDriver_info().getName()))
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    //拨打电话
                                    if (Build.VERSION.SDK_INT >= 23) {
                                        //判断有没有拨打电话权限
                                        if (PermissionChecker.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                            //请求拨打电话权限
                                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODEC);

                                        } else {
                                            callPhone();
                                        }

                                    } else {
                                        callPhone();
                                    }
                                }
                            }).show();
                }
            });
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        settabpsi();
        //动态设置空位的高度
        if (Integer.parseInt(mData.getSale_id()) == 0) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    int height = myRecyclerView.computeVerticalScrollExtent();
                    int itemheight = myRecyclerView.getLayoutManager().getChildAt(0).getHeight();
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                            , height - itemheight);
                    View view = new View(mContext);
                    view.setLayoutParams(layoutParams);
                    adapte.setFoootView(view);
                }
            }, 300);
        }
    }

    int position = 0;
    String cid = "";

    private void settabpsi() {
        for (int i = 0; i < mData.getItemlist().size(); i++) {
            OrderDatesBean.ItemlistBean bean = mData.getItemlist().get(i);
            if (i == 0) {
                bean.setIndex(position);
                bean.setClassname(mData.getData().get(position).getName());
                mData.getData().get(position).setPosition(i);
                cid = bean.getCid();
            } else {
                if (cid.equals(bean.getCid())) {
                    bean.setIndex(position);
                    bean.setClassname(mData.getData().get(position).getName());
                } else {
                    position++;
                    bean.setIndex(position);
                    bean.setClassname(mData.getData().get(position).getName());
                    cid = bean.getCid();
                    mData.getData().get(position).setPosition(i);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODEC && PermissionChecker.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            showToastShort("授权成功");
            callPhone();
        } else {
            showToastShort("授权失败");
        }
    }

    private void callPhone() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mData.getDriver_info().getTel()));
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


    public void initdata() {
        for (OrderDatesBean.DataBean bean : mData.getData()) {
            names.add(bean.getName());
            types.add(bean.getId());
        }
        setTab();
//        pageAdpter.setNames(names, types);
    }

    private void setTab() {
        for (OrderDatesBean.DataBean bean : mData.getData()) {
            tabLayout.addTab(tabLayout.newTab().setText(bean.getName()));
        }
        setTablayoutclick();
    }

    private void setTablayoutclick() {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab == null) return;
            //这里使用到反射，拿到Tab对象后获取Class
            Class c = tab.getClass();
            try {
                //Filed “字段、属性”的意思,c.getDeclaredField 获取私有属性。
                //"mView"是Tab的私有属性名称(可查看TabLayout源码),类型是 TabView,TabLayout私有内部类。
                Field field = c.getDeclaredField("mView");
                //值为 true 则指示反射的对象在使用时应该取消 Java 语言访问检查。值为 false 则指示反射的对象应该实施 Java 语言访问检查。
                //如果不这样会报如下错误
                // java.lang.IllegalAccessException:
                //Class com.test.accessible.Main
                //can not access
                //a member of class com.test.accessible.AccessibleTest
                //with modifiers "private"
                field.setAccessible(true);
                final View view = (View) field.get(tab);
                if (view == null) return;
                view.setTag(i);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = (int) view.getTag();
                        //这里就可以根据业务需求处理点击事件了。
                        currItem = position;
                        ((LinearLayoutManager) myRecyclerView.getLayoutManager()).scrollToPositionWithOffset(mData.getData().get(position).getPosition(), 0);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
