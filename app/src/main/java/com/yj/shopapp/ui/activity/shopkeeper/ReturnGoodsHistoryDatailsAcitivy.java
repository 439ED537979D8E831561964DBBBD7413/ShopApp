package com.yj.shopapp.ui.activity.shopkeeper;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextThemeWrapper;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.squareup.okhttp.Request;
import com.yj.shopapp.R;
import com.yj.shopapp.config.Contants;
import com.yj.shopapp.http.HttpHelper;
import com.yj.shopapp.http.OkHttpResponseHandler;
import com.yj.shopapp.ubeen.ReturnGoodsDatails;
import com.yj.shopapp.ui.activity.ShowLog;
import com.yj.shopapp.ui.activity.adapter.ShopHistoryAdpter;
import com.yj.shopapp.ui.activity.base.BaseActivity;
import com.yj.shopapp.util.DDecoration;
import com.yj.shopapp.util.JsonHelper;
import com.yj.shopapp.util.StatusBarUtils;
import com.yj.shopapp.view.YearPickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class ReturnGoodsHistoryDatailsAcitivy extends BaseActivity {

    @BindView(R.id.forewadImg)
    ImageView forewadImg;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.id_right_btu)
    TextView idRightBtu;
    @BindView(R.id.title_view)
    RelativeLayout titleView;
    @BindView(R.id.shopimag)
    ImageView shopimag;
    @BindView(R.id.goods_barcode)
    TextView goodsBarcode;
    @BindView(R.id.shopspec)
    TextView shopspec;
    @BindView(R.id.shop_stock_num)
    TextView shopStockNum;
    @BindView(R.id.shop_stock_money)
    TextView shopStockMoney;
    @BindView(R.id.returngoods_num)
    TextView returngoodsNum;
    @BindView(R.id.returngoods_money)
    TextView returngoodsMoney;
    @BindView(R.id.my_RecyclerView)
    RecyclerView myRecyclerView;
    @BindView(R.id.time_tv)
    TextView timeTv;
    @BindView(R.id.all_purchase_num)
    TextView allPurchaseNum;
    @BindView(R.id.all_returngoods_num)
    TextView allReturngoodsNum;
    @BindView(R.id.timeSelectView)
    ImageView timeSelectView;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    private int CurrentPage = 1;
    private String mMonth;
    private String shopName;
    private String itemid;
    private ReturnGoodsDatails datails;
    private ShopHistoryAdpter adpter;
    private int mYear, Month, mDay;
    private int cYear, cMonth, cDay;
    private List<ReturnGoodsDatails.ListBean> listBeans = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_return_goods_history_datails;
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
        if (getIntent().hasExtra("itemid")) {
            itemid = getIntent().getStringExtra("itemid");
        }
        if (getIntent().hasExtra("shopname")) {
            shopName = getIntent().getStringExtra("shopname");
        }
        title.setText(shopName);
        adpter = new ShopHistoryAdpter(mContext);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        myRecyclerView.addItemDecoration(new DDecoration(mContext, getResources().getDrawable(R.drawable.recyviewdecoration3)));
        myRecyclerView.setAdapter(adpter);
        if (isNetWork(mContext)) {
            getReturnGoodsDatails();
        }
        Refresh();
        Calendar dateAndTime = Calendar.getInstance(Locale.CHINA);
        mYear = dateAndTime.get(Calendar.YEAR);
        Month = dateAndTime.get(Calendar.MONTH);
        mDay = dateAndTime.get(Calendar.DAY_OF_MONTH);
        cYear = dateAndTime.get(Calendar.YEAR);
        cMonth = dateAndTime.get(Calendar.MONTH);
        cDay = dateAndTime.get(Calendar.YEAR);
        timeTv.setText("全部");
    }

    private void Refresh() {
        smartRefreshLayout.setHeaderHeight(50);
        smartRefreshLayout.setFooterHeight(50);
        smartRefreshLayout.setEnableRefresh(false);
        //smartRefreshLayout.setOnRefreshListener(this);
        smartRefreshLayout.setOnLoadMoreListener(v -> {
            CurrentPage++;
            getReturnGoodsDatails();
        });
        smartRefreshLayout.setDisableContentWhenRefresh(true);//是否在刷新的时候禁止列表的操作
        smartRefreshLayout.setDisableContentWhenLoading(true);//是否在加载的时候禁止列表的操作
    }

    private void getReturnGoodsDatails() {
        Map<String, String> params = new HashMap<>();
        params.put("uid", uid);
        params.put("token", token);
        params.put("itemid", itemid);
        params.put("p", CurrentPage + "");
        params.put("month", mMonth);
        HttpHelper.getInstance().post(mContext, Contants.PortU.SHOWRETURNHISTORY, params, new OkHttpResponseHandler<String>(mContext) {
            @Override
            public void onAfter() {
                super.onAfter();
                if (smartRefreshLayout != null) {
                    smartRefreshLayout.finishLoadMore();
                }
            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
                if (smartRefreshLayout != null) {
                    smartRefreshLayout.finishLoadMore();
                }
                CurrentPage--;
            }

            @Override
            public void onResponse(Request request, String response) {
                super.onResponse(request, response);
                ShowLog.e(response);
                if (JsonHelper.isRequstOK(response, mContext)) {
                    datails = JSONObject.parseObject(response, ReturnGoodsDatails.class);
                    setData();
                }
            }
        });
    }

    private void setData() {
        ReturnGoodsDatails.InfoBean bean = datails.getInfo();
        Glide.with(mContext).load(bean.getImgurl()).into(shopimag);
        allPurchaseNum.setText(String.format("进货：%s%s", bean.getStock_num(), bean.getUnit()));
        allReturngoodsNum.setText(String.format("退货：%s%s", bean.getNum(), bean.getUnit()));
        goodsBarcode.setText(String.format("条码：%s", bean.getItemnumber()));
        shopspec.setText(String.format("规格：%s", bean.getSpecs()));
        shopStockNum.setText(String.format("已购买：%s", bean.getStock_num()));
        shopStockMoney.setText(String.format("进货金额：%s", bean.getSock_money()));
        returngoodsNum.setText(String.format("已退货：%s", bean.getNum()));
        returngoodsMoney.setText(String.format("退货金额：%s", bean.getMoney()));
        listBeans.addAll(datails.getList());
        adpter.setList(listBeans);
    }

    @OnClick(R.id.timeSelectView)
    public void onViewClicked() {
        Context themed = new ContextThemeWrapper(mContext,
                android.R.style.Theme_Holo_Light_Dialog);
        YearPickerDialog datePicker = new YearPickerDialog(themed, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mMonth = String.format("%1$d-%2$02d", year, month + 1);
                timeTv.setText(String.format("%1$d-%2$02d", year, month + 1));
                CurrentPage = 1;
                listBeans.clear();
                getReturnGoodsDatails();
            }
        }
                , mYear, Month, mDay); //上下文，点击回调,Calendar年月日
        if (!datePicker.isHasNoDay()) {
            datePicker.setHasNoDay(true);
        }
        datePicker.getDatePicker().init(cYear, cMonth, cDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                cYear = year;
                cMonth = monthOfYear;
                cDay = dayOfMonth;
            }
        });
        datePicker.getDatePicker().setMaxDate((new Date()).getTime());
        datePicker.show();
    }

}
