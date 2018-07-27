package com.yj.shopapp.ui.activity.shopkeeper;

import android.Manifest;
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
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.WOrderFragmentAdapte;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.DateUtils;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.StatusBarUtil;
import com.yj.shopapp.wbeen.WOrderNewDetails;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by jm on 2016/5/23.
 * 供应商
 */
public class SOrderDetailActivity extends BaseActivity {


    @BindView(R.id.content_tv)
    TextView contentTv;
    @BindView(R.id.right_tv)
    TextView rightTv;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.oidTv)
    TextView oidTv;
    @BindView(R.id.order_status)
    TextView orderStatus;
    @BindView(R.id.usernameTv)
    TextView usernameTv;
    @BindView(R.id.order_total)
    TextView orderTotal;
    @BindView(R.id.moneyTv)
    TextView moneyTv;
    @BindView(R.id.oedertimeTv)
    TextView oedertimeTv;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.driver)
    TextView driver;
    @BindView(R.id.Contact_driver)
    TextView ContactDriver;
    @BindView(R.id.driver_info)
    RelativeLayout driverInfo;

    private final int REQUEST_CODEC = 0x1001;
    private static final int REQUEST_CODE = 1;
    @BindView(R.id.tab_viewpager)
    LinearLayout tabViewpager;
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
    private boolean isRequesting = false;//标记，是否正在刷新
    private WOrderFragmentAdapte fragmentAdapte;
    private int mCurrentPage = 0;
    String oid;
    int isType = 0;  // 0 批发商 1 零售商
    String url;
    private WOrderNewDetails snewDetails;
    //List<OrderDetails> notes = new ArrayList<>();
    private String mCid = "";
    private int position;

    @Override
    protected int getLayoutId() {
        return R.layout.sactivity_orderdetail;
    }

    @Override
    protected void initData() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener((v) -> finish());
        contentTv.setText("订单详情");
        if (getIntent().hasExtra("oid")) {
            oid = getIntent().getStringExtra("oid");
        }
        if (getIntent().hasExtra("isType")) {
            isType = getIntent().getIntExtra("isType", 0);
        }
        if (isType == 0) {
            url = Contants.PortA.Details;
            usernameTv.setVisibility(View.VISIBLE);
        } else {
            url = Contants.PortU.OrderDetails;
            usernameTv.setVisibility(View.GONE);
        }
        if (isNetWork(mContext)) {
            refreshRequest();
        }
        fragmentAdapte = new WOrderFragmentAdapte(mContext);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new DDecoration(mContext, getResources().getDrawable(R.drawable.recyviewdecoration1dp)));
        recyclerView.setAdapter(fragmentAdapte);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    if (firstItemPosition < snewDetails.getItemlist().size()) {
                        tabLayout.setFocusable(true);
                        tabLayout.setFocusableInTouchMode(true);
                        if (!tabLayout.getTabAt(snewDetails.getItemlist().get(firstItemPosition).getPosition()).isSelected()) {
                            tabLayout.getTabAt(snewDetails.getItemlist().get(firstItemPosition).getPosition()).select();
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.color_4c4c4c), 0);
    }

    public void refreshRequest() {
        mCurrentPage = 1;
        Map<String, String> params = new HashMap<String, String>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("oid", oid);
        params.put("p", String.valueOf(mCurrentPage));
        HttpHelper.getInstance().post(mContext, url, params, new OkHttpResponseHandler<String>(mContext) {

            @Override
            public void onAfter() {
                super.onAfter();
                isRequesting = false;
            }

            @Override
            public void onBefore() {
                super.onBefore();
                isRequesting = true;
            }

            @Override
            public void onResponse(Request request, String json) {
                super.onResponse(request, json);
                //notes.clear();
                ShowLog.e(json);
                if (JsonHelper.isRequstOK(json, mContext)) {
                    snewDetails = JSONObject.parseObject(json, WOrderNewDetails.class);
                    if (isFinishing()) return;
                    setData();
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                showToastShort(Contants.NetStatus.NETDISABLEORNETWORKDISABLE);
                // notes.clear();
            }
        });

    }

    private void setData() {
        if (null != snewDetails.getDriver_info()) {
            driverInfo.setVisibility(View.VISIBLE);
            driver.setText(String.format("司机：%s", snewDetails.getDriver_info().getName()));
        }
        oidTv.setText(String.format("订单号：%s", snewDetails.getOid()));
        orderStatus.setText(Contants.OrderStadus[Integer.parseInt(snewDetails.getStatus())]);
        usernameTv.setText(snewDetails.getAddress().getShopname());
        orderTotal.setText(String.format("总件数：%s件", String.valueOf(snewDetails.getAllnum())));
        moneyTv.setText(Html.fromHtml("金额：" + "<font color=#FE3000>" + "￥" + snewDetails.getMoney() + "</font>"));
        oedertimeTv.setText(String.format("下单时间：%s", DateUtils.timet(snewDetails.getAddtime())));
        setTabLayout();
        fragmentAdapte.setList(snewDetails.getItemlist());

        //动态设置空位高度
        if (Integer.parseInt(snewDetails.getSale_id()) == 0) {
            new Handler().postDelayed(() -> {
                try {
                    if (recyclerView != null && null != recyclerView.getLayoutManager()) {
                        int itemHeight = recyclerView.getLayoutManager().getChildAt(0).getHeight();
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                                , recyclerView.getHeight() - itemHeight);
                        View view = new View(mContext);
                        view.setLayoutParams(layoutParams);
                        fragmentAdapte.setFoootView(view);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, 300);
        } else {
            WOrderNewDetails.ItemlistBean bean = snewDetails.getItemlist().get(0);
            tabViewpager.setVisibility(View.GONE);
            buGoods.setVisibility(View.VISIBLE);
            bgoodsname.setText(bean.getItemname());
            ShowLog.e(bean.getUnit());
            bgoodsspesc.setText(String.format("数量:%1$s%2$s", bean.getItemcount(), bean.getUnit()));
            bgoodsprice.setText(String.format("￥%s", bean.getUnitprice()));
            Glide.with(mContext).load(bean.getImageUrl()).into(shopimag);
        }
    }

    private void setTabLayout() {
        if (snewDetails.getItemlist() == null) return;
        for (int i = 0; i < snewDetails.getItemlist().size(); i++) {
            WOrderNewDetails.ItemlistBean bean = snewDetails.getItemlist().get(i);
            if (i == 0) {
                bean.setPosition(position);
                snewDetails.getData().get(position).setPosition(i);
                mCid = bean.getCid();
            } else {
                if (mCid.equals(bean.getCid())) {
                    bean.setPosition(position);
                } else {
                    position++;
                    bean.setPosition(position);
                    mCid = bean.getCid();
                    snewDetails.getData().get(position).setPosition(i);
                }
            }
        }
        for (WOrderNewDetails.DataBean bean : snewDetails.getData()) {
            tabLayout.addTab(tabLayout.newTab().setText(bean.getName()));
        }
        setTablayoutclick();
    }

    @OnClick({R.id.usernameTv, R.id.Contact_driver})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.usernameTv:
                WOrderNewDetails.Address address = snewDetails.getAddress();
                new MaterialDialog.Builder(mContext).title("客户信息").positiveText("确认").items(Arrays.asList(String.format("店名：%s", address.getShopname())
                        , String.format("联系人：%s", address.getContacts())
                        , String.format("联系电话：%s", address.getMobile())
                        , String.format("店铺地址：%s", address.getAddress()))).show();
                break;
            case R.id.Contact_driver:
                new MaterialDialog.Builder(mContext).title("提示").positiveText("拨打").negativeText("取消")
                        .content(String.format("是否要拨打司机%s电话?", snewDetails.getDriver_info().getName()))
                        .onPositive((dialog, which) -> {
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
                        }).show();
                break;
        }

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
                view.setOnClickListener(v -> {
                    int position = (int) v.getTag();
                    //这里就可以根据业务需求处理点击事件了。
                    ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(snewDetails.getData().get(position).getPosition(), 0);
                });
            } catch (Exception e) {
                e.printStackTrace();
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
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + snewDetails.getDriver_info().getTel()));
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
